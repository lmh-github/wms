<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">快捷收货单</h2>
<form id="pagerForm" name="purchaseRecvForm" action="${ctx}/stock/purchaseRecv!confirmReceive.action?id=${id }&callbackType=closeCurrent&navTabId=tab_purchaseRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">

<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>收货编号：</dt>
				<dd>${transfer.transferId }</dd>
			</dl>
			<dl>
				<dt>收货仓：</dt>
				<dd>
					${transfer.transferTo }
				</dd>
			</dl>
			<dl>
				<dt>发货仓：</dt>
				<dd>
					${transfer.warehouseName }
				</dd>
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
								<th width="60">调拨数量</th>
								<th width="60">实收数量</th>
								<th width="60">操作</th>
							</tr>
						</thead>
						<tbody id="purRecvTbody">
							<s:iterator value="goodsList">
							<tr class="unitBox">
								<td>${skuCode }</td>
								<td>${skuName }</td>
								<td>${measureUnit }</td>
								<td>${quantity }</td>
								<td>${qty }</td>
								<td>
									<s:if test = "qty!=null">
										<a title="查看实收商品IMEI" target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/transferSf!inputGoods.action?skuCode=${skuCode}&transferId=${transfer.transferId }" class="btnView"></a>
									</s:if>
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
							<dt>操作人：</dt>
							<dt><input readonly="true" name="field5" type="text" value="${transfer.handledBy }"/></dt>
						</dl>
						<dl>
							<dt>创建日期：</dt>
							<dt><input readonly="true" name="field5" type="text" value="<fmt:formatDate value='${transfer.createTime}' dateStyle="long" pattern='yyyy-MM-dd' />"/></dt>
						</dl>
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>