package com.mizhousoft.digest.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mizhousoft.digest.authentication.exception.AuthenticationException;

/**
 * 认证入口点
 *
 * @version
 */
public interface AuthenticationEntryPoint
{
	/**
	 * 开始处理
	 * 
	 * @param request
	 * @param response
	 * @param authException
	 * @throws IOException
	 * @throws ServletException
	 */
	void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
	        throws IOException, ServletException;
}
