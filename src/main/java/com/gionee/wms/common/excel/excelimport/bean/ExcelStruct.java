package com.gionee.wms.common.excel.excelimport.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gionee.wms.common.excel.excelimport.util.StringUtil;


/**
 * Excel导入描述文件的结构
 */

public class ExcelStruct
{
	// 一次导入的单元格描述信息
	private List<ImportCellDesc> onceImportCells;
	
	// 重复导入的单元格描述信息
	private List<ImportCellDesc> repeatImportCells;
	
	// 重复结束的编码，如：END
	private String endCode;
	
	/**
	 * xml中定义的校验器
	 */
	private Map<String, String> sysValidatorMap = new HashMap<String, String>();
	// 单元格对应的校验器
	private Map<String, List<String>> cellValidatorMap = new HashMap<String, List<String>>();
	
	public ExcelStruct()
	{
		this.onceImportCells = new ArrayList<ImportCellDesc>();
		this.repeatImportCells = new ArrayList<ImportCellDesc>();
	};
	
	public ExcelStruct(List<ImportCellDesc> onceImportCells, List<ImportCellDesc> repeatImportCells, String endCode)
	{
		this.onceImportCells = onceImportCells;
		this.repeatImportCells = repeatImportCells;
		this.endCode = endCode;
	}

	public List<ImportCellDesc> getOnceImportCells()
	{
		return onceImportCells;
	}

	public void setOnceImportCells(List<ImportCellDesc> onceImportCells)
	{
		this.onceImportCells = onceImportCells;
	}

	public List<ImportCellDesc> getRepeatImportCells()
	{
		return repeatImportCells;
	}

	public void setRepeatImportCells(List<ImportCellDesc> repeatImportCells)
	{
		this.repeatImportCells = repeatImportCells;
	}

	public String getEndCode()
	{
		return endCode;
	}

	public void setEndCode(String endCode)
	{
		this.endCode = endCode;
	}

	public Map<String, String> getSysValidatorMap()
	{
		return sysValidatorMap;
	}

	public void setSysValidatorMap(Map<String, String> sysValidatorMap)
	{
		this.sysValidatorMap = sysValidatorMap;
	}

	public Map<String, List<String>> getCellValidatorMap()
	{
		return cellValidatorMap;
	}

	public void setCellValidatorMap(Map<String, List<String>> cellValidatorMap)
	{
		this.cellValidatorMap = cellValidatorMap;
	}

	/**
	 * 添加校验器定义
	 * @param name	校验器名称
	 * @param value	校验器的类全称
	 */
	public void addSysValidator(String name, String value)
	{
		if(StringUtil.isEmpty(name) || StringUtil.isEmpty(value))
		{
			return;
		}
		if(this.sysValidatorMap == null)
		{
			this.sysValidatorMap = new HashMap<String, String>();
		}
		this.sysValidatorMap.put(name, value);
	}
	
	/**
	 * 给单元格添加校验器
	 * @param cellname	单元格名称
	 * @param validator	校验器名称
	 */
	public void addCellValidator(String cellname, String validator)
	{
		if(StringUtil.isEmpty(cellname) || StringUtil.isEmpty(validator))
		{
			return;
		}
		if(this.cellValidatorMap == null)
		{
			this.cellValidatorMap = new HashMap<String, List<String>>();
		}
		cellname = cellname.toUpperCase();
		List<String> validatorList = this.cellValidatorMap.get(cellname);
		if(validatorList == null)
		{
			validatorList = new ArrayList<String>();
			this.cellValidatorMap.put(cellname, validatorList);
		}
		if(sysValidatorMap != null && sysValidatorMap.containsKey(validator))
		{
			validatorList.add(sysValidatorMap.get(validator));
		}
	}
}