package com.gionee.wms.web.action.stock;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.CommonAjaxResult;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.TransferGoods;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.action.stock.base.TransPrepareBaseAction;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("TransPrepareAction")
@Scope("prototype")
public class TransPrepareAction extends TransPrepareBaseAction {
    private static final long serialVersionUID = 2728587467025993326L;
    private static final int PART_CODE_MAXLENGTH = 5;    // 配件编码最大长度

    @Autowired
    private TransferService transferService;
    @Autowired
    private IndivService indivService;
    @Autowired
    private WarehouseService warehouseService;

    private Long transferId;
    private String logisticNo;
    private List<TransferGoods> goodsList;
    private String indivCode;
    private List<Indiv> indivList;
    private String goodsId;
    private Integer preparedNum;
    private Transfer transfer;
    private Boolean finished;
    private Integer isImei;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public String loadGoods() throws Exception {
        boolean unfinished = false;
        if (null != transferId) {
            goodsList = transferService.getTransferGoodsForPrep(transferId);
            indivList = transferService.getIndivList(transferId);
            for (TransferGoods goods : goodsList) {
                if (goods.getPreparedNum() == null || goods.getPreparedNum() < goods.getQuantity()) {
                    unfinished = true;
                }
            }
            finished = !unfinished;    // 没有未完成为已完成
        }
        return "load_goods";
    }

    /**
     * 扫描时检查商品个体
     */
    public String prepareIndiv() throws Exception {
        Validate.notNull(indivCode);
        CommonAjaxResult result = new CommonAjaxResult();
        Map<String, Object> params = Maps.newHashMap();
        int prepareRst = 0;
        // 获取配货订单仓库信息
        Transfer transfer = transferService.getTransferById(transferId);
        if (checkTransferStatus(result, transfer)) {
            return null;
        }
        if ((isImei != null && isImei == 0) || isImei == null) {
            if (indivCode.length() <= PART_CODE_MAXLENGTH) {
                // 扫描sku编码
                prepareSku(result, params, prepareRst);
            } else {
                // 扫描个体编码
                prepareIndiv(result, prepareRst, transfer);
            }
        } else if (isImei == 1) {
            //扫描箱号
            prepareCase(result, transfer);
        }

        ajaxObject(result);
        return null;
    }

    private void prepareIndiv(CommonAjaxResult result, int prepareRst, Transfer transfer) {
        Indiv indiv = indivService.getIndivByCode(indivCode);
        Warehouse warehouse = warehouseService.getWarehouse(transfer.getWarehouseId());

        result.setOk(true);
        validateIndivTransAndSetResult(result, transfer, indiv, warehouse);
        if (result.getOk()) {
            String skuCode = indiv.getSkuCode();
            prepareRst = getPrepareRst(prepareRst, transfer, indiv, skuCode, goodsList, transferService);
            switchPrepareAndSetResult(result, prepareRst, skuCode);
        }
    }

    private void prepareSku(CommonAjaxResult result, Map<String, Object> params, int prepareRst) {
        prepareRst = getPrepareRst(params, prepareRst, goodsList, transferService, indivCode);
        switchPrepareAndSetResult(result, prepareRst, indivCode);
    }

    private void prepareCase(CommonAjaxResult result, Transfer transfer) {
        result.setOk(false);
        String message = "未找到箱号对应个体!";
        List<Indiv> indivs = indivService.getIndivListByCaseCode(indivCode);
        if (!CollectionUtils.isEmpty(indivs) && !CollectionUtils.isEmpty(goodsList)) {
            for (TransferGoods transferGoods : goodsList) {
                if (transferGoods.getSkuCode().equals(indivs.get(0).getSkuCode())
                    && transferGoods.getSkuCode().startsWith("1")) {
                    // 申请数量必须大于等于包装箱中个体数量
                    Integer quantity = transferGoods.getQuantity();

                    if (quantity >= indivs.size()) {
                        // 待配货数量必须为null或0
                        // 待配货数量大于等于包装箱中个体数量
                        Integer preparedNum = transferGoods.getPreparedNum();

                        if ((preparedNum == null || preparedNum == 0) || (quantity - preparedNum >= indivs.size())) {
                            result.setOk(true);
                            Warehouse warehouse = warehouseService.getWarehouse(transfer.getWarehouseId());

                            for (Indiv indiv : indivs) {
                                validateIndivTransAndSetResult(result, transfer, indiv, warehouse);
                                if (!result.getOk()) {
                                    return;
                                }
                            }
                            transferService.addIndivs(transfer.getTransferId(), transfer.getTransferId().toString(), indivs);
                            message = "成功!";
                        } else {
                            message = "待配货数量必须大于等于包装箱中个体数量!";
                        }
                    } else {
                        message = "申请数量必须大于等于包装箱中个体数量!";
                    }
                }
            }
        }
        result.setMessage(message);
    }

    public String readyPrepare() throws Exception {

        Validate.notNull(transferId);
        CommonAjaxResult result = new CommonAjaxResult();
        try {
            transfer = transferService.getTransferById(transferId);    // 获得调货单
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put("transferId", transferId);
            criteria.put("transferTo", "8610752");
            int t = transferService.getTransferListTotalSf(criteria);
            if (t != 0) {
                result.setMessage("调拨单不属于调货模块下的单号");
                ajaxObject(result);
                return null;
            }
            validateIndivTransAndSetResult(result, transferId, transfer, transferService);

        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            result.setMessage("获取调拨单失败：" + e.getMessage());
        }
        ajaxObject(result);
        return null;
    }


    /**
     * 确认调拨
     * @return
     * @throws Exception
     */
    public String confirm() throws Exception {
        try {
            Validate.notNull(transferId);    // 调拨单号
            Validate.notNull(logisticNo);    // 物流单号
            Transfer transfer = transferService.getTransferById(transferId);
            if (WmsConstants.TransferStatus.DELIVERYED.getCode() == transfer.getStatus()) {
                ajaxError("调拨单已发货，请不要重复发货");
            } else if (WmsConstants.TransferStatus.CANCELED.getCode() == transfer.getStatus()) {
                ajaxError("调拨单已取消，无法发货");
            } else {
                List<TransferGoods> goodsList = transferService.getTransferGoodsForPrep(transfer.getTransferId());    // 调拨的商品
                if (goodsList == null) {
                    ajaxError("调拨商品为空，无法发货");
                    return null;
                }
                for (TransferGoods goods : goodsList) {
                    if (goods.getQuantity() != null && goods.getPreparedNum() != null && goods.getQuantity().intValue() != goods.getPreparedNum().intValue()) {
                        ajaxError("配货数量不正确，无法提交");
                        return null;
                    }
                }
                logisticNo = logisticNo.trim();
                transferService.confirmDelivery(transfer, logisticNo, goodsList);
                ajaxSuccess("");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            ajaxError("配货失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ajaxError("系统错误");
        }
        return null;
    }

    public String batchPrepare() {
        CommonAjaxResult result = new CommonAjaxResult();
        Map<String, Object> params = Maps.newHashMap();
        try {
            params.put("preparedNum", preparedNum);
            params.put("goodsId", goodsId);
            transferService.updateTransferGoods(params);
            result.setOk(true);
        } catch (Exception e) {
            result.setOk(false);
            result.setMessage(e.getMessage());
        }
        ajaxObject(result);
        return null;
    }

    @Override
    public Transfer getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String list() throws Exception {
        Validate.notNull(transferId);
        //获取调拨商品列表
        transfer = transferService.getTransferById(transferId);
        goodsList = transferService.getTransferGoodsForPrep(transferId);
        return LIST;
    }

    @Override
    public String input() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String add() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String update() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String delete() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void prepareInput() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void prepareUpdate() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void prepareAdd() throws Exception {
        // TODO Auto-generated method stub

    }

    public List<TransferGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<TransferGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getIndivCode() {
        return indivCode;
    }

    public void setIndivCode(String indivCode) {
        this.indivCode = indivCode;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public void setLogisticNo(String logisticNo) {
        this.logisticNo = logisticNo;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public List<Indiv> getIndivList() {
        return indivList;
    }

    public void setIndivList(List<Indiv> indivList) {
        this.indivList = indivList;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getPreparedNum() {
        return preparedNum;
    }

    public void setPreparedNum(Integer preparedNum) {
        this.preparedNum = preparedNum;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Integer getIsImei() {
        return isImei;
    }

    public void setIsImei(Integer isImei) {
        this.isImei = isImei;
    }
}
