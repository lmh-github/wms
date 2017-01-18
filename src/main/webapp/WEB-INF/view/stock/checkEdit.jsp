<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle">创建盘点单</h2>
<form action="${ctx}/stock/stockCheck!add.action?callbackType=closeCurrent&navTabId=tab_stockCheck" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<!-- <dl>
				<dt>盘点编号：</dt>
				<dd><input readonly="true" name="checkCode" type="text" value="${checkCode }"/></dd>
			</dl> -->
			<dl>
				<dt>盘点仓库：</dt>
				<dd>
					<select class="required combox" name="check.warehouseId">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.id }>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>盘点类型：</dt>
				<dd>
					<select class="required" name="check.checkType">
						<option value="">请选择</option>
						<option value="1" <s:if test="check.checkType==1">selected</s:if>>配件盘点</option>
						<option value="2" <s:if test="check.checkType==2">selected</s:if>>个体盘点</option>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>计划盘点时间：</dt>
				<dd>
					<input type="text" name="check.plannedTime" class="date" dateFmt="yyyy-MM-dd" minDate="{%y}-%M-%d" value="<fmt:formatDate value="${plannedTime }" pattern="yyyy-MM-dd"/>"/>
					<a class="inputDateButton" href="javascript:;">选择</a>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="check.remark" cols="80" rows="2"></textarea></dd>
			</dl>
		</fieldset>
		<!--  
		<h3 class="contentTitle">盘点单SKU列表</h3>
		<div class="tabs">
			<div class="tabsContent" style="height: 200px;">
				<div>
					<table class="list nowrap itemDetail" addButton="添加" width="100%">
						<thead>
							<tr>
								<th type="lookup" name="checkDetail[#index#].sku.skuCode" bringBackName="checkDetail[#index#].sku.skuCode" lookupGroup="checkDetail[#index#].sku" lookupUrl="${ctx}/wares/sku!lookup.action" rel="dlg_skuLookup" postField="keywords" size="12" fieldClass="required" readonly="true">SKU编码</th>
								<th type="text" bringBackName="checkDetail[#index#].sku.skuName" size="30" fieldClass="required" readonly="true">SKU名称</th>
								<th type="text" bringBackName="checkDetail[#index#].sku.measureUnit" size="12" fieldClass="required" readonly="true">单位</th>
								<th type="del" width="60">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${checkDetail}" var="item" varStatus="status">
								<tr class="unitBox">
									<td>
										<input type="hidden" name="checkDetail[${status.index}].sku.id" value="${item.sku.id }">
										<input type="text" class="required textInput readonly valid" size="12" lookuppk="id" readonly="readonly" name="checkDetail[${status.index}].sku.skuCode" value="${item.sku.skuCode }">
										<a title="查找带回" lookuppk="id" readonly="readonly" lookupgroup="checkDetail[1].sku" href="/wms/wares/sku!lookup.action" class="btnLook">查找带回</a>
									</td>
									<td>
										<input type="text" class="required textInput readonly" size="30" name="checkDetail[${status.index}].sku.skuName" readonly="readonly" value="${item.sku.skuName }">
									</td>
									<td>
										<input type="text" class="required textInput readonly" size="12" name="checkDetail[${status.index}].sku.wares.measureUnit" readonly="readonly" value="${item.sku.wares.measureUnit }">
									</td>
									<td>
										<a class="btnDel " href="javascript:void(0)">删除</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div> -->
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
