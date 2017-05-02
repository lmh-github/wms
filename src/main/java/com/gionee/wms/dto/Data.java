package com.gionee.wms.dto;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class Data implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataDescription dataDescription;
	private String content;
	
	public DataDescription getDataDescription() {
		return dataDescription;
	}
	public void setDataDescription(DataDescription dataDescription) {
		this.dataDescription = dataDescription;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
