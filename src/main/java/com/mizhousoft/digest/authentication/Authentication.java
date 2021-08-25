package com.mizhousoft.digest.authentication;

import java.io.Serializable;
import java.security.Principal;

/**
 * 认证信息
 *
 * @version
 */
public interface Authentication extends Principal, Serializable
{
	/**
	 * 获取用户账号
	 * 
	 * @return
	 */
	UserDetails getPrincipal();

	/**
	 * 获取访问的IP地址
	 * 
	 * @return
	 */
	String getHost();

	/**
	 * 获取详情
	 * 
	 * @return
	 */
	Object getDetails();
}
