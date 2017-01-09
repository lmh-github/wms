package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SkuAttr;
import com.gionee.wms.service.ServiceException;

public interface SkuAttrService {
	/**
	 * 取属性列表.
	 */
	List<SkuAttr> getAttrList(Map<String, Object> criteria);
	
	/**
	 * 取指定的属性信息(属性、可选项).
	 */
	SkuAttr getAttr(Long attrId);

	/**
	 * 添加属性信息(属性、可选项).
	 */
	void addAttr(SkuAttr skuAttr) throws ServiceException;

	/**
	 * 更新属性信息(属性、可选项).
	 */
	void updateAttr(SkuAttr skuAttr) throws ServiceException;

	/**
	 * 删除属性.
	 */
	void deleteAttr(Long attrId) throws ServiceException;
	
	
}
