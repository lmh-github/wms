<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<form method="post" action="${ctx}/stock/stock!updateLimit.action?callbackType=closeCurrent&navTabId=tab_stock" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="id" value="${id}"/>
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>仓库：</label>
				<input type="text" class="readonly" readonly="readonly" value="${warehouse.warehouseName }" />
			</p>
			<p>
				<label>SKU编码：</label>
				<input type="text" class="readonly" readonly="readonly" value="${sku.skuCode }" />
			</p>
			<p>
				<label>SKU名称：</label>
				<input type="text" class="readonly" readonly="readonly" value="${sku.skuName }" />
			</p>
			<p>
				<label>库存下限：</label>
				<input type="text" name="limitLower" class="digits" min="1" maxlength="8" value="${limitLower>-1?limitLower:'' }"/>
			</p>
			<p>
				<label>库存上限：</label>
				<input type="text" name="limitUpper" class="digits" min="1" maxlength="8" value="${limitUpper>-1?limitUpper:'' }"/>
			</p>
		</div>
		<div class="formBar">
			<ul>
				<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
