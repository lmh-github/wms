package com.gionee.wms.entity;

/**
 * Created by Pengbin on 2017/5/3.
 */
public class DbProperty {
    private String key;
    private String value;
    private Integer revision = 0;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getNextRevision() {
        return this.revision + 1;
    }
}
