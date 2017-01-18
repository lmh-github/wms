<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/basis/shipping!input.action" rel="dlg_shippingAdd" target="dialog" mask="true" width="800" height="600"><span>添加配送方式</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%">
		<thead>
			<tr>
				<th>配送方式编号</th>
				<th>配送公司编码</th>
				<th>配送方式名称</th>
				<th>联系电话</th>
				<th>联系人</th>
				<th>默认配送方式</th>
				<th>创建时间</th>
				<th>备注</th>
				<th>配送方式状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="shippingList">
			<tr>
				<td>${shippingCode }</td>
				<td>${companyCode }</td>
				<td>${shippingName }</td>
				<td>${phone }</td>
				<td>${contact }</td>
				<td>${defaultStatus==1?"默认":"" }</td>
				<td><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${remark }</td>
				<td>${enabled==1?"正常":"<span style='color:red;'>停用</span>" }</td>
				<td>
					<a title="编辑" rel="dlg_shippingEdit" target="dialog" mask="true" width="800" height="600" href="${ctx}/basis/shipping!input.action?id=${id}" class="btnEdit"></a>
					<s:if test="defaultStatus==0 && enabled==1">
						&nbsp;|&nbsp;
						<a title="确实要设置为默认配送方式吗？" target="ajaxTodo" href="${ctx}/basis/shipping!setDefault.action?id=${id}&navTabId=tab_shipping">设为默认</a>
					</s:if>
					&nbsp;|&nbsp;
					<s:if test="enabled==1">
						<a title="确实要停用吗？" target="ajaxTodo" href="${ctx}/basis/shipping!disable.action?id=${id}&navTabId=tab_shipping">停用</a>
					</s:if>
					<s:else>
						<a title="确实要启用吗？" target="ajaxTodo" href="${ctx}/basis/shipping!enable.action?id=${id}&navTabId=tab_shipping">启用</a>
					</s:else>
					<shiro:hasPermission name="printableTemplate:editShippingTemplate">
						&nbsp;|&nbsp;
						<a title="运单模板" target="navTab" rel="tab_printableTemplateInput" href="${ctx}/basis/printableTemplate!inputShippingTemplate.action?templateName=${templateName}">编辑运单模板</a>
					</shiro:hasPermission>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</div>