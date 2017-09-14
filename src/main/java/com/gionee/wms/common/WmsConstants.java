package com.gionee.wms.common;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WMS常量
 *
 * @author kevin
 */
public class WmsConstants {

    public static final String WMS_COMPANY = System.getProperty("wms.company", "1");// 1:金立，2:IUNI

    public static final String OFFICIAL_GIONEE_HOUSE_CODE = System.getProperty("wms.official.gionee.warehouse.code", "01");// 金立官方商城仓库编码
    public static final String OFFICIAL_IUNI_HOUSE_CODE = System.getProperty("wms.official.iuni.warehouse.code", "01");// IUNI官方商城仓库编码
    public static final String TMALL_GIONEE_HOUSE_CODE = System.getProperty("wms.tmall.gionee.warehouse.code", "01");// 金立天猫仓库编码
    public static final String TMALL_IUNI_HOUSE_CODE = System.getProperty("wms.tmall.iuni.warehouse.code", "01");// IUNI天猫仓库编码
    public static final String VIP_GIONEE_HOUSE_CODE = System.getProperty("wms.vip.gionee.warehouse.code", "01");// 金立
    // VIP仓库编码
    public static final String VIP_IUNI_HOUSE_CODE = System.getProperty("wms.vip.iuni.warehouse.code", "01");// IUNI
    // VIP仓库编码
    public static final String TAOBAO_FX_GIONEE_HOUSE_CODE = System.getProperty("wms.taobao.fx.gionee.warehouse.code", "01");// 金立淘宝分销仓库编码
    public static final String SHUNFENG_CUSTID = System.getProperty("wms.shunfeng.custid", "7693255199"); // 顺丰快递客户号
    public static final String SHUNFENG_CHECKWORK = System.getProperty("wms.shunfeng.checkwork", "_DQcPl7DO8[}p2wH"); // 顺丰校验码
    // =========顺丰对接属性 begin========
    // 校验字段
    public static final String SF_CHECKWORD = System.getProperty("wms.sf.ws.checkword", "01f18980363f40e48416464baf4cc7c0");
    // 货主
    public static final String SF_COMPANY = System.getProperty("wms.sf.ws.company", "GNEE");
    // 仓库代码
    public static final String SF_WAREHOUSE = System.getProperty("wms.sf.ws.warehouse", "571DCF");
    // 月结账号
    public static final String SF_MONTHLY_ACCOUNT = System.getProperty("wms.sf.ws.monthly_account", "7550144315");
    // 供应商
    public static final String SF_VENDOR = System.getProperty("wms.sf.ws.vendor", "GNEE");
    // =========顺丰对接属性 end ========
    public static final String EMS_VUSERNO = System.getProperty("wms.ems.vuserno", "test");
    public static final String EMS_PASSWORD = System.getProperty("wms.ems.password", "test");
    public static final String SHUNFENG_WS_URL = System.getProperty("wms.shunfeng.wsurl", "http://219.134.187.132:9090/bsp-ois/ws/expressService?wsdl");
    public static final String EMS_WS_URL = System.getProperty("wms.ems.wsurl", "http://219.134.187.38:7010/sdzzfwservice/webservice/mailQueryService");

    //======奇门对接属性======
    public static final String QIMEN_APPKEY = System.getProperty("wms.qimen.appkey", "21689850");
    public static final String QIMEN_SECRET = System.getProperty("wms.qimen.secret", "9116509adac664b2505c5afdbd653bfc");
    public static final String QIMEN_SESSIONKEY = System.getProperty("wms.qimen.sessionKey", "6100d178488f3c3be70e3e8c813605dde2de82437e511e2720912157");

    public static final String QIMEN_CUSTOMERID = System.getProperty("wms.qimen.customerId", "1442234");

    public static final String QIMEN_BIZURL = System.getProperty("wms.qimen.bizurl", "http://qimen.api.taobao.com/router/qimen/service");
    public static final String QIMEN_TESTURL = System.getProperty("wms.qimen.testurl", "http://qimenapi.tbsandbox.com/router/qimen/service");

    public static final String QIMEN_TESTAPPKEY = System.getProperty("wms.qimen.appkey", "1021689850");
    public static final String QIMEN_TESTSECRET = System.getProperty("wms.qimen.secret", "sandboxadac664b2505c5afdbd653bfc");


    /**
     * 应用名
     */
    public static final String APP_NAME = "wms";

    /**
     * 默认编码
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * WEB SERVICE的目标命名空间
     */
    public static final String TARGET_NS = "http://wms.cm.com/api/v1";

    /**
     * 布尔型常量
     */
    public static final int ENABLED_TRUE = 1; // 可用
    public static final int ENABLED_FALSE = 0; // 不可用

    /**
     * 商品分类 树形结构根ID
     */
    public static final int WARES_CAT_ROOT_ID = 0;

    /**
     * ECShop与KuaiDi100 快递公司编号映射
     */
    public static final Map<String, String> shippingComCodeMap = new HashMap<String, String>();
    /**
     * 以HTML格式返回的快递公司编号
     */
    public static final List<String> htmlShippingComCodeList = new ArrayList<String>();

    static {
        shippingComCodeMap.put("zto", "zhongtong");// 中通快递
        shippingComCodeMap.put("yto", "yuantong");// 圆通快递
        shippingComCodeMap.put("city_express", "city_express");// 城际快递

        shippingComCodeMap.put("sto_express", "shentong");// 申通快递
        shippingComCodeMap.put("ems", "ems");// EMS
        shippingComCodeMap.put("sf_express", "shunfeng");// 顺丰快递
        shippingComCodeMap.put("post_email", "youzhengguonei");// 邮政平邮
        shippingComCodeMap.put("post_express", "youzhengguonei");// 邮政快递包裹

        htmlShippingComCodeList.add("shentong");
        htmlShippingComCodeList.add("ems");
        htmlShippingComCodeList.add("shunfeng");
        htmlShippingComCodeList.add("youzhengguonei");
        System.out.println("=============初始化物流完毕=============");
    }

    /**
     * 接口请求结果常量
     */
    public static enum Result {
        SUCCESS("1", "success"), ERROR("0", "error");

        private final String code;
        private final String message;

        Result(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return message;
        }

    }

    /**
     * 与Kuaidi100接口请求结果状态常量
     */
    public static enum Status {
        NO_RECORD("0", "no record"), SUCCESS("1", "success"), EXCEPTION("2", "exception"), PARAM_ERROR("3", "param error"), VALID_CODE_ERROR("408", "valid code error");

        private final String code;
        private final String message;

        Status(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return message;
        }

    }

    /**
     * 与UUC对接的常量
     */
    public static final String UUC_URL = System.getProperty("wms.uuc.url", "http://passport.cm.com/iuc");// 用户中心地址
    public static final String UUC_AUTHC_TICKET_URI = "/sso?";// 取用户ticket
    public static final String UUC_AUTHC_LOGOUT_URI = "/logout?";// 登出
    public static final String UUC_AUTHC_CALLBACK_URI = "/authcCallback";// 认证回调接口，如
    // http://18.8.0.28:8080/wms/authcCallback?ticket=ST-271-5m6bog4ayUMUABXIHJU7-sso
    public static final String UUC_AUTHC_VALIDATE_URI = "/cas/validate";// 验证ticket
    public static final String UUC_AUTHZ_CHECK_URI = "/cas/validlogin";// 检测是否登录
    public static final String UUC_FETCH_MENU_URI = "/cas/menu";// 取用户菜单
    public static final String UUC_FETCH_PERMISSION_URI = "/cas/permission";// 取用户权限
    public static final String UUC_APP_WMS_ID = System.getProperty("wms.uuc.app.id");// WMS在用户中心配置的应用ID
    public static final String UUC_APP_WMS_KEY = System.getProperty("wms.uuc.app.key");// WMS在用户中心配置的应用ID

    /**
     * 与ECSHOP对接的常量
     */
    public static final String ECSHOP_SYNC_STOCK_API = System.getProperty("wms.ecs.sync.stock.api", "http://shop.cm.com/api/notify_stock.php");// ECSHOP同步库存接口
    public static final String ECSHOP_UPDATE_ORDER_API = System.getProperty("wms.ecs.update.order.api", "http://shop.cm.com/api/notify_order_shipping.php");// ECSHOP订单更新接口
    public static final String SYNC_ORDER_SALT = "#GFDK435&LKDD!"; // 作用于订单同步签名盐值
    public static final String ECSHOP_SALT = "#GFDK435&LKDD!"; // ECSHOP签名盐值

    /**
     * 与IUNI订单系统对接常量
     */
    public static final String OC_SYNC_STOCK_API = System.getProperty("wms.oc.sync.stock.api", "http://goods.virtual.iuni.com/api/deltaStock");// 订单系统同步库存接口
    public static final String OC_UPDATE_ORDER_API = System.getProperty("wms.oc.update.order.api", "http://18.8.0.142:9090/ordermgr/orderApi/notifyOrderShipping?");// 订单系统订单更新接口

    /**
     * 与SAP对接的常量
     */
    // public static final String SAP_SUPPLILER_CODE =
    // System.getProperty("wms.sap.supplier.code");//SAP对应的供应商编号
    // public static final String WMS_SAP_WS_USERNAME =
    // System.getProperty("wms.sap.ws.username",
    // "DIANSHANGINVOICE");//sap对接用户名（开发）
    public static final String WMS_SAP_WS_USERNAME = System.getProperty("wms.sap.ws.username", "IUNISALE");// sap对接用户名（测试）
    public static final String WMS_SAP_WS_PASSWORD = System.getProperty("wms.sap.ws.password", "888888");// sap对接密码

    public static final boolean INDIV_PUSH_FLAG = ("1".equals(System.getProperty("wms.sap.indivpush.flag", "1"))) ? true : false;// 个体推送开关1：打开，0：关闭

    /**
     * 与Kuaidi100对接的常量
     */
    public static final String Kuaidi100_KEY = System.getProperty("wms.biz.kuaidi100.key", "9675013a6cc700a6");// kuaidi100
    // key
    public static final String Kuaidi100_URL = System.getProperty("wms.biz.kuaidi100.url", "http://api.kuaidi100.com/api");// kuaidi100接口地址
    public static final String Kuaidi100_HTML_URL = System.getProperty("wms.biz.kuaidi100.html.url", "http://www.kuaidi100.com/applyurl");// kuaidi100接口地址
    public static final String Kuaidi100_CALLBACK_URL = System.getProperty("wms.biz.kuaidi100.callback", "http://kuaidi100.gionee.com/api/kuaidiCallback.action");// kuaidi回调地址
    public static final String Kuaidi100_PUSH_URL = System.getProperty("wms.biz.kuaidi100.push", "http://www.kuaidi100.com/poll"); // 快递100推送地址
    public static final String Kuaidi100_SUB_KEY = System.getProperty("wms.biz.kuaidi100.subkey");

    /**
     * 与淘宝对接常量
     */
    public static final boolean TAOBAO_FLAG = ("1".equals(System.getProperty("wms.taobao.flag", "1"))) ? true : false;// 淘宝订单同步开关1：打开，0：关闭
    public static final String TAOBAO_NOTIFY_URL = System.getProperty("wms.taobao.notify.url", "http://stream.api.taobao.com/stream");// 淘宝主动通知地址
    public static final String TAOBAO_TOP_URL = System.getProperty("wms.taobao.top.url", "http://gw.api.taobao.com/router/rest");// 淘宝TOP地址
    public static final String TAOBAO_APPKEY = System.getProperty("wms.taobao.appkey", "21642154");// 淘宝应用证书
    public static final String TAOBAO_SECRET = System.getProperty("wms.taobao.secret", "65dd78fa5f53758ccb50ad98457bbf3b");// 淘宝应用密钥
    public static final String TAOBAO_SESSIONKEY = System.getProperty("wms.taobao.sessionkey", "6100d178488f3c3be70e3e8c813605dde2de82437e511e2720912157");// 淘宝应用授权令牌
    public static final String TOP_TO_SEND_URL = System.getProperty("wms.top.send.url", "http://121.196.132.61:30001/orderSyncServer/order/toSend");// 聚石塔应用发货接口

    /**
     * 与唯品会订单对接同步
     */
    public static final boolean VIP_FLAG = ("1".equals(System.getProperty("wms.vip.scm.flag", "1"))) ? true : false;// 唯品订单同步开关1：打开，0：关闭
    public static final String VIP_SCM_SID = System.getProperty("wms.vip.scm.sid", "29");// 唯品SID
    public static final String VIP_SCM_SOURCE = System.getProperty("wms.vip.scm.source", "4ed8845ae941c129c9fbd18e62bc88f8");// 唯品SOURCE
    public static final String VIP_SCM_URL = System.getProperty("wms.vip.scm.url", "http://visopentest.vipshop.com/api/scm");// 唯品接口地址
    public static final String VIP_SCM_ORDER_LIST = System.getProperty("wms.vip.scm.order.list", "/pop/order_list.php");// 订单列表地址
    public static final String VIP_SCM_ORDER_LIST_API_NAME = System.getProperty("wms.vip.scm.order.list.api.name", "pop/order_list");// 订单列表接口名称
    public static final String VIP_SCM_ORDER_GOODS_LIST = System.getProperty("wms.vip.scm.order.goods.list", "/pop/get_pop_order_goods_list.php");// 订单商品列表地址
    public static final String VIP_SCM_ORDER_GOODS_LIST_API_NAME = System.getProperty("wms.vip.scm.order.goods.list.api.name", "pop/get_pop_order_goods_list");// 订单商品列表接口名称
    public static final String VIP_SCM_ORDER_EXPORT = System.getProperty("wms.vip.scm.order.export", "/pop/export.php");// 供应商根据订单号码修改订单导出状态，支持批量导出
    public static final String VIP_SCM_ORDER_EXPORT_API_NAME = System.getProperty("wms.vip.scm.order.export.api.name", "pop/export");// 订单导出接口名称
    public static final String VIP_SCM_SHIP = System.getProperty("wms.vip.scm.ship", "/pop/ship.php");// 批量发货地址
    public static final String VIP_SCM_SHIP_API_NAME = System.getProperty("wms.vip.scm.ship.api.name", "pop/ship");// 批量发货接口名称

    /**
     * excel相关常量
     */
    public static final String CHECK_EXCEL_DOWNLOAD_NAME = "stock_check_list.xls";// 盘点表客户端下载文件名
    public static final String DISTINFO_EXCEL_DOWNLOAD_NAME = "dist_info_list.xls";// 配送信息客户端下载文件名
    public static final String CHECK_EXP_TEMPLETE_FILE = "config/excel/check_exp_templete.xls";// 盘点单导出模板文件类路径
    public static final String DISTINFO_EXP_TEMPLETE = "config/excel/distinfo_exp_templete.xls";// 配送信息导出模板所在类路径
    public static final String CHECK_IMP_DESC_FILE = "config/excel/check_imp_desc.xml";// 盘点单导入模板描述文件路径
    public static final String CHECK_IMP_DATA_FILE = "config/excel/check_imp_data.xls";// 盘点单导入模板文件路径
    public static final String CHECK_IMP_DESC_INDIV_FILE = "config/excel/check_imp_indiv_desc.xml";// 个体盘点单导入模板描述文件路径
    public static final String CHECK_IMP_DATA_INDIV_FILE = "config/excel/check_imp_indiv_data.xls";// 个体盘点单导入模板文件路径
    public static final String EXCEL_TEMP_PATH = "/config/excel/";// Excel文件临时类路径
    public static final Long EXCEL_UPLOAD_MAXIMUM_SIZE = 1024 * 1024L;// 上传EXCEL的大小限制2M
    public static final String EXCEL_UPLOAD_ALLOWED_TYPES = "application/msexcel;application/vnd.ms-excel;application/kset;";// 上传EXCEL的大小限制2M
    public static final String XML_UPLOAD_ALLOWED_TYPES = "text/xml";// 上传XML文件类型

    public static final String TANSFER_IMP_DATA = "config/excel/tansfer_imp_data.xls";// 配送订单模版
    public static final String TANSFER_IMP_DESC = "config/excel/tansfer_imp_desc.xml";// 配送订单模版文件描述
    public static final String TANSFER_IMP_DEMO = "config/excel/tansfer_imp_demo.xls"; //导出模版

    public static final String TANSFER_GOODS_IMP_DATA = "config/excel/tansfer_goods_imp_data.xls";// 配送商品模版
    public static final String TANSFER_GOODS_IMP_DESC = "config/excel/tansfer_goods_imp_desc.xml";// 配送商品模版文件描述
    public static final String TANSFER_GOODS_IMP_DEMO = "config/excel/tansfer_goods_imp_demo.xls"; //导出商品模版

    public static final String SHOPPING_LIST_FTL_PATH = "/WEB-INF/ftl/shoppingList.ftl";// 购物清单模板所在类路径
    public static final String SHIPPING_FTL_PATH = "/WEB-INF/ftl/shipping.ftl";// 运单模板所在类路径
    public static final String SHIPPING_SF_TEMPLATE = "/WEB-INF/ftl/shippingSf.ftl";// 运单模板所在类路径

    public static final String LODOP_URL_PRE = System.getProperty("wms.lodop.url.pre", "http://127.0.0.1:8080/wms");// LODOP
    // url前缀
    public static final String PRINTER_PRE = System.getProperty("wms.printer.pre", "");// 网络打印机前缀

    /**
     * 发票相关
     */
    public static final String INVOICE_WS_URL = System.getProperty("wms.invoice.ws.url", "http://127.0.0.1/wsdl/");// 开票地址
    public static final String INVOICE_TYPE = System.getProperty("wms.invoice.type", "2");// 发票类型
    public static final String INVOICE_SELLER_NO = System.getProperty("wms.invoice.seller.no", "110101000000000");// 销方税号
    public static final String INVOICE_SELLER_PASSWORD = System.getProperty("wms.invoice.seller.password", "111111");// 密码
    public static final String INVOICE_SELLER_ADDRESS = System.getProperty("wms.invoice.seller.address", "深圳市福田区深南大道7028号时代科技大厦21楼 0755-83581704");// 销方地址电话
    // 最大长度100字节
    // 可为空有开票系统决定
    public static final String INVOICE_SELLER_ACCOUNT = System.getProperty("wms.invoice.seller.account", "上海浦东发展银行深圳分行79010154740016888");// 销方银行帐号
    // 最大长度100字节
    // 可为空有开票系统决定
    public static final String INVOICE_RATE = System.getProperty("wms.invoice.rate", "17");// 税率
    // 整数可为17、11、6等税局规定的税率
    public static final String INVOICE_MAKER = System.getProperty("wms.invoice.maker", "冯盼盼");// 开票人
    // 最大长度10字节
    public static final String INVOICE_CHECK = System.getProperty("wms.invoice.check", "陈金元");// 复核人
    // 最大长度10字节
    public static final String INVOICE_COLLECT = System.getProperty("wms.invoice.collect", "李慧");// 收款人
    // 最大长度10字节

    public static final String DEFAULT_USERNAME_LOG = "WMS业务记录者";// 业务日志记录者名称

    /**
     * 是否开启WMS辅助服务
     */
    public static final boolean WMS_ASSISTANT_FLAG = ("1".equals(System.getProperty("wms.biz.assistant.flag", "1"))) ? true : false;

    /**
     * 是否打开订单超卖
     */
    public static final boolean WMS_ORDER_OVER_FLAG = ("1".equals(System.getProperty("wms.biz.order.over.flag", "1"))) ? true : false;

    // 条形码存储相对路径(发货编号)
    public static final String BAR_CODE_PATH = "/barCodeTemp/";
    // 条形码存储相对路径(调拨批次号)
    public static final String BAR_CODE_TRANSER_PATH = "/barCodeTransferTemp/";

    /**
     * 库存类型
     */
    public static enum StockType {
        STOCK_SALES("1", "可销售库存"), // 可销售库存
        STOCK_OCCUPY("2", "占用库存"), // 占用库存
        STOCK_UNSALES("3", "不可销售库存"), // 不可销售库存
        STOCK_TOTAL("4", "总库存");// 总库存

        private final String code;
        private final String name;

        StockType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 库存变动业务类型
     */
    public static enum StockBizType {
        IN_PURCHASE("101", "采购进货"), IN_RMA("102", "退换货"), IN_REFUSE("103", "拒收"), IN_BACK_TRANSFER("104", "调拨退货"), IN_PURCHARMA("105", "采购退仓入库"), IN_SHUADAN("106", "刷单入库"), OUT_SALES("201", "销售发货"), OUT_TRANSFER("202", "调货"), CONVERT_ORDER_OCCUPY("301", "订单占用"), CONVERT_SALES2UNSALES("302",
            "良品转次品"), CONVERT_UNSALES2SALES("303", "次品转良品"), CONVERT_CANCEL_ORDER("304", "取消订单"), CONVERT_UPDATE_ORDER("305", "修改订单"), CONVERT_CANCEL_TRANS("306", "取消调拔"), CHECK_IN("401", "盘点入库"), CHECK_OUT("402", "盘点出库");
        private final String code;
        private final String name;

        StockBizType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 个体商品状态
     */
    public static enum IndivWaresStatus {
        NON_DEFECTIVE(1, "良品"), // 良品
        DEFECTIVE(0, "次品"); // 次品

        private final int code;
        private final String name;

        IndivWaresStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 个体库存状态
     */
    public static enum IndivStockStatus {
        STOCKIN_HANDLING(1, "入库中"), IN_WAREHOUSE(2, "在库"), STOCKOUT_HANDLING(3, "出库中"), OUT_WAREHOUSE(4, "出库");

        private final int code;
        private final String name;

        IndivStockStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    }

    /**
     * 商品个体流转类型
     */
    public static enum IndivFlowType {
        PUR_PRE_RECV("100", "采购预收"), IN_PURCHASE("101", "采购进货"), IN_RMA("102", "退换货"), // 在库
        OUT_SALES("201", "销售发货"), // 出库
        IN_REFUSE("103", "退换货"), // 在库
        IN_BACK_TRANSFER("104", "调拨退货"); // 入库

        private final String code;
        private final String name;

        IndivFlowType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 收货类型
     */
    public static enum ReceiveType {
        PURCHASE("101", "采购进货"), RMA("102", "退换货"), REFUSE("103", "拒收"), PURCHARMA("105", "采购退仓入库"), SHUADAN("106", "刷单入库");

        private final String code;
        private final String name;

        ReceiveType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 收货方式
     */
    public static enum ReceiveMode {
        AUTO(1, "自动收货"), MANUAL(2, "手工收货");
        private final int code;
        private final String name;

        ReceiveMode(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 入库状态
     */
    public static enum StockInStatus {
        PENDING("0", "待处理"), // 待处理
        FINISHED("1", "已完成"); // 处理完成

        private final String code;
        private final String name;

        StockInStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        // @Override
        // public String toString() {
        // return name;
        // }
    }

    /**
     * 销售订单来源
     */
    public static enum OrderSource {
        /**
         * OFFICIAL_GIONEE
         */
        OFFICIAL_GIONEE("1", "金立官网"),
        /**
         * TMALL_GIONEE
         */
        TMALL_GIONEE("2", "金立天猫"),
        /**
         * YHD
         */
        YHD("10", "1号店"),
        /**
         * OFFICIAL_IUNI
         */
        OFFICIAL_IUNI("3", "IUNI官网"),
        /**
         * TMALL_IUNI
         */
        TMALL_IUNI("4", "IUNI天猫"),
        /**
         * VIP_GIONEE
         */
        VIP_GIONEE("5", "金立唯品"),
        /**
         * VIP_IUNI
         */
        VIP_IUNI("6", "IUNI唯品"),
        /**
         * TAOBAO_FX_GIONEE
         */
        TAOBAO_FX_GIONEE("7", "金立淘宝分销"),
        /**
         * SELF_ORDER
         */
        SELF_ORDER("8", "内部购机"),
        /**
         * PAIPAI_GIONEE
         */
        PAIPAI_GIONEE("9", "金立拍拍网"),
        /**
         * WEI_DIAN
         */
        WEI_DIAN("11", "微店"),
        /**
         * 移动互联网
         */
        // YI_DONG_HU_LIAN_WANG("12", "移动互联网"),
        /**
         * 京东
         */
        JD("13", "京东"),
        /**
         * 苏宁
         */
        SU_NING("14", "苏宁"),
        /**
         * 云朵
         */
        YUN_DUO("15", "云朵"),
        /**
         * 金立钱包
         */
        GIONEE_WALLET("16", "金立钱包"),
        /**
         * 问鼎商贸
         */
        WDSM("17", "问鼎商贸"),
        /**
         * 中国移动
         */
        CHINA_MOBILE("18", "中国移动"),

        LTHC("19", "联通华盛"),
        /**
         * 山东三际
         */
        SDSJ("20", "山东三际"),
        /**
         * 杭州玩风
         */
        HZWF("21", "杭州玩风"),
        /**
         * 智轩优品
         */
        ZXYP("22", "智轩优品"),
        /**
         * 悦高数码
         */
        YGSM("23", "悦高数码"),
        /**
         * 自媒体
         */
        ZMT("24", "自媒体活动");

        private final String code;
        private final String name;

        OrderSource(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static Map<String, String> getValueMap() {
            Map<String, String> valueMap = Maps.newHashMap();
            for (OrderSource o : OrderSource.values()) {
                valueMap.put(o.getCode(), o.getName());
            }
            return valueMap;
        }

    }

    public static String getOrderSource(String orderSource) {
        if (orderSource.equals(OrderSource.OFFICIAL_IUNI.getCode())) {
            return OrderSource.OFFICIAL_IUNI.getName();
        } else if (orderSource.equals(OrderSource.TMALL_IUNI.getCode())) {
            return OrderSource.TMALL_IUNI.getName();
        } else if (orderSource.equals(OrderSource.VIP_IUNI.getCode())) {
            return OrderSource.VIP_IUNI.getName();
        } else if (orderSource.equals(OrderSource.OFFICIAL_GIONEE.getCode())) {
            return OrderSource.OFFICIAL_GIONEE.getName();
        } else if (orderSource.equals(OrderSource.TMALL_GIONEE.getCode())) {
            return OrderSource.TMALL_GIONEE.getName();
        } else if (orderSource.equals(OrderSource.VIP_GIONEE.getCode())) {
            return OrderSource.VIP_GIONEE.getName();
        } else if (orderSource.equals(OrderSource.TAOBAO_FX_GIONEE.getCode())) {
            return OrderSource.TAOBAO_FX_GIONEE.getName();
        } else if (orderSource.equals(OrderSource.SELF_ORDER.getCode())) {
            return OrderSource.SELF_ORDER.getName();
        }
        return "";
    }

    public static String getOrderCodeByName(String orderSourceName) {
        if (orderSourceName.equals(OrderSource.OFFICIAL_IUNI.getName())) {
            return OrderSource.OFFICIAL_IUNI.getCode();
        } else if (orderSourceName.equals(OrderSource.TMALL_IUNI.getName())) {
            return OrderSource.TMALL_IUNI.getCode();
        } else if (orderSourceName.equals(OrderSource.VIP_IUNI.getName())) {
            return OrderSource.VIP_IUNI.getCode();
        } else if (orderSourceName.equals(OrderSource.OFFICIAL_GIONEE.getName())) {
            return OrderSource.OFFICIAL_GIONEE.getCode();
        } else if (orderSourceName.equals(OrderSource.TMALL_GIONEE.getName())) {
            return OrderSource.TMALL_GIONEE.getCode();
        } else if (orderSourceName.equals(OrderSource.VIP_GIONEE.getName())) {
            return OrderSource.VIP_GIONEE.getCode();
        } else if (orderSourceName.equals(OrderSource.TAOBAO_FX_GIONEE.getName())) {
            return OrderSource.TAOBAO_FX_GIONEE.getCode();
        } else if (orderSourceName.equals(OrderSource.SELF_ORDER.getName())) {
            return OrderSource.SELF_ORDER.getCode();
        }
        return "";
    }

    /**
     * 销售订单来源，金立
     */
    public static enum OrderSourceGionee {
        OFFICIAL_GIONEE("1", "金立官网"),
        /**
         * TMALL_GIONEE
         */
        TMALL_GIONEE("2", "金立天猫"),
        /**
         * YHD
         */
        YHD("10", "1号店"),
        /**
         * TAOBAO_FX_GIONEE
         */
        TAOBAO_FX_GIONEE("7", "金立淘宝分销"),
        /**
         * SELF_ORDER
         */
        SELF_ORDER("8", "内部购机"),
        /**
         * 移动互联网
         */
        //YI_DONG_HU_LIAN_WANG("12", "移动互联网"),
        /**
         * 京东
         */
        JD("13", "京东"),
        /**
         * 苏宁
         */
        SU_NING("14", "苏宁"),
        /**
         * 云朵
         */
        YUN_DUO("15", "云朵"),
        /**
         * 金立钱包
         */
        GIONEE_WALLET("16", "金立钱包"),
        /**
         * 问鼎商贸
         */
        WDSM("17", "问鼎商贸"),
        /**
         * 中国移动
         */
        CHINA_MOBILE("18", "中国移动"),

        LTHC("19", "联通华盛"),
        /**
         * 山东三际
         */
        SDSJ("20", "山东三际"),
        /**
         * 杭州玩风
         */
        HZWF("21", "杭州玩风"),
        /**
         * 智轩优品
         */
        ZXYP("22", "智轩优品"),
        /**
         * 悦高数码
         */
        YGSM("23", "悦高数码"),
        /**
         * 自媒体
         */
        ZMT("24", "自媒体活动");

        private final String code;
        private final String name;

        OrderSourceGionee(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static OrderSourceGionee get(String code) {
            OrderSourceGionee[] values = OrderSourceGionee.values();
            for (OrderSourceGionee e : values) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }

    /**
     * 销售订单来源，IUNI
     */
    public static enum OrderSourceIuni {
        OFFICIAL_IUNI("3", "IUNI官网"), SELF_ORDER("8", "内部购机");

        private final String code;
        private final String name;

        OrderSourceIuni(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static String getOrderSourceZh(String code) {
        if (OrderSource.OFFICIAL_GIONEE.code.equals(code)) {
            return OrderSource.OFFICIAL_GIONEE.name;
        } else if (OrderSource.TMALL_GIONEE.code.equals(code)) {
            return OrderSource.TMALL_GIONEE.name;
        }
        return "";
    }

    /**
     * 销售订单处理状态
     */
    public static enum OrderStatus {
        FILTERED(0, "已筛单"), // UNSHIPPED
        PRINTED(2, "已打单"), // FILTED
        PICKED(3, "已配货"), SHIPPED(1, "已出库"), BACKED(4, "已退货"), RECEIVED(5, "已签收"), // CHECKED
        BACKING(6, "退货中"), REFUSEING(7, "拒收中"), PICKING(8, "配货中"), SHIPPING(9, "待出库"), REFUSED(10, "已拒收"), CANCELED(-1, "已取消"), UNFILTER(11, "未筛单");

        private final int code;
        private final String name;

        OrderStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static String getOrderStatusZh(int i) {
        switch (i) {
            case 0:
                return OrderStatus.FILTERED.name;
            case 2:
                return OrderStatus.PRINTED.name;
            case 3:
                return OrderStatus.PICKED.name;
            case 1:
                return OrderStatus.SHIPPED.name;
            case 4:
                return OrderStatus.BACKED.name;
            case 5:
                return OrderStatus.RECEIVED.name;
            case 7:
                return OrderStatus.REFUSEING.name;
            case -1:
                return OrderStatus.CANCELED.name;
            case 8:
                return OrderStatus.PICKING.name;
            case 9:
                return OrderStatus.SHIPPING.name;
            case 10:
                return OrderStatus.REFUSED.name;
            case 11:
                return OrderStatus.UNFILTER.name;
        }
        return "未知";
    }

    /**
     * 发货状态
     */
    public static enum DeliveryStatus {
        UNSHIPPED(0, "未发货"), SHIPPED(1, "已发货"), CANCELED(-1, "已取消");
        private final int code;
        private final String name;

        DeliveryStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 支付类型
     */
    public static enum PaymentType {
        ONLINE(1, "在线支付"), COD(2, "货到付款");
        private final int code;
        private final String name;

        PaymentType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 发票类型
     */
    public static enum InvoiceType {
        PLAIN(1, "普通发票"), VAT(2, "增值税发票");
        private final int code;
        private final String name;

        InvoiceType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 对外通知状态(订单状态变更、配送状态变更)
     */
    public static enum NotifyStatus {
        UNNOTIFIED(0, "未通知"), NOTIFIED_SUCCESS(1, "通知成功"), NOTIFIED_FAIL(2, "通知失败");

        private final int code;
        private final String name;

        NotifyStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 远程调用状态
     */
    public static enum RemoteCallStatus {
        PENDING(0, "待处理"), SUCCESS(1, "处理成功"), FAIL(2, "处理失败");

        private final int code;
        private final String name;

        RemoteCallStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 出库类型
     */
    public static enum StockOutType {
        SALES("201", "销售发货");

        private final String code;
        private final String name;

        StockOutType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 发货批次状态
     */
    public static enum DeliveryBatchStatus {
        PENDING(0, "待处理"), FINISHED(1, "已完成");
        // CANCELED(-1,"已取消");

        private final int code;
        private final String name;

        DeliveryBatchStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 盘点状态
     */
    public static enum StockCheckStatus {
        PENDING(0, "待处理"), UNCONFIRMED(2, "待确认"), FINISHED(1, "已确认");

        private final int code;
        private final String name;

        StockCheckStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    }

    /**
     * 盘点状态
     */
    public static enum CheckTaskStatus {
        PENDING("0"), // 待处理
        HANDLING_FIRST("2"), // 已预盘
        HANDLING_SECOND("3"), // 已复盘
        FINISHED("1");// 处理完成

        private final String value;

        CheckTaskStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * 盘点任务类型
     */
    public static enum StockCheckTaskType {
        FIRST("1", "预盘"), SECOND("1", "复盘");

        private final String code;
        private final String name;

        StockCheckTaskType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 操作记录类型
     */
    public static enum OpType {
        LOGIN("100", "登录"), CONFIRM_PURCHASE_IN("102", "确认采购进货"), CONFIRM_RMA_IN("103", "确认退换入库"), CONFIRM_SALES_OUT("201", "确认销售发货"), CONVERT_WARES_STATUS("302", "更改商品状态");
        private final String bizCode;
        private final String bizName;

        OpType(String bizCode, String bizName) {
            this.bizCode = bizCode;
            this.bizName = bizName;
        }

        public String getBizCode() {
            return bizCode;
        }

        public String getBizName() {
            return bizName;
        }

        @Override
        public String toString() {
            return bizName;
        }
    }

    /**
     * 仓库类型
     */
    public static enum WarehouseType {
        PHYSICAL("1", "实仓"), VIRTUAL("0", "虚仓");

        private final String code;
        private final String name;

        WarehouseType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 采购单状态
     */
    public static enum PurchaseOrderStatus {
        UNRECEIVED(0, "未收货"), RECEIVING(2, "收货中"), RECEIVED(1, "已发货"), CANCELED(-1, "已取消");
        private final int code;
        private final String name;

        PurchaseOrderStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 收货状态
     */
    public static enum ReceiveStatus {
        UNRECEIVED(0, "未收货"), RECEIVING(2, "收货中"), RECEIVED(1, "已收货"), CANCELED(-1, "已取消");
        private final int code;
        private final String name;

        ReceiveStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 快递100返回状态状态
     */
    public static enum KuaidiStatus {
        UNSUB(0, "未订阅"), SUCCESS(200, "提交成功"), REFUSE(701, "拒绝的快递公司"), NOTSUPPORT(700, "不支持的快递公司"), NOTAUTH(600, "未受权"), ERROR(500, "服务器异常");
        private final int code;
        private final String name;

        KuaidiStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 远程调用状态
     */
    public static enum RemoteOrderStatus {
        PREPARING("PREPARING", "已打单"), SENDING("SENDING", "已发货"), SHIPPED("SHIPPED", "已签收"), REFUNDING("REFUNDING", "已退货"), REJECTING("REJECTING", "拒收中"), REFUSED("REFUSED", "已拒收");

        private final String code;
        private final String name;

        RemoteOrderStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 退货状态
     */
    public static enum BackStatus {
        BACKING(1, "退货中"), BACKED(2, "已退货"), REFUND(3, "已退款"), RENEWED(4, "已换新"), CANCELED(-1, "已取消");
        private final int code;
        private final String name;

        BackStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static enum LogType {
        OP_LOG(1, "操作日志"), BIZ_LOG(2, "业务日志");

        private final int code;
        private final String name;

        LogType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static enum TransferStatus {
        UN_DELIVERYD(1, "未发货"), DELIVERYED(2, "已发货"), DELIVERYING(3, "配货中"), CHECKED(4, "已审核"), CANCELED(5, "已取消");

        private final int code;
        private final String name;

        TransferStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 盘点比较类型
     */
    public static enum StockCheckCompareType {
        CHECK_COMPARE_MORE("1", "盘点多出的数据"), CHECK_COMPARE_LESS("2", "系统多出的数据");

        private final String code;
        private final String name;

        StockCheckCompareType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 盘点异常类型
     */
    public static enum CheckStatusExceptionType {
        CHECK_DEFECTIVE(0, "次品"), CHECK_NONDEFECTIVE(1, "良品"), CHECK_OUTHOUSE(2, "已出库"), CHECK_INHOUSE(3, "在库"), CHECK_NONSKU(4, "IMEI存在,SKU不一致");

        private final Integer code;
        private final String name;

        CheckStatusExceptionType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 每日库存信息临时数据类型
     */
    public static enum DailyStockTempType {
        START_QTY(1, "期初结余"), OUT_QTY(2, "本期出库"), OCCUPY_QTY(3, "占用未出库"), END_QTY(4, "期末结余");

        private final Integer code;
        private final String name;

        DailyStockTempType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 盘点单库存信息备份状态
     */
    public static enum StockDumpStatus {
        UNDUMP(0, "未备份"), DUMPED(1, "已备份");

        private final Integer code;
        private final String name;

        StockDumpStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 订单推送状态代码枚举
     *
     * @author PengBin 00001550<br>
     * @date 2014年8月19日 下午5:10:30
     */
    public static enum OrderPushStatusEnum {
        /**
         * 未推送
         */
        UN_PUSHED(0),
        /**
         * 已经推送过
         */
        PUSHED(1);

        private OrderPushStatusEnum(int code) {
            this.code = code;
        }

        private final int code;

        public int getCode() {
            return code;
        }
    }

    /**
     * 配送方式枚举
     *
     * @author PengBin 00001550<br>
     * @date 2014年8月19日 下午5:43:37
     */
    public static enum ShippingEnum {
        /**
         * 顺丰速运
         */
        SF("sf_express", "顺丰速运");

        private ShippingEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private final String code;
        private final String name;

        /**
         * @return the code
         */
        public String getCode() {
            return code;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

    }

    /**
     * 库存状态枚举
     *
     * @author PengBin 00001550<br>
     * @date 2014年8月25日 下午6:46:51
     */
    public static enum InventoryStatusEnum {
        ZHENGPIN("10"), CIPIN("20");

        private InventoryStatusEnum(String code) {
            this.code = code;
        }

        private final String code;

        public String getCode() {
            return code;
        }
    }

    /**
     * 出库订单类型枚举
     *
     * @author PengBin 00001550<br>
     * @date 2014年8月26日 上午11:05:54
     */
    public static enum OutOrderTypeEnum {
        /**
         * 销售订单
         */
        SALE_ORDER("销售订单");

        private OutOrderTypeEnum(String code) {
            this.code = code;
        }

        private final String code;

        public String getCode() {
            return code;
        }
    }

    /**
     * 仓库编码枚举
     *
     * @author PengBin 00001550<br>
     * @date 2014年8月28日 下午3:24:02
     */
    public static enum WarehouseCodeEnum {
        /**
         * 东莞电商仓
         */
        DONG_GUAN_WAREHOUSE("01"),
        /**
         * 顺丰仓
         */
        SF_WAREHOUSE("SF");

        private WarehouseCodeEnum(String code) {
            this.code = code;
        }

        private final String code;

        /**
         * 返回wms_warehouse表的warehouse_code字段值
         *
         * @return
         */
        public String getCode() {
            return code;
        }
    }

    /**
     * SKU映射outerCode第三方公司编码
     *
     * @author PengBin 00001550<br>
     * @date 2014年8月29日 下午2:14:25
     */
    public static enum SkuMapOuterCodeEnum {
        /**
         * 销售订单
         */
        VIP("vip"), SF("sf");

        private SkuMapOuterCodeEnum(String code) {
            this.code = code;
        }

        private final String code;

        public String getCode() {
            return code;
        }
    }

    /**
     * 布尔型常量
     */
    public static final int TRANS_TYPE_NONDEFECTIVE = 0; // 良品调拨
    public static final int TRANS_TYPE_DEFECTIVE = 1; // 次品调拨

    /**
     * 系统配置表，是否自动推送到顺丰key
     */
    public static final String ORDER_AUTO_PUSH_SF = "ORDER_AUTO_PUSH_SF";
    /**
     * 推送前是否库存校验
     */
    public static final String ORDER_PUSH_CHECK_STOCK = "ORDER_PUSH_CHECK_STOCK";

    /**
     * 开票状态
     */
    public enum EInvoiceStatus {
        /**
         * 等待打印
         */
        WAIT_MAKE("等待开票"),
        /**
         * 等待下载
         */
        WAIT_DOWNLOAD("等待出票"),
        /**
         * 开票成功
         */
        SUCCESS("已开票"),
        /**
         * 开票失败
         */
        FAILURE("开票失败"),
        /**
         * 月底最后一天延后标记
         */
        KP_DELAYED("延期开票"),
        /**
         * 延期冲红
         */
        CH_DELAYED("延期冲红"),
        /**
         * 订单取消
         */
        ORDER_CANCEL("订单取消"),
        /**
         * 发票冲红
         */
        RED("已冲红"),
        /**
         * 无须开票
         */
        DO_NOTHING("无须开票");

        EInvoiceStatus(String text) {
            this.text = text;
        }

        private String text;

        public String getText() {
            return this.text;
        }
    }
}
