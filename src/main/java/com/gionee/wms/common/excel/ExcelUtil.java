package com.gionee.wms.common.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
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


    public static String read(LinkedHashMap<String, String> mapping, int choose,
                              MultipartFile multipartFile, int startRow, int startCell, int sheetNum) {
        String jsonStr = "";
        if (multipartFile.getOriginalFilename().contains(".xlsx")) {
            jsonStr = ConvertBean.getInstance(mapping, choose)
                .convert(readExcelByXlsx(multipartFile, startRow, startCell, sheetNum), jsonStr);
        } else if (multipartFile.getOriginalFilename().contains(".xls")) {
            jsonStr = ConvertBean.getInstance(mapping, choose)
                .convert(readExcelByXls(multipartFile, startRow, startCell, sheetNum), jsonStr);
        }
        return jsonStr;
    }

    // 获取07以下版本数据源
    private static List<Map<String, Object>> readExcelByXls(
        MultipartFile multipartFile, int startRow, int startCell, int sheetNum) {

        List<Map<String, Object>> hssfList = new ArrayList<>();

        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(multipartFile.getInputStream());
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetNum);

            for (int i = startRow; i < hssfSheet.getLastRowNum() + 1; i++) {
                Map<String, Object> map = new HashMap<>();
                hssfList.add(map);

                HSSFRow hssfRow = hssfSheet.getRow(i);
                for (int j = startCell; j < hssfRow.getLastCellNum(); j++) {
                    HSSFCell hssfCell = hssfRow.getCell(j);
                    if (hssfCell != null) {
                        map.put("cell" + j, switchCellValue(hssfCell));
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
        MultipartFile multipartFile, int startRow, int startCell, int sheetNum) {

        List<Map<String, Object>> xssfList = new ArrayList<>();

        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetNum);

            for (int i = startRow; i < xssfSheet.getLastRowNum() + 1; i++) {
                Map<String, Object> map = new HashMap<>();
                xssfList.add(map);

                XSSFRow xssfRow = xssfSheet.getRow(i);
                for (int j = startCell; j < xssfRow.getLastCellNum(); j++) {
                    XSSFCell xssfCell = xssfRow.getCell(j);
                    if (xssfCell != null) {
                        map.put("cell" + j, switchCellValue(xssfCell));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xssfList;
    }


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

    public interface Choose {
        int transfer = 1; //调拨单导入
        int IMEI = 2; // IMEI导入

    }

    public static class ConvertBean {
        private LinkedHashMap<String, String> mapping; //映射map key:字段名 value:cell num
        private static volatile ConvertBean TRANSFER_INSTANCE = null;
        private static volatile ConvertBean IMEI_INSTANCE = null;

        private ConvertBean(LinkedHashMap<String, String> mapping) {
            this.mapping = mapping;
        }

        public static ConvertBean getInstance(LinkedHashMap<String, String> mapping, int choose) {
            if (Choose.IMEI == choose) {
                if (IMEI_INSTANCE == null) {
                    synchronized (ConvertBean.class) {
                        if (IMEI_INSTANCE == null) {
                            IMEI_INSTANCE = new ConvertBean(mapping);
                        }
                    }
                }
                return IMEI_INSTANCE;
            } else {
                if (TRANSFER_INSTANCE == null) {
                    synchronized (ConvertBean.class) {
                        if (TRANSFER_INSTANCE == null) {
                            TRANSFER_INSTANCE = new ConvertBean(mapping);
                        }
                    }
                }
                return TRANSFER_INSTANCE;
            }
        }

        // 拼接json串
        String convert(List<Map<String, Object>> sourceList, String jsonStr) {
            jsonStr += JsonMappingUtil.getPrefixArray();
            StringBuilder jsonStrBuilder = new StringBuilder(jsonStr);
            for (Map<String, Object> objectMap : sourceList) {
                jsonStrBuilder.append(JsonMappingUtil.getPrefix());
                for (Map.Entry<String, String> entry : mapping.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String[] keys = key.split(",");
                    String appendStr = JsonMappingUtil.getAppendStr(keys, value, objectMap)
                        + JsonMappingUtil.getSplit();

                    // 集合类型特殊处理
                    if ("array".equals(keys[0])) {
                        if (JsonMappingUtil.getSuffixArray().equals(jsonStrBuilder.substring(jsonStrBuilder.length() - 2, jsonStrBuilder.length() - 1))) {
                            jsonStrBuilder = new StringBuilder(jsonStrBuilder.substring(0, jsonStrBuilder.length() - 3) + JsonMappingUtil.getSplit());
                            appendStr = appendStr.substring(
                                (JsonMappingUtil.getJsonStr(keys[1])
                                    + ":" +
                                    JsonMappingUtil.getPrefixArray()
                                    + JsonMappingUtil.getPrefix()
                                ).length()
                            );
                        }
                    }
                    jsonStrBuilder.append(appendStr);
                }
                jsonStrBuilder = new StringBuilder(jsonStrBuilder.substring(0, jsonStrBuilder.length() - 1) + JsonMappingUtil.getSuffix() + JsonMappingUtil.getSplit());
            }
            jsonStr = jsonStrBuilder.toString();
            return jsonStr.substring(0, jsonStr.length() - 1) + JsonMappingUtil.getSuffixArray();
        }
    }

    static class JsonMappingUtil {

        private final static String prefixKey = "cell";

        private static String getPrefix() {
            return "{";
        }

        private static String getPrefixArray() {
            return "[";
        }

        private static String getSplit() {
            return ",";
        }

        private static String getSuffix() {
            return "}";
        }

        private static String getSuffixArray() {
            return "]";
        }

        static String getAppendStr(String[] keys, String mappingValue, Map<String, Object> map) {
            Object value = map.get(prefixKey + mappingValue);

            if (keys.length == 2) { // 对象.对象
                return null;
            } else if (keys.length == 3) { // 对象.集合对象
                if (keys[0].contains("array")) {
                    // if (keys[0].contains("first")) {
                    return getJsonStr(keys[1]) +
                        ":" +
                        getPrefixArray() +
                        getPrefix() +
                        getJsonStr(keys[2]) +
                        ":" +
                        getJsonStr(value) +
                        getSuffix() +
                        getSuffixArray();
                    //  }
                }
            }
            return getJsonStr(keys[0]) + ":" + getJsonStr(value);
        }

        private static String getJsonStr(Object value) {
            return "\"" + value + "\"";
        }


    }
}
