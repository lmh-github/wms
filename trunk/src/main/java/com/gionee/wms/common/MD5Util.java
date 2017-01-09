package com.gionee.wms.common;

import java.security.MessageDigest;

import org.apache.commons.lang.StringUtils;

/**
 * MD5Util
 * @author jade
 */
public class MD5Util {


	private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
	
	
	private static String byteArrayToHexString(byte[] b) { 
		StringBuffer resultSb = new StringBuffer(); 
		for (int i = 0; i < b.length; i++) { 
			resultSb.append(byteToHexString(b[i])); 
		} 
		return resultSb.toString(); 
	} 

	private static String byteToHexString(byte b) { 
		int n = b; 
		if (n < 0) n = 256 + n; 
		int d1 = n / 16; 
		int d2 = n % 16; 
		return hexDigits[d1] + hexDigits[d2]; 
	} 

	public static String encode(String origin) { 
		String resultString = null; 
		if(StringUtils.isBlank(origin)){
			return "";
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			resultString=byteArrayToHexString(md.digest(origin.getBytes())); 
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} 
		return resultString; 
	}
	
	public static void main(String[] args) {
		//生产
		System.out.println(encode("5af78fad46985f6a8f1e454f586e10dcpop/order_list1389408425627"));
		System.out.println(encode("5af78fad46985f6a8f1e454f586e10dcpop/get_pop_order_goods_list1389408425627"));
		System.out.println(encode("5af78fad46985f6a8f1e454f586e10dcpop/get_pop_order_goods_list1389408425627"));
		System.out.println(encode("4552深圳市金立通信设备有限公司"));
		//测试
		System.out.println(encode("4ed8845ae941c129c9fbd18e62bc88f8pop/order_list1389408425627"));
		System.out.println(encode("4ed8845ae941c129c9fbd18e62bc88f8pop/get_pop_order_goods_list1389408425627"));
		System.out.println(encode("4ed8845ae941c129c9fbd18e62bc88f8pop/export1389408425627"));
	}
}