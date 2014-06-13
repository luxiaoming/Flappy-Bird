package com.lxm.tools;

import java.io.UnsupportedEncodingException;

import java.security.SecureRandom;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.DESKeySpec;

// 加密解密工具
public class Secret

{
	public static void main(String[] args)

	throws UnsupportedEncodingException

	{

		// 待加密内容

		String str = "我是中国人";

		// 密码，长度要是8的倍数

		String password = "12345678";

		byte[] result = desCrypto(str.getBytes(), password);

		System.out.println("加密后内容为：" + new String(result));

		// 直接将如上内容解密

		try

		{

			byte[] decryResult = decrypt(result, password);

			System.out.println("加密前内容为：" + new String(decryResult));

		}

		catch (Exception e1)

		{

			e1.printStackTrace();

		}

	}

	/**
	 * 
	 * <对字符串进行Des加密，将字符串转化为字节数组解密>
	 */

	public static byte[] desCrypto(byte[] datasource, String password)

	{

		try

		{

			SecureRandom random = new SecureRandom();

			DESKeySpec desKey = new DESKeySpec(password.getBytes());

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			SecretKey securekey = keyFactory.generateSecret(desKey);

			// Cipher对象实际完成加密操作

			Cipher cipher = Cipher.getInstance("DES");

			// 用密匙初始化Cipher对象

			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);

			// 现在，获取数据并加密

			// 正式执行加密操作

			return cipher.doFinal(datasource);

		}

		catch (Throwable e)

		{

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * <将加密的密文字节数组转化为明文字节数组>
	 */

	public static byte[] decrypt(byte[] src, String password)

	throws Exception

	{

		// DES算法要求有一个可信任的随机数源

		SecureRandom random = new SecureRandom();

		// 创建一个DESKeySpec对象

		DESKeySpec desKey = new DESKeySpec(password.getBytes());

		// 创建一个密匙工厂

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

		// 将DESKeySpec对象转换成SecretKey对象

		SecretKey securekey = keyFactory.generateSecret(desKey);

		// Cipher对象实际完成解密操作

		Cipher cipher = Cipher.getInstance("DES");

		// 用密匙初始化Cipher对象

		cipher.init(Cipher.DECRYPT_MODE, securekey, random);

		// 真正开始解密操作

		return cipher.doFinal(src);

	}

}