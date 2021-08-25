package com.mizhousoft.digest.authentication.exception;

/**
 * 用户名未找到异常
 *
 * @version
 */
public class UsernameNotFoundException extends AuthenticationException
{
	private static final long serialVersionUID = 6806856298452917768L;

	/**
	 * 构造函数
	 *
	 * @param errorCode
	 * @param message
	 * @param throwable
	 */
	public UsernameNotFoundException(String errorCode, String message, Throwable throwable)
	{
		super(errorCode, message, throwable);
	}

	/**
	 * 构造函数
	 *
	 * @param errorCode
	 * @param message
	 */
	public UsernameNotFoundException(String errorCode, String message)
	{
		super(errorCode, message);
	}

	/**
	 * 构造函数
	 *
	 * @param errorCode
	 * @param codeParams
	 * @param message
	 * @param throwable
	 */
	public UsernameNotFoundException(String errorCode, String[] codeParams, String message, Throwable throwable)
	{
		super(errorCode, codeParams, message, throwable);
	}

	/**
	 * 构造函数
	 *
	 * @param errorCode
	 * @param codeParams
	 * @param message
	 */
	public UsernameNotFoundException(String errorCode, String[] codeParams, String message)
	{
		super(errorCode, codeParams, message);
	}

	/**
	 * 构造函数
	 *
	 * @param message
	 * @param throwable
	 */
	public UsernameNotFoundException(String message, Throwable throwable)
	{
		super(message, throwable);
	}

	/**
	 * 构造函数
	 *
	 * @param message
	 */
	public UsernameNotFoundException(String message)
	{
		super(message);
	}

}
