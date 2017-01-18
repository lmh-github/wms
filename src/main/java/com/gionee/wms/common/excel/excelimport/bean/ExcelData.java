package com.gionee.wms.common.excel.excelimport.bean;
/**
 * Excel导入的信息
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gionee.wms.common.excel.excelimport.util.CellRefComparator;
import com.gionee.wms.common.excel.excelimport.util.StringUtil;
import com.gionee.wms.common.excel.excelimport.validate.AbstractValidator;


public class ExcelData
{
	// 一次数据（key是字段名）
	private Map<String, ImportCellDesc> onceData;
	
	// 重复数据（key是字段名），一个map是单元格中一行数据
	private List<Map<String, ImportCellDesc>> repeatData;
	
	// 数据校验时的异常信息
	private Map<String, Set<String>> errorMsgList;
	
	public ExcelData(){}

	public Map<String, ImportCellDesc> getOnceData()
	{
		return onceData;
	}

	public void setOnceData(Map<String, ImportCellDesc> onceData)
	{
		this.onceData = onceData;
	}

	public List<Map<String, ImportCellDesc>> getRepeatData()
	{
		return repeatData;
	}

	public void setRepeatData(List<Map<String, ImportCellDesc>> repeatData)
	{
		this.repeatData = repeatData;
	}
	
	public Map<String, Set<String>> getErrorMsgList()
	{
		return errorMsgList;
	}
	
	/**
	 * 返回错误信息
	 */
	public String getErrorMsg()
	{
		if(errorMsgList == null || errorMsgList.size() <= 0)
		{
			return null;
		}
		StringBuffer buf = new StringBuffer(500);
		Set<String> keys = errorMsgList.keySet();
		for(String key : keys)
		{
			Set<String> errorList = errorMsgList.get(key);
			if(errorList == null || errorList.size() <= 0)
			{
				continue;
			}
			for(String msg : errorList)
			{
				if(StringUtil.isNotEmpty(msg) && !AbstractValidator.OK.equals(msg))
				{
					buf.append(msg).append("\n");
				}
			}
		}
		return buf.toString();
	}

	public void setErrorMsgList(Map<String, Set<String>> errorMsgList)
	{
		this.errorMsgList = errorMsgList;
	}
	
	public void addErrorMsg(String cellRef, String errorMsg)
	{
		if(StringUtil.isEmpty(cellRef) || StringUtil.isEmpty(errorMsg) || AbstractValidator.OK.equals(errorMsg))
		{
			return;
		}
		if(this.errorMsgList == null)
		{
			this.errorMsgList = new TreeMap<String, Set<String>>(new CellRefComparator());
		}
		Set<String> errorList = errorMsgList.get(cellRef);
		if(errorList == null)
		{
			errorList = new HashSet<String>();
			errorMsgList.put(cellRef, errorList);
		}
		errorList.add(errorMsg);
	}
	
	public static void main(String[] args)
	{
		String reg = "^([a-zA-Z]+)(\\d+)$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher("A123");
		if(matcher.matches())
		{
			System.out.println(matcher.group(1));
		}
		// String str = matcher.group(1);
		// System.out.println(str);
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
			for(Map<String, ImportCellDesc> map : repeatData)
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
		if(this.errorMsgList != null && this.errorMsgList.size() > 0)
		{
			buf.append("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^异常信息^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n");
			buf.append(getErrorMsg());
		}
		return buf.toString();
	}
	
	/**
	 * 返回所有的一次导入的单元格数据
	 */
	public List<ImportCellDesc> getOnceCellList()
	{
		if(onceData == null || onceData.size() <= 0)
		{
			return null;
		}
		List<ImportCellDesc> list = new ArrayList<ImportCellDesc>();
		Set<String> keys = onceData.keySet();
		for(String key : keys)
		{
			if(StringUtil.isEmpty(key))
			{
				continue;
			}
			ImportCellDesc cellDesc = onceData.get(key);
			if(cellDesc != null)
			{
				list.add(cellDesc);
			}
		}
		return list;
	}
	
	/**
	 * 返回所有的循环次导入的单元格数据
	 */
	public List<ImportCellDesc> getRepeatCellList()
	{
		if(repeatData == null || repeatData.size() <= 0)
		{
			return null;
		}
		
		List<ImportCellDesc> list = new ArrayList<ImportCellDesc>();
		
		// 循环每行数据
		for(Map<String, ImportCellDesc> rowMap : repeatData)
		{
			if(rowMap == null || rowMap.size() <= 0)
			{
				continue;
			}
			Set<String> keys = rowMap.keySet();
			// 循环一次中每列数据
			for(String key : keys)
			{
				if(StringUtil.isEmpty(key))
				{
					continue;
				}
				ImportCellDesc cellDesc = rowMap.get(key);
				if(cellDesc != null)
				{
					list.add(cellDesc);
				}
			}
		}
		return list;
	}
	
	/**
	 * 返回导入的所有的单元格数据
	 */
	public List<ImportCellDesc> getAllCellList()
	{
		List<ImportCellDesc> list = new ArrayList<ImportCellDesc>();
		
		List<ImportCellDesc> onceList = getOnceCellList();
		List<ImportCellDesc> repeatList = getRepeatCellList();
		
		if(onceList != null && onceList.size() > 0)
		{
			list.addAll(onceList);
		}
		if(repeatList != null && repeatList.size() > 0)
		{
			list.addAll(repeatList);
		}
		
		return list;
	}
}