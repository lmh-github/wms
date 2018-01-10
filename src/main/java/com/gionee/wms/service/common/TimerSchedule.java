package com.gionee.wms.service.common;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时计划
 * Created by lmh on 2018/1/10.
 */
public abstract class TimerSchedule{
    private boolean runFlag = true;

    public boolean isRunFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    /**
     * 执行固定次数的定时任务
     * 需要实现task()方法
     * @param delay 延时时间  单位毫秒
     * @param intevalPeriod  间隔时间  单位毫秒
     * @param time  次数
     */
    public void schedule(final long delay,final long intevalPeriod ,final int time){
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            private int count =0;
            @Override
            public void run() {
                if(!isRunFlag() || count >= time){
                    timer.cancel();
                    return;
                }
                count++;
                task();
            }
        };
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    /**
     * 任务体
     */
    public abstract void task();
}


class Testss extends TimerSchedule{
    private int count = 0;
    @Override
    public void task() {
        System.out.println(++count);
    }

    public static void main(String[] args) {
        long delay = 0;
        long intevalPeriod = 1 * 1000;//30分钟一次
        new Testss().schedule(delay,intevalPeriod,5);
    }
}
