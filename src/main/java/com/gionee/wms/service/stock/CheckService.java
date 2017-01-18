package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Check;
import com.gionee.wms.entity.CheckGoods;
import com.gionee.wms.entity.CheckItem;
import com.gionee.wms.entity.CheckStatusException;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.service.ServiceException;

public interface CheckService {
	/**
	 * 添加盘点单
	 */
	void addCheck(Check check) throws ServiceException;

	/**
	 * 添加待盘点商品清单
	 */
	void addCheckGoodsList(Check check, List<Long> skuIds) throws ServiceException;

	/**
	 * 添加实盘商品清单
	 */
	void addPhysicalGoodsList(Check check, Map<String, CheckGoods> physicalGoodsList) throws ServiceException;

	//	/**
	// * 分配盘点任务.
	//	 */
	//	void assignCheckTask(CheckTask checkTask) throws ServiceException;

	/**
	 * 下载盘点商品清单.
	 */
	void downloadCheckGoodsList(Long checkId) throws ServiceException;

	/**
	 * 确认盘点结果.
	 */
	void confirmCheck(Check check) throws ServiceException;

	/**
	 * 分页取符合条件的盘点单列表.
	 */
	List<Check> getCheckList(Map<String, Object> criteria, Page page);

	/**
	 * 取符合条件的盘点单总数.
	 */
	Integer getCheckTotal(Map<String, Object> criteria);

	/**
	 * 取指定的盘点单.
	 */
	Check getCheck(Long id);

	/**
	 * 分页取符合条件的盘点商品
	 */
	List<CheckGoods> getCheckGoodsList(Map<String, Object> criteria, Page page);
	
	/**
	 * 根据盘点单ID取盘点商品清单
	 */
	List<CheckGoods> getCheckGoodsList(Long checkId);

	/**
	 * 取符合条件的盘点商品总数
	 */
	Integer getCheckGoodsTotal(Map<String, Object> criteria);
	
	/**
	 * 取指定的盘点商品
	 */
	CheckGoods getCheckGoods(Long id);
	
	/**
	 * 删除指定的盘点商品
	 */
	void deleteCheckGoods(CheckGoods checkGoods) throws ServiceException;

	/**
	 * 删除指定的盘点单
	 */
	void deleteCheck(Long id) throws ServiceException;

	/**
	 * 入库盘点比较数据
	 * 
	 * @param physicalIndivMap
	 */
	String addPhysicalIndivList(Map<String, List<CheckItem>> physicalIndivMap, Long checkId);

	/**
	 * 获取盘点比较数据
	 * 
	 * @param criteria
	 * @return
	 */
	List<CheckItem> getCheckItemList(Map<String, Object> criteria);

	/**
	 * 确认盘点数据
	 * 
	 * @param check
	 */
	void confirmCheckItem(Check check, String confirmType, String skuCode, String remark);
	
	/**
	 * 确认盘点数据(配件)
	 */
	void confirmCheckItemPart(Check check, String confirmType, String skuCode, String remark);

	/**
	 * 查询盘点商品
	 * 
	 * @param criteria
	 * @return
	 */
	CheckGoods getCheckGoods(Map<String, Object> criteria);

	List<CheckStatusException> checkPhysicalIndivList(
			Map<String, List<CheckItem>> physicalIndivMap, Long id);

	void addCheckStatusExceptionsList(
			List<CheckStatusException> checkStatusExceptionList);

	List<CheckStatusException> getCheckStatusExceptionList(Long checkId);

	void auditCheck(Long id);

	Integer getStockHistoryListTotal(Map<String, Object> criteria);

	List<Stock> getStockHistoryList(Map<String, Object> criteria, Page page);

}
