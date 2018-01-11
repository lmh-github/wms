package com.gionee.wms.service.stock;

import com.gionee.wms.common.constant.Consts;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.SalesOrderImeiDao;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderImei;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.service.common.UpdDestJsonTimerSchedule;
import com.gionee.wms.vo.UpdDestJsonRequestVo;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;


/**
 * Created by lmh on 2018/1/9.
 */
@Service
public class UpdDestJsonServiceImpl  implements  UpdDestJsonService{
    private static final Logger LOG = Logger.getLogger(UpdDestJsonServiceImpl.class);

    @Autowired
    private SalesOrderImeiDao salesOrderImeiDao;
    @Autowired
    private IndivDao indivDao;

    @Override
    public void sendIMEI(UpdDestJsonRequestVo vo) {
        long delay = 0;//延时时间
        long intevalPeriod = 1 * 1000;//间隔时间，30分钟一次 1 * 1000 * 60 * 30
        int time = 3;//任务执行三次
        try{
            new UpdDestJsonTimerSchedule(vo).schedule(delay,intevalPeriod,time);//执行定时任务
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("发送IMEI信息错误",e);
        }

    }

    @Override
    public void sendIMEI(List<Indiv> indivs) {
        sendIMEI(fillSaleOrderBackParam(indivs));
    }

    @Override
    public void sendIMEI(SalesOrder salesOrder) {
        sendIMEI(fillSaleOrderPollStockParam(salesOrder));
    }

    @Override
    public void sendIMEI(Transfer transfer) {
        sendIMEI(fillTransferPollStockParam(transfer));
    }

    @Override
    public void sendIMEI(String[] indivCodes) {
        sendIMEI(fillTransferBackParam(indivCodes));
    }

    /**
     * 构造销售单退货参数
     * @param indivs
     * @return
     */
    private UpdDestJsonRequestVo fillSaleOrderBackParam(List<Indiv> indivs){
        UpdDestJsonRequestVo vo = new UpdDestJsonRequestVo();
        for (Indiv indiv : indivs) {
            vo.put(indiv.getIndivCode(), Consts.BACK_DEST_NAME_DEFAULT);
        }
        return vo;
    }

    /**
     * 构造销售订单出库参数
     * @param salesOrder
     * @return
     */
    private UpdDestJsonRequestVo fillSaleOrderPollStockParam(SalesOrder salesOrder){
        UpdDestJsonRequestVo vo = new UpdDestJsonRequestVo();;
        List<SalesOrderImei> salesOrderImeis = queryImeis(salesOrder);
        for (SalesOrderImei order:salesOrderImeis) {
            vo.put(order.getImei(),salesOrder.getFullAddress());
        }
        return vo;
    }

    /**
     * 根据 order_code 查询所有的 Imei
     * @param salesOrder
     * @return
     */
    private  List<SalesOrderImei> queryImeis(SalesOrder salesOrder){
        if(salesOrder.getOrderCode()!=null){
            Map<String, String> params = new HashMap<String, String>();
            params.put("order_code", salesOrder.getOrderCode());
            List<SalesOrderImei> salesOrderImeis = salesOrderImeiDao.queryImeis(params);
            return salesOrderImeis;
        }
        return null;
    }


    /**
     * 构造调货出库参数
     * @param transfer
     * @return
     */
    private UpdDestJsonRequestVo fillTransferPollStockParam(Transfer transfer){
        UpdDestJsonRequestVo vo = new UpdDestJsonRequestVo();;
        List<Indiv> indivList = getIndivList(transfer.getTransferId());
        for (Indiv indiv:indivList) {
            vo.put(indiv.getIndivCode(),transfer.getTransferTo());
        }
        return vo;
    }

    /**
     * 根据调货id获取所有的个体编号及IMEI
     * @param transferId
     * @return
     */
    private List<Indiv> getIndivList(Long transferId) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("prepareId", transferId);
        return indivDao.queryIndivList(criteria);
    }

    /**
     * 构造调货退货参数
     * @return
     */
    private UpdDestJsonRequestVo fillTransferBackParam(String[] indivCodes){
        UpdDestJsonRequestVo vo = new UpdDestJsonRequestVo();
        for(String indivcode:indivCodes){
            vo.put(indivcode, Consts.BACK_DEST_NAME_DEFAULT);
        }
        return vo;
    }
}
