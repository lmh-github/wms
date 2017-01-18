<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form action="${ctx}/wares/indiv!update.action?callbackType=closeCurrent&navTabId=tab_indiv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>更改商品类型</legend>
			<dl class="nowrap">
				<dt>个体编码：</dt>
				<dd><input name="indivCode" class="readonly" disabled="disabled" type="text" size="30" value="${indivCode }"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>商品性质：</dt>
				<dd>
					<label><input type="radio" name="waresStatus" class="required" value="<s:property value='@com.gionee.wms.common.WmsConstants$IndivWaresStatus@NON_DEFECTIVE.code'/>" <s:if test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@NON_DEFECTIVE.code">checked="checked"</s:if> />良品</label>
					<label><input type="radio" name="waresStatus" class="required" value="<s:property value='@com.gionee.wms.common.WmsConstants$IndivWaresStatus@DEFECTIVE.code'/>" <s:if test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@DEFECTIVE.code">checked="checked"</s:if> />次品</label>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="remark" cols="80" rows="2">${remark }</textarea></dd>
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




