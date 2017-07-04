package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pengbin on 2017/5/25.
 */
public class JaxbDateAdapter extends XmlAdapter<String, Date> {
    @Override
    public Date unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public String marshal(Date v) throws Exception {
        if (v == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
        } catch (Exception e) {
            return null;
        }
    }
}
