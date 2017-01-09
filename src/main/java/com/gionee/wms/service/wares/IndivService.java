package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.service.ServiceException;

public interface IndivService {
	/**
	 * 分页取商品个体列表.
	 */
	List<Indiv> getIndivList(Map<String, Object> criteria, Page page) throws ServiceException;

	/**
	 * 取库存商品个体列表总数.
	 */
	Integer getIndivListTotal(Map<String, Object> criteria);

	/**
	 * 取商品个体
	 */
	Indiv getIndiv(Long id) throws ServiceException;

	/**
	 * 根据个体编码取商品个体
	 */
	Indiv getIndivByCode(String indivCode) throws ServiceException;

	// /**
	// * 根据个体编号批量获取个体列表
	// */
	// List<Indiv> getIndivListByCodes(Set<String> indivCodes);

	/**
	 * 更新商品个体信息
	 */
	void updateIndiv(Indiv indiv) throws ServiceException;
	
	/**
	 * 批量更新商品个体信息
	 */
	void updateIndivList(List<Indiv> indivList) throws ServiceException;

	/**
	 * 添加库存个体信息
	 */
	void addIndiv(Indiv indiv) throws ServiceException;

	/**
	 * 批量添加商品身份信息
	 */
	void addIndivs(List<Indiv> indivs) throws ServiceException;

	// /**
	// * 批量更新商品个体的出库信息
	// */
	// int updateIndivsOutInfo(StockOutItem stockOutItem,Set<String>
	// indivCodes)throws ServiceException;

	/**
	 * 根据入库明细项取商品身份信息
	 */
	List<Indiv> getIndivsByStockInItemId(Long stockInItemId);

	/**
	 * 根据出库明细项取商品身份信息
	 */
	List<Indiv> getIndivsByStockOutItemId(Long stockOutItemId);

	/**
	 * 批量更新与指定入库明细项相关联的商品身份信息
	 * 
	 * @param stockInItemIds 入库明细项ID集
	 * @param indivInfo 待更新的商品身份信息
	 * @throws ServiceException
	 */
	void updateIndivsByInItemIds(List<Long> stockInItemIds, Map<String, Object> indivInfo) throws ServiceException;

	/**
	 * 更新与指定出库明细项相关联的商品身份信息
	 * 
	 * @param stockInItemIds 出库明细项ID集
	 * @param indivInfo 待更新的商品身份信息
	 * @throws ServiceException
	 */
	void updateIndivsByOutItemId(Long stockOutItemId, Map<String, Object> indivInfo) throws ServiceException;

	/**
	 * 分页取商品个体流转历史列表.
	 */
	List<IndivFlow> getIndivFlowList(Map<String, Object> criteria, Page page) throws ServiceException;

	/**
	 * 取库存商品个体流转理历史列表总数.
	 */
	Integer getIndivFlowListTotal(Map<String, Object> criteria);

	/**
	 * 根据流转类型与流转单明细项ID取商品个体流转信息
	 */
	List<IndivFlow> getIndivFlowsByFlowItemId(String flowType, Long flowItemId);

	/**
	 * 取发货商品个体
	 */
	List<Indiv> getDeliveryIndivs(String deliveryCode, Long skuId);

	/**
	 * 分页取未被推送的已发货的销售商品个体
	 */
	List<Indiv> getUnpushedDeliveredIndivList(Map<String, Object> criteria, Page page);

	/**
	 * 取未被推送的已发货的销售商品个体总数
	 */
	Integer getUnpushedDeliveredIndivTotal(Map<String, Object> criteria);
	
	/**
	 * 根据条件获得个体列表
	 */
	List<Indiv> getIndivList(Map<String, Object> criteria);
	
	//按箱号查询
	List<Indiv> getIndivListByCaseCode(String caseCode);
	
	/**
	 * 更新商品个体库存状态
	 */
	void updateIndivStockStatus(Indiv indiv) throws ServiceException;
}
