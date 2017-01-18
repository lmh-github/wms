package com.gionee.wms.web.action.basis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.PermissionConstants;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.web.AccessException;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("WarehouseAction")
@Scope("prototype")
public class WarehouseAction extends CrudActionSupport<Warehouse> {
	private static final long serialVersionUID = -7962875699740595307L;

	private WarehouseService warehouseService;
	private AccountService accountService;

	/** 页面相关属性 **/
	private List<Warehouse> warehouseList;
	private Long id;
	private Warehouse warehouse;
	private Page page = new Page();

	/**
	 * 查询列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		warehouseList = warehouseService.getWarehouseList(criteria);
		return SUCCESS;
	}

	/**
	 * 打开创建或编辑页面
	 */
	@Override
	public String input() throws Exception {
		return INPUT;
	}

	/**
	 * 创建
	 */
	@Override
	public String add() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.WAREHOUSE_ADD)) {
			throw new AccessException();
		}
		try {
			warehouseService.addWarehouse(warehouse);
			ajaxSuccess("创建成功");
		} catch (ServiceException e) {
			logger.error("创建仓库时出错", e);
			ajaxError("创建失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新
	 */
	@Override
	public String update() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.WAREHOUSE_EDIT)) {
			throw new AccessException();
		}
		try {
			warehouseService.updateWarehouse(warehouse);
			ajaxSuccess("编辑成功");
		} catch (ServiceException e) {
			logger.error("编辑仓库时出错", e);
			ajaxError("编辑失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 设置默认仓库
	 */
	public String setDefault() throws Exception {
		Validate.notNull(id);
		try {
			warehouseService.updateWarehouseToDefault(id);
			ajaxSuccess("设置成功");
		} catch (ServiceException e) {
			logger.error("设置默认仓库时出错", e);
			ajaxError("设置失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 停用仓库
	 */
	public String disable() throws Exception {
		Validate.notNull(id);
		try {
			warehouseService.disableWarehouse(id);
			ajaxSuccess("停用成功");
		} catch (ServiceException e) {
			logger.error("停用仓库时出错", e);
			ajaxError("停用失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 启用仓库
	 */
	public String enable() throws Exception {
		Validate.notNull(id);
		try {
			warehouseService.enableWarehouse(id);
			ajaxSuccess("启用成功");
		} catch (ServiceException e) {
			logger.error("启用仓库时出错", e);
			ajaxError("启用失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除
	 */
	@Override
	public String delete() throws Exception {
		// try {
		// warehouseService.deleteWarehouse(id);
		// ajaxSuccess("商品删除成功");
		// } catch (ServiceException e) {
		// logger.error("商品删除时出错", e);
		// ajaxError("商品删除失败：" + e.getMessage());
		// }
		return null;
	}

	@Override
	public void prepareList() throws Exception {
		// 初始化属性对象
		warehouse = new Warehouse();

	}

	// 为input方法准备数据
	@Override
	public void prepareInput() throws Exception {
		if (id != null) {
			warehouse = warehouseService.getWarehouse(id);
		} else {
			warehouse = new Warehouse();
		}
		// 初始化商品类型列表
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		warehouse = new Warehouse();
	}

	// 为update方法准备数据
	@Override
	public void prepareUpdate() throws Exception {
		warehouse = new Warehouse();
	}

	// ModelDriven接口方法
	@Override
	public Warehouse getModel() {
		return warehouse;
	}

	// 供页面取值
	public Page getPage() {
		if (page == null) {
			page = new Page();
		}
		return page;
	}

	// 供页面传值
	public void setId(Long id) {
		this.id = id;
	}

	// 供页面取值
	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	@Autowired
	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

}
