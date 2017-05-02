package com.gionee.wms.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pengbin on 2017/4/19.
 */
public class QueryMap {

    private Map<String, Object> map = new HashMap<>();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
