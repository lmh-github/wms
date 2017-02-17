/**
 * Project Name:wms
 * File Name:ServiceCtrlMessage.java
 * Package Name:com.gionee.wms.vo
 * Date:2014年8月27日下午2:51:23
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.vo;

/**
 * Service操作消息
 * @author PengBin 00001550<br>
 * @date 2014年8月27日 下午2:51:23
 */
public class ServiceCtrlMessage<T> {

    /** 操作结果,true|false 即成功|失败 */
    private boolean result;
    /** 说明，备注 */
    private String message;

    private T data;

    public ServiceCtrlMessage() {
        super();
    }

    public ServiceCtrlMessage(boolean result, String message) {
        super();
        this.result = result;
        this.message = message;
    }

    public ServiceCtrlMessage(boolean result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    /**
     * @return the result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * @param result the result
     */
    public void setResult(boolean result) {
        this.result = result;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ServiceCtrlMessage [result=" + result + ", message=" + message + "]";
    }

}
