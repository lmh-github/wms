package com.gionee.wms.common.excel.excelimport.validate;

import com.gionee.wms.common.excel.excelimport.util.StringUtil;

/**
 * 长度校验器
 */
public class LengthValidator extends AbstractValidator
{
	public String processValidate()
	{
		int maxLength = 20;
		int minLength = 6;
		
		if(StringUtil.isNotEmpty(getFieldValue()) && getFieldValue().length() >= minLength && getFieldValue().length() <= maxLength)
		{
			return OK;
		}
		
		return getCellRef() + "单元格数据 : " + getFieldValue() + ", 长度不合法, 必须在 " + minLength + "~" + maxLength + " 之间!";
	}
}