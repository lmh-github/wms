/**
 * @(#) Editor.java Created on 2014年8月12日
 *      Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.wms.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAXB工具，通过使用注解，实现java对象与XML文件的相互转换
 * @author dujz
 * @version 1.0
 */
public class JaxbUtil {
	private static Logger logger = LoggerFactory.getLogger(JaxbUtil.class);
	private static ConcurrentHashMap<Class<?>, JAXBContext> jaxbContextMap = new ConcurrentHashMap<Class<?>, JAXBContext>();

	/**
	 * 安全的对象转XML字符串
	 * @param tclz
	 * @param t
	 */
	public static <T> String safeMarshToXmlBinding(Class<T> tclz, T t) {
		if (tclz == null || t == null) {
			return null;
		}

		try {
			return marshToXmlBinding(tclz, t);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MessageFormat.format("对象：{0}，转XML字符串出现异常！", tclz.getName()));
			return null;
		}
	}

	/**
	 * 将JAXB实现对象转换成XML格式的字符串
	 * @param <T> 这里的类是具体类，不能是接口
	 * @param tclz 转换对象实例
	 * @return
	 */
	public static <T> String marshToXmlBinding(Class<T> tclz, T t) throws JAXBException {
		JAXBContext jc = null;
		if (jaxbContextMap.get(tclz) == null) {
			Map<String, String> properties = new HashMap<String, String>();
			jc = JAXBContext.newInstance(new Class<?>[] { tclz }, properties);
			jaxbContextMap.put(tclz, jc);
		} else {
			jc = jaxbContextMap.get(tclz);
		}

		Marshaller u = jc.createMarshaller();
		// XML内容格式化
		u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		u.marshal(t, sw);
		return sw.toString();
	}

	/**
	 * 将XML格式的字符串转换成JAXB实现对象
	 * @param <T> 这里的类是具体类，不能是接口
	 * @param tclz
	 * @param file 当目录存在文件不存在时，自动创建；当目录不存在时，将抛异常
	 * @return
	 */
	public static <T> void marshToXmlBinding(Class<T> tclz, T t, File file) throws JAXBException {
		if (tclz == null || file == null) {
			return;
		}

		JAXBContext jc = null;
		if (jaxbContextMap.get(tclz) == null) {
			Map<String, String> properties = new HashMap<String, String>();
			jc = JAXBContext.newInstance(new Class<?>[] { tclz }, properties);
			jaxbContextMap.put(tclz, jc);
		} else {
			jc = jaxbContextMap.get(tclz);
		}

		Marshaller u = jc.createMarshaller();
		u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		u.marshal(t, file);
	}

	/**
	 * 将XML格式的字符串转换成JAXB实现对象
	 * @param <T> 这里的类是具体类，不能是接口
	 * @param tclz
	 * @param xmlstr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshToObjBinding(Class<T> tclz, String xmlstr) {
		try {
			JAXBContext jc = null;
			if (jaxbContextMap.get(tclz) == null) {
				Map<String, String> properties = new HashMap<String, String>();
				jc = JAXBContext.newInstance(new Class<?>[] { tclz }, properties);
				jaxbContextMap.put(tclz, jc);
			} else {
				jc = jaxbContextMap.get(tclz);
			}

			Unmarshaller un = jc.createUnmarshaller();
			return (T) un.unmarshal(new ByteArrayInputStream(xmlstr.getBytes("utf-8")));

		} catch (JAXBException e) {
			logger.error("转换对象失败", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("转换对象失败", e);
		}
		return null;
	}

	/**
	 * 将XML格式的文件转换成JAXB实现对象
	 * @param <T>
	 * @param tclz
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshToObjBinding(Class<T> tclz, File file) {
		if (tclz == null || file == null || !file.exists()) {
			return null;
		}

		try {
			JAXBContext jc = null;
			if (jaxbContextMap.get(tclz) == null) {
				Map<String, String> properties = new HashMap<String, String>();
				jc = JAXBContext.newInstance(new Class<?>[] { tclz }, properties);
				jaxbContextMap.put(tclz, jc);
			} else {
				jc = jaxbContextMap.get(tclz);
			}

			Unmarshaller un = jc.createUnmarshaller();
			return (T) un.unmarshal(file);

		} catch (JAXBException e) {
			logger.error("转换对象失败", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshToObjBinding(Class<T> tclz, InputStream inputStream) {
		if (tclz == null || inputStream == null) {
			return null;
		}

		try {
			JAXBContext jc = null;
			if (jaxbContextMap.get(tclz) == null) {
				Map<String, String> properties = new HashMap<String, String>();
				jc = JAXBContext.newInstance(new Class<?>[] { tclz }, properties);
				jaxbContextMap.put(tclz, jc);
			} else {
				jc = jaxbContextMap.get(tclz);
			}

			Unmarshaller un = jc.createUnmarshaller();
			return (T) un.unmarshal(inputStream);

		} catch (JAXBException e) {
			logger.error("转换对象失败", e);
		}
		return null;
	}

}
