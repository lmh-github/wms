/*
 * @(#)KuaiDiAction.java 2013-7-30
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.web.action.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.HttpClientUtil;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.entity.ShippingInfo;
import com.gionee.wms.kuaidi.JacksonHelper;
import com.gionee.wms.kuaidi.pojo.NoticeRequest;
import com.gionee.wms.kuaidi.pojo.NoticeResponse;
import com.gionee.wms.kuaidi.pojo.Result;
import com.gionee.wms.kuaidi.pojo.ResultItem;
import com.gionee.wms.service.basis.ShippingInfoService;
import com.gionee.wms.vo.ShippingParamVo;

/**
 * WMS与ECShop快递接口类
 * 
 * @author ZuoChangjun 2013-7-30
 */
@Controller("shippingAction")
@Scope("prototype")
public class ShippingAction {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Logger kuaidiLog = LoggerFactory.getLogger("kuaidiLog");
	
	@Autowired
	private ShippingInfoService shippingInfoService;
	
	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	/**
	 * 根据快递公司编号和快递单编号，查询快递信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryShippingInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			// 接收请求参数并转换为Map对象
			// Map<String,Object>
			// pMap=JsonUtil.toBean(ServletActionContext.getRequest(),HashMap.class);
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream());
			ShippingParamVo searchParam = jsonUtils.fromJson(jsonStr,
					ShippingParamVo.class);
			if (searchParam == null
					|| StringUtils.isBlank(searchParam.getKey())
					|| StringUtils.isBlank(searchParam.getComCode())
					|| StringUtils.isBlank(searchParam.getBillCode())) {
				logger.error("queryShippingInfo error：key is null,or comCode is null,or billCode is null.");
				resultMap.put("status", WmsConstants.Status.PARAM_ERROR.getCode());
				resultMap
						.put("message",
								WmsConstants.Status.PARAM_ERROR.getMessage()
										+ ":key is null,or comCode is null,or billCode is null.");
				// 输出json结果
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
				return null;
			}
			String url = WmsConstants.Kuaidi100_URL + "?id="
					+ searchParam.getKey() + "&com="
					+ searchParam.getComCode() + "&nu="
					+ searchParam.getBillCode() + "&valicode="
					+ searchParam.getValidCode() + "&show="
					+ searchParam.getResultType() + "&muti="
					+ searchParam.getIsMultiLine() + "&order="
					+ searchParam.getOrder();
			String result = HttpClientUtil.httpGet(url);
			//resultMap.put("code", WmsConstants.Status.SUCCESS.getCode());
			//resultMap.put("message", WmsConstants.Status.SUCCESS.getMessage());
			//resultMap.put("result", result);
			// 输出json结果
			//ActionUtils.outputJson(jsonUtils.toJson(resultMap));
			ActionUtils.outputJson(result);
		} catch (Exception e) {
			logger.error("queryShippingInfo error", e);
			resultMap.put("status", WmsConstants.Status.EXCEPTION.getCode());
			resultMap.put("message", WmsConstants.Status.EXCEPTION.getMessage()
					+ ":" + e.getMessage());
			// 输出json结果
			ActionUtils.outputJson(jsonUtils.toJson(resultMap));
		}
		return null;
	}
	
	/**
	 * 根据快递公司编号和快递单编号,返回快递信息查询url
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryHTMLShippingInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			// 接收请求参数并转换为Map对象
			// Map<String,Object>
			// pMap=JsonUtil.toBean(ServletActionContext.getRequest(),HashMap.class);
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream());
			ShippingParamVo searchParam = jsonUtils.fromJson(jsonStr,
					ShippingParamVo.class);
			if (searchParam == null
					|| StringUtils.isBlank(searchParam.getKey())
					|| StringUtils.isBlank(searchParam.getComCode())
					|| StringUtils.isBlank(searchParam.getBillCode())) {
				logger.error("queryShippingInfo error：key is null,or comCode is null,or billCode is null.");
				resultMap.put("status", WmsConstants.Status.PARAM_ERROR.getCode());
				resultMap
						.put("message",
								WmsConstants.Status.PARAM_ERROR.getMessage()
										+ ":key is null,or comCode is null,or billCode is null.");
				// 输出json结果
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
				return null;
			}
			String url = WmsConstants.Kuaidi100_HTML_URL + "?key="
					+ searchParam.getKey() + "&com="
					+ searchParam.getComCode() + "&nu="
					+ searchParam.getBillCode();
			String result = HttpClientUtil.httpGet(url);
			resultMap.put("status", WmsConstants.Status.SUCCESS.getCode());
			resultMap.put("message", WmsConstants.Status.SUCCESS.getMessage());
			resultMap.put("url", result);
			// 输出json结果
			ActionUtils.outputJson(jsonUtils.toJson(resultMap));
			//ActionUtils.outputJson(result);
		} catch (Exception e) {
			logger.error("queryShippingInfo error", e);
			resultMap.put("status", WmsConstants.Status.EXCEPTION.getCode());
			resultMap.put("message", WmsConstants.Status.EXCEPTION.getMessage()
					+ ":" + e.getMessage());
			// 输出json结果
			ActionUtils.outputJson(jsonUtils.toJson(resultMap));
		}

		return null;
	}
	
	/**
	 * 根据快递公司编号和快递单编号，查询快递信息(合并上面两个接口)
	 * 下面几家快递公司查询信息以HTML页面返回
	 *  shippingComCodeMap.put("sto_express", "shentong");//申通快递
	 *	shippingComCodeMap.put("ems", "ems");//EMS
	 *	shippingComCodeMap.put("sf_express", "shunfeng");//顺丰快递
	 *	shippingComCodeMap.put("post_email", "youzhengguonei");//邮政平邮
	 *	shippingComCodeMap.put("post_express", "youzhengguonei");//邮政快递包裹
	 * @return
	 * @throws Exception
	 */
	public String queryShippingInfoByComCodeAndBillCode() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			// 接收请求参数并转换为Map对象
			// Map<String,Object>
			// pMap=JsonUtil.toBean(ServletActionContext.getRequest(),HashMap.class);
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream());
			ShippingParamVo searchParam = jsonUtils.fromJson(jsonStr,
					ShippingParamVo.class);
			if (searchParam == null
					|| StringUtils.isBlank(searchParam.getComCode())
					|| StringUtils.isBlank(searchParam.getBillCode())) {
				logger.error("queryShippingInfo error：comCode is null,or billCode is null.");
				resultMap.put("status", WmsConstants.Status.PARAM_ERROR.getCode());
				resultMap.put("message",
								WmsConstants.Status.PARAM_ERROR.getMessage()
										+ ":or comCode is null,or billCode is null.");
				// 输出json结果
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
				return null;
			}
			String url = WmsConstants.Kuaidi100_URL + "?id="
					+ WmsConstants.Kuaidi100_KEY + "&com="
					+ searchParam.getComCode() + "&nu="
					+ searchParam.getBillCode() + "&valicode="
					+ searchParam.getValidCode() + "&show="
					+ searchParam.getResultType() + "&muti="
					+ searchParam.getIsMultiLine() + "&order="
					+ searchParam.getOrder();
			String html_url = WmsConstants.Kuaidi100_HTML_URL + "?key="
					+ WmsConstants.Kuaidi100_KEY + "&com="
					+ searchParam.getComCode() + "&nu="
					+ searchParam.getBillCode();
			//快递公司:查询信息以HTML页面返回
			if (WmsConstants.htmlShippingComCodeList.contains(searchParam.getComCode())) {
				String reply_url = HttpClientUtil.httpGet(html_url);
				logger.info(html_url + " result: " + reply_url);
				resultMap.put("status", WmsConstants.Status.SUCCESS.getCode());
				resultMap.put("message", WmsConstants.Status.SUCCESS.getMessage());
				resultMap.put("url", reply_url);
				// 输出json结果
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
			} 
			//快递公司:查询信息以JSON或XML或HTML Table片段或Text格式返回
			else {
				String result = HttpClientUtil.httpGet(url);
				logger.info(url + " result: " + result);
				ActionUtils.outputJson(result);
			}
			//resultMap.put("code", WmsConstants.Status.SUCCESS.getCode());
			//resultMap.put("message", WmsConstants.Status.SUCCESS.getMessage());
			//resultMap.put("result", result);
			// 输出json结果
			//ActionUtils.outputJson(jsonUtils.toJson(resultMap));
		} catch (Exception e) {
			logger.error("queryShippingInfo error", e);
			resultMap.put("status", WmsConstants.Status.EXCEPTION.getCode());
			resultMap.put("message", WmsConstants.Status.EXCEPTION.getMessage()
					+ ":" + e.getMessage());
			// 输出json结果
			ActionUtils.outputJson(jsonUtils.toJson(resultMap));
		}
		return null;
	}
	
	/**
	 * 快递100订阅后的回调接口
	 * @return
	 * @throws Exception
	 */
	public String kuaidiCallback() throws Exception {
		NoticeResponse resp = new NoticeResponse();
		resp.setResult(false);
		resp.setReturnCode("500");
		resp.setMessage("保存失败");
		try {
			String param = ActionUtils.getRequest().getParameter("param");
			NoticeRequest nReq = JacksonHelper.fromJSON(param,
					NoticeRequest.class);

			Result result = nReq.getLastResult();
			// 处理快递结果,根据推送过来的公司编号和快递单号更新快递信息
			ShippingInfo shippingInfo=shippingInfoService.getShippingInfo(result.getCom(), result.getNu());
			if(null!=shippingInfo) {
				shippingInfo.setPushStatus(nReq.getStatus());
				shippingInfo.setPushMessage(nReq.getMessage());
				shippingInfo.setIsCheck(result.getIscheck());
				shippingInfo.setState(result.getState());
				shippingInfo.setData(JacksonHelper.toJSON(result.getData()));
				shippingInfo.setLastPushTime(new Date());
				shippingInfoService.updateShippingAndOrder(shippingInfo);
			}
			kuaidiLog.info(sdf.format(new Date()) + "{]" + nReq.getStatus() + "{]" + nReq.getMessage() + "{]" + result.getCom() + "{]" + result.getNu() + "{]" + result.getState() + "{]" + result.getIscheck() + "{]");
			
			resp.setResult(true);
			resp.setMessage("成功");
			resp.setReturnCode("200");
			ActionUtils.outputJson(JacksonHelper.toJSON(resp));	//这里必须返回，否则认为失败，过30分钟又会重复推送。
		} catch (Exception e) {
			resp.setMessage("保存失败" + e.getMessage());
			ActionUtils.outputJson(JacksonHelper.toJSON(resp));//保存失败，服务端等30分钟会重复推送。
		}
		return null;
	}
	
	/**
	 * 根据快递公司编号和快递单编号，查询快递信息
	 * @return
	 * @throws Exception
	 */
	public String queryShippingInfoKd() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		String jsoncallback = ActionUtils.getRequest().getParameter("jsoncallback");
		try {
			// 接收请求参数并转换为Map对象
			// Map<String,Object>
			// pMap=JsonUtil.toBean(ServletActionContext.getRequest(),HashMap.class);
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream());
			ShippingParamVo searchParam = jsonUtils.fromJson(jsonStr,
					ShippingParamVo.class);
			if (searchParam == null
					|| StringUtils.isBlank(searchParam.getComCode())
					|| StringUtils.isBlank(searchParam.getBillCode())) {
				logger.error("queryShippingInfo error：comCode is null,or billCode is null.");
				resultMap.put("status", WmsConstants.Status.PARAM_ERROR.getCode());
				resultMap.put("message",
								WmsConstants.Status.PARAM_ERROR.getMessage()
										+ ":or comCode is null,or billCode is null.");
				// 输出json结果
				if(null!=jsoncallback) {
					// jsonp
					ActionUtils.outputJson(jsonUtils.toJsonP(jsoncallback, resultMap));
				} else {
					ActionUtils.outputJson(jsonUtils.toJson(resultMap));
				}
				return null;
			}
			
			// 获得快递100传入的内容原样输出
			ShippingInfo shippingInfo=shippingInfoService.getShippingInfo(searchParam.getComCode(), searchParam.getBillCode());
			if(null==shippingInfo) {
				resultMap.put("status", WmsConstants.Status.NO_RECORD.getCode());
				resultMap.put("message", WmsConstants.Status.NO_RECORD.getMessage());
			} else {
				resultMap.put("status", WmsConstants.Status.SUCCESS.getCode());
				resultMap.put("message", WmsConstants.Status.SUCCESS.getMessage());
				resultMap.put("state", shippingInfo.getState());
				resultMap.put("isCheck", shippingInfo.getIsCheck());
				resultMap.put("com", shippingInfo.getCompany());
				resultMap.put("nu", shippingInfo.getShippingNo());
				if(null!=shippingInfo.getData() && !"".equals(shippingInfo.getData())) {
					resultMap.put("data", JacksonHelper.fromJSONList(shippingInfo.getData(), ResultItem.class));
				} else {
					resultMap.put("data", new JSONObject());
				}
			}
			if(null!=jsoncallback) {
				// jsonp
				ActionUtils.outputJson(jsonUtils.toJsonP(jsoncallback, resultMap));
			} else {
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
			}
		} catch (Exception e) {
			logger.error("queryShippingInfo error", e);
			resultMap.put("status", WmsConstants.Status.EXCEPTION.getCode());
			resultMap.put("message", WmsConstants.Status.EXCEPTION.getMessage()
					+ ":" + e.getMessage());
			// 输出json结果
			if(null!=jsoncallback) {
				// jsonp
				ActionUtils.outputJson(jsonUtils.toJsonP(jsoncallback, resultMap));
			} else {
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
			}
		}
		return null;
	}
}
