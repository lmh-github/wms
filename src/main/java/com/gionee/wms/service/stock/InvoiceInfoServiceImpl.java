package com.gionee.wms.service.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants.EInvoiceStatus;
import com.gionee.wms.common.WmsConstants.OrderSourceGionee;
import com.gionee.wms.common.excel.excelimport.util.StringUtil;
import com.gionee.wms.dao.InvoiceInfoDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.PageResult;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.service.common.IDGenerator;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.gionee.wms.common.WmsConstants.EInvoiceStatus.*;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.leftPad;

/**
 * Created by Pengbin on 2017/3/13.
 */
@Component
public class InvoiceInfoServiceImpl implements InvoiceInfoService {

    /** 发票请求唯一流水号生成器，共20位 */
    SimpleDateFormat fpqqlshPrefixFormat = new SimpleDateFormat("'G'yyyyMMddHHmmss");

    @Autowired
    private InvoiceInfoDao invoiceInfoDao;
    @Autowired
    private EInvoiceService eInvoiceService;
    @Autowired
    private IDGenerator idGenerator;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public int saveOrUpdate(InvoiceInfo invoiceInfo, boolean exclude) {
        if (invoiceInfo == null) {
            return 0;
        }
        if (StringUtil.isEmpty(invoiceInfo.getOrderCode())) {
            return 0;
        }
        if (invoiceInfoDao.exist(invoiceInfo.getOrderCode())) {
            if (exclude) {
                return invoiceInfoDao.updateExcludeNull(invoiceInfo);
            } else {
                return invoiceInfoDao.update(invoiceInfo);
            }
        } else {
            return invoiceInfoDao.insert(invoiceInfo);
        }
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public int saveByOrder(SalesOrder order) {
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        invoiceInfo.setOrderCode(order.getOrderCode());
        invoiceInfo.setMobile(defaultString(order.getInvoiceMobile(), order.getMobile()));
        invoiceInfo.setEmail(order.getInvoiceEmail());
        invoiceInfo.setStatus(WAIT_MAKE.toString()); // 未开发票
        invoiceInfo.setInvoiceType("E"); // 电子发票
        invoiceInfo.setOpDate(new Date());
        invoiceInfo.setOpUser(ActionUtils.getLoginName());
        return saveOrUpdate(invoiceInfo, true);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ServiceCtrlMessage cancelOrder(String idOrCode) {
        InvoiceInfo invoiceInfo = invoiceInfoDao.get(idOrCode);
        if (invoiceInfo == null) {
            return new ServiceCtrlMessage(false, "没有相应发票信息！");
        }
        // 如果发票未生成，则直接标记为订单取消
        if (newArrayList(FAILURE.toString(), WAIT_MAKE.toString(), KP_DELAYED.toString()).contains(invoiceInfo.getStatus())) {
            InvoiceInfo toUpinvoiceInfo = new InvoiceInfo();
            toUpinvoiceInfo.setOrderCode(invoiceInfo.getOrderCode());
            toUpinvoiceInfo.setStatus(EInvoiceStatus.ORDER_CANCEL.toString());
            toUpinvoiceInfo.setOpDate(new Date());
            saveOrUpdate(toUpinvoiceInfo, true);

            return new ServiceCtrlMessage(true, "操作成功！");
        } else {
            return eInvoiceService.invalidEIvoice(invoiceInfo.getOrderCode(), false);
        }
    }

    /** {@inheritDoc} */
    @Override
    public PageResult<InvoiceInfo> query(Map<String, Object> paramMap, Page page) {
        paramMap.put("page", page);
        PageResult<InvoiceInfo> pageResult = new PageResult<>();
        int count = invoiceInfoDao.queryCount(paramMap);
        if (count == 0) {
            pageResult.setRows(new ArrayList<InvoiceInfo>(0));
            pageResult.setTotal(0);
        } else {
            page.setTotalRow(count);
            page.calculate();
            pageResult.setRows(invoiceInfoDao.query(paramMap));
            pageResult.setTotal(count);
        }
        return pageResult;
    }

    @Override
    public List<Map<String, Object>> extQuery(Map<String, Object> paramMap) {
        return invoiceInfoDao.extQuery(paramMap);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> query(List<String> statusList) {
        return invoiceInfoDao.queryOrderCodesByStatus(statusList);
    }

    /** {@inheritDoc} */
    @Override
    public InvoiceInfo get(String idOrCode) {
        if (StringUtils.isBlank(idOrCode)) {
            return null;
        }
        return invoiceInfoDao.get(idOrCode);
    }

    /** {@inheritDoc} */
    @Override
    public String createKpLsh(String orderCode) {
        InvoiceInfo invoiceInfo = invoiceInfoDao.get(orderCode);
        if (invoiceInfo.getKpLsh() != null) {
            return invoiceInfo.getKpLsh();
        }
        String kpLshPrefix = fpqqlshPrefixFormat.format(new Date());
        String kplshSuffix = leftPad(idGenerator.getId("KP-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())) + "", 5, "0");
        String kplsh = kpLshPrefix + kplshSuffix;
        InvoiceInfo toUpInvoice = new InvoiceInfo();
        toUpInvoice.setId(invoiceInfo.getId());
        toUpInvoice.setKpLsh(kplsh);
        invoiceInfoDao.updateExcludeNull(toUpInvoice);

        return kplsh;
    }

    /** {@inheritDoc} */
    @Override
    public String createChLsh(String orderCode) {
        InvoiceInfo invoiceInfo = invoiceInfoDao.get(orderCode);
        if (invoiceInfo.getChLsh() != null) {
            return invoiceInfo.getChLsh();
        }
        String chLshPrefix = fpqqlshPrefixFormat.format(new Date());
        String chLshSuffix = leftPad(idGenerator.getId("CH-" + new SimpleDateFormat("yyyy-mm-dd").format(new Date())) + "", 5, "0");
        String chLsh = chLshPrefix + chLshSuffix;
        InvoiceInfo toUpInvoice = new InvoiceInfo();
        toUpInvoice.setId(invoiceInfo.getId());
        toUpInvoice.setChLsh(chLsh);
        invoiceInfoDao.updateExcludeNull(toUpInvoice);

        return chLsh;
    }

    /** {@inheritDoc} */
    @Override
    public boolean exist(String orderCode) {
        return invoiceInfoDao.exist(orderCode);
    }


    /** {@inheritDoc} */
    @Override
    @Transactional
    public boolean successZInvoice(String idOrCode) {
        InvoiceInfo invoiceInfo = invoiceInfoDao.get(idOrCode);
        if (invoiceInfo != null) {
            invoiceInfo.setStatus(EInvoiceStatus.SUCCESS.toString());
            invoiceInfo.setInvoiceType("Z");
            invoiceInfo.setOpUser(ActionUtils.getLoginNameAndDefault());
            invoiceInfo.setOpDate(new Date());

            invoiceInfoDao.updateExcludeNull(invoiceInfo);

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> queryForJob(List<?> orderStatus, List<?> invoiceStatus) {
        return invoiceInfoDao.queryForJob(orderStatus, invoiceStatus);
    }

    /** {@inheritDoc} */
    @Override
    public List<Map<String, String>> exprotQuery(Map<String, Object> paramMap) {
        Map<Object, String> orderSourceMap = Maps.newHashMap();
        for (OrderSourceGionee o : OrderSourceGionee.values()) {
            orderSourceMap.put(o.getCode(), o.getName());
        }

        Map<Object, String> invoiceStatusMap = Maps.newHashMap();
        for (EInvoiceStatus o : EInvoiceStatus.values()) {
            invoiceStatusMap.put(o.toString(), o.getText());
        }

        List<Map<String, String>> mapList = invoiceInfoDao.exprotQuery(paramMap);
        for (Map<String, String> o : mapList) {
            o.put("ORDER_SOURCE", orderSourceMap.get(o.get("ORDER_SOURCE")));
            o.put("STATUS", invoiceStatusMap.get(o.get("STATUS")));
        }

        return mapList;
    }

}
