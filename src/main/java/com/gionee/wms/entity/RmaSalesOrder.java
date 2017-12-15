package com.gionee.wms.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * 退换货相关
 * @author
 * @date
 */
@Data
public class RmaSalesOrder {
    /** 订单号 */
    private String orderCode;
    /** 仓库ID */
    private Long warehouseId;
    /** 备注 */
    private String remark;
    /** 良品IMEI */
    private List<String> goodImeis;
    /** 次品IMEI */
    private List<String> badImeis;
    /** 退货商品 */
    private List<RmaSalesOrderGoods> goodsList;

    /**
     * 退货商品
     */
    @Data
    public static class RmaSalesOrderGoods {
        private String skuCode;
        private Integer goodQuantity;
        private Integer badQuantity;
    }
}
