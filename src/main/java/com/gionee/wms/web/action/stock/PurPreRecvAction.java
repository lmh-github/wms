package com.gionee.wms.web.action.stock;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.DateConvert;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.XMLUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.PurPreRecvGoods;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.basis.SupplierService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.PurchaseService;
import com.gionee.wms.service.stock.ReceiveService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.gionee.wms.web.ws.PurchaseSoapService;
import com.gionee.wms.web.ws.response.WSResult;
import com.gionee.wms.web.ws.response.dto.PurPreRecvDTO;
import com.gionee.wms.web.ws.response.dto.PurPreRecvGoodsDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller("PurPreRecvAction")
@Scope("prototype")
public class PurPreRecvAction extends AjaxActionSupport {
	private static final long serialVersionUID = 8945106312777211809L;

	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private IndivService indivService;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private PurchaseSoapService purchaseSoapService;

	/** 页面相关属性 **/
	private Long id;
	private Long warehouseId;
	private Long supplierId;
	private String postingNo;
	private String purchaseCode;
	private Integer handlingStatus;
	private Date preparedTimeBegin;// 制单起始时间
	private Date preparedTimeEnd;// 制单起始时间
	private List<PurPreRecv> preRecvList;
	private List<PurPreRecvGoods> goodsList;
	private List<IndivFlow> indivList;
	private List<Warehouse> warehouseList;// 仓库列表
	private List<Supplier> supplierList;// 供应商列表
	private Page page = new Page();
	private File upload;
	private String uploadContentType;
	private String uploadFileName;

	/**
	 * 进入采购预收单列表界面
	 */
	public String list() throws Exception {
		// 初始化页面数据
		warehouseList = warehouseService.getValidWarehouses();
		supplierList = supplierService.getValidSuppliers();

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", warehouseId);
		criteria.put("supplierId", supplierId);
		criteria.put("postingNo", StringUtils.defaultIfBlank(postingNo, null));
		criteria.put("purchaseCode", StringUtils.defaultIfBlank(purchaseCode, null));
		criteria.put("handlingStatus", handlingStatus);
		criteria.put("preparedTimeBegin", preparedTimeBegin);
		criteria.put("preparedTimeEnd", preparedTimeEnd != null ? new Date(preparedTimeEnd.getTime() + (24 * 3600 - 1)
				* 1000) : null);
		int totalRow = purchaseService.getPurPreRecvTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		preRecvList = purchaseService.getPurPreRecvList(criteria, page);
		return SUCCESS;
	}

	/**
	 * 进入采购预收商品清单界面
	 */
	public String listGoods() throws Exception {
		Validate.notNull(id);
		goodsList = purchaseService.getPurPreRecvGoodsList(id);
		return INPUT;
	}

	/**
	 * 进入采购预收商品个体界面
	 */
	public String listIndiv() throws Exception {
		Validate.notNull(id);
		indivList = indivService.getIndivFlowsByFlowItemId(IndivFlowType.PUR_PRE_RECV.getCode(), id);
		return "input_indiv";
	}

	/**
	 * 根据采购预收单创建收货单
	 */
	public String createReceive() throws Exception {
		Validate.notNull(id);
		PurPreRecv purPreRecv = purchaseService.getPurPreRecv(id);
		try {
			receiveService.addPurchaseReceiveByPreRecv(purPreRecv);
			ajaxSuccess("创建收货单成功");
		} catch (Exception e) {
			logger.error("创建收货单时出错", e);
			ajaxError("创建收货单失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 进入采购数据上传界面
	 */
	public String inputUploadGoods() {
		return "input_upload";
	}
	
	/**
	 * 上传采购数据
	 */
	public String uploadGoods() throws Exception {
		String errorMsg = fileValidate(upload);
		if (StringUtils.isNotBlank(errorMsg)) {
			logger.error(errorMsg);
			ajaxError(errorMsg);
			return null;
		}
		try {
			Document doc = XMLUtil.parseXml(FileUtils.openInputStream(upload));
			PurPreRecvDTO dto = new PurPreRecvDTO();
			dto.setWarehouseCode(doc.getRootElement().elementText("warehouseCode"));
			dto.setRemark(doc.getRootElement().elementText("remark"));
			dto.setPurchaseCode(doc.getRootElement().elementText("purchaseCode"));
			dto.setPreparedTime(DateConvert.convertS2Date(doc.getRootElement().elementText("preparedTime")));
			dto.setPreparedBy(doc.getRootElement().elementText("preparedBy"));
			dto.setPostingNo(doc.getRootElement().elementText("postingNo"));
			List<PurPreRecvGoodsDTO> goodsList = Lists.newArrayList();
			List<Element> goodsListElements = doc.getRootElement().element("goodsList").elements("goods");
			for(Element goodsItem : goodsListElements){
				PurPreRecvGoodsDTO goodsDto = new PurPreRecvGoodsDTO();
				goodsDto.setMaterialCode(goodsItem.elementText("materialCode"));
				goodsDto.setMaterialName(goodsItem.elementText("materialName"));
				goodsDto.setProductBatchNo(goodsItem.elementText("productBatchNo"));
				goodsDto.setQuantity(Integer.valueOf(goodsItem.elementText("quantity")));
				List<String> indivList = Lists.newArrayList();
				List<Element> indivListElements = goodsItem.element("indivCodeList").elements("indivCode");
				for(Element indivItem : indivListElements){
					indivList.add(indivItem.getTextTrim());
				}
				goodsDto.setIndivCodeList(indivList);
				goodsList.add(goodsDto);
			}
			dto.setGoodsList(goodsList);
			
			WSResult result = purchaseSoapService.putPurPreRecv(dto);
			if(result.getCode().equals("1")){
				ajaxSuccess("上传采购数据成功");
			}else{
				String errMsg = "";
				if(result.getCode().equals(WSResult.WAREHOUSE_CODE_NOT_EXISTS)){
					errMsg = "仓库编码不存在"+result.getMessage();
				}else if(result.getCode().equals(WSResult.WAREHOUSE_IS_DISABLED)){
					errMsg = "仓库不可用"+result.getMessage();
				}else if(result.getCode().equals(WSResult.MATERAIL_CODE_NOT_EXISTS)){
					errMsg = "物料编码不存在"+result.getMessage();
				}else if(result.getCode().equals(WSResult.GOODS_LIST_IS_NULL)){
					errMsg = "商品列表为空"+result.getMessage();
				}else if(result.getCode().equals(WSResult.INDIV_CODES_IS_NULL)){
					errMsg = "个体编码为空"+result.getMessage();
				}else if(result.getCode().equals(WSResult.INDIV_CODES_QUANTITY_MISMATCHING)){
					errMsg = "个体编码与数量不匹配"+result.getMessage();
				}else if(result.getCode().equals(WSResult.PARAMETER_ERROR)){
					errMsg = "参数错误"+result.getMessage();
				}else if(result.getCode().equals(WSResult.SYSTEM_ERROR)){
					errMsg = "系统错误"+result.getMessage();
				}else if(result.getCode().equals(WSResult.DUPLICATE_PUR_RERECEIVE)){
					errMsg = "数据重复"+result.getMessage();
				}
				ajaxError("上传采购数据失败：" + errMsg);
			}
		} catch (Exception e) {
			logger.error("上传采购数据时出错", e);
			ajaxError("上传采购数据失败：" + e.getMessage());
		}

		return null;
	}
	
	private String fileValidate(File upload) {
		String errorMsg = "";
		if (upload == null) {
			errorMsg = "上传文件为空";
		} else if (StringUtils.isBlank(uploadContentType)
				|| !WmsConstants.XML_UPLOAD_ALLOWED_TYPES.contains(uploadContentType)) {
			errorMsg = "上传文件类型不是xml";
		} else if (upload.length() > WmsConstants.EXCEL_UPLOAD_MAXIMUM_SIZE) {
			errorMsg = "上传文件不能大于2M";
		}
		logger.info("upload file name: " + uploadFileName);
		logger.info("upload file type: " + uploadContentType);
		logger.info("upload file size: " + upload.length());
		return errorMsg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public Integer getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(Integer handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public Date getPreparedTimeBegin() {
		return preparedTimeBegin;
	}

	public void setPreparedTimeBegin(Date preparedTimeBegin) {
		this.preparedTimeBegin = preparedTimeBegin;
	}

	public Date getPreparedTimeEnd() {
		return preparedTimeEnd;
	}

	public void setPreparedTimeEnd(Date preparedTimeEnd) {
		this.preparedTimeEnd = preparedTimeEnd;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<Warehouse> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public List<Supplier> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List<Supplier> supplierList) {
		this.supplierList = supplierList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<PurPreRecv> getPreRecvList() {
		return preRecvList;
	}

	public List<PurPreRecvGoods> getGoodsList() {
		return goodsList;
	}

	public String getPostingNo() {
		return postingNo;
	}

	public void setPostingNo(String postingNo) {
		this.postingNo = postingNo;
	}

	public List<IndivFlow> getIndivList() {
		return indivList;
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

}
