package com.gionee.wms.service.stock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.common.WmsConstants.StockCheckCompareType;
import com.gionee.wms.common.WmsConstants.StockCheckStatus;
import com.gionee.wms.common.WmsConstants.StockType;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dao.CheckDao;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.StockDao;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.Check;
import com.gionee.wms.entity.CheckGoods;
import com.gionee.wms.entity.CheckItem;
import com.gionee.wms.entity.CheckStatusException;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("checkService")
public class CheckServiceImpl extends CommonServiceImpl implements CheckService {
	@Autowired
	private CheckDao checkDao;
	@Autowired
	private StockDao stockDao;
	@Autowired
	private WaresDao waresDao;
	@Autowired
	private IndivDao indivDao;
	@Autowired
	private StockService stockService;

	@Override
	public void addCheck(Check check) throws ServiceException {
		check.setCheckCode(getBizCode(STOCK_CHECK));
		check.setPreparedBy(ActionUtils.getLoginName());
		check.setPreparedTime(new Date());
		check.setHandlingStatus(StockCheckStatus.PENDING.getCode());
		try {
			// 保存盘点单
			if (checkDao.addCheck(check) == 0) {
				throw new ServiceException("盘点单不能重复添加");
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void addCheckGoodsList(Check check, List<Long> skuIds)
			throws ServiceException {
		if (check == null
				|| StockCheckStatus.PENDING.getCode() != check
						.getHandlingStatus()) {
			throw new ServiceException("盘点已开始，不能再添加商品");
		}
		try {
			List<Sku> skuList = waresDao.querySkuListByIds(skuIds);
			if (CollectionUtils.isEmpty(skuList)) {
				throw new ServiceException("SKU不存在");
			}
			List<CheckGoods> goodsList = Lists.newArrayList();
			for (Sku sku : skuList) {
				CheckGoods goods = new CheckGoods();
				goods.setCheckId(check.getId());
				goods.setSkuId(sku.getId());
				goods.setSkuCode(sku.getSkuCode());
				goods.setSkuName(sku.getSkuName());
				goods.setMeasureUnit(sku.getWares().getMeasureUnit());
				goodsList.add(goods);
			}
			if (checkDao.addGoodsList(goodsList) != goodsList.size()) {
				throw new ServiceException("同一SKU不能重复添加");
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addPhysicalGoodsList(Check check,
			Map<String, CheckGoods> physicalGoodsList) throws ServiceException {
		try {

			if (check == null
					|| StockCheckStatus.FINISHED.getCode() == check
							.getHandlingStatus()) {
				throw new ServiceException("盘点单不存在或重复处理已完成的盘点任务");
			}
			List<CheckGoods> bookGoodsList = getCheckGoodsList(check.getId());
			if (CollectionUtils.isEmpty(bookGoodsList)) {
				throw new ServiceException("待盘点商品明细不存在");
			}
			if (physicalGoodsList.size() != bookGoodsList.size()) {
				throw new ServiceException("实盘商品与盘点单商品不一致");
			}
			// 更新盘点数据
			for (CheckGoods bookGoods : bookGoodsList) {
				CheckGoods physicalGoods = physicalGoodsList.get(bookGoods
						.getSkuCode());
				if (physicalGoods == null) {
					throw new ServiceException("实盘数据不完整");
				} else if (!bookGoods.getMeasureUnit().equals(
						physicalGoods.getMeasureUnit())) {
					throw new ServiceException("计量单位不符");
				} else {
					bookGoods.setBookNondefective(bookGoods.getStock()
							.getSalesQuantity()
							+ bookGoods.getStock().getOccupyQuantity());
					bookGoods.setBookDefective(bookGoods.getStock()
							.getUnsalesQuantity());
					bookGoods.setFirstNondefective(physicalGoods
							.getFirstNondefective());
					bookGoods.setFirstNondefectivePl(physicalGoods
							.getFirstNondefective()
							- bookGoods.getBookNondefective());
					bookGoods.setFirstDefective(physicalGoods
							.getFirstDefective());
					bookGoods
							.setFirstDefectivePl(physicalGoods
									.getFirstDefective()
									- bookGoods.getBookDefective());
				}
			}
			for (CheckGoods systemItem : bookGoodsList) {
				checkDao.updateDetailItem(systemItem);
			}
			// 更新盘点单
			check.setFirstTime(new Date());// 设置实际盘点时间
			check.setHandlingStatus(StockCheckStatus.UNCONFIRMED.getCode());
			check.setHandledTime(new Date());
			check.setHandledBy(ActionUtils.getLoginName());
			checkDao.updateCheck(check);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Check getCheck(Long id) {
		return checkDao.queryCheck(id);
	}

	@Override
	public List<CheckGoods> getCheckGoodsList(Map<String, Object> criteria,
			Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		return checkDao.queryGoodsByPage(criteria);
	}

	@Override
	public List<CheckGoods> getCheckGoodsList(Long checkId) {
		return checkDao.queryGoodsListByCheckId(checkId);
	}

	@Override
	public Integer getCheckGoodsTotal(Map<String, Object> criteria) {
		return checkDao.queryGoodsTotal(criteria);
	}

	@Override
	public CheckGoods getCheckGoods(Long id) {
		return checkDao.queryGoods(id);
	}

	// @Override
	// public void assignCheckTask(CheckTask checkTask) throws ServiceException
	// {
	// checkTask.setCreateTime(new Date());
	// checkTask.setTaskStatus(CheckTaskStatus.PENDING.toString());
	// try {
	// stockCheckDao.addCheckTask(checkTask);
	// StockCheck stockCheck = checkTask.getStockCheck();
	// stockCheck.setHandlingStatus(StockCheckStatus.ASSIGNED.toString());
	// stockCheckDao.updateStockCheck(stockCheck);
	// } catch (DataAccessException e) {
	// throw new ServiceException("database error.", e);
	// }
	//
	// }

	@Override
	public List<Check> getCheckList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		return checkDao.queryCheckByPage(criteria);
	}

	@Override
	public Integer getCheckTotal(Map<String, Object> criteria) {
		return checkDao.queryCheckTotal(criteria);
	}

	@Override
	public void deleteCheck(Long id) throws ServiceException {
		Check check = getCheck(id);
		if (check == null) {
			throw new ServiceException("盘点单不存在");
		}
		if (StockCheckStatus.PENDING.getCode() != check.getHandlingStatus()) {
			throw new ServiceException("盘点单状态不正确");
		}

		// 删除盘点商品
		try {
			checkDao.deleteGoodsListByCheckId(check.getId());
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 删除盘点单
		try {
			checkDao.deleteCheck(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteCheckGoods(CheckGoods checkGoods) throws ServiceException {
		if (StockCheckStatus.PENDING.getCode() != checkGoods.getCheck()
				.getHandlingStatus()) {
			throw new ServiceException("盘点单状态异常");
		}
		try {
			checkDao.deleteGoods(checkGoods.getId());
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void confirmCheck(Check check) throws ServiceException {
		if (StockCheckStatus.UNCONFIRMED.getCode() != check.getHandlingStatus()) {
			throw new ServiceException("盘点单状态异常");
		}

		try {
			// 更新盘点单信息
			check.setHandlingStatus(StockCheckStatus.FINISHED.getCode());
			check.setHandledTime(new Date());
			check.setHandledBy(ActionUtils.getLoginName());
			checkDao.updateCheck(check);

			// 更新各SKU的库存盘点盈亏数据
			// stockDao.updateStockCheckInfoByCheckId(check.getId());
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void downloadCheckGoodsList(Long checkId) throws ServiceException {
		// 取指定的盘点信息
		Check check = getCheck(checkId);
		if (check == null) {
			throw new ServiceException("盘点单不存在");
		}
		if (StockCheckStatus.FINISHED.getCode() == check.getHandlingStatus()) {
			throw new ServiceException("盘点已完成，不能下载");
		}
		List<CheckGoods> goodsList = getCheckGoodsList(Long.valueOf(checkId));
		if (CollectionUtils.isEmpty(goodsList)) {
			// throw new ServiceException("盘点商品明细不存在");
		}
		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		Map<String, String> onceData = new HashMap<String, String>();
		onceData.put("WAREHOUSE_NAME", check.getWarehouseName());
		sheetData.add(onceData);
		for (CheckGoods goods : goodsList) {
			Map<String, String> repeatData = new HashMap<String, String>();
			repeatData.put("SKU_CODE", goods.getSkuCode());
			repeatData.put("SKU_NAME", goods.getSkuName());
			repeatData.put("MEASURE_UNIT", goods.getMeasureUnit());
			sheetData.add(repeatData);
		}
		ExcelModule excelModule = new ExcelModule(sheetData);

		HttpServletResponse response = ServletActionContext.getResponse();
		// 清空输出流
		response.reset();
		// 设置响应头和下载保存的文件名
		response.setHeader("content-disposition", "attachment;filename="
				+ WmsConstants.CHECK_EXCEL_DOWNLOAD_NAME);
		// 定义输出类型
		response.setContentType("APPLICATION/msexcel");
		OutputStream out = null;
		try {
			String templeteFile = ActionUtils.getClassPath()
					+ WmsConstants.CHECK_EXP_TEMPLETE_FILE;
			String tempFile = ActionUtils.getClassPath()
					+ WmsConstants.EXCEL_TEMP_PATH
					+ WmsConstants.CHECK_EXCEL_DOWNLOAD_NAME;
			File file = ExcelExpUtil.expExcel(excelModule, templeteFile,
					tempFile);
			out = response.getOutputStream();
			FileUtils.copyFile(file, out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// 返回null,可防止报异常：getOutputStream() has already been called for this
		// response

	}

	@Override
	public String addPhysicalIndivList(
			Map<String, List<CheckItem>> physicalIndivMap, Long checkId) {
		checkDao.deleteCheckItemByCheckId(checkId);// 清除盘点单盘点的历史数据
													// 主要是考虑到二次上传盘点数据
		checkDao.deleteGoodsListByCheckId(checkId);// 清除盘点单盘点的历史数据
		String result = "";
		Check check = checkDao.queryCheck(checkId);
		// 遍历sku，根据sku获取系统所有在库个体（包括出库中），然后与盘点单中个体进行比较，入库盘点多出的数据和系统多出的数据
		for (Map.Entry<String, List<CheckItem>> entry : physicalIndivMap
				.entrySet()) {
			int defectiveCount = 0;// 实盘次品数
			int nonDefectiveCount = 0;// 实盘良品数
			int bookDefectiveCount = 0;// 账面次品数
			int bookNonDefectiveCount = 0;// 账面良品数
			String skuCode = entry.getKey().toString();
			List<CheckItem> itemList = entry.getValue();
			Map<String, Object> criteria = Maps.newHashMap();
			criteria.put("skuCode", skuCode);
			// criteria.put("wareHouseId", check.getWarehouseId());
			criteria.put("stockStatusNotIn",
					IndivStockStatus.OUT_WAREHOUSE.getCode());
			List<Indiv> indivList = indivDao.queryIndivList(criteria);

			Map<String, Object> criteria1 = Maps.newHashMap();
			criteria1.put("skuCode", skuCode);
			// criteria1.put("warehouseId", check.getWarehouseId());
			List<Stock> stockList = stockDao.queryStockList(criteria1);
			if (stockList.isEmpty()) {
				result = "SKU库存信息不存在:" + skuCode;
				return result;
			}
			Stock stock = stockList.get(0);
			List<CheckItem> comparedItem = Lists.newArrayList();// 记录盘比较数据
			// 比较出盘点多出的数据
			for (CheckItem item : itemList) {
				if (item.getWaresStatus() == WmsConstants.IndivWaresStatus.DEFECTIVE
						.getCode()) {
					defectiveCount++;
				} else {
					nonDefectiveCount++;
				}
				boolean writeFlag = true;
				for (Indiv indiv : indivList) {
					if (item.getIndivCode().equals(indiv.getIndivCode())) {
						writeFlag = false;
						break;
					}
				}
				if (writeFlag) {
					CheckItem ci = new CheckItem();
					ci.setCheckId(item.getCheckId());
					ci.setIndivCode(item.getIndivCode());
					ci.setIndivEnabled(item.getIndivEnabled());
					ci.setSkuCode(item.getSkuCode());
					ci.setSkuName(stock.getSku().getSkuName());
					ci.setNum(itemList.size());
					ci.setCompareType("1");
					ci.setWaresStatus(item.getWaresStatus());
					comparedItem.add(ci);
				}
			}
			// 比较出系统多出的数据
			for (Indiv indiv : indivList) {
				if (indiv.getWaresStatus() == WmsConstants.IndivWaresStatus.DEFECTIVE
						.getCode()) {
					bookDefectiveCount++;
				} else {
					bookNonDefectiveCount++;
				}
				boolean writeFlag = true;
				CheckItem ci = new CheckItem();
				for (CheckItem item : itemList) {
					ci.setCheckId(item.getCheckId());
					ci.setSkuCode(indiv.getSkuCode());
					ci.setSkuName(indiv.getSkuName());
					ci.setNum(itemList.size());
					ci.setIndivEnabled(item.getIndivEnabled());
					ci.setWaresStatus(indiv.getWaresStatus());
					if (item.getIndivCode().equals(indiv.getIndivCode())) {
						writeFlag = false;
						break;
					}
				}
				if (writeFlag) {
					ci.setIndivCode(indiv.getIndivCode());
					ci.setCompareType("2");
					comparedItem.add(ci);
				}
			}

			// 入库盘点数量统计
			if (comparedItem.size() > 0) {
				checkDao.addCheckItemList(comparedItem);
			}
			CheckGoods checkGoods = new CheckGoods();
			checkGoods.setCheckId(checkId);
			checkGoods.setSkuId(stock.getSku().getId());
			checkGoods.setSkuCode(skuCode);
			checkGoods.setSkuName(stock.getSku().getSkuName());
			checkGoods.setMeasureUnit(stock.getSku().getWares()
					.getMeasureUnit());
			// checkGoods.setBookNondefective(stock.getSalesQuantity());
			checkGoods.setBookNondefective(bookNonDefectiveCount);
			checkGoods.setBookDefective(bookDefectiveCount);
			checkGoods.setFirstNondefective(nonDefectiveCount);
			checkGoods.setFirstNondefectivePl(nonDefectiveCount
					- bookNonDefectiveCount);
			checkGoods.setFirstDefective(defectiveCount);
			checkGoods.setFirstDefectivePl(defectiveCount - bookDefectiveCount);
			checkGoods.setSecondDefective(0);
			checkGoods.setSecondNondefective(0);
			checkGoods.setSendDefectivePl(0);
			checkGoods.setSendNondefectivePl(0);
			checkDao.addCheckGoods(checkGoods);

		}
		// 更新盘点单为待确认
		check.setHandlingStatus(StockCheckStatus.UNCONFIRMED.getCode());
		check.setHandledTime(new Date());
		check.setHandledBy(ActionUtils.getLoginName());
		checkDao.updateCheck(check);
		return result;
	}

	@Override
	public List<CheckItem> getCheckItemList(Map<String, Object> criteria) {
		return checkDao.queryCheckItemList(criteria);
	}

	@Override
	public void confirmCheckItem(Check check, String confirmType,
			String skuCode, String remark) {
		if (check.getStockDumpStatus() == WmsConstants.StockDumpStatus.UNDUMP
				.getCode()) {// 盘点之前对当前库存信息进行一次历史记录
			// 前提：当前盘点单没做过记录
			checkDao.addStockHistory(check);
			check.setStockDumpStatus(WmsConstants.StockDumpStatus.DUMPED
					.getCode());
			checkDao.updateCheck(check);
		}
		// 查询盘点比较数据
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("checkId", check.getId());
		criteria.put("skuCode", skuCode);
		Map<String, Object> stockCriteria = Maps.newHashMap();
		stockCriteria.put("skuCode", skuCode);
		stockCriteria.put("warehouseId", check.getWarehouseId());
		Stock stock = stockDao.queryStock(stockCriteria);
		List<CheckItem> itemList = null;
		CheckGoods checkGoods = checkDao.queryGoodsByMap(criteria);// 查处对应sku的对比数据，主要是取良次品的盈亏数据
		String checkCode = check.getCheckCode();
		Long warehouseId = stock.getWarehouse().getId();
		int defectivePl = 0;// 实盘多出次品数
		int nonDefectivePl = 0;// 实盘多出良品数
		int bookDefectivePl = 0;// 系统多出次品数
		int bookNonDefectivePl = 0;// 系统多出良品数
		if (confirmType.equals("in")) {
			// 新增盘点多出的数据，compareType=1
			criteria.put("compareType",
					StockCheckCompareType.CHECK_COMPARE_MORE.getCode());
			itemList = checkDao.queryCheckItemList(criteria);
			List<Indiv> indivs = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(itemList)) {
				for (CheckItem item : itemList) {
					if (item.getWaresStatus() == WmsConstants.IndivWaresStatus.DEFECTIVE
							.getCode()) {
						defectivePl++;
					} else {
						nonDefectivePl++;
					}
					Indiv indiv = new Indiv();
					indiv.setIndivCode(item.getIndivCode());
					indiv.setInTime(new Date());
					indiv.setInId(null);
					indiv.setInCode("");
					indiv.setProductBatchNo("");
					indiv.setMeasureUnit(stock.getSku().getWares()
							.getMeasureUnit());
					indiv.setSkuId(stock.getSku().getId());
					indiv.setSkuCode(stock.getSku().getSkuCode());
					indiv.setSkuName(stock.getSku().getSkuName());
					indiv.setWarehouseId(stock.getWarehouse().getId());
					indiv.setWarehouseName(stock.getWarehouse()
							.getWarehouseName());
					indiv.setWaresStatus(item.getWaresStatus());
					indiv.setRmaCount(0);
					indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE
							.getCode());
					indiv.setPushStatus(0);
					indiv.setPushCount(0);
					indivs.add(indiv);
				}
				indivDao.addIndivs(indivs);
				criteria.put("remarkIn", remark);
				checkDao.updateCheckGoods(criteria);
				if (defectivePl > 0) {// //增加不可销库存
					StockRequest stockRequest = new StockRequest(warehouseId,
							skuCode, StockType.STOCK_UNSALES, defectivePl,
							StockBizType.CHECK_IN, checkCode);
					stockService.increaseStock(stockRequest);
				}
				if (nonDefectivePl > 0) {// 增加可销库存
					StockRequest stockRequest = new StockRequest(warehouseId,
							skuCode, StockType.STOCK_SALES, nonDefectivePl,
							StockBizType.CHECK_IN, checkCode);
					stockService.increaseStock(stockRequest);
				}
			}

		} else if (confirmType.equals("out")) {
			// 删除系统多余数据，compareType=2
			criteria.put("compareType",
					StockCheckCompareType.CHECK_COMPARE_LESS.getCode());
			itemList = checkDao.queryCheckItemList(criteria);
			List<String> indivCodeList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(itemList)) {
				for (CheckItem item : itemList) {
					if (item.getWaresStatus() == WmsConstants.IndivWaresStatus.DEFECTIVE
							.getCode()) {
						bookDefectivePl++;
					} else {
						bookNonDefectivePl++;
					}
					indivCodeList.add(item.getIndivCode());
				}
				indivDao.deleteIndivsByCodes(indivCodeList);
				criteria.put("remarkOut", remark);
				checkDao.updateCheckGoods(criteria);
				if (bookDefectivePl > 0) {// 减少不可销库存
					StockRequest stockRequest = new StockRequest(warehouseId,
							skuCode, StockType.STOCK_UNSALES, bookDefectivePl,
							StockBizType.CHECK_OUT, checkCode);
					stockService.decreaseStock(stockRequest);
				}
				if (bookNonDefectivePl > 0) {// 减少可销库存
					StockRequest stockRequest = new StockRequest(warehouseId,
							skuCode, StockType.STOCK_SALES, bookNonDefectivePl,
							StockBizType.CHECK_OUT, checkCode);
					stockService.decreaseStock(stockRequest);
				}
			}
		}
	}

	@Override
	public CheckGoods getCheckGoods(Map<String, Object> criteria) {
		return checkDao.queryGoodsByMap(criteria);
	}

	@Override
	public void confirmCheckItemPart(Check check, String confirmType,
			String skuCode, String remark) {
		if (check.getStockDumpStatus() == WmsConstants.StockDumpStatus.UNDUMP
				.getCode()) {// 盘点之前对当前库存信息进行一次历史记录
			// 前提：当前盘点单没做过记录
			checkDao.addStockHistory(check);
			check.setStockDumpStatus(WmsConstants.StockDumpStatus.DUMPED
					.getCode());
			checkDao.updateCheck(check);
		}
		// 查询盘点比较数据
		Map<String, Object> criteria = Maps.newHashMap(); // 盘点商品条件
		criteria.put("checkId", check.getId());
		criteria.put("skuCode", skuCode);
		Map<String, Object> stockCriteria = Maps.newHashMap(); // 库存条件
		stockCriteria.put("skuCode", skuCode);
		stockCriteria.put("warehouseId", check.getWarehouseId());
		Stock stock = stockDao.queryStock(stockCriteria);
		CheckGoods checkGoods = checkDao.queryGoodsByMap(criteria);
		if (confirmType.equals("in")) {
			criteria.put("remarkIn", remark);
			checkDao.updateCheckGoods(criteria);
			if (checkGoods.getFirstNondefectivePl() > 0) {
				// 增加总库存和可销售库存
				StockRequest stockRequest = new StockRequest(stock
						.getWarehouse().getId(), skuCode,
						StockType.STOCK_SALES,
						checkGoods.getFirstNondefectivePl(),
						StockBizType.CHECK_IN, check.getCheckCode());
				stockService.increaseStock(stockRequest);
			}
			if (checkGoods.getFirstDefectivePl() > 0) {
				// 增加总库存和不可销售库存(次品)
				StockRequest stockRequest1 = new StockRequest(stock
						.getWarehouse().getId(), skuCode,
						StockType.STOCK_UNSALES,
						checkGoods.getFirstDefectivePl(),
						StockBizType.CHECK_IN, check.getCheckCode());
				stockService.increaseStock(stockRequest1);
			}
		} else if (confirmType.equals("out")) {
			criteria.put("remarkOut", remark);
			checkDao.updateCheckGoods(criteria);
			if (checkGoods.getFirstNondefectivePl() < 0) {
				// 减少总库存和可销售库存
				StockRequest stockRequest = new StockRequest(stock
						.getWarehouse().getId(), skuCode,
						StockType.STOCK_SALES, Math.abs(checkGoods
								.getFirstNondefectivePl()),
						StockBizType.CHECK_OUT, check.getCheckCode());
				stockService.decreaseStock(stockRequest);
			}
			if (checkGoods.getFirstDefectivePl() < 0) {
				// 减少总库存和不可销售库存
				StockRequest stockRequest = new StockRequest(stock
						.getWarehouse().getId(), skuCode,
						StockType.STOCK_UNSALES, Math.abs(checkGoods
								.getFirstDefectivePl()),
						StockBizType.CHECK_OUT, check.getCheckCode());
				stockService.decreaseStock(stockRequest);
			}
		}
	}

	@Override
	public List<CheckStatusException> checkPhysicalIndivList(
			Map<String, List<CheckItem>> physicalIndivMap, Long checkId) {
		// TODO Auto-generated method stub
		checkDao.deleteCheckStatusExceptions(checkId);// 在做异常检查之前先把历史盘点异常信息清理
		List<CheckStatusException> checkStatusExceptionsList = new ArrayList<CheckStatusException>();
		Check check = checkDao.queryCheck(checkId);
		Map<String, Object> criteria = new HashMap<String, Object>(10);
		List<String> indivCodeList = new ArrayList<String>();
		for (Map.Entry<String, List<CheckItem>> entry : physicalIndivMap
				.entrySet()) {
			List<CheckItem> checkItemsList = entry.getValue();
			for (CheckItem item : checkItemsList) {
				indivCodeList.add(item.getIndivCode());
			}
		}
		List<Indiv> indivList = indivDao.queryIndivListByCodes(indivCodeList);
		for (Map.Entry<String, List<CheckItem>> itemEntry : physicalIndivMap
				.entrySet()) {// 检查异常数据
			List<CheckItem> checkItemsList = itemEntry.getValue();
			for (CheckItem item : checkItemsList) {
				Integer itemWaresStatus = item.getWaresStatus();
				String itemindivCode = item.getIndivCode();
				String itemSku = item.getSkuCode();
				for (Indiv indiv : indivList) {
					Integer indivWaresStatus = indiv.getWaresStatus();
					String indivindivCode = indiv.getIndivCode();
					Integer stockStatus = indiv.getStockStatus();
					String indivSku = indiv.getSkuCode();
					if (itemindivCode.equals(indivindivCode)) {
						if (!itemWaresStatus.equals(indivWaresStatus)) {// 良次品状态不一致
							CheckStatusException statusException = new CheckStatusException();
							statusException.setIndivCode(indivindivCode);
							statusException.setCheckId(checkId);
							if (indivWaresStatus == WmsConstants.IndivWaresStatus.DEFECTIVE
									.getCode()) {
								statusException
										.setBookStatus(WmsConstants.CheckStatusExceptionType.CHECK_DEFECTIVE
												.getCode());
								statusException
										.setCheckStatus(WmsConstants.CheckStatusExceptionType.CHECK_NONDEFECTIVE
												.getCode());
							} else {
								statusException
										.setCheckStatus(WmsConstants.CheckStatusExceptionType.CHECK_DEFECTIVE
												.getCode());
								statusException
										.setBookStatus(WmsConstants.CheckStatusExceptionType.CHECK_NONDEFECTIVE
												.getCode());
							}
							checkStatusExceptionsList.add(statusException);
						}
						if (stockStatus == WmsConstants.IndivStockStatus.OUT_WAREHOUSE
								.getCode()) {// 已出库
							CheckStatusException statusException = new CheckStatusException();
							statusException.setIndivCode(indivindivCode);
							statusException.setCheckId(checkId);
							statusException
									.setBookStatus(WmsConstants.CheckStatusExceptionType.CHECK_OUTHOUSE
											.getCode());
							statusException
									.setCheckStatus(WmsConstants.CheckStatusExceptionType.CHECK_INHOUSE
											.getCode());
							checkStatusExceptionsList.add(statusException);
						}
						if (!itemSku.equals(indivSku)) {// imei编码存在 但是sku不正确
							CheckStatusException statusException = new CheckStatusException();
							statusException.setIndivCode(indivindivCode);
							statusException.setCheckId(checkId);
							statusException
									.setBookStatus(WmsConstants.CheckStatusExceptionType.CHECK_NONSKU
											.getCode());
							statusException
									.setCheckStatus(WmsConstants.CheckStatusExceptionType.CHECK_NONSKU
											.getCode());
							checkStatusExceptionsList.add(statusException);
						}
					}
				}
			}
		}
		return checkStatusExceptionsList;
	}

	@Override
	public void addCheckStatusExceptionsList(
			List<CheckStatusException> checkStatusExceptionList) {
		// TODO Auto-generated method stub
		checkDao.addCheckStatusExceptionsList(checkStatusExceptionList);
	}

	@Override
	public List<CheckStatusException> getCheckStatusExceptionList(Long checkId) {
		// TODO Auto-generated method stub
		return checkDao.getCheckStatusExceptionList(checkId);
	}

	@Override
	public void auditCheck(Long id) {
		// TODO Auto-generated method stub
		Check check = checkDao.queryCheck(id);
		// check.setAuditStatus(check.getAuditStatus() + 1);// 每审核一次 状态加1
		check.setAuditStatus(999);// 暂时直接使用快速审核
		checkDao.updateCheck(check);
	}

	@Override
	public Integer getStockHistoryListTotal(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return checkDao.queryStockHistoryTotal(criteria);
	}

	@Override
	public List<Stock> getStockHistoryList(Map<String, Object> criteria,
			Page page) {
		// TODO Auto-generated method stub
		return checkDao.queryStockHistoryList(criteria);
	}
}
