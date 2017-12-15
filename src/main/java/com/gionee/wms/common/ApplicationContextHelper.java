package com.gionee.wms.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring ApplicationContext对象
 * @author PB
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> t) {
        return applicationContext.getBean(t);
    }

    /**
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getAliases(String name) {
        return (T) applicationContext.getAliases(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextHelper.applicationContext == null) {
            ApplicationContextHelper.applicationContext = applicationContext;
        }
    }

}
