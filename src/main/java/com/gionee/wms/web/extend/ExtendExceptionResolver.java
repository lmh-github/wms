package com.gionee.wms.web.extend;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pengbin on 2017/4/19.
 */
public class ExtendExceptionResolver extends SimpleMappingExceptionResolver {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        ModelAndView mv = new ModelAndView(viewName);
        String errorCode = ("" + Math.random()).substring(2, 10);
        mv.addObject("errorCode", errorCode);
        mv.addObject("success", false);
        mv.addObject("message", ex.getMessage());
        System.out.println("[" + dateFormat.format(new Date()) + "] ERROR_CODE: " + errorCode + " --> " + ex.getMessage());
        ex.printStackTrace();
        return mv;
    }
}
