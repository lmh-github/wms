package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.TransferGoods;
import com.gionee.wms.entity.TransferPartner;

@BatisDao
public interface TransferDao {

	int getTransferListTotal(Map<String, Object> criteria);

	List<Transfer> getTransferList(Map<String, Object> criteria);
	
	int getTransferListTotalSf(Map<String,Object> criteria);
	List<Transfer> getTransferListSf(Map<String,Object> criteria);
	List<Transfer> getTransferSfList(Map<String,Object> criteria);

	Transfer getTransferById(Long transferId);
	
	Transfer getTransferSfById(Long transferId);
	
	List<Transfer> getTransfered();
	List<Transfer> getTransferPush();

	void addTransfer(Transfer transfer);
	
	int addTransferSf(Transfer transfer);
	
	List<Transfer> getImeiSf(@Param("param1") String transferId, @Param("param2") String skuCode);
	
	void batchAddTransferGoods(List<TransferGoods> goods);
	
	int updateTransferSf(Transfer transfer);

	void updateTransfer(Transfer transfer);

	void deleteTransfer(Long transferId);

	void addTransferGoods(TransferGoods goods);
	
	List<TransferGoods> getTransferGoodsForPrep(Long transferId);
	
	List<TransferGoods> getTransferGoodsForView(Long transferId);

	List<TransferGoods> getTransferGoodsById(Long transferId);

	void deleteTransferGoods(Long transferId);

	void deleteGoodsById(Long goodsId);

	List<TransferGoods> getTransferGoods(Map<String, Object> params);

	void updateTransferGoods(Map<String, Object> params);

	List<TransferPartner> getTransferPartnerList(Map<String, Object> criteria);
	
	/**
	 * 导出调拨单
	 * @param criteria
	 * @return
	 */
	List<Map<String,Object>> exportTransferList(Map<String, Object> criteria);
	
}
