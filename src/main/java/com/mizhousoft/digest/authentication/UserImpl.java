package com.mizhousoft.digest.authentication;

/**
 * 用户
 *
 * @version
 */
public class UserImpl implements UserDetails
{
	private static final long serialVersionUID = 2437692192994207856L;

	// 密码
	private String password;

	// 用户码
	private final String username;

	// 帐号是否未过期
	private final boolean accountNonExpired;

	// 帐号是否未锁定
	private final boolean accountNonLocked;

	// 凭证是否未过期
	private final boolean credentialsNonExpired;

	// 是否启用
	private final boolean enabled;

	// 详情
	private final Object details;

	public UserImpl(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
	        boolean accountNonLocked, Object details)
	{
		if (((username == null) || "".equals(username)) || (password == null))
		{
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}

		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.details = details;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return credentialsNonExpired;
	}

	/**
	 * 获取details
	 * 
	 * @return
	 */
	public Object getDetails()
	{
		return details;
	}
}
