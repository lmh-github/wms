package com.gionee.wms.web.action.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.DateConvert;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.entity.SaleStat;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.stat.OrderStatService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;

/**
 * 
 * 作者:milton.zhang
 * 时间:2014-4-24
 * 描述:销售统计报表
 */
@Controller("SaleReportAction")
@Scope("prototype")
public class SaleReportAction extends AjaxActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5100900009175777714L;

	@Autowired
	private OrderStatService orderStatService;
	
	private List<SaleStat> statList;
	private Date statTimeBegin;// 统计起始时间
	private Date statTimeEnd;// 统计结束时间
	private String exports;
	
	/**
	 * 查询销售订单统计列表
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("statTimeBegin", statTimeBegin);
		criteria.put("statTimeEnd", statTimeEnd);
		statList = orderStatService.querySaleStatList(criteria);
		if("1".equals(exports)) {
			if (CollectionUtils.isEmpty(statList)) {
				throw new ServiceException("数据不存在");
			}
			List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
			int count = 0 ;
			for (SaleStat item : statList) {
				Map<String, String> repeatData = new HashMap<String, String>();
				repeatData.put("ORDER_TYPE", item.getOrderType());
				repeatData.put("SALE_ORG", item.getSaleOrg());
				repeatData.put("FX_CHANNEL", item.getFxChannel());
				repeatData.put("SALER", item.getSaler());
				repeatData.put("SENDER", item.getSender());
				repeatData.put("INVOICER", item.getInvoicer());
				repeatData.put("SHIPPER", item.getShipper());
				repeatData.put("ORDER_REASON", item.getOrderReason());
				repeatData.put("MATERIAL_CODE", item.getMaterialCode());
				repeatData.put("ORDER_NUM", String.valueOf(item.getOrderNum()));
				repeatData.put("PURCHASE_CODE", item.getPurchaseCode());
				repeatData.put("PURCHASE_DATE", DateConvert.convertD2Str(item.getPurchaseDate()));
				repeatData.put("SHIPPING_TYPE", item.getShippingType());
				repeatData.put("INVOICE_TYPE", item.getInvoiceType());
				repeatData.put("USE", item.getUse());
				repeatData.put("PO_CODE", item.getPoCode());
				repeatData.put("PO_PRO_CODE", item.getPoProCode());
				repeatData.put("UNIT_PRICE", item.getUnitPrice()==null?"0":item.getUnitPrice().toString());
				repeatData.put("POSTING_DATE", DateConvert.convertD2Str(item.getPostingDate()));
				repeatData.put("FACTORY", item.getFactory());
				repeatData.put("WAREHOUSE", item.getWarehouse());
				sheetData.add(repeatData);
				count++;
				if(count>=10000) {
					break;	// 大于10000条终止，防止内存溢出
				}
			}
			ExcelModule excelModule = new ExcelModule(sheetData);
			HttpServletResponse response = ServletActionContext.getResponse();
			// 清空输出流
			response.reset();
			// 设置响应头和下载保存的文件名
			response.setHeader("content-disposition", "attachment;filename=order_stat_list.xls");
			// 定义输出类型
			response.setContentType("APPLICATION/msexcel");
			OutputStream out = null;
			try {
				String templeteFile = ActionUtils.getProjectPath() + "/export/order_stat_template.xls";
				String tempFile = ActionUtils.getProjectPath() + "/export/order_stat_list.xls";
				File file = ExcelExpUtil.expExcel(excelModule, templeteFile, tempFile);
				out = response.getOutputStream();
				FileUtils.copyFile(file, out);
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return null;
		} else {
			return SUCCESS;
		}
	}
	
	public List<SaleStat> getStatList() {
		return statList;
	}

	public void setStatList(List<SaleStat> statList) {
		this.statList = statList;
	}

	public Date getStatTimeBegin() {
		return statTimeBegin;
	}

	public void setStatTimeBegin(Date statTimeBegin) {
		this.statTimeBegin = statTimeBegin;
	}

	public Date getStatTimeEnd() {
		return statTimeEnd;
	}

	public void setStatTimeEnd(Date statTimeEnd) {
		this.statTimeEnd = statTimeEnd;
	}

	public String getExports() {
		return exports;
	}

	public void setExports(String exports) {
		this.exports = exports;
	}

}
