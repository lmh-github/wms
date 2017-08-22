package com.gionee.wms.service.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants.OrderSourceGionee;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.UcUserDao;
import com.gionee.wms.dao.WorkOrderDao;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.UcUser;
import com.gionee.wms.entity.WorkOrder;
import com.gionee.wms.service.common.IDGenerator;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * Created by Pengbin on 2017/6/6.
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private static final String SEPARATOR = "^_^";

    @Autowired
    private WorkOrderDao workOrderDao;
    @Autowired
    private UcUserDao ucUserDao;
    @Autowired
    private SalesOrderDao salesOrderDao;
    @Autowired
    private IDGenerator idGenerator;

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage save(WorkOrder workOrder) {
        if (isBlank(workOrder.getOrderCode())) {
            return new ServiceCtrlMessage(false, "订单号为空！");
        }
        SalesOrder salesOrder = salesOrderDao.queryOrderByOrderCode(workOrder.getOrderCode());
        if (salesOrder == null) {
            return new ServiceCtrlMessage(false, "未找到订单号【" + workOrder.getOrderCode() + "】相关信息！");
        }
        workOrder.setPlatform(OrderSourceGionee.get(salesOrder.getOrderSource()).getName());

        if (isBlank(workOrder.getWorker())) {
            return new ServiceCtrlMessage(false, "处理人信息为空！");
        }
        UcUser ucUser = ucUserDao.get(null, null, workOrder.getWorker());
        if (ucUser == null) {
            return new ServiceCtrlMessage(false, "处理人【" + workOrder.getWorker() + "】无法找到！");
        }

        Integer seq = idGenerator.getId(WorkOrder.class.getSimpleName() + new SimpleDateFormat(".yyyy-MM").format(new Date()));
        workOrder.setWorkCode(new SimpleDateFormat("yyyyMMdd").format(new Date()) + StringUtils.leftPad(seq.toString(), 6, "0"));
        workOrder.setWorker(ucUser.getUserName());
        workOrder.setCreateTime(new Date());
        workOrder.setStatus("待处理");

        workOrderDao.insert(workOrder);
        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage update(WorkOrder workOrder) {
        if (workOrder.getId() != null && !StringUtils.isEmpty(workOrder.getRemarks())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String remarksStr = simpleDateFormat.format(new Date()) +
                " " +
                ucUserDao.get(null, ActionUtils.getLoginName(), null).getUserName() +
                ":" +
                workOrder.getRemarks();
            workOrder.setRemarks(remarksStr);

            WorkOrder tmp = get(workOrder.getId());
            if (tmp != null && !StringUtils.isEmpty(tmp.getRemarks())) {
                workOrder.setRemarks(tmp.getRemarks() + SEPARATOR + workOrder.getRemarks());
            }
            workOrderDao.update(workOrder);
            return new ServiceCtrlMessage(true, "", workOrder.getRemarks());
        } else {
            workOrderDao.update(workOrder);
            return new ServiceCtrlMessage(true, "");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage accept(Long id) {
        WorkOrder workOrder = workOrderDao.get(id);
        if ("待处理".equals(workOrder.getStatus())) {
            workOrder.setStatus("跟进中");
            workOrder.setAcceptTime(new Date());
            workOrderDao.update(workOrder);
        } else {
            return new ServiceCtrlMessage(false, "此工单已经接收或者处理完成！");
        }
        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage up(WorkOrder workOrder) {
        Long id = workOrder.getId();
        WorkOrder poWorkOrder = workOrderDao.get(id);
        if (!"跟进中".equals(poWorkOrder.getStatus())) {
            return new ServiceCtrlMessage(false, "此工单已无法执行升级操作！");
        }
        UcUser ucUser = ucUserDao.get(null, null, workOrder.getUper());
        if (ucUser == null) {
            return new ServiceCtrlMessage(false, "升级处理人【" + workOrder.getUper() + "】无法找到！");
        }

        poWorkOrder.setUper(ucUser.getUserName());
        poWorkOrder.setUpTime(new Date());
        poWorkOrder.setLv(workOrder.getLv());
        poWorkOrder.setSuggest(workOrder.getSuggest());

        workOrderDao.update(poWorkOrder);

        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage finish(WorkOrder workOrder) {
        Long id = workOrder.getId();
        WorkOrder poWorkOrder = workOrderDao.get(id);
        if (!"跟进中".equals(poWorkOrder.getStatus())) {
            return new ServiceCtrlMessage(false, "此工单已无法执行完成操作！");
        }

        poWorkOrder.setResultMsg(workOrder.getResultMsg());
        poWorkOrder.setStatus("已完成");
        poWorkOrder.setLastTime(new Date());

        workOrderDao.update(poWorkOrder);

        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage cancel(WorkOrder workOrder) {
        Long id = workOrder.getId();
        WorkOrder poWorkOrder = workOrderDao.get(id);
        if (!Arrays.asList("待处理", "跟进中").contains(poWorkOrder.getStatus())) {
            return new ServiceCtrlMessage(false, "此工单已无法执行作废操作！");
        }

        poWorkOrder.setResultMsg(workOrder.getResultMsg());
        poWorkOrder.setStatus("已作废");
        poWorkOrder.setLastTime(new Date());

        workOrderDao.update(poWorkOrder);

        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkOrder> query(Map<String, Object> params) {
        return workOrderDao.query(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int queryCount(Map<String, Object> params) {
        return workOrderDao.queryCount(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int queryToDoCount(Map<String, Object> params) {
        params.put("statusList", Lists.newArrayList("待处理", "跟进中"));
        return workOrderDao.queryToDoCount(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkOrder get(Long id) {
        return workOrderDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, String>> exportQuery(Map<String, Object> params) {
        return workOrderDao.exportQuery(params);
    }
}
