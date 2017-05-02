package com.gionee.wms.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 链表式操作Map
 * Created by Pengbin on 2017/4/26.
 */
public class LinkMapUtils {

    private LinkMapUtils() {
        // do nothing
    }

    /**
     * 创建链式HashMap
     * @param <K> K
     * @param <V> V
     * @return LinkMapUtils
     */
    public static <K, V> LinkMap<K, V> newHashMap() {
        return new LinkMapUtils().new LinkMap(new HashMap<K, V>());
    }

    /**
     * 创建链式HashMap
     * @param sourceMap sourceMap
     * @param <K>       K
     * @param <V>       V
     * @return LinkMapUtils
     */
    public static <K, V> LinkMap<K, V> newHashMap(Map<K, V> sourceMap) {
        return new LinkMapUtils().new LinkMap(sourceMap);
    }


    /**
     * 连式操作Map
     * @param <K> K
     * @param <V> V
     */
    public final class LinkMap<K, V> {

        private Map<K, V> map;

        private LinkMap(Map<K, V> sourceMap) {
            this.map = sourceMap;
        }

        public LinkMap<K, V> put(K k, V v) {
            this.map.put(k, v);
            return this;
        }

        public Map<K, V> getMap() {
            return this.map;
        }
    }
}
