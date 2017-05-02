package com.gionee.wms.service.stock;

import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/3/28.
 */
@Component("XZ")
public class XzBuilder implements EInvoiceBuildService {
    @Override
    public Map<String, Object> buildContent(SalesOrder order, List<SalesOrderGoods> goods) throws Exception {
        Map<String, Object> fpxxxzMap = new HashMap<>();
        fpxxxzMap.put("FPQQLSH", ""); // 发票请求唯一流水号
        fpxxxzMap.put("DSPTBM", ""); // 平台编码
        fpxxxzMap.put("DDH", ""); // 订单号
        fpxxxzMap.put("NSRSBH", ""); // 开票方识别号
        fpxxxzMap.put("PDF_XZFS", ""); // PDF 下载方式

        return fpxxxzMap;
    }

    @Override
    public String getTemplate() {
        return "e-invoice-down-content.ftl";
    }

    @Override
    public String getInterfaceCode() {
        return "ECXML.FPXZ.CX.E_INV";
    }
}
