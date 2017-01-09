package com.gionee.wms.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.gionee.wms.dto.Menu;
import com.google.common.collect.ImmutableList;

public class User {
	private Long id;
	private String loginName;
	private String name;
	private String plainPassword;
	private String password;
	private String salt;
	private String roles;
	private Date registerDate;

	private List<Menu> permissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Menu> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Menu> permissions) {
		this.permissions = permissions;
	}

	//
	public List<String> getPermissionList() {
		List<String> permissionList = new ArrayList<String>();
		for (Menu menu : permissions) {
			permissionList.add(menu.getName());
		}
		return permissionList;
	}

	// 不持久化到数据库
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public List<String> getRoleList() {
		// 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		return ImmutableList.copyOf(StringUtils.split(roles, ","));
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}