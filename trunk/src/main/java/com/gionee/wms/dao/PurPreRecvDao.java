package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.PurPreRecvGoods;

@BatisDao
public interface PurPreRecvDao {
	/**
	 * 分页查询符合条件的采购预收单列表.
	 */
	List<PurPreRecv> queryPreRecvByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的采购预收单总数
	 */
	int queryPreRecvTotal(Map<String, Object> criteria);

	/**
	 * 根据ID获取采购预收单
	 */
	PurPreRecv queryPurPreRecv(Long id);

	/**
	 * 根据过账凭证号获取采购预收单
	 */
	PurPreRecv queryPurPreRecvByPostingNo(String postingNo);
	
	/**
	 * 根据收货单ID获取采购预收单
	 */
	PurPreRecv queryPurPreRecvByReceiveId(Long receiveId);

	/**
	 * 添加采购预收单
	 */
	int addPreRecv(PurPreRecv purPreRecv);

	/**
	 * 更新采购预收单
	 */
	int updatePurPreRecv(PurPreRecv purPreRecv);

	/**
	 * 添加采购预收商品清单
	 */
	int addGoodsList(List<PurPreRecvGoods> goodsList);

	/**
	 * 根据采购预收单ID取预收商品清单
	 */
	List<PurPreRecvGoods> queryGoodsListByPreRecvId(Long preRecvId);
	
}
