/**
* @author jay_liang
* @date 2014-6-18 下午3:26:47
*/
package com.gionee.top.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Before;
import org.junit.Test;

import com.gionee.top.config.SystemConfig;
import com.gionee.top.util.DateConvert;
import com.gionee.top.util.HttpUtil;
import com.gionee.top.util.JsonUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.domain.TradeRecord;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.request.AlipayUserTradeSearchRequest;
import com.taobao.api.response.AlipayUserTradeSearchResponse;
import com.taobao.api.response.TradeFullinfoGetResponse;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-6-18 下午3:26:47
 * @=======================================
 */
public class AlipayTest {

	private HttpUtil httpUtil;
	JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	
	@Before
	public void init(){
		httpUtil = new HttpUtil();
		httpUtil.init();
	}

	@Test
	public void testAliInfo() throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(
				SystemConfig.AOP_URL, SystemConfig.APPKEY,
				SystemConfig.APPSECRET);
		AlipayUserTradeSearchRequest req=new AlipayUserTradeSearchRequest();
		Date nowTime = new Date();
		req.setEndTime(DateConvert.convertD2String(nowTime, "yyyy-MM-dd")+" 23:59:59");//开始时间，时间必须是今天范围之内。格式为yyyy-MM-dd HH:mm:ss，精确到秒
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowTime);
		calendar.add(Calendar.DATE, -6);
		Date startTime = calendar.getTime();
		req.setStartTime(DateConvert.convertD2String(startTime, "yyyy-MM-dd") + " 00:00:00");//	 结束时间。与开始时间间隔在七天之内
		req.setAlipayOrderNo("2014052411001001290076688652");//支付宝订单号，为空查询所有记录
//		req.setMerchantOrderNo(orderInfo.getOrderCode());//商户订单号，为空查询所有记录
		req.setOrderType(null);//订单类型，为空查询所有类型订单。
		req.setOrderStatus(null);//订单状态，为空查询所有状态订单
		req.setOrderFrom(null);//订单来源，为空查询所有来源。淘宝(TAOBAO)，支付宝(ALIPAY)，其它(OTHER)
		req.setPageNo("1");
		req.setPageSize("1");
		AlipayUserTradeSearchResponse response = client.execute(req , SystemConfig.ACCESS_TOKEN);
		System.out.println("AlipayUserTradeSearchResponse--" + jsonUtils.toJson(response));
		List<TradeRecord> tradeRecords = response.getTradeRecords();
		if(tradeRecords != null && tradeRecords.size() > 0){
			TradeRecord record = tradeRecords.get(0);
			System.out.println("TradeRecord--" + jsonUtils.toJson(record));
			System.out.println(new BigDecimal(record.getTotalAmount()));// 发票金额
		}
	}
	
	@Test
	public void testTaobaoUtil() throws ApiException {
		String tradeInfoStr="{\"trade_fullinfo_get_response\":{\"trade\":{\"adjust_fee\":\"0.00\",\"alipay_id\":2088202473139191,\"alipay_no\":\"2014070111001001190018872497\",\"available_confirm_fee\":\"182.00\",\"buyer_alipay_no\":\"79168481@qq.com\",\"buyer_area\":\"广西南宁电信\",\"buyer_cod_fee\":\"0.00\",\"buyer_email\":\"79168481@qq.com\",\"buyer_nick\":\"zyz薹薹s\",\"buyer_obtain_point_fee\":0,\"buyer_rate\":false,\"cod_fee\":\"0.00\",\"cod_status\":\"NEW_CREATED\",\"commission_fee\":\"0.00\",\"created\":\"2014-07-01 15:17:15\",\"credit_card_fee\":\"181.62\",\"discount_fee\":\"0.00\",\"has_post_fee\":true,\"is_3D\":false,\"is_brand_sale\":false,\"is_force_wlb\":false,\"is_lgtype\":false,\"is_part_consign\":false,\"is_wt\":false,\"modified\":\"2014-07-01 15:20:43\",\"orders\":{\"order\":[{\"adjust_fee\":\"0.00\",\"buyer_rate\":false,\"cid\":50024110,\"discount_fee\":\"0.00\",\"divide_order_fee\":\"69.00\",\"is_daixiao\":false,\"is_oversold\":false,\"num\":1,\"num_iid\":39093741460,\"oid\":715412746589495,\"order_from\":\"WAP\",\"outer_iid\":\"1006\",\"part_mjz_discount\":\"0.00\",\"payment\":\"69.00\",\"pic_path\":\"http://img02.taobaocdn.com/bao/uploaded/i2/T1k_IjFThcXXXXXXXX_!!0-item_pic.jpg\",\"price\":\"69.00\",\"refund_status\":\"NO_REFUND\",\"seller_rate\":false,\"seller_type\":\"B\",\"snapshot_url\":\"f:715412746589495_1\",\"status\":\"WAIT_SELLER_SEND_GOODS\",\"title\":\"IUNI/艾优尼 U2 手机镜头 银色 广角微距鱼眼多功能镜头\",\"total_fee\":\"69.00\"},{\"adjust_fee\":\"0.00\",\"buyer_rate\":false,\"cid\":50012587,\"discount_fee\":\"0.00\",\"divide_order_fee\":\"25.00\",\"is_daixiao\":false,\"is_oversold\":false,\"num\":1,\"num_iid\":39103424038,\"oid\":715412746599495,\"order_from\":\"WAP\",\"outer_iid\":\"1008\",\"outer_sku_id\":\"1008\",\"part_mjz_discount\":\"0.00\",\"payment\":\"25.00\",\"pic_path\":\"http://img01.taobaocdn.com/bao/uploaded/i1/T12JnTFpVkXXXXXXXX_!!0-item_pic.jpg\",\"price\":\"25.00\",\"refund_status\":\"NO_REFUND\",\"seller_rate\":false,\"seller_type\":\"B\",\"sku_id\":\"50964265850\",\"sku_properties_name\":\"颜色分类:透明\",\"snapshot_url\":\"f:715412746599495_1\",\"status\":\"WAIT_SELLER_SEND_GOODS\",\"title\":\"IUNI/艾优尼 U2  高透明贴膜 离型PET材质 高清防指纹贴膜\",\"total_fee\":\"25.00\"},{\"adjust_fee\":\"0.00\",\"buyer_rate\":false,\"cid\":50050366,\"discount_fee\":\"0.00\",\"divide_order_fee\":\"19.00\",\"is_daixiao\":false,\"is_oversold\":false,\"num\":1,\"num_iid\":39068975897,\"oid\":715412746609495,\"order_from\":\"WAP\",\"outer_iid\":\"1011\",\"part_mjz_discount\":\"0.00\",\"payment\":\"19.00\",\"pic_path\":\"http://img01.taobaocdn.com/bao/uploaded/i1/T1YHMnFP8dXXXXXXXX_!!0-item_pic.jpg\",\"price\":\"19.00\",\"refund_status\":\"NO_REFUND\",\"seller_rate\":false,\"seller_type\":\"B\",\"snapshot_url\":\"f:715412746609495_1\",\"status\":\"WAIT_SELLER_SEND_GOODS\",\"title\":\"IUNI/艾优尼原装 白色 USB接口 OTG数据线 手机U盘键盘连接线\",\"total_fee\":\"19.00\"},{\"adjust_fee\":\"0.00\",\"buyer_rate\":false,\"cid\":50005266,\"discount_fee\":\"0.00\",\"divide_order_fee\":\"69.00\",\"is_daixiao\":false,\"is_oversold\":false,\"num\":1,\"num_iid\":39102984690,\"oid\":715412746619495,\"order_from\":\"WAP\",\"outer_iid\":\"摇滚动力耳机\",\"outer_sku_id\":\"1007\",\"part_mjz_discount\":\"0.00\",\"payment\":\"69.00\",\"pic_path\":\"http://img04.taobaocdn.com/bao/uploaded/i4/T1H5QBFPXcXXXXXXXX_!!0-item_pic.jpg\",\"price\":\"69.00\",\"refund_status\":\"NO_REFUND\",\"seller_rate\":false,\"seller_type\":\"B\",\"sku_id\":\"68513420910\",\"sku_properties_name\":\"颜色分类:极光绿\",\"snapshot_url\":\"f:715412746619495_1\",\"status\":\"WAIT_SELLER_SEND_GOODS\",\"title\":\"IUNI/艾优尼 摇滚动力 入耳式  通用 手机耳机\",\"total_fee\":\"69.00\"}]},\"pay_time\":\"2014-07-01 15:20:42\",\"payment\":\"182.00\",\"point_fee\":38,\"post_fee\":\"0.00\",\"promotion_details\":{\"promotion_detail\":[{\"discount_fee\":\"0.00\",\"id\":715412746579495,\"promotion_desc\":\"全场满百包邮:省0.00元\",\"promotion_id\":\"mjs-2091235919_137180542\",\"promotion_name\":\"全场满百包邮\"}]},\"real_point_fee\":0,\"received_payment\":\"0.00\",\"receiver_address\":\"民主路6—10号望仙坡小区2—9栋1单元501室\",\"receiver_city\":\"南宁市\",\"receiver_district\":\"兴宁区\",\"receiver_mobile\":\"13877180925\",\"receiver_name\":\"郑雅丹\",\"receiver_phone\":\"\",\"receiver_state\":\"广西壮族自治区\",\"receiver_zip\":\"530023\",\"seller_alipay_no\":\"iunitmall2014@iuni.com\",\"seller_can_rate\":false,\"seller_cod_fee\":\"0.00\",\"seller_email\":\"iunitmall2014@iuni.com\",\"seller_flag\":0,\"seller_mobile\":\"18898583538\",\"seller_name\":\"深圳市艾优尼科技有限公司\",\"seller_nick\":\"iuni官方旗舰店\",\"seller_rate\":false,\"service_tags\":{\"logistics_tag\":[{\"logistic_service_tag_list\":{\"logistic_service_tag\":[{\"service_tag\":\"esTime=2;cutTime=null;lgType=-4\",\"service_type\":\"FAST\"}]},\"order_id\":\"715412746579495\"}]},\"shipping_type\":\"express\",\"snapshot_url\":\"f:715412746579495_1\",\"status\":\"WAIT_SELLER_SEND_GOODS\",\"tid\":715412746579495,\"title\":\"iuni官方旗舰店\",\"total_fee\":\"182.00\",\"trade_from\":\"WAP\",\"type\":\"fixed\"}}}";
		TradeFullinfoGetResponse tradeInfo = TaobaoUtils.parseResponse(tradeInfoStr, TradeFullinfoGetResponse.class);
		Trade trade = tradeInfo.getTrade();
		System.out.println(trade.getAlipayNo());
		String timestamp = String.valueOf(System.currentTimeMillis());
		StringBuilder plainStr = new StringBuilder();
		plainStr.append(trade.getTid()).append(trade.getReceiverName())
				.append(timestamp).append(SystemConfig.SYNC_ORDER_SALT);
		String signature = DigestUtils.md5Hex(plainStr.toString());
		System.out.println(timestamp);
		System.out.println(signature);
	}
}
