package com.gionee.wms.entity;

public class Stock {
	private Long id;
	private Integer totalQuantity;// 总库存
	private Integer salesQuantity;// 可售库存
	private Integer unsalesQuantity;// 不可售库存（如次品等情况）
	private Integer occupyQuantity;// 占用库存
	private Integer limitUpper;// 库存上限
	private Integer limitLower;// 库存下限
	private Integer ver; // 版本号
	private Sku sku; // sku
	private Warehouse warehouse;// 仓库
	private Long checkId;//盘点单ID
	private String checkCode;//盘点编号
	private Integer nonDefectivePl;// 盘点良品盈亏
	private Integer defectivePl;// 盘点次品盈亏
	private Integer syncStatus;//库存同步状态

	public Stock() {

	}

	public Stock(Long warehouseId, Long skuId) {
		this.warehouse = new Warehouse();
		warehouse.setId(warehouseId);
		this.sku = new Sku();
		sku.setId(skuId);
		totalQuantity = 0;
		salesQuantity = 0;
		unsalesQuantity = 0;
		occupyQuantity = 0;
		limitUpper = -1;
		limitLower = -1;
		ver = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Integer getSalesQuantity() {
		return salesQuantity;
	}

	public void setSalesQuantity(Integer salesQuantity) {
		this.salesQuantity = salesQuantity;
	}

	public Integer getUnsalesQuantity() {
		return unsalesQuantity;
	}

	public void setUnsalesQuantity(Integer unsalesQuantity) {
		this.unsalesQuantity = unsalesQuantity;
	}

	public Integer getOccupyQuantity() {
		return occupyQuantity;
	}

	public void setOccupyQuantity(Integer occupyQuantity) {
		this.occupyQuantity = occupyQuantity;
	}

	public Integer getLimitUpper() {
		return limitUpper;
	}

	public void setLimitUpper(Integer limitUpper) {
		this.limitUpper = limitUpper;
	}

	public Integer getLimitLower() {
		return limitLower;
	}

	public void setLimitLower(Integer limitLower) {
		this.limitLower = limitLower;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getVer() {
		return ver;
	}

	public void setVer(Integer ver) {
		this.ver = ver;
	}

	
	public Long getCheckId() {
		return checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

	public Integer getNonDefectivePl() {
		return nonDefectivePl;
	}

	public void setNonDefectivePl(Integer nonDefectivePl) {
		this.nonDefectivePl = nonDefectivePl;
	}

	public Integer getDefectivePl() {
		return defectivePl;
	}

	public void setDefectivePl(Integer defectivePl) {
		this.defectivePl = defectivePl;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

}
