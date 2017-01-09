package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.common.WmsConstants.StockType;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.stock.StockService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("indivService")
public class IndivServiceImpl implements IndivService {
	private IndivDao indivDao;
	private StockService stockService;

	@Override
	public List<Indiv> getIndivList(Map<String, Object> criteria, Page page) throws ServiceException {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return indivDao.queryIndivByPage(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getIndivListTotal(Map<String, Object> criteria) {
		return indivDao.queryIndivTotal(criteria);
	}

	// @Override
	// public List<Indiv> getIndivListByCodes(Set<String> indivCodes) throws
	// ServiceException {
	// try {
	// return indivDao.queryIndivListByCodes(indivCodes);
	// } catch (DataAccessException e) {
	// throw new ServiceException(e);
	// }
	// }

	@Override
	public Indiv getIndiv(Long id) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", id);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		List<Indiv> list = getIndivList(criteria, page);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public Indiv getIndivByCode(String indivCode) {
		return indivDao.queryIndivByCode(indivCode);
//		Map<String, Object> criteria = Maps.newHashMap();
//		criteria.put("indivCode", indivCode);
//		Page page = new Page();
//		page.setStartRow(1);
//		page.setEndRow(1);
//		List<Indiv> list = getIndivList(criteria, page);
//		if (CollectionUtils.isEmpty(list)) {
//			return null;
//		} else if (list.size() == 1) {
//			return list.get(0);
//		} else {
//			throw new RuntimeException("数据异常");
//		}
	}

	@Override
	public void updateIndiv(Indiv indiv) throws ServiceException {
		Indiv oldIndiv = getIndiv(indiv.getId());
		if (oldIndiv == null || oldIndiv.getWaresStatus() == null) {
			throw new ServiceException("商品个体不存在或商品状态异常");
		}
		if (IndivStockStatus.OUT_WAREHOUSE.getCode()==oldIndiv.getStockStatus()) {
			throw new ServiceException("已出库的商品不能更改商品状态");
		}

		try {
			// 保存商品个体更改
			indivDao.updateIndiv(indiv);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 如果商品状态变更，则调整库存
		if (!indiv.getWaresStatus().equals(oldIndiv.getWaresStatus())) {
			if (IndivWaresStatus.DEFECTIVE.getCode() == indiv.getWaresStatus()) {// 良品转次品
				StockRequest stockRequest = new StockRequest(oldIndiv.getWarehouseId(), oldIndiv.getSkuId(), StockType.STOCK_SALES, StockType.STOCK_UNSALES, 1,
						StockBizType.CONVERT_SALES2UNSALES, oldIndiv.getIndivCode());
				stockService.convertStock(stockRequest);
			} else {// 次品转良品
				StockRequest stockRequest = new StockRequest(oldIndiv.getWarehouseId(), oldIndiv.getSkuId(), StockType.STOCK_UNSALES, StockType.STOCK_SALES, 1,
						StockBizType.CONVERT_UNSALES2SALES, oldIndiv.getIndivCode());
				stockService.convertStock(stockRequest);
			}
		}

		// TODO:同步库存到外围系统
	}
	
	@Override
	public void updateIndivStockStatus(Indiv indiv) throws ServiceException {
		Indiv oldIndiv = getIndiv(indiv.getId());
		if (oldIndiv == null || oldIndiv.getWaresStatus() == null) {
			throw new ServiceException("商品个体不存在或商品状态异常");
		}
		// 只更改商品库存状态，不调整库存
		try {
			// 保存商品个体更改
			indivDao.updateIndiv(indiv);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateIndivList(List<Indiv> indivList) throws ServiceException {
		try {
			indivDao.batchUpdateIndiv(indivList);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addIndiv(Indiv indiv) throws ServiceException {
		try {
			indivDao.addIndiv(indiv);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addIndivs(List<Indiv> indivs) throws ServiceException {
		try {
			indivDao.addIndivs(indivs);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Indiv> getIndivsByStockInItemId(Long stockInItemId) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockInItemId", stockInItemId);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		return getIndivList(criteria, page);
	}

	@Override
	public List<Indiv> getIndivsByStockOutItemId(Long stockOutItemId) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockOutItemId", stockOutItemId);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		return getIndivList(criteria, page);
	}

	@Override
	public List<Indiv> getDeliveryIndivs(String deliveryCode, Long skuId) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("outCode", deliveryCode);
		criteria.put("skuId", skuId);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		return getIndivList(criteria, page);
	}

	@Override
	public List<Indiv> getUnpushedDeliveredIndivList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return indivDao.queryUnpushedDeliveredIndivByPage(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getUnpushedDeliveredIndivTotal(Map<String, Object> criteria) {
		try {
			return indivDao.queryUnpushedDeliveredIndivCount(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateIndivsByInItemIds(List<Long> stockInItemIds, Map<String, Object> indivInfo)
			throws ServiceException {
		indivInfo.put("stockInItemIds", stockInItemIds);
		try {
			indivDao.updateIndivsByInItemIds(indivInfo);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	private void updateIndivsByOutItemIds(List<Long> stockOutItemIds, Map<String, Object> indivInfo)
			throws ServiceException {
		indivInfo.put("stockOutItemIds", stockOutItemIds);
		try {
			indivDao.updateIndivsByOutItemIds(indivInfo);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateIndivsByOutItemId(Long stockOutItemId, Map<String, Object> indivInfo) throws ServiceException {
		updateIndivsByOutItemIds(Lists.newArrayList(stockOutItemId), indivInfo);
	}

	@Override
	public List<IndivFlow> getIndivFlowList(Map<String, Object> criteria, Page page) throws ServiceException {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return indivDao.queryIndivFlowByPage(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getIndivFlowListTotal(Map<String, Object> criteria) {
		return indivDao.queryIndivFlowListTotal(criteria);
	}

	@Override
	public List<IndivFlow> getIndivFlowsByFlowItemId(String flowType, Long flowItemId) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("flowType", flowType);
		criteria.put("flowItemId", flowItemId);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		return getIndivFlowList(criteria, page);
	}

	@Autowired
	public void setIndivDao(IndivDao indivDao) {
		this.indivDao = indivDao;
	}

	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	@Override
	public List<Indiv> getIndivList(Map<String, Object> criteria) {
		return indivDao.queryIndivList(criteria);
	}
	@Override
	public List<Indiv> getIndivListByCaseCode(String caseCode) {
		return indivDao.queryIndivListByCaseCode(caseCode);
	}
}
