package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.StockIn;
import com.gionee.wms.entity.StockInItem;
import com.gionee.wms.service.ServiceException;

public interface StockInService {
	// /**
	// * 添加入库信息.
	// */
	// void addStockIn(StockIn stockIn) throws ServiceException;
	/**
	 * 确认采购入库
	 */
	void confirmStockIn(StockIn stockIn) throws ServiceException;
	
	/**
	 * 确认退货入库
	 */
	void confirmRmaIn(StockIn stockIn) throws ServiceException;

	/**
	 * 删除指定入库编号的入库单
	 */
	void deleteStockIn(String stockInCode) throws ServiceException;

	/**
	 * 删除指定的入库单
	 */
	void deleteStockIn(Long id) throws ServiceException;

	/**
	 * 取入库单列表.
	 */
	List<StockIn> getStockInList(Map<String, Object> criteria, Page page);

	/**
	 * 取入库单列表总数.
	 */
	Integer getStockInListTotal(Map<String, Object> criteria);

	// /**
	// * 取指定的入库单及明细.
	// */
	// StockIn getStockInWithDetail(Long stockInId);

	/**
	 * 取指定的入库单.
	 */
	StockIn getStockIn(Long stockInId);

	/**
	 * 根据编码取入库单
	 */
	StockIn getStockInByCode(String stockInCode);

	/**
	 * 取指定的入库明细.
	 */
	List<StockInItem> getStockInDetail(Long stockInId);

	/**
	 * 分页取入库明细列表.
	 */
	List<StockInItem> getStockInDetailList(Map<String, Object> criteria, Page page);
	
	/**
	 * 分页取入库汇总列表
	 */
	List<ReceiveSummary> getStockInSummaryList(Map<String, Object> criteria, Page page) throws ServiceException;

	/**
	 * 取入库汇总列表总数
	 */
	Integer getStockInSummaryListTotal(Map<String, Object> criteria);
	
	/**
	 * 取入库明细列表总数.
	 */
	Integer getStockInDetailListTotal(Map<String, Object> criteria);
	
	/**
	 * 取指定的入库明细项.
	 */
	StockInItem getStockInItem(Long stockInItemId);

//	/**
//	 * 为指定的入库单添加入库明细项
//	 */
//	void addStockInItem(StockInItem stockInItem) throws ServiceException;
	
	/**
	 * 为指定的退货入库单添加入库明细项
	 */
	void addRmaInItem(StockInItem stockInItem) throws ServiceException;

	/**
	 * 添加入库明细项及商品身份信息
	 */
	void addPurchaseInItem(StockInItem stockInItem, List<IndivFlow> indivList) throws ServiceException;
	
//	/**
//	 * 添加退货入库明细项及商品身份信息
//	 */
//	void addRmaInItemAndIndivs(StockInItem stockInItem, List<IndivFlow> indivList) throws ServiceException;

	/**
	 * 更新指定的入库单明细项
	 */
	void updateStockInItem(StockInItem stockInItem) throws ServiceException;

	/**
	 * 更新入库明细项及商品身份信息
	 */
	void updateStockInItemAndIndivs(StockInItem stockInItem, List<IndivFlow> indivList) throws ServiceException;

	/**
	 * 删除指定的入库单明细项及相关商品个体流转信息
	 */
	void deleteStockInItem(Long stockInItemId) throws ServiceException;
	
	/**
	 * 添加退货商品个体
	 */
	void addRmaIndiv(IndivFlow indivFlow)throws ServiceException;
	
	
//	/**
//	 * 批量添加商品个体流转信息
//	 */
//	void addIndivFlows(List<IndivFlow> indivFlows) throws ServiceException;
//	
//	
//	
//	/**
//	 * 删除与指定入库明细项相关联的商品个体流转信息
//	 */
//	void deleteIndivFlowByInItemId(Long stockInItemId) throws ServiceException;

}
