package com.mizhousoft.digest.authentication.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.mizhousoft.commons.lang.HexUtils;

/**
 * 摘要认证工具
 *
 * @version
 */

public final class DigestAuthUtils
{
	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static String encodePasswordInA1Format(String username, String realm, String password)
	{
		String a1 = username + ":" + realm + ":" + password;

		return md5Hex(a1);
	}

	public static String[] splitIgnoringQuotes(String str, char separatorChar)
	{
		if (str == null)
		{
			return null;
		}

		int len = str.length();

		if (len == 0)
		{
			return EMPTY_STRING_ARRAY;
		}

		List<String> list = new ArrayList<>();
		int i = 0;
		int start = 0;
		boolean match = false;

		while (i < len)
		{
			if (str.charAt(i) == '"')
			{
				i++;
				while (i < len)
				{
					if (str.charAt(i) == '"')
					{
						i++;
						break;
					}
					i++;
				}
				match = true;
				continue;
			}
			if (str.charAt(i) == separatorChar)
			{
				if (match)
				{
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match)
		{
			list.add(str.substring(start, i));
		}

		return list.toArray(new String[0]);
	}

	public static String generateDigest(boolean passwordAlreadyEncoded, String username, String realm, String password, String httpMethod,
	        String uri, String qop, String nonce, String nc, String cnonce) throws IllegalArgumentException
	{
		String a1Md5;
		String a2 = httpMethod + ":" + uri;
		String a2Md5 = md5Hex(a2);

		if (passwordAlreadyEncoded)
		{
			a1Md5 = password;
		}
		else
		{
			a1Md5 = DigestAuthUtils.encodePasswordInA1Format(username, realm, password);
		}

		String digest;

		if (qop == null)
		{
			// as per RFC 2069 compliant clients (also reaffirmed by RFC 2617)
			digest = a1Md5 + ":" + nonce + ":" + a2Md5;
		}
		else if ("auth".equals(qop))
		{
			// As per RFC 2617 compliant clients
			digest = a1Md5 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + a2Md5;
		}
		else
		{
			throw new IllegalArgumentException("This method does not support a qop: '" + qop + "'");
		}

		return md5Hex(digest);
	}

	public static Map<String, String> splitEachArrayElementAndCreateMap(String[] array, String delimiter, String removeCharacters)
	{
		if ((array == null) || (array.length == 0))
		{
			return null;
		}

		Map<String, String> map = new HashMap<>();

		for (String s : array)
		{
			String postRemove;

			if (removeCharacters == null)
			{
				postRemove = s;
			}
			else
			{
				postRemove = StringUtils.replace(s, removeCharacters, "");
			}

			String[] splitThisArrayElement = split(postRemove, delimiter);

			if (splitThisArrayElement == null)
			{
				continue;
			}

			map.put(splitThisArrayElement[0].trim(), splitThisArrayElement[1].trim());
		}

		return map;
	}

	public static String[] split(String toSplit, String delimiter)
	{
		if (StringUtils.isBlank(toSplit))
		{
			throw new IllegalArgumentException("Cannot split a null or empty string");
		}

		if (StringUtils.isBlank(delimiter))
		{
			throw new IllegalArgumentException("Cannot use a null or empty delimiter to split a string");
		}

		if (delimiter.length() != 1)
		{
			throw new IllegalArgumentException("Delimiter can only be one character in length");
		}

		int offset = toSplit.indexOf(delimiter);

		if (offset < 0)
		{
			return null;
		}

		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + 1);

		return new String[] { beforeDelimiter, afterDelimiter };
	}

	public static String md5Hex(String data)
	{
		MessageDigest digest;
		try
		{
			digest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new IllegalStateException("No MD5 algorithm available!");
		}

		return HexUtils.encodeHexString(digest.digest(data.getBytes()), true);
	}
}