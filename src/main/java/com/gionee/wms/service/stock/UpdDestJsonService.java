package com.gionee.wms.service.stock;

import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.vo.UpdDestJsonRequestVo;

import java.util.List;

/**
 * 发送出库的IMEI到第三方业务
 *   调用接口http://www.datatransetl.com:8085/dc/app/updDestJson.json
 * Created by admin on 2018/1/9.
 */
public interface UpdDestJsonService {

    /**
     * 发送IMEI和地址信息到第三方
     * @param vo
     */
    void sendIMEI(UpdDestJsonRequestVo vo);

    /**
     * 销售单退货发送IMEI
     * @param indivs
     */
    void sendIMEI(List<Indiv> indivs);

    /**
     * 销售单出库发送IMEI
     * @param salesOrder
     */
    void sendIMEI(SalesOrder salesOrder);

    /**
     * 销售单批量出库发送IMEI
     * @param salesOrders
     */
    void sendIMEIBat(List<SalesOrder> salesOrders);

    /**
     * 调货出库发送IMEI
     * @param transfer
     */
    void sendIMEI( Transfer transfer);

    /**
     * 调货退货发送IMEI
     * @param indivCodes
     */
    void sendIMEI(String[] indivCodes);
}
