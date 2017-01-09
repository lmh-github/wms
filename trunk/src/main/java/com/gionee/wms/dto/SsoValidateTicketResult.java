package com.gionee.wms.dto;

import java.io.Serializable;

public class SsoValidateTicketResult implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -6536239785274733476L;
	private String code;
	private String tgt;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTgt() {
		return tgt;
	}

	public void setTgt(String tgt) {
		this.tgt = tgt;
	}

}