package com.gionee.wms.web.extend;

import com.gionee.wms.dto.QueryMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Pengbin on 2017/4/19.
 */
public class QueryMapArgumentResolver implements HandlerMethodArgumentResolver {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** {@inheritDoc} */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterType() == QueryMap.class);
    }

    /** {@inheritDoc} */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        QueryMap ret = new QueryMap();
        Map<String, String[]> map = webRequest.getParameterMap();
        for (String key : map.keySet()) {
            String value = StringUtils.trimToNull(map.get(key)[0]);
            if (value != null) {
                if (key.endsWith("_split")) {
                    String newKey = key.substring(0, key.length() - 6).concat("_list");
                    if (value == null) {
                        ret.put(newKey, value);
                        continue;
                    }
                    String[] splitValues = StringUtils.stripAll(value.split(";"));
                    ArrayList<String> list = new ArrayList<>(splitValues.length);
                    for (String s : splitValues) {
                        if (s.length() == 0) {
                            continue;
                        }

                        list.add(s);
                    }

                    ret.put(newKey, list);
                } else if (key.equals("id")) { // 默认ID字段为 Long 类型
                    if (Pattern.matches("\\d+", value)) {
                        ret.put(key, Long.valueOf(value));
                    } else {
                        ret.put(key, value);
                    }
                } else if (key.endsWith("_i")) {
                    ret.put(key.substring(0, key.length() - 2), Integer.parseInt(value));
                } else if (key.endsWith("_d")) {
                    ret.put(key.substring(0, key.length() - 2), Double.parseDouble(value));
                } else if (key.endsWith("_f")) {
                    ret.put(key.substring(0, key.length() - 2), Float.parseFloat(value));
                } else if (key.endsWith("l")) {
                    ret.put(key.substring(0, key.length() - 2), Long.parseLong(value));
                } else if (key.endsWith("_d")) {
                    ret.put(key.substring(0, key.length() - 2), dateFormat.parse(value));
                } else if (key.endsWith("_t")) {
                    ret.put(key.substring(0, key.length() - 2), timeFormat.parse(value));
                } else {
                    ret.put(key, value);
                }
            }
        }
        return ret;
    }
}