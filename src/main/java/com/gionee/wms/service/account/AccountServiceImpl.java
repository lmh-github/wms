package com.gionee.wms.service.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.dto.Menu;
import com.gionee.wms.dto.ShiroUser;
import com.gionee.wms.entity.User;
import com.gionee.wms.service.ServiceException;
import com.google.common.collect.Lists;

/**
 * 账号管理类.
 * 
 * @author kevin
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
	private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	public User findUserByLoginName(String loginName) {
		// TODO:远程取用户信息，下为测试数据
		User user = new User();
		user.setId(1L);
		user.setLoginName("testtest");
		user.setPassword("670b14728ad9902aecba32e22fa4f6bd");

		return user;
	}

	public ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

	public void authenticate(String loginName, String password) {
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
		try {
			user.login(token);
			ShiroUser shiroUser = getCurrentUser();
			Menu menu = getMenusByUserId(shiroUser.getId());
			shiroUser.setMenu(menu);
		} catch (DisabledAccountException e) {
			throw new ServiceException("用户已被禁用.", e);
		} catch (IncorrectCredentialsException e) {
			throw new ServiceException("密码错误.", e);
		} catch (AuthenticationException e) {
			throw new ServiceException("登录失败，请重试.", e);
		} finally {
			token.clear();
		}
	}

	public boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}

	public Menu getMenusByUserId(Long userId) {
		Menu rootMenu = new Menu();
		rootMenu.setId(Menu.ROOT_ID);
		rootMenu.setName(Menu.ROOT_NAME);
		List<Menu> permissions = getPermissions(userId);
		generateTree(permissions, rootMenu);
		return rootMenu;
	}

	/**
	 * 递归取子节点
	 * 
	 * @param allMenu
	 * @param parentMenu
	 */
	private void generateTree(List<Menu> allMenu, Menu parentMenu) {
		for (Menu menu : allMenu) {
			if (menu.getParent_id() == parentMenu.getId()) {
				parentMenu.addChildMenu(menu);
				generateTree(allMenu, menu);
			}
		}
	}

	public List<String> getPermissionList(Long userId) {
		List<String> permissionList = new ArrayList<String>();
		List<Menu> permissions = getPermissions(userId);
		for (Menu menu : permissions) {
			permissionList.add(menu.getName());
		}
		return permissionList;
	}

	// 远程取权限
	public List<Menu> getPermissions(Long userId) {
		// TODO:远程取用户权限信息，下为测试数据
		List menus = new ArrayList();

		 
		List<Menu> child = Lists.newArrayList();
		Menu menu1 = new Menu();

		menu1.setId(1L);
		menu1.setName("库存管理");
		menu1.setParent_id(0L);
		child = Lists.newArrayList();
		menu1.setChild(child);
		menus.add(menu1);

		Menu menu2 = new Menu();
		menu2.setId(11L);
		menu2.setName("库存信息");
		menu2.setParent_id(1L);
		menu2.setPath("/stock/stock.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(371L);
		menu2.setName("库存流水");
		menu2.setParent_id(1L);
		menu2.setPath("/stock/stockChange.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(37L);
		menu2.setName("库存盘点");
		menu2.setParent_id(1L);
		menu2.setPath("/stock/stockCheck.action");
		child.add(menu2);

		menu1 = new Menu();
		menu1.setId(2L);
		menu1.setName("报表管理");
		menu1.setParent_id(0L);
		child = Lists.newArrayList();
		menu1.setChild(child);
		menus.add(menu1);

		
		menu2 = new Menu();
		menu2.setId(22L);
		menu2.setName("采购进货汇总");
		menu2.setParent_id(2L);
		menu2.setPath("/report/purchaseInReport!listSummary.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(21L);
		menu2.setName("采购进货明细");
		menu2.setParent_id(2L);
		menu2.setPath("/report/purchaseInReport!listDetail.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(23L);
		menu2.setName("销售出库汇总");
		menu2.setParent_id(2L);
		menu2.setPath("/report/salesOutReport!listSummary.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(24L);
		menu2.setName("销售出库明细");
		menu2.setParent_id(2L);
		menu2.setPath("/report/salesOutReport!listDetail.action");
		child.add(menu2);
		

		menu1 = new Menu();
		menu1.setId(3L);
		menu1.setName("商品管理");
		menu1.setParent_id(0L);
		child = Lists.newArrayList();
		menu1.setChild(child);
		menus.add(menu1);


		menu2 = new Menu();
		menu2.setId(31L);
		menu2.setName("商品分类");
		menu2.setParent_id(3L);
		menu2.setPath("/wares/category.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(311L);
		menu2.setName("商品类型");
		menu2.setParent_id(3L);
		menu2.setPath("/wares/attrSet.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(312L);
		menu2.setName("基本商品");
		menu2.setParent_id(3L);
		menu2.setPath("/wares/wares.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(313L);
		menu2.setName("商品SKU");
		menu2.setParent_id(3L);
		menu2.setPath("/wares/sku.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(42L);
		menu2.setName("商品个体");
		menu2.setParent_id(3L);
		menu2.setPath("/wares/indiv.action");
		child.add(menu2);

		menu1 = new Menu();
		menu1.setId(36L);
		menu1.setName("入库管理");
		menu1.setParent_id(0L);
		child = Lists.newArrayList();
		menu1.setChild(child);
		menus.add(menu1);

		
		menu2 = new Menu();
		menu2.setId(43L);
		menu2.setName("入库单列表");
		menu2.setParent_id(36L);
		menu2.setPath("/stock/stockIn.action");
		child.add(menu2);

		menu2 = new Menu();
		menu2.setId(12L);
		menu2.setName("采购入库单");
		menu2.setParent_id(36L);
		menu2.setPath("/stock/stockIn!inputPurchaseIn.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(41L);
		menu2.setName("退货入库单");
		menu2.setParent_id(36L);
		menu2.setPath("/stock/rmaIn!inputRmaIn.action");
		child.add(menu2);
		
		menu1 = new Menu();
		menu1.setId(39L);
		menu1.setName("出库管理");
		menu1.setParent_id(0L);
		child = Lists.newArrayList();
		menu1.setChild(child);
		menus.add(menu1);

		
		menu2 = new Menu();
		menu2.setId(40L);
		menu2.setName("出库单列表");
		menu2.setParent_id(39L);
		menu2.setPath("/stock/stockOut.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(45L);
		menu2.setName("销售订单");
		menu2.setParent_id(39L);
		menu2.setPath("/stock/salesOrder.action");
		child.add(menu2);


		menu1 = new Menu();
		menu1.setId(38L);
		menu1.setName("基本资料");
		menu1.setParent_id(0L);
		child = Lists.newArrayList();
		menu1.setChild(child);
		menus.add(menu1);


		menu2 = new Menu();
		menu2.setId(12L);
		menu2.setName("仓库资料");
		menu2.setParent_id(38L);
		menu2.setPath("/basis/warehouse.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(12L);
		menu2.setName("供应商资料");
		menu2.setParent_id(38L);
		menu2.setPath("/basis/supplier.action");
		child.add(menu2);
		
		menu2 = new Menu();
		menu2.setId(12L);
		menu2.setName("配送资料");
		menu2.setParent_id(38L);
		menu2.setPath("/basis/shipping.action");
		child.add(menu2);
		
		menu1 = new Menu();
		menu1.setId(50L);
		menu1.setName("系统管理");
		menu1.setParent_id(0L);
		child = Lists.newArrayList();
		menu1.setChild(child);
		menus.add(menu1);


		menu2 = new Menu();
		menu2.setId(501L);
		menu2.setName("操作日志");
		menu2.setParent_id(50L);
		menu2.setPath("/account/opLog.action");
		child.add(menu2);
		return menus;
	}

	@Override
	public boolean isPermitted(String permission) {
		return SecurityUtils.getSubject().isPermitted(permission);
//		return true;
	}

	public static void main(String[] args) {
		AccountService accountService = new AccountServiceImpl();
		Menu menu = accountService.getMenusByUserId(100L);
		JsonUtils jsonUtils = new JsonUtils();
		System.out.println(jsonUtils.toJson(menu));
		String[] s = StringUtils.splitByWholeSeparator("abbcbd", "bb");
		System.out.println(s.toString());
		System.out.println(false || false);
	}

}
