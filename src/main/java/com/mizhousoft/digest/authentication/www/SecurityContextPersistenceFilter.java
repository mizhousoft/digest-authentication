package com.mizhousoft.digest.authentication.www;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mizhousoft.commons.web.util.WebUtils;
import com.mizhousoft.digest.authentication.DigestAuthenticationService;
import com.mizhousoft.digest.authentication.SecurityConstants;
import com.mizhousoft.digest.authentication.UserDetails;
import com.mizhousoft.digest.authentication.UserPasswordAuthentication;
import com.mizhousoft.digest.authentication.context.SecurityContext;
import com.mizhousoft.digest.authentication.context.SecurityContextHolder;
import com.mizhousoft.digest.authentication.context.SecurityContextImpl;
import com.mizhousoft.digest.authentication.exception.BadCredentialsException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * SecurityContext持久化过滤器
 *
 * @version
 */
public class SecurityContextPersistenceFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(SecurityContextPersistenceFilter.class);

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

		Object object = request.getSession().getAttribute(SecurityConstants.DIGEST_AUTH_SESSION_KEY);
		if (null == object || !(object instanceof UserDetails))
		{
			String host = WebUtils.getFirstRemoteIPAddress(request);
			LOG.error("Digest auth session not found, host is {}.", host);

			authenticationEntryPoint.commence(request, response, new BadCredentialsException("Digest auth session not found."));
			return;
		}

		UserDetails userDetails = (UserDetails) object;

		try
		{
			SecurityContext securityContext = buildSecurityContext(userDetails, request);
			SecurityContextHolder.setContext(securityContext);

			chain.doFilter(request, response);
		}
		finally
		{
			SecurityContextHolder.clearContext();
		}
	}

	private SecurityContext buildSecurityContext(UserDetails userDetails, HttpServletRequest request)
	{
		String host = com.mizhousoft.commons.web.util.WebUtils.getRemoteIPAddress(request);

		UserPasswordAuthentication authentication = new UserPasswordAuthentication(userDetails);
		authentication.setHost(host);

		SecurityContextImpl securityContext = new SecurityContextImpl();
		securityContext.setAuthentication(authentication);

		return securityContext;
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
