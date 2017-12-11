package com.gionee.wms.common;

import com.gionee.wms.dto.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public class ActionUtils {
	// -- Content Type 定义 --//
	private static final String TEXT_TYPE = "text/plain";
	private static final String JSON_TYPE = "application/json";

	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	/**
	 * 判断是否为json格式请求
	 *
	 * @return
	 */
	public static boolean isJsonRequest() {
		boolean isJsonRequest = false;
		if (StringUtils.isBlank(getRequest().getContentType())) {
			isJsonRequest = false;
		} else if (getRequest().getContentType().startsWith("text/plain")
				|| getRequest().getContentType().startsWith("text/html")
				|| getRequest().getContentType().startsWith("application/json")) {
			isJsonRequest = true;
		}
		return isJsonRequest;
	}

	/**
	 * 取project绝对路径
	 */
	public static String getProjectPath() {
		return ServletActionContext.getServletContext().getRealPath("/");
	}

    /**
     * 取类绝对路径
     */
    public static String getClassPath() {
        String classPath = ActionUtils.class.getClassLoader().getResource("").getPath();
        // 对 Windows下的物理路径做特殊处理
        if ("\\".equals(File.separator)) {
            classPath = classPath.substring(1);// .replaceAll("/", "\\\\");
        }
        return classPath;
    }

    /**
     * 登录用户
     * @return
     */
    public static ShiroUser getCurrentUser() {
        try {
            if (SecurityUtils.getSubject() != null) {
                ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
                return user;
            }
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public static String getLoginName() {
        ShiroUser user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getLoginName();
    }

    /**
     * 获取当前登录的用户名
     * @return
     */
    public static String getLoginNameAndDefault() {
        return StringUtils.defaultString(getLoginName(), WmsConstants.DEFAULT_USERNAME_LOG);
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 输出json类型到客户端
     */
    public static void outputJson(final String content) {
        output(JSON_TYPE, WmsConstants.DEFAULT_ENCODING, content);
    }

    /**
     * 输出text类型到客户端
     */
    public static void outputText(final String content) {
        output(TEXT_TYPE, WmsConstants.DEFAULT_ENCODING, content);
    }

    private static void output(final String contentType, final String encoding, final String content) {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType(contentType + ";charset=" + encoding);
        try {
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取请求上下文中的真实IP地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        if (request == null) {
            request = ServletActionContext.getRequest();
        }

        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP
            int index = ip.indexOf(',');
            if (index > -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("X-Cluster-Client-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

}
