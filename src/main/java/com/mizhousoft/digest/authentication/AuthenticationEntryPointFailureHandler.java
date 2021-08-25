package com.mizhousoft.digest.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mizhousoft.digest.authentication.exception.AuthenticationException;

/**
 * 认证失败处理器
 *
 * @version
 */
public class AuthenticationEntryPointFailureHandler implements AuthenticationFailureHandler
{
	private final AuthenticationEntryPoint authenticationEntryPoint;

	/**
	 * 构造函数
	 *
	 * @param authenticationEntryPoint
	 */
	public AuthenticationEntryPointFailureHandler(AuthenticationEntryPoint authenticationEntryPoint)
	{
		super();
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
	        throws IOException, ServletException
	{
		this.authenticationEntryPoint.commence(request, response, exception);
	}
}
