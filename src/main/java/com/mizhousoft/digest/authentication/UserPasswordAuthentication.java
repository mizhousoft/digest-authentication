package com.mizhousoft.digest.authentication;

/**
 * 账号认证
 *
 * @version
 */
public class UserPasswordAuthentication implements Authentication
{
	private static final long serialVersionUID = -3872720959509253551L;

	// 认证账号
	private final UserDetails principal;

	// 访问IP地址
	private String host;

	/**
	 * 构造函数
	 *
	 * @param principal
	 */
	public UserPasswordAuthentication(UserDetails principal)
	{
		this.principal = principal;
	}

	/**
	 * 获取认证账号
	 * 
	 * @return
	 */
	@Override
	public UserDetails getPrincipal()
	{
		return this.principal;
	}

	/**
	 * 获取host
	 * 
	 * @return
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * 设置host
	 * 
	 * @param host
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * 获取认证账号名字
	 * 
	 * @return
	 */
	@Override
	public String getName()
	{
		return this.principal.getUsername();
	}

	/**
	 * 获取details
	 * 
	 * @return
	 */
	@Override
	public Object getDetails()
	{
		return this.principal.getDetails();
	}
}
