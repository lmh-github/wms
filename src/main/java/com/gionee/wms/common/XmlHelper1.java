package com.gionee.wms.common;

import com.gionee.wms.dto.KpInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * 将XML转对象/对象转XML
 * @author DT
 */
public class XmlHelper1 {

    /**
     * xml转对象
     * @param xmlStr
     * @param cls
     * @return
     */
    public static <T> T toBean(String xmlStr, Class<T> cls) throws Exception {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        xstream.ignoreUnknownElements();
        @SuppressWarnings("unchecked") T t = (T) xstream.fromXML(xmlStr);
        return t;
    }

    /**
     * 实体类转XML
     * @param obj
     * @return
     */
    public static String toXml(Object obj) throws Exception {
        XStream xstream = new XStream(new DomDriver("utf8"));
        xstream.processAnnotations(obj.getClass()); // 识别obj类中的注解
        // 以格式化的方式输出XML
        return xstream.toXML(obj);
    }

    public static void main(String[] args) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            String xml = TemplateHelper.generate(model, "request_open_key.xml");
            System.out.println(xml);
            KpInterface interfaceXml = toBean(xml, KpInterface.class);
            System.out.println(interfaceXml.getData().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
