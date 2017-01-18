package com.gionee.wms.service.stat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants.InvoiceType;
import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.dao.StatDao;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SaleStat;
import com.gionee.wms.entity.SalesOutStat;
import com.gionee.wms.entity.TransferPartner;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.vo.SalesStatVo;
import com.gionee.wms.vo.TransferStatVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("orderStatService")
public class OrderStatServiceImpl implements OrderStatService {
	private static Logger logger = LoggerFactory.getLogger(OrderStatServiceImpl.class);
	
	private static final String IN = "in";
	private static final String OUT = "out";

	@Autowired
	private TransferService transferService;
	@Autowired
	private StatDao statDao;
	
	@Override
	public void statSaleData(Date statDate, Date startDate, Date endDate) {
		/**
		 * 根据发货时间查询已发货的订单和商品
		 * 按照同一客户、同一物料编码、同一单价为一行统计数据
		 * 入库统计表
		 * 注：先处理普通订单，再处理调拨单
		 */
		Map<String, SaleStat> statMap = Maps.newHashMap();//行数据标识，key:客户-物料编码-单价，value:订单数量
		List<SaleStat> statList = Lists.newArrayList();//统计结果列表
//		Map<String, String> orderSourcePartnerMap = Maps.newHashMap();//存储订单来源与客户编码对应关系：key:订单来源，value:客户编码
		
		//处理普通订单
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("opTimeBegin", startDate);
		criteria.put("opTimeEnd", endDate);
		List<SalesStatVo> orderList = statDao.queryOrderListAndGoodsForStat(criteria);
		if(logger.isInfoEnabled()) {
			logger.info("销售统计时间段: " + startDate + " - " + endDate + " 记录数: " + (null==orderList?"0":orderList.size()));
		}
		if(CollectionUtils.isNotEmpty(orderList)){
			for(SalesStatVo item : orderList){
				String partnerCode = getPartnerCodeBySource(item.getOrderSource(), item.getPaymentCode());
				String materialCode = item.getMaterialCode();
				Integer quantity = item.getQuantity();
				if(quantity<0) {
					quantity = -quantity;
				}
				String bizType = IN;
				if(item.getBizType().equals(StockBizType.OUT_SALES.getCode())) {
					bizType = OUT;
				}
				String orderType = getOrderTypeByMaterialCode(materialCode, bizType, item.getUnitPrice(), item.getIndivEnabled());
				String key = partnerCode + "-" + materialCode + "-" + orderType + "-" + item.getUnitPrice().toString();
				if(statMap.containsKey(key)){
					SaleStat statItem = statMap.get(key);
					statItem.setOrderNum(statItem.getOrderNum()+quantity.intValue());
					statMap.put(key, statItem);
				}else{
					//新建一行统计数据
					SaleStat statItem = new SaleStat();
					statItem.setFactory("2000");
					statItem.setFxChannel("31");
					statItem.setInvoicer(partnerCode);
					if(item.getInvoiceType()==null){
						statItem.setInvoiceType("无");
					}else{
						statItem.setInvoiceType(InvoiceType.VAT.getName());
					}
					statItem.setMaterialCode(materialCode);
					statItem.setOrderNum(quantity.intValue());
					statItem.setOrderReason("无");
					statItem.setOrderType(orderType);
					statItem.setPoCode("无");
					statItem.setPoProCode("无");
					statItem.setPostingDate(new Date());
					statItem.setPurchaseCode("");
					statItem.setPurchaseDate(item.getOrderTime());
					statItem.setSaleOrg("1000");
					statItem.setSaler(partnerCode);
					statItem.setSender(partnerCode);
					statItem.setShipper(null);
					statItem.setShippingType("Z40");
					statItem.setStatDate(statDate);
					statItem.setUnitPrice(item.getUnitPrice());
					statItem.setUse(null);
					statItem.setWarehouse("");//??
					statMap.put(key, statItem);
				}
			}
		}
		
		//处理调拨单
		criteria.clear();
		criteria.put("shippingTimeBegin", startDate);
		criteria.put("shippingTimeEnd", endDate);
		List<TransferStatVo> transferList = statDao.getTransferAndGoodsForStat(criteria);
		if(logger.isInfoEnabled()) {
			logger.info("调拨统计时间段: " + startDate + " - " + endDate + " 记录数: " + (null==transferList?"0":transferList.size()));
		}
		criteria.clear();
		List<TransferPartner> partnerList = transferService.getTransferPartnerList(criteria);
		HashMap<Long, TransferPartner> partnerHm = Maps.newHashMap(); 	// 合作方id对应表
		for (TransferPartner transferPartner : partnerList) {
			partnerHm.put(transferPartner.getId(), transferPartner);
		}
		if(CollectionUtils.isNotEmpty(transferList)){
			for(TransferStatVo item : transferList){
				String partnerCode = item.getTransferSale()==null?"无":partnerHm.get(item.getTransferSale()).getCode()+"";//调拨客户使用售达方
				String materialCode = item.getMaterialCode();
				Integer quantity = item.getQuantity();
				if(quantity<0) {
					quantity = -quantity;
				}
				String bizType = IN;	// 业务类型(进or出)
				if(item.getBizType().equals(StockBizType.OUT_TRANSFER.getCode())) {
					bizType = OUT;
				}
				String orderType = getOrderTypeByMaterialCode(materialCode, bizType, item.getUnitPrice(), item.getIndivEnabled());	// 订单类型
				String key = partnerCode  + "-" + materialCode + "-" + orderType + "-" +  item.getUnitPrice().toString();	// 如果单价，则为空
				if(statMap.containsKey(key)){
					SaleStat statItem = statMap.get(key);
					statItem.setOrderNum(statItem.getOrderNum()+quantity.intValue());
					statMap.put(key, statItem);
				}else{
					//新建一行统计数据
					SaleStat statItem = new SaleStat();
					statItem.setFactory("2000");
					statItem.setFxChannel("31");
					statItem.setInvoicer(item.getTransferInvoice()==null?"无":partnerHm.get(item.getTransferInvoice()).getCode()); // 开票
					statItem.setInvoiceType("无");//调拨暂无开发票
					statItem.setMaterialCode(materialCode);
					statItem.setOrderNum(quantity.intValue());
					statItem.setOrderReason("无");
					statItem.setOrderType(orderType);
					statItem.setPoCode("无");
					statItem.setPoProCode("无");
					statItem.setPostingDate(new Date());
					statItem.setPurchaseCode("");
					statItem.setPurchaseDate(item.getCreateTime());
					statItem.setSaleOrg("1000");
					statItem.setSaler(item.getTransferSale()==null?"无":partnerHm.get(item.getTransferSale()).getCode());	// 售达
					statItem.setSender(item.getTransferSend()==null?"无":partnerHm.get(item.getTransferSend()).getCode()); // 送达
					statItem.setShipper(null);
					statItem.setShippingType("Z40");
					statItem.setStatDate(statDate);
					statItem.setUnitPrice(item.getUnitPrice());
					statItem.setUse(null);
					statItem.setWarehouse("");//??
					statMap.put(key, statItem);
				}
			}
		}
		
		//入库统计数据
		for(Map.Entry<String, SaleStat> entry : statMap.entrySet()){
			SaleStat stat = entry.getValue();
			statList.add(stat);
		}
		
		if(statList.size() > 0) {
			statDao.addSaleStats(statList);
		}
	}
	
	@Override
	public List<SaleStat> querySaleStatList(Map<String, Object> criteria) {
		return statDao.querySaleStatList(criteria);
	}

	/**
	 * 根据物料编号等信息获取订单类型
	 * @param materialCode
	 * @param orderStatus
	 * @param unitPrice
	 * @param indivEnabled
	 * @return
	 */
	private String getOrderTypeByMaterialCode(String materialCode, String bizType, BigDecimal unitPrice, Integer indivEnabled) {
		if(logger.isInfoEnabled()) {
			logger.info("物料号: " + materialCode +" 类型: " + bizType + " 单价: " + unitPrice +" 是否个体: " + indivEnabled);
		}
		String orderType = "";
		char startChar = materialCode.charAt(0);
		boolean isStartWith2346 = false;
		if(startChar=='2' || startChar=='3' || startChar=='4' || startChar=='6'){
			isStartWith2346 = true;
		}
		double price = unitPrice==null?0:unitPrice.doubleValue();
		if(startChar=='5' && price>0 && bizType.equals(OUT)){
			orderType = "ZOR1";	// 成品订单
		}else if(isStartWith2346 && price>0 && bizType.equals(OUT)){
			orderType = "ZOR4";	// 配件订单
		}else if(isStartWith2346 && price==0 && indivEnabled.equals(0) && bizType.equals(OUT)){
			orderType = "ZFD1";	// 广告物料订单
		}else if(startChar=='5' && price==0 && bizType.equals(OUT)){
			orderType = "ZFD7";	// 免费成品订单
		}else if(startChar=='5' && price>0 && bizType.equals(IN)){
			orderType = "ZRE1";	// 成品退单
		}else if(isStartWith2346 && price>0 && bizType.equals(IN)){
			orderType = "ZRE4";	// 配件退单
		}else if(isStartWith2346 && price==0  && indivEnabled.equals(0) && bizType.equals(IN)){
			orderType = "ZRD1";	// 广告物料退单
		}else if(startChar=='5' && price==0 && bizType.equals(IN)){
			orderType = "ZRD7";	// 免费成品退单
		}
		return orderType;
	}

	/**
	 * 根据订单来源获取客户编码
	 * @param orderSource
	 * @return
	 */
	private String getPartnerCodeBySource(String orderSource, String paymentCode) {
		String partnerCode = "";
		switch (Integer.valueOf(orderSource)) {
			case 1:
				if("alipay".equals(paymentCode)) {
					partnerCode = "100578";
				} else if("cmb".equals(paymentCode)) {
					partnerCode = "100558";
				} else {
					partnerCode = "100578";
				}
				break;
			case 2:
				partnerCode = "100314";
				break;
			case 3:
				partnerCode = "100578";	// iuni官网未确定
				break;
			case 4:
				partnerCode = "100314";// iuni天猫未确定
				break;
			case 5:
				partnerCode = "100543";
				break;
			case 6:
				partnerCode = "100314";	// iuni唯品未确定
				break;
			case 7:
				partnerCode = "100316";
				break;
			case 8:
				partnerCode = "910018";
				break;
			default :
				partnerCode = "000000";
				break;
		}
		return partnerCode;
	}
	
	@Override
	public List<SalesOutStat> getSalesOutStatList(Map<String, Object> criteria, Page page) throws ServiceException {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return statDao.querySalesOutStatByPage(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int getSalesOutStatTotal(Map<String, Object> criteria) {
		return statDao.querySalesOutStatTotal(criteria);
	}

	@Override
	public void addSalesOutStat(List<SalesOutStat> statList) {
		for (SalesOutStat salesOutStat : statList) {
			statDao.addSalesOutStat(salesOutStat);
		}
	}
}
