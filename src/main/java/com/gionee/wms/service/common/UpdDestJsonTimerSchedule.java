package com.gionee.wms.service.common;

import com.gionee.wms.common.HttpClientUtil;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.constant.Consts;
import com.gionee.wms.vo.UpdDestJsonRequestVo;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 *
 * Created by lmh on 2018/1/11.
 */
public class UpdDestJsonTimerSchedule extends TimerSchedule{
    private static final Logger LOG = Logger.getLogger(UpdDestJsonTimerSchedule.class);

    private UpdDestJsonRequestVo requestParam;

    public UpdDestJsonRequestVo getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(UpdDestJsonRequestVo requestParam) {
        this.requestParam = requestParam;
    }

    public UpdDestJsonTimerSchedule(UpdDestJsonRequestVo requestParam){
        setRequestParam(requestParam);
    }

    @Override
    public void task() {
        send();
//        setRunFlag(false);//停止定时任务
    }

    /**
     * 发送请求
     */
    private  void send() {
        try {
            JsonUtils jsonUtils = new JsonUtils();
            String param =jsonUtils .toJson(getRequestParam());
            LOG.info("请求参数："+param);
            String res = HttpClientUtil.httpPostWithJson(Consts.URL_DATATRANSETL_UPDDESTJSON, param);
//            UpdDestJsonResponseVo result = jsonUtils.fromJson(res, UpdDestJsonResponseVo.class);
//            System.out.println(res);
            LOG.info(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UpdDestJsonRequestVo vo = new UpdDestJsonRequestVo();
        vo.put("123", "test");
        new UpdDestJsonTimerSchedule(vo).schedule(0, 1000,2);
    }
}
