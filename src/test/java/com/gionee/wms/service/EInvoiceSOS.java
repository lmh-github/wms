package com.gionee.wms.service;

import com.gionee.wms.common.*;
import com.gionee.wms.dto.Data;
import com.gionee.wms.dto.DataDescription;
import com.gionee.wms.dto.GlobalInfo;
import com.gionee.wms.dto.KpInterface;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.service.common.IDGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.math.BigDecimal.ROUND_DOWN;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.leftPad;

/**
 * 临时解决5-26号的发票重复冲红问题
 * Created by Pengbin on 2017/5/26.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring*.xml"})
public class EInvoiceSOS extends TestCase {

    /** 开票接口成功状态码 */
    private static final String SUCCESS_CODE = "0000";
    private static final Pattern KPRQ_PATTERN = Pattern.compile("\\d{14}");
    private static final SimpleDateFormat KPRQ_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Logger logger = LoggerFactory.getLogger(EInvoiceSOS.class);
    @Autowired
    private IDGenerator idGenerator;

    @Test
    public void test() {
        try {
            System.out.println(Integer.parseInt("abc")); // 防止误运行

            SalesOrder salesOrder = new SalesOrder();
            salesOrder.setOrderCode("20170427244013706621");
            salesOrder.setInvoiceTitle("个人");
            salesOrder.setMobile("15815545458");

            List<SalesOrderGoods> goods = Lists.newArrayList();
            SalesOrderGoods salesOrderGoods = new SalesOrderGoods();
            salesOrderGoods.setSkuName("W909爵士金");
            salesOrderGoods.setMeasureUnit("台");
            salesOrderGoods.setSkuCode("1200");
            salesOrderGoods.setQuantity(1); // 数量
            salesOrderGoods.setUnitPrice(new BigDecimal(3999));
            goods.add(salesOrderGoods);

            for (int i = 0; i < 256; i++) {
                KpInterface requestInterface = createRequestInterface(salesOrder, goods);
                System.out.println(requestInterface);
                action(requestInterface);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求航信发票接口
     * @param requestKpInterface 请求报文
     * @return KpInterface
     * @throws Exception
     */
    private KpInterface action(KpInterface requestKpInterface) throws Exception {
        Map<String, Object> model = Maps.newHashMap();
        model.put("model", requestKpInterface);
        String xml = TemplateHelper.generate(model, "e-invoice.ftl");
        logger.info("EInvoice xml:" + xml);

        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("xml", xml); // 请求参数设置
        String info = new HttpRequestor().doPost("http://ei.szhtxx.com:8090/front/request/91440300591872133B", parameterMap);
        logger.info("EInvoice responst info:\n" + info);

        new XStream().alias("interface", KpInterface.class);
        KpInterface kpInterface = XmlHelper1.toBean(info, KpInterface.class);
        String returnCode = kpInterface.getReturnStateInfo().getReturnCode();
        String returnMessage = Base64.getFromBase64(kpInterface.getReturnStateInfo().getReturnMessage());
        kpInterface.getReturnStateInfo().setReturnMessage(returnMessage);
        logger.info("EInvoice responst returnCode:\n" + returnCode + "\nreturnMessage:\n" + returnMessage);

        if (SUCCESS_CODE.equals(returnCode)) {
            String decryptContent = AesUtil.decrypt(kpInterface.getData().getContent());
            kpInterface.getData().setContent(decryptContent);
            logger.info("EInvoice responst content:\n" + decryptContent);

            FileUtils.write(new File("H:\\fp\\20170427244013706621.txt"), decryptContent + SystemUtils.LINE_SEPARATOR, "UTF-8", true);
        } else {
            FileUtils.write(new File("H:\\fp\\err.txt"), "EInvoice responst returnCode:\n" + returnCode + "\nreturnMessage:\n" + returnMessage + SystemUtils.LINE_SEPARATOR, "UTF-8", true);
        }

        return kpInterface;
    }

    private KpInterface createRequestInterface(SalesOrder order, List<SalesOrderGoods> goods) throws Exception {
        GlobalInfo globalInfo = new GlobalInfo();
        globalInfo.setTerminalCode("0");
        globalInfo.setAppId("168XVB14");
        globalInfo.setVersion("2.0");
        globalInfo.setInterfaceCode("DFXJ1009"); // 接口编码
        globalInfo.setUserName("18239169"); // 平台编码
        globalInfo.setPassWord("9608701637B0NJR1pBY593gsoSb5zg+w=="); // 10 位随机数+Base64({（ 10 位随机数+注册码）MD5})
        globalInfo.setTaxpayerId("91440300741233215A"); // 纳税人识别号
        globalInfo.setAuthorizationCode("911233215A"); // 接入系统平台授权码（ 由平台提供）
        globalInfo.setRequestCode(EInvoiceConfig.E_REQUEST_CODE); // 数据交换请求发起方代码
        globalInfo.setRequestTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss SSS").format(new Date()));
        globalInfo.setResponseCode(EInvoiceConfig.E_RESPONSE_CODE); // 数据交换请求接受方代码
        globalInfo.setDataExchangeId("P0000001ECXML.FPKJ.BC.E_INV000000001"); // 数据交换流水号（ 唯一） requestCode+8 位日期(YYYYMMDD)+9 位序列号

        DataDescription dataDescription = new DataDescription();
        dataDescription.setZipCode("0"); // 非压缩
        dataDescription.setEncryptCode("2"); // CA加密
        dataDescription.setCodeType("CA"); // CA加密

        Map<String, Object> contentMap = buildContent(order, goods);
        String contentSource = TemplateHelper.generate(contentMap, "e-invoice-make-content.ftl");
        logger.info(" EInvoice contentSource:\n" + contentSource);
        String content = AesUtil.encrypt(contentSource);

        Data data = new Data();
        data.setDataDescription(dataDescription);
        data.setContent(content);

        KpInterface request = new KpInterface();
        request.setGlobalInfo(globalInfo);
        request.setData(data);

        return request;
    }


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
            xmxxMap.put("XMSL", g.getQuantity()); // 项目数量
            xmxxMap.put("HSBZ", "1"); // 含税标志；表示项目单价和项目金额是否含税。 0表示都不含税， 1 表示都含税
            xmxxMap.put("FPHXZ", "0"); // 发票行性质 0 正常行、1 被折扣行 2 折扣行
            xmxxMap.put("XMDJ", g.getUnitPrice().setScale(2, ROUND_DOWN)); // 项目单价
            xmxxMap.put("SPBM", "3070401000000000000"); // 商品编码
            xmxxMap.put("ZXBM", g.getSkuCode()); // 自行编码
            xmxxMap.put("YHZCBS", "0"); // 优惠政策标识
            xmxxMap.put("XMJE", xmje.toString()); // 项目金额
            xmxxMap.put("SL", "0.17"); // 税率;如果税率为 0，表示免税
            xmxxMap.put("SE", se.toString()); // 税额

            xmxxMapList.add(xmxxMap);
        }

        modelMap.put("FPQQLSH", getLSH()); // 发票请求唯一流水号
        modelMap.put("DSPTBM", "168XVB14"); // 平台编码
        modelMap.put("NSRSBH", "91440300741233215A"); // 开票方识别号
        modelMap.put("NSRMC", EInvoiceConfig.E_NSRMC); // 开票方名称
        modelMap.put("DKBZ", "1"); // 代开标志 自开(0) 默认为自开
        modelMap.put("KPXM", "手机"); // 主要开票商品，或者第一条商品，取项目信息中第一条
        modelMap.put("BMB_BBH", "12.0"); //编码表版本号，目前为1.0
        modelMap.put("XHF_NSRSBH", "91440300741233215A"); // 销货方识别号
        modelMap.put("XHFMC", EInvoiceConfig.E_XHFMC); // 销货方名称
        modelMap.put("XHF_DZ", EInvoiceConfig.E_XHF_DZ); // 销货方地址
        modelMap.put("XHF_DH", EInvoiceConfig.E_XHF_DH); // 销货方电话
        modelMap.put("GHFMC", defaultString(order.getInvoiceTitle(), order.getConsignee())); // 购货方名称，即发票抬头 购货方为“ 个人” 时，可输入名称，输入名称是为“个人(名称)”，”（”为半角；例 个人(王杰)
        modelMap.put("GHF_SJ", order.getMobile()); // 购货方手机
        modelMap.put("GHFQYLX", "01"); // 购货方企业类型（03 个人）
        modelMap.put("KPY", EInvoiceConfig.E_KPY); // 开票员
        modelMap.put("SKY", EInvoiceConfig.E_SKY); // 收款员
        modelMap.put("FHR", EInvoiceConfig.E_FHR); // 复核人
        modelMap.put("KPLX", "1"); // 开票类型 1正票
        modelMap.put("TSCHBZ", "0"); // 特殊冲红标志
        modelMap.put("CZDM", "10"); // 操作代码 10 正票正常开具
        modelMap.put("QD_BZ", "0"); // 清单标志
        modelMap.put("KPHJJE", hjxmje.setScale(2, ROUND_DOWN).toString()); // 价税合计金额
        modelMap.put("HJBHSJE", hjxmje.subtract(hjse).setScale(2, ROUND_DOWN)); // 合计不含税金额
        modelMap.put("HJSE", hjse.setScale(2, ROUND_DOWN).toString()); // 合计税额
        modelMap.put("DDH", order.getOrderCode()); // 订单号
        modelMap.put("BZ", "冲正，订单号：" + order.getOrderCode());

        return modelMap;
    }

    public String getLSH() {
        String kpLshPrefix = new SimpleDateFormat("'G'yyyyMMddHHmmss").format(new Date());
        String kplshSuffix = leftPad(idGenerator.getId("KP-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())) + "", 5, "0");
        String kplsh = kpLshPrefix + kplshSuffix;
        return kplsh;
    }
}
