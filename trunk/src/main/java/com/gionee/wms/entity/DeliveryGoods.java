package com.gionee.wms.entity;

public class DeliveryGoods {
	private Long id;
	private Long deliveryId; // 发货单ID
	private Long skuId; // SKU ID
	private String skuCode;// SKU 编码
	private String skuName;// SKU名称
	private Integer quantity;// 数量
	private String measureUnit;// 计量单位
	private Integer indivEnabled; // 是否需绑定个体
	private Integer indivFinished;// 是否已绑定个体
	private Integer waresStatus;// 商品状态
	private Integer enabled;// 是否可用
	private Delivery delivery;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getIndivEnabled() {
		return indivEnabled;
	}

	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}

	public Integer getIndivFinished() {
		return indivFinished;
	}

	public void setIndivFinished(Integer indivFinished) {
		this.indivFinished = indivFinished;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Integer getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}
