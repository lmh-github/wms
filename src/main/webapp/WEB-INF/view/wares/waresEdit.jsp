<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form action="<s:if test='id == null'>${ctx}/wares/wares!add.action?callbackType=closeCurrent&navTabId=tab_wares</s:if><s:else>${ctx}/wares/wares!update.action?callbackType=closeCurrent&navTabId=tab_wares</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>商品名称：</dt>
				<dd><input name="waresName" class="required" type="text" maxlength="20" size="20" value="${waresName }" /></dd>
			</dl>
			<dl class="nowrap">
				<dt>商品编码：</dt>
				<dd><input name="waresCode" class="required" type="text" maxlength="10" size="20" value="${waresCode }"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>商品分类：</dt>
				<dd>
					<select  name="category.id">
					<c:forEach items="${categoryList}" var="cat">
						<option value="${cat.id }" ${(cat.id==category.id)?"selected='true'":""}>
						<c:set var="level" value="${fn:length(fn:split(cat.catPath,','))}"/>
						<c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>${cat.catName }
						</option>
					</c:forEach>
					</select>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>商品品牌：</dt>
				<dd><input name="waresBrand" type="text" maxlength="20" size="20" value="${waresBrand }"/></dd>
			</dl>
			<dl>
				<dt>商品类型：</dt>
				<dd>
					<select class="required combox" name="attrSet.id">
						<option value="">请选择</option>
						<c:forEach items="${attrSetList}" var="item">
							<option value=${item.id } ${(item.id==attrSet.id)?"selected='true'":""}>${item.attrSetName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>计量单位：</dt>
				<dd><input name="measureUnit" class="required" type="text" maxlength="5" size="10" value="${measureUnit }" /></dd>
			</dl>
			<dl class="nowrap">
				<dt>是否管理商品身份码：</dt>
				<dd>
					<label><input type="radio" name="indivEnabled" class="required" value="1" ${indivEnabled==1?'checked="checked"':''}/>是</label>
					<label><input type="radio" name="indivEnabled" class="required" value="0" ${indivEnabled==0?'checked="checked"':''}/>否</label>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>商品规格型号：</dt>
				<dd><input name="waresModel" class="required" type="text" maxlength="20" size="20" value="${waresModel}"/></dd>
			</dl>
			<dl>
				<dt>发票备注信息：</dt>
				<dd><input name="waresRemark" type="text" maxlength="20" size="20" value="${waresRemark}"/></dd>
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




