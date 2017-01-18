package com.gionee.wms.web.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: HttpUtil
 * @Description: HTTP 工具类(基于HttpClient)
 * @author Kevin
 * @date 2013-7-6 上午09:36:49
 * 
 */
@Component("httpUtil")
public class HttpUtil {
	private static Log logger = LogFactory.getLog(HttpUtil.class);
	private static final String JSON_TYPE = "application/json";
	private static final String DEFAULT_ENCODING = "UTF-8";
//	private static final int CONNECTION_POOL_SIZE = 10;
	private static final int CONNECTION_POOL_SIZE = 1;
	private static final int TIMEOUT_SECONDS = 5;
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
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_SECONDS *1000);
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

	public String doGet(String url) throws IOException {
		String returnMsg = "";
		HttpGet httpget = new HttpGet(url);
		logger.info("get: " + httpget.getURI());
		HttpResponse response = httpClient.execute(httpget);
		logger.debug("status: " + response.getStatusLine());
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				returnMsg = EntityUtils.toString(entity);
				entity.consumeContent();
			}
		} else {
			throw new RuntimeException(response.getStatusLine().toString());
		}
		// System.out.println(html);
		return returnMsg;
	}

	public String testGet(String url) throws IOException {
		String returnMsg = "";
		HttpGet httpget = new HttpGet(url);
		logger.debug("get: " + httpget.getURI());
		HttpResponse response = httpClient.execute(httpget);
		logger.debug("status: " + response.getStatusLine());
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				returnMsg = EntityUtils.toString(entity);
				entity.consumeContent();
			}
		} else {
			throw new RuntimeException(response.getStatusLine().toString());
		}
		return returnMsg;
	}

	public String doPost(String url, Map<String, String> parameters) throws IOException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (parameters != null && !parameters.isEmpty()) {
			Iterator<Entry<String, String>> itr = parameters.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) itr.next();
				NameValuePair nvp = new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue());
				nvps.add(nvp);
			}
		}
		HttpPost httpPost = new HttpPost(url);
		StringEntity params = new UrlEncodedFormEntity(nvps, DEFAULT_ENCODING);	
		httpPost.setEntity(params);
		logger.info("post: " + httpPost.getURI());
		logger.info(nvps.toString());
		HttpEntity entity = null;
		try {
			HttpContext context = new BasicHttpContext();
			HttpResponse remoteResponse = httpClient.execute(httpPost, context);
			logger.info(remoteResponse.getStatusLine().toString());
			entity = remoteResponse.getEntity();
		} catch (NoSuchElementException e){
			logger.error("java.util.NoSuchElementException\r\nfetch remote content" + url + "  error");
			httpPost.abort();
			return null;			
		} catch (Exception e) {
			logger.error("fetch remote content" + url + "  error", e);
			httpPost.abort();
			return null;
		}

		// 404错误
		if (entity == null) {
			throw new RuntimeException(url + " is not found");
		}

		InputStream input = entity.getContent();
		try {
			return IOUtils.toString(input, DEFAULT_ENCODING);
		} finally {
			// 保证InputStream的关闭.
			IOUtils.closeQuietly(input);
		}
	}

	public String doPost2(String url, Map<String, Object> parameters) throws IOException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (parameters != null && !parameters.isEmpty()) {
			Iterator<Entry<String, Object>> itr = parameters.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue()+"");
				nvps.add(nvp);
			}
		}
		HttpPost httpPost = new HttpPost(url);
		StringEntity params = new UrlEncodedFormEntity(nvps, DEFAULT_ENCODING);	
		httpPost.setEntity(params);
		logger.info("post: " + httpPost.getURI());
		logger.info(nvps.toString());
		HttpEntity entity = null;
		try {
			HttpContext context = new BasicHttpContext();
			HttpResponse remoteResponse = httpClient.execute(httpPost, context);
			logger.info(remoteResponse.getStatusLine().toString());
			entity = remoteResponse.getEntity();
		} catch (Exception e) {
			logger.error("fetch remote content" + url + "  error", e);
			httpPost.abort();
			return null;
		}

		// 404错误
		if (entity == null) {
			throw new RuntimeException(url + " is not found");
		}

		InputStream input = entity.getContent();
		try {
			return IOUtils.toString(input, DEFAULT_ENCODING);
		} finally {
			// 保证InputStream的关闭.
			IOUtils.closeQuietly(input);
		}
	}
	public String testPost(String url) throws IOException {
		String returnMsg = "";
		HttpPost httpost = new HttpPost(url);
		logger.debug("post: " + httpost.getURI());
		HttpResponse response = httpClient.execute(httpost);
		logger.debug("status: " + response.getStatusLine());
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				returnMsg = EntityUtils.toString(entity);
				entity.consumeContent();
			}
		} else {
			throw new RuntimeException(response.getStatusLine().toString());
		}
		return returnMsg;
	}

	public String doPostJson(String url, String jsonData) throws IOException {
		StringEntity params = new StringEntity(jsonData, DEFAULT_ENCODING);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", JSON_TYPE);
		httpPost.setEntity(params);

		logger.info("post: " + httpPost.getURI());
		logger.info(jsonData);

		HttpEntity entity = null;
		try {
			HttpContext context = new BasicHttpContext();
			HttpResponse remoteResponse = httpClient.execute(httpPost, context);
			logger.info(remoteResponse.getStatusLine().toString());
			entity = remoteResponse.getEntity();
		} catch (Exception e) {
			logger.error("fetch remote content" + url + "  error", e);
			httpPost.abort();
			return null;
		}

		// 404错误
		if (entity == null) {
			throw new RuntimeException(url + " is not found");
		}

		InputStream input = entity.getContent();
		try {
			return IOUtils.toString(input, DEFAULT_ENCODING);
		} finally {
			// 保证InputStream的关闭.
			IOUtils.closeQuietly(input);
		}
	}

	public void close() {
		httpClient.getConnectionManager().shutdown();
	}

	public static void main(String[] args) {
		long t = System.currentTimeMillis();
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.init();
		try {
			// System.out.println(httpUtil.doGet("http://gamesvr.keno8.com:3334/cusinfo2.xml?key=2ce5336cd3d3c1bf4009f75e2f20562&loginname=gtest"));
			// Map map = new HashMap();
			// map.put("key", "2ce5336cd3d3c1bf4009f75e2f20562");
			// map.put("loginname", "gtest");
			// System.out.println(httpUtil.doPost("http://www.google.com/",map));
			JSONObject jsonObj = new JSONObject();
			// jsonObj.put("body", new JSONObject().put("tgt",
			// "TGT-89-1LQEcgx90JzE3ubNW2LXme5iib5tNCgGeaCj7YgtonAwiqDrUh-sso"));
			// JSONObject head = new JSONObject();
			// head.put("validcode", "b61c1284660f3cc87602e9ee07c9801b");
			// head.put("appid", "9F8ED8DE05FD4B22AC6DE76C6FBB1149");
			// head.put("ttl", "1373074250581");
			// jsonObj.put("head", head);
			// String jsonData =
			// "{"body":{"tgt":"TGT-89-1LQEcgx90JzE3ubNW2LXme5iib5tNCgGeaCj7YgtonAwiqDrUh-sso"},"head":{"validcode":"b61c1284660f3cc87602e9ee07c9801b","appid":"9F8ED8DE05FD4B22AC6DE76C6FBB1149","ttl":"1373074250581"}}";
			jsonObj.put("warehouseCode", "001");
			jsonObj.put("skuCode", "1");
			System.out.println(httpUtil.doPostJson("http://127.0.0.1:8080/wms/api/queryStock.action", jsonObj
					.toString()));
			;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpUtil.destroy();
			System.out.println(System.currentTimeMillis() - t);
		}
	}

}
