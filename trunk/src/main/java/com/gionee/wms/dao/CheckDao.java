package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Check;
import com.gionee.wms.entity.CheckGoods;
import com.gionee.wms.entity.CheckItem;
import com.gionee.wms.entity.CheckStatusException;
import com.gionee.wms.entity.CheckTask;
import com.gionee.wms.entity.Stock;

@BatisDao
public interface CheckDao {
	/**
	 * 添加盘点单
	 */
	int addCheck(Check check);

	/**
	 * 更新盘点单
	 */
	int updateCheck(Check check);

	/**
	 * 删除盘点单
	 */
	int deleteCheck(Long id);

	/**
	 * 添加盘点商品清单
	 */
	int addStockCheckDetail(Map<String, Object> stockCheckDetail);
	
	/**
	 * 添加盘点商品清单
	 */
	int addGoodsList(List<CheckGoods> goodsList);

	/**
	 * 根据盘点单ID删除对应商品清单
	 */
	int deleteGoodsListByCheckId(Long checkId);

	/**
	 * 删除指定的盘点商品
	 */
	int deleteGoods(Long id);

	/**
	 * 更新指定的盘点明细项
	 */
	int updateDetailItem(CheckGoods item);

	/**
	 * 添加盘点任务
	 */
	int addCheckTask(CheckTask checkTask);

	/**
	 * 分页查询符合条件的盘点单列表
	 */
	List<Check> queryCheckByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的盘点单总数
	 */
	int queryCheckTotal(Map<String, Object> criteria);
	
	/**
	 * 获取指定的盘点单
	 */
	Check queryCheck(Long id);

	/**
	 * 分页查询符合条件的商品清单
	 */
	List<CheckGoods> queryGoodsByPage(Map<String, Object> criteria);
	
	/**
	 * 根据盘点单ID获取商品清单
	 */
	List<CheckGoods> queryGoodsListByCheckId(Long checkId);

	/**
	 * 查询符合条件的盘点商品总数
	 */
	int queryGoodsTotal(Map<String, Object> criteria);
	
	/**
	 * 获取指定的盘点商品
	 */
	CheckGoods queryGoods(Long id);

	/**
	 * 批量增加盘点数据
	 * 
	 * @param comparedItem
	 */
	void addCheckItemList(List<CheckItem> comparedItem);

	/**
	 * 获取盘点比较结果数据
	 * 
	 * @param criteria
	 * @return
	 */
	List<CheckItem> queryCheckItemList(Map<String, Object> criteria);

	/**
	 * 增加盘点商品
	 * 
	 * @param checkGoods
	 */
	void addCheckGoods(CheckGoods checkGoods);

	/**
	 * 更新盘点商品
	 * 
	 * @param criteria
	 */
	void updateCheckGoods(Map<String, Object> criteria);

	/**
	 * 查询盘点商品
	 * 
	 * @param criteria
	 * @return
	 */
	CheckGoods queryGoodsByMap(Map<String, Object> criteria);

	void addCheckStatusExceptionsList(
			List<CheckStatusException> checkStatusExceptionsList);

	List<CheckStatusException> getCheckStatusExceptionList(Long checkId);

	int deleteCheckStatusExceptions(Long checkId);

	int deleteCheckItemByCheckId(Long checkId);

	/**
	 * 在审核的时候生成历史库存信息
	 * 
	 * @param Check
	 *            check
	 * @return
	 */
	void addStockHistory(Check check);

	/**
	 * 查询库存历史记录条数
	 * 
	 * @param criteria
	 * @return
	 */
	Integer queryStockHistoryTotal(Map<String, Object> criteria);

	/**
	 * 查询库存历史
	 * 
	 * @param criteria
	 * @return
	 */

	List<Stock> queryStockHistoryList(Map<String, Object> criteria);

}
