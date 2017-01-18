<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="checkDetailForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
	<input type="hidden" name="wares.category.catPath" value="${wares.category.catPath }" />
	<input type="hidden" name="skuCode" value="${skuCode }" />
	<input type="hidden" name="skuName" value="${skuName }" />
	<input type="hidden" name="wares.id" value="${wares.id }" />
</form>

<div class="pageContent">
	<div class="pageFormContent">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>盘点编号：</dt>
				<dd>${check.checkCode }</dd>
			</dl>
			<dl>
				<dt>仓库：</dt>
				<dd>${check.checkCode }</dd>
			</dl>
		</fieldset>
	</div>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/stock/checkDetail!input.action?stockCheckId=${id}" rel="dlg_checkDetail" target="dialog" mask="true" width="1250" height="600"><span>添加待盘点SKU</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="${ctx}/stock/stockCheck!downloadCheckDetail.action?id=${id }" target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="${ctx}/stock/stockCheck!inputUploadCheckList.action?id=${id}" rel="dlg_checkDetail" target="dialog" mask="true" width="800" height="600"><span>上传采集数据</span></a></li>
			<li class="line">line</li>
			<li><a href="${ctx}/stock/stockCheck!confirmStockCheck.action?id=${id }" target="ajaxTodo" targetType="navTab" title="确认盘点结果，库存数量会根据实盘库存进行校准，确实要进行确认操作吗?"><span>确认盘点</span></a></li>
			
		</ul>
	</div>
	<table class="list" width="1200" layoutH="138">
		<thead>
			<tr align="center">
				<th colspan="3">SKU信息</th>
				<th colspan="3">良品</th>
				<th colspan="3">次品</th>
				<th colspan="1"></th>
			</tr>
			<tr>
				<th width="50">SKU编码</th>
				<th width="100">SKU名称</th>
				<th width="50">计量单位</th>
				<th width="50">账面盘良品数</th>
				<th width="50">实盘良品数</th>
				<th width="50">实盘良品盈亏</th>
				<th width="50">账面盘次品数</th>
				<th width="50">实盘次品数</th>
				<th width="50">实盘次品盈亏</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${stockCheckDetail}" var="item">
			<tr target="sid_user" rel="1">
				<td>${item.sku.skuCode }</td>
				<td>${item.sku.skuName }</td>
				<td>${item.sku.wares.measureUnit }</td>
				<td>${item.bookNondefective }</td>
				<td>${item.firstNondefective }</td>
				<td><span ${item.firstNondefectivePl>0?"style='color:green;'":(item.firstNondefectivePl<0?"style='color:red;'":"")}>${item.firstNondefectivePl }</span></td>
				<td>${item.bookDefective }</td>
				<td>${item.firstDefective }</td>
				<td><span ${item.firstDefectivePl>0?"style='color:green;'":(item.firstDefectivePl<0?"style='color:red;'":"")}>${item.firstDefectivePl }</span></td>
				<td>
					<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/checkDetail!delete.action?detailItemId=${item.id}&navTabId=tab_checkDetail" class="btnDel"></a>
				</td>

			</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="1" ${page.pageSize==1?"selected='true'":""}>1</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>