<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>金立电商-统一用户中心</title>
<link href="${ctx}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/template/css/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/template/css/page_admin_main.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<table border="0" cellspacing="0" cellpadding="0" class="adminMain_top">
		<tbody>
			<tr>
				<td class="td01"></td>
				<td class="td02"><h3 class="topTitle fb f14">欢迎登录统一用户中心后台</h3></td>
				<td class="td03"></td>
			</tr>
		</tbody>
	</table>
	<table border="0" cellspacing="0" cellpadding="0" class="adminMain_main">
		<tbody>
			<tr>
				<td class="td01"></td>
				<td class="adminMain_wrap">
					<div class="wrapMain" style="height: 1000px; background-color: #F2F5FA; font-size: 24px; color: blue; text-align: center; vertical-align: middle;">
						<center> 
						  <img src="${ctx}/static/images/welcom.jpg" alt="欢迎登录统一用户中心后台" /> 
					    </center>
					</div>
				</td>
				<td class="td03"></td>
			</tr>
		</tbody>
	</table>

</body>
</html>
