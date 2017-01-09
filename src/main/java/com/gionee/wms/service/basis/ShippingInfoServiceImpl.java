package com.gionee.wms.service.basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.SalesOrderLogDao;
import com.gionee.wms.dao.ShippingInfoDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderLog;
import com.gionee.wms.entity.ShippingInfo;
import com.gionee.wms.kuaidi.pojo.Result;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.stock.SalesOrderService;
import com.google.common.collect.Maps;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-10 下午7:41:24
 * @=======================================
 */
@Service
public class ShippingInfoServiceImpl implements ShippingInfoService {
	
	private static Logger logger = LoggerFactory	.getLogger(ShippingInfoServiceImpl.class);
	
	@Autowired
	private ShippingInfoDao shippingInfoDao;
	@Autowired
	private SalesOrderDao salesOrderDao;
	@Autowired
	private SalesOrderService salesOrderService;
	@Autowired
	public SalesOrderLogDao salesOrderLogDao;

	@Override
	public List<ShippingInfo> getShippingInfoNeedToSub() {
		return shippingInfoDao.getShippingInfoNeedToSub();
	}

	@Override
	public int updateShippingInfo(ShippingInfo shippingInfo) {
		return shippingInfoDao.updateShippingInfo(shippingInfo);
	}

	@Override
	public int addShippingInfo(ShippingInfo shippingInfo) {
		return shippingInfoDao.addShippingInfo(shippingInfo);
	}

	@Override
	public ShippingInfo getShippingInfo(String company, String shippingNo) {
		ShippingInfo info=new ShippingInfo();
		info.setCompany(company);
		info.setShippingNo(shippingNo);
		return shippingInfoDao.getShippingInfo(info);
	}

	@Override
	public List<ShippingInfo> queryShippingInfoList(
			Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return shippingInfoDao.queryShippingInfoByPage(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer queryShippingInfoTotal(Map<String, Object> criteria) {
		return shippingInfoDao.queryShippingInfoTotal(criteria);
	}

	@Override
	public ShippingInfo getShippingInfoById(Long id) {
		return shippingInfoDao.getShippingInfoById(id);
	}

	@Override
	public int updateShippingAndOrder(ShippingInfo shippingInfo) {
		if(Result.IS_CHECK_OK.equals(shippingInfo.getIsCheck())) {
			// 如果快递ischeck状态为已签收
			SalesOrder order=salesOrderDao.queryOrderByOrderCode(shippingInfo.getOrderCode());
			if(null!=order) {
				String remark="";
				if(Result.STATE_SIGN_IN.equals(shippingInfo.getState())) {	// 正常签收
					order.setOrderStatus(WmsConstants.OrderStatus.RECEIVED.getCode());
					remark = "更新为已签收";
				} else if(Result.STATE_TURN_BACK.equals(shippingInfo.getState())) {	// 拒收中
					order.setOrderStatus(WmsConstants.OrderStatus.REFUSEING.getCode());
					remark = "更新为拒收中";
				}
				List<SalesOrder> orders=new ArrayList<SalesOrder>();
				orders.add(order);	// 用batch的来更新状态
				salesOrderDao.batchUpdateOrder(orders);
								
				if(order.getOrderStatus() == WmsConstants.OrderStatus.RECEIVED.getCode() || 
						order.getOrderStatus() == WmsConstants.OrderStatus.REFUSEING.getCode()){
					//保存操作日志
					SalesOrderLog salesOrderLog = new SalesOrderLog();
					try {
						salesOrderLog.setOrderId(order.getId());
						salesOrderLog.setOrderStatus(order.getOrderStatus().intValue());
						salesOrderLog.setOpUser(WmsConstants.DEFAULT_USERNAME_LOG);
						salesOrderLog.setOpTime(new Date());
						salesOrderLog.setRemark(remark);
						salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
					} catch (Exception e) {
						logger.error("业务日志记录异常", e);
					}
					// 通知订单中心
					salesOrderService.notifyOrder(orders);
				}
			}
		}
		return shippingInfoDao.updateShippingInfo(shippingInfo);
	}

}
