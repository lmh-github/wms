package com.gionee.wms.web.action.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.PermissionConstants;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.dto.CommonAjaxResult;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.log.SalesOrderLogService;
import com.gionee.wms.service.stock.DeliveryService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.vo.SalesOrderVo;
import com.gionee.wms.web.AccessException;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller("DeliveryAction")
@Scope("prototype")
public class DeliveryAction extends AjaxActionSupport implements Preparable {
    private static final long serialVersionUID = 2728587467025993326L;
    @Autowired
    public SalesOrderLogService salesOrderLogService = null;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SalesOrderService salesOrderService;
    @Autowired
    private WarehouseService warehouseService;
    /** 页面相关属性 **/
    private Long id;
    private Delivery delivery;
    private Long batchId;
    private String batchCode;
    private List<DeliveryGoods> goodsList;
    private DeliveryGoods goods;
    private List<Shipping> shippingList;// 配送方式列表
    private DeliveryBatch deliveryBatch;// 收货批次model
    private List<Delivery> deliveryList;//
    private List<SalesOrderVo> orderList; // 销售订单列表
    private List<IndivFlow> indivList;// 商品身份信息
    private SalesOrder order;// 订单信息
    private Page page = new Page(50);

    private Long shippingId;
    private String skuName;
    private String indivCode;
    private String orderNum;
    private String consignee;
    private String mobile;
    private Date orderTimeBegin;// 订单起始时间
    private Date orderTimeEnd;// 订单结束时间
    private String shippingNo;    // 快递单号
    private String[] orderIds;    // 订单id号数组
    private Long warehouseId;    // 要发货的仓库id
    private List<Warehouse> warehouseList;// 仓库列表
    private Integer quantity;
    private String flag;
    private String templateName;

    /**
     * 进入发货单列表界面
     */
    @Override
    public String execute() throws Exception {
        // 初始化页面数据
        warehouseList = warehouseService.getValidWarehouses();
        return "delivery_to_kd1";
    }

    public String dispWap() throws Exception {
        // 初始化页面数据
        warehouseList = warehouseService.getValidWarehouses();
        warehouseId = warehouseService.getDefaultWarehouse().getId();
        return "wap_disp_dely_kd";
    }

    public String listForBatch() throws Exception {
        Validate.notNull(batchId);
        // 初始化页面数据
        shippingList = shippingService.getValidShippings();
        deliveryBatch = deliveryService.getDeliveryBatch(batchId);
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("batchId", batchId);
        orderList = salesOrderService.getSalesOrderAndGoods(criteria);
        return SUCCESS;
    }


    public String listForBatchSearch() throws Exception {
        Validate.notNull(batchId);
        //        // 初始化页面数据
        shippingList = shippingService.getValidShippings();
        deliveryBatch = deliveryService.getDeliveryBatch(batchId);

        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("batchId", batchId);
        criteria.put("shippingId", shippingId);
        criteria.put("skuName", skuName);
        criteria.put("indivCode", indivCode);
        criteria.put("orderCode", orderNum);
        criteria.put("consignee", consignee);
        criteria.put("mobile", mobile);
        criteria.put("orderTimeBegin", orderTimeBegin);
        criteria.put("orderTimeEnd", orderTimeEnd);
        orderList = salesOrderService.getSalesOrderListSearch(criteria);
        return SUCCESS;
    }

    /**
     * 进入订单配送信息页面
     */
    public String inputShippingInfo() throws Exception {
        if (!accountService.isPermitted(PermissionConstants.SALES_OUT_SHIPPING_EDIT)) {
            throw new AccessException();
        }
        shippingList = shippingService.getValidShippings();
        return "input_shipping";
    }

    public void prepareInputShippingInfo() throws Exception {
        Validate.notNull(id);
        // 初始化Model对象
        delivery = deliveryService.getDelivery(id);
    }

    /**
     * 添加配送信息
     */
    public String updateShippingInfo() throws Exception {
        Validate.notNull(delivery);
        try {
            deliveryService.updateShippingInfo(delivery);
            ajaxSuccess("添加配送信息成功");
        } catch (ServiceException e) {
            logger.error("添加配送信息出错", e);
            ajaxError("添加配送信息失败：" + e.getMessage());
        }
        return null;
    }

    public void prepareUpdateShippingInfo() throws Exception {
        Validate.notNull(id);
        // 初始化Model对象
        delivery = deliveryService.getDelivery(id);
    }

    /**
     * 添加发票信息
     */
    public String addInvoiceInfo() throws Exception {
        Validate.notNull(delivery);
        String invoiceStatus = ActionUtils.getRequest().getParameter("invoiceStatus");
        Validate.notBlank(invoiceStatus);
        if (!"1".equals(invoiceStatus)) {
            throw new IllegalArgumentException();
        }
        delivery.setInvoiceStatus(Integer.valueOf(invoiceStatus));
        try {
            deliveryService.addInvoiceInfo(delivery);
            ajaxSuccess("设置订单发票状态成功");
        } catch (ServiceException e) {
            logger.error("设置订单发票状态出错", e);
            ajaxError("设置订单发票状态失败：" + e.getMessage());
        }
        return null;
    }

    public void prepareAddInvoiceInfo() throws Exception {
        Validate.notNull(id);
        // 初始化Model对象
        delivery = deliveryService.getDelivery(id);
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * 打印预览运单
     */
    public String previewShipping() {
        Validate.notNull(id);
        delivery = deliveryService.getDelivery(id);
        Validate.notNull(delivery);
        Shipping shipping = shippingService.getShipping(delivery.getShippingId());
        Validate.notNull(shipping);
        //根据销售订单ID获取销售订单信息
        order = salesOrderService.getSalesOrder(delivery.getOriginalId());
        templateName = shipping.getTemplateName();
        return "preview_print";
    }

    public void prepare() throws Exception {

    }

    public String genDeliveryCode() {
        String forwardUrl = getForwardUrl();
        setForwardUrl(forwardUrl + "?batchCode=" + batchCode);
        ajaxSuccess("");
        return null;
    }

    /**
     * 去扫描，PDA操作
     * @return
     */
    public String toScan() {
        if (null == warehouseId) {
            Warehouse warehouse = warehouseService.getDefaultWarehouse();
            warehouseId = warehouse.getId();
        }
        //查询该批次号已扫描的运单数量和orderIds
        List<Delivery> deliveryList = deliveryService.getDeliveryListByBatchCode(batchCode);
        quantity = deliveryList.size();
        return "wap_delivery_to_kd";
    }

    public String wapGenDelyCode() {
        if (null == warehouseId) {
            Warehouse warehouse = warehouseService.getDefaultWarehouse();
            warehouseId = warehouse.getId();
        }
        batchCode = System.currentTimeMillis() + "";    // 批次号
        return "wap_delivery_to_kd";
    }

    public String deliveryToKd() {
        //查询该批次号已扫描的运单数量和orderIds
        deliveryList = deliveryService.getDeliveryListByBatchCode(batchCode);
        quantity = deliveryList.size();
        return "delivery_to_kd";
    }

    public String deliveryToKdList() {
        //查询该批次号已扫描的运单数量和orderIds
        deliveryList = deliveryService.getDeliveryListByBatchCode(batchCode);
        quantity = deliveryList.size();
        return "delivery_to_kd_list";
    }

    public String checkKuaidi() {
        CommonAjaxResult result = new CommonAjaxResult();
        try {
            shippingNo = shippingNo.trim();
            SalesOrder order = salesOrderService.getSalesOrderByShipAndStatus(null, shippingNo);
            if (null == order) {
                result.setOk(false);
                result.setMessage(shippingNo + "的订单未找到");
            } else if (!order.getOrderStatus().equals(OrderStatus.PICKED.getCode())) {
                // 必须是已配货的订单
                result.setOk(false);
                result.setMessage(shippingNo + "的订单" + WmsConstants.getOrderStatusZh(order.getOrderStatus()));
            } else {
                warehouseId = warehouseService.getWarehouseByOrderSource(order.getOrderSource()).getId();
                Map<String, Object> params = Maps.newHashMap();
                params.put("batchCode", batchCode);
                params.put("originalCode", order.getOrderCode());
                params.put("originalId", order.getId());
                params.put("warehouseId", warehouseId);
                params.put("shippingNo", order.getShippingNo());
                deliveryService.addDelivery(params);

                //更新该订单为待出库，前置条件为已配货
                params.clear();
                params.put("orderCode", order.getOrderCode());
                params.put("orderStatus", OrderStatus.SHIPPING.getCode());
                params.put("orderStatusWhere", OrderStatus.PICKED.getCode());
                salesOrderService.updateSalesOrderStatus(params);

                //保存操作日志
                SalesOrderLog salesOrderLog = new SalesOrderLog();
                try {
                    salesOrderLog.setOrderId(order.getId());
                    salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.SHIPPING.getCode());
                    salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                    salesOrderLog.setOpTime(new Date());
                    salesOrderLog.setRemark("更新订单为待出库状态");
                    salesOrderLogService.insertSalesOrderLog(salesOrderLog);
                } catch (Exception e) {
                    logger.error("业务日志记录异常", e);
                }

                result.setOk(true);
                params.clear();
                params.put("batchCode", batchCode);
                int count = deliveryService.getDeliveryListTotal(params);
                result.setResult(count);    // 返回订单id
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            result.setOk(false);
            result.setMessage(shippingNo + "数据异常");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setOk(false);
            result.setMessage(shippingNo + "内部错误");
        }
        ajaxObject(result);
        return null;
    }

    /**
     * 东莞电商仓库，手工确认出库
     * @return
     */
    public String confirmDelivery() {
        try {
            // 检查已扫描数量是否为空
            Validate.notNull(quantity, "发货失败：发货数量为空！");
            if (0 == quantity) {
                ajaxError("发货失败：发货数量为0！");
                return null;
            }
            String resultMsg = deliveryService.confirmDeliveryWap(warehouseId, batchCode);
            ajaxSuccess(resultMsg);
        } catch (ServiceException e) {
            logger.error("发货失败: ", e);
            ajaxError("发货失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * PDA确认发货
     * @return
     */
    public String confirmDeliveryWap() {
        CommonAjaxResult result = new CommonAjaxResult();
        try {
            Validate.notNull(quantity);
            if (0 == quantity) {
                result.setOk(false);
                result.setMessage("发货失败：发货数量为0");
            } else {
                String resultMsg = deliveryService.confirmDeliveryWap(warehouseId, batchCode);
                result.setOk(true);
                result.setMessage(resultMsg);
            }
        } catch (ServiceException e) {
            logger.error("发货失败: ", e);
            result.setOk(false);
            result.setMessage("发货失败：" + e.getMessage());
        }
        ajaxObject(result);
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public List<DeliveryGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<DeliveryGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<Delivery> getDeliveryList() {
        return deliveryList;
    }

    public List<IndivFlow> getIndivList() {
        return indivList;
    }

    public void setIndivList(List<IndivFlow> indivList) {
        this.indivList = indivList;
    }

    public DeliveryGoods getGoods() {
        return goods;
    }

    public void setGoods(DeliveryGoods goods) {
        this.goods = goods;
    }

    public List<Shipping> getShippingList() {
        return shippingList;
    }

    public Page getPage() {
        return page;
    }

    public DeliveryBatch getDeliveryBatch() {
        return deliveryBatch;
    }

    public void setDeliveryBatch(DeliveryBatch deliveryBatch) {
        this.deliveryBatch = deliveryBatch;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public SalesOrder getOrder() {
        return order;
    }

    public void setOrder(SalesOrder order) {
        this.order = order;
    }

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getOrderTimeBegin() {
        return orderTimeBegin;
    }

    public void setOrderTimeBegin(Date orderTimeBegin) {
        this.orderTimeBegin = orderTimeBegin;
    }

    public Date getOrderTimeEnd() {
        return orderTimeEnd;
    }

    public void setOrderTimeEnd(Date orderTimeEnd) {
        this.orderTimeEnd = orderTimeEnd;
    }

    public String getIndivCode() {
        return indivCode;
    }

    public void setIndivCode(String indivCode) {
        this.indivCode = indivCode;
    }

    public List<SalesOrderVo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<SalesOrderVo> orderList) {
        this.orderList = orderList;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
