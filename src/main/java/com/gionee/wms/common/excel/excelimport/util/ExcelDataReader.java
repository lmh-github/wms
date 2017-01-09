package com.gionee.wms.common.excel.excelimport.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import com.gionee.wms.common.excel.excelimport.bean.ExcelData;
import com.gionee.wms.common.excel.excelimport.bean.ExcelStruct;
import com.gionee.wms.common.excel.excelimport.bean.ImportCellDesc;



/**
 * 根据描述文件，读取Excel的内容
 *
 */
public class ExcelDataReader
{
	private ExcelDataReader(){}

	/**
	 * 读取并组装Excel中的信息
	 * @param excelStruct		Excel结构描述文件
	 * @param importExcelStream	被导入的Excel
	 * @param sheetIndex		读取的sheetIndex，从0开始
	 * @return					组装后的Excel数据
	 */
	public static ExcelData readExcel(ExcelStruct excelStruct, InputStream importExcelStream, int sheetIndex) throws IOException
	{
		if(excelStruct == null || importExcelStream == null)
		{
			return null;
		}
		// 1.判断有无重复导入的数据
		// 2.如果没有，则直接读取一次数据，并返回
		// 3.如果有，则得到循环的记录行数量 repeatCount，和重复开始行repeatBeginRow
		// 4.读取所有的重复数据，读取的过程中，读取到终止时终止
		// 5.根据repeatCount、repeatBeginRow来更新一次性导入数据的行号（从1开始）
		// 6.读取所有的一次性数据
		ExcelData excelData = new ExcelData();
		// 读取excel
		Workbook wb = new HSSFWorkbook(importExcelStream);
		Sheet sheet = wb.getSheetAt(sheetIndex);
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		// 1.	2.
		List<ImportCellDesc> repeatImportCells = excelStruct.getRepeatImportCells();
		List<ImportCellDesc> onceImportCells = excelStruct.getOnceImportCells();
		if(repeatImportCells == null || repeatImportCells.size() <= 0 || StringUtil.isEmpty(excelStruct.getEndCode()))
		{
			readOnceImportData(onceImportCells, sheet, evaluator, excelData);
			return excelData;
		}
		// 3.
		int repeatBeginRow = repeatImportCells.get(0).getCellRow();
		// 4.
		// 重复导入的次数
		int repeatCount = readRepeatImportData(repeatImportCells, sheet, evaluator, excelStruct.getEndCode(), excelData);
		// 5.
		updateOnceImportCells(onceImportCells, repeatBeginRow, repeatCount);
		// 6.
		readOnceImportData(onceImportCells, sheet, evaluator, excelData);
		// 7.进行数据校验
		ValidateUtil.processValidate(excelData);
		return excelData;
	}

	/**
	 * 更新单次导入的行号
	 */
	private static void updateOnceImportCells(List<ImportCellDesc> onceImportCells, int repeatBeginRow, int repeatCount)
	{
		// 如果只重复了一次，则不需要更新（因为这个空行是模板中原有的）
		if(repeatCount <= 1 || onceImportCells == null || onceImportCells.size() <= 0)
		{
			return;
		}
		// 将行号大于onceImportCells的，行数加(repeatCount-1)
		for(ImportCellDesc desc : onceImportCells)
		{
			if(desc.getCellRow() <= repeatBeginRow)
			{
				continue;
			}
			int newRowIndex = desc.getCellRow() + repeatCount - 1;
			CellReference ref = new CellReference(newRowIndex - 1, desc.getCellCol() - 1);
			desc.setCellRef(ref.formatAsString());	// 重新设置位置
		}
	}

	/**
	 * 读取重复数据
	 * @param repeatImportCells	重复数据的描述
	 * @param excelData			需要组装的数据
	 */
	private static int readRepeatImportData(List<ImportCellDesc> repeatImportCells, Sheet sheet, FormulaEvaluator evaluator, String endCode, ExcelData excelData)
	{
		int repeatCount = 0;
		boolean endFlag = false;	// 是否到达终止标识
		if(repeatImportCells == null || repeatImportCells.size() <= 0 || StringUtil.isEmpty(endCode) || sheet == null || sheet.getLastRowNum() <= 0)
		{
			return repeatCount;
		}
		List<Map<String, ImportCellDesc>> repeatData = new ArrayList<Map<String,ImportCellDesc>>();
		int beginRow = repeatImportCells.get(0).getCellRow() - 1;
		for(int i = beginRow; i <= sheet.getLastRowNum(); i++)
		{
			Map<String, ImportCellDesc> map = new HashMap<String, ImportCellDesc>();
			Row row = sheet.getRow(i);
			if(row == null)
			{
				continue;
			}
			for(ImportCellDesc desc : repeatImportCells)
			{
				// 一定要使用新的desc，否则会有问题。
				ImportCellDesc newDesc = new ImportCellDesc(desc);
				
				CellReference ref = new CellReference(i, newDesc.getCellCol() - 1);
				newDesc.setCellRef(ref.formatAsString());	// 重新设置位置
				
				map.put(newDesc.getFieldName(), newDesc);
				Cell cell = row.getCell(newDesc.getCellCol() - 1);
				if(cell == null)
				{
					continue;
				}
				// 获取值
				String fieldValue = getCellValue(evaluator, cell);
				if(endCode.equals(fieldValue))	// 终止了
				{
					endFlag = true;
					break;
				}
				newDesc.setFieldValue(fieldValue);
			}
			if(endFlag)			// 表示已经读到最后一行了
			{
				break;
			}
			repeatCount++;		// 成功添加一行记录
			repeatData.add(map);
		}
		if(!endFlag)
		{
			//System.out.println("excel中未找到结束字符 : " + endCode);
		}
		excelData.setRepeatData(repeatData);
		return repeatCount;
	}

	/**
	 * 读取一次性导入的数据
	 * @param onceImportCells	一次性导入数据描述
	 * @param excelData			需要组装的数据
	 */
	private static void readOnceImportData(List<ImportCellDesc> onceImportCells, Sheet sheet, FormulaEvaluator evaluator, ExcelData excelData)
	{
		if(onceImportCells == null || onceImportCells.size() <= 0 || sheet == null || sheet.getLastRowNum() <= 0)
		{
			return;
		}
		Map<String, ImportCellDesc> onceData = new HashMap<String, ImportCellDesc>();
		for(ImportCellDesc desc : onceImportCells)
		{
			if(desc == null || StringUtil.isEmpty(desc.getCellRef()))
			{
				continue;
			}
			// 获取单元格
			CellReference cellReference = new CellReference(desc.getCellRef());
			Row row = sheet.getRow(cellReference.getRow());
			Cell cell = row.getCell(cellReference.getCol());
			// 获取值
			String fieldValue = getCellValue(evaluator, cell);
			desc.setFieldValue(fieldValue);
			onceData.put(desc.getFieldName(), desc);
		}
		excelData.setOnceData(onceData);
	}
	
	/**
	 * 获取单元格的值
	 */
	private static String getCellValue(FormulaEvaluator evaluator, Cell cell)
	{
		String fieldValue = "";
		if(cell != null)
		{
			switch (evaluator.evaluateInCell(cell).getCellType())
			{
				case Cell.CELL_TYPE_STRING:		// 字符串
					fieldValue = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_BOOLEAN:	// bool型
					fieldValue = cell.getBooleanCellValue() + "";
					break;
				case Cell.CELL_TYPE_NUMERIC:	// 数值型
					//判断具体的数据类型(kv)
					if(HSSFDateUtil.isCellDateFormatted(cell)){//时间类型
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						fieldValue = df.format(cell.getDateCellValue());
					}else{
						fieldValue = Double.toString(cell.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_BLANK:		// 空
					fieldValue = "";
					break;
				case Cell.CELL_TYPE_ERROR:
					fieldValue = cell.getErrorCellValue() + "";
					break;
				case Cell.CELL_TYPE_FORMULA:
					fieldValue = "";
					break;
			}
		}
		return fieldValue;
	}
}