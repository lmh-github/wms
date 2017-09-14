package com.routdata.zzfw.webservice.service;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 * Created by gionee on 2017/9/14.
 */
public class OrderCodeUtils {
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);
    private static final long MAX = 999;
    private static long seq = 0;


    static String generate() {
        synchronized (OrderCodeUtils.class) {
            StringBuffer sb = new StringBuffer("");
            long datetime = System.currentTimeMillis();
            sb.append(datetime);
            NumberFormat nf = new DecimalFormat("000");
            nf.format(seq, sb, HELPER_POSITION);
            if (seq == MAX) {
                seq = 0;
            } else {
                seq++;
            }
            return sb.toString();
        }
    }

    public static String generateRadix() {
        return Long.toString(Long.parseLong(generate()), Character.MAX_RADIX);
    }

}
