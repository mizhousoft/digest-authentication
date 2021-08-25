package com.mizhousoft.digest.authentication;

import com.mizhousoft.digest.authentication.exception.UsernameNotFoundException;

/**
 * 摘要认证服务
 *
 * @version
 */
public interface DigestAuthenticationService
{
	/**
	 * 是否启用
	 * 
	 * @return
	 */
	boolean isEnable();

	/**
	 * 根据用户码获取用户详情
	 * 
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
