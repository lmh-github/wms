package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.SkuBomDetail;
import com.gionee.wms.entity.Wares;
import com.gionee.wms.vo.SkuVo;

@BatisDao
public interface WaresDao {
	/**
	 * 添加商品信息
	 */
	int addWares(Wares wares);

	/**
	 * 更新商品信息
	 */
	int updateWares(Wares wares);

	/**
	 * 删除商品信息
	 */
	int deleteWares(Long id);

	/**
	 * 查询商品信息
	 */
	Wares queryWares(Long id);

	/**
	 * 查询商品信息列表
	 */
	List<Wares> queryWaresList(Map<String, Object> criteria);

	/**
	 * 查询商品总数
	 */
	int queryWaresListTotal(Map<String, Object> criteria);

	/**
	 * 查询商品属性信息(属性集、属性、可选项)
	 */
	Wares queryWaresWithAttrInfo(Map<String, Object> criteria);

	/**
	 * 添加sku信息
	 */
	int addSku(Sku sku);

	/**
	 * 批量添加SKU与可选项的关联条目
	 */
	int addSkuItemRelation(Map<String, Object> skuItems);

	/**
	 * 更新SKU信息
	 */
	int updateSku(Sku sku);

	/**
	 * 删除SKU信息
	 */
	int deleteSku(Long id);

	/**
	 * 删除SKU与可选项关联关系
	 */
	int deleteSkuItemRelation(Long skuId);

	/**
	 * 查询sku列表
	 */
	List<Sku> querySkuList(Map<String, Object> criteria);

	/**
	 * 查询带属性信息sku列表
	 */
	List<Sku> querySkuWithAttrList(Map<String, Object> criteria);

	/**
	 * 查询sku列表总数
	 */
	int querySkuListTotal(Map<String, Object> criteria);

	/**
	 * 查询带属性信息sku列表总数
	 */
	int querySkuWithAttrListTotal(Map<String, Object> criteria);

	/**
	 * 查询只要符合任何一个条件的SKU列表
	 */
	List<Sku> queryAnySkuList(Map<String, Object> criteria);
	
	/**
	 * 查询指定的sku
	 */
	Sku querySku(Long id);
	
	/**
	 * 根据SKU编码取SKU
	 */
	Sku querySkuBySkuCode(String skuCode);
	
	/**
	 * 根据sku编码批量获取SKU
	 */
	List<Sku> querySkuListByCodes(List<String> skuCodes);
	
	/**
	 * 根据ERP物料编码批量获取SKU
	 */
	List<Sku> querySkuListByMaterialCodes(List<String> materialCodes);
	
	/**
	 * 根据sku ID批量获取SKU
	 */
	List<Sku> querySkuListByIds(List<Long> skuIds);
	
	/**
	 * 根据商品ID获取SKU列表
	 */
	List<SkuVo> querySkuListByWaresId(Map<String, Object> criteria);
	
	/**
	 * 根据商品Code获取SKU列表
	 */
	List<SkuVo> querySkuListByWaresCode(Map<String, Object> criteria);
	
	/**
	 * 批量添加组合SKU
	 * @param skuItems
	 * @return
	 */
	int addSkuBomDetailRelation(Map<String,Object> skuItems);
	
	/**
	 * 删除组合SKU
	 * @param skuCode
	 * @return
	 */
	int deleteSkuBomDetailRelation(String skuCode);
	
	/**
	 * 查询组合SKU
	 * @param skuCode
	 * @return
	 */
	List<SkuBomDetail> selectSkuBomDetailRelation(String skuCode);

}
