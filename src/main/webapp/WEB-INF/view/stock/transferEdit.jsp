<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form action="<s:if test='transfer == null'>${ctx}/stock/transfer!add.action?callbackType=closeCurrent&navTabId=tab_transfer</s:if><s:else>${ctx}/stock/transfer!update.action?callbackType=closeCurrent&navTabId=tab_transfer</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="transfer.transferId" value="${transfer.transferId}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl class="nowrap">
				<dt>发货仓：</dt>
				<dd>
					<select class="required" name="transfer.warehouseId">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.id } ${(item.id eq transfer.warehouseId)?"selected='true'":""}>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>收货地址：</dt>
				<dd><input name="transfer.transferTo" class="required" type="text" maxlength="40" size="40" value="${transfer.transferTo }" ${editEnabled?"":"readonly='readonly'"} /></dd>
			</dl>
			<dl>
				<dt>收货人：</dt>
				<dd><input name="transfer.consignee" type="text" maxlength="20" size="20" value="${transfer.consignee }" ${editEnabled?"":"readonly='readonly'"}/></dd>
			</dl>
			<dl>
				<dt>联系方式：</dt>
				<dd><input name="transfer.contact" type="text" maxlength="20" size="20" value="${transfer.contact }" ${editEnabled?"":"readonly='readonly'"}/></dd>
			</dl>
			<dl>
				<dt>PO：</dt>
				<dd><input name="transfer.po" type="text" maxlength="20" size="20" value="${transfer.po }" ${editEnabled?"":"readonly='readonly'"}/></dd>
			</dl>
			<dl>
				<dt>物流公司：</dt>
				<dd><input name="transfer.logisticName" type="text" maxlength="20" size="20" value="${transfer.logisticName }" ${editEnabled?"":"readonly='readonly'"}/></dd>
			</dl>
			<dl class="nowrap">
				<dt>售达方：</dt>
				<select class="required" name="transfer.transferSale">
					<option value="">请选择</option>
					<c:forEach items="${transferPartnerList}" var="item">
						<option value=${item.id} ${(item.id eq transfer.transferSale)?"selected='true'":""}>${item.name}</option>
					</c:forEach>
				</select>
			</dl>
			<dl>
				<dt>送达方：</dt>
				<select class="required" name="transfer.transferSend">
					<option value="">请选择</option>
					<c:forEach items="${transferPartnerList}" var="item">
						<option value=${item.id} ${(item.id eq transfer.transferSend)?"selected='true'":""}>${item.name}</option>
					</c:forEach>
				</select>
			</dl>
			<dl class="nowrap">
				<dt>开票方：</dt>
				<select class="required" name="transfer.transferInvoice">
					<option value="">请选择</option>
					<c:forEach items="${transferPartnerList}" var="item">
						<option value=${item.id} ${(item.id eq transfer.transferInvoice)?"selected='true'":""}>${item.name}</option>
					</c:forEach>
				</select>
			</dl>
			<dl class="nowrap">
				<dt>订单金额：</dt>
				<dd><input name="transfer.orderAmount" type="text" class="textInput number valid required" value="${transfer.orderAmount eq null ? '0' : transfer.orderAmount}" /></dd>
			</dl>
			<dl>
				<dt>调货类型：</dt>
				<dd>
					<select class="required" name="transfer.transType">
						<option value="0" ${(0==transfer.transType)?"selected='true'":""}>良品调拨</option>
						<option value="1" ${(1==transfer.transType)?"selected='true'":""}>次品调拨</option>
					</select>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="transfer.remark" cols="80" rows="2">${transfer.remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<div class="tabs">
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>




