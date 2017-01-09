package com.gionee.wms.entity;

/*
 * 用来存储临时每日库存信息
 */
public class DailyStockTemp {
	private Long id;// 主键
	private Long stockId;// 库存id
	private Integer type;// 数据类型 1:期初结余;2:本期出库;3:占用未出库;4:期末结余
	private Integer quantity;// 数量

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
