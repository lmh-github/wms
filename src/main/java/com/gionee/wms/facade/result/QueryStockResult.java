package com.gionee.wms.facade.result;

import com.gionee.wms.facade.dto.StockInfoDTO;

public class QueryStockResult extends WmsResult {
	private static final long serialVersionUID = -8670493745041490966L;
	
	private StockInfoDTO stockInfo;

	public StockInfoDTO getStockInfo() {
		return stockInfo;
	}

	public void setStockInfoDTO(StockInfoDTO stockInfo) {
		this.stockInfo = stockInfo;
	}

}
