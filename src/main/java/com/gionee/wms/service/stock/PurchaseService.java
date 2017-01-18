package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.PurPreRecvGoods;

public interface PurchaseService {
	/**
	 * 分页取采购预收单
	 */
	List<PurPreRecv> getPurPreRecvList(Map<String, Object> criteria, Page page);

	/**
	 * 取采购预收单总数
	 */
	int getPurPreRecvTotal(Map<String, Object> criteria);

	/**
	 * 取采购预收单
	 */
	PurPreRecv getPurPreRecv(Long id);

	/**
	 * 根据过账凭证号取采购预收单
	 */
	PurPreRecv getPurPreRecvByPostingNo(String postingNo);

	/**
	 * 取采购预收单ID取对应的商品清单
	 */
	List<PurPreRecvGoods> getPurPreRecvGoodsList(Long purPreRecvId);

	/**
	 * 添加采购预收单和预收商品清单
	 */
	void addPreRecvWithGoodsList(PurPreRecv purPreRecv, List<PurPreRecvGoods> goodsList);
}
