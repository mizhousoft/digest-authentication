package com.mizhousoft.digest.authentication.www;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mizhousoft.commons.web.util.WebUtils;
import com.mizhousoft.digest.authentication.DigestAuthenticationService;
import com.mizhousoft.digest.authentication.DigestData;
import com.mizhousoft.digest.authentication.SecurityConstants;
import com.mizhousoft.digest.authentication.UserDetails;
import com.mizhousoft.digest.authentication.exception.AuthenticationException;
import com.mizhousoft.digest.authentication.exception.BadCredentialsException;
import com.mizhousoft.digest.authentication.exception.NonceExpiredException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 摘要认证过滤器
 *
 * @version
 */
public class DigestAuthenticationFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(DigestAuthenticationFilter.class);

	private DigestAuthenticationEntryPoint authenticationEntryPoint;

	private DigestAuthenticationService digestAuthenticationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws ServletException, IOException
	{
		if (!digestAuthenticationService.isEnable())
		{
			chain.doFilter(request, response);

			return;
		}

		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Digest "))
		{
			chain.doFilter(request, response);

			return;
		}

		LOG.debug("Digest Authorization header received from user agent: " + header);

		DigestData digestAuth = new DigestData(header);

		try
		{
			digestAuth.validateAndDecode(this.authenticationEntryPoint.getKey(), this.authenticationEntryPoint.getRealmName());
		}
		catch (BadCredentialsException e)
		{
			fail(request, response, e);

			return;
		}

		// Lookup password for presented username
		// NB: DAO-provided password MUST be clear text - not encoded/salted
		// (unless this instance's passwordAlreadyEncoded property is 'false')
		UserDetails user = null;
		String serverDigestMd5 = null;

		try
		{
			user = this.digestAuthenticationService.loadUserByUsername(digestAuth.getUsername());
			if (user == null)
			{
				throw new AuthenticationException("AuthenticationDao returned null, which is an interface contract violation");
			}

			serverDigestMd5 = digestAuth.calculateServerDigest(user.getPassword(), request.getMethod());
		}
		catch (AuthenticationException notFound)
		{
			fail(request, response, new BadCredentialsException("Username " + digestAuth.getUsername() + " not found"));

			return;
		}

		// If digest is still incorrect, definitely reject authentication attempt
		if (!serverDigestMd5.equals(digestAuth.getResponse()))
		{
			fail(request, response, new BadCredentialsException("Incorrect response"));
			return;
		}

		// To get this far, the digest must have been valid
		// Check the nonce has not expired
		// We do this last so we can direct the user agent its nonce is stale
		// but the request was otherwise appearing to be valid
		if (digestAuth.isNonceExpired())
		{
			fail(request, response, new NonceExpiredException("Nonce has expired/timed out"));

			return;
		}

		String host = WebUtils.getFirstRemoteIPAddress(request);
		LOG.info("Authentication success for user: {}, host is {}.", digestAuth.getUsername(), host);

		success(request, response, user);

		chain.doFilter(request, response);
	}

	private void success(HttpServletRequest request, HttpServletResponse response, UserDetails user)
	{
		HttpSession session = request.getSession();
		session.setAttribute(SecurityConstants.DIGEST_AUTH_SESSION_KEY, user);
	}

	private void fail(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
	        throws IOException, ServletException
	{
		String host = WebUtils.getFirstRemoteIPAddress(request);
		LOG.error("Authentication fail, host is {}.", host, failed);

		this.authenticationEntryPoint.commence(request, response, failed);
	}

	/**
	 * 设置authenticationEntryPoint
	 * 
	 * @param authenticationEntryPoint
	 */
	public void setAuthenticationEntryPoint(DigestAuthenticationEntryPoint authenticationEntryPoint)
	{
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	/**
	 * 设置digestAuthenticationService
	 * 
	 * @param digestAuthenticationService
	 */
	public void setDigestAuthenticationService(DigestAuthenticationService digestAuthenticationService)
	{
		this.digestAuthenticationService = digestAuthenticationService;
	}
}
