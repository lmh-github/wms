package com.gionee.wms.web.action.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.ShippingInfo;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingInfoService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@Controller("KuaidiReportAction")
@Scope("prototype")
public class KuaidiReportAction extends AjaxActionSupport {
	private static final long serialVersionUID = -5550025051973418931L;
	@Autowired
	private ShippingInfoService shippingInfoService; 

	/** 页面相关属性 **/
	private Long id;
	private String company;
	private String shippingNo;
	private Date finishedTimeBegin;// 入库起始时间
	private Date finishedTimeEnd;// 入库结束时间
	private List<ShippingInfo> shippingInfoList;// 快递信息列表
	private Page page = new Page();
	private ShippingInfo shippingInfo;
	private String orderCode;
	
	public String listKuaidi() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("company", company);
		criteria.put("shippingNo", shippingNo);
		criteria.put("orderCode", orderCode);
		int totalRow = shippingInfoService.queryShippingInfoTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		shippingInfoList = shippingInfoService.queryShippingInfoList(criteria, page);
		return "summary_kuaidi";
	}
	
	public String kuaidiDetail() throws Exception {
		shippingInfo=shippingInfoService.getShippingInfoById(id);
		return "detail_kuaidi";
	}
	
	public String reDescribe() throws Exception {
		ShippingInfo shippingInfo=shippingInfoService.getShippingInfoById(id);
		shippingInfo.setSubscribeResult(WmsConstants.KuaidiStatus.UNSUB.getCode()+"");	// 设置为未订阅
		shippingInfo.setSubscribeCount(new Integer(0));
		try {
			shippingInfoService.updateShippingInfo(shippingInfo);
			ajaxSuccess("重新订阅快递信息");
		} catch (ServiceException e) {
			logger.error("重新订阅快递信息出错", e);
			ajaxError("重新订阅快递信息失败：" + e.getMessage());
		}
		return null;
	}

	public void setFinishedTimeBegin(Date finishedTimeBegin) {
		this.finishedTimeBegin = finishedTimeBegin;
	}

	public void setFinishedTimeEnd(Date finishedTimeEnd) {
		this.finishedTimeEnd = finishedTimeEnd;
	}

	public Date getFinishedTimeBegin() {
		return finishedTimeBegin;
	}

	public Date getFinishedTimeEnd() {
		return finishedTimeEnd;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getShippingNo() {
		return shippingNo;
	}

	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}

	public List<ShippingInfo> getShippingInfoList() {
		return shippingInfoList;
	}

	public void setShippingInfoList(List<ShippingInfo> shippingInfoList) {
		this.shippingInfoList = shippingInfoList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public ShippingInfo getShippingInfo() {
		return shippingInfo;
	}

	public void setShippingInfo(ShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}
