package com.gionee.wms.web.extend;

import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pengbin on 2017/4/19.
 */
public class StringToDate implements Converter<String, Date> {

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String source) {
        if (source == null || "".equals(source)) {
            return null;
        }
        try {
            if (source.contains(":")) {
                return TIMEFORMAT.parse(source);
            } else {
                return DATEFORMAT.parse(source);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
