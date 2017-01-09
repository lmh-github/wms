package com.gionee.wms.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class MyCollectionUtils {

	/**
	 * 判断集合中元素是否存在空值（元素本身不能为集合）
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean noBlankElements(final Collection collection) {
		try {
			Validate.noNullElements(collection);
		} catch (IllegalArgumentException e) {
			return false;
		}
		for (Object o : collection) {
			if (String.class.isInstance(o) && StringUtils.isBlank(String.valueOf(o))) {
				return false;
			}
			if (Collection.class.isInstance(o)) {
				throw new IllegalArgumentException();
			}
		}
		return true;
	}

	/**
	 * 提取集合中的对象属性, 组合成List.
	 * 
	 * @param collection 集合.
	 * @param propertyName 属性名.
	 */
	public static List collectionElementPropertyToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
					|| e instanceof NoSuchMethodException) {
				throw new IllegalArgumentException(e);
			} else if (e instanceof InvocationTargetException) {
				throw new RuntimeException(((InvocationTargetException) e).getTargetException());
			} else if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		}

		return list;
	}

	public static void main(String[] args) {
		List list = Lists.newArrayList("test", "a", " ");
		System.out.println(MyCollectionUtils.noBlankElements(list));

	}
}
