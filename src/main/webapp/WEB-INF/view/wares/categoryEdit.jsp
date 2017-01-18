<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<form method="post" action="<s:if test='id == null'>${ctx}/wares/category!add.action?callbackType=closeCurrent&navTabId=tab_category</s:if><s:else>${ctx}/wares/category!update.action?callbackType=closeCurrent&navTabId=tab_category</s:else>" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="id" value="${id}"/>
		<div class="pageFormContent" layoutH="56">
			<fieldset>
				<legend>分类信息</legend>
				<dl class="nowrap">
					<dt>分类名称：</dt>
					<dd><input name="catName" class="required" type="text" maxlength="10" size="20" value="${catName}" alt="请输入分类名称"/></dd>
				</dl>
				<dl class="nowrap">
					<dt>上级分类：</dt>
					<dd>
						<select name=catPid class="required">
							<option value="">请选择</option>
							<c:forEach items="${categoryList}" var="cat">
								<option value="${cat.id }" ${(cat.id==catPid)?"selected='true'":""}>
								<c:set var="level" value="${fn:length(fn:split(cat.catPath,','))}"/>
								<c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>${cat.catName }
								</option>
							</c:forEach>
						</select>
					</dd>
				</dl>
				<dl class="nowrap">
					<dt>备注：</dt>
					<dd><textarea name="catDesc" cols="80" rows="2">${catDesc }</textarea></dd>
				</dl>
			</fieldset>
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
