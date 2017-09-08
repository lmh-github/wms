package com.gionee.wms.service.stock;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.PurPreRecvDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.PurPreRecvGoods;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("purchaseService")
public class PurchaseServiceImpl extends CommonServiceImpl implements PurchaseService {
    @Autowired
    private PurPreRecvDao purPreRecvDao;
    @Autowired
    private IndivDao indivDao;

    @Override
    public void addPreRecvWithGoodsList(PurPreRecv purPreRecv, List<PurPreRecvGoods> goodsList) {
        try {
            // 添加预收单
            purPreRecv.setPreRecvCode(getBizCode(PUR_PRE_RECV));
            purPreRecvDao.addPreRecv(purPreRecv);
            for (PurPreRecvGoods goods : goodsList) {
                goods.setPurPreRecv(purPreRecv);
            }
            // 添加预收商品
            purPreRecvDao.addGoodsList(goodsList);

            // 绑定预收个体
            List<PurPreRecvGoods> savedGoodsList = purPreRecvDao.queryGoodsListByPreRecvId(purPreRecv.getId());
            Map<String, PurPreRecvGoods> goodsMap = Maps.newHashMap();
            for (PurPreRecvGoods goods : savedGoodsList) {
                goodsMap.put(goods.getSkuId() + goods.getProductBatchNo(), goods);
            }
            for (PurPreRecvGoods goods : goodsList) {
                if (goodsMap.containsKey(goods.getSkuId() + goods.getProductBatchNo())) {
                    goods.setId(goodsMap.get(goods.getSkuId() + goods.getProductBatchNo()).getId());
                }
            }
            List<IndivFlow> indivFlowList = Lists.newArrayList();
            for (PurPreRecvGoods goods : goodsList) {
                if (CollectionUtils.isNotEmpty(goods.getIndivCodeList())) {
                    for (String indivCode : goods.getIndivCodeList()) {
                        IndivFlow indivFlow = new IndivFlow();

                        indivFlow.setIndivCode(indivCode);
                        indivFlow.setCaseCode(goods.getCaseCode());

                        indivFlow.setIndivCode(indivCode);
                        indivFlow.setSkuId(goods.getSkuId());
                        indivFlow.setSkuCode(goods.getSkuCode());
                        indivFlow.setSkuName(goods.getSkuName());
                        indivFlow.setMeasureUnit(goods.getMeasureUnit());
                        indivFlow.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());
                        indivFlow.setWarehouseId(goods.getPurPreRecv().getWarehouseId());
                        indivFlow.setWarehouseName(goods.getPurPreRecv().getWarehouseName());
                        indivFlow.setFlowType(IndivFlowType.PUR_PRE_RECV.getCode());
                        indivFlow.setFlowId(goods.getPurPreRecv().getId());
                        indivFlow.setFlowCode(goods.getPurPreRecv().getPreRecvCode());
                        indivFlow.setFlowGoodsId(goods.getId());
                        indivFlow.setFlowTime(new Date());
                        indivFlow.setProductBatchNo(goods.getProductBatchNo());
                        indivFlow.setEnabled(WmsConstants.ENABLED_TRUE);
                        indivFlowList.add(indivFlow);
                    }
                }
            }
            if (indivFlowList != null && indivFlowList.size() > 0) {
                indivDao.addIndivFlows(indivFlowList);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<PurPreRecv> getPurPreRecvList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return purPreRecvDao.queryPreRecvByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getPurPreRecvTotal(Map<String, Object> criteria) {
        return purPreRecvDao.queryPreRecvTotal(criteria);
    }

    @Override
    public PurPreRecv getPurPreRecv(Long id) {
        try {
            return purPreRecvDao.queryPurPreRecv(id);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PurPreRecv getPurPreRecvByPostingNo(String postingNo) {
        try {
            return purPreRecvDao.queryPurPreRecvByPostingNo(postingNo);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<PurPreRecvGoods> getPurPreRecvGoodsList(Long purPreRecvId) {
        try {
            return purPreRecvDao.queryGoodsListByPreRecvId(purPreRecvId);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

}
