package com.gionee.wms.web.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.SsoGetMenuResult;
import com.gionee.wms.dto.SsoLoginCheckResult;
import com.gionee.wms.dto.SsoValidateTicketResult;
import com.google.common.collect.Maps;

/**
 * 单点登录
 * @author Administrator
 *
 */
@Component("ssoClient")
public class SsoClient {
	private static Logger logger = LoggerFactory.getLogger(SsoClient.class);

	private static final int CONNECTION_POOL_SIZE = 10;
	private static final int TIMEOUT_SECONDS = 10;
	private HttpClient httpClient = null;

	/**
	 * 始始化HttpClient,且为多线程安全.
	 */
	@PostConstruct
	public void init() {
		// Set connection pool
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(CONNECTION_POOL_SIZE);
		httpClient = new DefaultHttpClient(cm);

		// set timeout
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_SECONDS * 1000);
	}

	/**
	 * 销毁HttpClient实例.
	 */
	@PreDestroy
	public void destroy() {
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	
	/**
	 * 从用户中心iuc获取菜单信息(http://passport.cm.com/iuc/cas/menu)
	 * @param ssoTgt
	 * @return
	 * @throws IOException
	 */
	public SsoGetMenuResult getMenus(String ssoTgt) throws IOException{
		String url = WmsConstants.UUC_URL + WmsConstants.UUC_FETCH_MENU_URI;
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.addHeader("Content-Type", "application/json");
	
		//填充IUC参数并转换为JSON串
		String jsonString = fillJsonParamsWithTgt(ssoTgt);			
		StringEntity params = new StringEntity(jsonString, "UTF-8");
		httpPost.setEntity(params);

		// 获取菜单信息
		HttpEntity entity = null;
		try {
			HttpContext context = new BasicHttpContext();
			HttpResponse remoteResponse = httpClient.execute(httpPost, context);
			logger.info(remoteResponse.getStatusLine().getStatusCode()+"");
			entity = remoteResponse.getEntity();
		} catch (Exception e) {
			logger.error("Get remote menue error,url=" + url, e);
			httpPost.abort();
			return null;
		}
		if (entity == null) {
			throw new RuntimeException("SsoGetMenuResult is  null.");
		}

		// 转换菜单json字符串为java对象
		InputStream input = entity.getContent();
		try {
			String jsonStr = IOUtils.toString(input,"UTF-8");
//			String jsonStr = "{\"code\":\"0\",\"menu\":[{\"child\":[{\"name\":\"库存信息\",\"path\":\"/stock/stock.action\"},{\"name\":\"库存盘点\",\"path\":\"/stock/stockCheck.action\"},{\"name\":\"库存流水\",\"path\":\"/stock/stockChange.action\"}],\"name\":\"库存管理\",\"path\":\"0\"},{\"child\":[{\"name\":\"商品分类\",\"path\":\"/wares/category.action\"},{\"name\":\"商品类型\",\"path\":\"/wares/attrSet.action\"},{\"name\":\"基本商品\",\"path\":\"/wares/wares.action\"},{\"name\":\"商品SKU\",\"path\":\"/wares/sku.action\"},{\"name\":\"商品个体\",\"path\":\"/wares/indiv.action\"}],\"name\":\"商品管理\",\"path\":\"0\"},{\"child\":[{\"name\":\"采购收货\",\"path\":\"/stock/purchaseRecv.action\"},{\"name\":\"采购预收\",\"path\":\"/stock/purPreRecv!list.action\"},{\"name\":\"退货查询\",\"path\":\"/stock/rmaRecv.action\"},{\"name\":\"快捷收货\",\"path\":\"/stock/shortcutRecv.action\"}],\"name\":\"入库管理\",\"path\":\"0\"},{\"child\":[{\"name\":\"销售发货\",\"path\":\"/stock/deliveryBatch.action\"},{\"name\":\"创建发货批次\",\"path\":\"/stock/salesOrder!listForDely.action\"}],\"name\":\"出库管理\",\"path\":\"0\"},{\"child\":[{\"name\":\"采购进货汇总\",\"path\":\"/report/receiveReport!listPurRecvSummary.action\"},{\"name\":\"采购进货明细\",\"path\":\"/report/receiveReport!listPurRecvDetail.action\"},{\"name\":\"销售发货汇总\",\"path\":\"/report/deliveryReport!listSalesDelySummary.action\"},{\"name\":\"销售发货明细\",\"path\":\"/report/deliveryReport!listSalesDelyDetail.action\"}],\"name\":\"报表管理\",\"path\":\"0\"},{\"child\":[{\"name\":\"仓库资料\",\"path\":\"/basis/warehouse.action\"},{\"name\":\"供应商资料\",\"path\":\"/basis/supplier.action\"},{\"name\":\"物流资料\",\"path\":\"/basis/shipping.action\"},{\"name\":\"购物清单模板\",\"path\":\"/basis/template!inputShoppingListTemplate.action\"}],\"name\":\"基础资料\",\"path\":\"0\"},{\"child\":[{\"name\":\"销售订单\",\"path\":\"/stock/salesOrder.action\"}],\"name\":\"订单管理\",\"path\":\"0\"}]}";
			logger.info(jsonStr);
			JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
			SsoGetMenuResult result = jsonUtils.fromJson(jsonStr, SsoGetMenuResult.class);
			return result;
		} finally {
			// 保证InputStream的关闭.
			IOUtils.closeQuietly(input);
		}
	}
	
	/**
	 * Ticket校验接口(http://passport.cm.com/iuc/cas/validate)
	 * @param ticket
	 * @return
	 * @throws IOException
	 */
	public SsoValidateTicketResult validateTicket(String ticket) throws IOException{
		// 获取内容
		HttpEntity entity = null;
		String url = WmsConstants.UUC_URL + WmsConstants.UUC_AUTHC_VALIDATE_URI;
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.addHeader("Content-Type", "application/json");
	
		//填充IUC参数并转换为JSON串
		String jsonString = fillJsonParamsWithTicket(ticket);
		
		StringEntity params = new StringEntity(jsonString, "UTF-8");
		httpPost.setEntity(params);

		try {
			HttpContext context = new BasicHttpContext();
			HttpResponse remoteResponse = httpClient.execute(httpPost, context);
			entity = remoteResponse.getEntity();
			remoteResponse.getStatusLine().getStatusCode();
		} catch (Exception e) {
			logger.error("SsoValidateTicketResult error,url=" + url, e);
			httpPost.abort();
			return null;
		}
		if (entity == null) {
			throw new RuntimeException("SsoValidateTicketResult From IUC is null.");
		}

		// 输出内容
		InputStream input = entity.getContent();
		try {
			String jsonStr = IOUtils.toString(input,"UTF-8");
//			String jsonStr = "{\"code\":\"0\",\"tgt\":\"TGT-160-pbgOFYUn1wnMkMuaUtIcZmWW6dAOTnxSXqPaJRcIoA1ARK6mCA-sso\"}";
			JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
			SsoValidateTicketResult result = jsonUtils.fromJson(jsonStr, SsoValidateTicketResult.class);
			return result;
		} finally {
			// 保证InputStream的关闭.
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * 判定用户是否登陆接口(http://passport.cm.com/iuc/cas/validlogin)
	 * @param ssoTgt
	 * @return
	 * @throws IOException
	 */
	public SsoLoginCheckResult checkLogin(String ssoTgt) throws IOException {
		// 获取内容
		HttpEntity entity = null;
		String url = WmsConstants.UUC_URL + WmsConstants.UUC_AUTHZ_CHECK_URI;
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.addHeader("Content-Type", "application/json");
		
		//填充IUC参数并转换为JSON串
		String jsonString = fillJsonParamsWithTgt(ssoTgt);	
		StringEntity params = new StringEntity(jsonString, "UTF-8");
		httpPost.setEntity(params);

		try {
			HttpContext context = new BasicHttpContext();
			HttpResponse remoteResponse = httpClient.execute(httpPost, context);
			entity = remoteResponse.getEntity();
		} catch (Exception e) {
			logger.error("SsoCheckLoginResult error,url=" + url, e);
			httpPost.abort();
			return null;
		}
		if (entity == null) {
			throw new RuntimeException("SsoCheckLoginResult is null.");
		}

		// 输出内容
		InputStream input = entity.getContent();
		try {
			String jsonStr = IOUtils.toString(input,"UTF-8");
//			String jsonStr = "{\"code\":\"0\",\"id\":\"1\",\"permission\":[\"stockCheck:edit\",\"basisManage:edit\",\"shipping:save\",\"stockManage:save\",\"stockOutManage:delete\",\"stockInManage:view\",\"attrSet:save\",\"out:editSalesOutIndiv\",\"listSummary:view\",\"wms:save\",\"shipping:edit\",\"stock:save\",\"stockCheck:save\",\"salesOrder:edit\",\"shoppingListTemplate:save\",\"out:confirmSalesOut\",\"stockChange:edit\",\"out:editSalesOutInvoice\",\"waresManage:delete\",\"stock:view\",\"stockOutManage:edit\",\"attrSet:view\",\"stock:delete\",\"salesOutReport!listDetail:delete\",\"warehouse:edit\",\"stockManage:edit\",\"inputRmaIn:view\",\"inputPurchaseIn:save\",\"stockManage:view\",\"category:delete\",\"stockInManage:delete\",\"category:save\",\"warehouse:delete\",\"waresManage:edit\",\"wares:edit\",\"reportManage:view\",\"warehouse:view\",\"stockChange:view\",\"inputRmaIn:delete\",\"stockOutManage:save\",\"shoppingListTemplate:edit\",\"salesOutReport!listDetail:save\",\"stock:edit\",\"salesOrder:view\",\"salesOutSummaryList:edit\",\"wms:edit\",\"salesOutReport!listDetail:view\",\"out:addSalesOut\",\"sku:view\",\"supplier:save\",\"sku:save\",\"salesOutSummaryList:save\",\"waresManage:save\",\"supplier:view\",\"stockInManage:edit\",\"supplier:edit\",\"inputPurchaseIn:edit\",\"stockIn:view\",\"out:delete\",\"stockIn:edit\",\"out:deleteSalesOut\",\"wms:delete\",\"shipping:view\",\"inputRmaIn:edit\",\"listDetail:save\",\"salesOrder:delete\",\"stockInManage:save\",\"listSummary:edit\",\"stockCheck:view\",\"out:edit\",\"out:save\",\"basisManage:save\",\"attrSet:edit\",\"listSummary:save\",\"stockIn:save\",\"indiv:save\",\"category:edit\",\"listDetail:delete\",\"stockChange:delete\",\"stockChange:save\",\"attrSet:delete\",\"salesOutReport!listDetail:edit\",\"warehouse:save\",\"indiv:delete\",\"shoppingListTemplate:view\",\"shipping:delete\",\"wms:view\",\"sku:edit\",\"indiv:view\",\"out:view\",\"listSummary:delete\",\"inputPurchaseIn:view\",\"stockOutManage:view\",\"basisManage:view\",\"inputRmaIn:save\",\"basisManage:delete\",\"indiv:edit\",\"reportManage:edit\",\"wares:save\",\"reportManage:delete\",\"out:printSalesOutSummary\",\"wares:view\",\"stockManage:delete\",\"stockCheck:delete\",\"inputPurchaseIn:delete\",\"wares:delete\",\"stockIn:delete\",\"sku:delete\",\"category:view\",\"supplier:delete\",\"salesOrder:save\",\"listDetail:view\",\"listDetail:edit\",\"out:editSalesOutShipping\",\"shoppingListTemplate:delete\",\"waresManage:view\",\"reportManage:save\",\"salesOutSummaryList:view\",\"salesOutSummaryList:delete\"],\"type\":\"super\",\"username\":\"admin\"}";
			JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
			SsoLoginCheckResult checkResponse = jsonUtils.fromJson(jsonStr, SsoLoginCheckResult.class);
			return checkResponse;
		} finally {
			// 保证InputStream的关闭.
			IOUtils.closeQuietly(input);
		}
	}

	
	/**
	 * 封装IUC参数(ticket)
	 * @param ticket
	 * @return
	 */
	public String fillJsonParamsWithTicket(String ticket){

		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		// Body信息
		Map<String, String> bodyMap = Maps.newHashMap();
		bodyMap.put("ticket",ticket);
		// Head信息
		Map<String, String> headMap = Maps.newHashMap();
		headMap.put("appid", WmsConstants.UUC_APP_WMS_ID);
		String timeStr = String.valueOf(System.currentTimeMillis());
		headMap.put("ttl", timeStr);
		headMap.put("validcode",DigestUtils.md5Hex(jsonUtils.toJson(bodyMap)+ WmsConstants.UUC_APP_WMS_KEY + timeStr));
		// 整个内容信息
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("body", bodyMap);
		contentMap.put("head", headMap);
		String jsonString = jsonUtils.toJson(contentMap);
		logger.info(jsonString);
		return jsonString;
	
	}
	
	/**
	 * 封装IUC参数(ssoTgt)
	 * @param ssoTgt
	 * @return
	 */
	public String fillJsonParamsWithTgt(String ssoTgt) {
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		// Body信息
		Map<String, String> bodyMap = Maps.newHashMap();
		bodyMap.put("tgt", ssoTgt);

		// Head信息
		Map<String, String> headMap = Maps.newHashMap();
		headMap.put("appid", WmsConstants.UUC_APP_WMS_ID);
		String timeStr = String.valueOf(System.currentTimeMillis());
		headMap.put("ttl", timeStr);
		headMap.put("validcode",DigestUtils.md5Hex(jsonUtils.toJson(bodyMap)+ WmsConstants.UUC_APP_WMS_KEY + timeStr));
		// 整个内容信息
		Map<String, Object> contentMap = Maps.newHashMap();
		contentMap.put("body", bodyMap);
		contentMap.put("head", headMap);
		String jsonString = jsonUtils.toJson(contentMap);
		logger.info(jsonString);
		return jsonString;
	}
	
	public static void main(String[] args) throws IOException {
		SsoClient client = new SsoClient();
		client.init();
		client.checkLogin("TGT-1-RgUbodDbgr9lOQHOm0OojGuuhfBNbsQBNq46eY6bNDiWibfxMO-sso");
		client.destroy();
	}
	
	
	


}
