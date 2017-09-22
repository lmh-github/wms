package com.gionee.wms.message.service;

/**
 * Created by gionee on 2017/7/11.
 */
public interface MessageService {
    /**
     * 加载任意数据
     *
     * @return
     */
    String load();

    /**
     * 初始化dwrScriptSessionManagerUtil
     * 将userId存入scriptSession
     *
     * @param userId
     */
    void push(final String userId);

    /**
     * 接受前台发送的消息 并推送给其他客户端
     * account 指定登录用户
     *
     * @param messageAuto
     */
    void sendMessageAuto(final String messageAuto , final String account);

    /**
     * 接受前台发送的消息 并推送给其他客户端
     *
     * @param messageAuto
     */
    void sendMessageAuto(final String messageAuto);
}
