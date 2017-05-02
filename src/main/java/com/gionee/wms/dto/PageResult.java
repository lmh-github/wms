package com.gionee.wms.dto;

import java.util.List;

/**
 * Created by Pengbin on 2017/3/14.
 */
public class PageResult<T> {

    private List<T> rows;
    private Integer total;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
