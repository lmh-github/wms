<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" /> <input
		type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/purchaseRecv.action" method="post">
	<div class="searchBar">
		<ul class="searchContent" style="height: 50%;">
			<li>
				<label>仓库：</label>
				<s:select name="warehouseId" list="warehouseList" listKey="id" listValue="warehouseName" headerValue="请选择" headerKey=""/>
			</li>
			<li>
				<label>收货编号：</label>
				<input type="text" name="receiveCode" value="${receiveCode}"/>
			</li>
			<li>
				<label>采购编号：</label>
				<input type="text" name="purchaseCode" value="${purchaseCode}"/>
			</li>
			<li style="width:380px;">
				<label>制单时间从：</label>
				<input type="text" name="preparedTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${preparedTimeBegin }" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="preparedTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${preparedTimeEnd }" pattern="yyyy-MM-dd"/>"/>
			</li>
			<li>
				<label>处理状态：</label>
				<s:select name="handlingStatus" list="@com.gionee.wms.common.WmsConstants$ReceiveStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>采购类型：</label>
				<select name="receiveType">
					<option value="101" ${receiveType=='101'?"selected='true'":""}><s:property value="@com.gionee.wms.common.WmsConstants$ReceiveType@PURCHASE.name"/></option>
					<option value="105" ${receiveType=='105'?"selected='true'":""}><s:property value="@com.gionee.wms.common.WmsConstants$ReceiveType@PURCHARMA.name"/></option>
					<option value="106" ${receiveType=='106'?"selected='true'":""}><s:property value="@com.gionee.wms.common.WmsConstants$ReceiveType@SHUADAN.name"/></option>
				</select>
			</li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
			</ul>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" target="dialog" mask="true" width="800"
				height="600" href="${ctx}/stock/purchaseRecv!inputNew.action"><span>创建收货单</span></a></li>
			<li class="line">line</li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="140">
		<thead>
			<tr align="center">
				<th>制单时间</th>
				<th>收货编号</th>
				<th>仓库</th>
				<th>供应商</th>
				<th>制单人</th>
				<th>采购编号</th>
				<th>收货方式</th>
				<th>处理状态</th>
				<th>处理时间</th>
				<th>处理人</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="receiveList" status="status">
				<tr target="sid_receive" rel="${id }" align="center">
					<td><fmt:formatDate value="${preparedTime }"
							pattern="yyyy-MM-dd" /></td>
					<td>${receiveCode }</td>
					<td>${warehouseName }</td>
					<td>${supplierName }</td>
					<td>${preparedBy }</td>
					<td>${originalCode }</td>
					<td><s:if
							test="receiveMode==@com.gionee.wms.common.WmsConstants$ReceiveMode@AUTO.code">
							<s:property
								value="@com.gionee.wms.common.WmsConstants$ReceiveMode@AUTO.name" />
						</s:if> <s:elseif
							test="receiveMode==@com.gionee.wms.common.WmsConstants$ReceiveMode@MANUAL.code">
							<s:property
								value="@com.gionee.wms.common.WmsConstants$ReceiveMode@MANUAL.name" />
						</s:elseif></td>
					<td><s:if
							test="handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@UNRECEIVED.code">
							<s:property
								value="@com.gionee.wms.common.WmsConstants$ReceiveStatus@UNRECEIVED.name" />
						</s:if> <s:elseif
							test="handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
							<s:property
								value="@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.name" />
						</s:elseif> <s:elseif
							test="handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVED.code">
							<s:property
								value="@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVED.name" />
						</s:elseif> <s:elseif
							test="handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@CANCELED.code">
							<s:property
								value="@com.gionee.wms.common.WmsConstants$ReceiveStatus@CANCELED.name" />
						</s:elseif></td>
					<td><fmt:formatDate value="${handledTime }"
							pattern="yyyy-MM-dd" /></td>
					<td>${handledBy }</td>
					<td>${remark }</td>
					<td><s:if
							test="handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
							<s:if
								test="receiveMode==@com.gionee.wms.common.WmsConstants$ReceiveMode@AUTO.code">
								<a title="收货确认" target="dialog" mask="true" width="1024"
									height="700" rel="dlg_purchaseRecvInput"
									href="${ctx}/stock/purchaseRecv!input.action?id=${id}"
									class="btnEdit"></a>
							</s:if>
							<s:elseif
								test="receiveMode==@com.gionee.wms.common.WmsConstants$ReceiveMode@MANUAL.code">
								<a title="收货确认" target="dialog" mask="true" width="1024"
									height="700" rel="dlg_purchaseRecvInputManual"
									href="${ctx}/stock/purchaseRecv!inputManual.action?id=${id}"
									class="btnEdit"></a>
							</s:elseif>
						</s:if> <s:if
							test="handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVED.code">
							<a title="查看" target="dialog" mask="true" width="1024"
								height="700" rel="dlg_purchaseRecvInput"
								href="${ctx}/stock/purchaseRecv!input.action?id=${id}"
								class="btnView"></a>
						</s:if></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span> <select class="combox" name="numPerPage"
				onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select> <span>条，共${page.totalRow}条</span>
		</div>

		<div class="pagination" targetType="navTab"
			totalCount="${page.totalRow }" numPerPage="${page.pageSize }"
			pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>