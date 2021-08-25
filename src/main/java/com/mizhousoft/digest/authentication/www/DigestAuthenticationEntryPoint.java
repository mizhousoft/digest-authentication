package com.mizhousoft.digest.authentication.www;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mizhousoft.digest.authentication.AuthenticationEntryPoint;
import com.mizhousoft.digest.authentication.exception.AuthenticationException;
import com.mizhousoft.digest.authentication.exception.NonceExpiredException;
import com.mizhousoft.digest.authentication.util.DigestAuthUtils;

/**
 * 摘要认证入口点
 *
 * @version
 */
public class DigestAuthenticationEntryPoint implements AuthenticationEntryPoint
{
	private static final Logger LOG = LoggerFactory.getLogger(DigestAuthenticationEntryPoint.class);

	// 随机数有效时间
	private int nonceValiditySeconds = 300;

	// 安全密钥
	private String key;

	// 安全域
	private String realmName;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
	        throws IOException, ServletException
	{
		HttpServletResponse httpResponse = response;

		// destroySession(request);

		// compute a nonce (do not use remote IP address due to proxy farms)
		// format of nonce is:
		// base64(expirationTime + ":" + md5Hex(expirationTime + ":" + key))
		long expiryTime = System.currentTimeMillis() + (nonceValiditySeconds * 1000);
		String signatureValue = DigestAuthUtils.md5Hex(expiryTime + ":" + key);
		String nonceValue = expiryTime + ":" + signatureValue;
		String nonceValueBase64 = new String(Base64.getEncoder().encode(nonceValue.getBytes()));

		// qop is quality of protection, as defined by RFC 2617.
		// we do not use opaque due to IE violation of RFC 2617 in not
		// representing opaque on subsequent requests in same session.
		String authenticateHeader = "Digest realm=\"" + realmName + "\", " + "qop=\"auth\", nonce=\"" + nonceValueBase64 + "\"";

		if (authException instanceof NonceExpiredException)
		{
			authenticateHeader = authenticateHeader + ", stale=\"true\"";
		}

		LOG.debug("WWW-Authenticate header sent to user agent: " + authenticateHeader);

		httpResponse.addHeader("WWW-Authenticate", authenticateHeader);
		httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * 销毁session
	 * 
	 * @param request
	 */
	protected void destroySession(HttpServletRequest request)
	{
		try
		{
			request.getSession().invalidate();
		}
		catch (Throwable e)
		{
			LOG.error("Destroy session failed.", e);
		}
	}

	public String getKey()
	{
		return key;
	}

	public int getNonceValiditySeconds()
	{
		return nonceValiditySeconds;
	}

	public String getRealmName()
	{
		return realmName;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public void setNonceValiditySeconds(int nonceValiditySeconds)
	{
		this.nonceValiditySeconds = nonceValiditySeconds;
	}

	public void setRealmName(String realmName)
	{
		this.realmName = realmName;
	}
}
