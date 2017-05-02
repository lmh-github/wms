package com.gionee.wms.dto;

import java.io.Serializable;

public class DataDescription implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String zipCode;
	private String encryptCode;
	private String codeType;
	
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getEncryptCode() {
		return encryptCode;
	}
	public void setEncryptCode(String encryptCode) {
		this.encryptCode = encryptCode;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	
}
