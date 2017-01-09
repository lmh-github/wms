package com.gionee.top.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gionee.top.dao.RdsDao;
import com.gionee.top.db.DBHelper;
import com.gionee.top.util.DateConvert;

@Repository
public class RdsDaoImpl implements RdsDao {
	private static final Log log = LogFactory.getLog(RdsDaoImpl.class);

	@Autowired
	private DBHelper dbHelper;

	@Override
	public List<Map<String, Object>> getTradeInfoList(Date start, Date end) {
		String sql = "select tid,jdp_response from jdp_tb_trade where jdp_modified >=? and jdp_modified<? and status=? order by jdp_modified,tid";
		log.info("sql:{" + sql + "}--params:{"
				+ DateConvert.convertD2String(start) + ","
				+ DateConvert.convertD2String(end) + ","
				+ "WAIT_SELLER_SEND_GOODS");
		return dbHelper.queryMapList(
				sql,
				new Object[]{DateConvert.convertD2String(start),
						DateConvert.convertD2String(end),
						"WAIT_SELLER_SEND_GOODS"});
	}
	
	@Override
	public List<Map<String, Object>> getFxTradeInfoList(Date start, Date end) {
		String sql = "select fenxiao_id,jdp_response from jdp_fx_trade where jdp_modified >=? and jdp_modified<? and status=? order by jdp_modified";
		log.info("sql:{" + sql + "}--params:{"
				+ DateConvert.convertD2String(start) + ","
				+ DateConvert.convertD2String(end) + ","
				+ "WAIT_SELLER_SEND_GOODS");
		return dbHelper.queryMapList(
				sql,
				new Object[]{DateConvert.convertD2String(start),
						DateConvert.convertD2String(end),
						"WAIT_SELLER_SEND_GOODS"});
	}

}
