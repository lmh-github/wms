package com.gionee.wms.service.account;

import java.util.List;

import com.gionee.wms.dto.Menu;
import com.gionee.wms.dto.ShiroUser;
import com.gionee.wms.entity.User;

public interface AccountService {
	/**
	 * 查找用户.
	 */
	User findUserByLoginName(String loginName);

	/**
	 * 取当前用户.
	 */
	ShiroUser getCurrentUser();

	/**
	 * 取用户菜单树.
	 */

	Menu getMenusByUserId(Long userId);

	/**
	 * 取权限列表.
	 */
	List<String> getPermissionList(Long userId);
	
	/**
	 * 取权限列表
	 */
	List<Menu> getPermissions(Long userId);

	/**
	 * 判断是否已经过认证.
	 */
	boolean isAuthenticated();

	/**
	 * 登录认证
	 */
	void authenticate(String loginName, String password);
	
	/**
	 * 判断用户是否具有权限
	 * @param permission 权限，如"增加分类"
	 * @return
	 */
	boolean isPermitted(String permission);

}
