package com.gionee.wms.common.excel.excelimport.util;

public class StringUtil
{
	private StringUtil(){};
	
	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str)
	{
		if(str == null || str.length() <= 0 || str.trim() == null || str.trim().length() <= 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否不为空
	 */
	public static boolean isNotEmpty(String str)
	{
		return !isEmpty(str);
	}
}