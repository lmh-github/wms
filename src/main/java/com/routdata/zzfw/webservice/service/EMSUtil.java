/**
 * @author jay_liang
 * @date 2014-4-1 下午2:51:06
 */
package com.routdata.zzfw.webservice.service;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * @author jay_liang
 * @=======================================
 * @Description TODO
 * @date 2014-4-1 下午2:51:06
 * @=======================================
 */
public class EMSUtil {
    // 签名程序代码片段
    public static String encrypt(String content, String keyValue, String charset) throws Exception {
        if (keyValue != null) {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**
     * 对传入的字符串进行MD5加密
     * @param plainText
     * @return
     */
    public static String MD5(String plainText, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plainText.getBytes(charset));
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) i += 256;
            if (i < 16) buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    /**
     * base64编码
     * @param str
     * @return
     * @throws Exception
     */
    public static String base64(String str, String charset) throws Exception {
        return new String(Base64.encodeBase64(str.getBytes(charset)));
    }
}
