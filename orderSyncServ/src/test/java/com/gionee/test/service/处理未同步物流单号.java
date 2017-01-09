/**
 * File Name:处理物理单号.java
 * Project Name:rds2wms
 * Package Name:com.gionee.test.service
 * Date:2016年12月14日上午10:30:44
 * Copyright (c) 2016 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.test.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gionee.top.config.SystemConfig;
import com.gionee.top.util.JsonUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Shipping;
import com.taobao.api.request.LogisticsOfflineSendRequest;
import com.taobao.api.response.LogisticsOfflineSendResponse;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2016年12月14日 上午10:30:44
 */
public class 处理未同步物流单号 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			JsonUtils jsonUtils = new JsonUtils();

			Map<String, String> map = new HashMap<>();
			map.put("2401480170694230", "590948268983"); // 订单号 : 物流单号
			map.put("2820677916018590", "590948244663");
			map.put("2831065898026370", "590948246362");
			map.put("2832802890961870", "590948268868");
			map.put("2833918494144950", "590948246468");
			map.put("2834404703051480", "590948248051");
			map.put("2834882499901860", "590948235789");
			map.put("2897358606211810", "590948246177");
			map.put("2899818611607110", "590948244927");
			map.put("2899968600465400", "590948270713");
			map.put("2900068215938500", "590948246371");
			map.put("2900940628654220", "590948245191");

			Set<Entry<String, String>> entrySet = map.entrySet();
			for (Entry<String, String> entry : entrySet) {
				TaobaoClient client = new DefaultTaobaoClient(SystemConfig.TOP_URL, "21689850", "9116509adac664b2505c5afdbd653bfc");
				LogisticsOfflineSendRequest req = new LogisticsOfflineSendRequest();
				req.setTid(Long.valueOf(entry.getKey()));
				req.setOutSid(entry.getValue());
				req.setCompanyCode("SF");
				LogisticsOfflineSendResponse res = client.execute(req, SystemConfig.ACCESS_TOKEN);

				Shipping shipping = res.getShipping();
				if (shipping != null && shipping.getIsSuccess()) {
					System.out.println("成功：" + jsonUtils.toJson(res));
				} else {
					System.out.println("失败：" + jsonUtils.toJson(res));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
