package com.gionee.wms.dto;

import java.io.Serializable;

public class SpMContactsPhoneValue implements Serializable {
	private static final long serialVersionUID = 5593378195003911466L;
	private String phoneType;
	private String phoneLabel;
	private String phoneNumber;

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneLabel() {
		return phoneLabel;
	}

	public void setPhoneLabel(String phoneLabel) {
		this.phoneLabel = phoneLabel;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}