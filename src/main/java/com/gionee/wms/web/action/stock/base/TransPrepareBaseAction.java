package com.gionee.wms.web.action.stock.base;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.CommonAjaxResult;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.TransferGoods;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.web.action.CrudActionSupport;

import java.util.List;
import java.util.Map;

/**
 * Created by gionee on 2017/9/4.
 */
public abstract class TransPrepareBaseAction extends CrudActionSupport<Transfer> {

    protected int getPrepareRst(int prepareRst, Transfer transfer, Indiv indiv, String skuCode, List<TransferGoods> goodsList, TransferService transferService) {
        if (null != goodsList) {
            for (TransferGoods goods : goodsList) {
                if (skuCode.equals(goods.getSkuCode()) && WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
                    Integer preparedNum = null == goods.getPreparedNum() ? 1 : goods.getPreparedNum() + 1;
                    if (preparedNum > goods.getQuantity()) {
                        prepareRst = 1; // 超出
                        break;
                    }
                    transferService.addIndiv(transfer.getTransferId(), transfer.getTransferId() + "", indiv.getId());
                    prepareRst = 2; // 成功
                    break;
                }
            }
        }
        return prepareRst;
    }

    protected int getPrepareRst(Map<String, Object> params, int prepareRst, List<TransferGoods> goodsList, TransferService transferService, String indivCode) {
        if (null != goodsList) {
            for (TransferGoods goods : goodsList) {
                if (indivCode.equals(goods.getSkuCode()) && WmsConstants.ENABLED_FALSE == goods.getIndivEnabled()) {
                    Integer preparedNum = null == goods.getPreparedNum() ? 1 : goods.getPreparedNum() + 1;
                    if (preparedNum > goods.getQuantity()) {
                        prepareRst = 1; // 已超出
                        break;
                    }
                    goods.setPreparedNum(preparedNum);
                    params.clear();
                    params.put("goodsId", goods.getId());
                    params.put("preparedNum", preparedNum);
                    transferService.updateTransferGoods(params);
                    prepareRst = 2; // 成功
                    break;
                }
            }
        }
        return prepareRst;
    }


    protected void validateIndivTransAndSetResult(CommonAjaxResult result, Long transferId, Transfer transfer, TransferService transferService) {
        if (null != transfer) {
            if (!(WmsConstants.TransferStatus.UN_DELIVERYD.getCode() == transfer.getStatus() || WmsConstants.TransferStatus.DELIVERYING.getCode() == transfer.getStatus())) {
                result.setMessage("未发货的单才能进行配货");
            } else {
                Transfer tr = new Transfer();
                tr.setTransferId(transfer.getTransferId());
                tr.setStatus(WmsConstants.TransferStatus.DELIVERYING.getCode());
                transferService.updateTransferSf(tr);
                result.setOk(true);
                result.setResult(transfer);
            }
        } else {
            result.setMessage("调拨单" + transferId + "不存在");
        }
    }

    protected void validateIndivTransAndSetResult(CommonAjaxResult result, Transfer transfer, Indiv indiv, Warehouse warehouse) {
        if (null == indiv) {
            result.setOk(false);
            result.setMessage("未找到商品个体");
        } else if (WmsConstants.IndivStockStatus.IN_WAREHOUSE.getCode() != indiv.getStockStatus()) {
            result.setOk(false);
            result.setMessage("商品不在库中");
        } else if (!(warehouse.getId().equals(indiv.getWarehouseId()))) {
            result.setOk(false);
            result.setMessage("该商品不属于该仓库");
        } else {
            if (null != transfer.getTransType() && WmsConstants.TRANS_TYPE_DEFECTIVE == transfer.getTransType().intValue()) {
                // 次品单
                if (WmsConstants.IndivWaresStatus.NON_DEFECTIVE.getCode() == indiv.getWaresStatus()) {
                    result.setOk(false);
                    result.setMessage("次品调拨的物品必须为次品");
                }
            } else {
                // 良品单
                if (WmsConstants.IndivWaresStatus.DEFECTIVE.getCode() == indiv.getWaresStatus()) {
                    result.setOk(false);
                    result.setMessage("良品调拨的物品必须为良品");
                }
            }
        }
    }

    protected void switchPrepareAndSetResult(CommonAjaxResult result, int prepareRst, String indivCode) {
        if (0 == prepareRst) {
            result.setOk(false);
            result.setMessage("未找到sku");
        } else if (1 == prepareRst) {
            result.setOk(false);
            result.setMessage("已达到数量");
        } else if (2 == prepareRst) {
            result.setOk(true);
            result.setResult(indivCode);
            result.setMessage("成功");
        }
    }

    protected boolean checkTransferStatus(CommonAjaxResult result, Transfer transfer) {
        if (!(WmsConstants.TransferStatus.UN_DELIVERYD.getCode() == transfer.getStatus() || WmsConstants.TransferStatus.DELIVERYING.getCode() == transfer.getStatus())) {
            // 不为未发货或者配货中
            result.setOk(false);
            result.setMessage("调拨单状态异常，请检查");
            ajaxObject(result);
            return true;
        }
        return false;
    }
}
