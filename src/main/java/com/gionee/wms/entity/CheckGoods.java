package com.gionee.wms.entity;

public class CheckGoods {
	private Long id;
	private Long checkId;
	private Long skuId;
	private String skuCode;
	private String skuName;
	private String measureUnit;// 计量单位
	private Integer bookNondefective;// 账面盘良品数
	private Integer bookDefective;// 账面盘次品数
	private Integer firstNondefective;// 预盘良品数
	private Integer firstNondefectivePl;// 预盘良品盈亏
	private Integer firstDefective;// 预盘次品数
	private Integer firstDefectivePl;// 预盘次品盈亏
	private Integer secondNondefective;// 复盘良品数
	private Integer sendNondefectivePl;// 复盘良品盈亏
	private Integer secondDefective;// 复盘次品数
	private Integer sendDefectivePl;// 复盘次品盈亏
	private String checkResult;// 盘点项结果 1:正常 2:盘盈 3:盘亏
	private String remark;// 备注
	private String remarkIn;// 盘点入库备注
	private String remarkOut;// 盘点出库备注
	private Check check;// 盘点单
	// private Sku sku;// SKU对象
	private Stock stock;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public Check getCheck() {
		return check;
	}

	public void setCheck(Check check) {
		this.check = check;
	}

	public Long getCheckId() {
		return checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
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

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Integer getBookNondefective() {
		return bookNondefective;
	}

	public void setBookNondefective(Integer bookNondefective) {
		this.bookNondefective = bookNondefective;
	}

	public Integer getBookDefective() {
		return bookDefective;
	}

	public void setBookDefective(Integer bookDefective) {
		this.bookDefective = bookDefective;
	}

	public Integer getFirstNondefective() {
		return firstNondefective;
	}

	public void setFirstNondefective(Integer firstNondefective) {
		this.firstNondefective = firstNondefective;
	}

	public Integer getFirstNondefectivePl() {
		return firstNondefectivePl;
	}

	public void setFirstNondefectivePl(Integer firstNondefectivePl) {
		this.firstNondefectivePl = firstNondefectivePl;
	}

	public Integer getFirstDefective() {
		return firstDefective;
	}

	public void setFirstDefective(Integer firstDefective) {
		this.firstDefective = firstDefective;
	}

	public Integer getFirstDefectivePl() {
		return firstDefectivePl;
	}

	public void setFirstDefectivePl(Integer firstDefectivePl) {
		this.firstDefectivePl = firstDefectivePl;
	}

	public Integer getSecondNondefective() {
		return secondNondefective;
	}

	public void setSecondNondefective(Integer secondNondefective) {
		this.secondNondefective = secondNondefective;
	}

	public Integer getSendNondefectivePl() {
		return sendNondefectivePl;
	}

	public void setSendNondefectivePl(Integer sendNondefectivePl) {
		this.sendNondefectivePl = sendNondefectivePl;
	}

	public Integer getSecondDefective() {
		return secondDefective;
	}

	public void setSecondDefective(Integer secondDefective) {
		this.secondDefective = secondDefective;
	}

	public Integer getSendDefectivePl() {
		return sendDefectivePl;
	}

	public void setSendDefectivePl(Integer sendDefectivePl) {
		this.sendDefectivePl = sendDefectivePl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public String getRemarkIn() {
		return remarkIn;
	}

	public void setRemarkIn(String remarkIn) {
		this.remarkIn = remarkIn;
	}

	public String getRemarkOut() {
		return remarkOut;
	}

	public void setRemarkOut(String remarkOut) {
		this.remarkOut = remarkOut;
	}

}
