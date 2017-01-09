package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.AttrSet;
import com.gionee.wms.service.ServiceException;

public interface AttrSetService {
	/**
	 * 取商品类型列表.
	 */
	List<AttrSet> getAttrSetList(Map<String, Object> criteria);

	/**
	 * 取指定的商品类型.
	 */
	AttrSet getAttrSet(Long id);

	/**
	 * 添加商品类型.
	 */
	void addAttrSet(AttrSet attrSet) throws ServiceException;

	/**
	 * 更新商品类型.
	 */
	void updateAttrSet(AttrSet attrSet) throws ServiceException;

	/**
	 * 删除商品类型.
	 */
	void deleteAttrSet(Long id) throws ServiceException;
}
