package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/5/26.
 */
public interface OrderReportService {

    /**
     * @param params
     * @return
     */
    List<Map<String, Object>> rightOrderTimelineQuery(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int rightOrderTimelineQueryCount(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    List<Map<String, String>> exportQuery(Map<String, Object> params);
}
