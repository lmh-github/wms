<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">采购收货单</h2>
<form id="pagerForm" name="purchaseInForm" action="${ctx}/stock/purchaseRecv!confirmReceive.action?id=${id }&callbackType=closeCurrent&navTabId=tab_purchaseRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">

<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>收货编号：</dt>
				<dd>${receive.receiveCode }</dd>
			</dl>
			<dl>
				<dt>仓库：</dt>
				<dd>
					${receive.warehouseName }
				</dd>
			</dl>
			<dl>
				<dt>供应商：</dt>
				<dd>
					${receive.supplierName }
				</dd>
			</dl>
			<dl>
				<dt>采购编号：</dt>
				<dd>
					${receive.originalCode }
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="receive.remark" cols="80" rows="1" id="remark">${receive.remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<h3 class="contentTitle">商品清单</h3>
		<div class="tabs">
			<div class="tabsContent" style="height: 300px;">
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="60">SKU编码</th>
								<th width="60">SKU名称</th>
								<th width="60">单位</th>
								<th width="60">数量</th>
								<th width="60">操作</th>
							</tr>
						</thead>
						<tbody id="stockInDetailTbody">
							<s:iterator value="goodsList">
							<tr class="unitBox">
								<td>${skuCode }</td>
								<td>${skuName }</td>
								<td>${measureUnit }</td>
								<td>${quantity }</td>
								<td>
									<!-- <a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/stockIn!deleteStockInItem.action?id=${item.id}&navTabId=tab_inputPurchaseIn" class="btnDel"></a> -->
									<s:if test="receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
									<a title="修改数量" target="dialog" rel="dlg_purchaseRecvInputGoods" mask="true" width="800" height="600" href="${ctx}/stock/purchaseRecv!inputGoods.action?id=${id}" class="btnEdit"></a>
									</s:if>
									<s:else>
									<a title="查看商品" target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/purchaseRecv!inputGoods.action?id=${id}" class="btnView"></a>
									</s:else>
								</td>
							</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
		<div class="divider"></div>
		<div class="tabs">
						<dl>
							<dt>制单人：</dt>
							<dt><input readonly="true" name="field5" type="text" value="${receive.preparedBy }"/></dt>
						</dl>
						<dl>
							<dt>制单日期：</dt>
							<dt><input readonly="true" name="field5" type="text" value="<fmt:formatDate value='${receive.preparedTime}' dateStyle="long" pattern='yyyy-MM-dd' />"/></dt>
						</dl>
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<s:if test="receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="purchaseInSubmit();">确认</button></div></div></li>
			</s:if>
			<li>
			<s:if test="id==null">
				<a class="button" target="ajaxTodo" href="${ctx}/stock/stockIn!cancel.action?stockInCode=${stockInCode}&callbackType=closeCurrent&navTabId=tab_stockIn"><span>取消</span></a>			
			</s:if>
			<s:else>
				<div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div>
			</s:else>
			</li>
		</ul>
	</div>
</div>
</form>
<script>
function openPurchaseInSkuInput(){
	var $form=$("form[name=purchaseInForm]");
	$form.find("input[name='warehouse.warehouseCode']").val($("#warehouseCode").val());
	$form.find("input[name='opposite']").val($("#opposite").val());
	$form.find("input[name='originalCode']").val($("#originalCode").val());
	$form.find("input[name='handledBy']").val($("#handledBy").val());
	$form.find("input[name='handledDate']").val($("#handledDate").val());
	$form.find("input[name='remark']").val($("#remark").val());
	$.pdialog.open('${ctx}/stock/stockIn!inputInItem.action?stockInCode='+$form.find("input[name='stockInCode']").val(), 'dlg_stockIn', '添加入库商品', {width:800,height:650,mask:true});
}
function purchaseInSubmit(){
	if($('#stockInDetailTbody tr').length==0){
		alert("入库商品明细不能为空");
		return false;
	}
	if(confirm("确认收货后商品库存将会相应增加，确定要执行吗？")){
		$("form[name=purchaseInForm]").submit();
	}
}
</script>