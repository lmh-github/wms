package com.gionee.wms.web.action.stock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.excel.excelimport.bean.ExcelData;
import com.gionee.wms.common.excel.excelimport.bean.ImportCellDesc;
import com.gionee.wms.common.excel.excelimport.userinterface.ExcelImportUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Category;
import com.gionee.wms.entity.Check;
import com.gionee.wms.entity.CheckGoods;
import com.gionee.wms.entity.CheckItem;
import com.gionee.wms.entity.CheckStatusException;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.entity.StockHistory;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.CheckService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;

@Controller("StockCheckAction")
@Scope("prototype")
public class StockCheckAction extends AjaxActionSupport implements Preparable {
	private static final long serialVersionUID = 2566942049805660757L;
	@Autowired
	private CheckService checkService;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private StockService stockService;
	@Autowired
	private CategoryService categoryService;

	/** 页面相关属性 **/
	private Long id;
	private String checkCode;
	private String catPath;
	private String skuCode;
	private String skuName;
	private Check check;
	private CheckGoods checkGoods;
	private List<Check> checkList;
	private List<CheckGoods> goodsList;
	private List<Warehouse> warehouseList;
	private List<Category> categoryList;// 商品分类
	private List<Stock> stockList;// 库存列表
	private List<Long> skuIds;
	private List<CheckStatusException> checkStatusExceptionList = new ArrayList<CheckStatusException>(
			50);
	private Page page = new Page();
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	private String type;
	List<CheckItem> checkItemList = null;// 记录盘比较数据
	private String confirmType;// 盘点类型
	private String remark;// 出入库备注

	private List<StockHistory> stockHistoryList = new ArrayList<StockHistory>(
			50); // 库存历史记录列表
	private Long warehouseId;
	private Long checkId;


	/**
	 * 进入盘点单列表
	 */
	public String execute() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("checkCode", StringUtils.defaultIfBlank(checkCode, null));
		int stockCheckListTotal = checkService.getCheckTotal(criteria);
		page.setTotalRow(stockCheckListTotal);
		page.calculate();
		criteria.put("page", page);
		checkList = checkService.getCheckList(criteria, page);
		return SUCCESS;
	}

	/**
	 * 进入盘点商品清单
	 */
	public String listGoods() throws Exception {
		Validate.notNull(id);
		check = checkService.getCheck(id);
//		if(StringUtils.isNotEmpty(check.getCheckType()) && check.getCheckType().equals("2")){
		// //查询个体盘点比较结果信息
//			Map<String, Object> criteria = Maps.newHashMap();
//			criteria.put("checkId", id);
//			checkItemList = checkService.getCheckItemList(criteria);
//		}else{
//			goodsList = checkService.getCheckGoodsList(id);
//		}
		goodsList = checkService.getCheckGoodsList(id);
		checkStatusExceptionList = checkService.getCheckStatusExceptionList(id);
		return "list_goods";
	}
	
	/**
	 * 进入个体盘点明细
	 */
	public String listItem() throws Exception {
		Validate.notNull(id);
		check = checkService.getCheck(id);
		// 查询盘点商品
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("checkId", id);
		criteria.put("skuCode", skuCode);
		checkGoods = checkService.getCheckGoods(criteria);
		// 查询个体盘点比较结果信息
		criteria.clear();
		criteria.put("checkId", id);
		criteria.put("skuCode", skuCode);
		checkItemList = checkService.getCheckItemList(criteria);
		return "list_item";
	}

	/**
	 * 进入创建盘点单界面
	 */
	public String input() throws Exception {
		warehouseList = warehouseService.getValidWarehouses();
		return INPUT;
	}

	/**
	 * 进入批量添加盘点商品界面
	 */
	public String inputGoods() throws Exception {
		Validate.notNull(id);
		check = checkService.getCheck(id);

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", check.getWarehouseId());
		criteria.put("catPath", StringUtils.defaultIfBlank(catPath, null));
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		int totalRow = stockService.getStockListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		stockList = stockService.getStockList(criteria, page);
		categoryList = categoryService.getCategoryList(null);
		return "input_goods";
	}

	/**
	 * 进入实盘结果上传界面
	 */
	public String inputUploadGoodsList() {
		return "input_upload";
	}
	
	/**
	 * 进入个体盘点上传界面
	 */
	public String inputUploadIndivGoodsList() {
		return "input_upload_indiv";
	}

	/**
	 * 进入盘点单确认界面
	 */
	public String inputConfirm() throws Exception {
		Validate.notNull(id);
		check = checkService.getCheck(id);
		return "input_confirm";
	}
	
	/**
	 * 进入盘点入库或出库确认界面
	 */
	public String inputConfirmItem() throws Exception {
		Validate.notNull(id);
		check = checkService.getCheck(id);
		return "input_confirm_item";
	}

	/**
	 * 创建盘点单
	 */
	public String add() throws Exception {
		Validate.notNull(check.getWarehouseId());
		try {
			Warehouse warehouse = warehouseService.getWarehouse(check.getWarehouseId());
			Validate.notNull(warehouse);
			check.setWarehouseName(warehouse.getWarehouseName());
			check.setAuditStatus(0);
			checkService.addCheck(check);
			ajaxSuccess("添加盘点单成功");
		} catch (ServiceException e) {
			logger.error("添加盘点单时出错", e);
			ajaxError("添加盘点单失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 批量添加盘点商品
	 */
	public String addGoods() throws Exception {
		Validate.notNull(id);
		Validate.notNull(skuIds);
		try {
			check = checkService.getCheck(id);
			checkService.addCheckGoodsList(check, skuIds);
			ajaxSuccess("SKU成功添加到盘点明细");
		} catch (ServiceException e) {
			logger.error("SKU添加到盘点明细时出错", e);
			ajaxError("SKU添加到盘点明细失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除盘点单
	 */
	public String delete() throws Exception {
		try {
			checkService.deleteCheck(id);
			ajaxSuccess("删除盘点单成功");
		} catch (ServiceException e) {
			logger.error("删除盘点单时出错", e);
			ajaxError("删除盘点单失败：" + e.getMessage());
		}
		return null;
	}


	/**
	 * 审核盘点单
	 */
	public String audit() throws Exception {
		try {
			checkService.auditCheck(id);
			ajaxSuccess("审核盘点单通过");
		} catch (ServiceException e) {
			logger.error("审核盘点单时出错", e);
			ajaxError("审核盘点单失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除盘点商品
	 */
	public String deleteGoods() throws Exception {
		Validate.notNull(id);
		checkGoods = checkService.getCheckGoods(id);
		try {
			checkService.deleteCheckGoods(checkGoods);
			ajaxSuccess("删除盘点项成功");
		} catch (ServiceException e) {
			logger.error("删除盘点项时出错", e);
			ajaxError("删除盘点项失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 确认盘点单
	 */
	public String confirm() throws Exception {
		Validate.notNull(check);
		if(StringUtils.isEmpty(remark)){
			ajaxError("请填写备注信息");
			return null;
		}
		try {
			if(StringUtils.isNotEmpty(check.getCheckType()) && check.getCheckType().equals("2")){
				checkService.confirmCheckItem(check,confirmType,skuCode,remark);
			} else if(StringUtils.isNotEmpty(check.getCheckType()) && check.getCheckType().equals("1")) {
				checkService.confirmCheckItemPart(check, confirmType, skuCode, remark);
			} else {
				ajaxError("请确认盘点单类型");
				return null;
			}
			ajaxSuccess("确认盘点单成功");
		} catch (ServiceException e) {
			logger.error("确认盘点单时出错", e);
			ajaxError("确认盘点单失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 确认盘点单
	 */
	public String confirmCheck() throws Exception {
		Validate.notNull(check);
		if(StringUtils.isEmpty(check.getRemark())){
			ajaxError("请填写备注信息");
			return null;
		}
		try {
			checkService.confirmCheck(check);
			ajaxSuccess("确认盘点单成功");
		} catch (ServiceException e) {
			logger.error("确认盘点单时出错", e);
			ajaxError("确认盘点单失败：" + e.getMessage());
		}
		return null;
	}

	public void prepareConfirm() throws Exception {
		Validate.notNull(id);
		check = checkService.getCheck(id);
	}
	
	/**
	 * 下载盘点商品清单
	 */
	public String downloadGoodsList() {
		Validate.notNull(id);
		checkService.downloadCheckGoodsList(id);
		return null;
	}

	/**
	 * 上传实盘结果
	 */
	public String uploadGoodsList() throws Exception {
		Validate.notNull(id);
		// InputStream importExcelStream =
		// getClass().getClassLoader().getResourceAsStream(
		// WmsConstants.CHECK_IMP_DATA_FILE);
		String errorMsg = fileValidate(upload);
		if (StringUtils.isNotBlank(errorMsg)) {
			logger.error(errorMsg);
			ajaxError(errorMsg);
			return null;
		}
		try {
			Map<String, CheckGoods> physicalGoodsList = getPhysicalGoodsList(upload);
			checkService.addPhysicalGoodsList(checkService.getCheck(id), physicalGoodsList);
			ajaxSuccess("上传实盘数据成功");
		} catch (Exception e) {
			logger.error("上传实盘数据时出错", e);
			ajaxError("上传实盘数据失败：" + e.getMessage());
		}

		return null;
	}
	
	/**
	 * 上传个体盘点结果
	 */
	public String uploadIndivList() throws Exception {
		Validate.notNull(id);
		String errorMsg = fileValidate(upload);
		if (StringUtils.isNotBlank(errorMsg)) {
			logger.error(errorMsg);
			ajaxError(errorMsg);
			return null;
		}
		try {
			Map<String, List<CheckItem>> physicalIndivMap = getPhysicalIndivList(upload, id);
			// 比较并入库盘点结果
			checkStatusExceptionList = new ArrayList<CheckStatusException>(
					50);
			checkStatusExceptionList = checkService.checkPhysicalIndivList(
					physicalIndivMap, id);
			String result = "";
			if (CollectionUtils.isEmpty(checkStatusExceptionList)) {
				result = checkService
						.addPhysicalIndivList(physicalIndivMap, id);
			} else {
				checkService
						.addCheckStatusExceptionsList(checkStatusExceptionList);
			}

			if(result.length()>0){
				ajaxError("上传实盘数据失败：" + result);
			}else{
				ajaxSuccess("上传实盘数据成功");
			}
		} catch (Exception e) {
			logger.error("上传实盘数据时出错", e);
			ajaxError("上传实盘数据失败：" + e.getMessage());
		}
		return null;
	}

	private Map<String, CheckGoods> getPhysicalGoodsList(File upload) throws Exception {
		Map<String, CheckGoods> physicalGoodsList = Maps.newHashMap();
		ExcelData uploadData = null;
		uploadData = ExcelImportUtil.readExcel(WmsConstants.CHECK_IMP_DESC_FILE, FileUtils.openInputStream(upload));

		if (uploadData == null || CollectionUtils.isEmpty(uploadData.getRepeatData())) {
			throw new RuntimeException("文件数据格式不正确");
		}

		List<Map<String, ImportCellDesc>> physicalExcelRows = uploadData.getRepeatData();
		for (Map<String, ImportCellDesc> row : physicalExcelRows) {
			CheckGoods physicalGoods = new CheckGoods();
			physicalGoods.setSkuCode(row.get("SKU_CODE").getFieldValue());
			physicalGoods.setMeasureUnit(row.get("MEASURE_UNIT").getFieldValue());
			try {
				physicalGoods.setFirstNondefective(Integer.parseInt(StringUtils.substringBefore(row.get("NON_DEFECTIVED")
						.getFieldValue(), ".")));
				physicalGoods.setFirstDefective(Integer.parseInt(StringUtils.substringBefore(row.get("DEFECTIVED")
						.getFieldValue(), ".")));
			} catch (Exception e) {
				throw new RuntimeException("请正确填写表格盘点数据");
			}
			if (physicalGoodsList.containsKey(physicalGoods.getSkuCode())) {
				throw new RuntimeException("商品条目存在重复");
			}
			physicalGoodsList.put(physicalGoods.getSkuCode(), physicalGoods);
		}
		return physicalGoodsList;
	}
	
	private Map<String, List<CheckItem>> getPhysicalIndivList(File upload, Long checkId) throws Exception {
		Map<String, List<CheckItem>> physicalIndivMap = Maps.newHashMap();
		List<CheckItem> itemList = null;
		CheckItem item = null;
		ExcelData uploadData = null;
		uploadData = ExcelImportUtil.readExcel(WmsConstants.CHECK_IMP_DESC_INDIV_FILE, FileUtils.openInputStream(upload));

		if (uploadData == null || CollectionUtils.isEmpty(uploadData.getRepeatData())) {
			throw new RuntimeException("文件数据格式不正确");
		}

//		Map<String, ImportCellDesc> title = uploadData.getOnceData();
		// 记录当前skuCode
		String currSkuCode = "";
		List<Map<String, ImportCellDesc>> physicalExcelRows = uploadData.getRepeatData();
		for (Map<String, ImportCellDesc> row : physicalExcelRows) {
			item = new CheckItem();
			String skuCode = row.get("SKU_CODE").getFieldValue();
			String indivCode = row.get("IMEI").getFieldValue();
			String status = row.get("STATUS").getFieldValue();
			// if(StringUtils.isEmpty(indivCode) && StringUtils.isEmpty(skuCode)
			// && StringUtils.isEmpty(num)){
			// continue;
			// }
			item.setCheckId(checkId);
			// if(StringUtils.isEmpty(indivCode) &&
			// StringUtils.isNotEmpty(num)){
			// //为配件
			// item.setIndivEnabled(0);
			// item.setNum(Integer.valueOf(num));
			// }else{
				item.setIndivCode(indivCode);
				item.setIndivEnabled(1);
			// }
			item.setWaresStatus(Integer.valueOf(status));// 如果excel为空值不做特殊处理，后期对比作为异常处理
			if(StringUtils.isEmpty(skuCode)){
				item.setSkuCode(currSkuCode);
				physicalIndivMap.get(currSkuCode).add(item);
			}else{
				if(physicalIndivMap.containsKey(skuCode)){
					item.setSkuCode(skuCode);
					physicalIndivMap.get(skuCode).add(item);
				}else{
					currSkuCode = skuCode;
					itemList = Lists.newArrayList();
					item.setSkuCode(skuCode);
					itemList.add(item);
					physicalIndivMap.put(skuCode, itemList);
				}
			}
		}
		return physicalIndivMap;
	}

	private String fileValidate(File upload) {
		String errorMsg = "";
		if (upload == null) {
			errorMsg = "上传文件为空";
		} else if (StringUtils.isBlank(uploadContentType)
				|| !WmsConstants.EXCEL_UPLOAD_ALLOWED_TYPES.contains(uploadContentType)) {
			errorMsg = "上传文件类型不是Excel";
		} else if (upload.length() > WmsConstants.EXCEL_UPLOAD_MAXIMUM_SIZE) {
			errorMsg = "上传文件不能大于2M";
		}
		logger.info("upload file name: " + uploadFileName);
		logger.info("upload file type: " + uploadContentType);
		logger.info("upload file size: " + upload.length());
		return errorMsg;
	}

	public String stockHistoryList() {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", warehouseId);
		criteria.put("catPath", StringUtils.defaultIfBlank(catPath, null));
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		criteria.put("checkId", checkId);
		int totalRow = checkService.getStockHistoryListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		stockList = checkService.getStockHistoryList(criteria, page);
		warehouseList = warehouseService.getValidWarehouses();
		categoryList = categoryService.getCategoryList(null);
		return "stockHistoryList";
	}

	@Override
	public void prepare() throws Exception {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Check getCheck() {
		return check;
	}

	public void setCheck(Check check) {
		this.check = check;
	}

	public List<Check> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<Check> checkList) {
		this.checkList = checkList;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<Warehouse> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getCatPath() {
		return catPath;
	}

	public void setCatPath(String catPath) {
		this.catPath = catPath;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public List<CheckGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<CheckGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<Stock> getStockList() {
		return stockList;
	}

	public void setStockList(List<Stock> stockList) {
		this.stockList = stockList;
	}

	public List<Long> getSkuIds() {
		return skuIds;
	}

	public void setSkuIds(List<Long> skuIds) {
		this.skuIds = skuIds;
	}

	public List<CheckStatusException> getCheckStatusExceptionList() {
		return checkStatusExceptionList;
	}

	public void setCheckStatusExceptionList(
			List<CheckStatusException> checkStatusExceptionList) {
		this.checkStatusExceptionList = checkStatusExceptionList;
	}

	public CheckGoods getCheckGoods() {
		return checkGoods;
	}

	public void setCheckGoods(CheckGoods checkGoods) {
		this.checkGoods = checkGoods;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<CheckItem> getCheckItemList() {
		return checkItemList;
	}

	public void setCheckItemList(List<CheckItem> checkItemList) {
		this.checkItemList = checkItemList;
	}

	public String getConfirmType() {
		return confirmType;
	}

	public void setConfirmType(String confirmType) {
		this.confirmType = confirmType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getCheckId() {
		return checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

}
