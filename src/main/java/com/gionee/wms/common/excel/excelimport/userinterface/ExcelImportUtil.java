package com.gionee.wms.common.excel.excelimport.userinterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.JDOMException;

import com.gionee.wms.common.excel.excelimport.bean.ExcelData;
import com.gionee.wms.common.excel.excelimport.bean.ExcelImportException;
import com.gionee.wms.common.excel.excelimport.bean.ExcelStruct;
import com.gionee.wms.common.excel.excelimport.bean.SimpleExcelData;
import com.gionee.wms.common.excel.excelimport.util.ExcelDataReader;
import com.gionee.wms.common.excel.excelimport.util.ExcelDataUtil;
import com.gionee.wms.common.excel.excelimport.util.ParseXMLUtil;
import com.gionee.wms.common.excel.excelimport.util.StringUtil;


/**
 * 读取导入的Excel的内容
 */
public class ExcelImportUtil
{
	private ExcelImportUtil(){}
	
	// private static Logger log = Logger.getLogger(ExcelImportUtil.class);
	
	/**
	 * 读取导入的Excel的文件内容
	 * @param xmlFile			描述被导入的Excel的格式的XML文件
	 * @param importExcelStream	被导入的XML文件
	 * @return					Excel中需要导入的数据
	 */
	public static ExcelData readExcel(String xmlFile, InputStream importExcelStream) throws ExcelImportException
	{
		if(StringUtil.isEmpty(xmlFile) || importExcelStream == null)
		{
			return null;
		}
		try
		{
			// 1. 解析XML描述文件
			ExcelStruct excelStruct = ParseXMLUtil.parseImportStruct(xmlFile);
			// 2. 按照XML描述文件，来解析Excel中文件的内容
			return ExcelDataReader.readExcel(excelStruct, importExcelStream, 0);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			// log.error("导入Excel失败 - XML描述文件未找到 : ", e);
			throw new ExcelImportException("导入Excel失败 - XML描述文件未找到 : ", e);
		}
		catch (JDOMException e)
		{
			e.printStackTrace();
			// log.error("导入Excel失败 - 解析XML描述文件异常 : ", e);
			throw new ExcelImportException("导入Excel失败 - 解析XML描述文件异常 : ", e);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			// log.error("导入Excel失败 - IO异常 : ", e);
			throw new ExcelImportException("导入Excel失败 - IO异常 : ", e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// log.error("导入Excel失败 : ", e);
			throw new ExcelImportException("导入Excel失败 : ", e);
		}
	};
	
	/**
	 * 读取导入的Excel的文件内容
	 * @param xmlFile			描述被导入的Excel的格式的XML文件
	 * @param importExcelStream	被导入的XML文件
	 * @return					Excel中需要导入的数据
	 */
	public static SimpleExcelData simpleReadExcel(String xmlFile, InputStream importExcelStream) throws ExcelImportException
	{
		ExcelData excelData = readExcel(xmlFile, importExcelStream);
		return ExcelDataUtil.changeExcelDataToSimple(excelData);
	}
}