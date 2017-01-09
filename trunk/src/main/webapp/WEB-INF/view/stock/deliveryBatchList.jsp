<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/deliveryBatch.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li style="width:380px;">
				<label>制单时间从：</label>
				<input type="text" name="preparedTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${preparedTimeBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				到：
				<input type="text" name="preparedTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${preparedTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			</li>
			<li>
				<label>拣货编号：</label>
				<input type="text" name=batchCode value="${batchCode}"/>
			</li>
			<li>
				<label>处理状态：</label>
				<s:select name="handlingStatus" list="@com.gionee.wms.common.WmsConstants$DeliveryBatchStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
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
	<table class="list" width="100%" layoutH="90">
		<thead>
			<tr align="center">
				<th>拣货批次</th>
				<!-- <th>仓库</th> -->
				<th>制单时间</th>
				<th>制单人</th>
				<th>处理状态</th>
				<th>处理时间</th>
				<th>处理人</th>
				<!-- <th>备注</th> -->
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="deliveryBatchList" >
			<tr align="center">
				<td>${batchCode }</td>
				<!-- <td>${warehouseName }</td> -->
				<td><fmt:formatDate value="${preparedTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${preparedBy }</td>
				<td>
					<s:if test="handlingStatus==@com.gionee.wms.common.WmsConstants$DeliveryBatchStatus@PENDING.code"><span style="color:red;">待处理</span></s:if>
					<s:elseif test="handlingStatus==@com.gionee.wms.common.WmsConstants$DeliveryBatchStatus@FINISHED.code">已完成</s:elseif>
				</td>
				<td><fmt:formatDate value="${handledTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${handledBy }</td>
				<!-- <td>${remark }</td> -->
				<td>
					<a target="navTab" rel="tab_delivery" href="${ctx}/stock/delivery!listForBatch.action?batchId=${id}" ><span>查看详情</span></a>
				</td>

			</tr>
			</s:iterator>
			
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>