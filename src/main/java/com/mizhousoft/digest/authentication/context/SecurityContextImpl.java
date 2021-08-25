package com.mizhousoft.digest.authentication.context;

import com.mizhousoft.digest.authentication.Authentication;

/**
 * 安全上下文
 *
 * @version
 */
public class SecurityContextImpl implements SecurityContext
{
	private static final long serialVersionUID = -2284767993912383847L;

	// 认证信息
	private Authentication authentication;

	/**
	 * 获取认证信息
	 * 
	 * @return
	 */
	@Override
	public Authentication getAuthentication()
	{
		return authentication;
	}

	/**
	 * 设置authentication
	 * 
	 * @param authentication
	 */
	public void setAuthentication(Authentication authentication)
	{
		this.authentication = authentication;
	}
}
