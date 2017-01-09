package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.OrderItem;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.StockOut;
import com.gionee.wms.entity.StockOutItem;

@BatisDao
public interface StockOutDao {
	/**
	 * 添加销售订单
	 */
	int addSalesOrder(SalesOrder salesOrder);

	/**
	 * 添加销售订单明细
	 */
	int addSalesOrderDetail(List<OrderItem> orderDetail);

	/**
	 * 添加出库单明细
	 */
	int addStockOutDetail(List<StockOutItem> stockOutDetail);

	// /**
	// * 按条件查询销售订单出库明细
	// */
	// List<OrderItem> querySalesOrderOutDetail(Map<String, Object> criteria);

	/**
	 * 添加出库单
	 */
	int addStockOut(StockOut stockOut);

	/**
	 * 批量加入销售订单到出库单
	 */
	int addSalesOrdersToStockOut(Map<String, Object> criteria);

	/**
	 * 查询出库单列表
	 */
	List<StockOut> queryStockOutList(Map<String, Object> criteria);

	/**
	 * 查询出库单列表总数
	 */
	int queryStockOutListTotal(Map<String, Object> criteria);

	/**
	 * 更新出库单
	 */
	int updateStockOut(StockOut stockOut);

	/**
	 * 同步订单明细到出库明细中
	 */
	int copyOrderDetail(Map<String, Object> criteria);

	/**
	 * 查询出库明细列表
	 */
	List<StockOutItem> queryStockOutDetailList(Map<String, Object> criteria);
	
	/**
	 * 查询出库明细列表总数
	 */
	int queryStockOutDetailListTotal(Map<String, Object> criteria);

	/**
	 * 添加指定的出库明细项
	 */
	int addStockOutItem(StockOutItem stockOutItem);
	
	/**
	 * 更新指定的出库明细项
	 */
	int updateStockOutItem(StockOutItem stockOutItem);

	/**
	 * 删除出库单
	 */
	int deleteStockOut(Long id);
}
