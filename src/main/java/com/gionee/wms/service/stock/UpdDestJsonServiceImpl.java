package com.gionee.wms.service.stock;

import com.gionee.wms.common.constant.Consts;
import com.gionee.wms.common.HttpClientUtil;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.service.common.TimerSchedule;
import com.gionee.wms.vo.UpdDestJsonRequestVo;
import com.gionee.wms.vo.UpdDestJsonResponseVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lmh on 2018/1/9.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdDestJsonServiceImpl extends TimerSchedule implements  UpdDestJsonService{
    private static final Logger LOG = Logger.getLogger(UpdDestJsonServiceImpl.class);

    private UpdDestJsonRequestVo requestParam;

    public UpdDestJsonRequestVo getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(UpdDestJsonRequestVo requestParam) {
        this.requestParam = requestParam;
    }

    @Override
    public void sendIMEI(final UpdDestJsonRequestVo vo) {
        setRequestParam(vo);
        long delay = 0;//延时时间
        long intevalPeriod = 1 * 1000 * 60 * 30;//间隔时间，30分钟一次
        int time = 3;//任务执行三次
        try{
            schedule(delay,intevalPeriod,time);//执行定时任务
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("发送IMEI信息错误",e);
        }

    }

    /**
     * 发送请求
     */
    private  void send() {
        try {
            JsonUtils jsonUtils = new JsonUtils();
            String param =jsonUtils .toJson(getRequestParam());
            String res = HttpClientUtil.httpPostWithJson(Consts.URL_DATATRANSETL_UPDDESTJSON, param);
//            UpdDestJsonResponseVo result = jsonUtils.fromJson(res, UpdDestJsonResponseVo.class);
            System.out.println(res);
            LOG.info(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void task() {
        send();
//        setRunFlag(false);//停止定时任务
    }
    public static void main(String[] args) {
        UpdDestJsonRequestVo vo = new UpdDestJsonRequestVo();
        vo.put("123", "test");
        new UpdDestJsonServiceImpl().sendIMEI(vo);
    }

}
