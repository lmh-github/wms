<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/basis/supplier!input.action" rel="dlg_supplierAdd" target="dialog" mask="true" width="800" height="600"><span>添加供应商</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th>供应商ID</th>
				<th>供应商编号</th>
				<th>供应商名字</th>
				<th>供应商地址</th>
				<th>供应商电话</th>
				<th>供应商联系人</th>
				<th>默认供应商</th>
				<th>创建时间</th>
				<th>备注</th>
				<th>供应商状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="supplierList">
			<tr>
				<td>${id }</td>
				<td>${supplierCode }</td>
				<td>${supplierName }</td>
				<td>${supplierAddress }</td>
				<td>${supplierPhone }</td>
				<td>${supplierContact }</td>
				<td>${defaultStatus==1?"默认":"" }</td>
				<td><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${remark }</td>
				<td>${enabled==1?"正常":"<span style='color:red;'>停用</span>" }</td>
				<td>
					<a title="编辑" rel="dlg_supplierEdit" target="dialog" mask="true" width="800" height="600" href="${ctx}/basis/supplier!input.action?id=${id}" class="btnEdit"></a> |
					<s:if test="defaultStatus==0 && enabled==1">
						<a title="确实要设置为默认供应商吗？" target="ajaxTodo" href="${ctx}/basis/supplier!setDefault.action?id=${id}&navTabId=tab_supplier">设为默认</a> |
					</s:if>
					<s:if test="enabled==1">
						<a title="确实要停用吗？" target="ajaxTodo" href="${ctx}/basis/supplier!disable.action?id=${id}&navTabId=tab_supplier">停用</a>
					</s:if>
					<s:else>
						<a title="确实要启用吗？" target="ajaxTodo" href="${ctx}/basis/supplier!enable.action?id=${id}&navTabId=tab_supplier">启用</a>
					</s:else>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="page.pageSize" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>