package com.gionee.top.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanUtil
{
    private static final Log log = LogFactory.getLog(BeanUtil.class);
    
    /**
     * 设置bean属性
     * @param object
     * @param fieldName
     * @param fieldValue
     */
    public static void beanRegister(Object object, String fieldName, Object fieldValue)
    {
        try
        {
            Class clazz = object.getClass();
           //忽略对象属性大小写
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields)
            {
                String name = field.getName();
                if(name.equalsIgnoreCase(fieldName))
                {
                    fieldName = name;
                    break;
                }
            }
            Field field = clazz.getDeclaredField(fieldName);
            String methodName = "set"
                    + Character.toUpperCase(fieldName.charAt(0))
                    + fieldName.substring(1);
            String fieldTypeName = field.getType().getName();
            Object param = new Object();
            if(fieldValue == null)
            	return;
            if ("java.lang.String".equals(fieldTypeName))
            {
                param = fieldValue;
            }
            if ("int".equals(fieldTypeName)
                    || "java.lang.Integer".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    fieldValue = "0";
                }
                param = Integer.valueOf(String.valueOf(fieldValue));
            }
            if ("long".equals(fieldTypeName)
                    || "java.lang.Long".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    fieldValue = "0";
                }
                param = Long.valueOf(String.valueOf(fieldValue));
            }
            if ("float".equals(fieldTypeName)
                    || "java.lang.Float".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    fieldValue = "0";
                }
                param = Float.valueOf(String.valueOf(fieldValue));
            }
            if ("double".equals(fieldTypeName)
                    || "java.lang.Double".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    fieldValue = "0";
                }
                param = Double.valueOf(String.valueOf(fieldValue));
            }
            if ("java.math.BigDecimal".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    fieldValue = "0";
                }
                param = Long.valueOf(String.valueOf(fieldValue));
            }
            if ("boolean".equals(fieldTypeName)
                    || "java.lang.Boolean".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    fieldValue = "0";
                }
                param = (Boolean)fieldValue;
            }
            
            if ("java.util.Date".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    param = new Date();
                }  if (fieldValue instanceof java.sql.Timestamp) {
                	java.sql.Timestamp timestamp = (java.sql.Timestamp)fieldValue;
                	param = new Date(timestamp.getTime());
                } else
                {
            		param = DateConvert.convertS2Date(String.valueOf(fieldValue));
                }
            }
            
            if ("java.sql.Timestamp".equals(fieldTypeName))
            {
                if ("".equals(fieldValue))
                {
                    param = DateConvert.getCurTimestamp();
                } else {
                	param = fieldValue;
                }
            }
            if("java.sql.Clob".equals(fieldTypeName))
            {
                param = (java.sql.Clob)fieldValue;
            }
            Method method = clazz.getMethod(methodName, field.getType());
            method.invoke(object, new Object[] { param });
        } catch (Exception e)
        {
            log.error("BeanRegister",e);
        }
    }
    
}
