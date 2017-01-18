package com.gionee.wms.facade;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.facade.dto.StockInfoDTO;
import com.gionee.wms.facade.result.QueryStockResult;
import com.gionee.wms.facade.result.WmsResult.WmsCodeEnum;
import com.gionee.wms.service.stock.StockService;

@Component("stockManager")
public class StockManagerImpl implements StockManager {
	private static Logger logger = LoggerFactory.getLogger(StockManagerImpl.class);
	private StockService stockService;
	private JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);

	@Override
	public QueryStockResult queryStock(String warehouseCode, String skuCode) {
		long tempTimePoint = System.currentTimeMillis();
		QueryStockResult result = new QueryStockResult();
		// 参数验证
		WmsCodeEnum validateResult = validateQueryStockParam(warehouseCode, skuCode);
		if (WmsCodeEnum.SUCCESS.getCode().equals(validateResult.getCode())) {
			try {
				//取库存信息
				Stock stock = stockService.getStock(warehouseCode, skuCode);
				if(stock!=null){
					StockInfoDTO stockInfo = new StockInfoDTO();
					stockInfo.setStockId(stock.getId());
					stockInfo.setWarehouseCode(stock.getWarehouse().getWarehouseCode());
					stockInfo.setSkuCode(stock.getSku().getSkuCode());
					stockInfo.setSalesQuantity(stock.getSalesQuantity()+stock.getNonDefectivePl());//可销售库存=账面可销售库存+盘点良品盈亏
					stockInfo.setUnsalesQuantity(stock.getUnsalesQuantity()+stock.getDefectivePl());//不可销售库存=账面不可销售库存+盘点次品盈亏
					stockInfo.setMeasureUnit(stock.getSku().getWares().getMeasureUnit());
					result.setCode(WmsCodeEnum.SUCCESS.getCode());
					result.setStockInfoDTO(stockInfo);
				}else{
					result.setResult(WmsCodeEnum.STOCK_NOT_EXISTS.getCode());
				}
			} catch (Exception e) {
				logger.error("query stock error", e);
				result.setResult(WmsCodeEnum.SYSTEM_ERROR.getCode());
			}
		} else {
			result.setResult(validateResult.getCode());
		}

		return result;
	}

	private WmsCodeEnum validateQueryStockParam(String warehouseCode, String skuCode) {
		if (StringUtils.isBlank(warehouseCode)) {
			return WmsCodeEnum.PARAM_WAREHOUSE_CODE_NULL;
		}
		if (StringUtils.isBlank(skuCode)) {
			return WmsCodeEnum.PARAM_SKU_CODE_NULL;
		}
		return WmsCodeEnum.SUCCESS;
	}

	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

}
