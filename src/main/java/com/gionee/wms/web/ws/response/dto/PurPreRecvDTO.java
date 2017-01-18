package com.gionee.wms.web.ws.response.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.gionee.wms.common.WmsConstants;
import com.google.common.collect.Lists;

/**
 * WS传输采购预收信息的DTO.
 * 
 * @author kevin
 */
@XmlRootElement
@XmlType(name = "PurPreRecv", namespace = WmsConstants.TARGET_NS)
public class PurPreRecvDTO {
	/**
	 * 过账凭证号
	 */
	private String postingNo;
	/**
	 * 采购编号
	 */
	private String purchaseCode;
	/**
	 * 仓库编号
	 */
	private String warehouseCode;
	/**
	 * 制单人
	 */
	private String preparedBy;
	/**
	 * 制单时间
	 */
	private Date preparedTime;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 采购预收商品清单
	 */
	private List<PurPreRecvGoodsDTO> goodsList = Lists.newArrayList();

	public String getPostingNo() {
		return postingNo;
	}

	public void setPostingNo(String postingNo) {
		this.postingNo = postingNo;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElementWrapper(name = "goodsList")
	@XmlElement(name = "goods")
	public List<PurPreRecvGoodsDTO> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<PurPreRecvGoodsDTO> goodsList) {
		this.goodsList = goodsList;
	}

	/**
	 * 直接打印DTO信息.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
