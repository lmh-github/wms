package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.Wares;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.vo.SkuVo;

public interface WaresService {
	/**
	 * 取商品列表.
	 */
	List<Wares> getWaresList(Map<String, Object> criteria,Page page);

	/**
	 * 取商品列表总数.
	 */
	Integer getWaresListTotal(Map<String, Object> criteria);
	
	/**
	 * 取所有的商品.
	 */
	List<Wares> getWaresList(Map<String, Object> criteria);
	
	/**
	 * 取指定的商品信息.
	 */
	Wares getWares(Long waresId);
	
	/**
	 * 取指定的商品属性信息(属性集、属性、可选项).
	 */
	Wares getWaresWithAttrInfo(Long waresId);

	/**
	 * 添加商品.
	 */
	void addWares(Wares wares) throws ServiceException;

	/**
	 * 更新商品.
	 */
	void updateWares(Wares wares) throws ServiceException;

	/**
	 * 删除商品.
	 */
	void deleteWares(Long waresId) throws ServiceException;

	/**
	 * 添加SKU信息.
	 */
	void addSku(Sku sku) throws ServiceException;

	/**
	 * 更新SKU.
	 */
	void updateSku(Sku sku) throws ServiceException;

	/**
	 * 删除SKU及其与可选项的关联关系.
	 */
	void deleteSku(Long skuId) throws ServiceException;

	/**
	 * 取sku列表
	 */
	List<Sku> getSkuList(Map<String, Object> criteria, Page page);
	
	/**
	 * 取带属性信息的sku列表
	 */
	List<Sku> getSkuWithAttrList(Map<String, Object> criteria, Page page);
	
	/**
	 * 取只要满足任何一个条件的SKU列表(逻辑或)
	 * @param sku
	 * @return
	 */
	List<Sku> getAnySkuList(Map<String, Object> criteria);

	/**
	 * 根据商品ID获取SKU列表
	 */
	List<SkuVo> querySkuListByWaresId(Map<String, Object> criteria)throws ServiceException;
	
	/**
	 * 根据商品Code获取SKU列表
	 */
	List<SkuVo> querySkuListByWaresCode(Map<String, Object> criteria)throws ServiceException;
	
	/**
	 * 取SKU列表总数.
	 */
	Integer getSkuListTotal(Map<String, Object> criteria);
	
	/**
	 * 取带属性信息的SKU列表总数.
	 */
	Integer getSkuWithAttrListTotal(Map<String, Object> criteria);
	
	/**
	 * 根据ERP物料编码批量取SKU列表
	 */
	List<Sku> getSkuListByMaterialCodes(List<String> materialCodes);


	/**
	 * 取指定的sku信息
	 */
	Sku getSku(Long id);
	
	/**
	 * 根据sku编码取对应SKU
	 */
	Sku getSkuByCode(String skuCode);
	
	/**
	 * 根据sku条形码取对应SKU
	 */
	Sku getSkuByBarcode(String skuBarcode);
	
	/**
	 * 根据sku名称取对应SKU
	 */
	Sku getSkuByName(String skuName);
	
	/**
	 * 判断SKU是否存在身份码管理
	 */
	Boolean indivCodeEnabled(Long skuId)throws ServiceException;
	
}
