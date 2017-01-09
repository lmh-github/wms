package com.gionee.wms.dto;

import java.io.Serializable;
import java.util.List;

public class SsoLoginCheckResult implements Serializable {
	private static final long serialVersionUID = -3047864262158409409L;
	private String code;
	private String id;
	private String type;
	private String username;
	private List<String> permission;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getPermission() {
		return permission;
	}

	public void setPermission(List<String> permission) {
		this.permission = permission;
	}

}