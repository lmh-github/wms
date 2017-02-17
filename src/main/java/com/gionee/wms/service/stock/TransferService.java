package com.gionee.wms.service.stock;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.TransferGoods;
import com.gionee.wms.entity.TransferPartner;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.List;
import java.util.Map;

public interface TransferService {

    int getTransferListTotal(Map<String, Object> criteria);

    List<Transfer> getTransferList(Map<String, Object> criteria, Page page);

    List<Transfer> getTransferSfList(Map<String, Object> criteria);

    Transfer getTransferById(Long transferId);

    Transfer getTransferSfById(Long transferId);

    void addTransfer(Transfer transfer);

    void updateTransfer(Transfer transfer);

    void deleteTransfer(Long transferId);

    void addTransferGoods(Transfer transfer, TransferGoods goods);

    List<TransferGoods> getTransferGoodsForPrep(Long transferId);

    List<TransferGoods> getTransferGoodsForView(Long transferId);

    List<TransferGoods> getTransferGoodsById(Long transferId);

    void deleteTransferGoods(Long goodsId);

    void deleteGoodsById(Long warehouseId, Long goodsId);

    /**
     * 配货完成事务
     */
    void confirmDelivery(Transfer transfer, String logisticNo, List<TransferGoods> goodsList);

    List<TransferGoods> getTransferGoods(Map<String, Object> params);

    void updateTransferGoods(Map<String, Object> params);

    void addIndiv(Long transferId, String transferCode, Long indivId);

    void addIndivs(Long transferId, String transferCode, List<Indiv> indivList);

    List<Indiv> getIndivList(Long transferId);

    void confirmBack(String[] skuCodes, String[] indivCodes, Integer[] waresStatuss, Integer[] indivEnableds, Long warehouseId);

    void cancelTransfer(Long transferId);

    /**
     * 获取调拨合作伙伴列表
     * @return
     */
    List<TransferPartner> getTransferPartnerList(Map<String, Object> criteria);


    //顺丰
    List<Transfer> getTransferListSf(Map<String, Object> criteria, Page page);

    int getTransferListTotalSf(Map<String, Object> criteria);

    void addTransferSf(Transfer transfer);

    void updateTransferSf(Transfer transfer);

    void addTransferOrder(Transfer transfer, List<TransferGoods> goods);

    void updateTransferOrder(Transfer transfer, List<TransferGoods> goods);

    void confirmDeliverySf(Transfer transfer, List<TransferGoods> goodsList);

    List<Transfer> getTransfered();

    List<Transfer> getTransferPush();

    List<Transfer> getImeiSf(String transferId, String skuCode);

    /**
     * 查询顺丰入库状态
     * @param transferId
     */
    void getTransferInSfStatus(String transferId);

    /**
     * 导出
     * @param criteria
     * @param page
     * @return
     */
    List<Map<String, String>> exportTransferList(Map<String, Object> criteria, Page page);

    /**
     * 顺丰接口，入库单
     * @param transfer
     * @return ServiceCtrlMessage
     */
    ServiceCtrlMessage purchaseOrder(Transfer transfer);

    /**
     * 配货
     * @param sv         SKU|IMEI
     * @param num        数量
     * @param transferId 调拨单ID
     * @return ServiceCtrlMessage
     */
    ServiceCtrlMessage picking(String sv, Integer num, Long transferId);

    /**
     * 扫描运单号，完成配货
     * @param transferId
     * @param logisticNo
     * @return
     */
    ServiceCtrlMessage dispatch(Long transferId, String logisticNo);
}
