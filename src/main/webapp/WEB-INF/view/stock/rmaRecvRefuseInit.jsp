<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">拒收单</h2>
<form id="pagerForm" name="rmaRecvForm" action="${ctx}/stock/rmaRecv!refuse.action?callbackType=closeCurrent&navTabId=tab_salesOrder" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="receive.originalId" value="${order.id }">
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<dl>
				<dt>销售订单号：</dt>
				<dd>
					<input name="receive.originalCode" type="text" maxlength="30" class="required" value="${order.orderCode}" readonly="readonly"/>
				</dd>
			</dl>
			<dl>
				<dt>仓库：</dt>
				<dd>
					<select class="required" name="receive.warehouseId">
						<option value="">请选择</option>
						<s:iterator value="warehouseList">
							<option value=${id }>${warehouseName }</option>
						</s:iterator>
						
					</select>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>商品状态：</dt>
				<dd>
					<label><input type="radio"  name="waresStatus" class="required" value="1" />良品</label>
					<label><input type="radio"  name="waresStatus" class="required" value="0" />次品</label>
				</dd>
			</dl>
		</fieldset>
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
function doSubmit(){
	$("form[name=rmaRecvForm]").submit();
}

//自定义DWZ回调函数
function myAjaxDone(json){
	DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
    	$.pdialog.reload('${ctx}/stock/rmaRecv!input.action?id='+json.receiveId,'');
    }
}
//-->
</script>