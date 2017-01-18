package com.gionee.wms.dto;

import java.util.ArrayList;

/**
 *
 * <p>Title: ExcelModel</p>
 *
 * <p>Description: Excel表的操作模型</p>
 *
 * <p>Copyright: Copyright (c) 2005-10-20</p>
 *
 * <p>Company: *** </p>
 *
 * @author zzj
 * @version 1.0
 */

public class ExcelModel {
      /**
       * 文件路径，这里是包含文件名的路径
       */
      protected String path;
      /**
       * 工作表名
       */
      protected String sheetName;
      /**
       * 表内数据,保存在二维的ArrayList对象中
       */
      protected ArrayList data;
      /**
       * 数据表的标题内容
       */
      protected ArrayList header;
      /**
       * 用于设置列宽的整型数组
       * 这个方法在程序中暂未用到
       * 适用于固定列数的表格
       */
      protected int[] width;

      public ExcelModel() {
            path="report.xls";
      }

      public ExcelModel(String path) {
           this.path=path;
     }


      public void setPath(String path){
           this.path=path;
      }

      public String getPath(){
            return this.path;
      }
      
      public void setSheetName(String sheetName){
          this.sheetName=sheetName;
      }
      
      public String getSheetName(){
          return this.sheetName;
      }

      public void setData(ArrayList data){
            this.data=data;
      }

      public ArrayList getData(){
            return this.data;
      }

      public void setHeader(ArrayList header){
            this.header=header;
      }

      public ArrayList getHeader(){
            return this.header;
      }

      public void setWidth(int[] width){
            this.width=width;
      }

      public int[] getWidth(){
            return this.width;
      }
}