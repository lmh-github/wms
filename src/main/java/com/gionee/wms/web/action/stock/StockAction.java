package com.gionee.wms.web.action.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.InventoryStatusEnum;
import com.gionee.wms.common.WmsConstants.SkuMapOuterCodeEnum;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.service.wares.SkuMapService;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.sf.integration.warehouse.request.WmsInventoryBalancePageQueryRequest;
import com.sf.integration.warehouse.request.WmsRealTimeInventoryBalanceQueryRequest;
import com.sf.integration.warehouse.response.WmsInventoryBalancePageQueryResponse;
import com.sf.integration.warehouse.response.WmsInventoryBalancePageQueryResponseItem;
import com.sf.integration.warehouse.response.WmsRealTimeInventoryBalanceQueryResponse;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxx
 */
@Data
@Controller("StockAction")
@Scope("prototype")
public class StockAction extends CrudActionSupport<Stock> {

    private static final long serialVersionUID = -8940733721506429911L;

    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StockService stockService;
    @Autowired
    private WaresService waresService;
    @Autowired
    private SFWebService sfWebService;
    @Autowired
    private SkuMapService skuMapService;


    /** 库存列表 */
    private List<Stock> stockList;
    private Long warehouseId;
    private Long skuId;
    /** 库存model */
    private Stock stock;
    /** 商品分类 */
    private String catPath;
    /** 库存ID */
    private Long id;
    private String skuCode;
    private String skuName;
    /** 商品分类 */
    private List<Category> categoryList;
    private List<Warehouse> warehouseList;
    private Page page = new Page();
    private String exports;

    /**
     * 查询SKU列表
     */
    @Override
    public String list() throws Exception {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("skuId", skuId);
        criteria.put("warehouseId", warehouseId);
        criteria.put("catPath", StringUtils.defaultIfBlank(catPath, null));
        criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
        criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
        if ("1".equals(exports)) {
            stockList = stockService.getStockList(criteria);
            return exportStock();
        } else {
            int totalRow = stockService.getStockListTotal(criteria);
            page.setTotalRow(totalRow);
            page.calculate();
            criteria.put("page", page);
            stockList = stockService.getStockList(criteria, page);
            return SUCCESS;
        }
    }

    /**
     * 导出库存信息
     */
    private String exportStock() {
        if (CollectionUtils.isEmpty(stockList)) {
            throw new ServiceException("库存数据不存在");
        }
        List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
        int count = 0;
        for (Stock item : stockList) {
            Map<String, String> repeatData = new HashMap<String, String>();
            repeatData.put("SKU_CODE", item.getSku().getSkuCode());
            repeatData.put("WAREHOUSE_NAME", item.getWarehouse().getWarehouseName());
            repeatData.put("SKU_NAME", item.getSku().getSkuName());
            repeatData.put("MATERIAL_CODE", item.getSku().getMaterialCode());
            repeatData.put("UNIT", item.getSku().getWares().getMeasureUnit());
            Integer salesQuantity = item.getSalesQuantity() == null ? 0 : item.getSalesQuantity();
            Integer occupyQuantity = item.getOccupyQuantity() == null ? 0 : item.getOccupyQuantity();
            Integer unsalesQuantity = item.getUnsalesQuantity() == null ? 0 : item.getUnsalesQuantity();
            repeatData.put("OCCUPY_QUANTITY", occupyQuantity + "");
            repeatData.put("UNSALES_QUANTITY", unsalesQuantity + "");
            repeatData.put("TOTAL_QUAN", (salesQuantity + occupyQuantity + unsalesQuantity) + "");
            repeatData.put("SALES_QUAN", salesQuantity + "");
            repeatData.put("CAT_NAME", item.getSku().getWares().getCategory().getCatName());
            sheetData.add(repeatData);
            count++;
            if (count >= 10000) {
                break; // 大于10000条终止，防止内存溢出
            }
        }
        ExcelModule excelModule = new ExcelModule(sheetData);
        HttpServletResponse response = ServletActionContext.getResponse();
        // 清空输出流
        response.reset();
        // 设置响应头和下载保存的文件名
        response.setHeader("content-disposition", "attachment;filename=stock_exp_list.xls");
        // 定义输出类型
        response.setContentType("APPLICATION/msexcel");
        OutputStream out = null;
        try {
            String templeteFile = ActionUtils.getProjectPath() + "/export/stock_exp_template.xls";
            String tempFile = ActionUtils.getProjectPath() + "/export/stock_exp_list.xls";
            System.out.println(templeteFile + " " + tempFile);
            File file = ExcelExpUtil.expExcel(excelModule, templeteFile, tempFile);
            out = response.getOutputStream();
            FileUtils.copyFile(file, out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // 返回null,可防止报异常：getOutputStream() has already been called for this
        // response
        return null;
    }

    @Override
    public void prepareList() {
        // 初始化属性对象
        categoryList = categoryService.getCategoryList(null);

        warehouseList = warehouseService.getValidWarehouses();
    }

    public String getSkuInfo() {
        Validate.notNull(skuCode);
        Sku sku = waresService.getSkuByCode(skuCode);
        JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
        ActionUtils.outputJson(jsonUtils.toJson(sku));
        return null;
    }

    /**
     * 进入安全库存设置页面
     */
    public String inputLimit() {
        return "input_limit";
    }

    public void prepareInputLimit() {
        Validate.notNull(id);
        // 初始化Model对象
        stock = stockService.getStock(id);
    }

    /**
     * 更新安全库存
     */
    public String updateLimit() {
        Validate.notNull(stock);
        if (stock.getLimitLower() == null) {
            stock.setLimitLower(-1);
        }
        if (stock.getLimitUpper() == null) {
            stock.setLimitUpper(-1);
        }
        try {
            stockService.updateStockLimit(stock);
            ajaxSuccess("设置成功");
        } catch (ServiceException e) {
            logger.error("设置安全库存数量时出错", e);
            ajaxError("设置失败：" + e.getMessage());
        }
        return null;
    }

    public void prepareUpdateLimit() {
        Validate.notNull(id);
        // 初始化Model对象
        stock = stockService.getStock(id);
    }

    /**
     * 打开创建或编辑页面
     */
    @Override
    public String input() throws Exception {
        return INPUT;
    }

    /**
     * 添加库存信息
     */
    @Override
    public String add() throws Exception {
        return null;
    }

    /**
     * 编辑SKU
     */
    @Override
    public String update() throws Exception {
        return null;
    }

    /**
     * 删除SKU
     */
    @Override
    public String delete() throws Exception {
        return null;
    }

    @Override
    public void prepareAdd() throws Exception {
        stock = new Stock();
    }

    @Override
    public void prepareInput() throws Exception {
        // 初始化仓库列表
        warehouseList = warehouseService.getValidWarehouses();

    }


    @Override
    public void prepareUpdate() throws Exception {
        prepareModel();
    }

    private void prepareModel() {
    }


    @Override
    public Stock getModel() {

        return stock;
    }

    public String reset() {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("warehouseId", 1643);
        List<Stock> list = stockService.getStockList(criteria);
        for (Stock stock : list) {
            String skuCode = stock.getSku().getSkuCode();
            criteria = Maps.newHashMap();
            criteria.put("skuCode", skuCode);
            criteria.put("stockType", "4");

            Integer totalQuantity = stockService.getQuantity(criteria);
            criteria = Maps.newHashMap();
            criteria.put("skuCode", skuCode);
            criteria.put("stockType", "1");
            Integer salesQuantity = stockService.getQuantity(criteria);

            if (totalQuantity == null) {
                totalQuantity = 0;
            }
            if (salesQuantity == null) {
                salesQuantity = 0;
            }
            stock.setTotalQuantity(totalQuantity);
            stock.setSalesQuantity(salesQuantity);
        }
        stockService.updateBatch(list);
        return "refresh";
    }


    /**
     * 刷新所有顺丰仓库存
     * @return
     */
    public String refresh() {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("sfWarehouseName", "顺丰");
        List<Warehouse> warehouses = warehouseService.getWarehouseList(criteria);
        if (!CollectionUtils.isEmpty(warehouses)) {
            // 查询商品映射
            Map<String, String> skuMapping = Maps.newHashMap();
            List<WmsRealTimeInventoryBalanceQueryRequest> requests = Lists.newArrayList();
            for (Warehouse warehouse : warehouses) {
                List<String> itemIds = Lists.newArrayList();
                WmsRealTimeInventoryBalanceQueryRequest request = new WmsRealTimeInventoryBalanceQueryRequest(
                    itemIds,
                    WmsConstants.SF_COMPANY,
                    InventoryStatusEnum.ZHENGPIN.getCode(),
                    warehouse.getWarehouseCode()
                );
                List<String> stockIds = getStockIds(warehouse);
                if (!CollectionUtils.isEmpty(stockIds)) {
                    List<SkuMap> skuMaps = skuMapService.querySkuMapBySkuCodes(stockIds, SkuMapOuterCodeEnum.SF.getCode());
                    for (SkuMap skuMap : skuMaps) {
                        skuMapping.put(skuMap.getOuterSkuCode(), skuMap.getSkuCode());
                        itemIds.add(skuMap.getOuterSkuCode());
                    }
                    requests.addAll(split(request));
                }
            }

            if (!CollectionUtils.isEmpty(requests)) {
                List<WmsRealTimeInventoryBalanceQueryResponse> responses = sfWebService.outsideToLscmService(
                    WmsRealTimeInventoryBalanceQueryRequest.class,
                    WmsRealTimeInventoryBalanceQueryResponse.class,
                    requests
                );
                stockService.updateBatchByResponses(responses, skuMapping);
            }
        }
        return "refresh";
    }

    /**
     * 顺丰接口仅支持每次20条数据查询 为此拆分成多个WmsRealTimeInventoryBalanceQueryRequest对象
     * @param request
     * @return
     */
    private List<WmsRealTimeInventoryBalanceQueryRequest> split(WmsRealTimeInventoryBalanceQueryRequest request) {
        List<WmsRealTimeInventoryBalanceQueryRequest> list = new ArrayList<>();
        List<String> tmpSkuList = Lists.newArrayList();
        WmsRealTimeInventoryBalanceQueryRequest r = new WmsRealTimeInventoryBalanceQueryRequest(request);
        r.setItemList(tmpSkuList);
        list.add(r);

        List<String> skuList = request.getItemList();

        for (int i = 0; i < skuList.size(); i++) {
            if (i != 0 && i % 20 == 0) {
                tmpSkuList = Lists.newArrayList();
                r = new WmsRealTimeInventoryBalanceQueryRequest(request);
                r.setItemList(tmpSkuList);
                list.add(r);
            }
            tmpSkuList.add(skuList.get(i));
        }
        return list;
    }

    private List<String> getStockIds(Warehouse warehouse) {
        Map<String, Object> criteria;
        List<String> stockIds = Lists.newArrayList();
        criteria = Maps.newHashMap();
        criteria.put("warehouseId", warehouse.getId());
        List<Stock> list = stockService.getStockList(criteria);

        for (Stock stock : list) {
            stockIds.add(stock.getSku().getSkuCode());
        }
        return stockIds;
    }

    /**
     * 根据SKU查询顺丰实时库存
     * @return String
     */
    public String queryRealTimeInvBalance() {
        ActionContext context = ActionContext.getContext();
        try {
            SkuMap skuMap = skuMapService.getSkuMapBySkuCode(skuCode, SkuMapOuterCodeEnum.SF.getCode());
            if (skuMap == null) {
                logger.info(MessageFormat.format("页面查询库存；SKU:{0}，没有对应是顺丰SKU", skuCode));
                context.put("inv_response", null);
                return "sf_inv_list";
            }

            WmsRealTimeInventoryBalanceQueryRequest request = new WmsRealTimeInventoryBalanceQueryRequest(
                Lists.newArrayList(skuMap.getOuterSkuCode()),
                WmsConstants.SF_COMPANY,
                InventoryStatusEnum.ZHENGPIN.getCode(),
                ServletActionContext.getRequest().getParameter("warehouse")
            );
            WmsRealTimeInventoryBalanceQueryResponse response = sfWebService.outsideToLscmService(WmsRealTimeInventoryBalanceQueryRequest.class, WmsRealTimeInventoryBalanceQueryResponse.class, request);
            if (response == null || response.getResult() == false || CollectionUtils.isEmpty(response.getList())) {
                context.put("inv_response", null);
                return "sf_inv_list";
            }

            context.put("inv_response", response.getList().get(0));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("库存查询中，查询顺丰实时库存异常！", e);
            context.put("inv_response", null);
        }

        return "sf_inv_list";
    }

    /**
     * 根据SKU查询顺丰非实时库存
     * @return String
     */
    public String queryInvBalance() {
        ActionContext context = ActionContext.getContext();
        try {
            SkuMap skuMap = skuMapService.getSkuMapBySkuCode(skuCode, SkuMapOuterCodeEnum.SF.getCode());
            if (skuMap == null) {
                logger.info(MessageFormat.format("页面查询库存；SKU:{0}，没有对应是顺丰SKU", skuCode));
                context.put("inv_qty", null);
                return "sf_inv_list";
            }

            WmsInventoryBalancePageQueryRequest request = new WmsInventoryBalancePageQueryRequest();
            // 货主
            request.setCompany(WmsConstants.SF_COMPANY);
            // 仓库
            request.setWarehouse(WmsConstants.SF_WAREHOUSE);
            request.setItem(skuMap.getOuterSkuCode());

            WmsInventoryBalancePageQueryResponse response = sfWebService.outsideToLscmService(WmsInventoryBalancePageQueryRequest.class, WmsInventoryBalancePageQueryResponse.class, request);
            if (response == null || CollectionUtils.isEmpty(response.getList())) {
                logger.info(MessageFormat.format("页面查询库存；SKU:{0}，没有库存！", skuCode));
                context.put("inv_qty", null);
                return "sf_inv_list";
            }

            // 默认取第一个商品
            WmsInventoryBalancePageQueryResponseItem item = response.getList().get(0);
            context.put("inv_qty", item.getOn_hand_qty());
            return "sf_inv_list";

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("库存查询中，查询顺丰实时库存异常！", e);
            context.put("inv_response", null);
            return "sf_inv_list";
        }
    }


}
