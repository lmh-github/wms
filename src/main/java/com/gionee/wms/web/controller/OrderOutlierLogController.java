package com.gionee.wms.web.controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.OrderOutlierLog;
import com.gionee.wms.service.log.OrderOutlierLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2017/11/1
 * Time: 14:42
 *
 * @author huyunfan
 */
@Controller
@RequestMapping("/outlierlog")
public class OrderOutlierLogController {

    @Autowired
    private OrderOutlierLogService orderOutlierLogService;

    /**
     * @param modelMap
     * @param queryMap
     * @param page
     * @return
     */
    @RequestMapping("list.do")
    public String list(ModelMap modelMap, QueryMap queryMap, Page page) {
        int total = orderOutlierLogService.queryCount(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();

        List<OrderOutlierLog> list;
        if (total == 0) {
            list = new ArrayList<>(0);
        } else {
            queryMap.put("page", page);
            list = orderOutlierLogService.query(queryMap.getMap());
        }

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("page", page);
        return "log/outlierLog";
    }

}
