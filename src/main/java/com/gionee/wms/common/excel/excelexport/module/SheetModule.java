/**
 * 功能:定义一个Sheet中要输出的数据
 */
package com.gionee.wms.common.excel.excelexport.module;

import java.util.List;
import java.util.Map;

import com.gionee.wms.common.excel.excelexport.util.MapKeyUtil;


public class SheetModule
{
	// #colName#的数据
	private Map<String, String> onceData;
	// %colName%的数据
	private List<Map<String, String>> multData;

	public SheetModule(Map<String, String> onceData, List<Map<String, String>> multData)
	{
		this.onceData = MapKeyUtil.keyToUpper(onceData);
		this.multData = MapKeyUtil.keyToUpper(multData);
	}

	public Map<String, String> getOnceData()
	{
		return onceData;
	}

	public void setOnceData(Map<String, String> onceData)
	{
		this.onceData = onceData;
	}

	public List<Map<String, String>> getMultData()
	{
		return multData;
	}

	public void setMultData(List<Map<String, String>> multData)
	{
		this.multData = multData;
	}
}