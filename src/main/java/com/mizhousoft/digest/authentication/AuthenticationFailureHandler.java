package com.mizhousoft.digest.authentication;

import java.io.IOException;

import com.mizhousoft.digest.authentication.exception.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
