package com.mizhousoft.digest.authentication.exception;

/**
 * 随机数过期异常
 *
 * @version
 */
public class NonceExpiredException extends AuthenticationException
{
	private static final long serialVersionUID = -4367555352392605608L;

	/**
	 * 构造函数
	 *
	 * @param errorCode
	 * @param message
	 * @param throwable
	 */
	public NonceExpiredException(String errorCode, String message, Throwable throwable)
	{
		super(errorCode, message, throwable);
	}

	/**
	 * 构造函数
	 *
	 * @param errorCode
	 * @param message
	 */
	public NonceExpiredException(String errorCode, String message)
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
	public NonceExpiredException(String errorCode, String[] codeParams, String message, Throwable throwable)
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
	public NonceExpiredException(String errorCode, String[] codeParams, String message)
	{
		super(errorCode, codeParams, message);
	}

	/**
	 * 构造函数
	 *
	 * @param message
	 * @param throwable
	 */
	public NonceExpiredException(String message, Throwable throwable)
	{
		super(message, throwable);
	}

	/**
	 * 构造函数
	 *
	 * @param message
	 */
	public NonceExpiredException(String message)
	{
		super(message);
	}

}
