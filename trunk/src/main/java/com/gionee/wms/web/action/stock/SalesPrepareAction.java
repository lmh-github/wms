package com.gionee.wms.web.action.stock;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.dto.CommonAjaxResult;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.SalesOrderLog;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.log.SalesOrderLogService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("SalesPrepareAction")
@Scope("prototype")
public class SalesPrepareAction extends CrudActionSupport<SalesOrder> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8964256705441846449L;
	private static final int PART_CODE_MAXLENGTH = 5;	// 配件编码最大长度

	@Autowired
	private SalesOrderService salesOrderService;
	
	@Autowired
	private IndivService indivService;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	public SalesOrderLogService salesOrderLogService = null;
	
	private String deliveryCode;
	private Long orderId;
	private String shippingNo;
	private String weight;
	private List<SalesOrderGoods> goodsList;
	private SalesOrder salesOrder;
	private String indivCode;
	private Boolean finished;
	private Long goodsId;
	private Integer preparedNum;
	private List<Indiv> indivList;
	
	public String execute() throws Exception {
		if(null!=orderId) {
			salesOrder = salesOrderService.getSalesOrder(orderId);
			goodsList = salesOrderService.getOrderGoodsListForPrepare(orderId);
		}
		return SUCCESS;
	}
	
	public String loadGoods() throws Exception {
		boolean unfinished = false;
		if(null!=orderId) {
			goodsList = salesOrderService.getOrderGoodsListForPrepare(orderId);
			indivList = salesOrderService.getIndivList(orderId);
			for (SalesOrderGoods goods : goodsList) {
				if(goods.getPreparedNum()==null || goods.getPreparedNum()<goods.getQuantity()) {
					unfinished = true;
				}
			}
			finished = !unfinished;	// 没有未完成为已完成
		}
		return "load_goods";
	}

	/**
	 * 扫描时检查商品个体
	 */
	public String prepareIndiv() throws Exception {
		Validate.notNull(indivCode);
		CommonAjaxResult result=new CommonAjaxResult();
		SalesOrder order = salesOrderService.getSalesOrder(orderId);
		if(order.getOrderStatus().equals(OrderStatus.CANCELED.getCode())){
			result.setOk(false);
			result.setMessage("该订单已取消");
		}
		int prepareRst = 0;
		if(indivCode.length()<=PART_CODE_MAXLENGTH) {
			// sku编码
			if(null!=goodsList) {
				for (SalesOrderGoods goods : goodsList) {
					if(indivCode.equals(goods.getSkuCode()) && WmsConstants.ENABLED_FALSE == goods.getIndivEnabled()) {
						Integer preparedNum = null==goods.getPreparedNum()?1:goods.getPreparedNum()+1;
						if(preparedNum>goods.getQuantity()) {
							prepareRst = 1;
							break;
						}
						goods.setPreparedNum(preparedNum);
						salesOrderService.updateSalesOrderGoods(goods);
						prepareRst = 2;
						break;
					}
				}
			}
			if(0==prepareRst) {
				result.setOk(false);
				result.setMessage("未找到sku");
			} else if(1==prepareRst) {
				result.setOk(false);
				result.setMessage("已达到数量");
			} else if(2==prepareRst){
				result.setOk(true);
				result.setResult(indivCode);
				result.setMessage("成功");
				salesLog(orderId, "配件:"+ indivCode);
			}
		} else {
			// 个体编码
			Indiv indiv = indivService.getIndivByCode(indivCode);
			//获取配货订单仓库信息
			Warehouse warehouse = warehouseService.getWarehouseByOrderSource(order.getOrderSource());
			
			result.setOk(true);
			if(null==indiv) {
				result.setOk(false);
				result.setMessage("未找到商品个体");
			} else if(WmsConstants.IndivStockStatus.IN_WAREHOUSE.getCode()!=indiv.getStockStatus()) {
				result.setOk(false);
				result.setMessage("商品不在库中");
			} else if(WmsConstants.IndivWaresStatus.DEFECTIVE.getCode()==indiv.getWaresStatus()) {
				result.setOk(false);
				result.setMessage("该商品为次品");
			} else if(!(warehouse.getId().equals(indiv.getWarehouseId()))){
				result.setOk(false);
				result.setMessage("该商品不属于该仓库");
			}
			if(result.getOk()) {
				String skuCode = indiv.getSkuCode();
				if(null!=goodsList) {
					for (SalesOrderGoods goods : goodsList) {
						if(skuCode.equals(goods.getSkuCode()) && WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
							Integer preparedNum = null==goods.getPreparedNum()?1:goods.getPreparedNum()+1;
							if(preparedNum>goods.getQuantity()) {
								prepareRst = 1;
								break;
							}
//							goods.setPreparedNum(preparedNum);
							salesOrderService.addIndiv(orderId, order.getOrderCode(), indiv.getId());
							prepareRst =2;
							break;
						}
					}
				}
				if(0==prepareRst) {
					result.setOk(false);
					result.setMessage("未找到sku");
				} else if(1==prepareRst) {
					result.setOk(false);
					result.setMessage("已达到数量");
				} else if(2==prepareRst){
					result.setOk(true);
					result.setResult(skuCode);
					result.setMessage("成功");
					salesLog(orderId, "IMEI: " + indivCode);
				}
			}
		}
		ajaxObject(result);
		return null;
	}
	
	/**
	 * 保存操作日志
	 */
	private void salesLog(Long orderId, String remark) {
		SalesOrderLog salesOrderLog = new SalesOrderLog();
		try {
			salesOrderLog.setOrderId(orderId);
			salesOrderLog
					.setOrderStatus(WmsConstants.OrderStatus.PICKING
							.getCode());
			salesOrderLog
					.setOpUser(ActionUtils.getLoginName() == null
							? WmsConstants.DEFAULT_USERNAME_LOG
							: ActionUtils.getLoginName());
			salesOrderLog.setOpTime(new Date());
			salesOrderLog.setRemark(remark);
			salesOrderLogService.insertSalesOrderLog(salesOrderLog);
		} catch (Exception e) {
			logger.error("业务日志记录异常", e);
		}
	}
	
	/**
	 * 批量配货
	 */
	public String batchPrepare() {
		CommonAjaxResult result=new CommonAjaxResult();
		try {
			SalesOrderGoods goods = new SalesOrderGoods();
			goods.setId(goodsId);
			goods.setPreparedNum(preparedNum);
			salesOrderService.updateSalesOrderGoods(goods);
			result.setOk(true);
			salesLog(orderId, "SKU: 批量");
		} catch(Exception e) {
			result.setOk(false);
			result.setMessage(e.getMessage());
		}
		ajaxObject(result);
		return null;
	}

	/**
	 * 扫描发货单，准备进行配货
	 */
	public String readyPrepare() throws Exception {
		Validate.notNull(deliveryCode);
		CommonAjaxResult result=new CommonAjaxResult();
		try {
			SalesOrder salesOrder = salesOrderService.getSalesOrderByDeliveryCode(deliveryCode);	// 获得调货单
			if(null!=salesOrder) {
				if(OrderStatus.PRINTED.getCode()==salesOrder.getOrderStatus() || OrderStatus.PICKING.getCode()==salesOrder.getOrderStatus()) {
					//更新该订单为配货中，前置条件为已打单
					Map<String, Object> params = Maps.newHashMap();
					params.put("orderCode", salesOrder.getOrderCode());
					params.put("orderStatus", OrderStatus.PICKING.getCode());
					params.put("orderStatusWhere", OrderStatus.PRINTED.getCode());
					salesOrderService.updateSalesOrderStatus(params);
					
					result.setOk(true);
					result.setResult(salesOrder);
				} else {
					result.setMessage("发货单"+deliveryCode+"需为已打单或者配货中状态");
				}
			} else {
				result.setMessage("发货单"+deliveryCode+"不存在");
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			result.setMessage("添加发货单失败：" + e.getMessage());
		}
		ajaxObject(result);
		return null;
	}

	/**
	 * 配货完成，确认提交
	 */
	public String confirm() throws Exception {
		try {
			SalesOrder order=salesOrderService.getSalesOrder(orderId);
			if(null==order) {
				ajaxError("无该订单");
				return null;
			}else if(!order.getOrderStatus().equals(OrderStatus.PICKING.getCode())){
				ajaxError("该订单必须为配货中状态");
				return null;
			}
			shippingNo = shippingNo.trim();
			SalesOrder _shipOrder = salesOrderService.getSalesOrderByShippingNo(order.getShippingId(), shippingNo);
			if(null!=_shipOrder) {
				ajaxError(shippingNo + " 的快递单号已存在，请检查");
				return null;
			}
			goodsList = salesOrderService.getOrderGoodsListForPrepare(orderId);
			for (SalesOrderGoods goods : goodsList) {
				if(goods.getQuantity()!=null && goods.getPreparedNum()!=null && goods.getQuantity().intValue()!=goods.getPreparedNum().intValue()) {
					ajaxError("配货数量不正确，无法提交");
					return null;
				}
			}
			salesOrderService.confirmPrepare(orderId, shippingNo, weight);
			ajaxSuccess("");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			ajaxError("配货失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public SalesOrder getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		Validate.notNull(orderId);
		salesOrder = salesOrderService.getSalesOrder(orderId);
		//获取调拨商品列表
		goodsList = salesOrderService.getOrderGoodsList(orderId);
		return LIST;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareInput() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareUpdate() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareAdd() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public String getIndivCode() {
		return indivCode;
	}

	public void setIndivCode(String indivCode) {
		this.indivCode = indivCode;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public List<SalesOrderGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<SalesOrderGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public SalesOrder getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getShippingNo() {
		return shippingNo;
	}

	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getPreparedNum() {
		return preparedNum;
	}

	public void setPreparedNum(Integer preparedNum) {
		this.preparedNum = preparedNum;
	}

	public List<Indiv> getIndivList() {
		return indivList;
	}

	public void setIndivList(List<Indiv> indivList) {
		this.indivList = indivList;
	}
	
}
