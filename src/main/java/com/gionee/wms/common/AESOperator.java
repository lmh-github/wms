package com.gionee.wms.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES ��һ�ֿ�������㷨�����û���������Ϣ���ܴ��� ��ԭʼ��ݽ���AES���ܺ��ڽ���Base64����ת����
 */
public class AESOperator {

	/*
	 * �����õ�Key ������26����ĸ��������� �˴�ʹ��AES-128-CBC����ģʽ��key��ҪΪ16λ��
	 */
	private static String sKey = "1234567887654321";
	private static String ivParameter = "1234567887654321";
	private static AESOperator instance = null;

	private AESOperator() {

	}

	public static AESOperator getInstance() {
		if (instance == null)
			instance = new AESOperator();
		return instance;
	}
	
	/**
     * ����
     * @param content
     * @return
     * @throws Exception 
     */
    public static String EASencrypt(String content) throws Exception{
    	byte[] enResult = encrypt(content);
    	String hexStr = parseByte2HexStr(enResult);
    	String baseStr = Base64.getBase64(hexStr);
    	return baseStr;
    }
    
   
    
    /**
     * ����
     * @param content
     * @return
     * @throws Exception
     */
    public static String EASdecrypt(String content) throws Exception{
    	String enResult = Base64.getFromBase64(content);
    	byte[] bt = parseHexStr2Byte(enResult);
    	byte[] resultBt = decrypt(bt);
    	String result = new String(resultBt, "utf-8");
    	return result;
    }

	// ����
	public static byte[] encrypt(String sSrc) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// ʹ��CBCģʽ����Ҫһ������iv�������Ӽ����㷨��ǿ��
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return encrypted;
	}

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * ��16����ת��Ϊ������
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	// ����
	public static byte[] decrypt(byte[] sSrc) throws Exception {
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(sSrc);
			return original;
		} catch (Exception ex) {
			return null;
		}
	}


	public static String encodeBytes(byte[] bytes) {
		StringBuffer strBuf = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
			strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
		}

		return strBuf.toString();
	}

	public static void main(String[] args) throws Exception {
		// ��Ҫ���ܵ��ִ�
		String cSrc = "123456";

		// ����
		long lStart = System.currentTimeMillis();
		AESOperator.getInstance();
		String enString = AESOperator.EASencrypt(cSrc);
		System.out.println("加密后数据：" + enString);

		long lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("加密用时：" + lUseTime + "毫秒");
		// ����
		lStart = System.currentTimeMillis();
		AESOperator.getInstance();
		String DeString = AESOperator.EASdecrypt("MzEzQ0U3MTVFNzBERDkxMzExM0JEMkNBM0FFNUY2RjBFNTY1QjRGM0Y4ODVBRTc5NEY5REM5QTI0OENGNjUyMg==");
		System.out.println("解密后数据：" + DeString);
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("解密用时：" + lUseTime + "毫秒");
	}

}