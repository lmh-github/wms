package com.gionee.wms.job;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.listener.ConnectionLifeCycleListenerImpl;
import com.gionee.wms.listener.TopCometMessageListenerImpl;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.internal.stream.Configuration;
import com.taobao.api.internal.stream.TopCometStream;
import com.taobao.api.internal.stream.TopCometStreamFactory;
import com.taobao.api.request.IncrementCustomerPermitRequest;

/**
 * 
 * 描述: 淘宝订单同步任务
 * 作者: milton.zhang
 * 日期: 2013-9-24
 */
public class SyncTaobaoOrderTask {
	private static Logger logger = LoggerFactory
			.getLogger(SyncTaobaoOrderTask.class);

	/**
	 * 同步策略：
	 * 每天定时调用一次淘宝主动通知接口
	 * TOP（淘宝开放平台）会发送心跳包保持连接，连接保持24小时
	 * TOP主动通知数据到WMS
	 * WMS监听响应并入库订单数据（尽量不阻塞请求）
	 */
	public void execute() {
		if (WmsConstants.TAOBAO_FLAG) {
			try {
				logger.info("淘宝订单同步服务正在启动...");
				//授权获取指定用户的消息
				DefaultTaobaoClient client = new DefaultTaobaoClient(
						WmsConstants.TAOBAO_TOP_URL,
						WmsConstants.TAOBAO_APPKEY, WmsConstants.TAOBAO_SECRET,
						"json");
				IncrementCustomerPermitRequest req = new IncrementCustomerPermitRequest();
				//表示与topics相对应的消息状态，all表示开通应用订阅的所有的消息
				req.setStatus("all;all;all");
				//消息主题，必须是订阅的，分别表示：交易通知，获取退款变更通知，获取商品变更通知
				req.setTopics("trade;refund;item");
				//设置用户需要开通的功能，分别表示：增量api取消息，主动发送消息和同步数据功能
				req.setType("get,syn,notify");
				TaobaoResponse res = client
						.execute(req,
								"6100215067502f589802518177146c661821b91f0fbe0cc3605357540");
				JSONObject jsonObj = JSONObject.fromObject(res.getBody());
				String status = jsonObj
						.getJSONObject("increment_customer_permit_response")
						.getJSONObject("app_customer").getString("status");
				if (!status.equals("valid_session")) {
					//重新获取或刷新令牌

				}

				//连接淘宝主动通知服务
				Configuration conf = new Configuration(
						WmsConstants.TAOBAO_APPKEY, WmsConstants.TAOBAO_SECRET,
						null);
				conf.setConnectUrl(WmsConstants.TAOBAO_NOTIFY_URL);
				TopCometStream stream = new TopCometStreamFactory(conf)
						.getInstance();
				stream.setConnectionListener(new ConnectionLifeCycleListenerImpl());
				stream.setMessageListener(new TopCometMessageListenerImpl());
				stream.start();
				logger.info("淘宝订单同步服务启动成功");
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("淘宝订单同步服务启动异常", e);
			}
		}
	}

	public static void main(String[] args) {
		try {
//			String appKey = "1021631229";
//			String appSecret = "sandbox474e3e078894852e51494814d";
//			String sessionKey = "6101925111643fc587d7353d7aa698d71182f6aa8e4ec4e3605357540";
//			String serverUrl = "http://gw.api.tbsandbox.com/router/rest";
//			String cometUrl = "http://stream.api.tbsandbox.com/stream";
			
			String appKey = "21642154";
			String appSecret = "65dd78fa5f53758ccb50ad98457bbf3b";
			String sessionKey = "6100d178488f3c3be70e3e8c813605dde2de82437e511e2720912157";
			
			String serverUrl = "http://gw.api.taobao.com/router/rest";
			String cometUrl = "http://stream.api.taobao.com/stream";
			
			Configuration conf = new Configuration(appKey,
					appSecret, null);
			conf.setConnectUrl(cometUrl);
			TopCometStream stream = new TopCometStreamFactory(conf)
					.getInstance();
			stream.setConnectionListener(new ConnectionLifeCycleListenerImpl());
			stream.setMessageListener(new TopCometMessageListenerImpl());
			stream.start();

			DefaultTaobaoClient client = new DefaultTaobaoClient(serverUrl,
					appKey, appSecret, "json");
			IncrementCustomerPermitRequest req = new IncrementCustomerPermitRequest();
			//表示与topics相对应的消息状态，all表示开通应用订阅的所有的消息
			req.setStatus("all;all;all");
			//消息主题，必须是订阅的，分别表示：交易通知，获取退款变更通知，获取商品变更通知
			req.setTopics("trade;refund;item");
			//设置用户需要开通的功能，分别表示：增量api取消息，主动发送消息和同步数据功能
			req.setType("get,syn,notify");
			TaobaoResponse res = client
					.execute(req, sessionKey);
			JSONObject jsonObj = JSONObject.fromObject(res.getBody());
			String status = jsonObj
					.getJSONObject("increment_customer_permit_response")
					.getJSONObject("app_customer").getString("status");
			System.out.println(status);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
