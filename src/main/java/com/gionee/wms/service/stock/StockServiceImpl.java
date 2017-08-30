package com.gionee.wms.service.stock;

import com.gionee.wms.common.MyCollectionUtils;
import com.gionee.wms.common.UUIDGenerator;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.StockType;
import com.gionee.wms.dao.StockDao;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.web.client.OCClient;
import com.gionee.wms.web.client.OrderCenterClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.integration.warehouse.response.WmsRealTimeInventoryBalanceQueryResponse;
import com.sf.integration.warehouse.response.WmsRealTimeInventoryBalanceQueryResponseItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("stockService")
public class StockServiceImpl implements StockService {
    private static Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    @Autowired
    private StockDao stockDao;
    @Autowired
    private WaresDao waresDao;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private OrderCenterClient orderCenterClient;
    @Autowired
    private OCClient oCClient;

    @Override
    public List<Stock> getStockList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return stockDao.queryStockByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Integer getStockListTotal(Map<String, Object> criteria) {
        return stockDao.queryStockTotal(criteria);
    }

    public List<Stock> getStockList(Map<String, Object> criteria) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        try {
            return stockDao.queryStockList(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Stock> getStockList(Long skuId) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("skuId", skuId);
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        criteria.put("page", page);
        return stockDao.queryStockByPage(criteria);
    }

    @Override
    public Stock getStock(Long id) {
        if (id == null) {
            throw new ServiceException("参数错误");
        }
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("id", id);
            return stockDao.queryStock(paramMap);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    public Stock getStock(Long warehouseId, Long skuId) {
        if (warehouseId == null || skuId == null) {
            throw new ServiceException("参数错误");
        }
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("warehouseId", warehouseId);
            paramMap.put("skuId", skuId);
            return stockDao.queryStock(paramMap);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    public Stock getStock(Long warehouseId, String skuCode) {
        if (warehouseId == null || StringUtils.isBlank(skuCode)) {
            throw new ServiceException("参数错误");
        }
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("warehouseId", warehouseId);
            paramMap.put("skuCode", skuCode);
            return stockDao.queryStock(paramMap);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Stock getStock(String warehouseCode, Long skuId) {
        if (StringUtils.isBlank(warehouseCode) || skuId == null) {
            throw new ServiceException("参数错误");
        }
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("warehouseCode", warehouseCode);
            paramMap.put("skuId", skuId);
            return stockDao.queryStock(paramMap);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Stock getStock(String warehouseCode, String skuCode) {
        if (StringUtils.isBlank(warehouseCode) || StringUtils.isBlank(skuCode)) {
            throw new ServiceException("参数错误");
        }
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("warehouseCode", warehouseCode);
            paramMap.put("skuCode", skuCode);
            return stockDao.queryStock(paramMap);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void increaseStock(StockRequest stockRequest, boolean doDelta) throws ServiceException {
        // 校验请求参数
        validateRequest(stockRequest);

        // 检测待更新库存信息是否存在，不存在则需进行初始化
        Stock stock;
        if (stockRequest.getSkuId() != null) {
            stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuId());
        } else if (StringUtils.isNotBlank(stockRequest.getSkuCode())) {
            stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuCode());
        } else {
            throw new ServiceException("参数异常");
        }
        if (stock == null) {
            stock = initStock(stockRequest);
        }

        // 执行库存更新
        updateStockQuantity(stockRequest, stock, true);

        // 增量同步
        if (doDelta)
            doStockDelta(stockRequest, stock, true);

        // TODO:同步库存到外围系统
        handleStockChange(stockRequest, stock);
    }

    @Override
    public void decreaseStock(StockRequest stockRequest, boolean doDelta) throws ServiceException {
        // 校验请求参数
        validateRequest(stockRequest);

        stockRequest.setQuantity(-stockRequest.getQuantity());

        // 检测待更新库存信息是否存在，不存在则抛出异常
        Stock stock;
        if (stockRequest.getSkuId() != null) {
            stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuId());
        } else if (StringUtils.isNotBlank(stockRequest.getSkuCode())) {
            stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuCode());
        } else {
            throw new ServiceException("参数异常");
        }
        if (stock == null) {
            throw new ServiceException("库存信息不存在");
        }

        // 执行库存更新
        updateStockQuantity(stockRequest, stock, false);

        // IUNI的可销减少
        if (doDelta)
            doStockDelta(stockRequest, stock, true);

        // TODO:同步库存到外围系统
        handleStockChange(stockRequest, stock);
    }

    @Override
    public void convertStock(StockRequest stockRequest, boolean doDelta) throws ServiceException {
        // 校验请求参数
        validateRequest(stockRequest);

        // 检测待转换库存信息是否存在，不存在则抛出异常
        Stock stock;
        if (stockRequest.getSkuId() != null) {
            stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuId());
        } else if (StringUtils.isNotBlank(stockRequest.getSkuCode())) {
            stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuCode());
        } else {
            throw new ServiceException("参数异常");
        }
        if (stock == null) {
            // logger.error("库存信息不存在");
            throw new ServiceException("库存信息不存在");
        }

        // 执行库存转换
        doConvert(stockRequest, stock);

        if (doDelta) {
            if (StockType.STOCK_SALES.equals(stockRequest.getDestStockType())) {
                // 目标库存是可销，增加
                doStockDelta(stockRequest, stock, true);
            } else if (StockType.STOCK_SALES.equals(stockRequest.getSrcStockType())) {
                // 源库存是可销，减少
                doStockDelta(stockRequest, stock, false);
            }
        }

        // TODO:同步库存到外围系统
        handleStockChange(stockRequest, stock);
    }

    @Override
    public List<StockChange> getStockChangeList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        return stockDao.queryStockChangeList(criteria);
    }

    @Override
    public Integer getStockChangeListTotal(Map<String, Object> criteria) {
        return stockDao.queryStockChangeListTotal(criteria);
    }

    @Override
    public void updateStockLimit(Stock stock) throws ServiceException {
        if (stock.getLimitLower() < -1 || stock.getLimitUpper() < -1) {
            throw new ServiceException("参数异常");
        }
        if (stock.getLimitUpper() > -1 && stock.getLimitUpper() <= stock.getLimitLower()) {
            throw new ServiceException("上限数量必须大于下限数量");
        }
        try {
            stockDao.updateStockLimit(stock);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    private void doConvert(StockRequest stockRequest, Stock stock) {
        // 临时保存各库存数量
        Integer salesQuantity = stock.getSalesQuantity();
        Integer occupyQuantity = stock.getOccupyQuantity();
        Integer unsalesQuantity = stock.getUnsalesQuantity();
        // 初始化各库存数量
        stock.setSalesQuantity(null);
        stock.setOccupyQuantity(null);
        stock.setUnsalesQuantity(null);
        stock.setTotalQuantity(null);

        // 各库存明细
        List<StockChange> stockChangeList = new ArrayList<StockChange>();

        // 构建源库存变动流水
        StockChange srcStockChange = new StockChange();
        srcStockChange.setStock(stock);
        srcStockChange.setStockType(stockRequest.getSrcStockType().getCode());
        srcStockChange.setBizType(stockRequest.getStockBizType().getCode());
        srcStockChange.setOriginalCode(stockRequest.getOriginalCode());
        srcStockChange.setCreateTime(new Date());
        srcStockChange.setQuantity(-stockRequest.getQuantity());
        // 构建目标库存变动流水
        StockChange destStockChange = new StockChange();
        destStockChange.setStock(stock);
        destStockChange.setStockType(stockRequest.getDestStockType().getCode());
        destStockChange.setBizType(stockRequest.getStockBizType().getCode());
        destStockChange.setOriginalCode(stockRequest.getOriginalCode());
        destStockChange.setCreateTime(new Date());
        destStockChange.setQuantity(stockRequest.getQuantity());

        stockChangeList.add(srcStockChange);
        stockChangeList.add(destStockChange);

        // 根据源库存类型设置扣减后库存数量
        switch (stockRequest.getSrcStockType()) {
            case STOCK_SALES:// 可销售库存
                // 检测库存是否不足
                if (salesQuantity + stock.getNonDefectivePl() - stockRequest.getQuantity() < 0) {
                    throw new ServiceException("可销售库存不够");
                }
                srcStockChange.setOpeningStock(salesQuantity);
                srcStockChange.setClosingStock(salesQuantity - stockRequest.getQuantity());
                stock.setSalesQuantity(salesQuantity - stockRequest.getQuantity());
                break;
            case STOCK_OCCUPY:// 占用库存
                // 检测库存是否不足
                if (occupyQuantity - stockRequest.getQuantity() < 0) {
                    throw new ServiceException("占用库存不够");
                }
                srcStockChange.setOpeningStock(occupyQuantity);
                srcStockChange.setClosingStock(occupyQuantity - stockRequest.getQuantity());
                stock.setOccupyQuantity(occupyQuantity - stockRequest.getQuantity());
                break;
            case STOCK_UNSALES:// 不可销售库存
                // 检测库存是否不足
                if (unsalesQuantity + stock.getDefectivePl() - stockRequest.getQuantity() < 0) {
                    throw new ServiceException("不可销售库存不够");
                }
                srcStockChange.setOpeningStock(unsalesQuantity);
                srcStockChange.setClosingStock(unsalesQuantity - stockRequest.getQuantity());
                stock.setUnsalesQuantity(unsalesQuantity - stockRequest.getQuantity());
                break;
            default:
                throw new ServiceException("库存类型不存在");

        }

        // 根据目标库存类型设置增加后库存数量
        switch (stockRequest.getDestStockType()) {
            case STOCK_SALES:// 可销售库存
                destStockChange.setOpeningStock(salesQuantity);
                destStockChange.setClosingStock(salesQuantity + stockRequest.getQuantity());
                stock.setSalesQuantity(salesQuantity + stockRequest.getQuantity());
                break;
            case STOCK_OCCUPY:// 占用库存
                destStockChange.setOpeningStock(occupyQuantity);
                destStockChange.setClosingStock(occupyQuantity + stockRequest.getQuantity());
                stock.setOccupyQuantity(occupyQuantity + stockRequest.getQuantity());
                break;
            case STOCK_UNSALES:// 不可销售库存
                destStockChange.setOpeningStock(unsalesQuantity);
                destStockChange.setClosingStock(unsalesQuantity + stockRequest.getQuantity());
                stock.setUnsalesQuantity(unsalesQuantity + stockRequest.getQuantity());
                break;
            default:
                throw new ServiceException("库存类型不存在");
        }

        try {
            // 设置库存同步状态为未同步
            stock.setSyncStatus(0);
            // 更新各库存数量
            if (stockDao.updateStockQuantity(stock) == 0) {
                throw new OptimisticLockingFailureException("数据版本异常");
            }
            // 批量保存库存变化流水
            stockDao.addStockChange(stockChangeList);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    // 库存请求，库存实体， 是否增加
    private void updateStockQuantity(StockRequest stockRequest, Stock stock, boolean incr) {
        // 临时保存各库存数量
        Integer salesQuantity = stock.getSalesQuantity();
        Integer occupyQuantity = stock.getOccupyQuantity();
        Integer unsalesQuantity = stock.getUnsalesQuantity();

        // 初始化各库存数量
        stock.setSalesQuantity(null);
        stock.setOccupyQuantity(null);
        stock.setUnsalesQuantity(null);

        // 各库存明细
        List<StockChange> stockChangeList = new ArrayList<StockChange>();

        // 构建库存变动流水
        StockChange stockChange = new StockChange();
        stockChange.setStock(stock);
        stockChange.setStockType(stockRequest.getDestStockType().getCode());
        stockChange.setBizType(stockRequest.getStockBizType().getCode());
        stockChange.setOriginalCode(stockRequest.getOriginalCode());
        stockChange.setCreateTime(new Date());
        stockChange.setQuantity(stockRequest.getQuantity());

        // 构建总库存变动流水
        StockChange totalStockChange = new StockChange();
        totalStockChange.setStock(stock);
        totalStockChange.setStockType(StockType.STOCK_TOTAL.getCode());
        totalStockChange.setBizType(stockRequest.getStockBizType().getCode());
        totalStockChange.setOriginalCode(stockRequest.getOriginalCode());
        totalStockChange.setCreateTime(new Date());
        totalStockChange.setQuantity(stockRequest.getQuantity());
        totalStockChange.setOpeningStock(stock.getTotalQuantity());
        totalStockChange.setClosingStock(stock.getTotalQuantity() + stockRequest.getQuantity());

        stockChangeList.add(stockChange);
        stockChangeList.add(totalStockChange);

        // 根据不同的库存类型设置变动后库存数量并记录变动流水
        switch (stockRequest.getDestStockType()) {
            case STOCK_SALES:// 可销售库存
                // 检测库存是否不足
                if (!incr && salesQuantity + stock.getNonDefectivePl() + stockRequest.getQuantity() < 0) {
                    throw new ServiceException("可销售库存不够");
                }
                if (stock.getTotalQuantity() + stock.getNonDefectivePl() + stockRequest.getQuantity() < 0) {
                    throw new ServiceException("总库存不够");
                }
                stockChange.setOpeningStock(salesQuantity);
                stockChange.setClosingStock(salesQuantity + stockRequest.getQuantity());
                stock.setSalesQuantity(salesQuantity + stockRequest.getQuantity());
                break;
            case STOCK_OCCUPY:// 占用库存
                // 检测库存是否不足
                if (occupyQuantity + stockRequest.getQuantity() < 0) {
                    throw new ServiceException("占用库存不够");
                }
                if (stock.getTotalQuantity() + stockRequest.getQuantity() < 0) {
                    throw new ServiceException("总库存不够");
                }
                stockChange.setOpeningStock(occupyQuantity);
                stockChange.setClosingStock(occupyQuantity + stockRequest.getQuantity());
                stock.setOccupyQuantity(occupyQuantity + stockRequest.getQuantity());
                break;
            case STOCK_UNSALES:// 不可销售库存
                stockChange.setOpeningStock(unsalesQuantity);
                stockChange.setClosingStock(unsalesQuantity + stockRequest.getQuantity());
                stock.setUnsalesQuantity(unsalesQuantity + stockRequest.getQuantity());
                // 检测库存是否不足
                if (unsalesQuantity + stock.getDefectivePl() + stockRequest.getQuantity() < 0) {
                    throw new ServiceException("不可销售库存不够");
                }
                if (stock.getTotalQuantity() + stock.getDefectivePl() + stockRequest.getQuantity() < 0) {
                    throw new ServiceException("总库存不够");
                }
                break;
            default:
                throw new ServiceException("库存类型不存在");
        }

        // 设置变动后总库存数量
        stock.setTotalQuantity(stock.getTotalQuantity() + stockRequest.getQuantity());

        try {
            // 设置库存同步状态为未同步
            stock.setSyncStatus(0);
            // 更新各库存数量
            if (stockDao.updateStockQuantity(stock) == 0) {
                throw new OptimisticLockingFailureException("数据版本异常");
            }
            // 批量保存库存变化流水
            stockDao.addStockChange(stockChangeList);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    private Stock initStock(StockRequest stockRequest) {
        Stock stock;
        Sku sku = null;
        // 检测SKU是否存在
        if (stockRequest.getSkuId() != null) {
            sku = getSkuById(stockRequest.getSkuId());
        } else if (StringUtils.isNotBlank(stockRequest.getSkuCode())) {
            sku = getSkuByCode(stockRequest.getSkuCode());
        }
        if (sku == null) {
            throw new ServiceException("SKU不存在");
        }
        stock = new Stock(stockRequest.getWarehouseId(), sku.getId());
        stock.setNonDefectivePl(0);
        stock.setDefectivePl(0);
        addStock(stock);
        return stock;
    }

    private void validateRequest(StockRequest stockRequest) {
        if (stockRequest == null) {
            throw new ServiceException("参数异常");
        }
        List params = Lists.newArrayList(stockRequest.getWarehouseId(), stockRequest.getDestStockType(), stockRequest
            .getQuantity(), stockRequest.getStockBizType());
        if (!MyCollectionUtils.noBlankElements(params)) {
            throw new ServiceException("参数异常");
        }
        if (stockRequest.getQuantity() <= 0) {
            throw new ServiceException("库存更新数目必须大于0");
        }

    }

    private void addStock(Stock stock) throws ServiceException {
        try {
            // stock.setStockTotal(stock.getStockSales());
            stockDao.addStock(stock);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    private Sku getSkuByCode(String skuCode) {
        try {
            return waresDao.querySkuBySkuCode(skuCode);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    private Sku getSkuById(Long id) {
        try {
            return waresDao.querySku(id);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /*
     * 异步处理库存变动事件
     */
    private void handleStockChange(StockRequest stockRequest, Stock stock) {
        // 同步库存
        List<Stock> stockList = new ArrayList<Stock>();
//		Stock stock = null;
//		if (stockRequest.getSkuId() != null) {
//			stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuId());
//		} else if (StringUtils.isNotBlank(stockRequest.getSkuCode())) {
//			stock = getStock(stockRequest.getWarehouseId(), stockRequest.getSkuCode());
//		}
        // 只有当可销售库存变动的时候才进行通知
        if ((stockRequest.getSrcStockType() != null && stockRequest
            .getSrcStockType().getCode().equals(
                StockType.STOCK_SALES.getCode()))
            || (stockRequest.getDestStockType() != null && stockRequest
            .getDestStockType().getCode().equals(
                StockType.STOCK_SALES.getCode()))) {
            stockList.add(stock);
            try {
                taskExecutor.execute(new ProcExtBizTask(stockList, stock
                    .getWarehouse().getWarehouseCode(), stock
                    .getWarehouse().getId()));
            } catch (TaskRejectedException e) {
                // 屏蔽异常
                logger.error("异步同步库存出错", e);
            }
        }
    }

    private class ProcExtBizTask implements Runnable {
        private List<Stock> stockList;
        private String warehouseCode;// 仓库编号
        private Long warehouseId;

        public ProcExtBizTask(List<Stock> stockList, String warehouseCode, Long warehouseId) {
            this.stockList = stockList;
            this.warehouseCode = warehouseCode;
            this.warehouseId = warehouseId;
        }

        public void run() {
            try {
                if (warehouseCode == null || warehouseCode.equals("")) {

                } else {
                    // 根据仓库编码同步库存至相应系统，根据配置同步仓库库存，暂时不支持同步至天猫
                    if (warehouseCode.equals(WmsConstants.OFFICIAL_GIONEE_HOUSE_CODE)) {
                        orderCenterClient.syncStock(stockList, warehouseId);
                    } else if (warehouseCode.equals(WmsConstants.OFFICIAL_IUNI_HOUSE_CODE)) {
//						oCClient.syncStock(stockList, warehouseId);
                    } else if (warehouseCode.equals(WmsConstants.TMALL_GIONEE_HOUSE_CODE)) {

                    } else if (warehouseCode.equals(WmsConstants.TMALL_IUNI_HOUSE_CODE)) {

                    }
                }
            } catch (Exception e) {
                // 屏蔽异常
                logger.error("同步库存出错", e);
            }
        }
    }

    @Override
    public void insertStockDelta(StockDelta stockDelta) throws ServiceException {
        if (null == stockDelta) {
            throw new ServiceException("需要增量记录");
        }
        stockDelta.setId(UUIDGenerator.getUUID());
        stockDao.insertStockDelta(stockDelta);
    }

    /**
     * 记录变化增量
     *
     * @param stockRequest 库存请求
     * @param stock        库存记录
     * @param incr         是否增量
     */
    private void doStockDelta(StockRequest stockRequest, Stock stock, boolean incr) throws ServiceException {
        if (WmsConstants.OFFICIAL_IUNI_HOUSE_CODE.equals(stock.getWarehouse().getWarehouseCode())
            && (StockType.STOCK_SALES.equals(stockRequest.getSrcStockType())
            || StockType.STOCK_SALES.equals(stockRequest.getDestStockType()))) {
            // IUNI的库存，源或目标为可销库存才进行增量记录
            StockDelta stockDelta = new StockDelta();
            try {
                stockDelta.setBizType(stockRequest.getStockBizType().getCode());
                stockDelta.setCreateTime(new Date());
                if (incr) {
                    stockDelta.setQuantity(stockRequest.getQuantity());
                } else {
                    stockDelta.setQuantity(-stockRequest.getQuantity());
                }
                stockDelta.setStockType(stockRequest.getDestStockType()
                    .getCode());
                stockDelta.setSkuCode(stock.getSku().getSkuCode());
                stockDelta.setWarehouseId(stockRequest.getWarehouseId());
                stockDelta.setOriginalCode(stockRequest.getOriginalCode());
                stockDelta.setGoodsSid(stockRequest.getGoodsSid());
                insertStockDelta(stockDelta);
            } catch (DataAccessException e) {
                throw new ServiceException("增量产生异常: " + e.getMessage());
            }
        }
    }

    public List<Stock> queryStockList(Map<String, Object> criteria) {
        return stockDao.queryStockList(criteria);
    }

    public List<StockDelta> queryStockDelta() {
        return stockDao.queryStockDelta();
    }

    public void syncStockDelta(List<StockDelta> deltaList) {
        if (null != deltaList && deltaList.size() > 0) {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("deltaList", deltaList);
            stockDao.deleteStockDelta(paramMap);
            oCClient.syncStock(deltaList); // 网络同步，失败则抛出错误回滚
        }
    }

    @Override
    public void increaseStock(StockRequest stockRequest) throws ServiceException {
        increaseStock(stockRequest, false);
    }

    @Override
    public void decreaseStock(StockRequest stockRequest) throws ServiceException {
        decreaseStock(stockRequest, false);
    }

    @Override
    public void convertStock(StockRequest stockRequest) throws ServiceException {
        convertStock(stockRequest, false);
    }

    @Override
    public void addDailyStock(Date startDate, Date endDate) {// 目前只适用间隔一日的情况，主要是
        // TODO Auto-generated method stub
        if (startDate == null || endDate == null) {
            throw new RuntimeException("日期对象不能为null");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // String startDateStr = sdf.format(startDate);
        // String endDateStr = sdf.format(endDate);
        paramMap.put("date", startDate);
        paramMap.put("stockType", WmsConstants.StockType.STOCK_TOTAL.getCode());
        paramMap.put("dailyStockTempType",
            WmsConstants.DailyStockTempType.START_QTY.getCode());
        stockDao.addQty(paramMap);// 形成期初临时数据
        paramMap.clear();
        paramMap.put("date", endDate);
        paramMap.put("stockType", WmsConstants.StockType.STOCK_OCCUPY.getCode());
        paramMap.put("dailyStockTempType",
            WmsConstants.DailyStockTempType.OCCUPY_QTY.getCode());
        stockDao.addQty(paramMap);// 形成占用未出库临时数据
        paramMap.clear();
        paramMap.put("date", endDate);
        paramMap.put("stockType", WmsConstants.StockType.STOCK_TOTAL.getCode());
        paramMap.put("dailyStockTempType",
            WmsConstants.DailyStockTempType.END_QTY.getCode());
        stockDao.addQty(paramMap);// 形成期末临时数据
        paramMap.clear();
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("stockType", WmsConstants.StockType.STOCK_TOTAL.getCode());
        stockDao.addOutQty(paramMap);// 形成本期出库临时数据
        paramMap.clear();
        paramMap.put("reportDate", startDate);
        paramMap.put("createDate", new Date());
        stockDao.addDailyStock(paramMap);
        stockDao.deleteDailyStockTemp();// 清掉过渡数据
    }

    @Override
    public Integer getDailyStockTotalCount(Map<String, Object> criteria) {
        // TODO Auto-generated method stub
        return stockDao.queryDailyStockTotalCount(criteria);
    }

    @Override
    public List<DailyStock> getDailyStockList(Map<String, Object> criteria,
                                              Page page) {
        // TODO Auto-generated method stub
        criteria.put("page", page);
        return stockDao.queryDailyStockByPage(criteria);
    }

    @Override
    public List<DailyStock> getDailyStockList() {
        // TODO Auto-generated method stub
        return stockDao.queryDailyStockList();
    }

    @Override
    @Transactional
    public void updateBatchByResponses(List<WmsRealTimeInventoryBalanceQueryResponse> responses, Map<String, String> skuMapping) {
        if (!CollectionUtils.isEmpty(responses)) {
            List<Stock> stocks = Lists.newArrayList();
            for (WmsRealTimeInventoryBalanceQueryResponse response : responses) {
                if (response != null && response.getResult() && !CollectionUtils.isEmpty(response.getList())) {
                    for (WmsRealTimeInventoryBalanceQueryResponseItem responseItem : response.getList()) {
                        Stock stock = new Stock();
                        Sku sku = new Sku();
                        sku.setSkuCode(skuMapping.get(responseItem.getSku_no()));

                        stock.setSku(sku);
                        stock.setTotalQuantity(responseItem.getTotal_stock().intValue());
                        stock.setSalesQuantity(responseItem.getAvailable_stock().intValue());
                        stock.setOccupyQuantity(responseItem.getOn_hand_stock().intValue());
                        stock.setUnsalesQuantity(responseItem.getIn_transit_stock().intValue());
                        stocks.add(stock);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(stocks)) {
                stockDao.updateBatchBySkuCode(stocks);
            }
        }
    }
}
