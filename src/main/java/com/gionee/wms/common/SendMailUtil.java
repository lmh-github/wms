package com.gionee.wms.common;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Pengbin on 2017/4/1.
 */
public class SendMailUtil {

    public static void main(String[] args) {
        try {
            Map<String, Object> parameterMap = Maps.newHashMap();
            parameterMap.put("title", "CESHI");
            parameterMap.put("content", "asdfasdf");
            parameterMap.put("addressList", "pengbin@gionee.com");

            String info = new HttpRequestor().doPost("http://msg.gionee.com/send_mail/sendByAddressOnly.json", parameterMap);
            System.out.println(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
