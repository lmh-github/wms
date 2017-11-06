package com.gionee.wms.web.controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.OrderOutlier;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.service.stock.OrderOutlierService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.extend.DwzMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2017/11/1
 * Time: 15:07
 *
 * @author huyunfan
 */
@Controller
@RequestMapping("/stock/outlier")
public class OrderOutlierController {

    @Autowired
    private OrderOutlierService orderOutlierService;

    /**
     * @param modelMap
     * @param queryMap
     * @param page
     * @return
     */
    @RequestMapping("list.do")
    public String list(ModelMap modelMap, QueryMap queryMap, Page page) {
        int total = orderOutlierService.queryCount(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();

        List<OrderOutlier> list;
        if (total == 0) {
            list = new ArrayList<>(0);
        } else {
            queryMap.put("page", page);
            list = orderOutlierService.query(queryMap.getMap());

            String sku = (String) queryMap.getMap().get("sku");
            String source = (String) queryMap.getMap().get("source");

            if (!StringUtils.isEmpty(sku)) {
                // 重新计算 过滤sku
                list = againCalculate(page, list, sku, null);
            }
            if (!StringUtils.isEmpty(source)) {
                // 重新计算 过滤来源
                list = againCalculate(page, list, null, source);
            }
        }

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("page", page);
        return "stock/outlier/list";
    }

    private List<OrderOutlier> againCalculate(Page page, List<OrderOutlier> list, String sku, String source) {
        List<OrderOutlier> orderOutliers = new ArrayList<>();

        for (OrderOutlier orderOutlier : list) {
            if (sku != null) {
                String[] ids = orderOutlier.getSkuIds().split(",");
                for (String id : ids) {
                    if (sku.equals(id)) {
                        orderOutliers.add(orderOutlier);
                    }
                }
            }
            if (source != null) {
                String[] sources = orderOutlier.getOrderSource().split(",");
                for (String s : sources) {
                    if (source.equals(s)) {
                        orderOutliers.add(orderOutlier);
                    }
                }
            }
        }
        page.setTotalRow(orderOutliers.size());
        page.calculate();
        return orderOutliers;
    }

    /**
     * @param queryMap
     * @param orderOutlier
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public Object save(QueryMap queryMap, OrderOutlier orderOutlier) {
        try {
            ServiceCtrlMessage message;
            fillIdsAndNames(orderOutlier);
            if (!StringUtils.isEmpty(orderOutlier.getId())) {
                message = orderOutlierService.update(orderOutlier);
            } else {
                message = orderOutlierService.save(orderOutlier);
            }
            return message.isResult() ? DwzMessage.success("", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    private void fillIdsAndNames(OrderOutlier orderOutlier) {
        if (!CollectionUtils.isEmpty(orderOutlier.getSkuList())) {
            StringBuilder skuIds = new StringBuilder();
            StringBuilder skuNames = new StringBuilder();

            for (Sku sku : orderOutlier.getSkuList()) {
                skuIds.append(sku.getSkuCode());
                skuIds.append(",");
                skuNames.append(sku.getSkuName());
                skuNames.append(",");
            }
            orderOutlier.setSkuIds(skuIds.substring(0, skuIds.length() - 1));
            orderOutlier.setSkuNames(skuNames.substring(0, skuNames.length() - 1));
        }
    }

    /**
     * @param modelMap
     * @param id
     * @param to
     * @return
     */
    @RequestMapping("to.do")
    public String to(ModelMap modelMap, Long id, String to) {
        if (id != null) {
            OrderOutlier orderOutlier = orderOutlierService.get(id);
            modelMap.addAttribute("orderOutlier", orderOutlier);
        }
        return "stock/outlier/" + to;
    }

    /**
     * @param orderOutlier
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public Object update(OrderOutlier orderOutlier) {
        fillIdsAndNames(orderOutlier);
        return orderOutlierService.update(orderOutlier);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public Object delete(String id) {
        return orderOutlierService.remove(id);
    }


}
