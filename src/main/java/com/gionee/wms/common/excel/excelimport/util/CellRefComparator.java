package com.gionee.wms.common.excel.excelimport.util;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单元格位置比较器
 */
@SuppressWarnings("unchecked")
public class CellRefComparator implements Comparator<String>
{
	private static final Pattern pattern = Pattern.compile("^([a-zA-Z]+)(\\d+)$");
	
	public int compare(String cellRef1, String cellRef2)
	{
		if(cellRef1 == cellRef2)
		{
			return 0;
		}
		if(StringUtil.isEmpty(cellRef1))
		{
			return -1;
		}
		if(StringUtil.isEmpty(cellRef2))
		{
			return 1;
		}
		if(cellRef1.equals(cellRef2))
		{
			return 0;
		}
		// 单元格的列号
		String row1 = "";
		String row2 = "";
		
		// 单元格的行号
		int col1 = 0;
		int col2 = 0;
		// 解析第一个单元格
		Matcher matcher = pattern.matcher(cellRef1);
		if(matcher.find())
		{
			try
			{
				row1 = matcher.group(1);
				col1 = Integer.parseInt(matcher.group(2));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			return -1;
		}
		// 解析第二个单元格
		matcher = pattern.matcher(cellRef2);
		if(matcher.find())
		{
			try
			{
				row2 = matcher.group(1);
				col2 = Integer.parseInt(matcher.group(2));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			return 1;
		}
		
		// 先比较列号
		int _row = row1.compareToIgnoreCase(row2);
		if(_row != 0)
		{
			return _row;
		}
		
		// 再比较行号
		return col1 - col2;
	}
}