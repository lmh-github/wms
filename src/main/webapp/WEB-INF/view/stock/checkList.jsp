<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
	<input type="hidden" name="checkCode" value="${checkCode }" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/stockCheck.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>盘点编号：</label>
				<input type="text" name="checkCode" value="${checkCode}"/>
			</li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/stockCheck!input.action" target="navTab"><span>创建盘点单</span></a></li>
			<li class="line">line</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th>盘点编号</th>
				<th>盘点仓库</th>
				<th>计划盘点日期</th>
				<th>实际盘点时间</th>
				<th>制单人</th>
				<th>制单时间</th>
				<th>盘点类型</th>
				<th>处理状态</th>
				<th>处理时间</th>
				<th>处理人</th>
				<th>备注</th>
				<th>库存历史信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="checkList">
			<tr target="sid_user" rel="1">
				<td><a href="${ctx}/stock/stockCheck!input.action" target="navTab">${checkCode }</a></td>
				<td>${warehouseName }</td>
				<td><fmt:formatDate value="${plannedTime }" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${firstTime }" pattern="yyyy-MM-dd"/></td>
				<td>${preparedBy }</td>
				<td><fmt:formatDate value="${preparedTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<s:if test="checkType==2">盘点个体</s:if>
					<s:elseif test="checkType==1 || checkType==null">盘点配件</s:elseif>
			    </td>
				<td>
					<s:if test="handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@PENDING.code">待处理</s:if>
					<s:elseif test="handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@UNCONFIRMED.code">待确认</s:elseif>
					<s:elseif test="handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@FINISHED.code">已确认</s:elseif>
			    </td>
			    <td><fmt:formatDate value="${handledTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			    <td>${handledBy }</td>
				<td>${remark }</td>
				<td>
				<s:if test="stockDumpStatus==@com.gionee.wms.common.WmsConstants$StockDumpStatus@DUMPED.code">
				<a title="库存历史信息" target="navTab" style="color:blue" href="${ctx}/stock/stockCheck!stockHistoryList.action?checkId=${id}">库存记录</a>
				</s:if>
				</td>
				<td>
					<s:if test="handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@PENDING.code">
						<a title="删除" target="ajaxTodo" href="${ctx}/stock/stockCheck!delete.action?id=${id}" class="btnDel">删除</a>
						<a title="盘点明细" target="navTab" rel="tab_checkGoods" href="${ctx}/stock/stockCheck!listGoods.action?id=${id}&type=${checkType}" class="btnEdit"></a>
					</s:if>
					<s:if test="handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@UNCONFIRMED.code">
					    <s:if test="auditStatus==0">
					    <a title="审核" target="ajaxTodo" href="${ctx}/stock/stockCheck!audit.action?id=${id}" class="btnAssign">审核</a>
					    </s:if>
					    <a title="盘点明细" target="navTab" rel="tab_checkGoods" href="${ctx}/stock/stockCheck!listGoods.action?id=${id}&type=${checkType}" class="btnEdit"></a>
					</s:if>
					<s:if test="handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@FINISHED.code">
						<a title="盘点明细" target="navTab" rel="tab_checkGoods" href="${ctx}/stock/stockCheck!listGoods.action?id=${id}&type=${checkType}" class="btnView"></a>
					</s:if>
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