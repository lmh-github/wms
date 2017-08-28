package com.gionee.wms.common.excel;

import com.gionee.wms.common.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by gionee on 2017/8/22.
 */
public class ExcelUtil {

    // mapping key: cell num
    // split: ,
    // listValue start with array
    public static String read(LinkedHashMap<String, String> mapping,
                              MultipartFile multipartFile, int startRow, int startCell, int sheetNum) {
        String jsonStr = "";
        JsonUtils jsonUtils = new JsonUtils();
        if (multipartFile.getOriginalFilename().contains(".xlsx")) {
            jsonStr = jsonUtils.toJson(readExcelByXlsx(multipartFile, startRow, startCell, sheetNum, mapping));
        } else if (multipartFile.getOriginalFilename().contains(".xls")) {
            jsonStr = jsonUtils.toJson(readExcelByXls(multipartFile, startRow, startCell, sheetNum, mapping));
        }
        return jsonStr;
    }

    // 获取07以下版本数据源
    private static List<Map<String, Object>> readExcelByXls(
        MultipartFile multipartFile, int startRow, int startCell, int sheetNum,
        LinkedHashMap<String, String> mapping) {

        List<Map<String, Object>> hssfList = new ArrayList<>();

        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(multipartFile.getInputStream());
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetNum);

            for (int i = startRow; i < hssfSheet.getLastRowNum() + 1; i++) {
                if (isRowEmpty(hssfSheet.getRow(i))) {

                    Map<String, Object> map = new HashMap<>();
                    hssfList.add(map);

                    HSSFRow hssfRow = hssfSheet.getRow(i);
                    for (int j = startCell; j < hssfRow.getLastCellNum(); j++) {
                        HSSFCell hssfCell = hssfRow.getCell(j);
                        if (hssfCell != null) {
                            convert(mapping, map, j, hssfCell);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hssfList;
    }


    // 获取07版本数据源
    private static List<Map<String, Object>> readExcelByXlsx(
        MultipartFile multipartFile, int startRow, int startCell, int sheetNum,
        LinkedHashMap<String, String> mapping) {

        List<Map<String, Object>> xssfList = new ArrayList<>();

        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetNum);

            for (int i = startRow; i < xssfSheet.getLastRowNum() + 1; i++) {
                if (isRowEmpty(xssfSheet.getRow(i))) {
                    Map<String, Object> map = new HashMap<>();
                    xssfList.add(map);
                    XSSFRow xssfRow = xssfSheet.getRow(i);
                    for (int j = startCell; j < xssfRow.getLastCellNum(); j++) {
                        XSSFCell xssfCell = xssfRow.getCell(j);
                        if (xssfCell != null) {
                            convert(mapping, map, j, xssfCell);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xssfList;
    }

    // 判断空行
    private static boolean isRowEmpty(Row row) {
        Cell cell = row.getCell(0);
        return cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK;
    }

    // 转换
    private static void convert(LinkedHashMap<String, String> mapping
        , Map<String, Object> map, int cellNum, Cell cell) {
        String key = mapping.get(cellNum + "");
        if (!StringUtils.isEmpty(key)) {
            String[] keys = key.split(",");
            if (keys.length == 1) { //普通属性
                map.put(keys[0], switchCellValue(cell));
            } else if (keys.length == 3 && "array".equals(keys[0])) {// 集合属性
                String listKey = keys[1];
                Object listMap = map.get(listKey);
                if (listMap != null) {
                    ((List<LinkedHashMap<String, Object>>) listMap).get(0).put(keys[2], switchCellValue(cell));
                } else {
                    List<LinkedHashMap<String, Object>> linkedList = new ArrayList<>();
                    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
                    linkedHashMap.put(keys[2], switchCellValue(cell));
                    linkedList.add(linkedHashMap);
                    map.put(keys[1], linkedList);
                }
            }
        }
    }

    // 根据类型读取excel
    private static Object switchCellValue(Cell cell) {
        Object cellValue = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                Double num = cell.getNumericCellValue();
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 日期
                    cellValue = DateUtil.getJavaDate(num).getTime();
                } else {
                    // 数字
                    DecimalFormat df = new DecimalFormat("#");
                    cellValue = df.format(num);
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                break;
            default:
                break;
        }
        return cellValue;
    }

}
