package com.gionee.wms.web.extend;

import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.web.action.AjaxActionSupport.StatusCode;
import com.google.common.collect.Maps;

import java.util.Map;

import static com.gionee.wms.web.action.AjaxActionSupport.StatusCode.ERROR;
import static com.gionee.wms.web.action.AjaxActionSupport.StatusCode.SUCESS;

/**
 * DWZ框架消息
 * Created by Pengbin on 2017/4/27.
 */
public class DwzMessage {

    /**
     * DWZ框架成功信息
     * @param msg      返回消息
     * @param queryMap queryMap
     * @return Map
     */
    public static Map<String, Object> success(String msg, QueryMap queryMap) {
        return getBasicMap(SUCESS, msg, queryMap);
    }

    /**
     * DWZ框架失败消息
     * @param msg      返回消息
     * @param queryMap queryMap
     * @return Map
     */
    public static Map<String, Object> error(String msg, QueryMap queryMap) {
        return getBasicMap(ERROR, msg, queryMap);
    }

    /**
     * 返回基础Map
     * @param statusCode statusCode
     * @param message    message
     * @param queryMap   queryMap
     * @return Map
     */
    private static Map<String, Object> getBasicMap(StatusCode statusCode, String message, QueryMap queryMap) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("statusCode", statusCode.toString());
        result.put("message", message);
        result.put("callbackType", null);
        result.put("navTabId", null);
        result.put("forwardUrl", null);
        result.put("rel", null);

        Map<String, Object> resultMap;
        if (queryMap == null) {
            resultMap = Maps.newHashMap();
        } else {
            resultMap = queryMap.getMap();
        }
        result.putAll(resultMap);

        return result;
    }


}
