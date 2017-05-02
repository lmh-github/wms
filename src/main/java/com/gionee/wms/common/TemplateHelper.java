package com.gionee.wms.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TemplateHelper {

    private static Configuration conf = new Configuration();
    private static Map<String, Template> templateCache = new HashMap<String, Template>();

    static {
        conf.setEncoding(Locale.getDefault(), "UTF-8");
        conf.setDefaultEncoding("UTF-8");
        conf.setNumberFormat("#.##");
        conf.setBooleanFormat("true,false");
    }

    public static String generate(Map<String, Object> model, String templateName) {
        StringWriter w = new StringWriter();
        Template template = null;
        try {
            if (templateCache.containsKey(templateName)) {
                template = templateCache.get(templateName);
            } else {
                InputStream inputStream = TemplateHelper.class.getResourceAsStream("/ftl/" + templateName);
                String templateStr = null;
                try {
                    templateStr = IOUtils.toString(inputStream);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    template = new Template(templateName, new StringReader(templateStr), conf);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                templateCache.put(templateName, template);
            }
            template.process(model, w);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return w.toString();
    }

}
