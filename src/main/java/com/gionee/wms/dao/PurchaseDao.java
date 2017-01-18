package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Purchase;
import com.gionee.wms.entity.PurchaseGoods;

@BatisDao
public interface PurchaseDao {
	/**
	 * 分页查询符合条件的采购单列表.
	 */
	List<Purchase> queryPurchaseByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的采购单总数
	 */
	int queryPurchaseTotal(Map<String, Object> criteria);

	/**
	 * 根据ID获取采购单
	 */
	Purchase queryPurchase(Long id);

	/**
	 * 根据采购编号获取采购单
	 */
	Purchase queryPurchaseByPurchaseCode(String purchaseCode);

	/**
	 * 添加采购单
	 */
	int addPurchase(Purchase purchase);

	/**
	 * 更新采购单
	 */
	int updatePurchase(Purchase purchase);

	/**
	 * 添加采购商品清单
	 */
	int addGoodsList(List<PurchaseGoods> goodsList);

	/**
	 * 根据采购单ID取对应的商品清单
	 */
	List<PurchaseGoods> queryGoodsListByPurchaseId(Long purchaseId);
}
