package com.gionee.wms.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Pengbin on 2017/3/23.
 */
public class BaseConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String path = sce.getServletContext().getRealPath("/");
        System.setProperty("WEBCONTENT.BASE.PASH", path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // do nothing
    }
}
