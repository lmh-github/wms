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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/purPreRecv!list.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>仓库：</label>
				<s:select name="warehouseId" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/>
			</li>
			<li>
				<label>过账凭证号：</label>
				<input type="text" name="postingNo" value="${postingNo}"/>
			</li>
			<li>
				<label>采购编号：</label>
				<input type="text" name="purchaseCode" value="${purchaseCode}"/>
			</li>
			<li>
				<label>制单开时间：</label>
				<input type="text" name="preparedTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${preparedTimeBegin }" pattern="yyyy-MM-dd"/>"/>
            </li>
            <li>
                <label>制单截止间：</label>
				<input type="text" name="preparedTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${preparedTimeEnd }" pattern="yyyy-MM-dd"/>"/>
			</li>
			<li>
				<label>处理状态：</label>
				<s:select name="handlingStatus" list="@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
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
			<li><a class="edit" href="${ctx}/stock/purPreRecv!listGoods.action?id={sid_purchase}" target="dialog" rel="dlg_purPreRecvListGoods" mask="true" warn="请选择采购单"><span>查看商品清单</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" href="${ctx}/stock/purPreRecv!inputUploadGoods.action" rel="dlg_purPreRecvGoods" target="dialog" mask="true" width="800" height="600"><span>上传采购数据</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="118">
		<thead>
			<tr align="center">
				<th>制单时间</th>
				<th>预收编号</th>
				<th>过账凭证号</th>
				<th>采购编号</th>
				<th>采购时间</th>
				<th>采购人</th>
				<th>仓库</th>
				<th>供应商</th>
				<th>处理状态</th>
				<th>处理时间</th>
				<th>处理人</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="preRecvList" status="status">
			<tr target="sid_purchase" rel="${id }" align="center">
				<td><fmt:formatDate value="${preparedTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${preRecvCode }</td>
				<td>${postingNo }</td>
				<td>${purchaseCode }</td>
				<td><fmt:formatDate value="${purPreparedTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${purPreparedBy }</td>
				<td>${warehouseName }</td>
				<td>${supplierName }</td>
				<td>
					<s:if test="handlingStatus==@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@UNRECEIVED.code"><s:property value="@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@UNRECEIVED.name"/></s:if>
					<s:elseif test="handlingStatus==@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@RECEIVING.code">
						<s:property value="@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@RECEIVING.name"/>
						(<a title="收货确认" target="navTab" rel="tab_purchaseRecv" href="${ctx}/stock/purchaseRecv.action?receiveCode=${receiveCode }">${receiveCode }</a>)
					</s:elseif>
					<s:elseif test="handlingStatus==@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@RECEIVED.code">
						<s:property value="@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@RECEIVED.name"/>
						(<a title="查看收货单" target="dialog" mask="true" width="1024" height="700" rel="dlg_purchaseRecvInput" href="${ctx}/stock/purchaseRecv!input.action?id=${receiveId}">${receiveCode }</a>)
					</s:elseif>
					<s:elseif test="handlingStatus==@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@CANCELED.code"><s:property value="@com.gionee.wms.common.WmsConstants$PurchaseOrderStatus@CANCELED.name"/></s:elseif>
				</td>
				<td><fmt:formatDate value="${handledTime }" pattern="yyyy-MM-dd"/></td>
				<td>${handledBy }</td>
				<td>${remark }</td>
				<td>
					<s:if test="handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@UNRECEIVED.code">
					<a title="确定要根据当前采购预收单创建收货单吗？" target="ajaxTodo" href="${ctx}/stock/purPreRecv!createReceive.action?id=${id}" class="btnAdd"></a>
					</s:if>
				</td>
			</tr>
			</s:iterator>
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