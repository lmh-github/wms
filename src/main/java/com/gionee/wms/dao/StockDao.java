package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.DailyStock;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.entity.StockChange;
import com.gionee.wms.entity.StockDelta;

@BatisDao
public interface StockDao {
	/**
	 * 添加库存信息
	 */
	int addStock(Stock stock);

	/**
	 * 分页查询符合条件的库存列表
	 */
	List<Stock> queryStockByPage(Map<String, Object> criteria);

	/**
	 * 查询库存列表总数
	 */
	int queryStockTotal(Map<String, Object> criteria);
	
	/**
	 * 查询符合条件的库存列表
	 */
	List<Stock> queryStockList(Map<String, Object> criteria);

	/**
	 * 查询指定的库存信息
	 */
	Stock queryStock(Map<String, Object> criteria);

	/**
	 * 更新库存数量
	 */
	int updateStockQuantity(Stock stock);

	/**
	 * 添加库存流水
	 */
	// int addStockChange(StockChange stockChange);

	/**
	 * 批量添加库存流水
	 */
	int addStockChange(List<StockChange> stockChanges);

	/**
	 * 查询库存流水列表
	 */
	List<StockChange> queryStockChangeList(Map<String, Object> criteria);

	/**
	 * 查询库存流水列表总数
	 */
	int queryStockChangeListTotal(Map<String, Object> criteria);
	
	/**
	 * 更新库存盘点信息
	 */
	int updateStockCheckInfo(Long checkId);
	
	/**
	 * 根据盘点单ID更新库存盘点信息
	 */
	int updateStockCheckInfoByCheckId(Long checkId);
	
	/**
	 * 更新安全库存数量
	 */
	int updateStockLimit(Stock stock);
	
	/**
	 * 更新库存同步状态
	 */
	void updateStockSyncStatusBySkuIds(Map<String, Object> paramMap);
	
	/**
	 * 插入同步增量数据
	 */
	void insertStockDelta(StockDelta stockDelta);
	
	/**
	 * 获得同步内容
	 */
	List<StockDelta> queryStockDelta();
	
	/**
	 * 删除已同步的增量记录
	 */
	void deleteStockDelta(Map<String, Object> paramMap);

	/**
	 * 新增每日库存信息临时数据
	 */
	void addQty(Map<String, Object> paramMap);

	/**
	 * 新增每日库存信息临时数据 本期出库
	 */
	void addOutQty(Map<String, Object> paramMap);

	/**
	 * 新增每日库存信息数据
	 */
	void addDailyStock(Map<String, Object> paramMap);

	/**
	 * 删除每日库存信息临时数据
	 */
	void deleteDailyStockTemp();

	/**
	 * 获取每日库存信息记录数量
	 */
	Integer queryDailyStockTotalCount(Map<String, Object> criteria);

	/**
	 * 分页查询每日库存信息
	 */
	List<DailyStock> queryDailyStockByPage(Map<String, Object> criteria);

	/**
	 * 查询每日库存信息
	 */
	List<DailyStock> queryDailyStockList();

}
