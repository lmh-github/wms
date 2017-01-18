<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle">添加属性</h2>
<form action="<s:if test='id == null'>${ctx}/wares/skuAttr!add.action?callbackType=closeCurrent&navTabId=tab_skuAttr</s:if><s:else>${ctx}/wares/skuAttr!update.action?callbackType=closeCurrent&navTabId=tab_skuAttr</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<input type="hidden" name="attrSet.id" value="${attrSet.id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>属性名称：</dt>
				<dd><input name="attrName" class="required" type="text" size="30" value="${attrName }" alt="请输入分类名称"/></dd>
			</dl>
			<dl>
				<dt>商品类型：</dt>
				<dd>
					<select class="required" disabled="disabled">
						<option value="">请选择</option>
						<c:forEach items="${attrSetList}" var="item">
							<option value=${item.id } ${(item.id==attrSet.id)?"selected='true'":""}>${item.attrSetName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
		</fieldset>
		
		<h3 class="contentTitle">属性可选项</h3>
		<div class="tabs">
			<div class="tabsContent" style="height: 200px;">
				<div>
					<table class="list nowrap itemDetail" addButton="新增可选项" width="100%">
						<thead>
							<tr>
								<th type="text" name="itemList[#index#].itemName" size="12" fieldClass="required">可选项名称</th>
								<th type="del" width="60">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${itemList}" var="item" varStatus="status">
								<tr class="unitBox">
									<td>
										<input type="text" class="required textInput valid" size="12" value="${item.itemName}" name="itemList[${status.index}].itemName">
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
		</div>
		<div class="divider"></div>
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




