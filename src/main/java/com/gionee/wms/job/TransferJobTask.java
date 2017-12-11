package com.gionee.wms.job;

import com.gionee.wms.entity.Log;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.service.stock.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static com.gionee.wms.common.WmsConstants.LogType.BIZ_LOG;

/**
 * @author rcw
 */
public class TransferJobTask {

    private static Logger logger = LoggerFactory.getLogger(TransferJobTask.class);

    @Autowired
    private TransferService transferService;
    @Autowired
    private LogService logService;


    /**
     * 任务执行方法入口
     */
    public void execute() {
        logger.info("*****开始推送调拨单到顺丰start*****");
        List<Transfer> list = transferService.getTransfered();
        if (!list.isEmpty()) {
            for (Transfer transfer : list) {
                try {
                    transferService.purchaseOrder(transfer);
                } catch (Exception e) {
                    logService.insertLog(new Log(BIZ_LOG.getCode(), "顺丰入库单：" + transfer.getTransferId(), "异常：" + e.getMessage(), "system", new Date()));
                    logger.error(e.getMessage(), e);
                }
            }
            logger.info("******推送调拨单到顺丰入库任务结束end******,总共" + list.size() + "条记录入库");
        } else {
            logger.info("******推送调拨单到顺丰入库任务结束end******，无需要推送的调拨单信息");
        }
    }
}
