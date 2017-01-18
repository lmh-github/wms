<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>403 - 用户权限不足</title>
</head>

<body>
	<h2 align="center">403 - 用户权限不足.</h2>
	<p align="center"><a href="<c:url value="/account/login!index.action"/>" target="_top">返回首页</a></p>
</body>
</html>
