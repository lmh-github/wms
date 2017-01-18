package com.gionee.wms.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.internal.stream.connect.ConnectionLifeCycleListener;

public class ConnectionLifeCycleListenerImpl implements
		ConnectionLifeCycleListener {
	private static Logger logger = LoggerFactory.getLogger(ConnectionLifeCycleListenerImpl.class);
	
	@Override
	public void onBeforeConnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(Throwable arg0) {
		// TODO Auto-generated method stub
		logger.error("TOP connection error onException", arg0);
	}

	@Override
	public void onMaxReadTimeoutException() {
		// TODO Auto-generated method stub
		logger.error("TOP connection error onMaxReadTimeoutException");
	}

}
