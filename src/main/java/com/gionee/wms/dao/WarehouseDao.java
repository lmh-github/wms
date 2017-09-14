package com.gionee.wms.dao;

import com.gionee.wms.entity.Warehouse;

import java.util.List;
import java.util.Map;

@BatisDao
public interface WarehouseDao {
    /**
     * 获取仓库信息列表.
     */
    List<Warehouse> queryWarehouseList(Map<String, Object> criteria);

    /**
     * 添加仓库
     */
    int addWarehouse(Warehouse warehouse);

    /**
     * 更新仓库
     */
    int updateWarehouse(Warehouse warehouse);

    /**
     * 更新所有仓库为非默认状态
     */
    int updateAllWarehouseToUndefault();

    /**
     * @param warehouseCode
     * @return
     */
    String getWarehouseNameByCode(String warehouseCode);

    /**
     * 根据仓库名称获取仓库id
     * @param warehouseName
     * @return
     */
    String getWarehouseIdByName(String warehouseName);
}
