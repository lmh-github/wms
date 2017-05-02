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

    int insert(InvoiceInfo invoiceInfo);

    int update(InvoiceInfo invoiceInfo);

    int updateExcludeNull(InvoiceInfo invoiceInfo);

    boolean exist(String orderCode);

    List<InvoiceInfo> query(Map<String, Object> params);

    List<String> queryOrderCodesByStatus(@Param("list") List<String> list);

    int queryCount(Map<String, Object> params);

    InvoiceInfo get(String idOrCode);

    List<Map<String, Object>> extQuery(Map<String, Object> params);

    List<String> queryForJob(@Param("orderStatus") List<?> orderStatus, @Param("invoiceStatus") List<?> invoiceStatus);

}
