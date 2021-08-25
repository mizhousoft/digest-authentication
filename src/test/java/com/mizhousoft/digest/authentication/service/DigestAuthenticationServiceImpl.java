package com.mizhousoft.digest.authentication.service;

import org.springframework.stereotype.Service;

import com.mizhousoft.digest.authentication.DigestAuthenticationService;
import com.mizhousoft.digest.authentication.UserDetails;
import com.mizhousoft.digest.authentication.UserImpl;
import com.mizhousoft.digest.authentication.exception.UsernameNotFoundException;

/**
 * 摘要认证服务
 *
 * @version
 */
@Service
public class DigestAuthenticationServiceImpl implements DigestAuthenticationService
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnable()
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		UserImpl user = new UserImpl(username, "test", true, true, true, true, null);

		return user;
	}
}
