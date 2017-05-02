package com.gionee.wms.web.extend;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

/**
 * Created by Pengbin on 2017/4/19.
 */
public class ExtendObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    public ExtendObjectMapper(String dataFormat) {
        setDateFormat(new SimpleDateFormat(dataFormat));
    }

    public ExtendObjectMapper() {
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }
}
