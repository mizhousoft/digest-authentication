package com.mizhousoft.digest.authentication;

import java.io.Serializable;

/**
 * 用户详情
 *
 * @version
 */
public interface UserDetails extends Serializable
{
	/**
	 * 获取密码
	 * 
	 * @return
	 */
	String getPassword();

	/**
	 * 获取用户码
	 * 
	 * @return
	 */
	String getUsername();

	/**
	 * 帐号是否没过期
	 * 
	 * @return
	 */
	boolean isAccountNonExpired();

	/**
	 * 帐号是否没锁定
	 * 
	 * @return
	 */
	boolean isAccountNonLocked();

	/**
	 * 凭证是否没过期
	 * 
	 * @return
	 */
	boolean isCredentialsNonExpired();

	/**
	 * 帐号是否启用
	 * 
	 * @return
	 */
	boolean isEnabled();

	/**
	 * 获取详情
	 * 
	 * @return
	 */
	Object getDetails();
}
