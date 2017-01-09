<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>金立电商-统一用户中心后台</title> 
<link href="${ctx}/template/css/base.css" rel="stylesheet" type="text/css" /> 
<script type="text/javascript" src="${ctx}/template/javascript/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="${ctx}/template/javascript/tab.js"></script>
<script type="text/javascript" src="${ctx}/template/javascript/weebox.js"></script>
<script type="text/javascript" src="${ctx}/template/javascript/bgiframe.js"></script>
<script type="text/javascript" src="${ctx}/template/javascript/calendar/My97DatePicker/WdatePicker.js"></script>
</head> 
  <frameset rows="58,*" cols="*" frameborder="no" border="0" framespacing="0"> 
	  <frame src="${ctx}/account/adminTop.action" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" title="topFrame" /> 
	  <frameset cols="180,*" frameborder="no" border="0" framespacing="0">
	    	<frameset framespacing="0" border="0" frameborder="no" rows="40,*" id="leftFrame"> 
	  			<frame src="${ctx}/account/adminLeftTop.action" scrolling="no" title="left_topFrame" id="left_topFrame" noresize="noresize" name="left_topFrame" />
				<frame src="${ctx}/account/getMenus.action" scrolling="yes" title="left_botFrame" id="left_botFrame" name="left_botFrame" />
			</frameset>
	        <frameset cols="10,*" frameborder="no" border="0" framespacing="0"> 
	            <frame src="${ctx}/account/adminCenter.action" name="centerFrame" scrolling="no" id="centerFrame" title="centerFrame" /> 
	            <frameset rows="*,26" cols="*" frameborder="no" border="0" framespacing="0"> 
	                <frame src="${ctx}/account//adminMain.action" name="mainFrame" id="mainFrame" title="mainFrame" /> 
	                <frame src="${ctx}/account/adminBottom.action" name="bottomFrame" scrolling="no" id="bottomFrame" title="bottomFrame" /> 
	            </frameset> 
	        </frameset> 
	  </frameset> 
 </frameset> 
 <noframes>
   <body></body>
 </noframes> 
</html>