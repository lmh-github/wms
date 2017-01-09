package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SkuAttr;
import com.gionee.wms.entity.SkuItem;

@BatisDao
public interface SkuAttrDao {
	/**
	 * 查询SKU属性列表
	 * 
	 * @param criteria 查询条件
	 * @return
	 */
	List<SkuAttr> queryAttrList(Map<String, Object> criteria);

	/**
	 * 添加属性信息.
	 */
	int addAttr(SkuAttr skuAttr);

	/**
	 * 更新属性信息.
	 */
	int updateAttr(SkuAttr skuAttr);

	/**
	 * 删除属性.
	 */
	int deleteAttr(Long attrId);

	/**
	 * 添加可选项
	 */
	int addItem(List<SkuItem> itemList);

	/**
	 * 删除可选项
	 */
	int deleteItem(List<Long> itemIdList);
	
	/**
	 * 删除指定属性的所有可选项
	 */
	int deleteItemByAttrId(Long attrId);
}
