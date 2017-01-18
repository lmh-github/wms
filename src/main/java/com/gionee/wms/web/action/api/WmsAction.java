package com.gionee.wms.web.action.api;

import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.facade.OrderManager;
import com.gionee.wms.facade.StockManager;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.facade.request.SyncOrderRequest;
import com.gionee.wms.facade.result.CommonResult;
import com.gionee.wms.facade.result.CommonResult.ErrCodeEnum;
import com.gionee.wms.facade.result.CommonResult.RetCodeEnum;
import com.gionee.wms.facade.result.OperateOrderResult;
import com.gionee.wms.facade.result.QueryOrderResult;
import com.gionee.wms.facade.result.QueryStockResult;
import com.gionee.wms.facade.result.WmsResult;
import com.gionee.wms.facade.result.WmsResult.WmsCodeEnum;
import com.opensymphony.xwork2.ActionSupport;

@Controller("WmsAction")
@Scope("prototype")
public class WmsAction extends ActionSupport {
	private static Logger logger = LoggerFactory.getLogger(WmsAction.class);
	private static final long serialVersionUID = 1L;
	private StockManager stockManager;
	private OrderManager orderManager;
	private JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);

	/**
	 * 查询库存接口
	 */
	public String queryStock() throws Exception {
		QueryStockResult result = new QueryStockResult();
		if (ActionUtils.isJsonRequest()) {
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(),WmsConstants.DEFAULT_ENCODING);
			logger.info("WmsAction--queryStock--begin--" + jsonStr);
			Map<String, Object> paramMap = jsonUtils.fromJson(jsonStr, Map.class);
			WmsCodeEnum validateResult = validateQueryStock(paramMap);
			if (WmsCodeEnum.SUCCESS.getCode().equals(validateResult.getCode())) {
				result = stockManager.queryStock(paramMap.get("warehouseCode").toString(), paramMap.get("skuCode")
						.toString());
			} else {
				result.setResult(validateResult.getCode());
			}
		} else {
			result.setResult(WmsCodeEnum.CONTENT_TYPE_ILLEGAL.getCode());
		}

		String jsonString = jsonUtils.toJson(result);
		logger.info("WmsAction--queryStock--end--" + jsonString);
		ActionUtils.outputJson(jsonString);
		return null;
	}

	/**
	 * 同步订单
	 */
	public String syncOrder() throws Exception {
		WmsResult result = new WmsResult();
		if (ActionUtils.isJsonRequest()) {
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(),WmsConstants.DEFAULT_ENCODING);
			logger.info("WmsAction--syncOrder--begin--" + jsonStr);
			SyncOrderRequest request = jsonUtils.fromJson(jsonStr, SyncOrderRequest.class);
			if (request != null) {
				if(StringUtils.isBlank(request.getAppFlag())){
					request.getOrderInfo().setOrderSource(OrderSource.OFFICIAL_GIONEE.getCode());
				}else if(request.getAppFlag().equals("OFFICIAL_GIONEE")){
					request.getOrderInfo().setOrderSource(OrderSource.OFFICIAL_GIONEE.getCode());
				}else if(request.getAppFlag().equals("TMALL_GIONEE")){
					request.getOrderInfo().setOrderSource(OrderSource.TMALL_GIONEE.getCode());
				}else if(request.getAppFlag().equals("OFFICIAL_IUNI")){
					request.getOrderInfo().setOrderSource(OrderSource.OFFICIAL_IUNI.getCode());
				}else if(request.getAppFlag().equals("TMALL_IUNI")){
					request.getOrderInfo().setOrderSource(OrderSource.TMALL_IUNI.getCode());
				}
				result = orderManager.syncOrder(request.getOrderInfo(), request.getTimestamp(), request.getSignature(), request.getOperFlag());
			} else {
				result.setResult(WmsCodeEnum.PARAM_ERROR.getCode());
			}

		} else {
			result.setResult(WmsCodeEnum.CONTENT_TYPE_ILLEGAL.getCode());
		}

		String jsonString = jsonUtils.toJson(result);
		logger.info("WmsAction--syncOrder--end--" + jsonString);
		ActionUtils.outputJson(jsonString);
		return null;
	}

	/**
	 * 同步订单
	 */
	public String syncOrderNew() throws Exception {
		CommonResult result = new CommonResult();
		if (ActionUtils.isJsonRequest()) {
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(),WmsConstants.DEFAULT_ENCODING);
			logger.info("WmsAction--syncOrderNew--begin--" + jsonStr);
			SyncOrderRequest request = jsonUtils.fromJson(jsonStr, SyncOrderRequest.class);
			if (request != null) {
				if(StringUtils.isBlank(request.getAppFlag())){
					request.getOrderInfo().setOrderSource(OrderSource.OFFICIAL_GIONEE.getCode());
				}else if(request.getAppFlag().equals("OFFICIAL_GIONEE")){
					request.getOrderInfo().setOrderSource(OrderSource.OFFICIAL_GIONEE.getCode());
				}else if(request.getAppFlag().equals("TMALL_GIONEE")){
					request.getOrderInfo().setOrderSource(OrderSource.TMALL_GIONEE.getCode());
				}else if(request.getAppFlag().equals("OFFICIAL_IUNI")){
					request.getOrderInfo().setOrderSource(OrderSource.OFFICIAL_IUNI.getCode());
				}else if(request.getAppFlag().equals("TMALL_IUNI")){
					request.getOrderInfo().setOrderSource(OrderSource.TMALL_IUNI.getCode());
				}
				result = orderManager.syncOrderNew(request.getOrderInfo(), request.getOperFlag());
			} else {
				result.setResult(ErrCodeEnum.ERR_PARAM_ERROR.getErr());
			}

		} else {
			result.setResult(ErrCodeEnum.ERR_CONTENT_TYPE_ILLEGAL.getErr());
		}
		
		result.setRet(RetCodeEnum.RET_SUCCESS.getRet());
		String jsonString = jsonUtils.toJson(result);
		logger.info("WmsAction--syncOrderNew--end--" + jsonString);
		ActionUtils.outputJson(jsonString);
		return null;
	}
	
	/**
	 * 取消订单
	 */
	public String cancelOrder() throws Exception {
		WmsResult result = new WmsResult();
		if (ActionUtils.isJsonRequest()) {
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(),WmsConstants.DEFAULT_ENCODING);
			logger.info("WmsAction--cancelOrder--begin--" + jsonStr);
			Map<String, Object> paramMap = jsonUtils.fromJson(jsonStr, Map.class);
			WmsCodeEnum validateResult = validateCancelOrder(paramMap);
			if (WmsCodeEnum.SUCCESS.getCode().equals(validateResult.getCode())) {
				result = orderManager.cancelOrder(paramMap.get("orderCode").toString(), paramMap.get("timestamp")
						.toString(), paramMap.get("signature").toString());
			} else {
				result.setResult(validateResult.getCode());
			}

		} else {
			result.setResult(WmsCodeEnum.CONTENT_TYPE_ILLEGAL.getCode());
		}

		String jsonString = jsonUtils.toJson(result);
		logger.info("WmsAction--cancelOrder--end--" + jsonString);
		ActionUtils.outputJson(jsonString);
		return null;
	}
	
	/**
	 * 取消订单
	 */
	public String cancelOrderNew() throws Exception {
		CommonResult result = new CommonResult();
		if (ActionUtils.isJsonRequest()) {
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(), WmsConstants.DEFAULT_ENCODING);
			logger.info("WmsAction--cancelOrderNew--begin--" + jsonStr);
			Map<String, Object> paramMap = jsonUtils.fromJson(jsonStr, Map.class);
			if(paramMap.get("orderCode") == null || paramMap.get("orderCode").equals("")){
				result.setResult(ErrCodeEnum.ERR_PARAM_ORDER_CODE_NULL.getErr());
			}else{
				result = orderManager.cancelOrderNew(paramMap.get("orderCode").toString());
			}
		} else {
			result.setResult(ErrCodeEnum.ERR_CONTENT_TYPE_ILLEGAL.getErr());
		}

		result.setRet(RetCodeEnum.RET_SUCCESS.getRet());
		String jsonString = jsonUtils.toJson(result);
		logger.info("WmsAction--cancelOrderNew--end--" + jsonString);
		ActionUtils.outputJson(jsonString);
		return null;
	}
	
	/**
	 * 查询订单接口
	 */
	public String queryOrder() throws Exception {
		QueryOrderResult result = new QueryOrderResult();
		if (ActionUtils.isJsonRequest()) {
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(),WmsConstants.DEFAULT_ENCODING);
			logger.info("WmsAction--queryOrder--begin--" + jsonStr);
			Map<String, Object> paramMap = jsonUtils.fromJson(jsonStr, Map.class);
			WmsCodeEnum validateResult = validateQueryOrder(paramMap);
			if (WmsCodeEnum.SUCCESS.getCode().equals(validateResult.getCode())) {
				result = orderManager.queryOrder(paramMap.get("orderCode").toString());
			} else {
				result.setResult(ErrCodeEnum.ERR_CONTENT_TYPE_ILLEGAL.getErr());
			}
		} else {
			result.setResult(ErrCodeEnum.ERR_CONTENT_TYPE_ILLEGAL.getErr());
		}
		
		result.setRet(RetCodeEnum.RET_SUCCESS.getRet());
		String jsonString = jsonUtils.toJson(result);
		logger.info("WmsAction--queryOrder--end--" + jsonString);
		ActionUtils.outputJson(jsonString);
		return null;
	}
	
	/**
	 * 订单操作接口
	 */
	public String operateOrder() throws Exception {
		OperateOrderResult result = new OperateOrderResult();
		if (ActionUtils.isJsonRequest()) {
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(),WmsConstants.DEFAULT_ENCODING);
			logger.info("WmsAction--operateOrder--begin--" + jsonStr);
			OperateOrderRequest req = jsonUtils.fromJson(jsonStr, OperateOrderRequest.class);
			result = orderManager.operateOrder(req);
		} else {
			result.setResult(ErrCodeEnum.ERR_CONTENT_TYPE_ILLEGAL.getErr());
		}

		result.setRet(ErrCodeEnum.ERR_SUCCESS.getErr());
		String jsonString = jsonUtils.toJson(result);
		logger.info("WmsAction--operateOrder--end--" + jsonString);
		ActionUtils.outputJson(jsonString);
		return null;
	}

	private WmsCodeEnum validateQueryStock(Map<String, Object> params) {
		if (params == null) {
			return WmsCodeEnum.PARAM_ERROR;
		}
		if (params.get("warehouseCode") == null) {
			return WmsCodeEnum.PARAM_WAREHOUSE_CODE_NULL;
		}
		if (params.get("skuCode") == null) {
			return WmsCodeEnum.PARAM_WAREHOUSE_CODE_NULL;
		}
		return WmsCodeEnum.SUCCESS;
	}

	private WmsCodeEnum validateCancelOrder(Map<String, Object> params) {
		if (params == null) {
			return WmsCodeEnum.PARAM_ERROR;
		}
		if (params.get("orderCode") == null) {
			return WmsCodeEnum.PARAM_ORDER_CODE_NULL;
		}
		if (params.get("timestamp") == null) {
			return WmsCodeEnum.PARAM_TIMESTAMP_ILLEGAL;
		}
		if (params.get("signature") == null) {
			return WmsCodeEnum.SIGNATURE_CHECKSUM_FAILURE;
		}
		return WmsCodeEnum.SUCCESS;
	}
	
	private WmsCodeEnum validateQueryOrder(Map<String, Object> params) {
		if (params == null) {
			return WmsCodeEnum.PARAM_ERROR;
		}
		if (params.get("orderCode") == null) {
			return WmsCodeEnum.PARAM_ORDER_CODE_NULL;
		}
		return WmsCodeEnum.SUCCESS;
	}
	
	@Autowired
	public void setStockManager(StockManager stockManager) {
		this.stockManager = stockManager;
	}

	@Autowired
	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

}
