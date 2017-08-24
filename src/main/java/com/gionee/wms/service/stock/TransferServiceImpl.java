package com.gionee.wms.service.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.DateConvert;
import com.gionee.wms.common.LinkMapUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.*;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.StatDao;
import com.gionee.wms.dao.TransferDao;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.*;
import com.gionee.wms.facade.result.WmsResult;
import com.gionee.wms.facade.result.WmsResult.WmsCodeEnum;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.service.wares.SkuMapService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequest;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequestHeader;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequestItem;
import com.sf.integration.warehouse.response.WmsPurchaseOrderResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.dao.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gionee.wms.common.WmsConstants.IndivStockStatus.OUT_WAREHOUSE;
import static com.gionee.wms.common.WmsConstants.LogType.BIZ_LOG;
import static com.gionee.wms.common.WmsConstants.StockBizType.OUT_TRANSFER;
import static com.gionee.wms.common.WmsConstants.StockType.STOCK_SALES;
import static com.gionee.wms.common.WmsConstants.TransferStatus.*;

@Service("transferService")
public class TransferServiceImpl extends CommonServiceImpl implements TransferService {

    private static Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private IndivDao indivDao;
    @Autowired
    private StockService stockService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private StatDao statDao;
    @Autowired
    private WaresDao waresDao;
    @Autowired
    private SkuMapService skuMapService;
    @Autowired
    private SFWebService sfWebService;
    @Autowired
    private LogService logService;

    @Override
    public int getTransferListTotal(Map<String, Object> criteria) {
        // TODO Auto-generated method stub
        return transferDao.getTransferListTotal(criteria);
    }

    @Override
    public List<Transfer> getTransferList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
            criteria.put("page", page);
        }
        if (!criteria.containsKey("page")) {
            criteria.put("page", page);
        }
        return transferDao.getTransferList(criteria);
    }

    /**
     * 顺丰
     */
    @Override
    public int getTransferListTotalSf(Map<String, Object> criteria) {
        return transferDao.getTransferListTotalSf(criteria);
    }

    /**
     * 顺丰
     */
    @Override
    public List<Transfer> getTransferListSf(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
            criteria.put("page", page);
        }
        return transferDao.getTransferListSf(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, String>> exportTransferList(Map<String, Object> criteria, Page page) {
        criteria.put("page", page);
        try {
            List<Map<String, Object>> list = transferDao.exportTransferList(criteria);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> transferStatusMap = Maps.newHashMap();
            for (TransferStatus transferStatus : TransferStatus.values()) {
                transferStatusMap.put(transferStatus.getCode() + "", transferStatus.getName());
            }

            List<Map<String, String>> el = new ArrayList<Map<String, String>>(list.size());
            for (Map<String, Object> data : list) {
                Map<String, String> item = Maps.newHashMap();
                item.put("transferId", (String) data.get("TRANSFERID")); // 调拨批次号
                item.put("warehouseName", (String) data.get("WAREHOUSENAME")); // 发货仓
                item.put("consignee", StringUtils.defaultString((String) data.get("CONSIGNEE"), "")); // 收货人
                item.put("transferTo", StringUtils.defaultString((String) data.get("TRANSFERTO"), "")); // 收货地址
                item.put("logisticNo", StringUtils.defaultString((String) data.get("LOGISTICNO"), "")); // 物流单号
                // item.put("status", data.get("transferTo").toString()); // 调货状态
                item.put("statusText", transferStatusMap.get((String) data.get("STATUS"))); // 调货状态
                item.put("remark", StringUtils.defaultString((String) data.get("REMARK"), "")); // 备注
                item.put("createTime", data.get("CREATETIME") == null ? "" : sdf.format(data.get("CREATETIME"))); // 创建时间
                item.put("handledBy", StringUtils.defaultString((String) data.get("HANDLEDBY"), "")); // 操作人
                item.put("skuCode", StringUtils.defaultString((String) data.get("SKUCODE"), "")); // SKU
                item.put("skuName", StringUtils.defaultString((String) data.get("SKUNAME"), "")); //
                item.put("quantity", data.get("QUANTITY") == null ? "" : data.get("QUANTITY").toString()); // 数量
                item.put("measureUnit", StringUtils.defaultString((String) data.get("MEASUREUNIT"), "")); // 单位
                // item.put("indivEnabled", data.get("INDIVENABLED").toString()); //
                item.put("unitPrice", data.get("UNITPRICE") == null ? "" : data.get("UNITPRICE").toString()); // 单价
                item.put("rm", data.get("RM") == null ? "" : data.get("RM").toString()); // 已退货

                el.add(item);
            }
            return el;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage purchaseOrder(Transfer transfer) {
        WmsPurchaseOrderRequest request = new WmsPurchaseOrderRequest();
        WmsPurchaseOrderRequestHeader header = new WmsPurchaseOrderRequestHeader();

        Warehouse warehouse = warehouseService.getWarehouse(Long.valueOf(transfer.getTransferTo()));
        String warehouseCode = warehouse.getWarehouseCode();

        header.setCompany(WmsConstants.SF_COMPANY);
        header.setWarehouse(warehouseCode);
        header.setErp_order_num(String.valueOf(transfer.getTransferId()));
        header.setErp_order_type("调拨入库");
        header.setTransfer_warehouse(warehouseCode);
        header.setOrder_date(DateConvert.convertD2String(transfer.getCreateTime()));
        header.setScheduled_receipt_date(DateConvert.convertD2String(new Date()));

        List<TransferGoods> goods = getTransferGoodsById(transfer.getTransferId());
        List<WmsPurchaseOrderRequestItem> detailList = Lists.newArrayList();
        for (TransferGoods good : goods) {
            WmsPurchaseOrderRequestItem item = new WmsPurchaseOrderRequestItem();
            SkuMap skuMap = skuMapService.getSkuMapBySkuCode(good.getSkuCode(), "sf");
            if (skuMap == null) {
                logger.info("调拨单推送失败," + good.getSkuCode() + "不是顺丰sku");
                logService.insertLog(new Log(BIZ_LOG.getCode(), "顺丰入库单：" + transfer.getTransferId(), good.getSkuCode() + "没有", "system", new Date()));
                return new ServiceCtrlMessage(false, "调拨单推送失败," + good.getSkuCode() + "不是顺丰sku");
            }
            item.setItem(skuMap.getOuterSkuCode());
            item.setTotal_qty(String.valueOf(good.getQuantity()));
            detailList.add(item);
        }
        request.setHeader(header);
        request.setDetailList(detailList);
        WmsPurchaseOrderResponse response = sfWebService.outsideToLscmService(WmsPurchaseOrderRequest.class, WmsPurchaseOrderResponse.class, request);
        if (response.getResult()) {
            logger.info("返回结果:" + response.getResult() + "," + response.getRemark() + ",调拨单号：" + response.getOrderid() + ",调拨入库成功");
            transfer.setOrderPushStatus("1");
            transfer.setOrderConfirmStatus("0");
            updateTransferSf(transfer);

            return new ServiceCtrlMessage(true, "创建顺丰入库单成功，返回单号：" + response.getOrderid());
        } else {
            logger.error("返回结果:" + response.getResult() + "," + response.getRemark() + ",调拨单号：" + response.getOrderid() + ",调拨入库失败");
            return new ServiceCtrlMessage(false, "返回结果:" + response.getResult() + "," + response.getRemark());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage picking(String sv, Integer num, Long transferId) {
        Transfer transfer = getTransferById(transferId);
        if (!Arrays.asList(DELIVERYING.getCode(), UN_DELIVERYD.getCode()).contains(transfer.getStatus())) {
            return new ServiceCtrlMessage<>(false, "调拨单状态异常，请检查！");
        }
        if (sv.length() <= 5) { // 配件
            List<TransferGoods> goodsList = transferDao.getTransferGoods(LinkMapUtils.<String, Object>newHashMap().put("transferId", transferId).put("skuCode", sv).getMap());
            if (goodsList.isEmpty()) {
                return new ServiceCtrlMessage(false, "此调拨单未找到SKU：" + sv);
            }
            for (TransferGoods g : goodsList) {
                int preparedNum = g.getPreparedNum() == null ? 0 : g.getPreparedNum();
                if (num + preparedNum > g.getQuantity()) {
                    return new ServiceCtrlMessage(false, String.format("SKU：%s超出配货数量！", sv));
                }

                updateTransferGoods(LinkMapUtils.<String, Object>newHashMap().put("goodsId", g.getId()).put("preparedNum", num + preparedNum).getMap());
                return new ServiceCtrlMessage(true, "配货成功！");
            }


        } else { // 串码
            Indiv indiv = indivDao.queryIndivByCode(sv);
            if (indiv == null) {
                return new ServiceCtrlMessage(false, "未找到IMEI：" + sv);
            }
            List<TransferGoods> goodsList = transferDao.getTransferGoods(LinkMapUtils.<String, Object>newHashMap().put("transferId", transferId).put("skuCode", indiv.getSkuCode()).getMap());
            if (goodsList.isEmpty()) {
                return new ServiceCtrlMessage(false, "IMEI：" + sv + "不属于此调拨单的SKU");
            }
            for (TransferGoods g : goodsList) {
                int preparedNum = g.getPreparedNum() == null ? 0 : g.getPreparedNum();
                if (preparedNum + 1 > g.getQuantity()) {
                    return new ServiceCtrlMessage(false, String.format("SKU：%s 已经超出配货数量！", g.getSkuCode()));
                }

                Warehouse warehouse = warehouseService.getWarehouse(transfer.getWarehouseId());
                if (!indiv.getWarehouseId().equals(warehouse.getId())) {
                    return new ServiceCtrlMessage(false, String.format("IMEI：%s 不属于：%s", sv, warehouse.getWarehouseName()));
                }
                if (IndivStockStatus.IN_WAREHOUSE.getCode() != indiv.getStockStatus()) {
                    return new ServiceCtrlMessage(false, String.format("IMEI：%s 不是在库中", sv));
                }

                addIndiv(transferId, String.valueOf(transferId), indiv.getId());
                updateTransferGoods(LinkMapUtils.<String, Object>newHashMap().put("goodsId", g.getId()).put("preparedNum", ++preparedNum).getMap());
                return new ServiceCtrlMessage(true, "配货成功");
            }
        }

        return new ServiceCtrlMessage(false, "配货失败！");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage dispatch(Long transferId, String logisticNo) {
        Transfer transfer = getTransferById(transferId);
        if (DELIVERYED.getCode() == transfer.getStatus()) {
            return new ServiceCtrlMessage(false, "已经发货，无法继续操作！");
        }
        List<TransferGoods> transferGoods = getTransferGoodsById(transferId);
        if (CollectionUtils.isEmpty(transferGoods)) {
            return new ServiceCtrlMessage(false, "调拨商品为空，无法发货！");
        }
        for (TransferGoods g : transferGoods) {
            if (g.getQuantity() == null || g.getPreparedNum() == null || g.getQuantity().intValue() != g.getPreparedNum().intValue()) {
                return new ServiceCtrlMessage(false, String.format("SKU：%s 尚未配货完成，无法发货！", g.getSkuCode()));
            }
        }

        indivDao.updatePrepareToDly(LinkMapUtils.<String, Object>newHashMap().put("prepareId", transferId).put("outId", transferId).put("outCode", transferId).put("outTime", new Date()).put("stockStatus", OUT_WAREHOUSE.getCode()).getMap());
        for (TransferGoods g : transferGoods) {
            StockRequest stockRequest = new StockRequest(transfer.getWarehouseId(), g.getSkuId(), STOCK_SALES, g.getQuantity(), OUT_TRANSFER, String.valueOf(transferId));
            stockService.decreaseStock(stockRequest); // 扣减可销库存
        }

        transfer.setStatus(DELIVERYED.getCode());
        transfer.setLogisticNo(logisticNo);
        transfer.setShippingTime(new Date());

        transferDao.updateTransfer(transfer);

        return new ServiceCtrlMessage(true, "发货成功！");
    }

    /**
     * 顺丰
     */
    @Override
    public List<Transfer> getTransferSfList(Map<String, Object> criteria) {
        return transferDao.getTransferSfList(criteria);
    }

    /**
     * 顺丰
     */
    @Override
    public void addTransferSf(Transfer transfer) {
        // TODO Auto-generated method stub
        transfer.setTransferId(Long.valueOf(getBizCode(TRANSFER)));
        transfer.setOrderPushStatus("0");
        transferDao.addTransferSf(transfer);
    }

    /**
     * 顺丰
     */
    @Override
    public void updateTransferSf(Transfer transfer) {
        transferDao.updateTransferSf(transfer);
    }

    @Override
    public Transfer getTransferById(Long transferId) {
        // TODO Auto-generated method stub
        return transferDao.getTransferById(transferId);
    }

    @Override
    public Transfer getTransferSfById(Long transferId) {
        // TODO Auto-generated method stub
        return transferDao.getTransferSfById(transferId);
    }

    @Override
    public List<Transfer> getImeiSf(String transferId, String skuCode) {
        return transferDao.getImeiSf(transferId, skuCode);
    }

    @Override
    public void addTransfer(Transfer transfer) {
        // TODO Auto-generated method stub
        transfer.setTransferId(Long.valueOf(getBizCode(TRANSFER)));
        transfer.setFlowType("待审核");
        transferDao.addTransfer(transfer);
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        // TODO Auto-generated method stub
        transferDao.updateTransfer(transfer);
    }

    @Override
    public void deleteTransfer(Long transferId) {
        try {
            // 处理库存
            // Transfer transfer = getTransferById(transferId);
            // Map<String, Object> params = Maps.newHashMap();
            // params.put("transferId", transferId);
            // List<TransferGoods> goodsList = getTransferGoods(params);
            // if (goodsList != null && goodsList.size() > 0) {
            // for(TransferGoods goods : goodsList){
            // //调整库存
            // StockRequest stockRequest = new StockRequest(transfer.getWarehouseId(),
            // goods.getSkuId(), StockType.STOCK_OCCUPY, StockType.STOCK_SALES, goods.getQuantity(),
            // StockBizType.OUT_TRANSFER, goods.getTransferId());
            // stockService.convertStock(stockRequest);
            // }
            // }
            // 删除调拨单下的商品，删除调拨单
            transferDao.deleteTransferGoods(transferId);
            transferDao.deleteTransfer(transferId);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private Map<String, Sku> getSkuMap(List<TransferGoods> goodsList) {
        Map<String, Sku> skuMap = Maps.newHashMap();
        Set<String> skuCodes = Sets.newHashSet();
        for (TransferGoods transferGoods : goodsList) {
            skuCodes.add(transferGoods.getSkuCode());
        }
        List<Sku> skuList = null;
        skuList = waresDao.querySkuListByCodes(Lists.newArrayList(skuCodes));
        if (CollectionUtils.isEmpty(skuList) || skuList.size() != skuCodes.size()) {
            throw new ServiceException(WmsCodeEnum.SKU_NOT_EXISTS.getCode());
        }
        for (Sku sku : skuList) {
            skuMap.put(sku.getSkuCode(), sku);
        }

        return skuMap;
    }

    // 添加调货单(顺丰)
    public void addTransferOrder(Transfer transfer, List<TransferGoods> transferGoods) {
        // 保存订单
        transfer.setTransferId(Long.valueOf(getBizCode(TRANSFER)));
        if (transferDao.addTransferSf(transfer) == 0) {
            throw new ServiceException(WmsCodeEnum.DUPLICATE_ORDER.getCode());
        }
        ;
        // 保存订单商品清单
        Map<String, Sku> skuMap = getSkuMap(transferGoods);
        if (skuMap != null && skuMap.size() > 0) {
            for (TransferGoods goods : transferGoods) {
                goods.setTransferId(String.valueOf(transfer.getTransferId()));
                if (skuMap.containsKey(goods.getSkuCode())) {
                    Sku sku = skuMap.get(goods.getSkuCode());
                    goods.setSkuId(sku.getId());
                    goods.setSkuCode(sku.getSkuCode());
                    goods.setSkuName(sku.getSkuName());
                    goods.setMeasureUnit(sku.getWares().getMeasureUnit());
                    goods.setIndivEnabled(sku.getWares().getIndivEnabled());
                    // if (null == goods.getSubtotalPrice()) {
                    // goods.setSubtotalPrice(new BigDecimal(goods
                    // .getUnitPrice().floatValue()
                    // * goods.getQuantity().intValue())
                    // .setScale(2, BigDecimal.ROUND_HALF_UP));
                    // }
                }
            }
        }
        transferDao.batchAddTransferGoods(transferGoods);
    }

    // 编辑调货单(顺丰)
    @Override
    public void updateTransferOrder(Transfer transfer, List<TransferGoods> goods) {
        Transfer oldTransfer = transferDao.getTransferSfById(transfer.getTransferId());
        if (oldTransfer == null) {
            throw new ServiceException(WmsCodeEnum.ORDER_NOT_EXIST.getCode());
        }
        if (oldTransfer.getStatus().intValue() == WmsConstants.OrderStatus.PICKED.getCode() || oldTransfer.getStatus().intValue() == WmsConstants.OrderStatus.PICKING.getCode()) {
            throw new ServiceException(WmsCodeEnum.CURRENT_STATUS_FORBIDDEN_UPDATE.getCode());
        }
        // 更新订单
        if (transferDao.updateTransferSf(transfer) == 0) {
            throw new ServiceException(WmsCodeEnum.ORDER_NOT_EXIST.getCode());
        }
        if (null != goods && goods.size() > 0) {
            // 删除原有商品清单
            transferDao.deleteTransferGoods(oldTransfer.getTransferId());

            // 保存商品清单，调整新的库存占用信息
            Map<String, Sku> skuMap = getSkuMap(goods);
            transfer.setTransferId(oldTransfer.getTransferId());
            for (TransferGoods good : goods) {
                good.setTransferId(String.valueOf(transfer.getTransferId()));
                if (skuMap.containsKey(good.getSkuCode())) {
                    Sku sku = skuMap.get(good.getSkuCode());
                    good.setSkuId(sku.getId());
                    good.setSkuCode(sku.getSkuCode());
                    good.setSkuName(sku.getSkuName());
                    good.setMeasureUnit(sku.getWares().getMeasureUnit());
                    good.setIndivEnabled(sku.getWares().getIndivEnabled());
                    // if (null == orderGoods.getSubtotalPrice()) {
                    // orderGoods.setSubtotalPrice(new BigDecimal(
                    // orderGoods.getUnitPrice().floatValue()
                    // * orderGoods.getQuantity().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    // }
                }
            }
            transferDao.batchAddTransferGoods(goods);
        }
    }

    @Override
    public void addTransferGoods(Transfer transfer, TransferGoods goods) {
        try {
            // 新增或合并相同sku数量
            Map<String, Object> params = Maps.newHashMap();
            params.put("transferId", transfer.getTransferId());
            params.put("skuCode", goods.getSkuCode());
            List<TransferGoods> goodsList = transferDao.getTransferGoods(params);
            if (goodsList == null || goodsList.size() == 0) {
                goods.setTransferId(String.valueOf(transfer.getTransferId()));
                transferDao.addTransferGoods(goods);
            } else {
                TransferGoods oldGoods = goodsList.get(0);
                params.clear();
                params.put("goodsId", oldGoods.getId());
                params.put("quantity", oldGoods.getQuantity() + goods.getQuantity());
                transferDao.updateTransferGoods(params);
            }

            // //调整库存
            // StockRequest stockRequest = new StockRequest(transfer.getWarehouseId(),
            // goods.getSkuId(), StockType.STOCK_SALES, StockType.STOCK_OCCUPY, goods.getQuantity(),
            // StockBizType.OUT_TRANSFER, String.valueOf(transfer.getTransferId()));
            // stockService.convertStock(stockRequest);
        } catch (DataAccessException e) {
            throw new ServiceException("数据操作错误");
        }
    }

    public void addTransferGoodsSf(Transfer transfer, List<TransferGoods> goods) {
        if (goods.size() > 0) {
            boolean flag = true;
            for (TransferGoods transferGoods : goods) {
                if (flag) {
                    transferDao.addTransfer(transfer);
                    flag = false;
                }
                transferDao.addTransferGoods(transferGoods);
            }
        } else {
            transferDao.addTransfer(transfer);
        }
    }

    @Override
    public List<TransferGoods> getTransferGoodsById(Long transferId) {
        // TODO Auto-generated method stub
        return transferDao.getTransferGoodsById(transferId);
    }

    @Override
    public void deleteGoodsById(Long warehouseId, Long goodsId) {
        try {
            Map<String, Object> params = Maps.newHashMap();
            params.put("goodsId", goodsId);
            List<TransferGoods> goodsList = transferDao.getTransferGoods(params);
            if (goodsList == null || goodsList.size() == 0) {
                throw new ServiceException("该调拨商品不存在");
            }
            // else {
            // TransferGoods oldGoods = goodsList.get(0);
            // //调整库存
            // StockRequest stockRequest = new StockRequest(warehouseId,
            // oldGoods.getSkuId(), StockType.STOCK_OCCUPY, StockType.STOCK_SALES, oldGoods.getQuantity(),
            // StockBizType.OUT_TRANSFER, oldGoods.getTransferId());
            // stockService.convertStock(stockRequest);
            // }
            transferDao.deleteGoodsById(goodsId);
        } catch (DataAccessException e) {
            throw new ServiceException("数据更新错误");
        }
    }

    @Override
    public void deleteTransferGoods(Long transferId) {
        // TODO Auto-generated method stub
        transferDao.deleteTransferGoods(transferId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirmDelivery(Transfer transfer, String logisticNo, List<TransferGoods> goodsList) {
        Map<String, Object> criteria = Maps.newHashMap();
        try {
            // Map<Long, TransferGoods> goodsHm = new HashMap<Long, TransferGoods>(); // 通过skuId找到该调拨的商品
            // for (TransferGoods transferGoods : goodsList) {
            // goodsHm.put(transferGoods.getSkuId(), transferGoods);
            // }

            criteria.put("prepareId", transfer.getTransferId());
            List<Indiv> indivList = indivDao.queryIndivList(criteria);
            List<SalesOutStat> statList = new ArrayList<SalesOutStat>();
            Date nowTime = new Date();
            if (null != indivList && indivList.size() > 0) {
                // 将录入的商品个体更新为出库状态，建立与发货单的关联关系
                criteria.put("outId", transfer.getTransferId());
                criteria.put("outCode", transfer.getTransferId());
                criteria.put("outTime", nowTime);
                criteria.put("stockStatus", OUT_WAREHOUSE.getCode());
                criteria.put("indivCodes", indivList);
                indivDao.updatePrepareToDly(criteria);
            }
            criteria.clear();
            TransferPartner partner = null;
            criteria.put("id", transfer.getTransferSale());
            List<TransferPartner> partners = transferDao.getTransferPartnerList(criteria);
            if (null != partners && partners.size() > 0) {
                partner = partners.get(0);    // 获得渠道
            }
            for (TransferGoods goods : goodsList) {
                StockType stockType = STOCK_SALES;    // 变动的库存类型
                if (null != transfer.getTransType() && WmsConstants.TRANS_TYPE_DEFECTIVE == transfer.getTransType().intValue()) {
                    stockType = StockType.STOCK_UNSALES;    // 如果是次品调拨
                }
                StockRequest stockRequest = new StockRequest(transfer.getWarehouseId(), goods.getSkuId(), stockType, goods.getQuantity(), OUT_TRANSFER, transfer.getTransferId() + "");
                stockService.decreaseStock(stockRequest);    // 扣减可销库存
                SalesOutStat outStat = new SalesOutStat();
                outStat.setSkuId(goods.getSkuId());
                outStat.setSkuCode(goods.getSkuCode());
                outStat.setSkuName(goods.getSkuName());
                outStat.setQuantity(goods.getQuantity());
                outStat.setIndivEnabled(goods.getIndivEnabled());
                outStat.setUnitPrice(goods.getUnitPrice());
                outStat.setOrderId(transfer.getTransferId());
                outStat.setOrderCode(transfer.getTransferId() + "");
                outStat.setPayNo("");
                outStat.setShippingTime(nowTime);
                if (null != partner) {
                    outStat.setPartnerCode(partner.getCode());
                    outStat.setPartnerName(partner.getName());
                }
                outStat.setOutType(1);
                outStat.setOrderAmount(transfer.getOrderAmount());
                statList.add(outStat);
            }
            transfer.setStatus(DELIVERYED.getCode());    // 设为已发货
            transfer.setLogisticNo(logisticNo);    // 设置物流单号
            transfer.setShippingTime(nowTime);
            Transfer poTransfer = transferDao.getTransferById(transfer.getTransferId());
            if (poTransfer.getStatus() == DELIVERYED.getCode()) {
                throw new ServiceException("发货单状态异常！");
            }
            transferDao.updateTransfer(transfer);
            for (SalesOutStat outStat : statList) {
                statDao.addSalesOutStat(outStat);
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void confirmDeliverySf(Transfer transfer, List<TransferGoods> goodsList) {
        Map<String, Object> criteria = Maps.newHashMap();
        try {
            criteria.put("prepareId", transfer.getTransferId());
            List<Indiv> indivList = indivDao.queryIndivList(criteria);
            List<SalesOutStat> statList = new ArrayList<SalesOutStat>();
            Date nowTime = new Date();
            if (null != indivList && indivList.size() > 0) {
                // 将录入的商品个体更新为出库状态，建立与发货单的关联关系
                criteria.put("outId", transfer.getTransferId());
                criteria.put("outCode", transfer.getTransferId());
                criteria.put("outTime", nowTime);
                criteria.put("stockStatus", OUT_WAREHOUSE.getCode());
                criteria.put("indivCodes", indivList);
                indivDao.updatePrepareToDly(criteria);
            }
            criteria.clear();

            TransferPartner partner = null;
            criteria.put("id", transfer.getTransferSale());
            List<TransferPartner> partners = transferDao.getTransferPartnerList(criteria);
            if (null != partners && partners.size() > 0) {
                partner = partners.get(0);    // 获得渠道
            }
            for (TransferGoods goods : goodsList) {
                StockType stockType = STOCK_SALES;    // 变动的库存类型
                if (null != transfer.getTransType() && WmsConstants.TRANS_TYPE_DEFECTIVE == transfer.getTransType().intValue()) {
                    stockType = StockType.STOCK_UNSALES;    // 如果是次品调拨
                }
                StockRequest stockRequest = new StockRequest(transfer.getWarehouseId(), goods.getSkuId(), stockType, goods.getQuantity(), OUT_TRANSFER, transfer.getTransferId() + "");
                stockService.decreaseStock(stockRequest);    // 扣减可销库存
                SalesOutStat outStat = new SalesOutStat();
                outStat.setSkuId(goods.getSkuId());
                outStat.setSkuCode(goods.getSkuCode());
                outStat.setSkuName(goods.getSkuName());
                outStat.setQuantity(goods.getQuantity());
                outStat.setIndivEnabled(goods.getIndivEnabled());
                outStat.setUnitPrice(goods.getUnitPrice());
                outStat.setOrderId(transfer.getTransferId());
                outStat.setOrderCode(transfer.getTransferId() + "");
                outStat.setPayNo("");
                outStat.setShippingTime(nowTime);
                if (null != partner) {
                    outStat.setPartnerCode(partner.getCode());
                    outStat.setPartnerName(partner.getName());
                }
                outStat.setOutType(1);
                outStat.setOrderAmount(transfer.getOrderAmount());
                statList.add(outStat);
            }
            transfer.setStatus(DELIVERYED.getCode());    // 设为已发货
            transfer.setShippingTime(nowTime);
            Transfer poTransfer = transferDao.getTransferById(transfer.getTransferId());
            if (poTransfer.getStatus() == DELIVERYED.getCode()) {
                throw new ServiceException("发货单状态异常！");
            }
            transferDao.updateTransferSf(transfer);
            for (SalesOutStat outStat : statList) {
                statDao.addSalesOutStat(outStat);
            }
            increaseStock(transfer);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private void increaseStock(Transfer transfer) {
        try {
            List<TransferGoods> goodsList = transferDao.getTransferGoodsById(transfer.getTransferId());
            if (CollectionUtils.isEmpty(goodsList)) {
                throw new ServiceException("收货汇总为空");
            }
            for (TransferGoods good : goodsList) {
                StockType stockType = STOCK_SALES;
                StockBizType bizType = OUT_TRANSFER;
                StockRequest stockRequest = new StockRequest(Long.valueOf(transfer.getTransferTo()), good.getSkuId(), stockType, good.getQuantity(), bizType, transfer.getTransferId() + "");
                stockService.increaseStock(stockRequest, false);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<TransferGoods> getTransferGoods(Map<String, Object> params) {
        return transferDao.getTransferGoods(params);
    }

    @Override
    public void updateTransferGoods(Map<String, Object> params) {
        transferDao.updateTransferGoods(params);
    }

    @Override
    public void addIndiv(Long transferId, String transferCode, Long indivId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", indivId);
        params.put("prepareId", transferId);
        params.put("prepareCode", transferCode);
        params.put("stockStatus", WmsConstants.IndivStockStatus.STOCKOUT_HANDLING.getCode());
        indivDao.updateIndivPrepare(params);
    }

    @Override
    public void addIndivs(Long transferId, String transferCode, List<Indiv> indivList) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("prepareId", transferId);
        criteria.put("prepareCode", transferCode);
        criteria.put("stockStatus", WmsConstants.IndivStockStatus.STOCKOUT_HANDLING.getCode());
        criteria.put("indivList", indivList);
        indivDao.updateIndivsPrepare(criteria);
    }

    @Override
    public List<Indiv> getIndivList(Long transferId) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("prepareId", transferId);
        return indivDao.queryIndivList(criteria);
    }

    @Override
    public void confirmBack(String[] skuCodes, String[] indivCodes, Integer[] waresStatuss, Integer[] indivEnableds, Long warehouseId) {
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId); // 固定为东莞电商仓
        List<IndivFlow> indivFlowList = Lists.newArrayList();// 个体流转信息
        Map<String, Map<String, Integer>> salesSkuStockMap = Maps.newHashMap();// 良品sku数量，可销库存，格式 {skuCode : {调拨单ID : "数量"}}
        Map<String, Map<String, Integer>> unSalesSkuStockMap = Maps.newHashMap();// 次品sku数量，不可销库存，格式 {skuCode : {调拨单ID : "数量"}}
        for (int i = 0; i < indivCodes.length; i++) {
            // 更新商品个体并添加个体流转信息
            if (indivEnableds[i].intValue() == WmsConstants.ENABLED_TRUE) {
                // 良品个体处理
                Indiv indiv = indivDao.queryIndivByCode(indivCodes[i]);
                if (indiv == null) {
                    throw new ServiceException("商品个体不存在");
                }
                indiv.setRmaTime(new Date());
                indiv.setRmaId(indiv.getOutId());// 此处切换为调拨单ID
                indiv.setRmaCode(indiv.getOutCode());
                indiv.setRmaCount(indiv.getRmaCount() + 1);
                indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());

                IndivFlow flow = new IndivFlow();
                flow.setIndivCode(indiv.getIndivCode());
                flow.setSkuId(indiv.getSkuId());
                flow.setSkuCode(indiv.getSkuCode());
                flow.setSkuName(indiv.getSkuName());
                flow.setWarehouseId(indiv.getWarehouseId());
                flow.setWarehouseName(indiv.getWarehouseName());
                flow.setMeasureUnit(indiv.getMeasureUnit());
                flow.setFlowTime(new Date());
                flow.setFlowType(IndivFlowType.IN_BACK_TRANSFER.getCode());
                flow.setFlowId(indiv.getOutId());
                flow.setFlowCode(indiv.getOutCode());
                flow.setFlowGoodsId(null);
                flow.setEnabled(WmsConstants.ENABLED_TRUE);

                String transferId = indiv.getOutCode(), skuCode = indiv.getSkuCode();
                if (waresStatuss[i].intValue() == IndivWaresStatus.NON_DEFECTIVE.getCode()) { // 良品
                    indiv.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());
                    flow.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());

                    Map<String, Integer> tMap = salesSkuStockMap.get(skuCode) == null ? new HashMap<String, Integer>() : salesSkuStockMap.get(skuCode);
                    tMap.put(transferId, tMap.containsKey(transferId) ? tMap.get(transferId) + 1 : 1);
                    salesSkuStockMap.put(skuCode, tMap);
                } else { // 次品
                    indiv.setWaresStatus(IndivWaresStatus.DEFECTIVE.getCode());
                    flow.setWaresStatus(IndivWaresStatus.DEFECTIVE.getCode());

                    Map<String, Integer> tMap = unSalesSkuStockMap.get(skuCode) == null ? new HashMap<String, Integer>() : unSalesSkuStockMap.get(skuCode);
                    tMap.put(transferId, tMap.containsKey(transferId) ? tMap.get(transferId) + 1 : 1);
                    unSalesSkuStockMap.put(skuCode, tMap);
                }
                // 更新商品个体
                indivDao.updateIndiv(indiv);
                indivFlowList.add(flow);
            }
        }

        if (CollectionUtils.isNotEmpty(indivFlowList)) {
            // 添加个体流转信息
            indivDao.addIndivFlows(indivFlowList);
        }

        // 处理良品库存信息
        for (Map.Entry<String, Map<String, Integer>> entry : salesSkuStockMap.entrySet()) {
            for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                StockRequest stockRequest = new StockRequest(warehouse.getId(), entry.getKey(), STOCK_SALES, entry2.getValue().intValue(), StockBizType.IN_BACK_TRANSFER, entry2.getKey());
                stockService.increaseStock(stockRequest);
            }
        }
        // 处理次品库存信息
        for (Map.Entry<String, Map<String, Integer>> entry : unSalesSkuStockMap.entrySet()) {
            for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                StockRequest stockRequest = new StockRequest(warehouse.getId(), entry.getKey(), StockType.STOCK_UNSALES, entry2.getValue().intValue(), StockBizType.IN_BACK_TRANSFER, entry2.getKey());
                stockService.increaseStock(stockRequest);
            }
        }
    }

    /**
     * 取消调拨单
     */
    @Override
    public void cancelTransfer(Long transferId) {
        Transfer transfer;
        // StockRequest stockRequest;
        try {
            transfer = transferDao.getTransferById(transferId);
            if (null == transfer) {
                throw new ServiceException(WmsResult.WmsCodeEnum.TRANSFER_NOT_EXIST.getDescribe());
            } else if (DELIVERYED.getCode() == transfer.getStatus() || WmsConstants.TransferStatus.CANCELED.getCode() == transfer.getStatus()) {
                throw new ServiceException("不能取消");
            }
            // List<TransferGoods> goodsList = transferDao.getTransferGoodsById(transferId);
            // if (goodsList != null && goodsList.size() > 0) {
            // for(TransferGoods goods : goodsList){
            // //调整库存
            // stockRequest = new StockRequest(transfer.getWarehouseId(),
            // goods.getSkuId(), StockType.STOCK_OCCUPY, StockType.STOCK_SALES, goods.getQuantity(),
            // StockBizType.CONVERT_CANCEL_TRANS, goods.getTransferId());
            // stockService.convertStock(stockRequest);
            // }
            // }
            Map<String, Object> params = Maps.newHashMap();
            params.put("preparedNum", 0);
            params.put("transferId", transferId);
            transferDao.updateTransferGoods(params);
            params.clear();
            params.put("stockStatus", WmsConstants.IndivStockStatus.IN_WAREHOUSE.getCode());
            params.put("prepareId", transferId);
            indivDao.updateTransferCancel(params);
            transfer.setHandledBy(ActionUtils.getLoginName());
            transfer.setStatus(WmsConstants.TransferStatus.CANCELED.getCode());
            transferDao.updateTransfer(transfer);// 更新调拨单与个体的关联关系
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    // 查询顺丰入库状态
    public void getTransferInSfStatus(String transferId) {

    }

    @Override
    public List<Transfer> getTransfered() {
        return transferDao.getTransfered();
    }

    @Override
    public List<Transfer> getTransferPush() {
        return transferDao.getTransferPush();
    }

    @Override
    public List<TransferGoods> getTransferGoodsForPrep(Long transferId) {
        return transferDao.getTransferGoodsForPrep(transferId);
    }

    @Override
    public List<TransferGoods> getTransferGoodsForView(Long transferId) {
        return transferDao.getTransferGoodsForView(transferId);
    }

    @Override
    public List<TransferPartner> getTransferPartnerList(Map<String, Object> criteria) {
        return transferDao.getTransferPartnerList(criteria);
    }

    @Override
    public List<Transfer> convert(List<Transfer> transfers) throws Exception {
        List<Transfer> transferList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(transfers)) {
            for (int i = 0; i < transfers.size(); i++) {
                Transfer transfer = transfers.get(i);
                transfer.setFlowType("待审核");
                TransferGoods transferGoods = transfer.getGoodsList().get(0);
                Sku sku = waresDao.querySkuBySkuCode(transferGoods.getSkuCode());
                if (sku == null) {
                    throw new Exception("导入失败,第" + (i + 1) + "行sku code:" + transferGoods.getSkuCode() + "找不到对应商品!");
                }
                transferGoods.setSkuName(sku.getSkuName());
                transferGoods.setSkuId(sku.getId());
                transferGoods.setMeasureUnit(sku.getWares().getMeasureUnit());
                // 转换数量
                BigDecimal quantity = new BigDecimal(String.valueOf(transferGoods.getQuantity()));
                BigDecimal total = quantity.multiply(transferGoods.getUnitPrice());
                if (i > 0) {
                    Transfer previousTransfer = transfers.get(i - 1);
                    if (previousTransfer.getConsignee().equals(transfer.getConsignee())) {
                        Transfer baseTransfer = transferList.get(transferList.size() - 1);
                        baseTransfer.setOrderAmount(baseTransfer.getOrderAmount().add(total));
                        for (TransferGoods goods : transfer.getGoodsList()) {
                            goods.setTransferId(baseTransfer.getTransferId().toString());
                        }
                        baseTransfer.getGoodsList().addAll(transfer.getGoodsList());
                    } else {
                        setGoodsTransferIdAndTotalAndId(transfer, total);
                        transferList.add(transfer);
                    }
                } else {
                    setGoodsTransferIdAndTotalAndId(transfer, total);
                    transferList.add(transfer);
                }
            }
        }
        return transferList;
    }

    private void setGoodsTransferIdAndTotalAndId(Transfer transfer, BigDecimal total) {
        transfer.setOrderAmount(total);
        transfer.setTransferId(Long.valueOf(getBizCode(TRANSFER)));
        for (TransferGoods goods : transfer.getGoodsList()) {
            goods.setTransferId(transfer.getTransferId().toString());
        }
    }

    @Override
    @Transactional
    public void addBatch(List<Transfer> transferList) {
        List<TransferGoods> transferGoodsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(transferList)) {
            for (Transfer transfer : transferList) {
                List<TransferGoods> goods = transfer.getGoodsList();
                if (goods != null) {
                    transferGoodsList.addAll(goods);
                }
            }
            transferDao.insertBatch(transferList);
            if (!CollectionUtils.isEmpty(transferGoodsList)) {
                transferDao.insertBatchGoods(transferGoodsList);
            }
        }
    }
}
