package com.gionee.wms.job;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.service.stock.EInvoiceService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * 10分钟执行一次系在发票文件
 * Created by Pengbin on 2017/3/27.
 */
public class DownloadEInvoiceFileTask {

    private static Logger logger = LoggerFactory.getLogger(EInvoiceJob.class);

    @Autowired
    private EInvoiceService eInvoiceService;
    @Autowired
    private InvoiceInfoService invoiceInfoService;

    public void execute() {
        List<String> orderList = invoiceInfoService.query(Lists.newArrayList(WmsConstants.EInvoiceStatus.WAIT_DOWNLOAD.toString()));
        if (CollectionUtils.isEmpty(orderList)) {
            logger.info("没有需要下载的发票！");
            return;
        }
        logger.info(String.format("自动下载发票%d张，发票号：%s", orderList.size(), Arrays.toString(orderList.toArray())));
        for (String orderCode : orderList) {
            eInvoiceService.downloadInvoicePdfAnd2Img(orderCode);
        }
    }

}
