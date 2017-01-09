package com.gionee.wms.dao;

@BatisDao
public interface CommonDao {
	/**
	 * 查询指定sequence的Currval
	 */
	Long querySequenceCurrval(String sequenceName);

	/**
	 * 查询指定sequence的Nextval
	 */
	Long querySequenceNextval(String sequenceName);
}
