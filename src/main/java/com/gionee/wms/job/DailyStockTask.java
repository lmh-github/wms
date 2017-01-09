package com.gionee.wms.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.service.stock.StockService;

public class DailyStockTask {
	private static Logger logger = LoggerFactory
			.getLogger(DailyStockTask.class);
	@Autowired
	private StockService stockService;

	public void execute() throws ParseException {

		logger.info("执行每日库存任务开始...");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 此处反复转换是为了实现具体时间转化为只取前十位的效果，例如："2014-06-09 12:23:32"->"2014-06-09 00:00:00"
		Calendar calendar = Calendar.getInstance();
		Date endDate = calendar.getTime();
		String endDateStr = sdf.format(endDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date startDate = calendar.getTime();
		String startDateStr = sdf.format(startDate);
		stockService.addDailyStock(sdf.parse(startDateStr),
				sdf.parse(endDateStr));// startDate统计开始时间（包含），endDate统计结束时间（不包含），比如：(2014-07-01,2014-07-09),那统计的就是14年7月1号凌晨0点到14年7月8号24点;

		logger.info("执行每日库存任务结束");
	}

}
