package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.entity.StockIn;
import com.gionee.wms.entity.StockInItem;

@BatisDao
public interface StockInDao {
	/**
	 * 添加入库单
	 */
	int addStockIn(StockIn stockIn);

	/**
	 * 添加初始化的入库单
	 */
	int addInitialStockIn(StockIn stockIn);

	/**
	 * 添加入库单明细
	 */
	int addStockInDetail(List<StockInItem> stockInDetail);

	/**
	 * 添加指定的入库明细项
	 */
	int addStockInItem(StockInItem stockInItem);

	/**
	 * 更新指定的入库明细项
	 */
	int updateStockInItem(StockInItem stockInItem);
	
	/**
	 * 删除指定入库编号的所有明细项
	 */
	int deleteStockInItems(String stockInCode);
	
	/**
	 * 删除指定入库明细项
	 */
	int deleteStockInItem(Long id);

	/**
	 * 更新入库单
	 */
	int updateStockIn(StockIn stockIn);

	/**
	 * 删除入库单
	 */
	int deleteStockIn(Long id);

	/**
	 * 查询入库单头部信息列表
	 */
	List<StockIn> queryStockInList(Map<String, Object> criteria);

	/**
	 * 查询入库单头部信息列表总数
	 */
	int queryStockInListTotal(Map<String, Object> criteria);

	// /**
	// * 查询指定的入库信息及明细
	// */
	// StockIn queryStockInWithDetail(Long stockInId);

	/**
	 * 分页查询入库明细列表
	 */
	List<StockInItem> queryStockInDetailList(Map<String, Object> criteria);
	
	/**
	 * 查询入库明细列表总数
	 */
	int queryStockInDetailListTotal(Map<String, Object> criteria);
	
	/**
	 * 分页取入库汇总列表
	 */
	List<ReceiveSummary> queryStockInSummaryList(Map<String, Object> criteria);

	/**
	 * 查询入库汇总列表总数
	 */
	int queryStockInSummaryListTotal(Map<String, Object> criteria);

}
