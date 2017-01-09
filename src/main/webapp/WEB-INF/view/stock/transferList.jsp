<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/transfer.action" method="post">
	<div class="searchBar">
		<ul class="searchContent" style="min-height: 50px;">
			<li>
				<label>发货仓：</label>
				<select class="required" name="transfer.warehouseId">
					<option value="">请选择</option>
					<c:forEach items="${warehouseList}" var="item">
						<option value=${item.id } ${(item.id==transfer.warehouseId)?"selected='true'":""}>${item.warehouseName }</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<label>收货地址：</label>
				<input type="text" name="transfer.transferTo" value="${transfer.transferTo}"/>
			</li>
			<li>
				<label>调货批次号：</label>
				<input type="text" name="transfer.transferId" value="${transfer.transferId}"/>
			</li>
			<li>
				<label>调货状态：</label>
				<select name="transfer.status">
					<option value="">请选择</option>
					<option value="1" ${(1==transfer.status)?"selected='true'":""}>未发货</option>
					<option value="2" ${(2==transfer.status)?"selected='true'":""}>已发货</option>
				</select>
			</li>
			<li>
				<label>物流单号：</label>
				<input type="text" name="transfer.logisticNo" value="${transfer.logisticNo}"/>
			</li>
            <li style="width:380px;">
              <label>变动时间从：</label>
              <input type="text" name="createTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                              到：
              <input type="text" name="createTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
            </li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
                <li><div class="buttonActive"><a href="${ctx}/stock/transfer.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></div></li>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/stock/transfer!input.action" target="dialog" mask="true" width="800" height="600"><span>添加调拨单</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" href="${ctx}/stock/transfer!inputManual.action?transfer.transferId={sid_transfer}" target="dialog"  rel="dlg_transfer_manual" mask="true" width="800" height="600"><span>查看编辑调拨单</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="${ctx}/stock/transfer!printTransfer.action?transferId={sid_transfer}" target="navTab"  targetType="navTab" warn="请选择调拨单"><span>打印调拨单</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="140">
		<thead>
			<tr>
				<th>调拨批次号</th>
				<th>发货仓</th>
				<th>收货人</th>
				<th>收货地址</th>
				<th>物流单号</th>
				<th>调货状态</th>
				<th>备注</th>
				<th>创建时间</th>
				<th>操作人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${transferList}" var="item">
			<tr target="sid_transfer" rel="${item.transferId }">
				<td>${item.transferId }</td>
				<td>${item.warehouseName }</td>
				<td>${item.consignee }</td>
				<td>${item.transferTo }</td>
				<td>${item.logisticNo }</td>
				<td>
					<c:if test="${item.status=='1'}">未发货</c:if>
					<c:if test="${item.status=='2'}">已发货</c:if>
					<c:if test="${item.status=='3'}">配货中</c:if>
					<c:if test="${item.status=='4'}">已审核</c:if>
					<c:if test="${item.status=='5'}">已取消</c:if>
				</td>
				<td>${item.remark }</td>
				<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${item.handledBy }</td>
				<td>
					<c:if test="${!(item.status=='2' || item.status=='5')}">
						<a title="确实要取消吗？" target="ajaxTodo" href="${ctx}/stock/transfer!cancelTransfer.action?transferId=${item.transferId}&navTabId=tab_transfer" class="btnDel"></a>
					</c:if>
				</td>
			</tr>
			</c:forEach>
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