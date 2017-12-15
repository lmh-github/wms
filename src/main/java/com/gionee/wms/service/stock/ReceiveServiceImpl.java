package com.gionee.wms.service.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.LinkMapUtils;
import com.gionee.wms.common.MyCollectionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.*;
import com.gionee.wms.dao.*;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.*;
import com.gionee.wms.entity.RmaSalesOrder.RmaSalesOrderGoods;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.gionee.wms.web.client.OrderCenterClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("receiveService")
public class ReceiveServiceImpl extends CommonServiceImpl implements ReceiveService {
    private static Logger logger = LoggerFactory.getLogger(ReceiveServiceImpl.class);
    @Autowired
    public SalesOrderLogDao salesOrderLogDao = null;
    @Autowired
    private ReceiveDao receiveDao;
    @Autowired
    private PurPreRecvDao purPreRecvDao;
    @Autowired
    private IndivDao indivDao;
    @Autowired
    private SalesOrderDao orderDao;
    @Autowired
    private DeliveryDao deliveryDao;
    @Autowired
    private StockService stockService;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private OrderCenterClient orderCenterClient;
    @Autowired
    private SalesOrderService salesOrderService;
    @Autowired
    private WaresDao waresDao;

    @Override
    public void addPurchaseReceiveByPreRecv(PurPreRecv purPreRecv) {
        if (purPreRecv == null) {
            throw new ServiceException("参数错误");
        }
        if (PurchaseOrderStatus.UNRECEIVED.getCode() != purPreRecv.getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }
        try {
            // ---创建收货单---//
            Receive receive = new Receive();
            receive.setReceiveCode(getBizCode(PURCHASE_IN));
            receive.setReceiveType(ReceiveType.PURCHASE.getCode());
            receive.setReceiveMode(ReceiveMode.AUTO.getCode());
            receive.setOriginalCode(purPreRecv.getPurchaseCode());
            receive.setWarehouseId(purPreRecv.getWarehouseId());
            receive.setWarehouseName(purPreRecv.getWarehouseName());
            receive.setSupplierId(purPreRecv.getSupplierId());
            receive.setSupplierName(purPreRecv.getSupplierName());
            receive.setHandlingStatus(ReceiveStatus.RECEIVING.getCode());
            receive.setPreparedBy(ActionUtils.getLoginName());
            receive.setPreparedTime(new Date());
            receiveDao.addReceive(receive);

            // ---添加收货商品---//
            List<PurPreRecvGoods> purPreRecvGoodsList = purPreRecvDao.queryGoodsListByPreRecvId(purPreRecv.getId());
            List<ReceiveGoods> receiveGoodsList = Lists.newArrayList();
            for (PurPreRecvGoods purPreRecvGoods : purPreRecvGoodsList) {
                ReceiveGoods receiveGoods = new ReceiveGoods();
                receiveGoods.setSkuId(purPreRecvGoods.getSkuId());
                receiveGoods.setSkuCode(purPreRecvGoods.getSkuCode());
                receiveGoods.setSkuName(purPreRecvGoods.getSkuName());
                receiveGoods.setQuantity(purPreRecvGoods.getQuantity());
                receiveGoods.setMeasureUnit(purPreRecvGoods.getMeasureUnit());
                receiveGoods.setProductBatchNo(purPreRecvGoods.getProductBatchNo());
                receiveGoods.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());
                receiveGoods.setEnabled(WmsConstants.ENABLED_FALSE);
                receiveGoods.setIndivEnabled(purPreRecvGoods.getIndivEnabled());
                receiveGoods.setIndivFinished(purPreRecvGoods.getIndivEnabled() == 1 ? 1 : 0);
                receiveGoods.setReceive(receive);
                receiveGoodsList.add(receiveGoods);
            }
            receiveDao.addGoodsList(receiveGoodsList);

            // ---添加收货商品个体及流转信息--- //
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("flowType", IndivFlowType.PUR_PRE_RECV.getCode());
            criteria.put("flowId", purPreRecv.getId());
            List<IndivFlow> IndivFlowList = indivDao.queryIndivFlowList(criteria);
            List<IndivFlow> preRecvIndivFlowList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(IndivFlowList)) {
                for (IndivFlow indivFlow : IndivFlowList) {
                    if (StringUtils.isBlank(indivFlow.getIndivCode())) {
                        continue;
                    }
                    preRecvIndivFlowList.add(indivFlow);
                }
            }

            if (CollectionUtils.isNotEmpty(preRecvIndivFlowList)) {
                // 检测预收商品个体合法性
                List<String> indivCodesList = MyCollectionUtils.collectionElementPropertyToList(preRecvIndivFlowList, "indivCode");
                // 过滤掉重复的串号
                Set<String> indivCodes = new HashSet<String>(indivCodesList);
                indivCodesList = new ArrayList<String>(indivCodes);
                List<Indiv> existingIndivList = indivDao.queryIndivListByCodes(indivCodesList);
                List<String> handlingIndivCodes = Lists.newArrayList();
                List<String> notAdd = new ArrayList<String>(); // 不需要插入到个体串号表中的串号
                for (Indiv indiv : existingIndivList) {
                    if (IndivStockStatus.STOCKIN_HANDLING.getCode() == indiv.getStockStatus()) {
                        handlingIndivCodes.add("商品编码" + indiv.getIndivCode() + "已被收货单" + indiv.getInCode() + "绑定");
                    } else {
                        notAdd.add(indiv.getIndivCode());
                    }
                }
                // if (CollectionUtils.isNotEmpty(handlingIndivCodes)) {
                // throw new ServiceException(StringUtils.join(handlingIndivCodes, ","));
                // }

                // 构建收货商品个体对象及个体流转信息对象
                List<Indiv> newIndivList = Lists.newArrayList();
                List<ReceiveGoods> savedGoodsList = receiveDao.queryGoodsListByReceiveId(receive.getId());
                Map<String, ReceiveGoods> receiveGoodsMap = Maps.newHashMap();
                for (ReceiveGoods goods : savedGoodsList) {
                    receiveGoodsMap.put(goods.getSkuId() + goods.getProductBatchNo(), goods);
                }

                Set<String> hasAddCodes = new HashSet<String>();
                for (IndivFlow flow : preRecvIndivFlowList) {
                    if (receiveGoodsMap.containsKey(flow.getSkuId() + flow.getProductBatchNo())) {
                        flow.setFlowType(IndivFlowType.IN_PURCHASE.getCode());
                        flow.setFlowId(receive.getId());
                        flow.setFlowCode(receive.getReceiveCode());
                        flow.setFlowGoodsId(receiveGoodsMap.get(flow.getSkuId() + flow.getProductBatchNo()).getId());
                        flow.setFlowTime(null);
                        flow.setEnabled(WmsConstants.ENABLED_FALSE);
                        // 对于不在异常列表及不需要插入的列表中的串号，进行插入操作
                        if (!handlingIndivCodes.contains(flow.getIndivCode())) {
                            // 对于维修等重复入库情况，更新个体状态
                            if (notAdd.contains(flow.getIndivCode())) {
                                Indiv indiv = new Indiv();
                                indiv.setIndivCode(flow.getIndivCode());
                                indiv.setInId(receive.getId());
                                indiv.setInCode(receive.getReceiveCode());
                                indiv.setProductBatchNo(flow.getProductBatchNo());
                                indiv.setRmaCount(0);
                                indiv.setWaresStatus(flow.getWaresStatus());
                                indiv.setStockStatus(IndivStockStatus.STOCKIN_HANDLING.getCode());
                                indiv.setPushStatus(RemoteCallStatus.PENDING.getCode());
                                indiv.setPushCount(0);
                                indiv.setInTime(new Date());
                                indiv.setCaseCode(flow.getCaseCode());// 箱号
                                indiv.setRemark("预收单中的该串号在个体表中已经存在，现将其数据库中的库存状态更新为入库中");
                                indiv.setCaseCode(flow.getCaseCode());
                                indivDao.updateIndiv1(indiv);
                            }
                            // 如果不是重复入库，则需要插入个体数据
                            else {
                                // 防止个体重复添加
                                if (hasAddCodes.add(flow.getIndivCode())) {
                                    // 防止重复个体编码入库
                                    Indiv indiv = new Indiv();
                                    indiv.setIndivCode(flow.getIndivCode());
                                    indiv.setSkuId(flow.getSkuId());
                                    indiv.setSkuCode(flow.getSkuCode());
                                    indiv.setSkuName(flow.getSkuName());
                                    indiv.setWarehouseId(receive.getWarehouseId());
                                    indiv.setWarehouseName(receive.getWarehouseName());
                                    indiv.setMeasureUnit(flow.getMeasureUnit());
                                    indiv.setInId(receive.getId());
                                    indiv.setInCode(receive.getReceiveCode());
                                    indiv.setProductBatchNo(flow.getProductBatchNo());
                                    indiv.setRmaCount(0);
                                    indiv.setWaresStatus(flow.getWaresStatus());
                                    indiv.setStockStatus(IndivStockStatus.STOCKIN_HANDLING.getCode());
                                    indiv.setPushStatus(RemoteCallStatus.PENDING.getCode());
                                    indiv.setPushCount(0);
                                    indiv.setInTime(new Date());
                                    indiv.setCaseCode(flow.getCaseCode());// 箱号
                                    newIndivList.add(indiv);
                                }
                            }
                        }
                    }
                }
                // 保存收货商品个体流转信息
                indivDao.addIndivFlows(preRecvIndivFlowList);
                if (!newIndivList.isEmpty()) indivDao.addIndivs(newIndivList);
            }

            // ---更新采购单状态---//
            purPreRecv.setHandlingStatus(PurchaseOrderStatus.RECEIVING.getCode());
            purPreRecv.setHandledTime(new Date());
            purPreRecv.setHandledBy(ActionUtils.getLoginName());
            purPreRecv.setReceiveId(receive.getId());
            purPreRecv.setReceiveCode(receive.getReceiveCode());
            purPreRecvDao.updatePurPreRecv(purPreRecv);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addShortcutReceive(Receive receive) throws ServiceException {
        receive.setReceiveCode(getBizCode(SHORTCUT_IN));
        if (null == receive.getReceiveType()) {
            receive.setReceiveType(ReceiveType.PURCHASE.getCode());
        }
        receive.setReceiveMode(ReceiveMode.MANUAL.getCode());
        receive.setHandlingStatus(ReceiveStatus.RECEIVING.getCode());
        receive.setPreparedBy(ActionUtils.getLoginName());
        receive.setPreparedTime(new Date());
        receive.setOriginalCode(getBizCode(PURCHASE_NUM));
        try {
            if (receiveDao.addReceive(receive) == 0) {
                throw new ServiceException("收货单不能重复添加");
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addShortcutRecvGoodsWithIndivList(ReceiveGoods goods, List<IndivFlow> indivList) {
        // 效验录入的商品个体信息
        if (CollectionUtils.isEmpty(indivList)) {
            throw new ServiceException("商品编码为空");
        }
        if (indivList.size() != goods.getQuantity()) {
            throw new ServiceException("输入商品编码数目与商品数量不符");
        }
        if (ReceiveStatus.RECEIVING.getCode() != goods.getReceive().getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }
        Map<String, IndivFlow> currentIndivMap = Maps.newHashMap();
        for (IndivFlow indivFlow : indivList) {
            currentIndivMap.put(indivFlow.getIndivCode(), indivFlow);
        }
        if (currentIndivMap.size() < indivList.size()) {
            throw new ServiceException("录入的商品编码存在重复");
        }

        try {
            // 检测商品个体合法性
            List<Indiv> existingIndivList = indivDao.queryIndivListByCodes(Lists.newArrayList(currentIndivMap.keySet()));
            List<String> handlingIndivCodes = Lists.newArrayList();
            List<String> existingIndivCodes = Lists.newArrayList();
            for (Indiv indiv : existingIndivList) {
                if (IndivStockStatus.STOCKIN_HANDLING.getCode() == indiv.getStockStatus()) {
                    handlingIndivCodes.add("商品编码" + indiv.getIndivCode() + "已被收货单" + indiv.getInCode() + "绑定");
                } else {
                    existingIndivCodes.add("商品编号" + indiv.getIndivCode() + "重复入库");
                }
            }
            handlingIndivCodes.addAll(existingIndivCodes);
            if (CollectionUtils.isNotEmpty(handlingIndivCodes)) {
                throw new ServiceException(StringUtils.join(handlingIndivCodes, ","));
            }

            // 添加商品信息
            goods.setIndivFinished(WmsConstants.ENABLED_TRUE);
            goods.setEnabled(WmsConstants.ENABLED_FALSE);
            receiveDao.addGoods(goods);

            // 批量添加新的商品个体及流转信息
            List<Indiv> newIndivList = Lists.newArrayList();
            List<IndivFlow> newIndivFlowList = Lists.newArrayList();
            for (String newIndivCode : currentIndivMap.keySet()) {
                Indiv indiv = new Indiv();
                indiv.setIndivCode(newIndivCode);
                indiv.setSkuId(goods.getSkuId());
                indiv.setSkuCode(goods.getSkuCode());
                indiv.setSkuName(goods.getSkuName());
                indiv.setWarehouseId(goods.getReceive().getWarehouseId());
                indiv.setWarehouseName(goods.getReceive().getWarehouseName());
                indiv.setMeasureUnit(goods.getMeasureUnit());
                indiv.setInId(goods.getReceive().getId());
                indiv.setInCode(goods.getReceive().getReceiveCode());
                indiv.setInTime(new Date());
                indiv.setProductBatchNo(goods.getProductBatchNo());
                indiv.setRmaCount(0);
                indiv.setWaresStatus(goods.getWaresStatus());
                indiv.setStockStatus(IndivStockStatus.STOCKIN_HANDLING.getCode());
                indiv.setPushStatus(RemoteCallStatus.PENDING.getCode());
                indiv.setPushCount(0);
                newIndivList.add(indiv);
                IndivFlow indivFlow = new IndivFlow();
                indivFlow.setIndivCode(newIndivCode);
                indivFlow.setSkuId(goods.getSkuId());
                indivFlow.setSkuCode(goods.getSkuCode());
                indivFlow.setSkuName(goods.getSkuName());
                indivFlow.setWarehouseId(goods.getReceive().getWarehouseId());
                indivFlow.setWarehouseName(goods.getReceive().getWarehouseName());
                indivFlow.setFlowType(IndivFlowType.IN_PURCHASE.getCode());
                indivFlow.setFlowId(goods.getReceive().getId());
                indivFlow.setFlowCode(goods.getReceive().getReceiveCode());
                indivFlow.setFlowGoodsId(goods.getId());
                indivFlow.setWaresStatus(goods.getWaresStatus());
                indivFlow.setMeasureUnit(goods.getMeasureUnit());
                indivFlow.setProductBatchNo(goods.getProductBatchNo());
                indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
                newIndivFlowList.add(indivFlow);
            }
            indivDao.addIndivs(newIndivList);
            indivDao.addIndivFlows(newIndivFlowList);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addShortcutRecvGoodsWithIndivListRMA(ReceiveGoods goods, List<IndivFlow> indivList) {
        // 效验录入的商品个体信息
        if (CollectionUtils.isEmpty(indivList)) {
            throw new ServiceException("商品编码为空");
        }
        if (indivList.size() != goods.getQuantity()) {
            throw new ServiceException("输入商品编码数目与商品数量不符");
        }
        if (ReceiveStatus.RECEIVING.getCode() != goods.getReceive().getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }
        Map<String, IndivFlow> currentIndivMap = Maps.newHashMap();
        for (IndivFlow indivFlow : indivList) {
            currentIndivMap.put(indivFlow.getIndivCode(), indivFlow);
        }
        if (currentIndivMap.size() < indivList.size()) {
            throw new ServiceException("录入的商品编码存在重复");
        }

        try {
            Map<String, Object> params = Maps.newHashMap();
            params.put("skuCode", goods.getSkuCode());
            params.put("warehouseId", goods.getReceive().getWarehouseId());
            params.put("indivList", Lists.newArrayList(currentIndivMap.keySet()));
            // 检测商品个体合法性
            List<Indiv> existingIndivList = indivDao.queryIndivListByOutCodes(params);
            List<String> handlingIndivCodes = Lists.newArrayList();
            for (Indiv indiv : existingIndivList) {
                if (IndivStockStatus.STOCKIN_HANDLING.getCode() == indiv.getStockStatus()) {
                    handlingIndivCodes.add("商品编码" + indiv.getIndivCode() + "已被收货单" + indiv.getInCode() + "绑定");
                }
            }
            if (CollectionUtils.isNotEmpty(handlingIndivCodes)) {
                // 部份商品被绑定
                throw new ServiceException(StringUtils.join(handlingIndivCodes, ","));
            }
            if (existingIndivList.size() != currentIndivMap.size()) {
                throw new ServiceException("此sku和仓库的出库商品个体中存在与录入的个体编码不符");
            }

            // 添加商品信息
            goods.setIndivFinished(WmsConstants.ENABLED_TRUE);
            goods.setEnabled(WmsConstants.ENABLED_FALSE);
            receiveDao.addGoods(goods);

            // 批量添加新的商品个体及流转信息
            List<String> indivCodeList = Lists.newArrayList();
            List<IndivFlow> newIndivFlowList = Lists.newArrayList();
            for (String newIndivCode : currentIndivMap.keySet()) {
                indivCodeList.add(newIndivCode);
                IndivFlow indivFlow = new IndivFlow();
                indivFlow.setIndivCode(newIndivCode);
                indivFlow.setSkuId(goods.getSkuId());
                indivFlow.setSkuCode(goods.getSkuCode());
                indivFlow.setSkuName(goods.getSkuName());
                indivFlow.setWarehouseId(goods.getReceive().getWarehouseId());
                indivFlow.setWarehouseName(goods.getReceive().getWarehouseName());
                indivFlow.setFlowType(goods.getReceive().getReceiveType());
                indivFlow.setFlowId(goods.getReceive().getId());
                indivFlow.setFlowCode(goods.getReceive().getReceiveCode());
                indivFlow.setFlowGoodsId(goods.getId());
                indivFlow.setWaresStatus(goods.getWaresStatus());
                indivFlow.setMeasureUnit(goods.getMeasureUnit());
                indivFlow.setProductBatchNo(goods.getProductBatchNo());
                indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
                newIndivFlowList.add(indivFlow);
            }
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("indivCodes", indivCodeList);    // 查询条件
            criteria.put("stockStatus", IndivStockStatus.STOCKIN_HANDLING.getCode());    // 更新为处理中
            criteria.put("inId", goods.getReceive().getId());    // 更新为新的入库单id
            criteria.put("inCode", goods.getReceive().getReceiveCode());    // 更新为新的入库单号
            criteria.put("waresStatus", goods.getWaresStatus());    // 更新个体状态
            indivDao.updateIndivsByCodes(criteria);
            indivDao.addIndivFlows(newIndivFlowList);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addShortcutRecvGoods(ReceiveGoods goods) {
        if (ReceiveStatus.RECEIVING.getCode() != goods.getReceive().getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }
        goods.setIndivFinished(WmsConstants.ENABLED_FALSE);
        goods.setEnabled(WmsConstants.ENABLED_FALSE);
        try {
            receiveDao.addGoodsList(Lists.newArrayList(goods));
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addRmaSalesOrder(RmaSalesOrder rmaSalesOrder) {
        SalesOrder salesOrder = orderDao.queryOrderByOrderCode(rmaSalesOrder.getOrderCode());
        if (salesOrder == null) {
            throw new ServiceException(String.format("未找到订单【%s】！", rmaSalesOrder.getOrderCode()));
        }

        // 已出库、已签收订单可以退货
        if (Arrays.asList(OrderStatus.SHIPPED.getCode(), OrderStatus.RECEIVED.getCode(), OrderStatus.BACKED.getCode())
            .indexOf(salesOrder.getOrderStatus()) == -1) {
            throw new ServiceException("销售订单状态必须为【已出库】【已签收】【已退货】状态！");
        }

        // ---创建退货入库单---//
        Receive receive = new Receive();
        receive.setOriginalId(salesOrder.getId());
        receive.setOriginalCode(rmaSalesOrder.getOrderCode());
        receive.setWarehouseId(rmaSalesOrder.getWarehouseId());
        receive.setWarehouseName("东莞电商仓");
        receive.setRemark(rmaSalesOrder.getRemark());
        receive.setReceiveCode(getBizCode(RMA_IN));
        receive.setReceiveType(ReceiveType.RMA.getCode());
        receive.setReceiveMode(ReceiveMode.MANUAL.getCode());
        receive.setHandlingStatus(ReceiveStatus.RECEIVED.getCode());
        receive.setHandledBy(ActionUtils.getLoginName());
        receive.setHandledTime(new Date());
        receive.setPreparedBy(ActionUtils.getLoginName());
        receive.setPreparedTime(new Date());
        receiveDao.addReceive(receive);

        List<ReceiveGoods> receiveGoodsList = Lists.newArrayList();
        for (RmaSalesOrderGoods g : rmaSalesOrder.getGoodsList()) {
            Sku sku = waresDao.querySkuBySkuCode(g.getSkuCode());

            ReceiveGoods receiveGoods = new ReceiveGoods();
            receiveGoods.setSkuId(sku.getId());
            receiveGoods.setSkuCode(g.getSkuCode());
            receiveGoods.setSkuName(sku.getSkuName());
            receiveGoods.setMeasureUnit(sku.getWares().getMeasureUnit());
            receiveGoods.setEnabled(WmsConstants.ENABLED_TRUE);
            receiveGoods.setIndivEnabled(sku.getWares().getIndivEnabled());
            receiveGoods.setIndivFinished(WmsConstants.ENABLED_FALSE);
            receiveGoods.setReceive(receive);

            // 良品
            if (g.getGoodQuantity() != null && g.getGoodQuantity() > 0) {
                ReceiveGoods goodReceiveGoods = new ReceiveGoods();
                BeanUtils.copyProperties(receiveGoods, goodReceiveGoods);
                goodReceiveGoods.setQuantity(g.getGoodQuantity());
                goodReceiveGoods.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());
                receiveGoodsList.add(goodReceiveGoods);
            }
            // 次品
            if (g.getBadQuantity() != null && g.getBadQuantity() > 0) {
                ReceiveGoods badReceiveGoods = new ReceiveGoods();
                BeanUtils.copyProperties(receiveGoods, badReceiveGoods);
                badReceiveGoods.setQuantity(g.getBadQuantity());
                badReceiveGoods.setWaresStatus(IndivWaresStatus.DEFECTIVE.getCode());
                receiveGoodsList.add(badReceiveGoods);
            }
        }
        receiveDao.addGoodsList(receiveGoodsList);

        // 重新查询一遍，读取ID
        Map<String, Long> receiveGoodsMap = Maps.newHashMap();
        List<ReceiveGoods> savedGoodsList = receiveDao.queryGoodsListByReceiveId(receive.getId());
        for (ReceiveGoods goods : savedGoodsList) {
            receiveGoodsMap.put(goods.getSkuCode() + goods.getWaresStatus(), goods.getId());
        }

        List<Indiv> indivList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(rmaSalesOrder.getGoodImeis())) {
            List<Indiv> goodIndivList = indivDao.queryIndivListByCodes(rmaSalesOrder.getGoodImeis());
            for (Indiv indiv : goodIndivList) {
                indiv.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());
            }

            indivList.addAll(goodIndivList);
        }
        if (CollectionUtils.isNotEmpty(rmaSalesOrder.getBadImeis())) {
            List<Indiv> badIndivList = indivDao.queryIndivListByCodes(rmaSalesOrder.getBadImeis());
            for (Indiv indiv : badIndivList) {
                indiv.setWaresStatus(IndivWaresStatus.DEFECTIVE.getCode());
            }
            indivList.addAll(badIndivList);
        }

        List<IndivFlow> indivFlowList = Lists.newArrayList();
        for (Indiv indiv : indivList) {
            if (IndivStockStatus.OUT_WAREHOUSE.getCode() != indiv.getStockStatus()) {
                throw new ServiceException(String.format("提交的IMEI【%s】不是已出库状态！", indiv.getIndivCode()));
            }

            indiv.setRmaTime(new Date());
            indiv.setRmaId(receive.getId());
            indiv.setRmaCode(receive.getReceiveCode());
            indiv.setRmaCount(indiv.getRmaCount() == null ? 1 : indiv.getRmaCount() + 1);
            indiv.setWarehouseId(rmaSalesOrder.getWarehouseId());
            indiv.setWarehouseName("东莞电商仓");
            indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());

            IndivFlow flow = new IndivFlow();
            flow.setIndivCode(indiv.getIndivCode());
            flow.setSkuId(indiv.getSkuId());
            flow.setSkuCode(indiv.getSkuCode());
            flow.setSkuName(indiv.getSkuName());
            flow.setWarehouseId(receive.getWarehouseId());
            flow.setWarehouseName(receive.getWarehouseName());
            flow.setMeasureUnit(indiv.getMeasureUnit());
            flow.setFlowTime(new Date());
            flow.setFlowType(IndivFlowType.IN_RMA.getCode());
            flow.setFlowId(receive.getId());
            flow.setFlowCode(receive.getReceiveCode());
            flow.setFlowGoodsId(receiveGoodsMap.get(indiv.getSkuCode() + indiv.getWaresStatus()));
            flow.setWaresStatus(indiv.getWaresStatus());
            flow.setEnabled(WmsConstants.ENABLED_TRUE);
            indivFlowList.add(flow);
        }

        // 更改个体
        if (!indivList.isEmpty()) {
            indivDao.batchUpdateIndiv(indivList);
        }
        // 添加流转信息
        if (!indivFlowList.isEmpty()) {
            indivDao.addIndivFlows(indivFlowList);
        }


        orderDao.updateOrder(LinkMapUtils.<String, Object>newHashMap().put("orderCode", rmaSalesOrder.getOrderCode())
            .put("orderStatus", OrderStatus.BACKED.getCode())
            .put("orderNotifyStatus", 0)
            .put("remarkBacked", rmaSalesOrder.getRemark())
            .getMap());

        SalesOrderLog salesOrderLog = new SalesOrderLog();
        salesOrderLog.setOrderId(salesOrder.getId());
        salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.BACKED.getCode());
        salesOrderLog.setOpUser(ActionUtils.getLoginNameAndDefault());
        salesOrderLog.setOpTime(new Date());
        salesOrderLog.setRemark("更新订单为已退货状态");
        salesOrderLogDao.insertSalesOrderLog(salesOrderLog);

        // 库存调整
        increaseStock(receive);

        // ton通知订单中心已退货
        salesOrder.setOrderStatus(OrderStatus.BACKED.getCode());
        salesOrderService.notifyOrder(Lists.newArrayList(salesOrder));
    }

    /** {@inheritDoc} */
    @Override
    public List<Map<String, String>> exportQuery(Map<String, Object> paramMap) {
        return receiveDao.rmaSalesOrderExportQuery(paramMap);
    }

    @Override
    public Receive addRmaReceive(Receive receive, Integer waresStatus) {
        if (receive == null) {
            throw new ServiceException("参数错误");
        }
        SalesOrder order;
        try {
            order = orderDao.queryOrder(receive.getOriginalId());
            if (order == null) {
                throw new ServiceException("销售订单不存在");
            }
            if ((OrderStatus.SHIPPED.getCode() != order.getOrderStatus()) && (OrderStatus.RECEIVED.getCode() != order.getOrderStatus())) {
                throw new ServiceException("销售订单状态必须为已出库或者签收状态");
            }
            // ---创建退货入库单---//
            receive.setReceiveCode(getBizCode(RMA_IN));
            receive.setReceiveType(ReceiveType.RMA.getCode());
            receive.setReceiveMode(ReceiveMode.MANUAL.getCode());
            receive.setHandlingStatus(ReceiveStatus.RECEIVED.getCode());
            receive.setHandledBy(ActionUtils.getLoginName());
            receive.setHandledTime(new Date());
            receive.setPreparedBy(ActionUtils.getLoginName());
            receive.setPreparedTime(new Date());
            receiveDao.addReceive(receive);

            // ---根据订单商品生成退货商品---//
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("originalId", order.getId());
            List<SalesOrderGoods> orderGoodsList = orderDao.queryGoodsListByOrderId(order.getId());
            List<ReceiveGoods> receiveGoodsList = Lists.newArrayList();
            for (SalesOrderGoods orderGoods : orderGoodsList) {
                ReceiveGoods receiveGoods = new ReceiveGoods();
                receiveGoods.setSkuId(orderGoods.getSkuId());
                receiveGoods.setSkuCode(orderGoods.getSkuCode());
                receiveGoods.setSkuName(orderGoods.getSkuName());
                receiveGoods.setQuantity(orderGoods.getQuantity());
                receiveGoods.setMeasureUnit(orderGoods.getMeasureUnit());
                receiveGoods.setWaresStatus(waresStatus);
                receiveGoods.setEnabled(WmsConstants.ENABLED_TRUE);
                receiveGoods.setIndivEnabled(orderGoods.getIndivEnabled());
                receiveGoods.setIndivFinished(WmsConstants.ENABLED_FALSE);
                receiveGoods.setReceive(receive);
                receiveGoodsList.add(receiveGoods);
            }
            receiveDao.addGoodsList(receiveGoodsList);

            // ---更新商品个体并添加个体流转信息--- //
            criteria.clear();
            criteria.put("orderCode", order.getOrderCode());
            List<Indiv> delyIndivList = indivDao.queryIndivList(criteria);
            if (CollectionUtils.isNotEmpty(delyIndivList)) {
                // 检测商品个体合法性
                List<String> errorMsg = Lists.newArrayList();
                for (Indiv indiv : delyIndivList) {
                    if (IndivStockStatus.OUT_WAREHOUSE.getCode() != indiv.getStockStatus()) {
                        errorMsg.add("商品编码" + indiv.getIndivCode() + "未出库");
                    }
                }
                if (CollectionUtils.isNotEmpty(errorMsg)) {
                    throw new ServiceException(StringUtils.join(errorMsg, ","));
                }

                // 构建收货商品个体对象及个体流转信息对象
                List<IndivFlow> indivFlowList = Lists.newArrayList();
                List<ReceiveGoods> savedGoodsList = receiveDao.queryGoodsListByReceiveId(receive.getId());
                Map<String, ReceiveGoods> receiveGoodsMap = Maps.newHashMap();
                for (ReceiveGoods goods : savedGoodsList) {
                    receiveGoodsMap.put(goods.getSkuId().toString() + goods.getWaresStatus().toString(), goods);
                }
                for (Indiv indiv : delyIndivList) {
                    indiv.setRmaTime(new Date());
                    indiv.setRmaId(receive.getId());
                    indiv.setRmaCode(receive.getReceiveCode());
                    indiv.setRmaCount(indiv.getRmaCount() + 1);
                    indiv.setWarehouseId(receive.getWarehouseId());
                    indiv.setWarehouseName(receive.getWarehouseName());
                    indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());
                    if (receiveGoodsMap.containsKey(indiv.getSkuId().toString() + waresStatus)) {
                        ReceiveGoods goods = receiveGoodsMap.get(indiv.getSkuId().toString() + waresStatus);
                        indiv.setWaresStatus(goods.getWaresStatus());

                        IndivFlow flow = new IndivFlow();
                        flow.setIndivCode(indiv.getIndivCode());
                        flow.setSkuId(indiv.getSkuId());
                        flow.setSkuCode(indiv.getSkuCode());
                        flow.setSkuName(indiv.getSkuName());
                        flow.setWarehouseId(receive.getWarehouseId());
                        flow.setWarehouseName(receive.getWarehouseName());
                        flow.setMeasureUnit(indiv.getMeasureUnit());
                        flow.setFlowTime(new Date());
                        flow.setFlowType(IndivFlowType.IN_RMA.getCode());
                        flow.setFlowId(receive.getId());
                        flow.setFlowCode(receive.getReceiveCode());
                        flow.setFlowGoodsId(goods.getId());
                        flow.setWaresStatus(goods.getWaresStatus());
                        flow.setEnabled(WmsConstants.ENABLED_TRUE);
                        indivFlowList.add(flow);
                    }
                }
                // 更新商品个体
                indivDao.batchUpdateIndiv(delyIndivList);
                // 添加个体流转信息
                indivDao.addIndivFlows(indivFlowList);
            }

            // ---更新销售单状态---//
            criteria.clear();
            criteria.put("orderCode", order.getOrderCode());
            criteria.put("orderStatus", OrderStatus.BACKED.getCode());
            criteria.put("orderNotifyStatus", 0);
            criteria.put("remarkBacked", receive.getRemark());
            orderDao.updateOrder(criteria);

            // 保存操作日志
            SalesOrderLog salesOrderLog = new SalesOrderLog();
            try {
                salesOrderLog.setOrderId(order.getId());
                salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.BACKED.getCode());
                salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils
                    .getLoginName());
                salesOrderLog.setOpTime(new Date());
                salesOrderLog.setRemark("更新订单为已退货状态");
                salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }

            // ---遍历退货商品数量，增加库存---//
            increaseStock(receive);

            // 通知订单中心已退货
            order.setOrderStatus(OrderStatus.BACKED.getCode());
            List<SalesOrder> backedOrderList = new ArrayList<SalesOrder>();
            backedOrderList.add(order);
            salesOrderService.notifyOrder(backedOrderList);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return receive;
    }

    @Override
    public Receive refuseRmaReceive(Receive receive, Integer waresStatus) {
        if (receive == null) {
            throw new ServiceException("参数错误");
        }
        SalesOrder order;
        try {
            order = orderDao.queryOrder(receive.getOriginalId());
            if (order == null) {
                throw new ServiceException("销售订单不存在");
            }
            if ((OrderStatus.SHIPPED.getCode() != order.getOrderStatus()) && (OrderStatus.REFUSEING.getCode() != order.getOrderStatus())) {
                throw new ServiceException("销售订单状态必须为已出库或者拒收中状态");
            }
            // ---创建拒收入库单---//
            receive.setReceiveCode(getBizCode(REFUSE_IN));
            receive.setReceiveType(ReceiveType.REFUSE.getCode());
            receive.setReceiveMode(ReceiveMode.MANUAL.getCode());
            receive.setHandlingStatus(ReceiveStatus.RECEIVED.getCode());
            receive.setHandledBy(ActionUtils.getLoginName());
            receive.setHandledTime(new Date());
            receive.setPreparedBy(ActionUtils.getLoginName());
            receive.setPreparedTime(new Date());
            receiveDao.addReceive(receive);

            // ---根据订单商品生成拒收商品---//
            List<SalesOrderGoods> orderGoodsList = orderDao.queryGoodsListByOrderId(order.getId());
            List<ReceiveGoods> receiveGoodsList = Lists.newArrayList();
            for (SalesOrderGoods orderGoods : orderGoodsList) {
                ReceiveGoods receiveGoods = new ReceiveGoods();
                receiveGoods.setSkuId(orderGoods.getSkuId());
                receiveGoods.setSkuCode(orderGoods.getSkuCode());
                receiveGoods.setSkuName(orderGoods.getSkuName());
                receiveGoods.setQuantity(orderGoods.getQuantity());
                receiveGoods.setMeasureUnit(orderGoods.getMeasureUnit());
                receiveGoods.setWaresStatus(waresStatus);
                receiveGoods.setEnabled(WmsConstants.ENABLED_TRUE);
                receiveGoods.setIndivEnabled(orderGoods.getIndivEnabled());
                receiveGoods.setIndivFinished(WmsConstants.ENABLED_FALSE);
                receiveGoods.setGoodsSid(orderGoods.getGoodsSid());
                receiveGoods.setReceive(receive);
                receiveGoodsList.add(receiveGoods);
            }
            receiveDao.addGoodsList(receiveGoodsList);

            // ---更新商品个体并添加个体流转信息--- //
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("orderCode", order.getOrderCode());
            List<Indiv> delyIndivList = indivDao.queryIndivList(criteria);
            if (CollectionUtils.isNotEmpty(delyIndivList)) {
                // 检测商品个体合法性
                List<String> errorMsg = Lists.newArrayList();
                for (Indiv indiv : delyIndivList) {
                    if (IndivStockStatus.OUT_WAREHOUSE.getCode() != indiv.getStockStatus()) {
                        errorMsg.add("商品编码" + indiv.getIndivCode() + "未出库");
                    }
                }
                if (CollectionUtils.isNotEmpty(errorMsg)) {
                    throw new ServiceException(StringUtils.join(errorMsg, ","));
                }

                // 构建收货商品个体对象及个体流转信息对象
                List<IndivFlow> indivFlowList = Lists.newArrayList();
                List<ReceiveGoods> savedGoodsList = receiveDao.queryGoodsListByReceiveId(receive.getId());
                Map<String, ReceiveGoods> receiveGoodsMap = Maps.newHashMap();
                for (ReceiveGoods goods : savedGoodsList) {
                    receiveGoodsMap.put(goods.getSkuId().toString() + goods.getWaresStatus().toString(), goods);
                }
                for (Indiv indiv : delyIndivList) {
                    indiv.setRmaTime(new Date());
                    indiv.setRmaId(receive.getId());
                    indiv.setRmaCode(receive.getReceiveCode());
                    indiv.setRmaCount(indiv.getRmaCount() + 1);
                    indiv.setWarehouseId(receive.getWarehouseId());
                    indiv.setWarehouseName(receive.getWarehouseName());
                    indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());
                    if (receiveGoodsMap.containsKey(indiv.getSkuId().toString() + waresStatus)) {
                        ReceiveGoods goods = receiveGoodsMap.get(indiv.getSkuId().toString() + waresStatus);
                        indiv.setWaresStatus(goods.getWaresStatus());

                        IndivFlow flow = new IndivFlow();
                        flow.setIndivCode(indiv.getIndivCode());
                        flow.setSkuId(indiv.getSkuId());
                        flow.setSkuCode(indiv.getSkuCode());
                        flow.setSkuName(indiv.getSkuName());
                        flow.setWarehouseId(receive.getWarehouseId());
                        flow.setWarehouseName(receive.getWarehouseName());
                        flow.setMeasureUnit(indiv.getMeasureUnit());
                        flow.setFlowTime(new Date());
                        flow.setFlowType(IndivFlowType.IN_REFUSE.getCode());
                        flow.setFlowId(receive.getId());
                        flow.setFlowCode(receive.getReceiveCode());
                        flow.setFlowGoodsId(goods.getId());
                        flow.setWaresStatus(goods.getWaresStatus());
                        flow.setEnabled(WmsConstants.ENABLED_TRUE);
                        indivFlowList.add(flow);
                    }
                }
                // 更新商品个体
                indivDao.batchUpdateIndiv(delyIndivList);
                // 添加个体流转信息
                indivDao.addIndivFlows(indivFlowList);
            }

            // ---更新销售单状态---//
            criteria.clear();
            criteria.put("orderCode", order.getOrderCode());
            criteria.put("orderStatus", OrderStatus.REFUSED.getCode());
            orderDao.updateOrder(criteria);

            // 保存操作日志
            SalesOrderLog salesOrderLog = new SalesOrderLog();
            try {
                salesOrderLog.setOrderId(order.getId());
                salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.REFUSED.getCode());
                salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils
                    .getLoginName());
                salesOrderLog.setOpTime(new Date());
                salesOrderLog.setRemark("更新订单为已拒收状态");
                salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }

            // ---遍历退货商品数量，增加库存---//
            increaseStock(receive);

            // 通知订单中心已拒收
            order.setOrderStatus(OrderStatus.REFUSED.getCode());
            List<SalesOrder> orderList = new ArrayList<SalesOrder>();
            orderList.add(order);
            salesOrderService.notifyOrder(orderList);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return receive;
    }

    @Override
    public void deleteReceiveGoods(Long goodsId) {
        ReceiveGoods goods = getReceiveGoods(goodsId);
        if (goods == null) {
            return;
        }
        if (ReceiveStatus.RECEIVING.getCode() != goods.getReceive().getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }

        if (goods.getIndivEnabled() == WmsConstants.ENABLED_TRUE) {
            // 删除商品个体及流转信息
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("flowType", IndivFlowType.IN_PURCHASE.getCode());
            criteria.put("flowItemId", goods.getId());
            List<IndivFlow> boundIndivFlowList = indivDao.queryIndivFlowList(criteria);
            List<String> boundIndivCodes = MyCollectionUtils.collectionElementPropertyToList(boundIndivFlowList, "indivCode");
            indivDao.deleteIndivsByCodes(boundIndivCodes);
            indivDao.deleteIndivFlowsByCodes(boundIndivCodes);
        }

        // 删除商品
        receiveDao.deleteGoods(goodsId);
    }

    @Override
    public List<Receive> getReceiveList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return receiveDao.queryReceiveByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getReceiveTotal(Map<String, Object> criteria) {
        return receiveDao.queryReceiveTotal(criteria);
    }

    @Override
    public Receive getReceive(Long id) {
        try {
            return receiveDao.queryReceive(id);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ReceiveGoods> getReceiveGoodsByPage(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return receiveDao.queryReceiveGoodsByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getReceiveGoodsTotal(Map<String, Object> criteria) {
        try {
            return receiveDao.queryReceiveGoodsTotal(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public ReceiveGoods getReceiveGoods(Long id) {
        try {
            return receiveDao.queryGoods(id);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ReceiveGoods> getReceiveGoodsList(Long receiveId) {
        try {
            return receiveDao.queryGoodsListByReceiveId(receiveId);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ReceiveSummary> getReceiveSummaryList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return receiveDao.queryReceiveSummaryByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getUserAreaList(Map<String, Object> criteria, Page page) throws ServiceException {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return receiveDao.getUserAreaList(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getUserAreaForCascade(Map<String, Object> criteria) throws ServiceException {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        try {
            return receiveDao.getUserAreaForCascade(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }


    @Override
    public Integer getUserAreaTotle(Map<String, Object> criteria) {
        return receiveDao.getUserAreaTotle(criteria);
    }

    @Override
    public List<Map<String, Object>> orderInFactList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return receiveDao.orderInFactList(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int orderInFactTotle(Map<String, Object> map) {
        return receiveDao.orderInFactTotle(map);
    }

    @Override
    public List<Map<String, Object>> orderInFactCascade(Map<String, Object> map) {
        return receiveDao.orderInFactCascade(map);
    }

    @Override
    public Integer getReceiveSummaryTotal(Map<String, Object> criteria) {
        return receiveDao.queryReceiveSummaryTotal(criteria);
    }

    @Override
    public void updateReceiveGoods(ReceiveGoods goods) {
        if (ReceiveStatus.RECEIVING.getCode() != goods.getReceive().getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }
        try {
            receiveDao.updateGoods(goods);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updatePurchaseRecvGoodsWithIndivList(ReceiveGoods goods, List<IndivFlow> indivList) {
        // 效验录入的商品个体信息
        if (CollectionUtils.isEmpty(indivList)) {
            throw new ServiceException("商品编码为空");
        }
        if (indivList.size() != goods.getQuantity()) {
            throw new ServiceException("输入商品编码数目与商品数量不符");
        }
        if (ReceiveStatus.RECEIVING.getCode() != goods.getReceive().getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }
        Map<String, IndivFlow> currentIndivMap = Maps.newHashMap();
        for (IndivFlow indivFlow : indivList) {
            currentIndivMap.put(indivFlow.getIndivCode(), indivFlow);
        }
        if (currentIndivMap.size() < indivList.size()) {
            throw new ServiceException("录入的商品编码存在重复");
        }

        try {
            // 过滤出失效的个体编码和新增的个体编码
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("flowType", IndivFlowType.IN_PURCHASE.getCode());
            criteria.put("flowItemId", goods.getId());
            List<IndivFlow> boundIndivFlowList = indivDao.queryIndivFlowList(criteria);

            List<String> boundIndivCodes = MyCollectionUtils.collectionElementPropertyToList(boundIndivFlowList, "indivCode");
            List<String> requestIndivCodes = MyCollectionUtils.collectionElementPropertyToList(indivList, "indivCode");
            List<String> newIndivCodes = Lists.newArrayList(requestIndivCodes);
            List<String> invalidIndivCodes = Lists.newArrayList(boundIndivCodes);
            for (String boundIndivCode : boundIndivCodes) {
                newIndivCodes.remove(boundIndivCode);
            }
            for (String requestIndivCode : requestIndivCodes) {
                invalidIndivCodes.remove(requestIndivCode);
            }

            // 检测新增的个体编码合法性
            if (CollectionUtils.isNotEmpty(newIndivCodes)) {
                List<Indiv> existingIndivList = indivDao.queryIndivListByCodes(newIndivCodes);
                List<String> handlingIndivCodes = Lists.newArrayList();
                List<String> existingIndivCodes = Lists.newArrayList();
                for (Indiv indiv : existingIndivList) {
                    if (IndivStockStatus.STOCKIN_HANDLING.getCode() == indiv.getStockStatus()) {
                        handlingIndivCodes.add("商品编码" + indiv.getIndivCode() + "已被收货单" + indiv.getInCode() + "绑定");
                    } else {
                        existingIndivCodes.add("商品编号" + indiv.getIndivCode() + "重复入库");
                    }
                }
                handlingIndivCodes.addAll(existingIndivCodes);
                if (CollectionUtils.isNotEmpty(handlingIndivCodes)) {
                    throw new ServiceException(StringUtils.join(handlingIndivCodes, ","));
                }
            }

            // 删除失效的商品个体及流转信息
            if (CollectionUtils.isNotEmpty(invalidIndivCodes)) {
                indivDao.deleteIndivsByCodes(invalidIndivCodes);
                indivDao.deleteIndivFlowsByCodes(invalidIndivCodes);
            }

            // 批量添加新的商品个体及流转信息
            if (CollectionUtils.isNotEmpty(newIndivCodes)) {
                List<Indiv> newIndivList = Lists.newArrayList();
                List<IndivFlow> newIndivFlowList = Lists.newArrayList();
                for (String newIndivCode : newIndivCodes) {
                    Indiv indiv = new Indiv();
                    indiv.setIndivCode(newIndivCode);
                    indiv.setSkuId(goods.getSkuId());
                    indiv.setSkuCode(goods.getSkuCode());
                    indiv.setSkuName(goods.getSkuName());
                    indiv.setWarehouseId(goods.getReceive().getWarehouseId());
                    indiv.setWarehouseName(goods.getReceive().getWarehouseName());
                    indiv.setMeasureUnit(goods.getMeasureUnit());
                    indiv.setInId(goods.getReceive().getId());
                    indiv.setInCode(goods.getReceive().getReceiveCode());
                    indiv.setInTime(new Date());
                    indiv.setProductBatchNo(goods.getProductBatchNo());
                    indiv.setRmaCount(0);
                    indiv.setWaresStatus(goods.getWaresStatus());
                    indiv.setStockStatus(IndivStockStatus.STOCKIN_HANDLING.getCode());
                    indiv.setPushStatus(RemoteCallStatus.PENDING.getCode());
                    indiv.setPushCount(0);
                    newIndivList.add(indiv);
                    IndivFlow indivFlow = new IndivFlow();
                    indivFlow.setIndivCode(newIndivCode);
                    indivFlow.setSkuId(goods.getSkuId());
                    indivFlow.setSkuCode(goods.getSkuCode());
                    indivFlow.setSkuName(goods.getSkuName());
                    indivFlow.setWarehouseId(goods.getReceive().getWarehouseId());
                    indivFlow.setWarehouseName(goods.getReceive().getWarehouseName());
                    indivFlow.setFlowType(IndivFlowType.IN_PURCHASE.getCode());
                    indivFlow.setFlowId(goods.getReceive().getId());
                    indivFlow.setFlowCode(goods.getReceive().getReceiveCode());
                    indivFlow.setFlowGoodsId(goods.getId());
                    indivFlow.setWaresStatus(goods.getWaresStatus());
                    indivFlow.setMeasureUnit(goods.getMeasureUnit());
                    indivFlow.setProductBatchNo(goods.getProductBatchNo());
                    indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
                    newIndivFlowList.add(indivFlow);
                }
                indivDao.addIndivs(newIndivList);
                indivDao.addIndivFlows(newIndivFlowList);
            }

            // 更新收货商品信息
            goods.setIndivFinished(WmsConstants.ENABLED_TRUE);
            receiveDao.updateGoods(goods);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void confirmPurchaseRecv(Receive receive) throws ServiceException {
        if (ReceiveStatus.RECEIVING.getCode() != receive.getHandlingStatus()) {
            throw new ServiceException("状态异常");
        }
        try {
            List<ReceiveGoods> goodsList = receiveDao.queryGoodsListByReceiveId(receive.getId());
            if (CollectionUtils.isEmpty(goodsList)) {
                throw new ServiceException("收货商品为空");
            }

            boolean indivEnabled = false;
            for (ReceiveGoods goods : goodsList) {
                if (WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
                    if (WmsConstants.ENABLED_FALSE == goods.getIndivFinished()) {
                        throw new ServiceException("还有商品尚未绑定商品编码");
                    }
                    indivEnabled = true;
                }
            }

            // 更新收货单信息
            receive.setHandlingStatus(ReceiveStatus.RECEIVED.getCode());
            receive.setHandledBy(ActionUtils.getLoginName());
            receive.setHandledTime(new Date());
            receiveDao.updateReceive(receive);

            // 处理收货商品个体相关信息
            if (indivEnabled) {
                Map<String, Object> criteria = Maps.newHashMap();
                criteria.put("inId", receive.getId());
                criteria.put("stockStatus", IndivStockStatus.IN_WAREHOUSE.getCode());
                criteria.put("inTime", new Date());
                indivDao.updateIndivsByInId(criteria);

                // 更新商品个体流转信息
                criteria.clear();
                criteria.put("flowType", IndivFlowType.IN_PURCHASE.getCode());
                criteria.put("flowId", receive.getId());
                criteria.put("flowTime", new Date());
                criteria.put("enabled", WmsConstants.ENABLED_TRUE);
                indivDao.updateIndivFlowsByFlowId(criteria);
            }

            if (ReceiveMode.AUTO.getCode() == receive.getReceiveMode()) {
                // ---更新采购单状态---//
                PurPreRecv purPreRecv = purPreRecvDao.queryPurPreRecvByReceiveId(receive.getId());
                if (purPreRecv.getHandlingStatus() == PurchaseOrderStatus.RECEIVED.getCode()) {
                    throw new ServiceException("已经收货，无法再次确认！");
                }
                purPreRecv.setHandlingStatus(PurchaseOrderStatus.RECEIVED.getCode());
                purPreRecv.setHandledTime(new Date());
                purPreRecv.setHandledBy(ActionUtils.getLoginName());
                purPreRecvDao.updatePurPreRecv(purPreRecv);
            }

            // 遍历收货商品数量，增加库存
            increaseStock(receive);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        // TODO:同步库存到外围系统
    }

    private void increaseStock(Receive receive) {
        try {
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("receiveId", receive.getId());
            List<ReceiveSummary> recvSummaryList = receiveDao.queryReceiveSummaryByReceiveId(criteria);
            if (CollectionUtils.isEmpty(recvSummaryList)) {
                throw new ServiceException("收货汇总为空");
            }
            for (ReceiveSummary summary : recvSummaryList) {
                StockType stockType;
                if (IndivWaresStatus.NON_DEFECTIVE.getCode() == summary.getWaresStatus()) {
                    stockType = StockType.STOCK_SALES;
                } else {
                    stockType = StockType.STOCK_UNSALES;
                }
                StockBizType bizType;
                boolean doDelta = false;
                if (ReceiveType.PURCHASE.getCode().equals(receive.getReceiveType())) {
                    bizType = StockBizType.IN_PURCHASE;
                } else if (ReceiveType.RMA.getCode().equals(receive.getReceiveType())) {
                    bizType = StockBizType.IN_RMA;
                } else if (ReceiveType.REFUSE.getCode().equals(receive.getReceiveType())) {
                    bizType = StockBizType.IN_REFUSE;
                    doDelta = true;
                } else if (ReceiveType.PURCHARMA.getCode().equals(receive.getReceiveType())) {
                    bizType = StockBizType.IN_PURCHARMA;
                } else if (ReceiveType.SHUADAN.getCode().equals(receive.getReceiveType())) {
                    bizType = StockBizType.IN_SHUADAN;
                } else {
                    throw new ServiceException("收货单类型错误");
                }
                StockRequest stockRequest = new StockRequest(summary.getWarehouseId(), summary.getSkuId(), stockType, summary
                    .getQuantity(), bizType, receive.getOriginalCode());
                stockRequest.setGoodsSid(summary.getGoodsSid());
                stockService.increaseStock(stockRequest, doDelta);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    // @Override
    // public void addRmaIndiv(Receive receive, IndivFlow indivFlow) throws
    // ServiceException {
    // SalesOrder salesOrder =
    // orderDao.queryOrderByOrderCode(indivFlow.getOriginalCode());
    // if (salesOrder == null) {
    // throw new ServiceException("源订单不存在");
    // }
    // if (OrderStatus.SHIPPED.getCode() != salesOrder.getOrderStatus()) {
    // throw new ServiceException("源订单尚未完成出库");
    // }
    // try {
    // ReceiveGoods receiveGoods = new ReceiveGoods();
    // receiveGoods.setSkuId(indivFlow.getSkuId());
    // receiveGoods.setSkuCode(indivFlow.getSkuCode());
    // receiveGoods.setSkuName(indivFlow.getSkuName());
    // receiveGoods.setQuantity(1);
    // receiveGoods.setMeasureUnit(indivFlow.getMeasureUnit());
    // receiveGoods.setWaresStatus(indivFlow.getWaresStatus());
    // receiveGoods.setEnabled(WmsConstants.ENABLED_FALSE);
    // receiveGoods.setIndivEnabled(WmsConstants.ENABLED_TRUE);
    // receiveGoods.setIndivFinished(WmsConstants.ENABLED_FALSE);
    // receiveGoods.setReceive(receive);
    // // 添加入库明细项
    // StockInItem stockInItem = new StockInItem();
    // // stockInItem.setStockIn(receive);
    // stockInItem.setSkuId(indivFlow.getSkuId());
    // stockInItem.setMeasureUnit(indivFlow.getMeasureUnit());
    // stockInItem.setQuantity(1);
    // stockInItem.setIndivEnabled(WmsConstants.ENABLED_TRUE);
    // stockInItem.setIndivFinished(WmsConstants.ENABLED_TRUE);
    // stockInItem.setWaresStatus(indivFlow.getWaresStatus() + "");
    // stockInItem.setOriginalCode(indivFlow.getOriginalCode());
    // stockInItem.setRemark(indivFlow.getIndivCode());
    // // stockInDao.addStockInItem(stockInItem);
    //
    // // 添加商品个体流转信息
    // indivFlow.setFlowType(IndivFlowType.IN_RMA.getCode());
    // // indivFlow.setFlowItemId(stockInItem.getId());
    // indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
    // indivFlow.setSkuId(indivFlow.getSkuId());
    // indivDao.addIndivFlows(Lists.newArrayList(indivFlow));
    // } catch (Exception e) {
    // throw new ServiceException(e);
    // }
    // }

    @Override
    public Receive getReceiveByReceiveCode(String receiveCode) {
        return receiveDao.queryReceiveByReceiveCode(receiveCode);
    }

    @Override
    public Receive getReceiveByOrderId(Long orderId) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("receiveType", ReceiveType.RMA.getCode());
        criteria.put("originalId", orderId);
        return receiveDao.queryReceiveByOriginalId(criteria);
    }
}
