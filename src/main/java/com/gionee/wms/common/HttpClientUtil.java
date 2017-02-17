/*
 * @(#)HttpClient.java 2013-7-30
 *
 * Copyright 2013 SZ-Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * HTTP工具类
 * @author ZuoChangjun 2013-7-30
 */
public class HttpClientUtil {

    public static String addUrl(String head, String tail) {
        if (head.endsWith("/")) {
            if (tail.startsWith("/")) {
                return head.substring(0, head.length() - 1) + tail;
            } else {
                return head + tail;
            }
        } else {
            if (tail.startsWith("/")) {
                return head + tail;
            } else {
                return head + "/" + tail;
            }
        }
    }

    /**
     * get方式请求
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String httpGet(String url) throws ClientProtocolException, IOException {
        HttpClient client = HttpClients.createDefault();
        // String
        // url="http://partner.appflood.com/partner_bid_get_token?app_key=XALn1GsPvvvfHfhn&duid=a100001bbc9a0f&d=%7B%22av%22%3A%22%22%2C%22br%22%3A%22%22%2C%22dn%22%3A%22%22%2C%22dp%22%3A%22%22%2C%22lc%22%3A%22%22%2C%22ll%22%3A%22%22%2C%22mc%22%3Afalse%2C%22mf%22%3A%22%22%2C%22pm%22%3A%22%22%2C%22pn%22%3A%22%22%2C%22sm%22%3A%22%22%2C%22sn%22%3A%22%22%2C%22so%22%3A%22%22%2C%22wc%22%3Afalse%2C%22xx%22%3A0%2C%22yy%22%3A0%7D";
        HttpGet get = new HttpGet(url);
        String result = null;
        try {
            HttpResponse res = client.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
                // System.out.println(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            // 关闭连接 ,释放资源
            //client.getConnectionManager().shutdown();
        }
        return result;

    }

    /**
     * post方式请求
     * @param url
     * @param jsonParams
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String httpPost(String url, Map<String, String> params) throws ClientProtocolException, IOException {

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        // 创建名/值组列表
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            parameters.add(new BasicNameValuePair(key, params.get(key)));
        }
        post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        String result = null;
        try {
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
                // System.out.println(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            // 关闭连接 ,释放资源
            // client.getConnectionManager().shutdown();
        }
        return result;
    }
}
