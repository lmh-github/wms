package com.gionee.wms.facade;

import com.gionee.wms.facade.result.QueryStockResult;

public interface StockManager {
	/**
	 * 查询库存
	 * 
	 * @param skuCode SKU编码
	 * @param warehouseCode 库存编号
	 * @return
	 */
	QueryStockResult queryStock(String warehouseCode, String skuCode);
}
