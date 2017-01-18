/**
* @author jay_liang
* @date 2013-12-30 下午3:36:56
*/
package com.gionee.wms.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-12-30 下午3:36:56
 * @=======================================
 */
public class SpringPropertyConfigurer extends PropertyPlaceholderConfigurer {

	private static final Logger log = LoggerFactory	.getLogger(SpringPropertyConfigurer.class);
	
	private static Map<String, Object> ctxPropertiesMap;

	@Override
	public void setLocations(Resource[] locations) {
		Map<String, Resource> resourceMap = new HashMap<String, Resource>();
		for (Resource loc : locations) {
			File file = null;
			try {
				file = loc.getFile();
				if (log.isDebugEnabled()) {
					log.debug("old resouce is : " + file.getAbsolutePath());
				}
			} catch (IOException e) {
				log.error("load properties error", e);
			}
			String fileName = file.getName();
			Resource source = resourceMap.get(fileName);
			if (source != null) {
				if (file.getPath().indexOf("classes") > 0) {
					continue;
				}
			}
			resourceMap.put(fileName, loc);
		}

		Resource[] newResouce = resourceMap.values().toArray(
				new Resource[resourceMap.size()]);
		System.out.println("resouce is : " + resourceMap);
		if (log.isDebugEnabled()) {
			log.debug("resouce is : " + resourceMap);
		}
		super.setLocations(newResouce);
	}

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		ctxPropertiesMap = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
			System.setProperty(keyStr, value);
		}
	}

	public static Object getContextProperty(String name) {
		return ctxPropertiesMap.get(name);
	}
}
