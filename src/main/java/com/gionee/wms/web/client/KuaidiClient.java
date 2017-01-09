package com.gionee.wms.web.client;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.entity.ShippingInfo;
import com.gionee.wms.kuaidi.JacksonHelper;
import com.gionee.wms.kuaidi.pojo.TaskRequest;
import com.gionee.wms.kuaidi.pojo.TaskResponse;

/**
 * @=======================================
 * @Description 快递100通信客户端 
 * @author jay_liang
 * @date 2013-10-9 下午8:36:09
 * @=======================================
 */
@Component("kuaidiClient")
public class KuaidiClient {
	
	private static Logger logger = LoggerFactory.getLogger(KuaidiClient.class);
	
	@Autowired
	private HttpUtil httpUtil;
	
	/**
	 * 订阅快递信息,同时记录于本地
	 * @param order
	 */
	public TaskResponse subscribeKuaidi(ShippingInfo info) {
		TaskRequest req = new TaskRequest();
		req.setCompany(info.getCompany());
		req.setFrom("");
		req.setTo(info.getToAddr());
		req.setNumber(info.getShippingNo());	//快递单号
		req.getParameters().put("callbackurl", WmsConstants.Kuaidi100_CALLBACK_URL);
		req.setKey(WmsConstants.Kuaidi100_SUB_KEY);
		
		HashMap<String, String> p = new HashMap<String, String>(); 
		p.put("schema", "json");
		p.put("param", JacksonHelper.toJSON(req));
		try {
			String ret = httpUtil.doPost(WmsConstants.Kuaidi100_PUSH_URL, p);
			TaskResponse resp = JacksonHelper.fromJSON(ret, TaskResponse.class);
			if(resp.getResult()==true){
				logger.info("订阅成功");
			}else{
				logger.info("订阅失败");
			}
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
