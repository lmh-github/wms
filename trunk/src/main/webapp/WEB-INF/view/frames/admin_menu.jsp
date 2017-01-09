<%@page import="com.gionee.wms.dto.Menu"%>
<%@page import="java.util.List"%>
<%@ page pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="${ctx}/template/javascript/jquery-huadong.js"></script> 
<script type="text/javascript"> 
$(document).ready(function(){
	
	$(".list").hide();
 
	$("h3.trigger").toggle(function(){
		$(this).addClass("sole"); 
		}, function () {
		$(this).removeClass("sole");
	});
	
	$("h3.trigger").click(function(){
		$(this).next(".list").slideToggle("slow,");
	});
 
});
</script> 
 
</head> 
<body> 
<div class="adminLeft">  

<!-- 菜单的html结构 -->
<!-- 
<h3 class="trigger">菜单组名称</h3> 
<ul class="list"> 
		   <li> <a href="searchInventory.action" target="mainFrame">库存查询<span class="nav_baright"></span></a></li> 
		   <li> <a href="findInventoryLog.action" target="mainFrame">库存变动日志<span class="nav_baright"></span></a></li> 
		   <li> <a href="findCheckLog.action" target="mainFrame">库存盘点日志<span class="nav_baright"></span></a></li> 
		   <li> <a href="findCheckInventory.action" target="mainFrame">库存盘点异动日志<span class="nav_baright"></span></a></li> 
		   <li> <a href="batchManualInventory.action" target="mainFrame">批量修改手工库存<span class="nav_baright"></span></a></li> 
		   <li> <a href="searchCheckInventoryHistory.action" target="mainFrame">盘点历史明细查询<span class="nav_baright"></span></a></li> 
</ul> 
 -->
<%
	/* 
	root 是菜单树，但是根据显示的实际情况，只输出两级，所以配置菜单的时候需要注意。
	生成的菜单 参照以上 菜单的html结构
	by menglei
*/
Menu root = (Menu) request.getAttribute("menu");
List<Menu> menuGroups = root.getChildMenus();
for (Menu menu : menuGroups) {
	out.println("<h3 class=\"trigger\">" + menu.getName()+ "</h3> ");
	out.println("<ul class=\"list\">");
	List<Menu> childMenus = menu.getChildMenus();
	for (Menu childMenu : childMenus) {
		out.println("<li>");
		out.println("<a href=\"" + request.getContextPath()+ childMenu.getUrl()+ "\" target=\"mainFrame\">"
				+ childMenu.getName()+ "<span class=\"nav_baright\"></span></a>");
		out.println("</li>");
	}
	out.println("</ul>");
}
%>

<!--adminLeft--></div> 
</body> 
</html> 