package com.gionee.wms.web.extend;

import com.gionee.wms.dto.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.regex.Pattern;

/**
 * 分页转换器
 * Created by Pengbin on 2017/4/28.
 */
public class PageArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterType() == Page.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Page page = new Page();
        String currentPage = StringUtils.trimToNull(webRequest.getParameter("page.currentPage"));
        String pageSize = StringUtils.trimToNull(webRequest.getParameter("page.pageSize"));

        if (currentPage != null && Pattern.matches("\\d+", currentPage)) {
            page.setCurrentPage(Integer.parseInt(currentPage));
        }

        if (pageSize != null && Pattern.matches("\\d+", pageSize)) {
            page.setPageSize(Integer.parseInt(pageSize));
        }

        return page;
    }
}
