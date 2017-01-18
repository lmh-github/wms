<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>电商仓管系统</title>

<link href="${ctx}/static/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
<!--[if IE]>
<link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--[if lte IE 9]>
<script src="js/speedup.js" type="text/javascript"></script>
<![endif]-->

<script src="${ctx}/static/js/jquery-1.8.3.min.js" type="text/javascript"></script>

<script type="text/javascript">


$(function initAudio() {
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	if (window.ActiveXObject)
		Sys.ie = ua.match(/msie ([\d.]+)/)[1];
	else if (document.getBoxObjectFor)
		Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];
	else if (window.MessageEvent && !document.getBoxObjectFor)
		Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];
	else if (window.opera)
		Sys.opera = ua.match(/opera.([\d.]+)/)[1];
	else if (window.openDatabase)
		Sys.safari = ua.match(/version\/([\d.]+)/)[1];
	if (Sys.chrome) {
		$("#audioArea")
				.html(
						'<audio controls="controls" id="audio_error"><source src="${ctx}/static/audio/audio_error.wav"></audio>');
	} else {
		$("#audioArea")
				.html(
						'<EMBED id="audio_error" src="${ctx}/static/audio/audio_error.wav" align="center" border="0" width="1" height="1" autostart="false" loop="false"/>');
	}
});


function soundError() {
		document.getElementById("audio_error").play();
}

function toDo(action){
	//获取提交参数
	var warehouseId = $('#warehouseId').val();
	var batchCode = $('#batchCode').val();
	if(warehouseId == null || warehouseId == ''){
		$("#scanTips").html('请选择仓库');
		return;
	}
	if(batchCode == null || batchCode == ''){
		$("#scanTips").html('请输入批次号');
		return;
	}
	//打开运单扫描页面
	window.location.href='${ctx}/stock/delivery!toScan.action?warehouseId='+warehouseId+'&batchCode='+batchCode;
}
</script>
</head>

<body scroll="no">
	<div style="width: 100%; height: 100%; margin-top: 60px">
		<span id="scanTips" style="color: red;"></span>
		<form>
		仓库：<s:select id="warehouseId" name="warehouseId" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/><br />
		<span class="info">*如不选择仓库按默认仓库出货</span><br />
		批次号：<input type="text" id="batchCode" name="batchCode" value="" style="width: 100px;"/>
		<br/>
		<button type="button" onclick="toDo()">扫描运单或发货</button>
		</form>
	</div>
</body>
</html>