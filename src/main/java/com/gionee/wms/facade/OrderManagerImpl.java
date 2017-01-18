package com.gionee.wms.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.BackStatus;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.BackGoods;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.entity.SkuBomDetail;
import com.gionee.wms.facade.dto.OrderGoodsDTO;
import com.gionee.wms.facade.dto.OrderInfoDTO;
import com.gionee.wms.facade.dto.SalesOrderDTO;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.facade.result.CommonResult;
import com.gionee.wms.facade.result.CommonResult.ErrCodeEnum;
import com.gionee.wms.facade.result.CommonResult.RetCodeEnum;
import com.gionee.wms.facade.result.OperateOrderResult;
import com.gionee.wms.facade.result.QueryOrderResult;
import com.gionee.wms.facade.result.WmsResult;
import com.gionee.wms.facade.result.WmsResult.WmsCodeEnum;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.stock.BackService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component("orderManager")
public class OrderManagerImpl implements OrderManager {
	private static Logger logger = LoggerFactory.getLogger(OrderManagerImpl.class);
	public static final long ACCESS_VALID_TIME_OFFSET = 300000L; // 访问有效期,设为5分钟
	private JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	private SalesOrderService salesOrderService;
	private ShippingService shippingService;
	@Autowired
	private BackService backService;

	@Autowired
	private WaresDao waresDao;

	@Override
	public WmsResult syncOrder(OrderInfoDTO orderInfo, String timestamp, String signature, Integer operFlag) {
		WmsResult result = new WmsResult();

		// 请求参数校验
		WmsCodeEnum validateResult = validateSyncOrder(orderInfo, timestamp, signature);
		if (WmsCodeEnum.SUCCESS.getCode().equals(validateResult.getCode())) {
			orderInfo.setOrderCode(orderInfo.getOrderCode().trim());
			// 签名验证
			StringBuffer plainStr = new StringBuffer();
			plainStr.append(orderInfo.getOrderCode()).append(orderInfo.getConsignee()).append(timestamp).append(WmsConstants.SYNC_ORDER_SALT);
			String localSignature = DigestUtils.md5Hex(plainStr.toString());
			if (localSignature.equals(signature)) {// 签名验证通过
				// 构造订单对象及商品明细
				SalesOrder salesOrder = new SalesOrder();
				List<SalesOrderGoods> orderGoodsList = Lists.newArrayList();
				try {
					Shipping shipping = shippingService.getShippingByCode(orderInfo.getShippingCode());
					if (shipping != null) {
						// 初始化用户选择的配送方式
						salesOrder.setShippingId(shipping.getId());
						salesOrder.setShippingName(shipping.getShippingName());
						salesOrder.setDeliveryCode(orderInfo.getOrderCode() + "01");
						if (null == orderInfo.getOrderTime()) {
							orderInfo.setOrderTime(new Date());	// 没有下单时间
						}
						if (null == orderInfo.getPaymentTime()) {
							orderInfo.setPaymentTime(new Date());	// 没有支付时间
						}
						BeanUtils.copyProperties(salesOrder, orderInfo);

						for (OrderGoodsDTO goods : orderInfo.getGoodsList()) {
							// SalesOrderGoods orderGoods = new SalesOrderGoods();
							// orderGoods.setUnitPrice(goods.getUnitPrice());
							// orderGoods.setQuantity(goods.getQuantity());
							// orderGoods.setSubtotalPrice(goods.getSubtotalPrice());
							// orderGoods.setSkuCode(goods.getSkuCode());
							// orderGoods.setGoodsSid(goods.getGoodsSid());
							// orderGoodsList.add(orderGoods);
							orderGoodsList.addAll(splitGoods(goods));
						}
					} else {
						result.setResult(WmsCodeEnum.SHIPPING_CODE_NOT_EXISTS.getCode());
					}

				} catch (Exception e) {
					// logger.error("sync orders error", e);
					result.setResult(WmsCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
				}

				// 保存订单信息及商品明细
				if (StringUtils.isBlank(result.getCode())) {
					try {
						if (operFlag == null || operFlag != 1) {
							salesOrderService.addSalesOrder(salesOrder, orderGoodsList);
						} else {
							salesOrderService.updateSalesOrder(salesOrder, orderGoodsList);
						}
						result.setResult(WmsCodeEnum.SUCCESS.getCode(), salesOrder.getOrderCode());
					} catch (ServiceException e) {
						// logger.error("sync orders error", e);
						result.setResult(e.getMessage());
						if (e.getCause() != null) {
							result.setMessage(e.getCause().getMessage());
						}
					}
				}
			} else {// 签名验证未通过
				result.setResult(WmsCodeEnum.SIGNATURE_CHECKSUM_FAILURE.getCode());
			}
		} else {
			result.setResult(validateResult.getCode());
		}

		return result;

	}

	/**
	 * 商品组合商品
	 * @param goods
	 * @return
	 */
	private List<SalesOrderGoods> splitGoods(OrderGoodsDTO goods) {
		List<SalesOrderGoods> salesOrderGoods = Lists.newArrayList();
		List<SkuBomDetail> relation = waresDao.selectSkuBomDetailRelation(goods.getSkuCode());
		if (CollectionUtils.isEmpty(relation)) {
			SalesOrderGoods orderGoods = new SalesOrderGoods();
			orderGoods.setUnitPrice(goods.getUnitPrice());
			orderGoods.setQuantity(goods.getQuantity());
			orderGoods.setSubtotalPrice(goods.getSubtotalPrice());
			orderGoods.setSkuCode(goods.getSkuCode());
			orderGoods.setGoodsSid(goods.getGoodsSid());

			salesOrderGoods.add(orderGoods);

			return salesOrderGoods;
		}

		// 组合商品拆分
		for (SkuBomDetail skuBomDetail : relation) {
			SalesOrderGoods orderGoods = new SalesOrderGoods();
			boolean isBonus = skuBomDetail.getIsBonus() == null || BooleanUtils.toBooleanObject(skuBomDetail.getIsBonus()); // 是否为赠品
			orderGoods.setUnitPrice(isBonus ? new BigDecimal(0) : goods.getUnitPrice()); // 赠品的价格为0
			orderGoods.setQuantity(goods.getQuantity() * (skuBomDetail.getQuantity() == null ? 1 : skuBomDetail.getQuantity()));
			orderGoods.setSubtotalPrice(isBonus ? new BigDecimal(0) : goods.getSubtotalPrice()); // 赠品的小计为0
			orderGoods.setSkuCode(skuBomDetail.getCSkuCode());

			// orderGoods.setGoodsSid(goods.getGoodsSid());

			salesOrderGoods.add(orderGoods);
		}

		return salesOrderGoods;
	}

	@Override
	public CommonResult syncOrderNew(OrderInfoDTO orderInfo, Integer operFlag) {
		CommonResult result = new CommonResult();

		orderInfo.setOrderCode(orderInfo.getOrderCode().trim());

		// 构造订单对象及商品明细
		SalesOrder salesOrder = new SalesOrder();
		List<SalesOrderGoods> orderGoodsList = Lists.newArrayList();
		try {
			Shipping shipping = shippingService.getShippingByCode(orderInfo.getShippingCode());
			if (shipping != null) {
				// 初始化用户选择的配送方式
				salesOrder.setShippingId(shipping.getId());
				salesOrder.setShippingName(shipping.getShippingName());
				salesOrder.setDeliveryCode(orderInfo.getOrderCode() + "01");

				// 转化根据支付类型转化支付编码，转化相关金额
				if (orderInfo.getPaymentType() == 2 && StringUtils.isBlank(orderInfo.getPaymentCode())) {
					orderInfo.setPaymentCode("cod");
				}
				if (orderInfo.getGoodsAmount() == null) {
					orderInfo.setGoodsAmount(new BigDecimal(0));
				}
				if (orderInfo.getOrderAmount() == null) {
					orderInfo.setOrderAmount(new BigDecimal(0));
				}
				if (orderInfo.getInvoiceAmount() == null) {
					orderInfo.setInvoiceAmount(new BigDecimal(0));
				}
				if (orderInfo.getPaidAmount() == null) {
					orderInfo.setPaidAmount(new BigDecimal(0));
				}
				if (orderInfo.getPayableAmount() == null) {
					orderInfo.setPayableAmount(new BigDecimal(0));
				}

				BeanUtils.copyProperties(salesOrder, orderInfo);

				for (OrderGoodsDTO goods : orderInfo.getGoodsList()) {
					SalesOrderGoods orderGoods = new SalesOrderGoods();
					orderGoods.setUnitPrice(goods.getUnitPrice());
					orderGoods.setQuantity(goods.getQuantity());
					orderGoods.setSubtotalPrice(goods.getSubtotalPrice());
					orderGoods.setSkuCode(goods.getSkuCode());
					// 订单来源
					orderGoods.setOrderSource(goods.getOrderSource());
					orderGoods.setGoodsSid(goods.getGoodsSid());
					orderGoodsList.add(orderGoods);
				}
			} else {
				result.setResult(ErrCodeEnum.ERR_SHIPPING_CODE_NOT_EXISTS.getErr());
			}

		} catch (Exception e) {
			logger.error("sync orders error", e);
			result.setResult(ErrCodeEnum.ERR_SYSTEM_ERROR.getErr(), e.getMessage());
		}

		// 保存订单信息及商品明细
		if (result.getErr() == null) {
			try {
				if (operFlag == null || operFlag != 1) {
					salesOrderService.addSalesOrder(salesOrder, orderGoodsList);
				} else {
					salesOrderService.updateSalesOrder(salesOrder, orderGoodsList);
				}
				result.setResult(ErrCodeEnum.ERR_SUCCESS.getErr(), salesOrder.getOrderCode());
			} catch (ServiceException e) {
				logger.error("sync orders error", e);
				// 转换错误码
				result = convertResultErr(e, result);
			}
		}

		result.setRet(RetCodeEnum.RET_SUCCESS.getRet());
		return result;
	}

	private CommonResult convertResultErr(ServiceException e, CommonResult result) {
		if (e.getMessage().equals(WmsCodeEnum.DUPLICATE_ORDER.getCode())) {
			result.setResult(ErrCodeEnum.ERR_DUPLICATE_ORDER.getErr(), ErrCodeEnum.ERR_DUPLICATE_ORDER.getMsg());
		} else if (e.getMessage().equals(WmsCodeEnum.STOCK_OCCUPY_FAILED.getCode())) {
			result.setResult(ErrCodeEnum.ERR_STOCK_OCCUPY_FAILED.getErr(), ErrCodeEnum.ERR_STOCK_OCCUPY_FAILED.getMsg());
		} else if (e.getMessage().equals(WmsCodeEnum.ORDER_NOT_EXIST.getCode())) {
			result.setResult(ErrCodeEnum.ERR_ORDER_NOT_EXIST.getErr(), ErrCodeEnum.ERR_ORDER_NOT_EXIST.getMsg());
		} else if (e.getMessage().equals(WmsCodeEnum.CURRENT_STATUS_FORBIDDEN_UPDATE.getCode())) {
			result.setResult(ErrCodeEnum.ERR_CURRENT_STATUS_FORBIDDEN_UPDATE.getErr(), ErrCodeEnum.ERR_CURRENT_STATUS_FORBIDDEN_UPDATE.getMsg());
		} else if (e.getMessage().equals(WmsCodeEnum.STOCK_RELEASE_FAILED.getCode())) {
			result.setResult(ErrCodeEnum.ERR_STOCK_RELEASE_FAILED.getErr(), ErrCodeEnum.ERR_STOCK_RELEASE_FAILED.getMsg());
		} else if (e.getMessage().equals(WmsCodeEnum.ORDER_SHIPPED.getCode())) {
			result.setResult(ErrCodeEnum.ERR_ORDER_SHIPPED.getErr(), ErrCodeEnum.ERR_ORDER_SHIPPED.getMsg());
		} else if (e.getMessage().equals(WmsCodeEnum.NOT_ALLOWED_CANCEL.getCode())) {
			result.setResult(ErrCodeEnum.ERR_NOT_ALLOWED_CANCEL.getErr(), ErrCodeEnum.ERR_NOT_ALLOWED_CANCEL.getMsg());
		} else {
			result.setResult(ErrCodeEnum.ERR_SYSTEM_ERROR.getErr(), ErrCodeEnum.ERR_SYSTEM_ERROR.getMsg());
		}
		return result;
	}

	@Override
	public WmsResult cancelOrder(String orderCode, String timestamp, String signature) {
		WmsResult result = new WmsResult();

		// 请求参数校验
		WmsCodeEnum validateResult = validateCancelOrder(orderCode, timestamp, signature);
		if (WmsCodeEnum.SUCCESS.getCode().equals(validateResult.getCode())) {
			// 签名验证
			StringBuffer plainStr = new StringBuffer();
			plainStr.append(orderCode).append(timestamp).append(WmsConstants.SYNC_ORDER_SALT);
			String localSignature = DigestUtils.md5Hex(plainStr.toString());
			if (localSignature.equals(signature)) {// 签名验证通过
				// 取消订单
				try {
					salesOrderService.cancelSalesOrder(orderCode);
					result.setResult(WmsCodeEnum.SUCCESS.getCode(), orderCode);
				} catch (ServiceException e) {
					logger.error("sync orders error", e);
					result.setResult(e.getMessage());
				}
			} else {// 签名验证未通过
				result.setResult(WmsCodeEnum.SIGNATURE_CHECKSUM_FAILURE.getCode());
			}
		} else {
			result.setResult(validateResult.getCode());
		}

		return result;

	}

	@Override
	public CommonResult cancelOrderNew(String orderCode) {
		CommonResult result = new CommonResult();
		// 取消订单
		try {
			salesOrderService.cancelSalesOrder(orderCode);
			result.setResult(ErrCodeEnum.ERR_SUCCESS.getErr(), orderCode);
		} catch (ServiceException e) {
			logger.error("sync orders error", e);
			// 转换错误码
			result = convertResultErr(e, result);
		}
		return result;
	}

	private WmsCodeEnum validateCancelOrder(String orderCode, String timestamp, String signature) {
		if (StringUtils.isBlank(orderCode)) {
			return WmsCodeEnum.PARAM_ORDER_CODE_NULL;
		}
		// 访问有效期判断
		try {
			long timestampLong = Long.parseLong(timestamp);
			if (Math.abs(System.currentTimeMillis() - timestampLong) > ACCESS_VALID_TIME_OFFSET) {
				return WmsCodeEnum.ACCESS_EXPIRED;
			}
		} catch (NumberFormatException e) {
			return WmsCodeEnum.PARAM_TIMESTAMP_ILLEGAL;
		}
		if (StringUtils.isEmpty(signature)) {
			return WmsCodeEnum.SIGNATURE_CHECKSUM_FAILURE;
		}
		return WmsCodeEnum.SUCCESS;
	}

	private WmsCodeEnum validateSyncOrder(OrderInfoDTO orderInfo, String timestamp, String signature) {
		// 访问有效期判断
		try {
			long timestampLong = Long.parseLong(timestamp);
			if (Math.abs(System.currentTimeMillis() - timestampLong) > ACCESS_VALID_TIME_OFFSET) {
				return WmsCodeEnum.ACCESS_EXPIRED;
			}
		} catch (NumberFormatException e) {
			return WmsCodeEnum.PARAM_TIMESTAMP_ILLEGAL;
		}
		if (StringUtils.isEmpty(signature)) {
			return WmsCodeEnum.SIGNATURE_CHECKSUM_FAILURE;
		}
		return validateOrder(orderInfo);
	}

	private WmsCodeEnum validateOrder(OrderInfoDTO orderInfo) {
		if (orderInfo == null) {
			return WmsCodeEnum.PARAM_ORDER_INFO_NULL;
		}
		if (StringUtils.isBlank(orderInfo.getOrderCode())) {
			return WmsCodeEnum.PARAM_ORDER_CODE_NULL;
		}
		if (StringUtils.isBlank(orderInfo.getConsignee())) {
			return WmsCodeEnum.PARAM_CONSIGNEE_NULL;
		}
		if (StringUtils.isBlank(orderInfo.getProvince())) {
			return WmsCodeEnum.PARAM_PROVINCE_NULL;
		}
		if (StringUtils.isBlank(orderInfo.getCity())) {
			return WmsCodeEnum.PARAM_CITY_NULL;
		}
		if (StringUtils.isBlank(orderInfo.getAddress())) {
			return WmsCodeEnum.PARAM_ADDRESS_NULL;
		}
		if (StringUtils.isBlank(orderInfo.getTel()) && StringUtils.isBlank(orderInfo.getMobile())) {
			return WmsCodeEnum.PARAM_PHONE_NULL;
		}
		if (orderInfo.getInvoiceEnabled() == null) {
			return WmsCodeEnum.PARAM_INVOICE_ENABLED_NULL;
		}
		if (orderInfo.getGoodsAmount() == null) {
			return WmsCodeEnum.PARAM_GOODS_AMOUNT_NULL;
		}
		if (orderInfo.getOrderAmount() == null) {
			return WmsCodeEnum.PARAM_ORDER_AMOUNT_NULL;
		}
		if (orderInfo.getPayableAmount() == null) {
			return WmsCodeEnum.PARAM_PAYABLE_AMOUNT_NULL;
		}
		if (CollectionUtils.isEmpty(orderInfo.getGoodsList())) {
			return WmsCodeEnum.PARAM_GOODS_LIST_NULL;
		}

		return WmsCodeEnum.SUCCESS;
	}

	@Override
	public QueryOrderResult queryOrder(String orderCode) {
		QueryOrderResult result = new QueryOrderResult();
		SalesOrderDTO orderDTO = new SalesOrderDTO();
		SalesOrder order = salesOrderService.getSalesOrderByCode(orderCode);
		if (order == null) {
			result.setErr(ErrCodeEnum.ERR_ORDER_NOT_EXIST.getErr());
			result.setMsg(ErrCodeEnum.ERR_ORDER_NOT_EXIST.getMsg());
		} else {
			orderDTO.setOrderCode(order.getOrderCode());
			orderDTO.setOrderStatus(order.getOrderStatus());
			result.setData(orderDTO);
			result.setErr(ErrCodeEnum.ERR_SUCCESS.getErr());
		}
		return result;
	}

	@Override
	public OperateOrderResult operateOrder(OperateOrderRequest req) {
		OperateOrderResult result = new OperateOrderResult();
		try {
			String operFlag = req.getOperFlag();
			if (operFlag.equals("BACK")) {
				Map<String, Object> backParams = Maps.newHashMap();
				backParams.put("backCode", req.getBackCode());
				Back oldBack = backService.getBack(backParams);
				if (oldBack != null) {
					result.setErr(ErrCodeEnum.ERR_DUPLICATE_BACK.getErr());
					result.setMsg(ErrCodeEnum.ERR_DUPLICATE_BACK.getMsg());
				} else {
					// 查询该订单号是否存在退货中
					backParams.clear();
					backParams.put("orderCode", req.getOrderCode());
					backParams.put("backStatus", BackStatus.BACKING.getCode());
					Back backing = backService.getBack(backParams);
					if (backing == null) {
						// 入库退货单
						Back back = new Back();
						back.setBackCode(req.getBackCode());
						back.setOrderCode(req.getOrderCode());
						back.setRemarkBacking(req.getRemark());
						back.setShippingCode(req.getShippingCode());
						back.setShippingNo(req.getShippingNo());
						backService.addBack(back);
						// 入库退货商品信息
						List<OrderGoodsDTO> goods = req.getGoods();
						List<BackGoods> goodsList = Lists.newArrayList();
						for (OrderGoodsDTO good : goods) {
							BackGoods backGoods = new BackGoods();
							backGoods.setBackCode(back.getBackCode());
							backGoods.setSkuCode(good.getSkuCode());
							backGoods.setQuantity(good.getQuantity());
							backGoods.setGoodsSid(good.getGoodsSid());
							goodsList.add(backGoods);
						}
						backService.addBackGoods(goodsList);
						// 修改订单状态为退货中
						Map<String, Object> params = Maps.newHashMap();
						params.put("orderStatus", OrderStatus.BACKING.getCode());
						params.put("orderCode", back.getOrderCode());
						salesOrderService.updateSalesOrderStatus(params);
						result.setErr(ErrCodeEnum.ERR_SUCCESS.getErr());
					} else {
						result.setErr(ErrCodeEnum.ERR_ORDER_IS_BACKING.getErr());
						result.setMsg(ErrCodeEnum.ERR_ORDER_IS_BACKING.getMsg());
					}
				}
			} else if (operFlag.equals("CANCELBACK")) {
				Map<String, Object> backParams = Maps.newHashMap();
				backParams.put("backCode", req.getBackCode());
				Back oldBack = backService.getBack(backParams);
				if (oldBack == null) {
					result.setErr(ErrCodeEnum.ERR_BACK_NOT_EXISTS.getErr());
					result.setMsg(ErrCodeEnum.ERR_BACK_NOT_EXISTS.getMsg());
				} else {
					// 取消退货，恢复退货单状态为已取消
					backService.cancelBack(req);
					// 恢复订单状态为已签收
					Map<String, Object> params = Maps.newHashMap();
					params.put("orderStatus", OrderStatus.RECEIVED.getCode());
					params.put("orderCode", oldBack.getOrderCode());
					salesOrderService.updateSalesOrderStatus(params);

					result.setErr(ErrCodeEnum.ERR_SUCCESS.getErr());
				}
			}
		} catch (Exception e) {
			logger.error("Operate order error operFlag=" + req.getOperFlag() + " orderCode=" + req.getOrderCode(), e);
			result.setResult(ErrCodeEnum.ERR_SYSTEM_ERROR.getErr(), e.getMessage());
		}
		return result;
	}

	private WmsCodeEnum validateSyncOrders(List<OrderInfoDTO> orders, String timestamp, String signature) {
		if (CollectionUtils.isEmpty(orders)) {
			return WmsCodeEnum.PARAM_ORDERS_NULL;
		}
		// 访问有效期判断
		try {
			long timestampLong = Long.parseLong(timestamp);
			if (Math.abs(System.currentTimeMillis() - timestampLong) > ACCESS_VALID_TIME_OFFSET) {
				return WmsCodeEnum.ACCESS_EXPIRED;
			}
		} catch (NumberFormatException e) {
			return WmsCodeEnum.PARAM_TIMESTAMP_ILLEGAL;
		}
		if (StringUtils.isEmpty(signature)) {
			return WmsCodeEnum.SIGNATURE_CHECKSUM_FAILURE;
		}
		return WmsCodeEnum.SUCCESS;
	}

	@Autowired
	public void setSalesOrderService(SalesOrderService salesOrderService) {
		this.salesOrderService = salesOrderService;
	}

	@Autowired
	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

}