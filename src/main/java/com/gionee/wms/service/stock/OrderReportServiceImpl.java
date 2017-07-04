package com.gionee.wms.service.stock;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dao.SalesOrderNodeInfoDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/5/26.
 */
@Service
public class OrderReportServiceImpl implements OrderReportService {
    @Autowired
    private SalesOrderNodeInfoDao salesOrderNodeInfoDao;

    /** {@inheritDoc} */
    @Override
    public List<Map<String, Object>> rightOrderTimelineQuery(Map<String, Object> params) {
        Map<String, String> valueMap = WmsConstants.OrderSource.getValueMap();
        List<Map<String, Object>> list = salesOrderNodeInfoDao.query(params);
        for (Map<String, Object> map : list) {
            map.put("orderSourceName", valueMap.get(map.get("orderSource")));
            map.put("filterToPrintTime", getTimeUse(map.get("orderPushTime"), map.get("paymentTime"))); // =推单时间 - 支付时间
            map.put("printToSendTime", getTimeUse(map.get("orderSendTime"), map.get("orderPushTime"))); // =出库时间 - 推单时间
            map.put("totalUseTime", getTimeUse(map.get("orderFinishTime"), map.get("orderSendTime"))); // =签收时间 - 出库时间
            map.put("orderUseTime", getTimeUse(map.get("orderFinishTime"), map.get("paymentTime"))); // =签收时间 - 支付时间
        }
        return list;
    }

    /**
     * @param time1
     * @param time2
     * @return
     */
    private String getTimeUse(Object time1, Object time2) {
        try {
            Date t1 = (Date) time1;
            Date t2 = (Date) time2;
            if (t1 == null || t2 == null) {
                return null;
            }

            long l = Math.abs(t2.getTime() - t1.getTime());
            int day = (int) (l / (24 * 60 * 60 * 1000));
            int hour = ((int) (l / (60 * 60 * 1000))) - day * 24;
            int min = ((int) (l / (60 * 1000))) - (day * 24 * 60) - (hour * 60);
            // int sec = ((int) (l / 1000)) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (min * 60);

            StringBuilder sb = new StringBuilder();
            if (day > 0) {
                sb.append(day).append("天");
            }
            if (hour > 0) {
                sb.append(hour).append("小时");
            }
            if (min > 0) {
                sb.append(min).append("分钟");
            }
            // if (sec > 0) {
            //     sb.append(sec).append("秒");
            // }

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int rightOrderTimelineQueryCount(Map<String, Object> params) {
        return salesOrderNodeInfoDao.queryCount(params);
    }

    /** {@inheritDoc} */
    @Override
    public List<Map<String, String>> exportQuery(Map<String, Object> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, String>> mapList = Lists.newArrayList();
        List<Map<String, Object>> resultList = rightOrderTimelineQuery(params);
        for (Map<String, Object> map : resultList) {
            Map<String, String> item = Maps.newHashMap();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    if (entry.getValue() instanceof Date) {
                        item.put(entry.getKey(), sdf.format(entry.getValue()));
                    } else {
                        item.put(entry.getKey(), entry.getValue().toString());
                    }
                } else {
                    item.put(entry.getKey(), StringUtils.EMPTY);
                }
            }
            mapList.add(item);
        }
        return mapList;
    }
}
