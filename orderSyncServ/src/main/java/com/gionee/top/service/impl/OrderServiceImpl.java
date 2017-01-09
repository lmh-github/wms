package com.gionee.top.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.stereotype.Service;

import com.gionee.top.config.SystemConfig;
import com.gionee.top.entity.CommonResult;
import com.gionee.top.entity.CommonResult.ErrCodeEnum;
import com.gionee.top.entity.CommonResult.RetCodeEnum;
import com.gionee.top.entity.SendRequest;
import com.gionee.top.service.OrderService;
import com.gionee.top.util.JsonUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Shipping;
import com.taobao.api.request.LogisticsOfflineSendRequest;
import com.taobao.api.response.LogisticsOfflineSendResponse;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	private static final Log log = LogFactory.getLog(OrderServiceImpl.class);
	private JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);

	@Override
	public CommonResult toSend(SendRequest sendReq) {
		String sendJson = jsonUtils.toJson(sendReq);
		CommonResult result = new CommonResult();
		try {
			if (StringUtils.isBlank(sendReq.getOrderCode())
					|| StringUtils.isBlank(sendReq.getShippingCode())
					|| StringUtils.isBlank(sendReq.getShippingNo())) {
				result.setResult(RetCodeEnum.RET_SUCCESS.getRet(),
						ErrCodeEnum.ERR_PARAM_ERROR.getErr(),
						ErrCodeEnum.ERR_PARAM_ERROR.getMsg());
				log.warn("发货处理请求参数不合法--" + sendJson);
			} else {
				log.info("发货处理开始--" + sendJson);
				TaobaoClient client = new DefaultTaobaoClient(
						SystemConfig.TOP_URL, SystemConfig.APPKEY,
						SystemConfig.APPSECRET);
				LogisticsOfflineSendRequest req = new LogisticsOfflineSendRequest();
				req.setTid(Long.valueOf(sendReq.getOrderCode()));
				req.setOutSid(sendReq.getShippingNo());
				req.setCompanyCode(SystemConfig.SHIPPING_CODE_MAP.get(sendReq
						.getShippingCode()));
				LogisticsOfflineSendResponse res = client.execute(req,
						SystemConfig.ACCESS_TOKEN);

				log.info("LogisticsOfflineSendResponse--"
						+ jsonUtils.toJson(res));

				Shipping shipping = res.getShipping();
				if (shipping != null && shipping.getIsSuccess()) {
					result.setResult(RetCodeEnum.RET_SUCCESS.getRet(),
							ErrCodeEnum.ERR_SUCCESS.getErr(), "");
				} else {
					result.setResult(RetCodeEnum.RET_SUCCESS.getRet(),
							ErrCodeEnum.ERR_SYSTEM_ERROR.getErr(),
							jsonUtils.toJson(shipping));
				}
				log.info("发货处理结束--" + jsonUtils.toJson(result));
			}
		} catch (Exception e) {
			result.setResult(RetCodeEnum.RET_SUCCESS.getRet(),
					ErrCodeEnum.ERR_SYSTEM_ERROR.getErr(), e.getMessage());
			log.error("发货处理异常--" + sendJson);
		}
		return result;
	}

}
