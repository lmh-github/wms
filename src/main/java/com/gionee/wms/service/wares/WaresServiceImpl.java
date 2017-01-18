package com.gionee.wms.service.wares;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dao.StockDao;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.SkuBomDetail;
import com.gionee.wms.entity.Wares;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.vo.SkuVo;
import com.google.common.collect.Maps;

@Service("waresService")
public class WaresServiceImpl implements WaresService {
    private WaresDao waresDao;
    private StockDao stockDao;

    @Override
    public Wares getWares(Long waresId) {
        Validate.notNull(waresId);
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("id", waresId);
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        List<Wares> list = getWaresList(criteria, page);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException("数据异常");
        }
    }

    @Override
    public Wares getWaresWithAttrInfo(Long waresId) {
        Validate.notNull(waresId);
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("waresId", waresId);
        return waresDao.queryWaresWithAttrInfo(criteria);
    }

    @Override
    public List<Wares> getWaresList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        return waresDao.queryWaresList(criteria);
    }

    @Override
    public Integer getWaresListTotal(Map<String, Object> criteria) {
        return waresDao.queryWaresListTotal(criteria);
    }

    @Override
    public List<Wares> getWaresList(Map<String, Object> criteria) {
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        return getWaresList(criteria, page);
    }

    @Override
    public void addWares(Wares wares) throws ServiceException {
        try {
            wares.setCreateTime(new Date());
            wares.setEnabled(WmsConstants.ENABLED_TRUE);
            if (waresDao.addWares(wares) == 0) {
                throw new ServiceException("商品编码或商品名称重复");
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateWares(Wares wares) throws ServiceException {
        if (CollectionUtils.isNotEmpty(getSkuByWaresId(wares.getId()))) {
            // 基础商品已经生成SKU，与sku相关的属性被限制修改
            Wares oldWares = getWares(wares.getId());
            if (oldWares == null) {
                throw new ServiceException("商品不存在");
            }
            boolean canUpdate = true;
            if (!wares.getWaresName().equals(oldWares.getWaresName())) {
                canUpdate = false;
            } else if (!wares.getWaresCode().equals(oldWares.getWaresCode())) {
                canUpdate = false;
            } else if (wares.getAttrSet().getId().longValue() != oldWares.getAttrSet().getId().longValue()) {
                canUpdate = false;
            } else if (wares.getIndivEnabled().intValue() != oldWares.getIndivEnabled().intValue()) {
                canUpdate = false;
            } else if (!wares.getMeasureUnit().equals(oldWares.getMeasureUnit())) {
                canUpdate = false;
            }
            if (!canUpdate) {
                throw new ServiceException("商品已产生KU，只能修改其品牌与分类");
            }
        }
        try {
            if (waresDao.updateWares(wares) == 0) {
                throw new ServiceException("商品编码或商品名称重复");
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteWares(Long waresId) throws ServiceException {
        try {
            if (waresDao.deleteWares(waresId) == 0) {
                throw new ServiceException("商品已产生SKU，不能删除");
            }

        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addSku(Sku sku) throws ServiceException {
        Validate.notNull(sku);
        sku.setCreateTime(new Date());
        sku.setEnabled(WmsConstants.ENABLED_TRUE);
        // 判断sku编码、名称、条形码是否存在重复
        Map<String, Object> criteria = Maps.newHashMap();
        // if (StringUtils.isNotBlank(sku.getItemIds())) {
        // criteria.put("wares_id", sku.getWares().getId());
        // criteria.put("itemIds", sku.getItemIds());
        // }
        if (StringUtils.isNotBlank(sku.getSkuCode())) {
            criteria.put("skuCode", sku.getSkuCode());
        }
        if (StringUtils.isNotBlank(sku.getSkuName())) {
            criteria.put("skuFullName", sku.getSkuName());
        }
        if (StringUtils.isNotBlank(sku.getSkuBarcode())) {
            criteria.put("skuBarcode", sku.getSkuBarcode());
        }
        if (StringUtils.isNotBlank(sku.getMaterialCode())) {
            criteria.put("materialCode", sku.getMaterialCode());
        }
        List<Sku> skuList = getAnySkuList(criteria);
        if (CollectionUtils.isNotEmpty(skuList)) {
            throw new ServiceException("SKU名称、SKU编码、条形码、ERP物料号都不能重复");
        }
        try {
            // 添加sku
            if (waresDao.addSku(sku) == 0) {
                throw new ServiceException("相同属性的SKU已经存在，不能重复添加");
            }
            // 绑定sku属性
            if (StringUtils.isNotBlank(sku.getItemIds())) {
                Map<String, Object> skuItems = Maps.newHashMap();
                String[] itemIds = StringUtils.split(sku.getItemIds(), ",");
                skuItems.put("skuId", sku.getId());
                skuItems.put("itemList", itemIds);
                if (waresDao.addSkuItemRelation(skuItems) != itemIds.length) {
                    throw new ServiceException("SKU属性不能重复绑定");
                }
            }

            // 处理组合SKU
            /**
             * 1只能添加一个主商品
             * 2赠品不能为主商品
             */
            List<SkuBomDetail> skuBomList = sku.getSkuBomList();
            if (!skuBomList.isEmpty()) {
                int mainObject = 0;//主商品

                for (SkuBomDetail bom : skuBomList) {
                    int isBonus = bom.getIsBonus(); //0主商品 1赠品

                    if (isBonus == 0) {
                        mainObject++;
                    }
                    if (bom.getCSkuCode().charAt(0) == '1') {//1开头的是主商品
                        if (isBonus == 1) {//
                            throw new ServiceException(bom.getCSkuCode() + ":手机不能设置为赠品");
                        }
                    } else if (bom.getCSkuCode().charAt(0) == '2') {
                        if (isBonus == 0) {
                            throw new ServiceException(bom.getCSkuCode() + ":配件不能设置为主商品");
                        }
                    } else {
                        throw new ServiceException(bom.getCSkuCode() + ":此编码不合法");
                    }


                    bom.setPSkuCode(sku.getSkuCode());
                }
                if (mainObject != 1) {
                    throw new ServiceException("只能选择一个主商品");
                }
                Map<String, Object> skuBomMap = Maps.newHashMap();
                skuBomMap.put("itemList", skuBomList);
                waresDao.addSkuBomDetailRelation(skuBomMap);
            }

        } catch (DataAccessException e) {
            throw new ServiceException(e);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("添加SKU时出现未知异常", e);
        }
    }

    @Override
    public void updateSku(Sku sku) throws ServiceException {
        Validate.notNull(sku);
        // 判断SKU是否已产生库存
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("skuId", sku.getId());
        if (CollectionUtils.isNotEmpty(stockDao.queryStockList(criteria))) {
            // throw new ServiceException("已产生库存的SKU不能编辑");
            // 已产生库存的SKU只能更新有限参数
            Sku theSku = new Sku();
            theSku.setId(sku.getId());
            theSku.setRemark(sku.getRemark());
            try {
                waresDao.updateSku(theSku);
            } catch (DataAccessException e) {
                throw new ServiceException(e);
            }
            return;
        }

        // 判断sku编码、名称、条形码是否存在重复
        criteria.clear();
        Sku oldSku = getSku(sku.getId());
        if (oldSku == null) {
            throw new ServiceException("SKU不存在");
        }
        if (StringUtils.isNotBlank(sku.getSkuCode()) && !sku.getSkuCode().equals(StringUtils.defaultString(oldSku.getSkuCode()))) {
            criteria.put("skuCode", sku.getSkuBarcode());
        }
        if (StringUtils.isNotBlank(sku.getSkuName()) && !sku.getSkuName().equals(StringUtils.defaultString(oldSku.getSkuName()))) {
            criteria.put("skuName", sku.getSkuName());
        }
        if (StringUtils.isNotBlank(sku.getSkuBarcode()) && !sku.getSkuBarcode().equals(StringUtils.defaultString(oldSku.getSkuBarcode()))) {
            criteria.put("skuBarcode", sku.getSkuBarcode());
        }
        if (StringUtils.isNotBlank(sku.getMaterialCode()) && !sku.getMaterialCode().equals(StringUtils.defaultString(oldSku.getMaterialCode()))) {
            criteria.put("materialCode", sku.getMaterialCode());
        }
        if (criteria.size() != 0) {
            List<Sku> skuList = getAnySkuList(criteria);
            if (CollectionUtils.isNotEmpty(skuList)) {
                throw new ServiceException("SKU名称、SKU编码、条形码和ERP物料编码都不能重复");
            }
        }

        try {
            if (waresDao.updateSku(sku) == 0) {
                throw new ServiceException("SKU不存在");
            }

            // 处理组合SKU
            // 删除原有的
            waresDao.deleteSkuBomDetailRelation(sku.getSkuCode());
            List<SkuBomDetail> skuBomList = sku.getSkuBomList();
            if (!skuBomList.isEmpty()) {
                for (SkuBomDetail bom : skuBomList) {
                    bom.setPSkuCode(sku.getSkuCode());
                }
                Map<String, Object> skuBomMap = Maps.newHashMap();
                skuBomMap.put("itemList", skuBomList);
                waresDao.addSkuBomDetailRelation(skuBomMap);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteSku(Long skuId) throws ServiceException {
        try {
            Sku sku = getSku(skuId);
            if (waresDao.deleteSku(skuId) == 1) {
                waresDao.deleteSkuItemRelation(skuId);
                waresDao.deleteSkuBomDetailRelation(sku.getSkuCode());
            } else {
                throw new ServiceException("已产生库存的SKU不能删除");
            }

        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public List<Sku> getSkuList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        return waresDao.querySkuList(criteria);
    }

    /**
     * 根据商品ID获取SKU列表
     */
    public List<SkuVo> querySkuListByWaresId(Map<String, Object> criteria) throws ServiceException {
        return waresDao.querySkuListByWaresId(criteria);
    }

    /**
     * 根据商品Code获取SKU列表
     */
    public List<SkuVo> querySkuListByWaresCode(Map<String, Object> criteria) throws ServiceException {
        return waresDao.querySkuListByWaresCode(criteria);
    }

    @Override
    public List<Sku> getSkuWithAttrList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        return waresDao.querySkuWithAttrList(criteria);
    }

    @Override
    public Integer getSkuListTotal(Map<String, Object> criteria) {
        return waresDao.querySkuListTotal(criteria);
    }

    @Override
    public Integer getSkuWithAttrListTotal(Map<String, Object> criteria) {
        return waresDao.querySkuWithAttrListTotal(criteria);
    }

    @Override
    public List<Sku> getSkuListByMaterialCodes(List<String> materialCodes) {
        return waresDao.querySkuListByMaterialCodes(materialCodes);
    }

    @Override
    public Sku getSku(Long id) {
        try {
            Sku sku = waresDao.querySku(id);
            List<SkuBomDetail> skuBomList = waresDao.selectSkuBomDetailRelation(sku.getSkuCode());
            sku.setSkuBomList(skuBomList);
            return sku;
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Sku getSkuByCode(String skuCode) {
        return waresDao.querySkuBySkuCode(skuCode);
    }

    @Override
    public Sku getSkuByName(String skuCode) {
        Validate.notNull(skuCode);
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("skuFullName", skuCode);
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        List<Sku> list = getSkuList(criteria, page);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException("数据异常");
        }
    }

    @Override
    public Sku getSkuByBarcode(String skuBarcode) {
        Validate.notNull(skuBarcode);
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("skuBarcode", skuBarcode);
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        List<Sku> list = getSkuList(criteria, page);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException("数据异常");
        }
    }

    private List<Sku> getSkuByWaresId(Long waresId) {
        Validate.notNull(waresId);
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("waresId", waresId);
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        return getSkuList(criteria, page);
    }

    public Boolean indivCodeEnabled(Long skuId) throws ServiceException {
        Sku sku = getSku(skuId);
        if (sku == null) {
            throw new ServiceException("SKU不存在");
        }
        return WmsConstants.ENABLED_TRUE == sku.getWares().getIndivEnabled() ? true : false;
    }

    @Override
    public List<Sku> getAnySkuList(Map<String, Object> criteria) {
        return waresDao.queryAnySkuList(criteria);
    }

    @Autowired
    public void setWaresDao(WaresDao waresDao) {
        this.waresDao = waresDao;
    }

    @Autowired
    public void setStockDao(StockDao stockDao) {
        this.stockDao = stockDao;
    }

}
