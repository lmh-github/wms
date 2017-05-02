package com.gionee.wms.web.extend;

import freemarker.template.SimpleHash;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Pengbin on 2017/4/19.
 */
public class ExtendFreeMarkerView extends FreeMarkerView {

    private static final String CONTEXT_RANDOM_VALUE = "rand";
    private static final String CONTEXT_BASE_PATH = "base";

    @Override
    protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        SimpleHash fmModel = super.buildTemplateModel(model, request, response);
        String path = request.getContextPath();
        String basePath;
        if (request.getServerPort() == 80) {
            basePath = request.getScheme() + "://" + request.getServerName() + path + "/";
        } else {
            basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        }
        fmModel.put(CONTEXT_BASE_PATH, basePath);
        fmModel.put(CONTEXT_RANDOM_VALUE, ("" + Math.random()).substring(2, 8));
        return fmModel;
    }

    @Override
    /**
     * 用于配置文件的读取
     */ protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
        try {
            FreeMarkerConfig config = BeanFactoryUtils.beanOfTypeIncludingAncestors(getApplicationContext(), FreeMarkerConfig.class, true, false);
            return config;
        } catch (NoSuchBeanDefinitionException ex) {
            throw new ApplicationContextException("Must define a single FreeMarkerConfig bean in this web application context " + "(may be inherited): FreeMarkerConfigurer is the usual implementation. " + "This bean may be given any name.", ex);
        }
    }
}
