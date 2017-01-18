package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.Receive;
import com.gionee.wms.entity.ReceiveGoods;
import com.gionee.wms.service.ServiceException;

public interface ReceiveService {
	/**
	 * 分页取收货单
	 */
	List<Receive> getReceiveList(Map<String, Object> criteria, Page page);

	/**
	 * 取收货单总数
	 */
	int getReceiveTotal(Map<String, Object> criteria);
	
	/**
	 * 取收货单
	 */
	Receive getReceive(Long id);
	
	/**
	 * 分页取符合条件的收货商品列表
	 */
	List<ReceiveGoods> getReceiveGoodsByPage(Map<String, Object> criteria, Page page);

	/**
	 * 取符合条件的收货商品总数
	 */
	int getReceiveGoodsTotal(Map<String, Object> criteria);

	/**
	 * 取收货单对应的商品清单
	 */
	List<ReceiveGoods> getReceiveGoodsList(Long receiveId);
	
	/**
	 * 取指定的收货商品
	 */
	ReceiveGoods getReceiveGoods(Long id);
	
	/**
	 * 分页取符合条件下的收货汇总列表
	 */
	List<ReceiveSummary> getReceiveSummaryList(Map<String, Object> criteria, Page page) throws ServiceException;

	/**
	 * 用户区域分析统计报表
	 */
	List<Map<String,Object>> getUserAreaList(Map<String, Object> criteria, Page page) throws ServiceException;

	List<Map<String,Object>> getUserAreaForCascade(Map<String, Object> criteria) throws ServiceException;

	Integer getUserAreaTotle(Map<String, Object> criteria);


	List<Map<String,Object>> orderInFactList(Map<String,Object> map, Page page);

	int orderInFactTotle(Map<String,Object> map);


	List<Map<String,Object>> orderInFactCascade(Map<String,Object> map);

	/**
	 * 取符合条件下的收货汇总列表总数
	 */
	Integer getReceiveSummaryTotal(Map<String, Object> criteria);

	/**
	 * 根据采购预收单创建采购收货单
	 */
	void addPurchaseReceiveByPreRecv(PurPreRecv purPreRecv);
	
	/**
	 * 创建快捷收货单
	 */
	void addShortcutReceive(Receive receive);
	
	/**
	 * 添加快捷收货商品及个体绑定信息
	 */
	void addShortcutRecvGoodsWithIndivList(ReceiveGoods goods, List<IndivFlow> indivList);
	
	/**
	 * 添加快捷收货商品及个体绑定信息(采购退货情况)
	 */
	void addShortcutRecvGoodsWithIndivListRMA(ReceiveGoods goods, List<IndivFlow> indivList);
	
	/**
	 * 添加快捷收货商品
	 */
	void addShortcutRecvGoods(ReceiveGoods goods);
	
	/**
	 * 添加退货入库单
	 */
	Receive addRmaReceive(Receive receive,Integer waresStatus);
	
	/**
	 * 拒收单
	 */
	Receive refuseRmaReceive(Receive receive,Integer waresStatus);
	
	/**
	 * 确认采购收货单
	 */
	public void confirmPurchaseRecv(Receive receive);
	
	/**
	 * 更新收货商品
	 */
	void updateReceiveGoods(ReceiveGoods goods);
	
	/**
	 * 更新采购收货商品及个体绑定信息
	 */
	void updatePurchaseRecvGoodsWithIndivList(ReceiveGoods goods,List<IndivFlow> indivList);
	
	/**
	 * 删除收货商品
	 */
	void deleteReceiveGoods(Long goodsId);
	
	/**
	 * 添加退货商品个体
	 
	void addRmaIndiv(Receive receive,IndivFlow indivFlow)throws ServiceException;*/
	
//	/**
//	 * 为指定的退货入库单添加入库明细项
//	 */
//	void addRmaInItem(StockInItem stockInItem) throws ServiceException;
	
	/**
	 * 根据编码取入库单
	 */
	Receive getReceiveByReceiveCode(String receiveCode);
	
	/**
	 * 根据销售订单ID取收货单
	 */
	Receive getReceiveByOrderId(Long orderId);
	
	
}
