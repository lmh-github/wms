<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="pagerForm" action="${ctx}/stock/transPrepareSf!confirm.action?callbackType=forward&forwardUrl=${ctx}/stock/transPrepareSf.action&navTabId=tab_transPrepare" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
<div class="pageHeader">
	<div class="pageFormContent nowrap">
		<fieldset>
			<legend>调拨单信息</legend>
			<dl>
				<dt>调拨仓：</dt>
				<dd>${transfer.transferTo }</dd>
			</dl>
		</fieldset>
		<dl>
			<dt>调货单:</dt>
			<dd><input name="transferId" type="text" class="textInput digits required" value="${transferId }" readonly="readonly"/></dd>
		</dl>
		<dl id="scanIndiv">
			<dt>扫描个体编码/sku/箱号：</dt>
			<dd><input id="indivCode2" name="indivCode" type="text" class="textInput" onKeydown="doKeydown(event, this)"/><span id="scanCode" style="color: red; font-size: 12pt; float: left;"></span></dd>
		</dl>
		
		<div id="scanShipping" style="display: none;">
			<dl>
				<dt>扫描物流运单：</dt>
				<dd><input id="logisticNo" name="logisticNo" type="text" class="required textInput" maxlength="20" size="30" onkeydown="toSubmit(event, this)"/></dd>
			</dl>
		</div>
		
		<div class="subBar">
			<span id="errorTips" style="color: red; font-size: 12pt; float: left;"></span>
		</div>
	</div>
</div>
<div class="pageContent" id="goodsBox" style="overflow: auto;">
</div>
	<div class="pageContent" id="indivBox" style="overflow: auto;">
		<h2 class="contentTitle" align="center">已扫描列表</h2>
		<table class="list" width="100%" height="100%">
			<thead>
				<tr>
					<th>个体编码</th>
					<th>sku编号</th>
					<th>库存状态</th>
				</tr>
			</thead>
			<tbody id="indivScan">
				
			</tbody>
		</table>
	</div>
</form>
<script type="text/javascript">
<!--
var $pagerForm = $("#pagerForm", navTab.getCurrentPanel());
//防止回车提交表单
$pagerForm.keydown(function(e){
	var e = e || event;
	  var keyNum = e.keyCode || e.which || e.charCode;
	  return keyNum==13 ? false : true;
});

//监听输入框回车 
function doKeydown(event, obj) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		prepareIndiv(obj.value);
	}
}

//扫描内容提示
function scanCode(message) {
	$("#scanCode", navTab.getCurrentPanel()).html(message);
	$("#errorTips", navTab.getCurrentPanel()).html("");
}

//错误提示
function errorTips(message) {
	$("#errorTips", navTab.getCurrentPanel()).html(message);
	soundError();
}

//商品配货
// 配货
function prepareIndiv(value) {
	scanCode(value);
	var codeInput = $("#indivCode2", navTab.getCurrentPanel());
	codeInput.attr("readonly", "readonly");
	codeInput.attr("class", "textInput readonly");
	$.ajax({
	   url: "${ctx}/stock/transPrepareSf!prepareIndiv.action",
	   data:$pagerForm.serialize(),
	   success: function(data){
		   	if(data.ok==true) {
		   		var result = data.result;
		   		errorTips(data.message);
		   		$("#goodsBox").loadUrl("${ctx}/stock/transPrepareSf!loadGoods.action",$pagerForm.serialize(),confirmFinish);
		   		for(var i=0;i<result.length;i++){
		   			var indiv = result[i];
		   			var skuCode = indiv.skuCode;
		   			var indivCode = indiv.indivCode;
		   			if(indivCode!=null&&indivCode!=""){
			   			$("#indivScan").append("<tr><td>"+indivCode+"</td><td>"+skuCode+"</td><td>出库中</td></tr>");		   				
		   			}
		   		}
		   	} else {
		   		errorTips(data.message);
		   		soundError();
		   		alert(data.message);
		   		return false;
		   	}
	   },
	   error: function (XMLHttpRequest, textStatus, errorThrown) {     
			alert(errorThrown);     
		}
	});
	codeInput.removeAttr("readonly");
	codeInput.attr("class", "textInput");
	codeInput.val("");//清空输入框
	codeInput.focus();//聚焦输入框
}
/*
function addIndiv(code, transid) {
	$.ajax({
	   url: "${ctx}/stock/transPrepareSf!addIndiv.action" ,
	   data: "indivCode=" + code + "&transferId=" + transid,
	   success: function(data){
		   	if(data.ok==true) {
		   		// 更新商品个体列表
		   		$("#goodsBox").loadUrl("${ctx}/stock/transPrepareSf!loadGoods.action","transferId=" + transid,confirmFinish);
		   	} else {
		   		errorTips(data.message);
		   		return false;
		   	}
	   },
	   error: function (XMLHttpRequest, textStatus, errorThrown) {     
			alert(errorThrown);
		}
	});
}
*/
function confirmFinish() {
	var finished = $("#finished", navTab.getCurrentPanel());
	if(finished.val() == 'true') {
		$("#scanIndiv", navTab.getCurrentPanel()).find("dd").html("<span style=\"color:green;\">扫描已完成</span>");
		$("#scanShipping", navTab.getCurrentPanel()).show();
		$("#logisticNo", navTab.getCurrentPanel()).focus();
		return true;
	}
}

//提交物流单号 
function toSubmit(event, obj) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		$(obj).closest("form").submit();
	}
}

function batchPrepare(goodsId, quantity) {
	$.ajax({
		url:"${ctx}/stock/transPrepareSf!batchPrepare.action",
		data:"goodsId="+goodsId+"&preparedNum="+quantity,
		success: function(data){
		   	if(data.ok==true) {
		   		// 更新商品个体列表
		   		$("#goodsBox").loadUrl("${ctx}/stock/transPrepareSf!loadGoods.action",$pagerForm.serialize(),confirmFinish);
		   	} else {
		   		errorTips(data.message);
		   		return false;
		   	}
		},
		  error: function (XMLHttpRequest, textStatus, errorThrown) {     
			alert(errorThrown);
		}
	});
}

$(function(){
	// 页面初始化加载表单
	$("#goodsBox").loadUrl("${ctx}/stock/transPrepareSf!loadGoods.action",$pagerForm.serialize(),confirmFinish);
	setTimeout(function() {
		$("#indivCode2", navTab.getCurrentPanel()).focus();	
	},1000);
});
//-->
</script>
