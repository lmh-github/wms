package com.gionee.wms.listener;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.wms.common.ThreadUtils;
import com.gionee.wms.job.SyncTaobaoOrderThread;
import com.taobao.api.internal.stream.message.TopCometMessageListener;

public class TopCometMessageListenerImpl implements TopCometMessageListener {
	private static Logger logger = LoggerFactory.getLogger(TopCometMessageListenerImpl.class);
	
	@Override
	public void onConnectMsg(String paramString) {
		// TODO Auto-generated method stub
		System.out.println("connecting onConnectMsg:  " + paramString);
		logger.info("onConnectMsg--" + paramString);
	}

	@Override
	public void onHeartBeat() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveMsg(String paramString) {
		// TODO Auto-generated method stub
		System.out.println("connecting onReceiveMsg:  " + paramString); 
		logger.info("onReceiveMsg--" + paramString);
		//交给线程池处理
		ExecutorService pool = ThreadUtils.getThreadPool();
		SyncTaobaoOrderThread syncThread = new SyncTaobaoOrderThread(paramString);
		pool.execute(syncThread);
	}

	@Override
	public void onDiscardMsg(String paramString) {
		// TODO Auto-generated method stub
		System.out.println("connecting onDiscardMsg:  " + paramString);
		logger.info("onDiscardMsg--" + paramString);
	}

	@Override
	public void onServerUpgrade(String paramString) {
		// TODO Auto-generated method stub
		System.out.println("connecting onServerUpgrade:  " + paramString);
	}

	@Override
	public void onServerRehash() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onServerKickOff() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientKickOff() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOtherMsg(String paramString) {
		// TODO Auto-generated method stub
		System.out.println("connecting onOtherMsg:  " + paramString);
		logger.info("onOtherMsg--" + paramString);
	}

	@Override
	public void onException(Exception paramException) {
		// TODO Auto-generated method stub
		logger.error("onException", paramException);
	}
	
	public static void main(String[] args) {
		TopCometMessageListenerImpl top = new TopCometMessageListenerImpl();
		String test = "{\"notify_trade\":{\"topic\":\"trade\",\"status\":\"TradeCreate\",\"user_id\":720912157,\"nick\":\"金立手机旗舰店\",\"modified\":\"2013-10-09 11:41:08\",\"buyer_nick\":\"zhaobing315\",\"payment\":\"1699.00\",\"oid\":436445011972160,\"is_3D\":true,\"tid\":436445011972160,\"type\":\"guarantee_trade\",\"seller_nick\":\"金立手机旗舰店\"}}";
		top.onReceiveMsg(test);
	}

}
