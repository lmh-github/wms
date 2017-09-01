package com.gionee.wms.service.stock;

import com.gionee.wms.common.*;
import com.gionee.wms.common.WmsConstants.EInvoiceStatus;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dto.*;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.entity.Log;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.service.common.MailService;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.gionee.wms.common.ActionUtils.getLoginNameAndDefault;
import static com.gionee.wms.common.EInvoiceConfig.MAIL_SUBJECT;
import static com.gionee.wms.common.WmsConstants.EInvoiceStatus.*;
import static com.gionee.wms.common.WmsConstants.LogType.BIZ_LOG;
import static com.gionee.wms.common.WmsConstants.OrderStatus.*;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.indexOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 电子发票
 * Created by Pengbin on 2017/2/20.
 */
@Component
public class EInvoiceServiceImpl implements EInvoiceService {

    /**
     * 开票接口成功状态码
     */
    private static final String SUCCESS_CODE = "0000";
    private static final Pattern KPRQ_PATTERN = Pattern.compile("\\d{14}");
    private static final SimpleDateFormat KPRQ_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Logger logger = LoggerFactory.getLogger(EInvoiceServiceImpl.class);

    @Autowired
    private LogService logService;
    @Autowired
    private SalesOrderDao salesOrderDao;
    @Autowired
    private InvoiceInfoService invoiceInfoSerivce;
    @Autowired
    @Qualifier("KP")
    private EInvoiceBuildService kpService;
    @Autowired
    @Qualifier("CH")
    private EInvoiceBuildService chService;
    @Autowired
    @Qualifier("XZ")
    private EInvoiceBuildService xzService;
    @Autowired
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;
    @Autowired
    private MailService mailService;
    @Autowired
    private EwmGenerateService ewmGenerateService;


    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage<KpContentResp> makeEInvoice(String orderCode) {
        InvoiceInfo invoiceInfo1 = invoiceInfoSerivce.get(orderCode);
        if (invoiceInfo1 != null) {
            if (SUCCESS.toString().equals(invoiceInfo1.getStatus())) {
                return new ServiceCtrlMessage(false, "已经开过电子发票！", invoiceInfo1.getJsonData());
            }
            if (indexOf(new String[]{WAIT_MAKE.toString(), FAILURE.toString(), KP_DELAYED.toString()}, invoiceInfo1.getStatus()) == -1) {
                return new ServiceCtrlMessage(false, "此状态无法再从新开具电子发票！", invoiceInfo1.getJsonData());
            }
        }

        SalesOrder salesOrder = salesOrderDao.queryOrderByOrderCode(orderCode);
        if (salesOrder == null) {
            return new ServiceCtrlMessage(false, "订单号不存在！");
        }
        if (salesOrder.getInvoiceAmount().doubleValue() == 0) {
            return new ServiceCtrlMessage(false, "订单金额为0，不可以开发票！");
        }
        if (doLastDayOfMonth(orderCode, EInvoiceStatus.KP_DELAYED)) {
            return new ServiceCtrlMessage<>(false, "月末最后一天自动延期发票打印！");
        }
        List<SalesOrderGoods> salesOrderGoodses = salesOrderDao.queryGoodsListByOrderId(salesOrder.getId());

        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setOrderCode(orderCode);
        try {
            KpInterface responseKpInterface = action(createRequestInterface(kpService, salesOrder, salesOrderGoodses));
            ReturnStateInfo returnStateInfo = responseKpInterface.getReturnStateInfo();
            invoiceInfo.setReturnCode(returnStateInfo.getReturnCode());
            if (SUCCESS_CODE.equals(returnStateInfo.getReturnCode())) {
                KpContentResp contentResp = XmlHelper1.toBean(responseKpInterface.getData().getContent(), KpContentResp.class);

                invoiceInfo.setKprq(parseDate(contentResp.getKprq()));
                invoiceInfo.setFpDm(contentResp.getFpDm());
                invoiceInfo.setFpHm(contentResp.getFpHm());
                invoiceInfo.setInvoiceType("E");
                invoiceInfo.setStatus(WAIT_DOWNLOAD.toString());
                invoiceInfo.setOpDate(new Date());
                invoiceInfo.setOpUser(getLoginNameAndDefault());
                invoiceInfo.setJsonData(new XMLSerializer().read(responseKpInterface.getData().getContent()).toString());
                invoiceInfo.setPdfUrl(contentResp.getPdfUrl());

                invoiceInfoSerivce.saveOrUpdate(invoiceInfo, true);

                // 开票成功去生成二维码
                ewmGenerateService.ewmGenerate(orderCode);

                // 异步下载保存发票文件，并且转换成图片文件存储
                // 预计5分钟后才可以生成发票签章，需要延迟下载
                downFileAnd2Img(orderCode, contentResp.getPdfUrl());
                return new ServiceCtrlMessage<>(true, null, null);
            }

            invoiceInfo.setStatus(FAILURE.toString()); // 开票失败
            invoiceInfo.setJsonData(returnStateInfo.getReturnMessage());
            invoiceInfoSerivce.saveOrUpdate(invoiceInfo, true);
            logService.insertLog(new Log(BIZ_LOG.getCode(), orderCode, String.format("retcode:%s,returnMessage:%s", returnStateInfo.getReturnCode(), returnStateInfo.getReturnMessage()), "system", new Date()));
            return new ServiceCtrlMessage(false, String.format("%s:%s", returnStateInfo.getReturnCode(), returnStateInfo.getReturnMessage()));
        } catch (Exception e) {
            logger.error("开票异常！", e);

            invoiceInfo.setStatus(FAILURE.toString()); // 开票失败
            invoiceInfo.setJsonData("程序异常：" + e.getMessage());
            invoiceInfoSerivce.saveOrUpdate(invoiceInfo, true);

            logService.insertLog(new Log(BIZ_LOG.getCode(), orderCode, e.getMessage(), getLoginNameAndDefault(), new Date()));
            return new ServiceCtrlMessage(false, e.getMessage());
        }
    }


    /**
     * 月末最后一天处理
     *
     * @param orderCode 订单号
     * @param status    开票延迟|冲红延迟
     * @return
     */
    private boolean doLastDayOfMonth(String orderCode, EInvoiceStatus status) {
        Calendar calendar = Calendar.getInstance();
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (nowDay == lastDay) {
            InvoiceInfo invoiceInfo = new InvoiceInfo();
            invoiceInfo.setOrderCode(orderCode);
            invoiceInfo.setStatus(status.toString());
            invoiceInfoSerivce.saveOrUpdate(invoiceInfo, true);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage<XzContentResp> downEInvoice(String orderCode) {
        SalesOrder salesOrder = salesOrderDao.queryOrderByOrderCode(orderCode);
        if (salesOrder == null) {
            return new ServiceCtrlMessage(false, "订单号不存在！");
        }
        try {
            KpInterface requestInterface = createRequestInterface(xzService, salesOrder, null);
            KpInterface responseKpInterface = action(requestInterface);
            ReturnStateInfo returnStateInfo = responseKpInterface.getReturnStateInfo();
            if (SUCCESS_CODE.equals(returnStateInfo.getReturnCode())) {
                String content = AesUtil.decrypt(responseKpInterface.getData().getContent());
                XzContentResp contentResp = XmlHelper1.toBean(content, XzContentResp.class);
                return new ServiceCtrlMessage<>(true, null, contentResp);
            }
            return new ServiceCtrlMessage(false, String.format("%s:%s", returnStateInfo.getReturnCode(), returnStateInfo.getReturnMessage()));
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage invalidEIvoice(String orderCode, boolean forced) {
        InvoiceInfo invoiceInfo1 = invoiceInfoSerivce.get(orderCode);
        if (invoiceInfo1 == null) {
            return new ServiceCtrlMessage(false, "未找到相应的发票信息！");
        }
        if (RED.toString().equals(invoiceInfo1.getStatus())) {
            return new ServiceCtrlMessage(false, "发票已经冲红！");
        }
        if (!asList(SUCCESS.toString(), WAIT_DOWNLOAD.toString()).contains(invoiceInfo1.getStatus())) {
            return new ServiceCtrlMessage(false, "发票未开具，不能做冲红处理！", invoiceInfo1.getJsonData());
        }
        SalesOrder salesOrder = salesOrderDao.queryOrderByOrderCode(orderCode);
        if (salesOrder == null) {
            return new ServiceCtrlMessage(false, "订单号不存在！");
        }
        if (!forced) {
            if (indexOf(new int[]{CANCELED.getCode(), BACKED.getCode(), REFUSED.getCode()}, salesOrder.getOrderStatus()) == -1) {
                return new ServiceCtrlMessage(false, "订单未取消或拒收，无法冲红！");
            }
        }
        if (doLastDayOfMonth(orderCode, EInvoiceStatus.CH_DELAYED)) {
            return new ServiceCtrlMessage(false, "月末最后一天，自动延迟冲红！");
        }

        List<SalesOrderGoods> salesOrderGoodses = salesOrderDao.queryGoodsListByOrderId(salesOrder.getId());
        try {
            KpInterface responseKpInterface = action(createRequestInterface(chService, salesOrder, salesOrderGoodses));
            ReturnStateInfo returnStateInfo = responseKpInterface.getReturnStateInfo();
            if (SUCCESS_CODE.equals(returnStateInfo.getReturnCode())) {
                KpContentResp contentResp = XmlHelper1.toBean(responseKpInterface.getData().getContent(), KpContentResp.class);

                InvoiceInfo invoiceInfo = invoiceInfoSerivce.get(orderCode);
                invoiceInfo.setReturnCode(returnStateInfo.getReturnCode());
                invoiceInfo.setStatus(RED.toString());
                invoiceInfo.setPdfUrl(null);
                invoiceInfo.setEwmUrl(null);
                invoiceInfo.setPreviewImgUrl(null);
                invoiceInfo.setOpDate(new Date());
                invoiceInfo.setOpUser(getLoginNameAndDefault());
                invoiceInfo.setJsonData(new XMLSerializer().read(responseKpInterface.getData().getContent()).toString());

                invoiceInfoSerivce.saveOrUpdate(invoiceInfo, false);
                return new ServiceCtrlMessage<>(true, null, contentResp);
            }

            return new ServiceCtrlMessage(false, String.format("%s:%s", returnStateInfo.getReturnCode(), returnStateInfo.getReturnMessage()));
        } catch (Exception e) {
            logger.error("发票冲红异常！", e);
            logService.insertLog(new Log(BIZ_LOG.getCode(), orderCode, e.getMessage(), getLoginNameAndDefault(), new Date()));
            return new ServiceCtrlMessage(false, e.getMessage());
        }
    }

    /**
     * 创建开票请求
     *
     * @param eInvoice EInvoiceBuildService
     * @param order    订单
     * @param goods    订单商品
     * @return KpInterface
     * @throws Exception Exception
     */
    private KpInterface createRequestInterface(EInvoiceBuildService eInvoice, SalesOrder order, List<SalesOrderGoods> goods) throws Exception {
        GlobalInfo globalInfo = new GlobalInfo();
        globalInfo.setTerminalCode("0");
        globalInfo.setAppId(EInvoiceConfig.E_APP_ID);
        globalInfo.setVersion("2.0");
        globalInfo.setInterfaceCode(eInvoice.getInterfaceCode()); // 接口编码
        globalInfo.setUserName(EInvoiceConfig.E_USER_NAME); // 平台编码
        globalInfo.setPassWord(EInvoiceConfig.E_PASSWORD); // 10 位随机数+Base64({（ 10 位随机数+注册码）MD5})
        globalInfo.setTaxpayerId(EInvoiceConfig.E_TAX_PAYER_ID); // 纳税人识别号
        globalInfo.setAuthorizationCode(EInvoiceConfig.E_AUTHORIZATION_CODE); // 接入系统平台授权码（ 由平台提供）
        globalInfo.setRequestCode(EInvoiceConfig.E_REQUEST_CODE); // 数据交换请求发起方代码
        globalInfo.setRequestTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss SSS").format(new Date()));
        globalInfo.setResponseCode(EInvoiceConfig.E_RESPONSE_CODE); // 数据交换请求接受方代码
        globalInfo.setDataExchangeId("P0000001ECXML.FPKJ.BC.E_INV000000001"); // 数据交换流水号（ 唯一） requestCode+8 位日期(YYYYMMDD)+9 位序列号

        DataDescription dataDescription = new DataDescription();
        dataDescription.setZipCode("0"); // 非压缩
        dataDescription.setEncryptCode("2"); // CA加密
        dataDescription.setCodeType("CA"); // CA加密

        Map<String, Object> contentMap = eInvoice.buildContent(order, goods);
        String contentSource = TemplateHelper.generate(contentMap, eInvoice.getTemplate());
        logger.info(eInvoice.toString() + " EInvoice contentSource:\n" + contentSource);
        String content = AesUtil.encrypt(contentSource);

        Data data = new Data();
        data.setDataDescription(dataDescription);
        data.setContent(content);

        KpInterface request = new KpInterface();
        request.setGlobalInfo(globalInfo);
        request.setData(data);

        return request;
    }

    /**
     * 请求航信发票接口
     *
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
        String info = new HttpRequestor().doPost(EInvoiceConfig.E_INVOICE_API_URL, parameterMap);
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
        }

        return kpInterface;
    }

    /**
     * 日期格式转换
     *
     * @param dateText
     * @return
     */
    private Date parseDate(String dateText) {
        try {
            if (isBlank(dateText)) {
                return null;
            }
            if (KPRQ_PATTERN.matcher(dateText).matches()) {
                return KPRQ_FORMAT.parse(dateText);
            }
            return null;
        } catch (ParseException e) {
            // ignore
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage downloadInvoicePdfAnd2Img(String orderCode) {
        try {
            InvoiceInfo invoiceInfo = invoiceInfoSerivce.get(orderCode);
            if (Lists.newArrayList(FAILURE.toString(), RED.toString(), ORDER_CANCEL.toString()).contains(invoiceInfo.getStatus())) {
                return new ServiceCtrlMessage(false, "此状态发票不能再进行取票操作！");
            }
            if (isBlank(invoiceInfo.getPdfUrl())) {
                return new ServiceCtrlMessage(false, "发票文件未生成，无法取票！");
            }
            if ((new Date().getTime() - invoiceInfo.getOpDate().getTime()) > 1000 * 60 * 10) { // 发票生成十分钟内，等待签章生成后再下载
                downFileAnd2Img(orderCode, invoiceInfo.getPdfUrl());
                return new ServiceCtrlMessage(true, "操作成功！");
            }
            return new ServiceCtrlMessage(false, "等待签章生成，延迟下载！");
        } catch (Exception e) {
            logger.error("下载发票文件出现异常！", e);
            return new ServiceCtrlMessage(false, "下载发票文件出现异常：" + e.getMessage());
        }
    }

    /**
     * 异步下载PDF文件并转换成IMG图片格式，保存到部署目录
     *
     * @param orderCode 订单号
     * @param pdfUrl    PDF下载URL
     */
    public void downFileAnd2Img(final String orderCode, final String pdfUrl) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedInputStream bufferedInputStream = null; // 用于IO复用
                    InputStream inputStream = null;
                    String pdf = orderCode + ".pdf";
                    FileOutputStream pdfOutputStream = new FileOutputStream(new File(EInvoiceDir.PDF.getSavePath(pdf)));
                    String img = orderCode + ".jpg";
                    FileOutputStream imgOutputStream = new FileOutputStream(new File(EInvoiceDir.IMG.getSavePath(img)));
                    try {
                        inputStream = HttpClientUtil.httpRetryGet(pdfUrl).getContent();
                        bufferedInputStream = new BufferedInputStream(inputStream);
                        bufferedInputStream.mark(Integer.MAX_VALUE); // 最大值，标记整个流可以复用
                        IOUtils.copy(bufferedInputStream, pdfOutputStream);
                        bufferedInputStream.reset();
                        PdfUtil.pdf2Img(bufferedInputStream, imgOutputStream, 3.0f);

                        InvoiceInfo invoiceInfo = new InvoiceInfo();
                        invoiceInfo.setOrderCode(orderCode);
                        invoiceInfo.setStatus(SUCCESS.toString());
                        invoiceInfo.setPreviewImgUrl(EInvoiceDir.IMG.getResourceUrl(img));
                        invoiceInfo.setPdfUrl(EInvoiceDir.PDF.getResourceUrl(pdf));
                        invoiceInfoSerivce.saveOrUpdate(invoiceInfo, true);

                        // 发送邮件
                        InvoiceInfo poInvoiceInfo = invoiceInfoSerivce.get(orderCode);
                        if (isNotBlank(poInvoiceInfo.getEmail()) && new EmailValidator().isValid(poInvoiceInfo.getEmail(), null)) {
                            logger.info("Send einvoice Email to: " + poInvoiceInfo.getEmail());
                            MimeMessageHelper messageHelper = mailService.createMimeMessageHelper(false);
                            messageHelper.setSubject(MAIL_SUBJECT);
                            messageHelper.setTo(poInvoiceInfo.getEmail());
                            messageHelper.setText(TemplateHelper.generate(new HashMap<String, Object>(0), "invoice-mail.html"), true);// 以HTML格式发送
                            mailService.send(messageHelper);
                        }
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly(bufferedInputStream);
                        IOUtils.closeQuietly(pdfOutputStream);
                        IOUtils.closeQuietly(imgOutputStream);
                    }
                } catch (Exception e) {
                    logger.error("下载和转换发票PDF出现异常！\n" + e.getMessage(), e);

                    logService.insertLog(new Log(BIZ_LOG.getCode(), "下载发票文件:" + orderCode, e.getMessage(), getLoginNameAndDefault(), new Date()));
                }
            }
        });
    }

}
