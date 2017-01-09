package com.gionee.wms.job;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.DateConvert;
import com.gionee.wms.service.stat.OrderStatService;

/**
 * 
 * 作者:milton.zhang
 * 时间:2014-4-11
 * 描述:订单统计任务，定时统计发货商品数据
 */
public class OrderStatTask {
	private static Logger logger = LoggerFactory.getLogger(OrderStatTask.class);
	@Autowired
	private OrderStatService orderStatService;

	public void execute() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date statDate = cal.getTime();
		String yesterdayStr = DateConvert.convertD2Str(cal.getTime());
		String startDateStr = yesterdayStr + " 00:00:00";
		String endDateStr = yesterdayStr + " 23:59:59";
		try {
			logger.info("订单发货数据统计开始...--" + startDateStr + "--" + endDateStr);
			orderStatService.statSaleData(statDate,
					DateConvert.convertS2Date(startDateStr),
					DateConvert.convertS2Date(endDateStr));
			//记录业务日志
			logger.info("订单发货数据统计结束--" + startDateStr + "--" + endDateStr);
		} catch (Exception e) {
			logger.error("订单发货数据统计异常--" + startDateStr + "--" + endDateStr, e);
		}
	}

	public static void main(String[] args) {

	}
}
