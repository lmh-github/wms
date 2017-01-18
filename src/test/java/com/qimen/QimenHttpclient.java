package com.qimen;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class QimenHttpclient {
	public static String send(String url, String body) throws Exception {
        
		
		HttpClient client = new DefaultHttpClient();
		
        HttpPost post = new HttpPost(url);
        
        HttpEntity entity = new StringEntity(body, "utf-8");
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            String resEntityStr = EntityUtils.toString(response.getEntity());
            return new String(resEntityStr.getBytes("iso-8859-1"), "utf-8");
        }
        else if(response.getStatusLine().getStatusCode() == 404){
            throw new Exception("报错~~");
        }
        else {
            throw new Exception();
        }

    }

}