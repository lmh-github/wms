package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.entity.Receive;
import com.gionee.wms.entity.ReceiveGoods;

@BatisDao
public interface ReceiveDao {
	/**
	 * 分页查询符合条件的收货单列表.
	 */
	List<Receive> queryReceiveByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的收货单总数
	 */
	int queryReceiveTotal(Map<String, Object> criteria);

	/**
	 * 根据ID获取收货单
	 */
	Receive queryReceive(Long id);

	/**
	 * 根据收货编号获取收货单
	 */
	Receive queryReceiveByReceiveCode(String receiveCode);
	
	/**
	 * 根据源单ID获取收货单
	 */
	Receive queryReceiveByOriginalId(Map<String, Object> criteria);
	
	/**
	 * 添加收货单
	 */
	int addReceive(Receive receive);

	/**
	 * 更新收货单
	 */
	int updateReceive(Receive receive);

	/**
	 * 添加收货商品清单
	 */
	int addGoodsList(List<ReceiveGoods> goodsList);

	/**
	 * 分页获取符合条件下的收货商品列表
	 */
	List<ReceiveGoods> queryReceiveGoodsByPage(Map<String, Object> criteria);

	/**
	 * 获取符合条件下的收货商品列表总数
	 */
	int queryReceiveGoodsTotal(Map<String, Object> criteria);

	/**
	 * 根据收货单ID取对应的商品清单
	 */
	List<ReceiveGoods> queryGoodsListByReceiveId(Long receiveId);

	/**
	 * 根据ID获取收货商品
	 */
	ReceiveGoods queryGoods(Long id);

	/**
	 * 更新收货商品
	 */
	int updateGoods(ReceiveGoods goods);
	
	/**
	 * 添加收货商品
	 */
	int addGoods(ReceiveGoods goods);
	
	/**
	 * 删除收货商品
	 */
	int deleteGoods(Long id);

	/**
	 * 分页取收货汇总列表
	 */
	List<ReceiveSummary> queryReceiveSummaryByPage(Map<String, Object> criteria);

	/**
	 * 查询收货汇总列表总数
	 */
	int queryReceiveSummaryTotal(Map<String, Object> criteria);

	/**
	 * 根据收货单ID取收货汇总
	 */
	List<ReceiveSummary> queryReceiveSummaryByReceiveId(Map<String, Object> criteria);

	/**
	 * 用户区域分布统计报表
	 * @param criteria
	 * @return
	 */
	List<Map<String,Object>> getUserAreaList(Map<String,Object> criteria);


	List<Map<String,Object>> getUserAreaForCascade(Map<String,Object> criteria);



	/**
	 * 用户区域分布统计报表
	 * @param criteria
	 * @return
	 */
	int getUserAreaTotle(Map<String, Object> criteria);

	/**
	 *正想订单实效统计报表
	 */
	List<Map<String,Object>> orderInFactList(Map<String,Object> map);

	int orderInFactTotle(Map<String,Object> map);

	List<Map<String,Object>> orderInFactCascade(Map<String,Object> criteria);
}
