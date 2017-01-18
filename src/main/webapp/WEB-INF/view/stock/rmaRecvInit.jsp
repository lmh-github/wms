<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">退货入库单</h2>
<form id="pagerForm" name="rmaRecvForm" action="${ctx}/stock/rmaRecv!add.action?callbackType=closeCurrent&navTabId=tab_salesOrder" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="receive.originalId" value="${order.id }">
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<dl>
				<dt>销售订单号：</dt>
				<dd>
					<input name="receive.originalCode" type="text" maxlength="30" class="required" value="${order.orderCode }" readonly="readonly"/>
				</dd>
			</dl>
			<dl>
				<dt>退货仓库：</dt>
				<dd>
                    <input type="text" readonly="readonly" value="东莞电商仓" />
                    <input type="hidden" value="1643" name="receive.warehouseId" />
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>商品状态：</dt>
				<dd>
					<label><input type="radio"  name="waresStatus" class="required" value="1" />良品</label>
					<label><input type="radio"  name="waresStatus" class="required" value="0" />次品</label>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="receive.remark" cols="80" rows="1" id="remark"></textarea></dd>
			</dl>
		</fieldset>
		
		<!-- 
		<h2 class="contentTitle" align="center">商品信息</h2>
		<div>
			<table class="list" width="100%">
				<thead>
					<tr>
						<th width="22"><input type="checkbox" group="goodIds" class="checkboxCtrl"></th>
						<th align="center">SKU编码</th>
						<th align="center">SKU名称</th>
						<th align="center">单位</th>
						<th align="center">单价</th>
						<th align="center">数量</th>
						<th align="center">良品数量</th>
						<th align="center">次品数量</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="orderGoods" status="status">
					<tr class="unitBox" target="sid_user" rel="${status.index }">
						<td><input name="goodIds" value="${id}_${status.index}" type="checkbox"></td>
						<td align="center">${skuCode }</td>
						<td align="center">${skuName }</td>
						<td align="center">${measureUnit }</td>
						<td align="center">${unitPrice }</td>
						<td align="center">${quantity }</td>
						<td align="center"><input name="nonDefective" value="${quantity}" type="text" size="1"/></td>
						<td align="center"><input name="defective" value="0" type="text" size="1"/></td>
					</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		 -->
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