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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/saleReport.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li style="width:380px;">
				<label>出库日期从：</label>
				<input type="text" name="statTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${statTimeBegin}" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="statTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${statTimeEnd}" pattern="yyyy-MM-dd"/>"/>
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
			<li><a class="icon" href="${ctx}/report/saleReport.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>统计日期</th>
				<th>订单类型</th>
				<th>销售组织</th>
				<th>分销渠道</th>
				<th>售达方</th>
				<th>送达方</th>
				<th>开票方</th>
				<th>承运方</th>
				<th>订单原因</th>
				<th>物料编码</th>
				<th>订单数量</th>
				<th>采购订单号</th>
				<th>采购订单日期</th>
				<th>运输方式</th>
				<th>开票类型</th>
				<th>使用</th>
				<th>PO编号</th>
				<th>PO项目编号</th>
				<th>单价</th>
				<th>凭证日期</th>
				<th>工厂</th>
				<th>仓库</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="statList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td><fmt:formatDate value="${statDate}" pattern="yyyy-MM-dd"/></td>
				<td>${orderType}</td>
				<td>${saleOrg}</td>
				<td>${fxChannel}</td>
				<td>${saler}</td>
				<td>${sender}</td>
				<td>${invoicer}</td>
				<td>${shipper}</td>
				<td>${orderReason}</td>
				<td>${materialCode}</td>
				<td>${orderNum}</td>
				<td>${purchaseCode}</td>
				<td><fmt:formatDate value="${purchaseDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${shippingType}</td>
				<td>${invoiceType}</td>
				<td>${use}</td>
				<td>${poCode}</td>
				<td>${poProCode}</td>
				<td>${unitPrice}</td>
				<td><fmt:formatDate value="${postingDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${factory}</td>
				<td>${warehouse}</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
</div>