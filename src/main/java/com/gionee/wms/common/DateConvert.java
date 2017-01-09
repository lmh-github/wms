/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gionee.wms.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * <p>
 * Title: Time
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * 此类主要用来取得本地系统的系统时间并用下面5种格式显示<br>
 * 1.　YYMMDDHH 8位<br>
 * 2.　YYMMDDHHmm 10位<br>
 * 3.　YYMMDDHHmmss 12位<br>
 * 4.　YYYYMMDDHHmmss 14位<br>
 * 5.　YYMMDDHHmmssxxx 15位 (最后的xxx 是毫秒)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: hoten
 * </p>
 * 
 * @author lqf
 * @version 1.0
 */
public class DateConvert {
	private static final Calendar calender = Calendar.getInstance();
	public static final int sqltimestamp = 23;
	public static final int YYYYMMDDhhmmssxxx = 17;
	public static final int YYYYMMDDhhmmss = 14;
	public static final int YYMMDDhhmmss = 12;
	public static final int YYYY_MM_DD = 11;
	public static final int MMDDhhmmss = 10;
	public static final int YYMMDDhh = 8;

	/**
	 * 取得本地系统的时间，时间格式由参数决定
	 * 
	 * @param format
	 *            时间格式由常量决定
	 * @return　String 具有format格式的字符串
	 */
	public synchronized static String getTime(int len) {
		if (len < 0) {
			throw new IllegalArgumentException("非预期日期格式");
		}

		StringBuffer cTime = new StringBuffer(len);
		// 设为当前时间
		calender.setTimeInMillis(System.currentTimeMillis());

		int miltime = calender.get(Calendar.MILLISECOND);
		int second = calender.get(Calendar.SECOND);
		int minute = calender.get(Calendar.MINUTE);
		int hour = calender.get(Calendar.HOUR_OF_DAY);
		int day = calender.get(Calendar.DAY_OF_MONTH);
		int month = calender.get(Calendar.MONTH) + 1;
		int year = calender.get(Calendar.YEAR);

		switch (len) {
		case sqltimestamp:
			cTime.append(StringUtils.fillZeroStr(year, 4)).append('-');
			cTime.append(StringUtils.fillZeroStr(month, 2)).append('-');
			cTime.append(StringUtils.fillZeroStr(day, 2)).append(' ');
			cTime.append(StringUtils.fillZeroStr(hour, 2)).append(':');
			cTime.append(StringUtils.fillZeroStr(minute, 2)).append(':');
			cTime.append(StringUtils.fillZeroStr(second, 2)).append('.');
			cTime.append(StringUtils.fillZeroStr(miltime, 3));
			break;

		case YYYYMMDDhhmmssxxx:
			cTime.append(StringUtils.fillZeroStr(year, 4));
			cTime.append(StringUtils.fillZeroStr(month, 2));
			cTime.append(StringUtils.fillZeroStr(day, 2));
			cTime.append(StringUtils.fillZeroStr(hour, 2));
			cTime.append(StringUtils.fillZeroStr(minute, 2));
			cTime.append(StringUtils.fillZeroStr(second, 2));
			cTime.append(StringUtils.fillZeroStr(miltime, 3));
			break;

		case YYYYMMDDhhmmss:
			cTime.append(StringUtils.fillZeroStr(year, 4));
			cTime.append(StringUtils.fillZeroStr(month, 2));
			cTime.append(StringUtils.fillZeroStr(day, 2));
			cTime.append(StringUtils.fillZeroStr(hour, 2));
			cTime.append(StringUtils.fillZeroStr(minute, 2));
			cTime.append(StringUtils.fillZeroStr(second, 2));
			break;

		case YYYY_MM_DD:
			cTime.append(StringUtils.fillZeroStr(year, 4));
			cTime.append('-');
			cTime.append(StringUtils.fillZeroStr(month, 2));
			cTime.append('-');
			cTime.append(StringUtils.fillZeroStr(day, 2));
			break;

		case YYMMDDhhmmss:
			if (year >= 2000)
				year = year - 2000;
			else
				year = year - 1900;
			cTime.append(StringUtils.fillZeroStr(year, 2));
			cTime.append(StringUtils.fillZeroStr(month, 2));
			cTime.append(StringUtils.fillZeroStr(day, 2));
			cTime.append(StringUtils.fillZeroStr(hour, 2));
			cTime.append(StringUtils.fillZeroStr(minute, 2));
			cTime.append(StringUtils.fillZeroStr(second, 2));
			break;

		case MMDDhhmmss:
			cTime.append(StringUtils.fillZeroStr(month, 2));
			cTime.append(StringUtils.fillZeroStr(day, 2));
			cTime.append(StringUtils.fillZeroStr(hour, 2));
			cTime.append(StringUtils.fillZeroStr(minute, 2));
			cTime.append(StringUtils.fillZeroStr(second, 2));
			break;

		case YYMMDDhh:
			if (year >= 2000)
				year = year - 2000;
			else
				year = year - 1900;
			cTime.append(StringUtils.fillZeroStr(year, 2));
			cTime.append(StringUtils.fillZeroStr(month, 2));
			cTime.append(StringUtils.fillZeroStr(day, 2));
			cTime.append(StringUtils.fillZeroStr(hour, 2));
			break;

		default:
			break;
		}

		return cTime.toString();
	}

	/**
	 * 取得本地系统的时间
	 */
	public synchronized static java.util.Date getCurDate() {
		// 设为当前时间
		calender.setTimeInMillis(System.currentTimeMillis());

		return calender.getTime();
	}

	// 根据时时:分分，获取最近一个时间
	static public java.util.Date getNextTime(String timeStr) {
		int scanHourOfDay = 25;
		int scanMinute = 60;
		if (!(timeStr == null || "".equals(timeStr.trim()))) {
			try {
				int sp = timeStr.indexOf(':');
				scanHourOfDay = Integer.parseInt(timeStr.substring(0, sp));
				scanMinute = Integer.parseInt(timeStr.substring(sp + 1));
			} catch (Exception e) {
				scanHourOfDay = 25;
				scanMinute = 60;
			}
		}
		if (0 > scanHourOfDay || 0 > scanMinute || 23 < scanHourOfDay
				|| 60 < scanMinute || (23 == scanHourOfDay && 59 < scanMinute)) {
			return null;
		}

		Calendar ca = Calendar.getInstance();
		java.util.Date now = ca.getTime();
		ca.set(Calendar.HOUR_OF_DAY, scanHourOfDay);
		ca.set(Calendar.MINUTE, scanMinute);
		ca.set(Calendar.SECOND, 0);
		if (ca.getTime().before(now)) {
			ca.add(Calendar.DATE, 1);
		}
		return ca.getTime();
	}

	/**
	 * 把Date类型的日期转换为指定格式的字符串。
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	static public String convertD2String(java.util.Date date, String pattern) {
		if (date == null)
			return null;
		java.text.DateFormat formatter = new java.text.SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 把字符串代表的日期转换为Date类型。
	 * 
	 * @param text
	 *            yyyy-MM-dd HH:mm:ss格式的日期字符串。
	 * @return
	 */
	static public Date convertS2Date(String text) {
		if (text == null || "".equals(text.trim())) {
			return null;
		}
		String pattern = "yyyy-MM-dd HH:mm:ss";
		if (text.length() < pattern.length()) {
			pattern = pattern.substring(0, text.length());
		}
		DateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(text);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	/**
	 * 把字符串代表的日期转换为Date类型。
	 * 
	 * @param text
	 *            yyyy-MM-dd格式的日期字符串。
	 * @return
	 */
	static public Date convertS2date(String text) {
		if (text == null || "".equals(text.trim())) {
			return null;
		}
		String pattern = "yyyy-MM-dd";
		if (text.length() < pattern.length()) {
			pattern = pattern.substring(0, text.length());
		}
		DateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(text);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	/**
	 * 把字符串代表的日期转换为Date类型。
	 * 
	 * @param text
	 *            yyyy/MM/dd格式的日期字符串。
	 * @return
	 */
	static public Date convertStr2date(String text) {
		if (text == null || "".equals(text.trim())) {
			return null;
		}
		String pattern = "yyyy/MM/dd";
		if (text.length() < pattern.length()) {
			pattern = pattern.substring(0, text.length());
		}
		DateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(text);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	/**
	 * 把Date类型的日期转换为指定格式的字符串。
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	static public java.sql.Timestamp getCurTimestamp() {
		return java.sql.Timestamp.valueOf(getTime(sqltimestamp));
	}

	/**
	 * 把Date类型的日期转换为指定格式的字符串。
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	static public java.sql.Timestamp getTimestamp(String str, String pattern) {
		Date date = convertS2Date(str, pattern);
		return getTimestamp(date);
	}

	/**
	 * 把Date类型的日期转换为指定格式的字符串。
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	static public java.sql.Timestamp getTimestamp(Date date) {
		if (date == null)
			return null;
		java.sql.Timestamp retTimestamp = new java.sql.Timestamp(date.getTime());
		return retTimestamp;
	}

	/**
	 * 把Date类型的日期转换为指定格式的字符串。
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	static public String convertTimestamp2String(java.sql.Timestamp tstamp,
			int len) {
		if (tstamp == null)
			return null;
		// java.sql.Timestamp返回字串格式为yyyy-mm-dd hh:mm:ss.fffffffff
		if (len < 1)
			return "";
		if (len > 29)
			len = 29;
		return tstamp.toString().substring(0, len);
	}

	/**
	 * 把指定格式的代表日期的字符串转换为Date类型。
	 * 
	 * @param text
	 *            指定格式的日期字符串。
	 * @param pattern
	 *            日期的格式。
	 * @return
	 */
	static public Date convertS2Date(String text, String pattern) {
		if (text == null || pattern == null || "".equals(text.trim())) {
			return null;
		}
		if (text.length() < pattern.length()) {
			pattern = pattern.substring(0, text.length());
		}
		DateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(text);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	/**
	 * 把Date类型的日期转换为yyyy-MM-dd HH:mm:ss格式的字符串。
	 * 
	 * @param date
	 * @return
	 */
	static public String convertD2String(Date date) {
		if (date == null)
			return null;
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 把Date类型的日期转换为yyyyMMddHHmmss格式的字符串。
	 * 
	 * @param date
	 * @return
	 */
	static public String convertDate2Str(Date date) {
		if (date == null)
			return null;
		String pattern = "yyyyMMddHHmmss";
		DateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 把Date类型的日期转换为yyyy-MM-dd格式的字符串。
	 * 
	 * @param date
	 * @return
	 */
	static public String convertD2Str(Date date) {
		if (date == null)
			return null;
		String pattern = "yyyy-MM-dd";
		DateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 把Date类型的日期转换为yyyy-MM格式的字符串。
	 * 
	 * @param date
	 * @return
	 */
	static public String convertD2s(Date date) {
		if (date == null)
			return null;
		String pattern = "yyyy-MM";
		DateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 把Date类型的日期转换为yyyy-MM-dd HH:mm:ss格式的字符串，并只取指定的长度。
	 * 
	 * @param date
	 * @return
	 */
	static public String convertD2String(Date date, int length) {
		if (date == null)
			return null;
		String pattern = "yyyy-MM-dd HH:mm:ss";
		if (length < 0 || length > pattern.length()) {
			throw new IllegalArgumentException("日期格式的长度不正确");
		}
		DateFormat formatter = new SimpleDateFormat(pattern
				.substring(0, length));
		return formatter.format(date);
	}

	/**
	 * 把Date类型的日期转换为指定格式的字符串。
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	static public java.sql.Timestamp setTimestampHHMM(Date date, String hhmm) {
		if (date == null)
			return null;

		String strDate = DateConvert.convertD2String(date, "yyyy-MM-dd");
		if (!(hhmm == null || "".equals(hhmm.trim()))) {
			strDate += ' ' + hhmm.trim();
		}
		if (strDate.length() < 19) {
			strDate += "0000-00-00 00:00:00".substring(strDate.length());
		}
		return java.sql.Timestamp.valueOf(strDate);
	}

	/**
	 * 获取目标时间与源时间相隔的天数,如20050901与20050930相隔29天
	 * 
	 * @param start
	 *            格式要求yyyy-MM-dd
	 * @param end
	 *            格式要求yyyy-MM-dd
	 * @return
	 */
	public static final long HOUR_MILLSECONDS = 3600 * 1000;
	public static final long DAY_MILLSECONDS = 24 * HOUR_MILLSECONDS;

	public static int getIntervalDays(Date start, Date end) {
		start = convertS2Date(convertD2String(start, "yyyy-MM-dd"), "yyyy-MM-dd");
		end = convertS2Date(convertD2String(end, "yyyy-MM-dd"), "yyyy-MM-dd");
		long startMills = start.getTime();
		long endMills = end.getTime();
		int offset = 0;

		offset = (int) ((endMills - startMills) / DAY_MILLSECONDS);

		return offset;
	}

	/**
	 * 获取日期加天数
	 * 
	 * @return
	 */
	public static Date dateAddDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	/**
	 * 获取当前日期相隔天数的日期字符串"yyyy-mm-dd"格式显示
	 * 
	 * @return
	 */
	public static String getDateByNow(int days) {

		return convertD2String(dateAddDay(new Date(), days), "yyyy-mm-dd");
	}
	
	/**
	 * 获取date所在的自然周的周一
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
    	c.setFirstDayOfWeek(Calendar.MONDAY);
    	c.setTime(date);
    	c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
    	c.set(Calendar.HOUR_OF_DAY, 0);
    	c.set(Calendar.MINUTE, 0);
    	c.set(Calendar.SECOND, 0);
    	return c.getTime();
	}
	
	/**
	 * 获取date所在的自然周的周日
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
    	c.setFirstDayOfWeek(Calendar.MONDAY);
    	c.setTime(date);
    	c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
    	c.set(Calendar.HOUR_OF_DAY, 23);
    	c.set(Calendar.MINUTE, 59);
    	c.set(Calendar.SECOND, 59);
    	return c.getTime();
	}
	
	/**
	 * 获取date所在月份的1号那天的date
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar c = new GregorianCalendar();
    	c.setTime(date);
    	c.set(GregorianCalendar.DAY_OF_MONTH, 1); 
    	return c.getTime();
	}
	
	/**
	 * 获取date所在月份的最后一天的date
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set( Calendar.DATE, 1 );
		c.roll(Calendar.DATE, - 1 );
    	return c.getTime();
	}

	/**
	 * 获取时间段内每个周一的Date
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static List<Date> getFirstDateOfWeekList(Date beginDate, Date endDate) {
		List<Date> list = new ArrayList<Date>();
		Date firstDateOfWeek = getFirstDateOfWeek(beginDate);
		Date lastDate = getLastDateOfWeek(endDate);
		
		Date temp = firstDateOfWeek;
		while (temp.compareTo(lastDate) < 0) {
			list.add(temp);
			temp = dateAddDay(temp, 7);
		}
		return list;
	}
	
	public static Date getNextMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}
	
	public static void main(String[] args) {
		Date temp = convertS2Date("2011-12-20 11:25:54", "yyyy-MM-dd HH:mm:ss");
		Date end = convertS2Date("2011-12-29 10:45:25", "yyyy-MM-dd HH:mm:ss");
		Date date = getLastDateOfWeek(temp);
		System.out.println(convertD2String(date, "yyyy-MM-dd HH:mm:ss")); 
		
		System.out.println(getIntervalDays(temp, end));
		/*List<Date> list = getFirstDateOfWeekList(temp, end);
		for (Date date : list) {
			System.out.println(convertD2String(date, "yyyy-MM-dd")); 
		}*/
		/*Date firstDateOfWeek = getFirstDateOfWeek(temp);
		Date lastDateOfWeek = getLastDateOfWeek(temp);
		Date dateAddDay = dateAddDay(temp, 7);
		System.out.println("firstDateOfWeek:" + convertD2String(firstDateOfWeek, "yyyy-MM-dd"));  
		System.out.println("lastDateOfWeek:" + convertD2String(lastDateOfWeek, "yyyy-MM-dd"));  
		System.out.println("dateAddDay:" + convertD2String(dateAddDay, "yyyy-MM-dd"));*/  
	}

}