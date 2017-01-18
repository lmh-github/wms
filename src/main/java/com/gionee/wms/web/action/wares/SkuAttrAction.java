package com.gionee.wms.web.action.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.entity.AttrSet;
import com.gionee.wms.entity.SkuAttr;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.wares.AttrSetService;
import com.gionee.wms.service.wares.SkuAttrService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("SkuAttrAction")
@Scope("prototype")
public class SkuAttrAction extends CrudActionSupport<SkuAttr> {

	private static final long serialVersionUID = -8940733721506429911L;

	private SkuAttrService skuAttrService;
	private AttrSetService attrSetService;
	private AccountService accountService;

	/** 页面相关属性 **/
	private List<SkuAttr> skuAttrList;
	private Long id;
	private SkuAttr skuAttr;
	private List<AttrSet> attrSetList;

	/**
	 * 查询分类列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("attrSetId", skuAttr.getAttrSet().getId());
		skuAttrList = skuAttrService.getAttrList(criteria);
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
	 * 创建分类
	 */
	@Override
	public String add() throws Exception {
		if (CollectionUtils.isEmpty(skuAttr.getItemList())) {
			logger.error("创建属性时出错：属性可选项不能为空");
			ajaxError("创建属性失败：属性可选项不能为空");
			return null;
		}
		try {
			skuAttrService.addAttr(skuAttr);
			ajaxSuccess("创建属性成功");
		} catch (ServiceException e) {
			logger.error("创建属性出错", e);
			ajaxError("创建属性失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新分类
	 */
	@Override
	public String update() throws Exception {
		if (CollectionUtils.isEmpty(skuAttr.getItemList())) {
			logger.error("编辑属性时出错：属性可选项不能为空");
			ajaxError("编辑属性失败：属性可选项不能为空");
			return null;
		}
		try {
			skuAttrService.updateAttr(skuAttr);
			ajaxSuccess("编辑属性成功");
		} catch (ServiceException e) {
			logger.error("编辑属性时出错", e);
			ajaxError("编辑属性失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除分类
	 */
	@Override
	public String delete() throws Exception {
		try {
			skuAttrService.deleteAttr(id);
			ajaxSuccess("删除属性成功");
		} catch (ServiceException e) {
			logger.error("删除属性时出错", e);
			ajaxError("删除属性失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public void prepareList() throws Exception {
		//初始化属性对象
		skuAttr = new SkuAttr();
		AttrSet attrSet = new AttrSet();
		skuAttr.setAttrSet(attrSet);
		//初始化商品类型列表
		attrSetList = attrSetService.getAttrSetList(null);
	}

	// 为input方法准备数据
	@Override
	public void prepareInput() throws Exception {
		if (id != null) {
			skuAttr = skuAttrService.getAttr(id);
		} else {
			skuAttr = new SkuAttr();
			AttrSet attrSet = new AttrSet();
			skuAttr.setAttrSet(attrSet);
		}
		//初始化商品类型列表
		attrSetList = attrSetService.getAttrSetList(null);
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		skuAttr = new SkuAttr();
	}

	// 为update方法准备数据
	@Override
	public void prepareUpdate() throws Exception {
		skuAttr = new SkuAttr();
	}

	// ModelDriven接口方法
	@Override
	public SkuAttr getModel() {
		return skuAttr;
	}

	// 供页面传值
	public void setId(Long id) {
		this.id = id;
	}

	// 供页面取值
	public List<SkuAttr> getSkuAttrList() {
		return skuAttrList;
	}

	// 供页面取值
	public List<AttrSet> getAttrSetList() {
		return attrSetList;
	}

	@Autowired
	public void setSkuAttrService(SkuAttrService skuAttrService) {
		this.skuAttrService = skuAttrService;
	}

	@Autowired
	public void setAttrSetService(AttrSetService attrSetService) {
		this.attrSetService = attrSetService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

}
