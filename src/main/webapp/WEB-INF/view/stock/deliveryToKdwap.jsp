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

$(function initTab() {
	$("#shippingNo1").focus();
});

// 防止回车提交表单
$("#deliveryForm").keydown(function(e){
  var e = e || event;
  var keyNum = e.keyCode || e.which || e.charCode;
  return keyNum==13 ? false : true;
});

//监听输入框回车 
function doKeydown(event, obj) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		scanKuaidi(obj.value);
	   	$(obj).val("");//清空输入框
		$(obj).focus();//聚焦下个输入框
	}
}

function scanKuaidi(value) {
	scanTips(value);
	var batchCode = $("#batchCode").val();
	var warehouseId = $("#warehouseId").val();
	$.ajax({
	   url: "${ctx}/stock/delivery!checkKuaidi.action?shippingNo="+value+"&flag=pda"+"&batchCode="+batchCode+"&warehouseId="+warehouseId,
	   success: function(data){
		   	if(data.ok==true) {
		   		$("#quantity").val(data.result);
		   	} else {
		   		scanTips(data.message);
		   		return false;
		   	}
	   },
	   error: function (XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);     
		}
	});
}

function scanTips(message) {
	$("#scanTips").html(message);
}

function confirmDelivery(){
	$.ajax({
	   type: "POST",
	   url: "${ctx}/stock/delivery!confirmDeliveryWap.action",
	   data: $('#deliveryForm').serialize(),
	   async: false,
	   success: function(data){
		   	if(data.ok==true) {
		   		//跳转到发货PDA首页
		   		window.location.href='${ctx}/stock/delivery!dispWap.action';
		   	} else {
		   		$("#scanTips").html(data.message);
		   	}
	   }
	});
}

function returnHome() {
	window.location.href='${ctx}/stock/delivery!dispWap.action';
}
</script>
</head>

<body scroll="no">
	<div style="width: 100%; height: 100%; margin-top: 60px;">
		<span id="scanTips" style="color: red;"></span>
		<form id="deliveryForm">
			<table style="width: 100%;">
				<tr>
					<td align="right">出库批次：</td>
					<td align="left"><input id="batchCode" name="batchCode" value="${batchCode}" readonly="true" style="width: 100px;"/></td>
				</tr>
				<tr>
					<td align="right">已扫描：</td>
					<td align="left"><input id="quantity" name="quantity" value="${quantity}" readonly="true" style="width: 100px;"/></td>
				</tr>
				<tr>
					<td align="right">包裹单号：</td>
					<td align="left"><input id="shippingNo1" name="shippingNo" value="" onkeydown="doKeydown(event, this)" style="width: 100px;"/></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><button type="button" onclick="confirmDelivery()">确认发货</button></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><button type="button" onclick="returnHome()">返回发货首页</button></td>
				</tr>
			</table>
			<input id="orderIds" name="orderIds" type="hidden" value="${orderIds}" />
			<input id="warehouseId" name="warehouseId" type="hidden" value="${warehouseId}">
		</form>
	</div>
</body>
</html>

