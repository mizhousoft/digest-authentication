package com.mizhousoft.digest.authentication;

import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.digest.authentication.exception.BadCredentialsException;
import com.mizhousoft.digest.authentication.util.DigestAuthUtils;

/**
 * 摘要数据
 *
 * @version
 */
public final class DigestData
{
	private static final Logger LOG = LoggerFactory.getLogger(DigestData.class);

	// 用户名
	private final String username;

	// 安全域
	private final String realm;

	// 服务端向客户端发送质询时附带的一个随机数
	private final String nonce;

	// 请求URL
	private final String uri;

	// 计算出的一个字符串，以证明用户知道口令
	private final String response;

	// 保护质量，包含auth（默认的）和auth-int（增加了报文完整性检测）两种策略
	private final String qop;

	// nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量
	private final String nc;

	// 客户端随机数
	private final String cnonce;

	// Digest
	private final String section212response;

	// 服务端向客户端发送质询时附带的一个随机数过期时间
	private long nonceExpiryTime;

	public DigestData(String header)
	{
		this.section212response = header.substring(7);
		String[] headerEntries = DigestAuthUtils.splitIgnoringQuotes(this.section212response, ',');
		Map<String, String> headerMap = DigestAuthUtils.splitEachArrayElementAndCreateMap(headerEntries, "=", "\"");

		this.username = headerMap.get("username");
		this.realm = headerMap.get("realm");
		this.nonce = headerMap.get("nonce");
		this.uri = headerMap.get("uri");
		this.response = headerMap.get("response");
		this.qop = headerMap.get("qop"); // RFC 2617 extension
		this.nc = headerMap.get("nc"); // RFC 2617 extension
		this.cnonce = headerMap.get("cnonce"); // RFC 2617 extension

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Extracted username: '" + this.username + "'; realm: '" + this.realm + "'; nonce: '" + this.nonce + "'; uri: '"
			        + this.uri + "'; response: '" + this.response + "'");
		}
	}

	/**
	 * 验证并转码
	 * 
	 * @param entryPointKey
	 * @param expectedRealm
	 * @throws BadCredentialsException
	 */
	public void validateAndDecode(String entryPointKey, String expectedRealm) throws BadCredentialsException
	{
		// Check all required parameters were supplied (ie RFC 2069)
		if ((this.username == null) || (this.realm == null) || (this.nonce == null) || (this.uri == null) || (this.response == null))
		{
			throw new BadCredentialsException("Missing mandatory digest value; received header {0}");
		}
		// Check all required parameters for an "auth" qop were supplied (ie RFC 2617)
		if ("auth".equals(this.qop))
		{
			if ((this.nc == null) || (this.cnonce == null))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("extracted nc: '" + this.nc + "'; cnonce: '" + this.cnonce + "'");
				}

				throw new BadCredentialsException("Missing mandatory digest value; received header {0}");
			}
		}

		// Check realm name equals what we expected
		if (!expectedRealm.equals(this.realm))
		{
			throw new BadCredentialsException("Response realm name '{0}' does not match system realm name of '{1}'");
		}

		// Check nonce was Base64 encoded (as sent by DigestAuthenticationEntryPoint)
		try
		{
			Base64.getDecoder().decode(this.nonce.getBytes());
		}
		catch (IllegalArgumentException e)
		{
			throw new BadCredentialsException("Nonce is not encoded in Base64; received nonce {0}");
		}

		// Decode nonce from Base64
		// format of nonce is:
		// base64(expirationTime + ":" + md5Hex(expirationTime + ":" + key))
		String nonceAsPlainText = new String(Base64.getDecoder().decode(this.nonce.getBytes()));
		String[] nonceTokens = StringUtils.split(nonceAsPlainText, ":");

		if (nonceTokens.length != 2)
		{
			throw new BadCredentialsException("Nonce should have yielded two tokens but was {0}");
		}

		// Extract expiry time from nonce

		try
		{
			this.nonceExpiryTime = new Long(nonceTokens[0]);
		}
		catch (NumberFormatException nfe)
		{
			throw new BadCredentialsException("Nonce token should have yielded a numeric first token, but was {0}");
		}

		// Check signature of nonce matches this expiry time
		String expectedNonceSignature = DigestAuthUtils.md5Hex(this.nonceExpiryTime + ":" + entryPointKey);

		if (!expectedNonceSignature.equals(nonceTokens[1]))
		{
			throw new BadCredentialsException("Nonce token compromised {0}");
		}
	}

	/**
	 * 计算摘要
	 * 
	 * @param password
	 * @param httpMethod
	 * @return
	 */
	public String calculateServerDigest(String password, String httpMethod)
	{
		// Compute the expected response-digest (will be in hex form)

		// Don't catch IllegalArgumentException (already checked validity)
		return DigestAuthUtils.generateDigest(false, this.username, this.realm, password, httpMethod, this.uri, this.qop, this.nonce,
		        this.nc, this.cnonce);
	}

	public boolean isNonceExpired()
	{
		long now = System.currentTimeMillis();
		return this.nonceExpiryTime < now;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getResponse()
	{
		return this.response;
	}

	public String getNonce()
	{
		return nonce;
	}
}
