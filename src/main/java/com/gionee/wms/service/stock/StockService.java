package com.gionee.wms.service.stock;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.DailyStock;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.entity.StockChange;
import com.gionee.wms.entity.StockDelta;
import com.gionee.wms.service.ServiceException;
import com.sf.integration.warehouse.response.WmsRealTimeInventoryBalanceQueryResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StockService {

    /**
     * 分页取库存列表.
     */
    List<Stock> getStockList(Map<String, Object> criteria, Page page);

    /**
     * 取库存列表总数.
     */
    Integer getStockListTotal(Map<String, Object> criteria);

    /**
     * 按条件取库存列表
     */
    List<Stock> getStockList(Map<String, Object> criteria);

    /**
     * 根据仓库与SKU ID取库存信息.
     */
    Stock getStock(String warehouseCode, Long skuId);

    /**
     * 根据仓库与SKU编码取库存信息.
     */
    Stock getStock(String warehouseCode, String skuCode);

    /**
     * 取库存信息.
     */
    Stock getStock(Long id);

    /**
     * 根据sku id取库存信息列表.
     */
    List<Stock> getStockList(Long skuId);

    /**
     * 增加库存
     */
    void increaseStock(StockRequest stockRequest) throws ServiceException;

    /**
     * 增加库存，同时生成增量数据
     */
    void increaseStock(StockRequest stockRequest, boolean doDelta) throws ServiceException;

    /**
     * 减少库存
     */
    void decreaseStock(StockRequest stockRequest) throws ServiceException;

    /**
     * 减少库存，同时生成增量数据
     */
    void decreaseStock(StockRequest stockRequest, boolean doDelta) throws ServiceException;

    /**
     * 转换库存
     */
    void convertStock(StockRequest stockRequest) throws ServiceException;

    /**
     * 转换库存，同时生成增量数据
     */
    void convertStock(StockRequest stockRequest, boolean doDelta) throws ServiceException;

    /**
     * 取库存流水列表.
     */
    List<StockChange> getStockChangeList(Map<String, Object> criteria, Page page);

    /**
     * 取库存流水列表总数.
     */
    Integer getStockChangeListTotal(Map<String, Object> criteria);

    // /**
    // * 取库存个体列表.
    // */
    // List<Indiv> getIndivList(Long stockId);

    /**
     * 更新安全库存数量
     */
    void updateStockLimit(Stock stock) throws ServiceException;

    /**
     * 插入库存增量记录
     */
    void insertStockDelta(StockDelta stockDelta);

    /**
     * 查询库存
     */
    List<Stock> queryStockList(Map<String, Object> criteria);

    /**
     * 查询增量
     */
    List<StockDelta> queryStockDelta();

    /**
     * 同步库存
     */
    public void syncStockDelta(List<StockDelta> deltaList);

    /**
     * 新增每日库存信息
     *
     * @param startDate
     * @param endDate
     */
    void addDailyStock(Date startDate, Date endDate);

    /**
     * 获取每日库存信息总数据条数
     *
     * @param criteria
     * @return
     */
    Integer getDailyStockTotalCount(Map<String, Object> criteria);

    /**
     * 获取每日库存信息 分页
     *
     * @param criteria
     * @param page
     * @return
     */
    List<DailyStock> getDailyStockList(Map<String, Object> criteria, Page page);

    /**
     * 获取每日库存信息
     *
     * @return
     */
    List<DailyStock> getDailyStockList();


    /**
     * 批量更新库存
     *
     * @param responses 顺丰响应参数集合
     * @param skuMapping 响应商品映射
     */
    void updateBatchByResponses(List<WmsRealTimeInventoryBalanceQueryResponse> responses, Map<String, String> skuMapping);

    /**
     * 查询东莞库存
     * @param skuCode
     * @return
     */
    Integer getQuantity(Map<String, Object> skuCode);

    /**
     * 批量更新
     * @param list
     */
    void updateBatch(List<Stock> list);
}
