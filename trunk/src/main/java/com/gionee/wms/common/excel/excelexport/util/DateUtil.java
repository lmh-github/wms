package com.gionee.wms.common.excel.excelexport.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式处理类
 */
public class DateUtil {
    private DateUtil() {
    }

    ;

    /**
     * 返回:20100910210637578
     */
    public static String formate(Date date) {
        if (date == null) {
            return "";
        }
        return String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL", date);
    }

    /**
     * 返回:2010-09-10 21:08:17
     */
    public static String formateYMDHMS(Date date) {
        if (date == null) {
            return "";
        }
        return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", date);
    }

    /**
     * 返回:2010-09-10
     */
    public static String formateYMD(Date date) {
        if (date == null) {
            return "";
        }
        return String.format("%1$tY-%1$tm-%1$td", date);
    }

    /**
     * 返回:2010年09月10日
     */
    public static String formateYMD_CN(Date date) {
        if (date == null) {
            return "";
        }
        return String.format("%1$tY年%1$tm月%1$td日", date);
    }

    /**
     * 返回:09-10
     */
    public static String formateMD(Date date) {
        if (date == null) {
            return "";
        }
        return String.format("%1$tm-%1$td", date);
    }

    /**
     * 返回:09月10日
     */
    public static String formateMD_CN(Date date) {
        if (date == null) {
            return "";
        }
        return String.format("%1$tm月%1$td日", date);
    }

    public static String betweenTime(Date date1, Date date2) {

        if (date1 == null || date2 == null) {
            return "未知";
        }

        long l=date2.getTime()-date1.getTime();
        long day=l/(24*60*60*1000);
        long hour=(l/(60*60*1000)-day*24);
        long min=((l/(60*1000))-day*24*60-hour*60);
        long s=(l/1000-day*24*60*60-hour*60*60-min*60);




        return ""+day+"天"+hour+"小时"+min+"分"+s+"秒";

    }
    public static Date stringToDate(String date)  {
        SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if("null".equals(date) || "".equals(date)){
            return null;
        }
        if(date.trim().length()>=19){
            date=date.substring(0,19);
        }
        Date d=null;
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * @param args
     */

    public static void main2(String[] args) {
        System.out.println(formate(new Date()));
        System.out.println(formateYMDHMS(new Date()));
        System.out.println(formateYMD(new Date()));
        System.out.println(formateYMD_CN(new Date()));
        System.out.println(formateMD(new Date()));
        System.out.println(formateMD_CN(new Date()));
    }

    public static void main(String[] args) {


        System.out.println(stringToDate("2016-06-19 19:31:28.0"));

    }
/*
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date now = df.parse("2004-03-26 13:31:40");
        java.util.Date date=df.parse("2004-01-02 11:30:24");
        long l=now.getTime()-date.getTime();
        long day=l/(24*60*60*1000);
        long hour=(l/(60*60*1000)-day*24);
        long min=((l/(60*1000))-day*24*60-hour*60);
        long s=(l/1000-day*24*60*60-hour*60*60-min*60);
        System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
    }*/
}