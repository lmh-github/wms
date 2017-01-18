<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">盘点单</h2>
<form id="pagerForm" name="purchaseInForm" action="${ctx}/stock/purchaseRecv!confirmReceive.action?id=${id }&callbackType=closeCurrent&navTabId=tab_purchaseRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">

<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>盘点编号：</dt>
				<dd>${check.checkCode }</dd>
			</dl>
			<dl class="nowrap">
				<dt>盘点单备注：</dt>
				<dd><textarea cols="80" rows="1" id="remark" readonly="readonly">${check.remark }</textarea></dd>
			</dl>
			<s:if test="checkGoods.remarkIn!=null">
				<dl class="nowrap">
					<dt>入库盘点单备注：</dt>
					<dd><textarea cols="80" rows="1" id="remarkIn" readonly="readonly">${checkGoods.remarkIn}</textarea></dd>
				</dl>
			</s:if>
			<s:if test="checkGoods.remarkOut!=null">
				<dl class="nowrap">
					<dt>出库盘点单备注：</dt>
					<dd><textarea cols="80" rows="1" id="remarkOut" readonly="readonly">${checkGoods.remarkOut}</textarea></dd>
				</dl>
			</s:if>
		</fieldset>
		
		<h3 class="contentTitle">个体对比结果</h3>
		<div class="panelBar">
			<ul class="toolBar">
				<s:if test="check.handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@UNCONFIRMED.code&&check.auditStatus==999">
				<s:if test="checkGoods.remarkIn==null">
				<li><a class="add" href="${ctx}/stock/stockCheck!inputConfirmItem.action?id=${id}&skuCode=${skuCode}&confirmType=in" rel="dlg_checkConfirmItem" target="dialog" mask="true" width="750" height="400"><span>确认入库</span></a></li>
				<li class="line">line</li>
				</s:if>
				<s:if test="checkGoods.remarkOut==null">
				<li><a class="add" href="${ctx}/stock/stockCheck!inputConfirmItem.action?id=${id}&skuCode=${skuCode}&confirmType=out" rel="dlg_checkConfirmItem" target="dialog" mask="true" width="750" height="400"><span>确认出库</span></a></li>
				</s:if>
				</s:if>
			</ul>
		</div>
		
		<div class="tabs">
			<div class="tabsContent" style="height: 280px;">
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th>SKU编码</th>
								<th>SKU名称</th>
								<th>个体编码</th>
								<th>良/次</th>
							</tr>
						</thead>
						<tr class="unitBox" height="30px"><td colspan="4"><h1>盘点多出数据</h1></td></tr>
						<tbody id="stockInDetailTbody">
							<s:iterator value="checkItemList">
							<s:if test="compareType==1">
							<tr class="unitBox">
								<td>${skuCode }</td>
								<td>${skuName }</td>
								<td>${indivCode }</td>
								<s:if test="waresStatus==0">
								<td>次品</td>
								</s:if>
								<s:else>
								<td>良品</td>
								</s:else>
							</tr>
							</s:if>
							</s:iterator>
						</tbody>
						<tr class="unitBox" height="20px"><td colspan="4"></td></tr>
						<tr class="unitBox" height="30px"><td colspan="4"><h1>系统多出数据</h1></td></tr>
						<tbody id="stockInDetailTbody">
							<s:iterator value="checkItemList">
							<s:if test="compareType==2">
							<tr class="unitBox">
								<td>${skuCode }</td>
								<td>${skuName }</td>
								<td>${indivCode }</td>
								<s:if test="waresStatus==0">
								<td>次品</td>
								</s:if>
								<s:else>
								<td>良品</td>
								</s:else>							
							</tr>
							</s:if>
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
							<dt><input readonly="true" name="field5" type="text" value="${check.preparedBy }"/></dt>
						</dl>
						<dl>
							<dt>制单日期：</dt>
							<dt><input readonly="true" name="field5" type="text" value="<fmt:formatDate value='${check.preparedTime}' dateStyle="long" pattern='yyyy-MM-dd' />"/></dt>
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