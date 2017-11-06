package com.gionee.wms.aspect;

import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.OrderOutlier;
import com.gionee.wms.entity.OrderOutlierLog;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.service.log.OrderOutlierLogService;
import com.gionee.wms.service.stock.OrderOutlierService;
import org.apache.shiro.util.CollectionUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2017/10/26
 * Time: 14:36
 *
 * @author huyunfan
 */
@Aspect
@Component
public class OutlierAspect {

    @Autowired
    private OrderOutlierService orderOutlierService;

    @Autowired
    private OrderOutlierLogService orderOutlierLogService;

    private static ThreadLocal<List<OrderOutlierLog>> threadLocal = new ThreadLocal<>();

    private static final String TURN_ON = "0";

    private static final String EXCHANGE_ORDER = "换货订单";


    @Pointcut("@annotation(com.gionee.wms.aspect.annotation.OutlierLog)")
    public void logAspect() {
    }


    @AfterReturning(value = "logAspect() && args(order,orderGoodsList)", argNames = "order,orderGoodsList")
    public void doAfter(final SalesOrder order, List<SalesOrderGoods> orderGoodsList) {
        // 只处理普通订单
        if (!OutlierAspect.EXCHANGE_ORDER.equals(order.getType())) {
            try {
                // 匹配规则
                validationSalesOrder(order, orderGoodsList);
                List<OrderOutlierLog> logs = threadLocal.get();

                if (!CollectionUtils.isEmpty(logs)) {
                    // 批量插入
                    orderOutlierLogService.insertBatch(logs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                threadLocal.remove();
            }
        }
    }


    private void validationSalesOrder(SalesOrder order, List<SalesOrderGoods> orderGoodsList) {
        // 符合下单时间规则
        QueryMap queryMap = new QueryMap();
        queryMap.put("orderTime", order.getOrderTime());
        // 校验规则是否生效
        queryMap.put("logSwitch", OutlierAspect.TURN_ON);

        List<OrderOutlier> orderOutliers = orderOutlierService.queryAll(queryMap.getMap());

        if (!CollectionUtils.isEmpty(orderOutliers)) {
            List<OrderOutlierLog> logList = new ArrayList<>();

            for (OrderOutlier orderOutlier : orderOutliers) {
                for (SalesOrderGoods orderGoods : orderGoodsList) {
                    // 匹配来源
                    if (!orderOutlier.getOrderSource().contains(order.getOrderSource())) {
                        continue;
                    }
                    // 匹配sku
                    if (!orderOutlier.getSkuIds().contains(orderGoods.getSkuCode())) {
                        continue;
                    }
                    // 当前单价是否低于预期单价
                    if (orderGoods.getUnitPrice().compareTo(orderOutlier.getUnitPrice()) >= 0) {
                        continue;
                    }

                    OrderOutlierLog orderOutlierLog = new OrderOutlierLog();
                    orderOutlierLog.setOrderCode(order.getOrderCode());
                    orderOutlierLog.setHandledTime(order.getOrderTime());
                    orderOutlierLog.setHandledBy(order.getHandledBy());
                    orderOutlierLog.setOutlierId(orderOutlier.getId());
                    orderOutlierLog.setMinUnitPrice(orderOutlier.getUnitPrice());
                    orderOutlierLog.setSkuCode(orderGoods.getSkuCode());
                    orderOutlierLog.setUnitPrice(orderGoods.getUnitPrice());
                    orderOutlierLog.setOrderSource(order.getOrderSource());

                    logList.add(orderOutlierLog);
                }
            }

            if (!CollectionUtils.isEmpty(logList)) {
                threadLocal.set(logList);
            }
        }
    }

}
