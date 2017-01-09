package com.gionee.top.util;

import java.io.Reader;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 此类中封装一些常用的字符串操作。 所有方法都是静态方法，不需要生成此类的实例， 
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * 
 * @since 0.1
 */
public class StringUtils
{
    private static final Log log = LogFactory.getLog(StringUtils.class);

    private StringUtils()
    {
    }
    
    public static int string2Int(String value,int def)
    {
        try
        {
            if(null!=value&&!"".equals(value))
            {
                return Integer.parseInt(value);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return def;
    }
    
    /** 
     * 数据库Clob对象转换为String 
     */  
    public static String clob2String(Clob clob)  
    {  
        try  
        {  
            // 以 java.io.Reader 对象形式（或字符流形式）  
            //检索此 Clob 对象指定的 CLOB 值 --Clob的转换  
            Reader inStreamDoc = clob.getCharacterStream();  
            // 取得clob的长度  
            char[] tempDoc = new char[(int) clob.length()];  
            inStreamDoc.read(tempDoc);  
            inStreamDoc.close();  
            return new String(tempDoc);  
        }  
        catch (Exception e)  
        {  
            log.equals(e);  
        }  
        return null;  
    } 
    
    /**
     * 返回整形数的指定长度，指定填充因子的字符串
     *
     * @param number,指定整形数
     * @param destLength指定长度
     * @param paddedChar指定填充因子
     * @return 如果该整形数长度大于指定长度。截到一部分，如果小于指定长度，左填充指定填充因子
     */
    public static String formatNumber(int number, int destLength,
            char paddedChar)
    {
        String oldString = String.valueOf(number);
        StringBuffer newString = new StringBuffer("");
        int oldLength = oldString.length();
        if (oldLength > destLength)
        {
            newString.append(oldString.substring(oldLength - destLength));
        } else if (oldLength == destLength)
        {
            newString.append(oldString);
        } else
        {
            for (int i = 0; i < destLength - oldLength; i++)
            {
                newString.append(paddedChar);
            }
            newString.append(oldString);
        }
        return newString.toString();
    }

    /**
     * 检查是否为手机号码(13开头的11位数字字串)
     * @param sMobile 被检查的字串
     * @return
     */
    public static boolean isMobileNumber(String sMobile)
    {
        if (sMobile == null)
        {
            return false;
        }
        return java.util.regex.Pattern.matches(" *1\\d{10} *", sMobile);
    }

    /**
     * 检查是否为移动手机号码(13开头的11位数字字串)
     * @param sMobile 被检查的字串
     * @return
     */
    public static boolean isGmccMobile(String sMobile)
    {
        if (sMobile == null)
        {
            return false;
        }
        return java.util.regex.Pattern.matches(" *13[4-9]\\d{8} *", sMobile);
    }

    /**
     * 检查是否为联通手机号码(13开头的11位数字字串)
     * @param sMobile 被检查的字串
     * @return
     */
    public static boolean isUnicomMobile(String sMobile)
    {
        if (sMobile == null)
        {
            return false;
        }
        return java.util.regex.Pattern.matches(" *13[0-3]\\d{8} *", sMobile);
    }

    /**
     * 检查是否为数字字符串
     * @param sMobile 被检查的字串
     * @return
     */
    public static boolean isNumber(String str)
    {
        if (str == null)
        {
            return false;
        }
        return java.util.regex.Pattern.matches(" *\\d+ *", str);
    }

    /**
     * 字符分拆方法，类似String.split()
     * @param str
     * @param div
     * @return
     */
    static public String[] splitByChar(String str, char div)
    {
        if (str == null)
        {
            return null;
        } else if (str.indexOf(div) == -1)
        {
            return new String[] { str };
        } else
        {
            java.util.Collection values = new java.util.ArrayList();
            int sBeginNo = 0;
            int sEndNo = str.indexOf(div);
            while (sEndNo != -1)
            {
                values.add(str.substring(sBeginNo, sEndNo));
                sBeginNo = sEndNo + 1;
                sEndNo = str.indexOf(div, sBeginNo);
            }
            if (sBeginNo < str.length())
            {
                values.add(str.substring(sBeginNo));
            }
            String[] retArray = new String[values.size()];
            values.toArray(retArray);
            return retArray;
        }
    }

    /**
     * 使用正则表达式的字符串替换
     * @param src
     * @param patternStr 含正则式的被替换字符串
     * @param replacement 替换的字串
     * @return
     */
    static public String regReplace(String src, String patternStr,
            String replacement)
    {
        //System.out.println("替换：[" + src + "],[" + patternStr + "],[" + replacement + "]");
        if (src == null || patternStr == null || replacement == null)
        {
            return src;
        }
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(src);
        return matcher.replaceAll(replacement);
    }

    /**
     * 字符串替换，不用正则式。
     * @param src
     * @param seachWord 被替换字符串
     * @param replacement 替换的字串
     * @return
     */
    static public String strReplace(String src, String seachWord,
            String replacement)
    {
        if (src == null || seachWord == null || src.indexOf(seachWord) == -1)
        {
            return src;
        }
        StringBuffer temp = new StringBuffer(src.length() + 100);
        int fromPos = 0;
        int findPos = -1;
        int wordLen = seachWord.length();
        while ((findPos = src.indexOf(seachWord, fromPos)) != -1)
        {
            temp.append(src.substring(fromPos, findPos)).append(replacement);
            fromPos = findPos + wordLen;
        }
        if (fromPos < src.length())
        {
            temp.append(src.substring(fromPos));
        }
        return temp.toString();
    }

    /**
     * 使用正则表达式的字符串替换.替换{0},{1},...
     * @param src String
     * @param replacements String[]
     * @return String
     */
    static public String regReplace(String src, String[] replacements)
    {
        if (src == null || replacements == null || replacements.length == 0)
        {
            return src;
        }
        int len = replacements.length;
        String result = src;
        for (int i = 0; i < len; i++)
        {
            String irepValue = replacements[i];
            if (irepValue == null)
            {
                irepValue = "null";
            }
            result = regReplace(result, "#" + i + '#', irepValue);
        }

        return result;
    }

    /**
     * 字符串替换，不用正则式。
     * @param src
     * @param seachWord 被替换字符串
     * @param replacement 替换的字串
     * @return
     */
    static public String strReplace(String src, String[] replacements)
    {
        if (src == null || replacements == null || replacements.length == 0)
        {
            return src;
        }
        int len = replacements.length;
        String result = src;
        for (int i = 0; i < len; i++)
        {
            String irepValue = replacements[i];
            if (irepValue == null)
            {
                irepValue = "null";
            }
            result = strReplace(result, "#" + i + '#', irepValue);
        }

        return result;
    }

    /**
     * 取指定长度的字符串,位数不足则前面补0，如位数过长，则只取后len位
     * @param iStr 初始数字
     * @param len 长度
     * @return
     */
    static public String fillZeroStr(int iStr, int len)
    {
        if (len <= 0)
        {
            return "";
        }

        String str = String.valueOf(iStr);
        int slen = String.valueOf(iStr).length();
        if (len < slen)
        {
            return str.substring(slen - len);
        }

        StringBuffer temp = new StringBuffer(len);
        for (int i = slen; i < len; i++)
        {
            temp.append('0');
        }
        temp.append(iStr);
        return temp.toString();
    }

    /**
     * 截掉字符串中的空格
     */
    static public String cutSpace(String src)
    {
        if (src == null || src.length() == 0)
        {
            return src;
        }
        // 英文空格
        String temp = regReplace(src, " ", "");
        // 中文空格
        temp = regReplace(temp, "　", "");
        return temp;
    }

    /**
     * 使用正则表达式的字符串替换
     * @param src
     * @param patternStr 含正则式的被替换字符串
     * @param replacement 替换的字串
     * @return
     */
    static public String trimAll(String src)
    {
        if (src == null || "".equals(src))
        {
            return src;
        }

        String temp = regReplace(src, " ", "");
        temp = regReplace(temp, " ", "");
        return temp;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位.（现在默认取2位小数）
     * @return 四舍五入后的结果
     */
    public static float round(double v)
    {
        java.math.BigDecimal b = new java.math.BigDecimal(Double.toString(v));
        java.math.BigDecimal one = new java.math.BigDecimal("1");
        return (b.divide(one, 2, java.math.BigDecimal.ROUND_HALF_UP))
                .floatValue();
    }

    /**
     * 解决从AJAX调用两次escape编辑后的字符问题。用于解决中文乱码
     * @param src
     * @return
     */
    public static String unescape(String src)
    {
        if (src == null || "".equals(src.trim()))
        {
            return src;
        }
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0;
        int pos = 0;
        char ch;
        while (lastPos < src.length())
        {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos)
            {
                if (src.charAt(pos + 1) == 'u')
                {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else
                {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else
            {
                if (pos == -1)
                {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else
                {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * 将汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     * @param s 原字符串
     * @return 重新编码后的字串
     */
    public static String toUTF8String(String s)
    {
        if (s == null)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255)
            {
                sb.append(c);
            } else
            {
                byte[] b;
                try
                {
                    b = Character.toString(c).getBytes("UTF-8");
                } catch (Exception ex)
                {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++)
                {
                    int k = b[j];
                    if (k < 0)
                    {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 替换SQL语句模糊搜索中的特殊字符。
     * @param src
     * @return
     */
    public static String maskSpeChar4SqlLike(String src)
    {
        if (src == null || "".equals(src.trim()))
        {
            return src;
        }
        src = StringUtils.strReplace(src, "'", "_");
        src = StringUtils.strReplace(src, "?", "_");
        src = StringUtils.strReplace(src, "%", "_");
        src = StringUtils.strReplace(src, "^", "_");
        return src;
    }

    /**
     * 判断是否是手机号码
     * @param mobile
     * @return
     */
    public static String formatUid2Mobile(String mobile)
    {
        if (mobile != null && mobile.length() > 10)
        {
            mobile = mobile.substring(mobile.length() - 11);
            if (!StringUtils.isMobileNumber(mobile))
            {
                mobile = null;
            } else
            {
                mobile = null;
            }
        } else
        {
            mobile = null;
        }
        return mobile;
    }

    /**
     * 生成指定长度的随机字符串
     * @param length
     * @return
     */
    public static final String randomString(int length)
    {

        Random randGen = null;
        char[] numbersAndLetters = null;
        Object initLock = new Object();

        if (length < 1)
        {
            return null;
        }
        if (randGen == null)
        {
            synchronized (initLock)
            {
                if (randGen == null)
                {
                    randGen = new Random();
                    numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
                            + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                            .toCharArray();
                }
            }
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++)
        {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
            // randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
        }
        return new String(randBuffer);
    }
    
    /**
     * 根据分隔符将字符串解析成map
     * 如：a=1,b=2
     * @param str
     * @param regex
     * @return
     */
	public static Map<String, String> parseStrByRegex(String str, String regex)
	{
		Map<String, String> strMap = new HashMap<String, String>();
		String[] params =  str.split(regex);
    	for(String key : params)
    	{
    		String[] keys = key.split("=");
    		strMap.put(keys[0], keys[1]);
    	}
		return strMap;
	}
	
	/**
	 * 根据分隔符将字符串解析成map
	 * @param str
	 * @param attrRegex 属性间的分隔符
	 * @param kvRegex key、value间的分隔符
	 * @return
	 */
	public static Map<String, String> parseStrByRegex(String str, String attrRegex, String kvRegex) {
		Map<String, String> strMap = new HashMap<String, String>();
		String[] attrStrings =  str.split(attrRegex);
    	for(String attrStr : attrStrings)
    	{
    		String[] keyValue = attrStr.split(kvRegex);
    		strMap.put(keyValue[0], keyValue[1]);
    	}
		return strMap;
	}
	
	/**
	 * 根据分隔符将字符串解析成Object
	 * @param str
	 * @param attrRegex 属性间的分隔符
	 * @param kvRegex key、value间的分隔符
	 * @param cls
	 * @return
	 * @throws Exception 
	 */
	public static Object parseToObject(String str, String attrRegex, String kvRegex, Class<?> cls) throws Exception{
		Object obj = cls.newInstance();
		String[] attrStrings =  str.split(attrRegex);
    	for(String attrStr : attrStrings) {
    		String[] keyValue = attrStr.split(kvRegex);
    		BeanUtil.beanRegister(obj, keyValue[0], keyValue[1]);
    	}
		return obj;
	}
	
	/**
     * 转换
     * @param str String
     * @param defValue int
     * @return float
     */
    static public double parseDouble(String str ,double defValue) {
        if(str==null || "".equals(str.trim())) {
            return defValue ;
        }

        str = str.trim() ;
        try {
            return Double.parseDouble(str) ;
        } catch (NumberFormatException ex) {
            return defValue;
        }
   }
    
    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位.（现在默认取2位小数）
     * @return 四舍五入后的结果
     */
    public static double round(double v ,int scale){
        java.math.BigDecimal b = new java.math.BigDecimal(Double.toString(v));
        java.math.BigDecimal one = new java.math.BigDecimal("1");
        return (b.divide(one ,scale ,java.math.BigDecimal.ROUND_HALF_UP)).doubleValue() ;
    }

    public static void main(String args[])
    {
        System.out.println(StringUtils.formatNumber(1, 12, '5'));
        System.out.println(StringUtils.randomString(32));
    }
    
    
}
