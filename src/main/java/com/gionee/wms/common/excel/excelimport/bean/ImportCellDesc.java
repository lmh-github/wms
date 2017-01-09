package com.gionee.wms.common.excel.excelimport.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.CellReference;

import com.gionee.wms.common.excel.excelimport.util.StringUtil;


/**
 * 单元格的描述信息
 * 
 * （1）数据格式校验时，可以精确定位到某个单元格。
 */
public class ImportCellDesc
{
	/**
	 * 引用的单元格；如：A3
	 */
	private String cellRef;
	
	/**
	 * 单元格的对应数据库的字段名称；
	 * 如：fieldName = "username"
	 */
	private String fieldName;
	
	/**
	 * 字段值
	 */
	private String fieldValue;
	
	/**
	 * 字段的校验器
	 */
	private List<String> validatorList = new ArrayList<String>();
	
	public ImportCellDesc(){}
	
	public ImportCellDesc(ImportCellDesc o)
	{
		if(o != null)
		{
			this.cellRef = o.getCellRef();
			this.fieldName = o.getFieldName();
			this.fieldValue = o.getFieldValue();
			this.validatorList = o.getValidatorList();
		}
	}
	
	public ImportCellDesc(String cellRef, String fieldName, String fieldValue)
	{
		this.cellRef = cellRef;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	/*********************************************************************************************************************************
	 *************************************************************工具方法*************************************************************
	*********************************************************************************************************************************/
	/**
	 * 返回单元格的行标（从1开始）
	 * @param cellRef	如：B5
	 * @return	5
	 */
	public int getCellRow()
	{
		CellReference ref = new CellReference(cellRef);
		return ref.getRow() + 1;
	}
	/**
	 * 返回单元格的列标（从1开始）
	 * @param cellRef	如：B5
	 * @return	2
	 */
	public int getCellCol()
	{
		CellReference ref = new CellReference(cellRef);
		return ref.getCol() + 1;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer(100);
		if(validatorList != null && validatorList.size() > 0)
		{
			sb.append("\tvalidator : [ ");
			for(int i = 0; i < validatorList.size(); i++)
			{
				String validator = validatorList.get(i);
				if(i != validatorList.size() - 1)
				{
					sb.append(validator).append(" , ");
				}
				else
				{
					sb.append(validator);
				}
			}
			sb.append(" ]");
		}
		return getCellRef() + "\t(" + getFieldName() + " : " + getFieldValue() + ")" + sb.toString();
	}
	
	/*********************************************************************************************************************************
	 **********************************************************setter/getter**********************************************************
	*********************************************************************************************************************************/
	public String getCellRef()
	{
		return cellRef;
	}

	public void setCellRef(String cellRef)
	{
		this.cellRef = cellRef;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getFieldValue()
	{
		return fieldValue;
	}

	public void setFieldValue(String fieldValue)
	{
		this.fieldValue = fieldValue;
	}

	public List<String> getValidatorList()
	{
		return validatorList;
	}

	public void setValidatorList(List<String> validatorList)
	{
		this.validatorList = validatorList;
	}
	
	public void addValidator(String validator)
	{
		if(StringUtil.isEmpty(validator))
		{
			return;
		}
		if(validatorList == null)
		{
			validatorList = new ArrayList<String>();
		}
		validatorList.add(validator);
	}
}