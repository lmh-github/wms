package com.gionee.wms.dto;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Objects;

/**
 * 自定义shiro的 Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {
	private static final long serialVersionUID = -217161377690736728L;
	public Long id;
	public String loginName;
	public String ssoTgt;
	public Menu menu;
	public List<Menu> menuList;
	public String type;
	public List<String> permission;

	public ShiroUser(Long id, String loginName) {
		this.id = id;
		this.loginName = loginName;
	}

	public ShiroUser(String loginName, String ssoTgt) {
		this.loginName = loginName;
		this.ssoTgt = ssoTgt;
	}

	public Long getId() {
		return id;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<String> getPermission() {
		return permission;
	}

	public void setPermission(List<String> permission) {
		this.permission = permission;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSsoTgt() {
		return ssoTgt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setSsoTgt(String ssoTgt) {
		this.ssoTgt = ssoTgt;
	}

	/**
	 * 本方法输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return loginName;
	}

	/**
	 * 重载hashCode,只计算loginName;
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(loginName);
	}

	/**
	 * 重载equals,只计算loginName;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiroUser other = (ShiroUser) obj;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		return true;
	}
}