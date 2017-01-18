package com.gionee.wms.dto;

import java.util.List;

public class SkuStocksDto {
	private List<StockSyncDto> skuStocks;

	public List<StockSyncDto> getSkuStocks() {
		return skuStocks;
	}

	public void setSkuStocks(List<StockSyncDto> skuStocks) {
		this.skuStocks = skuStocks;
	}
	
}
