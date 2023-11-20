package com.mizhousoft.digest.authentication;

import java.io.IOException;

import com.mizhousoft.digest.authentication.exception.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
