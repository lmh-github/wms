package com.gionee.wms.common.excel.excelexport.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapKeyUtil
{
	private MapKeyUtil(){};
	
	/**
	 * 将Map<key, value>中的key全部转换成大写
	 */
	public static Map<String, String> keyToUpper(Map<String, String> map)
	{
		if(map == null || map.size() <= 0)
		{
			return null;
		}
		Set<String> keys = map.keySet();
		if(keys == null || keys.size() <= 0)
		{
			return null;
		}
		Map<String, String> rtnMap = new HashMap<String, String>();
		for(String key : keys)
		{
			String value = map.get(key);
			rtnMap.put(key.toUpperCase(), value);
		}
		return rtnMap;
	}
	
	/**
	 * 将Map<key, value>中的key全部转换成大写
	 */
	public static List<Map<String, String>> keyToUpper(List<Map<String, String>> listMap)
	{
		if(listMap == null || listMap.size() <= 0)
		{
			return null;
		}
		List<Map<String, String>> rtnListMap = new ArrayList<Map<String,String>>();
		for(Map<String, String> map : listMap)
		{
			if(map == null || map.size() <= 0)
			{
				continue;
			}
			rtnListMap.add(keyToUpper(map));
		}
		return rtnListMap;
	}
}