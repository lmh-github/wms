package com.gionee.wms.common;

import java.io.UnsupportedEncodingException;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class Base64 {
    // 加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new String(encodeBase64(b));
        }
        return s;
    }

    // 解密
    public static String getFromBase64(String s) {
        String result = null;
        if (s != null) {
            try {
                result = new String(decodeBase64(s), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}