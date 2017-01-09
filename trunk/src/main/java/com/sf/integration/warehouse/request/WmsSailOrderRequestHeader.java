/**
 * Project Name:wms
 * File Name:WmsSailOrderRequestHeader.java
 * Package Name:com.sf.integration.warehouse.request
 * Date:2014年8月20日下午3:25:48
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author PengBin 00001550br
 * @date 2014年8月20日 下午3:25:48
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "header")
public class WmsSailOrderRequestHeader {
	@XmlElement
	private String company;
	@XmlElement
	private String warehouse;
	@XmlElement
	private String shop_name;
	@XmlElement
	private String erp_order;
	@XmlElement
	private String order_type;
	@XmlElement
	private String order_date;
	@XmlElement
	private String ship_from_name;
	@XmlElement
	private String ship_from_attention_to;
	@XmlElement
	private String ship_from_country;
	@XmlElement
	private String ship_from_province;
	@XmlElement
	private String ship_from_city;
	@XmlElement
	private String ship_from_area;
	@XmlElement
	private String ship_from_address;
	@XmlElement
	private String ship_from_postal_code;
	@XmlElement
	private String ship_from_phone_num;
	@XmlElement
	private String ship_from_tel_num;
	@XmlElement
	private String ship_from_email_address;
	@XmlElement
	private String ship_to_name;
	@XmlElement
	private String ship_to_attention_to;
	@XmlElement
	private String ship_to_country;
	@XmlElement
	private String ship_to_province;
	@XmlElement
	private String ship_to_city;
	@XmlElement
	private String ship_to_area;
	@XmlElement
	private String ship_to_address;
	@XmlElement
	private String ship_to_postal_code;
	@XmlElement
	private String ship_to_phone_num;
	@XmlElement
	private String ship_to_tel_num;
	@XmlElement
	private String ship_to_email_address;
	@XmlElement
	private String carrier;
	@XmlElement
	private String carrier_service;
	@XmlElement
	private String route_numbers;
	@XmlElement
	private String packing_note;
	@XmlElement
	private String complete_delivery;
	@XmlElement
	private String freight;
	@XmlElement
	private String payment_of_charge;
	@XmlElement
	private String payment_district;
	@XmlElement
	private String cod;
	@XmlElement
	private String amount;
	@XmlElement
	private String self_pickup;
	@XmlElement
	private String value_insured;
	@XmlElement
	private String declared_value;
	@XmlElement
	private String return_receipt_service;
	@XmlElement
	private String delivery_date;
	@XmlElement
	private String delivery_requested;
	@XmlElement
	private String invoice;
	@XmlElement
	private String invoice_type;
	@XmlElement
	private String invoice_title;
	@XmlElement
	private String invoice_content;
	@XmlElement
	private String order_note;
	@XmlElement
	private String company_note;
	@XmlElement
	private String priority;
	@XmlElement
	private String order_total_amount;
	@XmlElement
	private String order_discount;
	@XmlElement
	private String balance_amount;
	@XmlElement
	private String coupons_amount;
	@XmlElement
	private String gift_card_amount;
	@XmlElement
	private String other_charge;
	@XmlElement
	private String actual_amount;
	@XmlElement
	private String customer_payment_method;
	@XmlElement
	private String monthly_account;
	@XmlElement
	private String is_appoint_delivery;
	@XmlElement
	private String appoint_delivery_status;
	@XmlElement
	private String appoint_delivery_remark;
	@XmlElement
	private String from_flag;
	@XmlElement
	private String is_easy;
	@XmlElement
	private String user_def1;
	@XmlElement
	private String user_def2;
	@XmlElement
	private String user_def3;
	@XmlElement
	private String user_def4;
	@XmlElement
	private String user_def5;
	@XmlElement
	private String user_def6;
	@XmlElement
	private String user_def7;
	@XmlElement
	private String user_def8;
	@XmlElement
	private String user_def9;
	@XmlElement
	private String user_def10;
	@XmlElement
	private String user_def11;
	@XmlElement
	private String user_def12;
	@XmlElement
	private String user_def13;
	@XmlElement
	private String user_def14;
	@XmlElement
	private String user_def15;
	@XmlElement
	private String user_def16;
	@XmlElement
	private String user_def17;
	@XmlElement
	private String user_def18;
	@XmlElement
	private String user_def19;
	@XmlElement
	private String user_def20;

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the warehouse
	 */
	public String getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse the warehouse
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	/**
	 * @return the shop_name
	 */
	public String getShop_name() {
		return shop_name;
	}

	/**
	 * @param shop_name the shop_name
	 */
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	/**
	 * @return the erp_order
	 */
	public String getErp_order() {
		return erp_order;
	}

	/**
	 * @param erp_order the erp_order
	 */
	public void setErp_order(String erp_order) {
		this.erp_order = erp_order;
	}

	/**
	 * @return the order_type
	 */
	public String getOrder_type() {
		return order_type;
	}

	/**
	 * @param order_type the order_type
	 */
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	/**
	 * @return the order_date
	 */
	public String getOrder_date() {
		return order_date;
	}

	/**
	 * @param order_date the order_date
	 */
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	/**
	 * @return the ship_from_name
	 */
	public String getShip_from_name() {
		return ship_from_name;
	}

	/**
	 * @param ship_from_name the ship_from_name
	 */
	public void setShip_from_name(String ship_from_name) {
		this.ship_from_name = ship_from_name;
	}

	/**
	 * @return the ship_from_attention_to
	 */
	public String getShip_from_attention_to() {
		return ship_from_attention_to;
	}

	/**
	 * @param ship_from_attention_to the ship_from_attention_to
	 */
	public void setShip_from_attention_to(String ship_from_attention_to) {
		this.ship_from_attention_to = ship_from_attention_to;
	}

	/**
	 * @return the ship_from_country
	 */
	public String getShip_from_country() {
		return ship_from_country;
	}

	/**
	 * @param ship_from_country the ship_from_country
	 */
	public void setShip_from_country(String ship_from_country) {
		this.ship_from_country = ship_from_country;
	}

	/**
	 * @return the ship_from_province
	 */
	public String getShip_from_province() {
		return ship_from_province;
	}

	/**
	 * @param ship_from_province the ship_from_province
	 */
	public void setShip_from_province(String ship_from_province) {
		this.ship_from_province = ship_from_province;
	}

	/**
	 * @return the ship_from_city
	 */
	public String getShip_from_city() {
		return ship_from_city;
	}

	/**
	 * @param ship_from_city the ship_from_city
	 */
	public void setShip_from_city(String ship_from_city) {
		this.ship_from_city = ship_from_city;
	}

	/**
	 * @return the ship_from_area
	 */
	public String getShip_from_area() {
		return ship_from_area;
	}

	/**
	 * @param ship_from_area the ship_from_area
	 */
	public void setShip_from_area(String ship_from_area) {
		this.ship_from_area = ship_from_area;
	}

	/**
	 * @return the ship_from_address
	 */
	public String getShip_from_address() {
		return ship_from_address;
	}

	/**
	 * @param ship_from_address the ship_from_address
	 */
	public void setShip_from_address(String ship_from_address) {
		this.ship_from_address = ship_from_address;
	}

	/**
	 * @return the ship_from_postal_code
	 */
	public String getShip_from_postal_code() {
		return ship_from_postal_code;
	}

	/**
	 * @param ship_from_postal_code the ship_from_postal_code
	 */
	public void setShip_from_postal_code(String ship_from_postal_code) {
		this.ship_from_postal_code = ship_from_postal_code;
	}

	/**
	 * @return the ship_from_phone_num
	 */
	public String getShip_from_phone_num() {
		return ship_from_phone_num;
	}

	/**
	 * @param ship_from_phone_num the ship_from_phone_num
	 */
	public void setShip_from_phone_num(String ship_from_phone_num) {
		this.ship_from_phone_num = ship_from_phone_num;
	}

	/**
	 * @return the ship_from_tel_num
	 */
	public String getShip_from_tel_num() {
		return ship_from_tel_num;
	}

	/**
	 * @param ship_from_tel_num the ship_from_tel_num
	 */
	public void setShip_from_tel_num(String ship_from_tel_num) {
		this.ship_from_tel_num = ship_from_tel_num;
	}

	/**
	 * @return the ship_from_email_address
	 */
	public String getShip_from_email_address() {
		return ship_from_email_address;
	}

	/**
	 * @param ship_from_email_address the ship_from_email_address
	 */
	public void setShip_from_email_address(String ship_from_email_address) {
		this.ship_from_email_address = ship_from_email_address;
	}

	/**
	 * @return the ship_to_name
	 */
	public String getShip_to_name() {
		return ship_to_name;
	}

	/**
	 * @param ship_to_name the ship_to_name
	 */
	public void setShip_to_name(String ship_to_name) {
		this.ship_to_name = ship_to_name;
	}

	/**
	 * @return the ship_to_attention_to
	 */
	public String getShip_to_attention_to() {
		return ship_to_attention_to;
	}

	/**
	 * @param ship_to_attention_to the ship_to_attention_to
	 */
	public void setShip_to_attention_to(String ship_to_attention_to) {
		this.ship_to_attention_to = ship_to_attention_to;
	}

	/**
	 * @return the ship_to_country
	 */
	public String getShip_to_country() {
		return ship_to_country;
	}

	/**
	 * @param ship_to_country the ship_to_country
	 */
	public void setShip_to_country(String ship_to_country) {
		this.ship_to_country = ship_to_country;
	}

	/**
	 * @return the ship_to_province
	 */
	public String getShip_to_province() {
		return ship_to_province;
	}

	/**
	 * @param ship_to_province the ship_to_province
	 */
	public void setShip_to_province(String ship_to_province) {
		this.ship_to_province = ship_to_province;
	}

	/**
	 * @return the ship_to_city
	 */
	public String getShip_to_city() {
		return ship_to_city;
	}

	/**
	 * @param ship_to_city the ship_to_city
	 */
	public void setShip_to_city(String ship_to_city) {
		this.ship_to_city = ship_to_city;
	}

	/**
	 * @return the ship_to_area
	 */
	public String getShip_to_area() {
		return ship_to_area;
	}

	/**
	 * @param ship_to_area the ship_to_area
	 */
	public void setShip_to_area(String ship_to_area) {
		this.ship_to_area = ship_to_area;
	}

	/**
	 * @return the ship_to_address
	 */
	public String getShip_to_address() {
		return ship_to_address;
	}

	/**
	 * @param ship_to_address the ship_to_address
	 */
	public void setShip_to_address(String ship_to_address) {
		this.ship_to_address = ship_to_address;
	}

	/**
	 * @return the ship_to_postal_code
	 */
	public String getShip_to_postal_code() {
		return ship_to_postal_code;
	}

	/**
	 * @param ship_to_postal_code the ship_to_postal_code
	 */
	public void setShip_to_postal_code(String ship_to_postal_code) {
		this.ship_to_postal_code = ship_to_postal_code;
	}

	/**
	 * @return the ship_to_phone_num
	 */
	public String getShip_to_phone_num() {
		return ship_to_phone_num;
	}

	/**
	 * @param ship_to_phone_num the ship_to_phone_num
	 */
	public void setShip_to_phone_num(String ship_to_phone_num) {
		this.ship_to_phone_num = ship_to_phone_num;
	}

	/**
	 * @return the ship_to_tel_num
	 */
	public String getShip_to_tel_num() {
		return ship_to_tel_num;
	}

	/**
	 * @param ship_to_tel_num the ship_to_tel_num
	 */
	public void setShip_to_tel_num(String ship_to_tel_num) {
		this.ship_to_tel_num = ship_to_tel_num;
	}

	/**
	 * @return the ship_to_email_address
	 */
	public String getShip_to_email_address() {
		return ship_to_email_address;
	}

	/**
	 * @param ship_to_email_address the ship_to_email_address
	 */
	public void setShip_to_email_address(String ship_to_email_address) {
		this.ship_to_email_address = ship_to_email_address;
	}

	/**
	 * @return the carrier
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * @param carrier the carrier
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	/**
	 * @return the carrier_service
	 */
	public String getCarrier_service() {
		return carrier_service;
	}

	/**
	 * @param carrier_service the carrier_service
	 */
	public void setCarrier_service(String carrier_service) {
		this.carrier_service = carrier_service;
	}

	/**
	 * @return the route_numbers
	 */
	public String getRoute_numbers() {
		return route_numbers;
	}

	/**
	 * @param route_numbers the route_numbers
	 */
	public void setRoute_numbers(String route_numbers) {
		this.route_numbers = route_numbers;
	}

	/**
	 * @return the packing_note
	 */
	public String getPacking_note() {
		return packing_note;
	}

	/**
	 * @param packing_note the packing_note
	 */
	public void setPacking_note(String packing_note) {
		this.packing_note = packing_note;
	}

	/**
	 * @return the complete_delivery
	 */
	public String getComplete_delivery() {
		return complete_delivery;
	}

	/**
	 * @param complete_delivery the complete_delivery
	 */
	public void setComplete_delivery(String complete_delivery) {
		this.complete_delivery = complete_delivery;
	}

	/**
	 * @return the freight
	 */
	public String getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight
	 */
	public void setFreight(String freight) {
		this.freight = freight;
	}

	/**
	 * @return the payment_of_charge
	 */
	public String getPayment_of_charge() {
		return payment_of_charge;
	}

	/**
	 * @param payment_of_charge the payment_of_charge
	 */
	public void setPayment_of_charge(String payment_of_charge) {
		this.payment_of_charge = payment_of_charge;
	}

	/**
	 * @return the payment_district
	 */
	public String getPayment_district() {
		return payment_district;
	}

	/**
	 * @param payment_district the payment_district
	 */
	public void setPayment_district(String payment_district) {
		this.payment_district = payment_district;
	}

	/**
	 * @return the cod
	 */
	public String getCod() {
		return cod;
	}

	/**
	 * @param cod the cod
	 */
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the self_pickup
	 */
	public String getSelf_pickup() {
		return self_pickup;
	}

	/**
	 * @param self_pickup the self_pickup
	 */
	public void setSelf_pickup(String self_pickup) {
		this.self_pickup = self_pickup;
	}

	/**
	 * @return the value_insured
	 */
	public String getValue_insured() {
		return value_insured;
	}

	/**
	 * @param value_insured the value_insured
	 */
	public void setValue_insured(String value_insured) {
		this.value_insured = value_insured;
	}

	/**
	 * @return the declared_value
	 */
	public String getDeclared_value() {
		return declared_value;
	}

	/**
	 * @param declared_value the declared_value
	 */
	public void setDeclared_value(String declared_value) {
		this.declared_value = declared_value;
	}

	/**
	 * @return the return_receipt_service
	 */
	public String getReturn_receipt_service() {
		return return_receipt_service;
	}

	/**
	 * @param return_receipt_service the return_receipt_service
	 */
	public void setReturn_receipt_service(String return_receipt_service) {
		this.return_receipt_service = return_receipt_service;
	}

	/**
	 * @return the delivery_date
	 */
	public String getDelivery_date() {
		return delivery_date;
	}

	/**
	 * @param delivery_date the delivery_date
	 */
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}

	/**
	 * @return the delivery_requested
	 */
	public String getDelivery_requested() {
		return delivery_requested;
	}

	/**
	 * @param delivery_requested the delivery_requested
	 */
	public void setDelivery_requested(String delivery_requested) {
		this.delivery_requested = delivery_requested;
	}

	/**
	 * @return the invoice
	 */
	public String getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice
	 */
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the invoice_type
	 */
	public String getInvoice_type() {
		return invoice_type;
	}

	/**
	 * @param invoice_type the invoice_type
	 */
	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	/**
	 * @return the invoice_title
	 */
	public String getInvoice_title() {
		return invoice_title;
	}

	/**
	 * @param invoice_title the invoice_title
	 */
	public void setInvoice_title(String invoice_title) {
		this.invoice_title = invoice_title;
	}

	/**
	 * @return the invoice_content
	 */
	public String getInvoice_content() {
		return invoice_content;
	}

	/**
	 * @param invoice_content the invoice_content
	 */
	public void setInvoice_content(String invoice_content) {
		this.invoice_content = invoice_content;
	}

	/**
	 * @return the order_note
	 */
	public String getOrder_note() {
		return order_note;
	}

	/**
	 * @param order_note the order_note
	 */
	public void setOrder_note(String order_note) {
		this.order_note = order_note;
	}

	/**
	 * @return the company_note
	 */
	public String getCompany_note() {
		return company_note;
	}

	/**
	 * @param company_note the company_note
	 */
	public void setCompany_note(String company_note) {
		this.company_note = company_note;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the order_total_amount
	 */
	public String getOrder_total_amount() {
		return order_total_amount;
	}

	/**
	 * @param order_total_amount the order_total_amount
	 */
	public void setOrder_total_amount(String order_total_amount) {
		this.order_total_amount = order_total_amount;
	}

	/**
	 * @return the order_discount
	 */
	public String getOrder_discount() {
		return order_discount;
	}

	/**
	 * @param order_discount the order_discount
	 */
	public void setOrder_discount(String order_discount) {
		this.order_discount = order_discount;
	}

	/**
	 * @return the balance_amount
	 */
	public String getBalance_amount() {
		return balance_amount;
	}

	/**
	 * @param balance_amount the balance_amount
	 */
	public void setBalance_amount(String balance_amount) {
		this.balance_amount = balance_amount;
	}

	/**
	 * @return the coupons_amount
	 */
	public String getCoupons_amount() {
		return coupons_amount;
	}

	/**
	 * @param coupons_amount the coupons_amount
	 */
	public void setCoupons_amount(String coupons_amount) {
		this.coupons_amount = coupons_amount;
	}

	/**
	 * @return the gift_card_amount
	 */
	public String getGift_card_amount() {
		return gift_card_amount;
	}

	/**
	 * @param gift_card_amount the gift_card_amount
	 */
	public void setGift_card_amount(String gift_card_amount) {
		this.gift_card_amount = gift_card_amount;
	}

	/**
	 * @return the other_charge
	 */
	public String getOther_charge() {
		return other_charge;
	}

	/**
	 * @param other_charge the other_charge
	 */
	public void setOther_charge(String other_charge) {
		this.other_charge = other_charge;
	}

	/**
	 * @return the actual_amount
	 */
	public String getActual_amount() {
		return actual_amount;
	}

	/**
	 * @param actual_amount the actual_amount
	 */
	public void setActual_amount(String actual_amount) {
		this.actual_amount = actual_amount;
	}

	/**
	 * @return the customer_payment_method
	 */
	public String getCustomer_payment_method() {
		return customer_payment_method;
	}

	/**
	 * @param customer_payment_method the customer_payment_method
	 */
	public void setCustomer_payment_method(String customer_payment_method) {
		this.customer_payment_method = customer_payment_method;
	}

	/**
	 * @return the monthly_account
	 */
	public String getMonthly_account() {
		return monthly_account;
	}

	/**
	 * @param monthly_account the monthly_account
	 */
	public void setMonthly_account(String monthly_account) {
		this.monthly_account = monthly_account;
	}

	/**
	 * @return the is_appoint_delivery
	 */
	public String getIs_appoint_delivery() {
		return is_appoint_delivery;
	}

	/**
	 * @param is_appoint_delivery the is_appoint_delivery
	 */
	public void setIs_appoint_delivery(String is_appoint_delivery) {
		this.is_appoint_delivery = is_appoint_delivery;
	}

	/**
	 * @return the appoint_delivery_status
	 */
	public String getAppoint_delivery_status() {
		return appoint_delivery_status;
	}

	/**
	 * @param appoint_delivery_status the appoint_delivery_status
	 */
	public void setAppoint_delivery_status(String appoint_delivery_status) {
		this.appoint_delivery_status = appoint_delivery_status;
	}

	/**
	 * @return the appoint_delivery_remark
	 */
	public String getAppoint_delivery_remark() {
		return appoint_delivery_remark;
	}

	/**
	 * @param appoint_delivery_remark the appoint_delivery_remark
	 */
	public void setAppoint_delivery_remark(String appoint_delivery_remark) {
		this.appoint_delivery_remark = appoint_delivery_remark;
	}

	/**
	 * @return the from_flag
	 */
	public String getFrom_flag() {
		return from_flag;
	}

	/**
	 * @param from_flag the from_flag
	 */
	public void setFrom_flag(String from_flag) {
		this.from_flag = from_flag;
	}

	/**
	 * @return the is_easy
	 */
	public String getIs_easy() {
		return is_easy;
	}

	/**
	 * @param is_easy the is_easy
	 */
	public void setIs_easy(String is_easy) {
		this.is_easy = is_easy;
	}

	/**
	 * @return the user_def1
	 */
	public String getUser_def1() {
		return user_def1;
	}

	/**
	 * @param user_def1 the user_def1
	 */
	public void setUser_def1(String user_def1) {
		this.user_def1 = user_def1;
	}

	/**
	 * @return the user_def2
	 */
	public String getUser_def2() {
		return user_def2;
	}

	/**
	 * @param user_def2 the user_def2
	 */
	public void setUser_def2(String user_def2) {
		this.user_def2 = user_def2;
	}

	/**
	 * @return the user_def3
	 */
	public String getUser_def3() {
		return user_def3;
	}

	/**
	 * @param user_def3 the user_def3
	 */
	public void setUser_def3(String user_def3) {
		this.user_def3 = user_def3;
	}

	/**
	 * @return the user_def4
	 */
	public String getUser_def4() {
		return user_def4;
	}

	/**
	 * @param user_def4 the user_def4
	 */
	public void setUser_def4(String user_def4) {
		this.user_def4 = user_def4;
	}

	/**
	 * @return the user_def5
	 */
	public String getUser_def5() {
		return user_def5;
	}

	/**
	 * @param user_def5 the user_def5
	 */
	public void setUser_def5(String user_def5) {
		this.user_def5 = user_def5;
	}

	/**
	 * @return the user_def6
	 */
	public String getUser_def6() {
		return user_def6;
	}

	/**
	 * @param user_def6 the user_def6
	 */
	public void setUser_def6(String user_def6) {
		this.user_def6 = user_def6;
	}

	/**
	 * @return the user_def7
	 */
	public String getUser_def7() {
		return user_def7;
	}

	/**
	 * @param user_def7 the user_def7
	 */
	public void setUser_def7(String user_def7) {
		this.user_def7 = user_def7;
	}

	/**
	 * @return the user_def8
	 */
	public String getUser_def8() {
		return user_def8;
	}

	/**
	 * @param user_def8 the user_def8
	 */
	public void setUser_def8(String user_def8) {
		this.user_def8 = user_def8;
	}

	/**
	 * @return the user_def9
	 */
	public String getUser_def9() {
		return user_def9;
	}

	/**
	 * @param user_def9 the user_def9
	 */
	public void setUser_def9(String user_def9) {
		this.user_def9 = user_def9;
	}

	/**
	 * @return the user_def10
	 */
	public String getUser_def10() {
		return user_def10;
	}

	/**
	 * @param user_def10 the user_def10
	 */
	public void setUser_def10(String user_def10) {
		this.user_def10 = user_def10;
	}

	/**
	 * @return the user_def11
	 */
	public String getUser_def11() {
		return user_def11;
	}

	/**
	 * @param user_def11 the user_def11
	 */
	public void setUser_def11(String user_def11) {
		this.user_def11 = user_def11;
	}

	/**
	 * @return the user_def12
	 */
	public String getUser_def12() {
		return user_def12;
	}

	/**
	 * @param user_def12 the user_def12
	 */
	public void setUser_def12(String user_def12) {
		this.user_def12 = user_def12;
	}

	/**
	 * @return the user_def13
	 */
	public String getUser_def13() {
		return user_def13;
	}

	/**
	 * @param user_def13 the user_def13
	 */
	public void setUser_def13(String user_def13) {
		this.user_def13 = user_def13;
	}

	/**
	 * @return the user_def14
	 */
	public String getUser_def14() {
		return user_def14;
	}

	/**
	 * @param user_def14 the user_def14
	 */
	public void setUser_def14(String user_def14) {
		this.user_def14 = user_def14;
	}

	/**
	 * @return the user_def15
	 */
	public String getUser_def15() {
		return user_def15;
	}

	/**
	 * @param user_def15 the user_def15
	 */
	public void setUser_def15(String user_def15) {
		this.user_def15 = user_def15;
	}

	/**
	 * @return the user_def16
	 */
	public String getUser_def16() {
		return user_def16;
	}

	/**
	 * @param user_def16 the user_def16
	 */
	public void setUser_def16(String user_def16) {
		this.user_def16 = user_def16;
	}

	/**
	 * @return the user_def17
	 */
	public String getUser_def17() {
		return user_def17;
	}

	/**
	 * @param user_def17 the user_def17
	 */
	public void setUser_def17(String user_def17) {
		this.user_def17 = user_def17;
	}

	/**
	 * @return the user_def18
	 */
	public String getUser_def18() {
		return user_def18;
	}

	/**
	 * @param user_def18 the user_def18
	 */
	public void setUser_def18(String user_def18) {
		this.user_def18 = user_def18;
	}

	/**
	 * @return the user_def19
	 */
	public String getUser_def19() {
		return user_def19;
	}

	/**
	 * @param user_def19 the user_def19
	 */
	public void setUser_def19(String user_def19) {
		this.user_def19 = user_def19;
	}

	/**
	 * @return the user_def20
	 */
	public String getUser_def20() {
		return user_def20;
	}

	/**
	 * @param user_def20 the user_def20
	 */
	public void setUser_def20(String user_def20) {
		this.user_def20 = user_def20;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsSailOrderRequestHeader [company=" + company + ", warehouse=" + warehouse + ", shop_name=" + shop_name + ", erp_order=" + erp_order + ", order_type=" + order_type + ", order_date=" + order_date + ", ship_from_name=" + ship_from_name + ", ship_from_attention_to="
				+ ship_from_attention_to + ", ship_from_country=" + ship_from_country + ", ship_from_province=" + ship_from_province + ", ship_from_city=" + ship_from_city + ", ship_from_area=" + ship_from_area + ", ship_from_address=" + ship_from_address + ", ship_from_postal_code="
				+ ship_from_postal_code + ", ship_from_phone_num=" + ship_from_phone_num + ", ship_from_tel_num=" + ship_from_tel_num + ", ship_from_email_address=" + ship_from_email_address + ", ship_to_name=" + ship_to_name + ", ship_to_attention_to=" + ship_to_attention_to + ", ship_to_country="
				+ ship_to_country + ", ship_to_province=" + ship_to_province + ", ship_to_city=" + ship_to_city + ", ship_to_area=" + ship_to_area + ", ship_to_address=" + ship_to_address + ", ship_to_postal_code=" + ship_to_postal_code + ", ship_to_phone_num=" + ship_to_phone_num
				+ ", ship_to_tel_num=" + ship_to_tel_num + ", ship_to_email_address=" + ship_to_email_address + ", carrier=" + carrier + ", carrier_service=" + carrier_service + ", route_numbers=" + route_numbers + ", packing_note=" + packing_note + ", complete_delivery=" + complete_delivery
				+ ", freight=" + freight + ", payment_of_charge=" + payment_of_charge + ", payment_district=" + payment_district + ", cod=" + cod + ", amount=" + amount + ", self_pickup=" + self_pickup + ", value_insured=" + value_insured + ", declared_value=" + declared_value
				+ ", return_receipt_service=" + return_receipt_service + ", delivery_date=" + delivery_date + ", delivery_requested=" + delivery_requested + ", invoice=" + invoice + ", invoice_type=" + invoice_type + ", invoice_title=" + invoice_title + ", invoice_content=" + invoice_content
				+ ", order_note=" + order_note + ", company_note=" + company_note + ", priority=" + priority + ", order_total_amount=" + order_total_amount + ", order_discount=" + order_discount + ", balance_amount=" + balance_amount + ", coupons_amount=" + coupons_amount + ", gift_card_amount="
				+ gift_card_amount + ", other_charge=" + other_charge + ", actual_amount=" + actual_amount + ", customer_payment_method=" + customer_payment_method + ", monthly_account=" + monthly_account + ", is_appoint_delivery=" + is_appoint_delivery + ", appoint_delivery_status="
				+ appoint_delivery_status + ", appoint_delivery_remark=" + appoint_delivery_remark + ", from_flag=" + from_flag + ", is_easy=" + is_easy + "]";
	}

}
