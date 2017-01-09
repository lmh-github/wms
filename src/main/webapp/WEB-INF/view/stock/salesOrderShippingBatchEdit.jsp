<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form id="theForm" action="${ctx}/stock/salesOrder!updateShippingInfo.action?callbackType=closeCurrent&navTabId=tab_salesOrderOrderPrint" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent">
		<table class="list" width="100%" layoutH="90">
			<thead>
				<tr>
					<th type="text">订单号</th>
					<th type="text">配送方式</th>
					<th type="text">配送单号</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="orderList" status="status">
					<tr class="unitBox">
						<td>
							<input type="hidden" name="orderList[${status.index}].id" value="${id}"></input>
							<input type="text" readonly="readonly" size="20" value="${orderCode }" name="orderList[${status.index}].orderCode">
						</td>
						<td>
							<select class="required" name="orderList[${status.index}].shippingId">
								<c:forEach items="${shippingList}" var="item">
									<option value=${item.id } ${(item.id==shippingId)?"selected='true'":""}>${item.shippingName }</option>
								</c:forEach>
							</select>
						</td>
						<td>
							<input type="text" size="20" minlength="10" maxlength="15" value="${shippingNo }" name="orderList[${status.index}].shippingNo" class="required alphanumeric textInput valid" onkeydown="doKeydown(event)">
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
	<div class="formBar">
		<ul>
			<s:if test="goods.receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
			</s:if>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
//监听回车 
function doKeydown(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		var currIndex = theEvent.target.name.substring(theEvent.target.name.indexOf('[')+1,theEvent.target.name.indexOf(']'));
    	var nextInputName = 'orderList['+(parseInt(currIndex)+1)+'].shippingNo';
		$("input[name='"+nextInputName+"']").focus();//聚焦下个输入框
		return false;
	}
}

function doSubmit(){
	$('#theForm').submit();
}
//-->
</script>
