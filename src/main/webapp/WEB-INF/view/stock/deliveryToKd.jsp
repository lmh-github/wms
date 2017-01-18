<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<form id="deliveryForm1" onsubmit="return validateCallback(this, navTabAjaxDone)" action="${ctx}/stock/delivery!confirmDelivery.action?callbackType=forward&forwardUrl=${ctx}/stock/delivery.action" method="post" class="pageForm required-validate">
		<input id="warehouseId" name="warehouseId" type="hidden" value="${warehouseId }">
		<div class="pageFormContent nowrap">
			<dl>
				<dt>出库批次：</dt>
				<dd><input id="batchCode" class="textInput" name="batchCode" value="${batchCode }" readonly="true"></dd>
			</dl>
			<dl>
				<dt>已扫描：</dt>
				<dd><input id="quantity" class="textInput" name="quantity" value="${quantity}" readonly="true"></dd>
			</dl>
			<dl>
				<dt>包裹单号：</dt>
				<dd><input type="text" id="shippingNo1" name="shippingNo" onkeydown="doKeydown(event, this)" style="width: 100px;"/>
				<span id="scanTips" style="color: red;"></span></dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">核对数量，提交出库</button></div></div></li>
			</ul>
		</div>
	</form>
</div>
<div class="pageContent" id="deliveryBox">
	<div class="panelBar">
		<span>共${quantity}条</span>
	</div>
	<table class="list" width="100%" layoutH="90">
		<thead>
			<tr align="center">
				<th>批次号</th>
				<th>订单号</th>
				<th>快递号</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="deliveryList" >
			<tr align="center">
				<td>${batchCode }</td>
				<td>${originalCode }</td>
				<td>${shippingNo }</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</div>

<script type="text/javascript">
$(function initTab() {
	$("#shippingNo1", navTab.getCurrentPanel()).focus();
});

// 防止回车提交表单
document.getElementById("deliveryForm1").onkeydown =function(e){
  var e = e || event;
  var keyNum = e.keyCode || e.which || e.charCode;
  return keyNum==13 ? false : true;
};

// 点击提交按钮
//function submitDelivery(object) {
//	$("#deliveryForm", navTab.getCurrentPanel()).submit();
//}

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
	var param="shippingNo="+value+"&batchCode="+batchCode+"&warehouseId="+warehouseId;
	$.ajax({
	   url: "${ctx}/stock/delivery!checkKuaidi.action",
	   data: param,
	   success: function(data){
		   	if(data.ok==true) {
		   		var quanInput = $("#quantity", navTab.getCurrentPanel());
		   		quanInput.val(data.result);
		   		$("#deliveryBox").loadUrl("${ctx}/stock/delivery!deliveryToKdList.action", "batchCode="+batchCode, null);
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
	$("#scanTips", navTab.getCurrentPanel()).html(message);
}
</script>
