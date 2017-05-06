/**
 * Project Name:wms
 * File Name:DockSFServlet.java
 * Package Name:com.gionee.wms.web.servlet
 * Date:2014年8月28日上午10:31:48
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.web.servlet;

import com.gionee.wms.common.JaxbUtil;
import com.gionee.wms.service.stock.DockSfService;
import com.sf.integration.warehouse.response.DockSFResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对接顺丰Servlet
 * @author PengBin 00001550<br>
 * @date 2014年8月28日 上午10:31:48
 */
public class DockSFServlet extends HttpServlet {

    /** serialVersionUID */
    private static final long serialVersionUID = -9070068505927381677L;
    private final Logger logger = LoggerFactory.getLogger(DockSFServlet.class);

    /** {@inheritDoc} */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<String> readLines = IOUtils.readLines(request.getInputStream(), "UTF-8");
            String requestBodyString = StringUtils.join(readLines, StringUtils.EMPTY);
            logger.info(MessageFormat.format("“出库单状态与明细推送接口”推送的内容为：{0}{1}", SystemUtils.LINE_SEPARATOR, requestBodyString));

            if (StringUtils.isEmpty(requestBodyString)) {
                logger.error("“出库单状态与明细推送接口”获取推送正文为空！");
                print(response, new DockSFResponse(false, "“出库单状态与明细推送接口”获取推送正文为空！"));
                return;
            }

            // 由于未知原因导致 request.getParameter("logistics_interface") 获取不到数据，故使用正则表达式获取请求内容
            String sailOrderPushInfoXML = getSailOrderPushInfoXML(requestBodyString);

            if (sailOrderPushInfoXML == null) {
                logger.error("“出库单状态与明细推送接口”获取logistics_interface 内容为空！");
                print(response, new DockSFResponse(false, "“出库单状态与明细推送接口”获取logistics_interface 内容为空！"));
                return;
            }
            logger.info(MessageFormat.format("“出库单状态与明细推送接口”推送的内容为：{0}{1}", SystemUtils.LINE_SEPARATOR, sailOrderPushInfoXML));

            DockSFResponse dockSFResponse = getApplicationContext(request).getBean(DockSfService.class).dock(sailOrderPushInfoXML);
            print(response, dockSFResponse);
        } catch (Exception e) {
            logger.error("“出库单状态与明细推送接口”推送接口被调用出现异常！", e);
            e.printStackTrace();
            print(response, new DockSFResponse(false, "请求的内容出现异常！"));
        }

    }

    /**
     * 返回调用端消息
     * @param response
     * @param msg
     */
    private void print(HttpServletResponse response, DockSFResponse msg) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");

            PrintWriter out = response.getWriter();
            out.println(JaxbUtil.safeMarshToXmlBinding(DockSFResponse.class, msg));

            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("“出库单状态与明细推送接口”向调用端返回数据时异常！", e);
            e.printStackTrace();
        }
    }

    /**
     * 获取Spring的ApplicationContext
     * @param request
     * @return
     */
    private ApplicationContext getApplicationContext(HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        return applicationContext;
    }

    /**
     * 获取wmsSailOrderPushInfo内容
     * @param requestBodyString
     * @return
     */
    private String getSailOrderPushInfoXML(String requestBodyString) {
        try {
            Pattern pattern = Pattern.compile("logistics_interface=([\\s\\S]*?)&");
            Matcher matcher = pattern.matcher(requestBodyString);
            if (matcher.find()) {
                String g1 = matcher.group(1);
                String xml = URLDecoder.decode(g1, "UTF-8");
                return xml;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
