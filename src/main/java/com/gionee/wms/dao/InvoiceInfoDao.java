package com.gionee.wms.dao;

import com.gionee.wms.entity.InvoiceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/3/13.
 */
@BatisDao
public interface InvoiceInfoDao {

    /**
     * 插入
     * @param invoiceInfo
     * @return
     */
    int insert(InvoiceInfo invoiceInfo);

    /**
     * 全量覆盖需该
     * @param invoiceInfo
     * @return
     */
    int update(InvoiceInfo invoiceInfo);

    /**
     * 排除null值修改
     * @param invoiceInfo
     * @return
     */
    int updateExcludeNull(InvoiceInfo invoiceInfo);

    /**
     * 检查是否存在
     * @param orderCode
     * @return
     */
    boolean exist(String orderCode);

    /**
     * 查询
     * @param params
     * @return
     */
    List<InvoiceInfo> query(Map<String, Object> params);

    /**
     * 根据状态查询
     * @param list
     * @return
     */
    List<String> queryOrderCodesByStatus(@Param("list") List<String> list);

    /**
     * 查询总记录数
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);

    /**
     * ID或订单号查询
     * @param idOrCode
     * @return
     */
    InvoiceInfo get(String idOrCode);

    /**
     * 外部接口查询
     * @param params
     * @return
     */
    List<Map<String, Object>> extQuery(Map<String, Object> params);

    /**
     * 导出查询
     * @param params
     * @return
     */
    List<Map<String, String>> exprotQuery(Map<String, Object> params);

    /**
     * 定时任务接口查询
     * @param orderStatus
     * @param invoiceStatus
     * @return
     */
    List<String> queryForJob(@Param("orderStatus") List<?> orderStatus, @Param("invoiceStatus") List<?> invoiceStatus);

}
