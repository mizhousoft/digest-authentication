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
public interface AuthenticationFailureHandler
{
	/**
	 * 认证失败
	 * 
	 * @param request
	 * @param response
	 * @param exception
	 * @throws IOException
	 * @throws ServletException
	 */
	void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
	        throws IOException, ServletException;
}
