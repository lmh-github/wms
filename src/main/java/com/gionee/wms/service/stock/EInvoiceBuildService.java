package com.gionee.wms.service.stock;

import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;

import java.util.List;
import java.util.Map;

/**
 * 模板参数
 * Created by Pengbin on 2017/3/28.
 */
public interface EInvoiceBuildService {

    /**
     * 构建发票模板参数
     * @param order 订单
     * @param goods 订单商品
     * @return model Map
     * @throws Exception Exception
     */
    Map<String, Object> buildContent(SalesOrder order, List<SalesOrderGoods> goods) throws Exception;

    /**
     * 返回与之对应的模板
     * @return String
     */
    String getTemplate();

    /**
     * 返回接口编码
     * @return String
     */
    String getInterfaceCode();
}