package com.gionee.wms.common.excel.excelimport.bean;

import java.util.List;
import java.util.Map;

/**
 * 简单的Excel数据
 * 不包含单元格的具体信息（位置）
 */
public class SimpleExcelData
{
	// 一次数据
	private Map<String, String> onceData;
	
	// 重复数据
	private List<Map<String, String>> repeatData;

	public Map<String, String> getOnceData()
	{
		return onceData;
	}

	public void setOnceData(Map<String, String> onceData)
	{
		this.onceData = onceData;
	}

	public List<Map<String, String>> getRepeatData()
	{
		return repeatData;
	}

	public void setRepeatData(List<Map<String, String>> repeatData)
	{
		this.repeatData = repeatData;
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer(100);
		if(onceData != null && onceData.size() > 0)
		{
			buf.append("-----------------------------一次性数据-----------------------------\n");
			for(String key : onceData.keySet())
			{
				buf.append((onceData.get(key))).append("\n");
			}
		}
		if(repeatData != null && repeatData.size() > 0)
		{
			buf.append("*****************************重复数据*****************************\n");
			for(Map<String, String> map : repeatData)
			{
				if(map != null && map.size() > 0)
				{
					buf.append("^^^^^^^^^^^^^^^^^^^^^^^^^一行数据^^^^^^^^^^^^^^^^^^^^^^^^^\n");
					for(String key : map.keySet())
					{
						buf.append((map.get(key))).append("\n");
					}
				}
			}
		}
		return buf.toString();
	}
}