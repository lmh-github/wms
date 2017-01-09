/*
 * @(#)ResetJHBatchCodeTask.java 2014-1-7
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.job;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每天凌晨重置拣货批次号的后缀为"001"，位置文件jhBatchCode.txt
 * @author ZuoChangjun 2014-1-7
 */
public class ResetJHBatchCodeTask {
	private static Logger logger = LoggerFactory
			.getLogger(ResetJHBatchCodeTask.class);

	public void execute() {

		try {
			logger.info("重置拣货批次号服务正在启动...");
			BufferedWriter writer = null;
			try {
				String basePath = ResetJHBatchCodeTask.class.getResource("/").toString();
				System.out.println("basePath="+basePath+"jhBatchCode.txt");
				// int endIndex=basePath.indexOf("WEB-INF");//file:/D:/Tomcat/webapps/wms/WEB-INF/classes/
				// if(endIndex==-1){
				// endIndex=basePath.indexOf("target");//兼容用Eclipse中的Jetty插件跑项目
				// }
				int beginIndex = "file:".length();
				basePath = basePath.substring(beginIndex);// 去掉前缀file:/
				writer = new BufferedWriter(new FileWriter(basePath + "jhBatchCode.txt"));
				writer.write("001");
				System.out.println("重置拣货批次号服务启动成功");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(writer!=null){
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}
			logger.info("重置拣货批次号服务启动成功");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("重置拣货批次号服务启动异常", e);
		}
	
	}
}
