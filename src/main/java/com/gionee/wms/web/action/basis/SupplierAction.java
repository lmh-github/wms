package com.gionee.wms.web.action.basis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.SupplierService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("SupplierAction")
@Scope("prototype")
public class SupplierAction extends CrudActionSupport<Supplier> {
	private static final long serialVersionUID = -7962875699740595307L;

	private SupplierService supplierService;

	/** 页面相关属性 **/
	private List<Supplier> supplierList;
	private Long id;
	private Supplier supplier;
	private Page page = new Page();

	/**
	 * 查询列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		supplierList = supplierService.getSupplierList(criteria);
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
		try {
			supplierService.addSupplier(supplier);
			ajaxSuccess("创建成功");
		} catch (ServiceException e) {
			logger.error("创建供应商时出错", e);
			ajaxError("创建失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新
	 */
	@Override
	public String update() throws Exception {
		try {
			supplierService.updateSupplier(supplier);
			ajaxSuccess("编辑成功");
		} catch (ServiceException e) {
			logger.error("编辑供应商时出错", e);
			ajaxError("编辑失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 设置默认供应商
	 */
	public String setDefault() throws Exception {
		Validate.notNull(id);
		try {
			supplierService.updateSupplierToDefault(id);
			ajaxSuccess("设置成功");
		} catch (ServiceException e) {
			logger.error("设置默认供应商时出错", e);
			ajaxError("设置失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 停用供应商
	 */
	public String disable() throws Exception {
		Validate.notNull(id);
		try {
			supplierService.disableSupplier(id);
			ajaxSuccess("停用成功");
		} catch (ServiceException e) {
			logger.error("停用供应商时出错", e);
			ajaxError("停用失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 启用供应商
	 */
	public String enable() throws Exception {
		Validate.notNull(id);
		try {
			supplierService.enableSupplier(id);
			ajaxSuccess("启用成功");
		} catch (ServiceException e) {
			logger.error("启用供应商时出错", e);
			ajaxError("启用失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除
	 */
	@Override
	public String delete() throws Exception {
//		try {
//			supplierService.deleteSupplier(id);
//			ajaxSuccess("商品删除成功");
//		} catch (ServiceException e) {
//			logger.error("商品删除时出错", e);
//			ajaxError("商品删除失败：" + e.getMessage());
//		}
		return null;
	}

	@Override
	public void prepareList() throws Exception {
		// 初始化属性对象
		supplier = new Supplier();

	}

	// 为input方法准备数据
	@Override
	public void prepareInput() throws Exception {
		if (id != null) {
			supplier = supplierService.getSupplier(id);
		} else {
			supplier = new Supplier();
		}
		// 初始化商品类型列表
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		supplier = new Supplier();
	}

	// 为update方法准备数据
	@Override
	public void prepareUpdate() throws Exception {
		supplier = new Supplier();
	}

	// ModelDriven接口方法
	@Override
	public Supplier getModel() {
		return supplier;
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
	public List<Supplier> getSupplierList() {
		return supplierList;
	}

	@Autowired
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

}
