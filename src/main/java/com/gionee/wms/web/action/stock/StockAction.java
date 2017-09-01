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
import java.util.*;

@Controller("StockAction")
@Scope("prototype")
public class StockAction extends CrudActionSupport<Stock> {

    private static final long serialVersionUID = -8940733721506429911L;

    private WarehouseService warehouseService;
    private CategoryService categoryService;
    private StockService stockService;
    private WaresService waresService;

    @Autowired
    private SFWebService sfWebService;

    @Autowired
    private SkuMapService skuMapService;

    /**
     * 页面相关属性
     **/
    private List<Stock> stockList; // 库存列表
    private Long warehouseId;
    private Long skuId;
    private Stock stock;// 库存model
    private String catPath;// 商品分类
    private Long id;// 库存ID
    private String skuCode;
    private String skuName;
    private List<Category> categoryList;// 商品分类
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
            // String tempFile = ActionUtils.getClassPath() +
            // "config/excel/order_info_list.xls";
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
    public void prepareList() throws Exception {
        // 初始化属性对象
        categoryList = categoryService.getCategoryList(null);

        warehouseList = warehouseService.getValidWarehouses();
    }

    public String getSkuInfo() throws Exception {
        Validate.notNull(skuCode);
        Sku sku = waresService.getSkuByCode(skuCode);
        JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
        ActionUtils.outputJson(jsonUtils.toJson(sku));
        return null;
    }

    /**
     * 进入安全库存设置页面
     */
    public String inputLimit() throws Exception {
        return "input_limit";
    }

    public void prepareInputLimit() throws Exception {
        Validate.notNull(id);
        // 初始化Model对象
        stock = stockService.getStock(id);
    }

    /**
     * 更新安全库存
     */
    public String updateLimit() throws Exception {
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

    public void prepareUpdateLimit() throws Exception {
        Validate.notNull(id);
        // 初始化Model对象
        stock = stockService.getStock(id);
    }

    /**
     * 打开创建或编辑页面
     */
    @Override
    public String input() throws Exception {
        // // 初始化商品属性信息
        // if (stock.getWares().getId() != null) {
        // waresAttrInfo =
        // waresService.getWaresWithAttrInfo(sku.getWares().getId());
        // }
        // // 如果为编辑操作，需要判断数据是否允许编辑
        // if (id != null) {
        // editEnabled = CollectionUtils.isEmpty(stockService.getStockList(id));
        // }
        return INPUT;
    }

    /**
     * 添加库存信息
     */
    @Override
    public String add() throws Exception {
        // if (CollectionUtils.isNotEmpty(itemIdList)) {
        // for (Integer itemId : itemIdList) {
        // if (itemId == null) {
        // logger.error("添加SKU时出错：属性值不能为空");
        // ajaxError("添加SKU失败：属性值不能为空");
        // return null;
        // }
        // }
        // // sku.setItemIds(StringUtils.join(itemIdList, ","));
        // sku.setItemIds(Joiner.on(",").skipNulls().join(itemIdList));
        // } else {
        // sku.setItemIds("");
        // }
        // try {
        // if(waresService.indivCodeEnabled(stock.getSku().getId())){
        // if(CollectionUtils.isNotEmpty(indivList)){
        // Iterator<Indiv> indivItr = indivList.iterator();
        // while(indivItr.hasNext()){
        // Indiv indiv = indivItr.next();
        // if(indiv!=null){
        // indiv.setWaresStatus(stockType);
        // }else{
        // indivItr.remove();
        // }
        // }
        // }else{
        // logger.warn("添加库存信息时未绑定商品身份码");
        // }
        //
        // }
        // stockService.addStockWithIndiv(stock,
        // indivList,StockType.valueOf(stockType));
        // ajaxSuccess("库存信息添加成功");
        // } catch (ServiceException e) {
        // logger.error(e.getMessage(), e);
        // ajaxError("库存信息添加失败：" + e.getMessage());
        // }
        return null;
    }

    /**
     * 编辑SKU
     */
    @Override
    public String update() throws Exception {
        // if (!accountService.isPermitted(PermissionConstants.CAT_EDIT)) {
        // throw new AccessException();
        // }
        // try {
        // waresService.updateSku(sku);
        // ajaxSuccess("SKU编辑成功");
        // } catch (ServiceException e) {
        // logger.error("SKU编辑时出错", e);
        // ajaxError("SKU编辑失败：" + e.getMessage());
        // }
        return null;
    }

    /**
     * 删除SKU
     */
    @Override
    public String delete() throws Exception {
        // if (!accountService.isPermitted(PermissionConstants.CAT_DELETE)) {
        // throw new AccessException();
        // }
        // try {
        // waresService.deleteSku(id);
        // ajaxSuccess("SKU删除成功");
        // } catch (ServiceException e) {
        // logger.error("SKU删除时出错", e);
        // ajaxError("SKU删除失败：" + e.getMessage());
        // }
        return null;
    }

    // 为add方法准备数据
    @Override
    public void prepareAdd() throws Exception {
        stock = new Stock();
    }

    // 为input方法准备数据
    @Override
    public void prepareInput() throws Exception {
        // 初始化Model对象

        // 初始化仓库列表
        warehouseList = warehouseService.getValidWarehouses();

    }

    // 为update方法准备数据
    @Override
    public void prepareUpdate() throws Exception {
        prepareModel();
    }

    private void prepareModel() throws Exception {
        // if (id != null) {
        // sku = waresService.getSku(id);
        // } else {
        // sku = new Sku();
        // }

    }

    // ModelDriven接口方法
    @Override
    public Stock getModel() {
        // if (sku == null) {
        // sku = new Sku();
        // Wares wares = new Wares();
        // Category cat = new Category();
        // wares.setCategory(cat);
        // sku.setWares(wares);
        // }
        return stock;
    }

    // 刷新所有顺丰仓库存
    public String refresh() {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("sfWarehouseName", "顺丰");
        List<Warehouse> warehouses = warehouseService.getWarehouseList(criteria);
        if (!CollectionUtils.isEmpty(warehouses)) {
            Map<String, String> skuMapping = Maps.newHashMap(); // 查询商品映射
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
                stockService.updateBatchByResponses(Optional.ofNullable(responses), skuMapping);
            }
        }
        return "refresh";
    }

    // 顺丰接口仅支持每次20条数据查询 为此拆分成多个WmsRealTimeInventoryBalanceQueryRequest对象
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
     *
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
            // List<WmsRealTimeInventoryBalanceQueryResponseItem> list = Lists.newArrayList();
            // WmsRealTimeInventoryBalanceQueryResponseItem item = new WmsRealTimeInventoryBalanceQueryResponseItem();
            // item.setTotal_stock(50d);
            // item.setOn_hand_stock(100d);
            // item.setAvailable_stock(150d);
            // item.setIn_transit_stock(30d);
            // list.add(item);
            // response.setList(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("库存查询中，查询顺丰实时库存异常！", e);
            context.put("inv_response", null);
        }

        return "sf_inv_list";
    }

    /**
     * 根据SKU查询顺丰非实时库存
     *
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
            request.setCompany(WmsConstants.SF_COMPANY); // 货主
            request.setWarehouse(WmsConstants.SF_WAREHOUSE); // 仓库
            request.setItem(skuMap.getOuterSkuCode());

            WmsInventoryBalancePageQueryResponse response = sfWebService.outsideToLscmService(WmsInventoryBalancePageQueryRequest.class, WmsInventoryBalancePageQueryResponse.class, request);
            if (response == null || CollectionUtils.isEmpty(response.getList())) {
                logger.info(MessageFormat.format("页面查询库存；SKU:{0}，没有库存！", skuCode));
                context.put("inv_qty", null);
                return "sf_inv_list";
            }

            WmsInventoryBalancePageQueryResponseItem item = response.getList().get(0); // 默认取第一个商品
            context.put("inv_qty", item.getOn_hand_qty());
            return "sf_inv_list";

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("库存查询中，查询顺丰实时库存异常！", e);
            context.put("inv_response", null);
            return "sf_inv_list";
        }
    }

    // -- 供页面传值 --

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    // --供页面取值--
    public Page getPage() {
        if (page == null) {
            page = new Page();
        }
        return page;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public List getWarehouseList() {
        return warehouseList;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    @Autowired
    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Autowired
    public void setWaresService(WaresService waresService) {
        this.waresService = waresService;
    }

    public String getExports() {
        return exports;
    }

    public void setExports(String exports) {
        this.exports = exports;
    }

}
