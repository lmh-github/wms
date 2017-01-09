package com.gionee.top.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 作者:milton.zhang
 * 时间:2013-11-30
 * 描述:系统配置
 */
public class SystemConfig {
	public static final String TOP_AUTH_URL = System.getProperty("top.auth.url", "https://oauth.taobao.com/authorize");//授权地址
	public static final String TOP_AUTH_CALLBACK_URL = System.getProperty("top.auth.callback.url", "http://121.196.132.61:30001/orderSyncServer/auth/getToken");//授权回调地址
	public static final String TOP_AUTH_TOKEN_URL = System.getProperty("top.auth.token.url", "https://oauth.taobao.com/token");//访问令牌获取地址
	public static final String TOP_URL = System.getProperty("top.url", "http://gw.api.taobao.com/router/rest");//淘宝开发平台接口地址
	public static final String AOP_URL = System.getProperty("aop.url", "https://eco.taobao.com/router/rest");//支付宝开发平台接口地址
	public static final int JDBC_QUERYTIMEOUT = Integer.valueOf(System.getProperty("jdbc.query.timeout", "60"));//jdbc查询超时时间
	public static final int SYNC_RATE = Integer.valueOf(System.getProperty("sync.rate", "120"));//订单同步频率
	public static final String WMS_ORDER_SYNC_URL = System.getProperty("wms.order.sync.url", "http://183.233.193.196:8300/wms_ec/api/syncOrder.action");//wms订单同步接口地址
	public static final String APPKEY = System.getProperty("appkey", "21796054");//appkey
	public static final String APPSECRET = System.getProperty("appsecret", "e1aa2b4a59c76dc461444d6ee535f8ee");//appsecret
	public static String ACCESS_TOKEN = "62000038e65ZZf9e0860bb8281891c8d451cab2d58bffc5720912157";//访问令牌
	
	public static final String TOP_SHIELD_TRADE_FROM = System.getProperty("top.shield.trade.from", "NONE");//需要屏蔽的交易来源，默认不屏蔽
	
	public static final String SYNC_ORDER_SALT = "#GFDK435&LKDD!"; // 作用于订单同步签名盐值
	
	public static Map<String, String> SHIPPING_CODE_MAP = new HashMap<String, String>();//WMS快递公司编码与淘宝快递公司编码转换
	static{
		SHIPPING_CODE_MAP.put("sf_express", "SF");
		SHIPPING_CODE_MAP.put("ems", "EMS");
		SHIPPING_CODE_MAP.put("yto", "YTO");
	}
	
	/**
	 * 销售订单来源
	 */
	public static enum OrderSource {
		OFFICIAL_GIONEE("1","金立官网"),
		TMALL_GIONEE("2","金立天猫"),
		OFFICIAL_IUNI("3","IUNI官网"),
		TMALL_IUNI("4","IUNI天猫"),
		VIP_GIONEE("5","金立唯品"),
		VIP_IUNI("6","IUNI唯品"),
		TAOBAO_FX_GIONEE("7","金立淘宝分销"),
		TAOBAO_FX_IUNI("8","金立淘宝分销");

		private final String code;
		private final String name;

		OrderSource(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}
}
