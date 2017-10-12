package com.gionee.wms.service.stock;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.PageResult;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/3/13.
 */
public interface InvoiceInfoService {

    /**
     * 新增或者修改信息
     * @param invoiceInfo 主体信息
     * @param exclude     ture:非覆盖模式，false:覆盖模式
     * @return int
     */
    int saveOrUpdate(InvoiceInfo invoiceInfo, boolean exclude);

    /**
     * 根据订单信息新增
     * @param order SalesOrder
     * @return int
     */
    int saveByOrder(SalesOrder order);

    /**
     * 取消订单
     * @param idOrCode 订单号
     * @return int
     */
    ServiceCtrlMessage cancelOrder(String idOrCode);

    /**
     * 分页查询
     * @param paramMap 查询参数
     * @param page     分页参数
     * @return 分页数据
     */
    PageResult<InvoiceInfo> query(Map<String, Object> paramMap, Page page);

    /**
     * 外部查询用
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> extQuery(Map<String, Object> paramMap);

    /**
     * @param statusList
     * @return
     */
    List<String> query(List<String> statusList);

    /**
     * 订单号或者ID查询
     * @param idOrCode 订单号或者ID
     * @return InvoiceInfo
     */
    InvoiceInfo get(String idOrCode);

    /**
     * 生成一个开票流水号
     * @param orderCode
     * @return
     */
    String createKpLsh(String orderCode);

    /**
     * 生成一个冲红流水号
     * @param orderCode
     * @return
     */
    String createChLsh(String orderCode);

    /**
     * 是否存在记录
     * @param orderCode 订单号
     * @return true|false
     */
    boolean exist(String orderCode);

    /**
     * 标记已经完成纸质发票打印
     * @param idOrCode 订单号或者ID
     * @return true|false
     */
    boolean successZInvoice(String idOrCode);

    /**
     * 开票定时任务查询专用方法
     * @param orderStatus
     * @param invoiceStatus
     * @return
     */
    List<String> queryForJob(List<?> orderStatus, List<?> invoiceStatus);

    /**
     * 查询需要开票的订单
     * @return
     */
    List<String> queryToMakeInvoiceOrder();

    /**
     * 查询需要冲红的订单
     * @return
     */
    List<String> queryToCancelInvoiceOrder();

    /**
     * 扫描并更改不需要开票的订单
     * @return
     */
    int autoDoNothingInvoiceOrder();

    /**
     * 导出查询
     * @param paramMap
     * @return
     */
    List<Map<String, String>> exprotQuery(Map<String, Object> paramMap);

}
