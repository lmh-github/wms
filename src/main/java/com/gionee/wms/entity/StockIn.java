package com.gionee.wms.entity;

import java.util.Date;

import com.gionee.wms.common.WmsConstants;

public class StockIn {
	private Long id;
	private String stockInCode;//入库单编号
	private String stockInType;// 入库类型
	private String opposite;// 对方
	private String originalCode;// 源单号
	private String handledBy;// 经手人
	private Date handledDate;// 入库日期
	private String preparedBy;// 制单人
	private Date preparedTime;// 制单时间
	private String handlingStatus;// 处理状态
	private Date finishedTime;//完结时间
	private Integer enabled; // 是否可用
	private String remark;// 备注
	private Warehouse warehouse;// 入库仓库
//	private List<StockInItem> stockInDetail = Lists.newArrayList(); // 入库物品明细

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStockInCode() {
		return stockInCode;
	}

	public void setStockInCode(String stockInCode) {
		this.stockInCode = stockInCode;
	}


	public String getStockInType() {
		return stockInType;
	}

	public void setStockInType(String stockInType) {
		this.stockInType = stockInType;
	}

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}

	public String getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(String handledBy) {
		this.handledBy = handledBy;
	}

	public Date getHandledDate() {
		return handledDate;
	}

	public void setHandledDate(Date handledDate) {
		this.handledDate = handledDate;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public Date getPreparedTime() {
		return preparedTime;
	}

	public void setPreparedTime(Date preparedTime) {
		this.preparedTime = preparedTime;
	}

	public String getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(String handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public Boolean isEnabled() {
		if (enabled != null) {
			return (WmsConstants.ENABLED_TRUE == enabled ? true : false);
		}
		return null;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled ? WmsConstants.ENABLED_TRUE : WmsConstants.ENABLED_FALSE;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

//	public List<StockInItem> getStockInDetail() {
//		return stockInDetail;
//	}
//
//	public void setStockInDetail(List<StockInItem> stockInDetail) {
//		this.stockInDetail = stockInDetail;
//	}

	public String getOpposite() {
		return opposite;
	}

	public void setOpposite(String opposite) {
		this.opposite = opposite;
	}

	public Date getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

}
