package com.gionee.wms.common.excel.excelimport.util; 

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.gionee.wms.common.excel.excelimport.bean.ExcelImportException;
import com.gionee.wms.common.excel.excelimport.bean.ExcelStruct;
import com.gionee.wms.common.excel.excelimport.bean.ImportCellDesc;


/**
 * 解析导入Excel的XML描述文件
 */
@SuppressWarnings("unchecked")
public class ParseXMLUtil
{
	private ParseXMLUtil(){};

	/**
	 * 根据给定的XML文件解析出Excel的结构
	 */
	public static ExcelStruct parseImportStruct(String xmlFile) throws JDOMException, IOException
	{
		if(StringUtil.isEmpty(xmlFile))
		{
			return null;
		}
		// InputStream is = XMLResourceHelper.class.getResourceAsStream(xmlFile);
		// InputStream is = new FileInputStream(xmlFile);
		InputStream is = ParseXMLUtil.class.getClassLoader().getResourceAsStream(xmlFile);
		if(is == null)
		{
			throw new FileNotFoundException("Excel的描述文件 : " + xmlFile + " 未找到.");
		}
		SAXBuilder saxBuilder = new SAXBuilder(false);
		Document document = saxBuilder.build(is);
		// 根节点
		Element root = document.getRootElement();
		// 一次导入
		List onceList = root.getChildren("onceImport");
		// 重复导入
		List repeatList = root.getChildren("repeatImport");
		// 校验器的定义
		List validators = root.getChildren("validators");
		// 单元格校验
		List cellValidators = root.getChildren("cell-validators");
		
		ExcelStruct excelStruct = new ExcelStruct();
		
		// 读取校验器配置
		parseValidatorConfig(excelStruct, validators, cellValidators);
		
		simpleParseOnceImport(excelStruct, onceList);
		
		simpleParseRepeatImport(excelStruct, repeatList);
		
		is.close();
		
		return excelStruct;
	}
	
	/**
	 * 读取校验器的相关配置
	 */
	private static void parseValidatorConfig(ExcelStruct excelStruct, List validators, List cellValidators)
	{
		if(excelStruct == null || validators == null || validators.size() <= 0 || cellValidators == null || cellValidators.size() <= 0)
		{
			return;
		}
		// 1.读取校验器的定义
		Element validElem = (Element)validators.get(0);
		if(validElem == null)
		{
			return;
		}
		List validatorList = validElem.getChildren("validator");
		if(validatorList == null || validatorList.size() <= 0)
		{
			return;
		}
		for(Object obj : validatorList)
		{
			if(obj == null)
			{
				continue;
			}
			Element validator = (Element)obj;
			String name = validator.getAttributeValue("name");
			String value = validator.getAttributeValue("value");
			excelStruct.addSysValidator(name, value);
		}
		// 2.读取单元格的校验器
		Element cellValidElem = (Element)cellValidators.get(0);
		if(cellValidElem == null)
		{
			return;
		}
		List cellValidatorList = cellValidElem.getChildren("cell-validator");
		if(cellValidatorList == null || cellValidatorList.size() <= 0)
		{
			return;
		}
		for(Object obj : cellValidatorList)
		{
			if(obj == null)
			{
				continue;
			}
			Element cellValidator = (Element)obj;
			String cellname = cellValidator.getAttributeValue("cellname");	// 单元格名称
			if(StringUtil.isEmpty(cellname))
			{
				continue;
			}
			List cValidators = cellValidator.getChildren("validator");		// 单元格所使用的校验器
			if(cValidators == null || cValidators.size() <= 0)
			{
				continue;
			}
			for(Object tmp : cValidators)
			{
				if(tmp == null)
				{
					continue;
				}
				Element validator = (Element)tmp;
				String validatorName = validator.getAttributeValue("name");
				excelStruct.addCellValidator(cellname, validatorName);
			}
		}
	}
	
	/*********************************************************************************************************************************
	 ***********************************************************使用CDATA区************************************************************
	 *********************************************************************************************************************************/
	/**
	 * 使用CDATA区简单解析XML
	 */
	private static void simpleParseOnceImport(ExcelStruct excelStruct, List onceList)
	{
		if(onceList == null || onceList.size() <= 0)
		{
			return;
		}
		Element onceElem = (Element)onceList.get(0);
		// 获取CDATA区内的内容
		String cdata = onceElem.getText();
		List<ImportCellDesc> onceImportCells = changeCDATAToList(excelStruct, cdata);
		if(onceImportCells == null || onceImportCells.size() <= 0)
		{
			return;
		}
		excelStruct.setOnceImportCells(onceImportCells);
	};
	
	/**
	 * 解析CDATA区
	 */
	private static void simpleParseRepeatImport(ExcelStruct excelStruct, List repeatList)
	{
		if(repeatList == null || repeatList.size() <= 0)
		{
			return;
		}
		Element repElem = (Element)repeatList.get(0);
		
		// 获取CDATA区内的内容
		String cdata = repElem.getText();
		List<ImportCellDesc> repeatImportCells = changeCDATAToList(excelStruct, cdata);
		if(repeatImportCells == null || repeatImportCells.size() <= 0)
		{
			return;
		}
		// 读取终止行
		String endCode = null;
		try
		{
			endCode = ((Element)repElem.getChildren("endCode").get(0)).getTextTrim();
		} catch (IndexOutOfBoundsException e)
		{
			throw new ExcelImportException("导入Excel失败 : 请在XML描述文件中添加<endCode/>配置项!");
		}
		excelStruct.setEndCode(endCode);
		excelStruct.setRepeatImportCells(repeatImportCells);
	}
	
	/**
	 * 将CDATA区中的数据转换成我们需要的对象
	 */
	private static List<ImportCellDesc> changeCDATAToList(ExcelStruct excelStruct, String cdata)
	{
		if(StringUtil.isEmpty(cdata))
		{
			return null;
		}
		// 去掉空白字符
		cdata = cdata.trim();
		cdata = cdata.replaceAll("\\s", "");
		if(StringUtil.isEmpty(cdata))
		{
			return null;
		}
		String[] arr = cdata.split(",");
		if(arr == null || arr.length <= 0)
		{
			return null;
		}
		List<ImportCellDesc> list = new ArrayList<ImportCellDesc>();
		for(int i = 0; i < arr.length; i++)
		{
			if(StringUtil.isEmpty(arr[i]))
			{
				continue;
			}
			String[] kv = arr[i].split("=");
			if(kv == null || kv.length < 2)
			{
				continue;
			}
			ImportCellDesc cellDesc = new ImportCellDesc();
			cellDesc.setCellRef(kv[0].toUpperCase());
			cellDesc.setFieldName(kv[1].toUpperCase());
			if(excelStruct != null)
			{
				cellDesc.setValidatorList(excelStruct.getCellValidatorMap().get(cellDesc.getCellRef()));
			}
			list.add(cellDesc);
		}
		return list;
	}
	/*********************************************************************************************************************************
	 **********************************************************不使用CDATA区***********************************************************
	 *********************************************************************************************************************************/
	/**
	 * 解析重复导入
	 */
	@SuppressWarnings("unused")
	private static void parseRepeatImport(ExcelStruct excelStruct, List repeatList)
	{
		if(repeatList == null || repeatList.size() <= 0)
		{
			return;
		}
		Element onceElem = (Element)repeatList.get(0);
		List cellDescList = onceElem.getChildren("cellDesc");
		if(cellDescList == null || cellDescList.size() <= 0)
		{
			return;
		}
		List<ImportCellDesc> repeatImportCells = new ArrayList<ImportCellDesc>();
		// 遍历每一个单元格的描述信息
		for(Object obj : cellDescList)
		{
			Element cellElem = (Element)obj;
			String cellRef = ((Element)cellElem.getChildren("cellRef").get(0)).getTextTrim();
			String fieldName = ((Element)cellElem.getChildren("fieldName").get(0)).getTextTrim();
			
			ImportCellDesc cellDesc = new ImportCellDesc();
			cellDesc.setCellRef(cellRef.toUpperCase());
			cellDesc.setFieldName(fieldName.toUpperCase());
			
			repeatImportCells.add(cellDesc);
		}
		// 读取终止行
		String endCode = null;
		try
		{
			endCode = ((Element)onceElem.getChildren("endCode").get(0)).getTextTrim();
		} catch (IndexOutOfBoundsException e)
		{
			throw new ExcelImportException("导入Excel失败 : 请在XML描述文件中添加<endCode/>配置项!");
		}
		excelStruct.setEndCode(endCode);
		excelStruct.setRepeatImportCells(repeatImportCells);
	}

	/**
	 * 解析一次导入
	 */
	@SuppressWarnings("unused")
	private static void parseOnceImport(ExcelStruct excelStruct, List onceList)
	{
		if(onceList == null || onceList.size() <= 0)
		{
			return;
		}
		Element onceElem = (Element)onceList.get(0);
		List cellDescList = onceElem.getChildren("cellDesc");
		if(cellDescList == null || cellDescList.size() <= 0)
		{
			return;
		}
		List<ImportCellDesc> onceImportCells = new ArrayList<ImportCellDesc>();
		// 遍历每一个单元格的描述信息
		for(Object obj : cellDescList)
		{
			Element cellElem = (Element)obj;
			String cellRef = ((Element)cellElem.getChildren("cellRef").get(0)).getTextTrim();
			String fieldName = ((Element)cellElem.getChildren("fieldName").get(0)).getTextTrim();
			
			ImportCellDesc cellDesc = new ImportCellDesc();
			cellDesc.setCellRef(cellRef.toUpperCase());
			cellDesc.setFieldName(fieldName.toUpperCase());
			
			onceImportCells.add(cellDesc);
		}
		excelStruct.setOnceImportCells(onceImportCells);
	};
}