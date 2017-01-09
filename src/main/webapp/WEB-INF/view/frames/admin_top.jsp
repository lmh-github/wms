<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta http-equiv="expires" content="0" />

<title>金立电商</title>
<link href="${ctx}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/template/css/page_admin.css" rel="stylesheet" type="text/css" />

</head>

<body>
<div class="adminTop">
<div class="adminTop_left"><a href="#"><img src="${ctx}/template/img/admin_logo.gif" alt="金立电商-统一用户中心后台" /></a></div>
<div class="adminTop_right">
<p class="rTop">
	<span><%=org.apache.commons.lang.time.DateFormatUtils.format(new java.util.Date(), "yyyy-MM-dd") %></span>
    <span>欢迎您：<strong class="fma f14 fb"><shiro:principal property="loginName"/></strong></span>
    <span><a href="${ctx}/account/admin.action" target="_top">平台首页</a></span>
    <span><a href="${ctx}/account/logout.action" target="_top">退出登录</a></span>
</p>

<!--adminTop_right--></div>
<!--adminTop--></div>
</body>
</html>
