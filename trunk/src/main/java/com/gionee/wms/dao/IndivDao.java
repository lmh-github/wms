package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;

@BatisDao
public interface IndivDao {
	/**
	 * 分页查询商品个体
	 */
	List<Indiv> queryIndivByPage(Map<String, Object> criteria);

	/**
	 * 查询商品个体总数
	 */
	int queryIndivTotal(Map<String, Object> criteria);

	/**
	 * 查询符合条件的商品个体列表
	 */
	List<Indiv> queryIndivList(Map<String, Object> criteria);
	
	/**
	 * 根据个体编号批量获取个体列表
	 */
	List<Indiv> queryIndivListByCodes(List<String> indivCodes);
	
	/**
	 * 根据个体编号批量获取出库的个体列表(退仓和刷单的使用)
	 */
	List<Indiv> queryIndivListByOutCodes(Map<String, Object> criteria);

	/**
	 * 分页获取未被推送的已发货的销售商品个体
	 */
	List<Indiv> queryUnpushedDeliveredIndivByPage(Map<String, Object> criteria);
	
	/**
	 * 获取未被推送的已发货的销售商品个体总数
	 */
	int queryUnpushedDeliveredIndivCount(Map<String, Object> criteria);

	/**
	 * 批量更新商品个体的出库信息
	 */
	int updateIndivsOutInfo(Map<String, Object> criteria);

	/**
	 * 更新库存个体
	 */
	int updateIndiv(Indiv indiv);
	
	/**
	 * 更新库存个体
	 * @param indiv
	 * @return
	 */
	int updateIndiv1(Indiv indiv);

	/**
	 * 批量更新商品个体
	 */
	int batchUpdateIndiv(List<Indiv> indivList);

	/**
	 * 添加库存个体
	 */
	int addIndiv(Indiv indiv);

	/**
	 * 批量添加商品身份信息
	 */
	int addIndivs(List<Indiv> indivs);

	/**
	 * 批量更新与指定入库明细项相关联的商品身份信息
	 */
	int updateIndivsByInItemIds(Map<String, Object> criteria);

	/**
	 * 批量更新与指定出库明细项相关联的商品身份信息
	 */
	int updateIndivsByOutItemIds(Map<String, Object> criteria);

	/**
	 * 清空个体发货信息
	 */
	int clearIndivDeliveryInfo(Map<String, Object> criteria);

	/**
	 * 根据发货单ID清空个体发货信息
	 */
	int clearIndivDeliveryInfoByOutId(Long outId);

	/**
	 * 添加个体发货信息
	 */
	int addIndivDeliveryInfo(Map<String, Object> criteria);

	/**
	 * 批量更新指定编号的个体信息
	 */
	int updateIndivsByCodes(Map<String, Object> criteria);

	/**
	 * 批量更新商品个体的出库确认信息
	 */
	int updateIndivsOutConfirmInfo(Map<String, Object> criteria);

	/**
	 * 批量更新商品个体的退货信息
	 */
	int updateIndivsRmaInfo(Map<String, Object> criteria);
	
	/**
	 * 批量删除商品个体
	 */
	int deleteIndivsByCodes(List<String> indivCodeList);

	/**
	 * 查询商品个体流转历史列表
	 */
	List<IndivFlow> queryIndivFlowByPage(Map<String, Object> criteria);

	/**
	 * 查询商品个体流转历史列表总数
	 */
	int queryIndivFlowListTotal(Map<String, Object> criteria);
	
	/**
	 * 查询商品个体流转信息
	 */
	List<IndivFlow> queryIndivFlowList(Map<String, Object> criteria);

	/**
	 * 批量添加商品个体流转信息
	 */
	int addIndivFlows(List<IndivFlow> indivFlows);

	/**
	 * 删除与指定流转商品相关联的商品个体流转信息
	 */
	int deleteIndivFlowsByFlowGoodsId(Map<String, Object> criteria);

	/**
	 * 删除与指定流转编号相关联的商品个体流转信息
	 */
	int deleteIndivFlowsByFlowCode(Map<String, Object> criteria);

	/**
	 * 更新与指定流转单编号相关联的商品个体流转信息
	 */
	int updateIndivFlowsByFlowCode(Map<String, Object> criteria);

	/**
	 * 根据流转单ID删除商品个体流转信息 
	 */
	int deleteIndivFlowsByFlowId(Map<String, Object> criteria);

	/**
	 * 批量更新商品个体流转确认信息
	 */
	int updateIndivsFlowConfirmInfo(Map<String, Object> criteria);
	
	/**
	 * 批量删除商品个体流转信息
	 */
	int deleteIndivFlowsByCodes(List<String> indivCodeList);
	
	/**
	 * 根据入库ID更新商品个体信息
	 */
	int updateIndivsByInId(Map<String, Object> criteria);
	
	/**
	 * 根据流转单ID更新商品个体流转信息
	 */
	int updateIndivFlowsByFlowId(Map<String, Object> criteria);
	
	/**
	 * 根据个体编码获得商品个体
	 */
	Indiv queryIndivByCode(String indivCode);

	/**
	 * 批量更新相同单的个体信息
	 */
	int batchUpdateIndivsStock(Map<String, Object> criteria);
	
	/**
	 * 批量更新相同(订)单号的个体状态
	 */
	int updateIndivStatusByOutId(Map<String, Object> criteria);
	
	/**
	 * 更新配货状态
	 */
	int updateIndivPrepare(Map<String, Object> params);
	
	/**
	 * 批量更新配货状态
	 * @param params
	 * @return
	 */
	int updateIndivsPrepare(Map<String,Object> criteria);
	
	
	int updatePrepareToDly(Map<String, Object> criteria);
	
	/**
	 *  取消个体与调拨的关联
	 */
	int updateTransferCancel(Map<String, Object> params);
	
	//按箱号查询
	List<Indiv> queryIndivListByCaseCode(String caseCode);
}
