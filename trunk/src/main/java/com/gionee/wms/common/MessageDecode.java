package com.gionee.wms.common;

import java.util.Map;

import com.taobao.api.ApiException;
import com.taobao.api.domain.NotifyItem;
import com.taobao.api.domain.NotifyRefund;
import com.taobao.api.domain.NotifyTrade;
import com.taobao.api.internal.parser.json.JsonConverter;
import com.taobao.api.internal.util.json.JSONValidatingReader;

/**
 * 解析主动通知的消息，解析成SDK中自带的NotifyItem,NotifyRefund,NotifyTrade
 * 
 * @author zhenzi 2012-7-12 14:37:33
 */
public class MessageDecode {

	public static final String NOTIFY_TRADE = "notify_trade";
	public static final String NOTIFY_ITEM = "notify_item";
	public static final String NOTIFY_REFUND = "notify_refund";

	/**
	 * 这里只针对业务消息做decode，这里业务消息有item,refund,trade
	 */
	public static Object decodeMsg(String msg) throws ApiException {
		JSONValidatingReader reader = new JSONValidatingReader();
		Object rootObj = reader.read(msg);
		if (rootObj instanceof Map<?, ?>) {
			Map<?, ?> rootJson = (Map<?, ?>) rootObj;
			Class<?> clazz = null;
			if (rootJson.containsKey(NOTIFY_ITEM)) {
				clazz = NotifyItem.class;
			} else if (rootJson.containsKey(NOTIFY_TRADE)) {
				clazz = NotifyTrade.class;
			} else if (rootJson.containsKey(NOTIFY_REFUND)) {
				clazz = NotifyRefund.class;
			}

			if (clazz != null && !rootJson.isEmpty()) {
				rootJson.values().iterator().next();
				Map<?, ?> rspJson = (Map<?, ?>) rootJson.values().iterator().next();
				JsonConverter jc = new JsonConverter();
				return jc.fromJson(rspJson, clazz);
			}
		}
		return null;
	}
}
