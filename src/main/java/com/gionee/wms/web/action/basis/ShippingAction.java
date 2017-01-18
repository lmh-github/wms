package com.gionee.wms.web.action.basis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("ShippingAction")
@Scope("prototype")
public class ShippingAction extends CrudActionSupport<Shipping> {
	private static final long serialVersionUID = -7962875699740595307L;

	private ShippingService shippingService;

	/** 页面相关属性 **/
	private List<Shipping> shippingList;
	private Long id;
	private Shipping shipping;
	private Page page = new Page();

	/**
	 * 查询列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		shippingList = shippingService.getShippingList(criteria);
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
			shippingService.addShipping(shipping);
			ajaxSuccess("创建成功");
		} catch (ServiceException e) {
			logger.error("创建配送方式时出错", e);
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
			shippingService.updateShipping(shipping);
			ajaxSuccess("编辑成功");
		} catch (ServiceException e) {
			logger.error("编辑配送方式时出错", e);
			ajaxError("编辑失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 设置默认配送方式
	 */
	public String setDefault() throws Exception {
		Validate.notNull(id);
		try {
			shippingService.updateShippingToDefault(id);
			ajaxSuccess("设置成功");
		} catch (ServiceException e) {
			logger.error("设置默认配送方式时出错", e);
			ajaxError("设置失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 停用配送方式
	 */
	public String disable() throws Exception {
		Validate.notNull(id);
		try {
			shippingService.disableShipping(id);
			ajaxSuccess("停用成功");
		} catch (ServiceException e) {
			logger.error("停用配送方式时出错", e);
			ajaxError("停用失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 启用配送方式
	 */
	public String enable() throws Exception {
		Validate.notNull(id);
		try {
			shippingService.enableShipping(id);
			ajaxSuccess("启用成功");
		} catch (ServiceException e) {
			logger.error("启用配送方式时出错", e);
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
//			shippingService.deleteShipping(id);
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
		shipping = new Shipping();

	}

	// 为input方法准备数据
	@Override
	public void prepareInput() throws Exception {
		if (id != null) {
			shipping = shippingService.getShipping(id);
		} else {
			shipping = new Shipping();
		}
		// 初始化商品类型列表
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		shipping = new Shipping();
	}

	// 为update方法准备数据
	@Override
	public void prepareUpdate() throws Exception {
		shipping = new Shipping();
	}

	// ModelDriven接口方法
	@Override
	public Shipping getModel() {
		return shipping;
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
	public List<Shipping> getShippingList() {
		return shippingList;
	}

	@Autowired
	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

}
