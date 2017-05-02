package com.gionee.wms.service.stock;

import com.gionee.wms.common.EInvoiceConfig;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * 冲红
 * Created by Pengbin on 2017/3/28.
 */
@Component("CH")
public class ChBuilder implements EInvoiceBuildService {

    @Autowired
    private InvoiceInfoService invoiceInfoService;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> buildContent(SalesOrder order, List<SalesOrderGoods> goods) throws Exception {
        Map<String, Object> modelMap = Maps.newHashMap();
        List<Map<String, Object>> xmxxMapList = Lists.newArrayList();
        modelMap.put("FPKJXX_XMXXS", xmxxMapList);

        BigDecimal hjxmje = new BigDecimal(0); // 合计项目金额
        BigDecimal hjse = new BigDecimal(0); // 合计税额
        for (SalesOrderGoods g : goods) {
            if (g.getUnitPrice().doubleValue() <= 20) { // 小于20块钱的物品不开发票
                continue;
            }
            BigDecimal xmje = g.getUnitPrice().multiply(new BigDecimal(g.getQuantity())).setScale(2, ROUND_DOWN);
            BigDecimal se = xmje.subtract(xmje.divide(new BigDecimal("1.17"), 2, BigDecimal.ROUND_DOWN)).setScale(2, ROUND_DOWN);
            hjxmje = hjxmje.add(xmje); // 相加合计金额
            hjse = hjse.add(se); // 相加税额

            Map<String, Object> xmxxMap = new HashMap<>();
            xmxxMap.put("XMMC", g.getSkuName()); // 项目名称
            xmxxMap.put("XMDW", g.getMeasureUnit()); // 项目单位
            xmxxMap.put("GGXH", g.getSkuCode()); // 规格型号
            xmxxMap.put("XMSL", g.getQuantity() * -1); // 项目数量
            xmxxMap.put("HSBZ", "1"); // 含税标志；表示项目单价和项目金额是否含税。 0表示都不含税， 1 表示都含税
            xmxxMap.put("FPHXZ", "0"); // 发票行性质 0 正常行、1 被折扣行 2 折扣行
            xmxxMap.put("XMDJ", g.getUnitPrice().setScale(2, ROUND_DOWN)); // 项目单价
            xmxxMap.put("SPBM", "3070401000000000000"); // 商品编码
            xmxxMap.put("ZXBM", "自行编码"); //
            xmxxMap.put("YHZCBS", "0"); // 优惠政策标识
            xmxxMap.put("XMJE", xmje.multiply(new BigDecimal(-1)).setScale(2, ROUND_DOWN)); // 项目金额
            xmxxMap.put("SL", "0.17"); // 税率;如果税率为 0，表示免税
            xmxxMap.put("SE", se.multiply(new BigDecimal(-1)).setScale(2, ROUND_DOWN)); // 税额

            xmxxMapList.add(xmxxMap);
        }

        InvoiceInfo invoiceInfo = invoiceInfoService.get(order.getOrderCode());
        modelMap.put("FPQQLSH", invoiceInfoService.createChLsh(order.getOrderCode())); // 发票请求唯一流水号
        modelMap.put("DSPTBM", EInvoiceConfig.E_DSPTBM); // 平台编码
        modelMap.put("NSRSBH", EInvoiceConfig.E_NSRSBH); // 开票方识别号
        modelMap.put("NSRMC", EInvoiceConfig.E_NSRMC); // 开票方名称
        modelMap.put("DKBZ", "1"); // 代开标志 自开(0) 默认为自开
        modelMap.put("KPXM", "11"); // 主要开票商品，或者第一条商品，取项目信息中第一条
        modelMap.put("BMB_BBH", "1.0"); //编码表版本号，目前为1.0
        modelMap.put("XHF_NSRSBH", EInvoiceConfig.E_XHF_NSRSBH); // 销货方识别号
        modelMap.put("XHFMC", EInvoiceConfig.E_XHFMC); // 销货方名称
        modelMap.put("XHF_DZ", EInvoiceConfig.E_XHF_DZ); // 销货方地址
        modelMap.put("XHF_DH", EInvoiceConfig.E_XHF_DH); // 销货方电话
        modelMap.put("GHFMC", order.getConsignee()); // 购货方名称，即发票抬头 购货方为“ 个人” 时，可输入名称，输入名称是为“个人(名称)”，”（”为半角；例 个人(王杰)
        modelMap.put("GHF_SJ", order.getMobile()); // 购货方手机
        modelMap.put("GHFQYLX", "01"); // 购货方企业类型（03 个人）
        modelMap.put("KPY", EInvoiceConfig.E_KPY); // 开票员
        modelMap.put("SKY", EInvoiceConfig.E_SKY); // 收款员
        modelMap.put("FHR", EInvoiceConfig.E_FHR); // 复核人
        modelMap.put("KPLX", "2"); // 开票类型 1正票 2红票
        modelMap.put("CZDM", "20"); // 操作代码 10 正票正常开具
        modelMap.put("YFP_DM", invoiceInfo.getFpDm()); // 原发票代码
        modelMap.put("YFP_HM", invoiceInfo.getFpHm()); // 原发票号码
        modelMap.put("TSCHBZ", "0"); // 特殊冲红标志（冲红电子）
        modelMap.put("CHYY", "订单取消冲红"); // 冲红原因
        modelMap.put("QD_BZ", "1"); // 清单标志
        modelMap.put("KPHJJE", hjxmje.multiply(new BigDecimal(-1)).setScale(2, ROUND_DOWN).toString()); // 价税合计金额
        modelMap.put("HJBHSJE", hjxmje.subtract(hjse).multiply(new BigDecimal(-1)).setScale(2, ROUND_DOWN)); // 合计不含税金额
        modelMap.put("HJSE", hjse.multiply(new BigDecimal(-1)).setScale(2, ROUND_DOWN).toString()); // 合计税额
        modelMap.put("BZ", ""); // 备注
        modelMap.put("DDH", order.getOrderCode()); // 订单号

        return modelMap;
    }

    /** {@inheritDoc} */
    @Override
    public String getTemplate() {
        return "e-invoice-make-content.ftl";
    }

    /** {@inheritDoc} */
    @Override
    public String getInterfaceCode() {
        return "DFXJ1009";
    }
}
