package com.gionee.wms.web.action.stock;

import com.gionee.wms.common.*;
import com.gionee.wms.common.WmsConstants.OrderPushStatusEnum;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.ShippingSummary;
import com.gionee.wms.entity.*;
import com.gionee.wms.facade.result.WmsResult;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.log.SalesOrderLogService;
import com.gionee.wms.service.stock.DeliveryService;
import com.gionee.wms.service.stock.SalesOrderMapService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.stock.SystemConfigService;
import com.gionee.wms.vo.OrderStatusStatVo;
import com.gionee.wms.vo.SalesOrderVo;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.struts2.ServletActionContext;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: SalesOrderAction
 * @Description: 销售订单
 * @author Kevin
 * @date 2013-6-2 下午10:48:23
 */

/**
 * @author PengBin 00001550<br>
 * @date 2015年3月23日 下午2:36:05
 */
@Controller("SalesOrderAction")
@Scope("prototype")
public class SalesOrderAction extends CrudActionSupport<SalesOrder> implements Preparable {
    private static final long serialVersionUID = -4504094216342775427L;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    @Autowired
    private SalesOrderService salesOrderService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private SalesOrderLogService salesOrderLogService;
    @Autowired
    private WaresDao waresDao;

    @Autowired
    private SystemConfigService configService;

    @Autowired
    private SalesOrderMapService salesOrderMapService;

    /**
     * 页面相关属性
     **/
    private String orderCode;
    private String orderUser;
    private String consignee;
    private String mobile;
    private String shippingNo;
    private String batchCode;
    private Integer orderStatus;
    private Date orderTimeBegin;// 入库起始时间
    private Date orderTimeEnd;// 入库结束时间
    private Date paymentTimeBegin;// 支付起始时间
    private Date paymentTimeEnd;// 支付结束时间
    private String orderSource;// 订单来源
    private String invoiceEnabled;// 是否包含发票
    private Long id;
    private SalesOrder order;
    private Long batchId;// 发货批次ID
    private List<SalesOrderVo> orderVoList; // 销售订单(包含商品清单)列表
    private List<SalesOrder> orderList; // 销售订单列表
    private List<SalesOrderGoods> goodsList;// 订单商品清单
    private Delivery delivery;// 发货单
    private List<DeliveryGoods> deliveryGoodsList;// 发货商品清单
    private List<Warehouse> warehouseList;// 仓库列表
    private Warehouse warehouse;
    private Long deliveryId;
    private Page page = new Page();
    private List<ShippingSummary> shippingSumList;
    private List<Shipping> shippingList;// 配送方式列表
    private Long shippingId; // 物流公司id
    private String shippingCode;
    private List<Long> orderIds;
    private String ids;// 订单checkbox ids
    private List<SalesOrderGoods> orderGoodsSummary;// 订单商品汇总
    private DeliveryBatch deliveryBatch;// 发货批次model
    private Long rownum;// 记录序列
    private String urlPre;// lodop url前缀
    private String printerPre;// 网络打印机前缀
    private Date shippingTimeBegin;
    private Date shippingTimeEnd;

    private String templateName;
    private String exports;
    private Boolean editGoods;

    private Integer orderNum;
    private String shippingRemark;// 运单上的备注信息
    private String condition;// 商品搜索表达式
    private String skuName;
    private String skuCode;
    private List<OrderStatusStatVo> orderStatusStatVoList;
    private List<SalesOrderLog> salesOrderLogList;// 订单日志
    private String queryFrom;// menu:点击菜单查询；button:点击按钮查询

    private Map<String, Object> bspSourceMap;// 面单打印数据源


    /**
     * 订单推送到顺丰状态
     */
    private Integer orderPushStatus;
    /**
     * 订单推送开始时间
     */
    private Date orderPushTimeBegin;
    /**
     * 订单推送结束时间
     */
    private Date orderPushTimeEnd;

    private Integer paymentType;// 支付类型(1:在线支付 2:货到付款)
    private String paymentName;// 支付方式名称

    private Map<String, Long> batchMap;
    private String errorMsg = "";

    /**
     * 订单自动推送配置
     */
    private SystemConfig config;

    public String execute() throws Exception {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderCode", StringUtils.trimToNull(orderCode));
        criteria.put("orderUser", StringUtils.trimToNull(orderUser));
        criteria.put("consignee", StringUtils.trimToNull(consignee));
        criteria.put("mobile", StringUtils.trimToNull(mobile));
        criteria.put("shippingId", shippingId);
        criteria.put("shippingNo", StringUtils.trimToNull(shippingNo));
        criteria.put("orderSource", orderSource);
        criteria.put("orderStatus", orderStatus);
        criteria.put("orderTimeBegin", orderTimeBegin);
        criteria.put("orderTimeEnd", orderTimeEnd);
        criteria.put("paymentTimeBegin", paymentTimeBegin);
        criteria.put("paymentTimeEnd", paymentTimeEnd);
        criteria.put("shippingTimeBegin", shippingTimeBegin);
        criteria.put("shippingTimeEnd", shippingTimeEnd);

        if (condition != null) {
            if (StringUtils.isNotBlank(skuCode)) {
                if (condition.equals("1")) {
                    String[] skuCodes = skuCode.split(",");
                    criteria.put("skuCodeInclude", skuCodes);
                } else if (condition.equals("2")) {
                    criteria.put("skuCode", skuCode);
                } else if (condition.equals("3")) {
                    String[] skuCodes = skuCode.split(",");
                    criteria.put("skuCodeNotInclude", skuCodes);
                }
            }
        }
        //criteria.put("skuCode", StringUtils.trimToNull(skuCode));

        criteria.put("paymentType", paymentType);
        criteria.put("paymentName", StringUtils.trimToNull(paymentName));
        if (orderPushStatus != null && WmsConstants.OrderPushStatusEnum.PUSHED.getCode() == orderPushStatus) {
            criteria.put("order_push_status", orderPushStatus);
        } else if (orderPushStatus != null && WmsConstants.OrderPushStatusEnum.UN_PUSHED.getCode() == orderPushStatus) {
            criteria.put("eliminate_sf_order", true);
        }
        if ("1".equals(exports)) {
            orderVoList = salesOrderService.getSalesOrderAndGoods(criteria);
            return downloadOrderInfo();
        } else {
            int totalRow = salesOrderService.getSalesOrderTotal(criteria);
            page.setTotalRow(totalRow);
            page.calculate();
            criteria.put("page", page);
            orderList = salesOrderService.getSalesOrderList(criteria, page);
            return SUCCESS;
        }
    }

    public String listForDely() throws Exception {
        // 查询物流方式
        shippingList = shippingService.getValidShippings();
        if (shippingList.size() > 0) {
            shippingSumList = new ArrayList<ShippingSummary>();
        }
        for (Shipping shipping : shippingList) {
            ShippingSummary ssum = new ShippingSummary();
            ssum.setShippingId(shipping.getId());
            ssum.setShippingName(shipping.getShippingName());
            Map<String, Object> criteria1 = Maps.newHashMap();
            criteria1.put("shippingId", shipping.getId());
            criteria1.put("orderStatus", OrderStatus.FILTERED.getCode());
            Integer total = salesOrderService.getSalesOrderTotal(criteria1);
            ssum.setCount(total);
            shippingSumList.add(ssum);
        }
        // 初始化属性对象
        order = new SalesOrder();
        // warehouse = warehouseService.getDefaultWarehouse();
        // warehouseList = warehouseService.getValidWarehouses();

        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderCode", StringUtils.defaultIfBlank(orderCode, null));
        criteria.put("orderUser", StringUtils.defaultIfBlank(orderUser, null));
        criteria.put("consignee", StringUtils.defaultIfBlank(consignee, null));
        criteria.put("orderStatus", OrderStatus.FILTERED.getCode());
        criteria.put("orderTimeBegin", orderTimeBegin);
        criteria.put("orderTimeEnd", orderTimeEnd);
        criteria.put("paymentTimeBegin", paymentTimeBegin);
        criteria.put("paymentTimeEnd", paymentTimeEnd);
        criteria.put("shippingId", shippingId);
        int totalRow = salesOrderService.getSalesOrderTotal(criteria);
        page.setTotalRow(totalRow);
        page.calculate();
        criteria.put("page", page);
        orderList = salesOrderService.getSalesOrderList(criteria, page);
        return "list_for_dely";
    }

    /**
     * 特殊功能，用于修改订单为已签收状态
     *
     * @return
     * @throws Exception
     */
    public String qinaShou() throws Exception {
        try {
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("orderCode", StringUtils.defaultIfBlank(orderCode, null));
            criteria.put("orderStatus", OrderStatus.RECEIVED.getCode());

            salesOrderService.updateSalesOrderStatus(criteria);

            // 保存操作日志
            SalesOrderLog salesOrderLog = new SalesOrderLog();
            try {
                SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(orderCode);
                salesOrderLog.setOrderId(salesOrder.getId());
                salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.CANCELED.getCode());
                salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                salesOrderLog.setOpTime(new Date());
                salesOrderLog.setRemark("更新订单为已签收，原来状态为：" + salesOrder.getOrderStatus());
                salesOrderLogService.insertSalesOrderLog(salesOrderLog);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }

            ajaxSuccess("更改成功！");

        } catch (Exception e) {
            e.printStackTrace();

            logger.error("更改失败", e);
            ajaxError("更改失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 打印订单
     *
     * @return
     * @throws Exception
     */
    public String orderPrint() throws Exception {
        // 查询物流方式
        shippingList = shippingService.getValidShippings();
        if (shippingList.size() > 0) {
            shippingSumList = new ArrayList<ShippingSummary>();
        }
        for (Shipping shipping : shippingList) {
            ShippingSummary ssum = new ShippingSummary();
            ssum.setShippingId(shipping.getId());
            ssum.setShippingName(shipping.getShippingName());
            ssum.setShippingCode(shipping.getShippingCode());
            Map<String, Object> criteria1 = Maps.newHashMap();
            criteria1.put("orderCode", StringUtils.defaultIfBlank(orderCode, null));
            criteria1.put("orderUser", StringUtils.defaultIfBlank(orderUser, null));
            criteria1.put("consignee", StringUtils.defaultIfBlank(consignee, null));
            criteria1.put("batchCode", StringUtils.defaultIfBlank(batchCode, null));
            criteria1.put("orderTimeBegin", orderTimeBegin);
            criteria1.put("orderTimeEnd", orderTimeEnd);
            criteria1.put("paymentTimeBegin", paymentTimeBegin);
            criteria1.put("paymentTimeEnd", paymentTimeEnd);
            criteria1.put("shippingId", shipping.getId());
            if (orderStatus == null) {
                orderStatus = OrderStatus.FILTERED.getCode();
                criteria1.put("orderStatus", orderStatus);
            } else if (orderStatus == 999) {
                criteria1.put("orderStatus", null);
            } else {
                criteria1.put("orderStatus", orderStatus);
            }
            criteria1.put("eliminate_sf_order", true);
            if (orderPushStatus != null && WmsConstants.OrderPushStatusEnum.PUSHED.getCode() == orderPushStatus) {
                criteria1.put("order_push_status", orderPushStatus);
                criteria1.put("eliminate_sf_order", false);
            }
            // if(StringUtils.isBlank(orderSource))
            // orderSource = OrderSource.OFFICIAL_GIONEE.getCode();
            criteria1.put("orderSource", orderSource);
            // if(StringUtils.isBlank(invoiceEnabled))
            // invoiceEnabled = "1";
            if (StringUtils.isBlank(invoiceEnabled)) invoiceEnabled = null;
            criteria1.put("invoiceEnabled", invoiceEnabled);
            if (condition != null) {
                if (StringUtils.isNotBlank(skuCode)) {
                    if (condition.equals("1")) {
                        String[] skuCodes = skuCode.split(",");
                        criteria1.put("skuCodeInclude", skuCodes);
                    } else if (condition.equals("2")) {
                        criteria1.put("skuCode", skuCode);
                    } else if (condition.equals("3")) {
                        String[] skuCodes = skuCode.split(",");
                        criteria1.put("skuCodeNotInclude", skuCodes);
                    }
                }
            }
            Integer total = salesOrderService.getSalesOrderTotal(criteria1);
            ssum.setCount(total);
            shippingSumList.add(ssum);
        }
        // 初始化属性对象
        order = new SalesOrder();
        // warehouse = warehouseService.getDefaultWarehouse();
        // warehouseList = warehouseService.getValidWarehouses();

        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderCode", StringUtils.defaultIfBlank(orderCode, null));
        criteria.put("orderUser", StringUtils.defaultIfBlank(orderUser, null));
        criteria.put("consignee", StringUtils.defaultIfBlank(consignee, null));
        criteria.put("batchCode", StringUtils.defaultIfBlank(batchCode, null));
        criteria.put("orderTimeBegin", orderTimeBegin);
        criteria.put("orderTimeEnd", orderTimeEnd);
        criteria.put("paymentTimeBegin", paymentTimeBegin);
        criteria.put("paymentTimeEnd", paymentTimeEnd);
        criteria.put("shippingId", shippingId);
        criteria.put("eliminate_sf_order", true); // 排除已经推送到顺丰侧的订单
        if (orderStatus == null) orderStatus = OrderStatus.FILTERED.getCode();
        else if (orderStatus == 999) {
            criteria.put("orderStatus", null);
        } else {
            criteria.put("orderStatus", orderStatus);
        }
        if (orderPushStatus != null && WmsConstants.OrderPushStatusEnum.PUSHED.getCode() == orderPushStatus) {
            criteria.put("eliminate_sf_order", false);
            criteria.put("order_push_status", orderPushStatus);
        }
        // if(StringUtils.isBlank(orderSource))
        // orderSource = OrderSource.OFFICIAL_GIONEE.getCode();
        criteria.put("orderSource", orderSource);
        // if(StringUtils.isBlank(invoiceEnabled))
        // invoiceEnabled = "1";
        if (StringUtils.isBlank(invoiceEnabled)) invoiceEnabled = null;
        criteria.put("invoiceEnabled", invoiceEnabled);
        if (condition != null) {
            if (StringUtils.isNotBlank(skuCode)) {
                if (condition.equals("1")) {
                    String[] skuCodes = skuCode.split(",");
                    criteria.put("skuCodeInclude", skuCodes);
                } else if (condition.equals("2")) {
                    criteria.put("skuCodeEqual", skuCode);
                } else if (condition.equals("3")) {
                    String[] skuCodes = skuCode.split(",");
                    criteria.put("skuCodeNotInclude", skuCodes);
                }
            }
        }
        int totalRow = salesOrderService.getSalesOrderTotal(criteria);
        if (page.getPageSize() == 20) page.setPageSize(40);
        page.setTotalRow(totalRow);
        page.calculate();
        criteria.put("page", page);
        criteria.put("sort", "asc");
        orderList = salesOrderService.getSalesOrderList(criteria, page);

        urlPre = WmsConstants.LODOP_URL_PRE;
        printerPre = WmsConstants.PRINTER_PRE;
        return "list_order_print";
    }

    /**
     * 查找订单以供批量添加至指定出库单
     *
     * public String lookup() throws Exception { Map<String, Object> criteria =
     * Maps.newHashMap(); criteria.put("orderCode",
     * StringUtils.defaultIfBlank(order.getOrderCode(), null));
     * criteria.put("orderUser",
     * StringUtils.defaultIfBlank(order.getOrderUser(), null));
     * criteria.put("consignee",
     * StringUtils.defaultIfBlank(order.getConsignee(), null));
     * criteria.put("shippingStatus", order.getShippingStatus());
     * criteria.put("orderTimeBegin", orderTimeBegin);
     * criteria.put("orderTimeEnd", orderTimeEnd); int totalRow =
     * salesOrderService.getSalesOrderListTotal(criteria);
     * page.setTotalRow(totalRow); page.calculate(); criteria.put("page", page);
     * orderList = salesOrderService.getSalesOrderList(criteria, page); return
     * "lookup"; }
     */

    /**
     * 打印订单
     *
     * @return
     * @throws Exception
     */
    public String sfOrderPrint() throws Exception {
        Shipping sf_Shipping = shippingService.getShippingByCode(WmsConstants.ShippingEnum.SF.getCode());
        shippingId = sf_Shipping.getId();
        // 查询物流方式
        shippingList = shippingService.getValidShippings();
        if (shippingList.size() > 0) {
            shippingSumList = new ArrayList<ShippingSummary>();
        }
        for (Shipping shipping : shippingList) {
            ShippingSummary ssum = new ShippingSummary();
            ssum.setShippingId(shipping.getId());
            ssum.setShippingName(shipping.getShippingName());
            ssum.setShippingCode(shipping.getShippingCode());
            Map<String, Object> criteria1 = Maps.newHashMap();
            criteria1.put("orderCode", StringUtils.defaultIfBlank(orderCode, null));
            criteria1.put("orderUser", StringUtils.defaultIfBlank(orderUser, null));
            criteria1.put("consignee", StringUtils.defaultIfBlank(consignee, null));
            criteria1.put("batchCode", StringUtils.defaultIfBlank(batchCode, null));
            criteria1.put("orderTimeBegin", orderTimeBegin);
            criteria1.put("orderTimeEnd", orderTimeEnd);
            criteria1.put("paymentTimeBegin", paymentTimeBegin);
            criteria1.put("paymentTimeEnd", paymentTimeEnd);
            criteria1.put("shippingId", shipping.getId());
            // if (orderStatus == null)
            // orderStatus = OrderStatus.FILTERED.getCode();
            // criteria1.put("orderStatus", orderStatus);
            // if(StringUtils.isBlank(orderSource))
            // orderSource = OrderSource.OFFICIAL_GIONEE.getCode();
            criteria1.put("orderSource", orderSource);
            // if(StringUtils.isBlank(invoiceEnabled))
            // invoiceEnabled = "1";
            if (StringUtils.isBlank(invoiceEnabled)) invoiceEnabled = null;
            criteria1.put("invoiceEnabled", invoiceEnabled);
            if (condition != null) {
                if (StringUtils.isNotBlank(skuCode)) {
                    if (condition.equals("1")) {
                        String[] skuCodes = skuCode.split(",");
                        criteria1.put("skuCodeInclude", skuCodes);
                    } else if (condition.equals("2")) {
                        criteria1.put("skuCodeEqual", skuCode);
                    } else if (condition.equals("3")) {
                        String[] skuCodes = skuCode.split(",");
                        criteria1.put("skuCodeNotInclude", skuCodes);
                    }
                }
            }
            Integer total = salesOrderService.getSalesOrderTotal(criteria1);
            ssum.setCount(total);
            shippingSumList.add(ssum);
        }
        // 初始化属性对象
        order = new SalesOrder();
        // warehouse = warehouseService.getDefaultWarehouse();
        // warehouseList = warehouseService.getValidWarehouses();

        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderCode", StringUtils.defaultIfBlank(orderCode, null));
        criteria.put("orderUser", StringUtils.defaultIfBlank(orderUser, null));
        criteria.put("consignee", StringUtils.defaultIfBlank(consignee, null));
        criteria.put("batchCode", StringUtils.defaultIfBlank(batchCode, null));
        criteria.put("orderTimeBegin", orderTimeBegin);
        criteria.put("orderTimeEnd", orderTimeEnd);
        criteria.put("paymentTimeBegin", paymentTimeBegin);
        criteria.put("paymentTimeEnd", paymentTimeEnd);
        criteria.put("shippingId", sf_Shipping.getId());
        criteria.put("shippingNo", shippingNo);
        criteria.put("order_push_status", WmsConstants.OrderPushStatusEnum.PUSHED.getCode());
        criteria.put("orderPushTimeBegin", orderPushTimeBegin);
        criteria.put("orderPushTimeEnd", orderPushTimeEnd);
        criteria.put("skuCode", skuCode);
        // if (orderStatus == null)
        // orderStatus = OrderStatus.FILTERED.getCode();
        // criteria.put("orderStatus", orderStatus);
        // if(StringUtils.isBlank(orderSource))
        // orderSource = OrderSource.OFFICIAL_GIONEE.getCode();
        criteria.put("orderSource", orderSource);
        // if(StringUtils.isBlank(invoiceEnabled))
        // invoiceEnabled = "1";
        if (StringUtils.isBlank(invoiceEnabled)) invoiceEnabled = null;
        criteria.put("invoiceEnabled", invoiceEnabled);
        if (condition != null) {
            if (StringUtils.isNotBlank(skuCode)) {
                if (condition.equals("1")) {
                    String[] skuCodes = skuCode.split(",");
                    criteria.put("skuCodeInclude", skuCodes);
                } else if (condition.equals("2")) {
                    criteria.put("skuCodeEqual", skuCode);
                } else if (condition.equals("3")) {
                    String[] skuCodes = skuCode.split(",");
                    criteria.put("skuCodeNotInclude", skuCodes);
                }
            }
        }
        int totalRow = salesOrderService.getSalesOrderTotal(criteria);
        if (page.getPageSize() == 20) page.setPageSize(40);
        page.setTotalRow(totalRow);
        page.calculate();
        criteria.put("page", page);
        criteria.put("sort", "asc");
        orderList = salesOrderService.getSalesOrderList(criteria, page);

        urlPre = WmsConstants.LODOP_URL_PRE;
        printerPre = WmsConstants.PRINTER_PRE;
        return "list_sf_order_print";
    }

    /**
     * 进入查看订单详细界面
     */
    public String showSalesOrder() throws Exception {
        Validate.notNull(id);
        shippingList = shippingService.getValidShippings();
        // 初始化Model对象
        order = salesOrderService.getSalesOrder(id);
        goodsList = salesOrderService.getOrderGoodsList(id);
        return "show";
    }

    @Override
    public String input() throws Exception {
        // 初始化页面数据
        shippingList = shippingService.getValidShippings();
        if (null != id) {
            order = salesOrderService.getSalesOrder(id);
            if (null == order) {
                throw new Exception("订单不存在");
            }
            goodsList = salesOrderService.getOrderGoodsList(id);
            salesOrderLogList = salesOrderLogService.selectSalesOrderLogsByOrderId(id);
        }
        return INPUT;
    }

    @Override
    public String add() throws Exception {
        Validate.notNull(order);
        try {
            order.setOrderCode(order.getOrderCode().trim());
            order.setOrderTime(new Date());
            Shipping shipping = shippingService.getShipping(order.getShippingId());
            order.setShippingName(shipping.getShippingName());
            order.setDeliveryCode(order.getOrderCode() + "01");
            order.setHandledBy(ActionUtils.getLoginName());
            order.setHandledTime(new Date());
            List<SalesOrderGoods> _goodsList = new ArrayList<SalesOrderGoods>();
            if (null != goodsList) {
                for (SalesOrderGoods goods : goodsList) {
                    if (null != goods) {
                        _goodsList.add(goods);
                    }
                }
            }
            if (_goodsList.size() == 0) {
                ajaxError("商品列表为空");
                return null;
            }
            salesOrderService.addSalesOrder(order, _goodsList);
            ajaxSuccess("新增成功");
        } catch (ServiceException e) {
            logger.error("订单新增错误", e);
            ajaxError("订单新增错误: " + WmsResult.getResultZh(e.getMessage()));
        } catch (Exception e) {
            logger.error("系统错误", e);
            ajaxError("系统错误" + e.getMessage());
        }
        return null;
    }

    @Override
    public String update() throws Exception {
        Validate.notNull(order.getId());
        try {
            SalesOrder persistent = salesOrderService.getSalesOrder(order.getId());
            if (WmsConstants.OrderStatus.FILTERED.getCode() != persistent.getOrderStatus()) {
                // 必须为未发货状态才能修改
                ajaxError("此订单" + WmsConstants.getOrderStatusZh(persistent.getOrderStatus()) + "不能修改");
                return null;
            }
            if (persistent.getOrderPushStatus() != null && persistent.getOrderPushStatus().intValue() == OrderPushStatusEnum.PUSHED.getCode()) {
                ajaxError("此订单已经推送到顺丰不能进行修改！<br>您可以线下通知顺丰取消当前订单，另外再重建一单再次进行推送！");
                return null;
            }
            Shipping shipping = shippingService.getShipping(order.getShippingId());
            order.setShippingName(shipping.getShippingName());
            order.setHandledBy(ActionUtils.getLoginName());
            order.setHandledTime(new Date());
            BeanUtils.copyProperties(order, persistent, new String[]{"orderSource", "orderTime", "paymentCode"});
            List<SalesOrderGoods> _goodsList = new ArrayList<SalesOrderGoods>();
            if (editGoods) {
                // 如果修改了商品
                if (null != goodsList) {
                    // 如果进行了修改
                    for (SalesOrderGoods goods : goodsList) {
                        if (null != goods) {
                            _goodsList.add(goods);
                        }
                    }
                }
                if (_goodsList.size() == 0) {
                    ajaxError("商品列表为空");
                    return null;
                }
            }
            salesOrderService.updateSalesOrder(persistent, _goodsList);
            ajaxSuccess("修改成功");
        } catch (ServiceException e) {
            logger.error("订单修改错误", e);
            ajaxError("订单修改错误: " + WmsResult.getResultZh(e.getMessage()));
        } catch (Exception e) {
            logger.error("系统错误", e);
            ajaxError("系统错误" + e.getMessage());
        }
        return null;
    }

    /**
     * 打印预览购物清单
     */
    public String previewShoppingList() {
        Validate.notNull(id);
        // 初始化Model对象
        order = salesOrderService.getSalesOrder(id);
        goodsList = salesOrderService.getOrderGoodsList(id);
        // 根据发货编号生成条码信息
        if (order.getDeliveryCode() != null && !order.getDeliveryCode().equals("")) {
            String barCodePath = ActionUtils.getProjectPath() + WmsConstants.BAR_CODE_PATH;
            String fileName = OneBarcodeUtil.generateBar(order.getDeliveryCode(), barCodePath);
            if (fileName != null) order.setBarCodeImgPath(WmsConstants.BAR_CODE_PATH + fileName);
        }
        return "preview_shopping_list";
    }

    /**
     * 批量打印预览购物清单
     */
    public String batchPreviewShoppingList() {
        Validate.notNull(ids);
        // 初始化Model对象
        // order = salesOrderService.getSalesOrder(id);
        // goodsList = salesOrderService.getOrderGoodsList(id);
        orderVoList = salesOrderService.queryGoodsListByOrderIds(ids.split(","));
        // 根据发货编号生成条码信息
        if (orderVoList != null) {
            for (SalesOrderVo order : orderVoList) {
                if (order.getDeliveryCode() == null || order.getDeliveryCode().equals("")) continue;
                String barCodePath = ActionUtils.getProjectPath() + WmsConstants.BAR_CODE_PATH;
                String fileName = OneBarcodeUtil.generateBar(order.getDeliveryCode(), barCodePath);
                if (fileName != null) order.setBarCodeImgPath(WmsConstants.BAR_CODE_PATH + fileName);
            }
        }
        return "batch_preview_shopping_list";
    }

    // public void prepareInputShippingInfo() throws Exception {
    // Validate.notNull(ids);
    // String[] idsArr = ids.split(",");
    // orderIds = new ArrayList<Long>();
    // for(String orderId : idsArr)
    // {
    // orderIds.add(Long.valueOf(orderId));
    // }
    // // 初始化Model对象
    // orderList = salesOrderService.getSalesOrderListByIds(orderIds);
    // }

    /**
     * 进入配送信息批量编辑界面
     */
    public String inputShippingInfo() throws Exception {
        shippingList = shippingService.getValidShippings();
        order = salesOrderService.getSalesOrder(id);
        if (null == order) {
            logger.error("订单不存在: id=" + id);
            ajaxError("订单不存在");
        }
        // orderList = salesOrderService.getSalesOrderListByIds(orderIds);

        return "input_shipping_info";
    }

    /**
     * 批量更新发货批次内的订单配送信息
     */
    public String updateShippingInfo() throws Exception {
        Validate.notNull(order);
        try {
            List<SalesOrder> list = new ArrayList<SalesOrder>();
            list.add(order);
            salesOrderService.updateShippingInfoList(list);
            ajaxSuccess("编辑配送信息成功");
        } catch (ServiceException e) {
            logger.error("编辑配送信息时出错", e);
            ajaxError("编辑配送信息失败：" + e.getMessage());
        }
        return null;
    }

    /**
     * 批量更新发货批次内的订单配送信息
     */
    public String setInvoiceStatus() throws Exception {
        try {
            if (CollectionUtils.isNotEmpty(orderIds)) {
                salesOrderService.setInvoiceStatus(orderIds);
                ajaxSuccess("设置成功");
            } else {
                ajaxError("请选择订单！");
            }
        } catch (ServiceException e) {
            logger.error("设置失败", e);
            ajaxError("设置发票状态失败：" + e.getMessage());
        }
        return null;
    }

    /**
     * 打印预览运单
     */
    public String previewShipping() throws Exception {
        Validate.notNull(id);
        order = salesOrderService.getSalesOrder(id);
        // 组装运单备注的商品详情，格式：SKU名称*数量
        List<SalesOrderGoods> orderGoods = salesOrderService.getOrderGoodsList(id);
        StringBuilder shippingRemarkStr = new StringBuilder("");
        /**
         * 1）对于订单中含有手机的（无论订单中的商品数量是多少），运单在打印时，仍然打印 U2（BL N2200）
         * 2）对于订单中不含手机的（无论订单中的商品数量是多少），运单在打印时，打印 “手机配件”
         */
        String pjStr = "";
        boolean isIndiv = false;
        for (SalesOrderGoods goods : orderGoods) {
            // shippingRemarkStr.append(goods.getSkuName()).append("*").append(goods.getQuantity()).append(goods.getMeasureUnit()).append("&nbsp;&nbsp;");
            skuCode = goods.getSkuCode();
            Sku sku = waresDao.querySkuBySkuCode(skuCode);
            if (goods.getIndivEnabled().equals(1)) {
                isIndiv = true;
                shippingRemarkStr.append(sku.getWares().getWaresRemark()).append("&nbsp;&nbsp;");
            } else {
                pjStr = sku.getWares().getWaresRemark();
            }
        }
        if (isIndiv) {
            shippingRemark = shippingRemarkStr.toString();
        } else {
            shippingRemark = pjStr;
        }

        Shipping shipping = shippingService.getShipping(order.getShippingId());
        Validate.notNull(shipping);
        // 根据销售订单ID获取销售订单信息
        templateName = shipping.getTemplateName();
        try {

            Map<String, Object> model = LinkMapUtils.<String, Object>newHashMap().put("order", this.order).getMap();
            model.put("j_company", "深圳市金立通信设备有限公司");
            model.put("j_contact", "电商配送中心");
            model.put("j_province", "广东省");
            model.put("j_city", "东莞市");
            model.put("j_county", "大岭山镇");
            model.put("j_address", "湖畔工业区金立工业园");
            String mailNo = sfOrder(TemplateHelper.generate(model, "e-sf-create.ftl"));

            bspSourceMap = new HashMap<>();
            bspSourceMap.put("sfCode", mailNo);
            bspSourceMap.put("order", order);
        } catch (Exception e) {
            if(e instanceof ServiceException){
                errorMsg = e.getMessage();
            }
            e.printStackTrace();
        }

        return "preview_print";
    }

    /**
     * 发送电子面单请求
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public String sfOrder(String xml) throws Exception {
        String uri = "http://bsp-ois.sit.sf-express.com:9080/bsp-ois/sfexpressService";
        String checkWord = "j8DzkIFgmlomPt0aLuwU";
        URL url = new URL(uri);
        String verifyCode = Util.md5EncryptAndBase64(xml + checkWord);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        OutputStream os = conn.getOutputStream();
        try {
            os.write(("xml=" + xml + "&verifyCode=" + verifyCode).getBytes("utf8"));
        } catch (IOException e) {
            IOUtils.closeQuietly(os);
        }
        InputStream in = conn.getInputStream();
        String errorMsg = "系统异常";
        try {
            byte[] data = new byte[in.available()];
            in.read(data);
            String responseXml = new String(data, "utf8");
            StringReader read = new StringReader(responseXml);
            InputSource source = new InputSource(read);
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(source);
            Element root = doc.getRootElement();
            List nodes = root.getChildren();
            Element element;
            for (Object node : nodes) {
                //循环依次得到子元素
                element = (Element) node;
                if ("Body".equals(element.getName())) {
                    for (Content content : element.getContent()) {
                        Element childElement = (Element) content;
                        for (Attribute attribute : childElement.getAttributes()) {
                            if ("mailno".equals(attribute.getName())) {
                                return attribute.getValue();
                            }
                        }
                    }
                }
                if ("ERROR".equals(element.getName())) {
                    errorMsg = element.getContent(0).getValue();
                }
            }
            throw new ServiceException(errorMsg);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 批量打印预览运单
     */
    public String batchPreviewShipping() {
        Validate.notNull(ids);
        // Validate.notNull(shippingId);
        String[] idsArr = ids.split(",");
        orderIds = new ArrayList<Long>();
        for (String orderId : idsArr) {
            orderIds.add(Long.valueOf(orderId));
        }
        orderList = salesOrderService.getSalesOrderListByIds(orderIds);
        Shipping shipping = shippingService.getShipping(1644l);
        Validate.notNull(shipping);
        // 根据销售订单ID获取销售订单信息
        templateName = "batch" + shipping.getTemplateName();
        return "preview_print_batch";
    }

    /**
     * 打印拣货清单
     */
    public String previewPicking() {
        Validate.notNull(ids);
        orderGoodsSummary = salesOrderService.getOrderGoodsListByIds(ids);

        // 获取拣货编号与订单单数
        SalesOrder order = salesOrderService.getSalesOrder(Long.valueOf(ids.split(",")[0]));
        batchCode = order.getBatchCode();
        orderNum = ids.split(",").length;
        return "preview_print_picking";
    }

    /**
     * 打印发票
     */
    public String printInvoice() {
        Validate.notNull(ids);
        String ip = ActionUtils.getRemoteAddr(request);
        if (StringUtils.isNotBlank(ip)) {
            // 统一外网，税控soap服务地址端口为8081
            if (!isInner(ip)) {
                ip = ip + ":8081";
            }
        }
        salesOrderService.makeInvoice(ids.split(","), ip);
        ajaxSuccess("");
        return null;
    }

    /**
     * 加载打印发票需要的data
     */
    public void getPrintInvoiceData() {
        if (StringUtils.isBlank(ids)) {
            ajaxError("没有要打印的订单！");
            return;
        }

        try {
            List<Map<String, Object>> list = salesOrderService.getPrintInvoiceData(ids.split(","));
            ajaxObject(list);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxError("程序异常！");
        }
    }

    /**
     * 处理发票打印成功
     */
    public void successInvoice() {
        try {
            if (orderIds != null && orderIds.size() > 0) {
                salesOrderService.setInvoiceStatus(orderIds);
            }
            if (batchMap != null && batchMap.size() > 0) {
                salesOrderService.handBatch(batchMap);
            }
            ajaxSuccess("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxSuccess("操作出现异常！");
        }
    }

	/* public static void main(String[] args) {
     * String ip ="113.106.56.226" ;
	 * System.out.println(isInner(ip));
	 * if(!isInner(ip)) {
	 * ip = ip + ":8081";
	 * }
	 * String address = "http://" + ip + "/wsdl/";
	 * System.out.println(address);
	 * } */

    /**
     * 判断是否为内网地址
     */
    public boolean isInner(String ip) {
        // 目前内网前缀有 16、15、192、10
        String reg = "(10|172|192|15|16)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";// 正则表达式=。
        // =、懒得做文字处理了、
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        return matcher.find();
    }

    /**
     * 创建发货批次
     */
    public String addDeliveryBatch() throws Exception {
        Warehouse warehouse = warehouseService.getDefaultWarehouse();
        Validate.notNull(warehouse);
        deliveryBatch = new DeliveryBatch();
        deliveryBatch.setWarehouseId(warehouse.getId());
        deliveryBatch.setWarehouseName(warehouse.getWarehouseName());
        deliveryBatch.setPreparedBy(ActionUtils.getLoginName());
        try {
            deliveryService.addDeliveryBatchByIds(deliveryBatch, ids);
            // 通知订单中心
            String[] idsArr = ids.split(",");
            List<Long> orderIdList = new ArrayList<Long>();
            for (String orderId : idsArr) {
                orderIdList.add(Long.valueOf(orderId));
            }
            List<SalesOrder> orderList = salesOrderService.getSalesOrderListByIds(orderIdList);
            salesOrderService.notifyOrder(orderList);
            ajaxSuccess("创建发货批次成功，请到批次列表中继续操作");
        } catch (ServiceException e) {
            logger.error("创建发货批次时出错", e);
            ajaxError("创建发货批次失败：" + e.getMessage());
        }
        return null;
    }

    /**
     * 取消订单
     */
    public String cancelOrder() throws Exception {
        try {
            salesOrderService.cancelSalesOrder(orderCode);
            ajaxSuccess("订单取消成功");
        } catch (ServiceException e) {
            logger.error("订单取消失败", e);
            ajaxError("订单取消失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 导出订单信息
     */
    private String downloadOrderInfo() {
        if (CollectionUtils.isEmpty(orderVoList)) {
            throw new ServiceException("订单数据不存在");
        }

        Map<String, String> orderSourceMap = Maps.newHashMap();
        for (OrderSource em : OrderSource.values()) {
            orderSourceMap.put(em.getCode(), em.getName());
        }

        List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
        int count = 0;
        for (SalesOrderVo item : orderVoList) {
            Map<String, String> repeatData = new HashMap<String, String>();
            String date = sdf.format(item.getOrderTime());
            Date shippingDate = item.getShippingTime();
            String shippingTime = "";
            if (null != shippingDate) {
                shippingTime = sdf.format(shippingDate);
            }

            Date opDate = item.getOpTime();
            String opTime = "";
            if (null != opDate) {
                shippingTime = sdf.format(opDate);
            }
            repeatData.put("ORDER_CODE", item.getOrderCode());
            repeatData.put("ORDER_TIME", date);
            repeatData.put("ORDER_SOURCE", orderSourceMap.get(item.getOrderSource()));
            repeatData.put("PAYMENT_NAME", item.getPaymentName());
            repeatData.put("PAY_NO", item.getPayNo());
            repeatData.put("ORDER_AMOUNT", item.getOrderAmount() + "");
            repeatData.put("PAYABLE_AMOUNT", item.getPayableAmount() + "");
            repeatData.put("SHIPPING_NAME", item.getShippingName());
            repeatData.put("SHIPPING_NO", item.getShippingNo());
            repeatData.put("ADDRESS", item.getFullAddress());
            repeatData.put("CONSIGNEE", item.getConsignee());
            repeatData.put("CONTECT", StringUtils.trimToEmpty(item.getTel()) + " " + StringUtils.trimToEmpty(item.getMobile()));
            repeatData.put("WEIGHT", item.getWeight());
            repeatData.put("STATUS", WmsConstants.getOrderStatusZh(item.getOrderStatus()));
            repeatData.put("SHIPPING_TIME", shippingTime);
            repeatData.put("INVOICE_ENABLED", item.getInvoiceEnabled() == 1 ? "是" : "否");
            repeatData.put("OP_TIME", opTime);
            repeatData.put("REMARK", item.getRemark());
            for (SalesOrderGoods goods : item.getGoodsList()) {
                // StringBuffer sb = new StringBuffer();
                // sb.append(goods.getSkuName());//.append("*").append(goods.getQuantity());
                Map<String, String> detailMap = Maps.newHashMap(repeatData);
                detailMap.put("DETAIL", StringUtils.trimToEmpty(goods.getSkuName()));
                detailMap.put("QTY", goods.getQuantity().toString());
                sheetData.add(detailMap);
                count++;
                if (count >= 40000) {
                    break; // 大于40000条终止，防止内存溢出
                }
            }
            // repeatData.put("DETAIL", sb.toString().trim());
            // sheetData.add(repeatData);
            // count++;
            if (count >= 40000) {
                break; // 大于40000条终止，防止内存溢出
            }
        }
        ExcelModule excelModule = new ExcelModule(sheetData);
        HttpServletResponse response = ServletActionContext.getResponse();
        // 清空输出流
        response.reset();
        // 设置响应头和下载保存的文件名
        response.setHeader("content-disposition", "attachment;filename=" + new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date()) + " order_list.xls");
        // 定义输出类型
        response.setContentType("APPLICATION/msexcel");
        OutputStream out = null;
        try {
            String templeteFile = ActionUtils.getProjectPath() + "/export/order_exp_template.xls";
            // String tempFile = ActionUtils.getClassPath() +
            // "config/excel/order_info_list.xls";
            String tempFile = ActionUtils.getProjectPath() + "/export/order_info_list.xls";
            // System.out.println(templeteFile + " " + tempFile);
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

    /**
     * 进入订单状态统计页面
     *
     * @return
     * @throws Exception
     */
    public String orderStatusStat() throws Exception {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderSource", orderSource);
        criteria.put("skuCode", skuCode);
        // 点击按钮查询
        if ("button".equals(queryFrom)) {
            criteria.put("orderTimeBegin", orderTimeBegin);
            criteria.put("orderTimeEnd", orderTimeEnd);
        }
        // 点击菜单查询
        else {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_MONTH, -1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            setOrderTimeBegin(c.getTime());
            setOrderTimeEnd(new Date());
            criteria.put("orderTimeBegin", getOrderTimeBegin());
            criteria.put("orderTimeEnd", getOrderTimeEnd());
        }
        orderStatusStatVoList = salesOrderService.queryOrderStatusAndCountGroupByShippingName(criteria);
        return "order_status_stat";
    }

    /**
     * 配置自动推送
     *
     * @return
     * @throws Exception
     */
    public String toConfigAutoPush() throws Exception {
        config = configService.getByKey(WmsConstants.ORDER_AUTO_PUSH_SF);
        return "config_input";
    }

    /**
     * 库存校验
     *
     * @return
     * @throws Exception
     */
    public String toConfigPushCheck() throws Exception {
        config = configService.getByKey(WmsConstants.ORDER_PUSH_CHECK_STOCK);
        return "config_input";
    }

    /**
     * 修改自动推送配置
     *
     * @return
     * @throws Exception
     */
    public String configAutoPush() throws Exception {
        configService.update(config);
        ajaxSuccess("修改成功！");
        return null;
    }

    /**
     * 订单手动推送到顺丰
     *
     * @return String
     * @throws Exception
     */
    public String pushToSF() throws Exception {
        Validate.notNull(id);
        // 初始化Model对象
        order = salesOrderService.getSalesOrder(id);
        ServiceCtrlMessage msg = salesOrderService.pushOrderToSF(order);
        if (msg.isResult()) {
            ajaxSuccess("操作成功！");
        } else {
            ajaxError(msg.getMessage());
        }

        return null;
    }

    /**
     * 一键复制订单
     *
     * @return
     * @throws Exception
     */
    public String copy() throws Exception {
        try {
            salesOrderService.copy(id);
            ajaxSuccess("复制成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxError("复制失败！");
        }
        return null;
    }

    /**
     * 查看串号
     *
     * @return
     */
    public String queryImeis() {
        Map<String, String> criteria = Maps.newHashMap();
        criteria.put("order_code", orderCode);
        List<SalesOrderImei> imeis = salesOrderMapService.queryImeis(criteria);
        ActionContext.getContext().put("imeis", imeis);

        return "order_imei_list";
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SalesOrder getOrder() {
        return order;
    }

    public void setOrder(SalesOrder order) {
        this.order = order;
    }

    public List<SalesOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<SalesOrder> orderList) {
        this.orderList = orderList;
    }

    public List<SalesOrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<SalesOrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public List<DeliveryGoods> getDeliveryGoodsList() {
        return deliveryGoodsList;
    }

    public void setDeliveryGoodsList(List<DeliveryGoods> deliveryGoodsList) {
        this.deliveryGoodsList = deliveryGoodsList;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public List<SalesOrderVo> getOrderVoList() {
        return orderVoList;
    }

    public void setOrderVoList(List<SalesOrderVo> orderVoList) {
        this.orderVoList = orderVoList;
    }

    public List<ShippingSummary> getShippingSumList() {
        return shippingSumList;
    }

    public void setShippingSumList(List<ShippingSummary> shippingSumList) {
        this.shippingSumList = shippingSumList;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public List<Shipping> getShippingList() {
        return shippingList;
    }

    public void setShippingList(List<Shipping> shippingList) {
        this.shippingList = shippingList;
    }

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<SalesOrderGoods> getOrderGoodsSummary() {
        return orderGoodsSummary;
    }

    public void setOrderGoodsSummary(List<SalesOrderGoods> orderGoodsSummary) {
        this.orderGoodsSummary = orderGoodsSummary;
    }

    public DeliveryBatch getDeliveryBatch() {
        return deliveryBatch;
    }

    public void setDeliveryBatch(DeliveryBatch deliveryBatch) {
        this.deliveryBatch = deliveryBatch;
    }

    public Long getRownum() {
        return rownum;
    }

    public void setRownum(Long rownum) {
        this.rownum = rownum;
    }

    public String getUrlPre() {
        return urlPre;
    }

    public void setUrlPre(String urlPre) {
        this.urlPre = urlPre;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getInvoiceEnabled() {
        return invoiceEnabled;
    }

    public void setInvoiceEnabled(String invoiceEnabled) {
        this.invoiceEnabled = invoiceEnabled;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getExports() {
        return exports;
    }

    public void setExports(String exports) {
        this.exports = exports;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public Boolean getEditGoods() {
        return editGoods;
    }

    public void setEditGoods(Boolean editGoods) {
        this.editGoods = editGoods;
    }

    @Override
    public void prepare() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public SalesOrder getModel() {
        return null;
    }

    @Override
    public String list() throws Exception {
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

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getShippingRemark() {
        return shippingRemark;
    }

    public void setShippingRemark(String shippingRemark) {
        this.shippingRemark = shippingRemark;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public List<OrderStatusStatVo> getOrderStatusStatVoList() {
        return orderStatusStatVoList;
    }

    public void setOrderStatusStatVoList(List<OrderStatusStatVo> orderStatusStatVoList) {
        this.orderStatusStatVoList = orderStatusStatVoList;
    }

    public List<SalesOrderLog> getSalesOrderLogList() {
        return salesOrderLogList;
    }

    public void setSalesOrderLogList(List<SalesOrderLog> salesOrderLogList) {
        this.salesOrderLogList = salesOrderLogList;
    }

    public String getQueryFrom() {
        return queryFrom;
    }

    public void setQueryFrom(String queryFrom) {
        this.queryFrom = queryFrom;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Date getPaymentTimeBegin() {
        return paymentTimeBegin;
    }

    public void setPaymentTimeBegin(Date paymentTimeBegin) {
        this.paymentTimeBegin = paymentTimeBegin;
    }

    public Date getPaymentTimeEnd() {
        return paymentTimeEnd;
    }

    public void setPaymentTimeEnd(Date paymentTimeEnd) {
        this.paymentTimeEnd = paymentTimeEnd;
    }

    public String getPrinterPre() {
        return printerPre;
    }

    public void setPrinterPre(String printerPre) {
        this.printerPre = printerPre;
    }

    public Date getShippingTimeBegin() {
        return shippingTimeBegin;
    }

    public void setShippingTimeBegin(Date shippingTimeBegin) {
        this.shippingTimeBegin = shippingTimeBegin;
    }

    public Date getShippingTimeEnd() {
        return shippingTimeEnd;
    }

    public void setShippingTimeEnd(Date shippingTimeEnd) {
        this.shippingTimeEnd = shippingTimeEnd;
    }

    /**
     * @return the config
     */
    public SystemConfig getConfig() {
        return config;
    }

    /**
     * @param config the config
     */
    public void setConfig(SystemConfig config) {
        this.config = config;
    }

    /**
     * @return the orderPushStatus
     */
    public Integer getOrderPushStatus() {
        return orderPushStatus;
    }

    /**
     * @param orderPushStatus the orderPushStatus
     */
    public void setOrderPushStatus(Integer orderPushStatus) {
        this.orderPushStatus = orderPushStatus;
    }

    public Map<String, Long> getBatchMap() {
        return batchMap;
    }

    public void setBatchMap(Map<String, Long> batchMap) {
        this.batchMap = batchMap;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Date getOrderPushTimeBegin() {
        return orderPushTimeBegin;
    }

    public void setOrderPushTimeBegin(Date orderPushTimeBegin) {
        this.orderPushTimeBegin = orderPushTimeBegin;
    }

    public Date getOrderPushTimeEnd() {
        return orderPushTimeEnd;
    }

    public void setOrderPushTimeEnd(Date orderPushTimeEnd) {
        this.orderPushTimeEnd = orderPushTimeEnd;
    }

}
