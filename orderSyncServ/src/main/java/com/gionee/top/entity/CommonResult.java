package com.gionee.top.entity;

import java.io.Serializable;

/**
 * 
 * 描述: 
 * 作者: milton.zhang
 * 日期: 2013-11-19
 */
public class CommonResult implements Serializable {
	private static final long serialVersionUID = 1459078718011652457L;
	
	private Integer ret;
	private Integer err;
	private String msg;

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public Integer getErr() {
		return err;
	}

	public void setErr(Integer err) {
		this.err = err;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void setResult(Integer ret, Integer err, String msg) {
		this.ret = ret;
		this.err = err;
		this.msg = msg;
	}
	
	public enum ErrCodeEnum {
		//0x0-0xFFFF，0x1-0xFF为系统保留
		ERR_SUCCESS(0x0, ""),
		ERR_SYSTEM_ERROR(0x3E8, "Unknown runtime exception"),//1000
		ERR_CONTENT_TYPE_ILLEGAL(0x3EA,"Content type of request is illegal"),//1002
		ERR_PARAM_ERROR(0x3EB, "Parameters is illegal"),//1003
		ERR_PARAM_WAREHOUSE_CODE_NULL(0x3EC,"'warehouseCode' is null"),//100
		ERR_PARAM_SKU_CODE_NULL(0x3ED,"'skuCode' is null"),//1005
		ERR_PARAM_ORDERS_NULL(0x3EE,"'orders' is null"),//1006
		ERR_PARAM_ORDER_INFO_NULL(0x3EF,"'orderInfo' is null"),//1007
		ERR_PARAM_ORDER_CODE_NULL(0x3F0,"'orderCode' is null"),//1008
		ERR_PARAM_CONSIGNEE_NULL(0x3F1,"'consignee' is null"),//1009
		ERR_PARAM_PROVINCE_NULL(0x3F2,"'province' is null"),//1010
		ERR_PARAM_CITY_NULL(0x3F3,"'city' is null"),//1011
		ERR_PARAM_ADDRESS_NULL(0x3F4,"'address' is null"),//1012
		ERR_PARAM_PHONE_NULL(0x3F5,"'tel' and 'mobile' is null"),//1013
		ERR_PARAM_INVOICE_ENABLED_NULL(0x3F6,"'invoiceEnabled' is null"),//1014
		ERR_PARAM_GOODS_AMOUNT_NULL(0x3F7,"'goodsAmount' is null"),//1015
		ERR_PARAM_ORDER_AMOUNT_NULL(0x3F8,"'orderAmount' is null"),//1016
		ERR_PARAM_PAYABLE_AMOUNT_NULL(0x3F9,"'payableAmount' is null"),//1017
		ERR_PARAM_GOODS_LIST_NULL(0x3FA,"'goodsList' is null"),//1018
		ERR_PARAM_TIMESTAMP_ILLEGAL(0x3FB, "timestamp is illegal"),//1019
		ERR_STOCK_NOT_EXISTS(0x3FC,"Stock info is not exists"),//1020
		ERR_SKU_NOT_EXISTS(0x3FD,"Sku is not exists"),//1021
		ERR_DUPLICATE_ORDER(0x3FE,"Duplicate order"),//1022
		ERR_STOCK_OCCUPY_FAILED(0x3FF,"Stock occupation fail"),//1023
		ERR_ORDER_NOT_EXIST(0x400,"Order is not exists"),//1024
		ERR_ORDER_SHIPPED(0x401,"Order is shipped"),//1025
		ERR_DUPLICATE_CANCEL_ORDER(0x402,"Duplicate cancel order"),//1026
		ERR_NOT_ALLOWED_CANCEL(0x403,"Not allowed to cancel"),//1027
		ERR_SIGNATURE_CHECKSUM_FAILURE(0x404, "Signature verification failed"),//1028
		ERR_ACCESS_EXPIRED(0x405, "Accessing is expired "),//1029
		ERR_STOCK_RELEASE_FAILED(0x406,"Stock release fail"),//1030
		ERR_SHIPPING_CODE_NOT_EXISTS(0x407,"Shipping code is not exists"),//1031
		ERR_PARAM_OPERFLAG_NULL(0x408,"'operFlag' is null"),//1032
		ERR_CURRENT_STATUS_FORBIDDEN_UPDATE(0x409,"Current status forbidden operate"),//1033
		ERR_DUPLICATE_BACK(0x40A,"Duplicate back"),//1034
		ERR_BACK_NOT_EXISTS(0x40B,"Back is not exists"),//1035
		ERR_ORDER_IS_BACKING(0x40C,"This order is backing");//1036

		private Integer err;
		private String msg;

		ErrCodeEnum(Integer err, String msg) {
			this.err = err;
			this.msg = msg;
		}

		public Integer getErr() {
			return err;
		}

		public void setErr(Integer err) {
			this.err = err;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	
	public enum RetCodeEnum {
		RET_SUCCESS(0);

		private Integer ret;

		RetCodeEnum(Integer ret) {
			this.ret = ret;
		}

		public Integer getRet() {
			return ret;
		}

		public void setRet(Integer ret) {
			this.ret = ret;
		}
		
	}
}
