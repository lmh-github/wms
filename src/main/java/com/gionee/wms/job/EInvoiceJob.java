package com.gionee.wms.job;

import com.gionee.wms.service.stock.EInvoiceService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * 定时扫描未发票相关任务
 * Created by Pengbin on 2017/3/24.
 */
public class EInvoiceJob {

    private static Logger logger = LoggerFactory.getLogger(EInvoiceJob.class);

    @Autowired
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;
    @Autowired
    private EInvoiceService eInvoiceService;
    @Autowired
    private InvoiceInfoService invoiceInfoService;

    public void execute() {
        {
            invoiceInfoService.autoDoNothingInvoiceOrder(); // 定时扫描并且更改不需要开票的订单
        }

        {
            List<String> orderList = invoiceInfoService.queryToMakeInvoiceOrder();
            if (isEmpty(orderList)) {
                logger.info("定时开票：没有扫描到需要开票的订单！");
                return;
            }
            logger.info(String.format("自动开具延期发票%d张，发票号：%s", orderList.size(), Arrays.toString(orderList.toArray())));
            for (final String orderCode : orderList) {
                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            eInvoiceService.makeEInvoice(orderCode);
                        } catch (Exception e) {
                            logger.error("自动开具发票任务异常！", e);
                        }
                    }
                });
            }
        }

        {
            List<String> orderList = invoiceInfoService.queryToCancelInvoiceOrder();
            if (isEmpty(orderList)) {
                logger.info("定时冲红：没有扫描到需要冲红的订单！");
                return;
            }
            logger.info(String.format("自动冲红延期发票%d张，发票号：%s", orderList.size(), Arrays.toString(orderList.toArray())));
            for (final String orderCode : orderList) {
                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            invoiceInfoService.cancelOrder(orderCode);
                        } catch (Exception e) {
                            logger.error("自动冲红发票任务异常！", e);
                        }
                    }
                });
            }
        }

    }
}
