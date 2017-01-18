package com.gionee.wms.web.action.account;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants.DeliveryBatchStatus;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.OperationLog;
import com.gionee.wms.service.account.OperationLogService;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;

@Controller("OperationLogAction")
@Scope("prototype")
public class OperationLogAction extends ActionSupport {
	private static final long serialVersionUID = -5550025051973418931L;

	private OperationLogService opLogService;

	/** 页面相关属性 **/
	private List<OperationLog> opLogList; // 操作日志列表
	private String opType;
	private String opKey;
	private String operator;
	private Date opTimeBegin;// 入库起始时间
	private Date opTimeEnd;// 入库结束时间
	private Page page = new Page();

	public String list() throws Exception {
		// 初始化属性对象
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("opType", StringUtils.defaultIfBlank(opType, null));
		criteria.put("opKey", StringUtils.defaultIfBlank(opKey, null));
		criteria.put("operator", StringUtils.defaultIfBlank(operator, null));
		criteria.put("opTimeBegin", opTimeBegin);
		criteria.put("opTimeEnd", opTimeEnd);
		criteria.put("handlingStatus", DeliveryBatchStatus.FINISHED.getCode());
		int totalRow = opLogService.getOpLogListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		opLogList = opLogService.getOpLogList(criteria, page);
		return SUCCESS;
	}

	public Page getPage() {
		return page;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getOpKey() {
		return opKey;
	}

	public void setOpKey(String opKey) {
		this.opKey = opKey;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOpTimeBegin() {
		return opTimeBegin;
	}

	public void setOpTimeBegin(Date opTimeBegin) {
		this.opTimeBegin = opTimeBegin;
	}

	public Date getOpTimeEnd() {
		return opTimeEnd;
	}

	public void setOpTimeEnd(Date opTimeEnd) {
		this.opTimeEnd = opTimeEnd;
	}

	public List<OperationLog> getOpLogList() {
		return opLogList;
	}
	@Autowired
	public void setOpLogService(OperationLogService opLogService) {
		this.opLogService = opLogService;
	}

	
	

}
