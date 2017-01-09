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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/back.action" method="post">
	<div class="searchBar">
		<ul class="searchContent">
			<li style="width:380px;">
				<label>退货时间从：</label>
				<input type="text" name="backTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${backTimeBegin}" pattern="yyyy-MM-dd"/>"/>
				到：
				<input type="text" name="backTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${backTimeEnd}" pattern="yyyy-MM-dd"/>"/>
			</li>
			<li>
				<label>退货状态：</label>
				<s:select name="backStatus" cssStyle="width: 133px" list="@com.gionee.wms.common.WmsConstants$BackStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>  
			</li>
			<li>
				<label>退货单号：</label>
				<input type="text" name="backCode" value="${backCode}"/>
			</li>
		</ul>
		<ul class="searchContent">
			<li style="width:380px;">
				<label>订单号：</label>
				<input type="text" name="orderCode" value="${orderCode}" style="width: 190px"/>
			</li>
			<li>
				<label>运单号：</label>
				<input type="text" name="shippingNo" value="${shippingNo}"/>
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
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>退货单号</th>
				<th>订单号</th>
				<th>快递类型</th>
				<th>运单号</th>
				<th>退货请求备注</th>
				<th>退货时间</th>
				<th>已退货备注</th>
				<th>操作人</th>
				<th>操作时间</th>
				<th>退货状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="backList" status="status">
			<tr target="sid_order" rel="${id }" align="center">
				<td>${backCode}</td>
				<td>${orderCode}</td>
				<td>${shippingCode}</td>
				<td>${shippingNo}</td>
				<td>${remarkBacking}</td>
				<td><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${remarkBacked}</td>
				<td>${handledBy}</td>
				<td><fmt:formatDate value="${handledTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<s:if test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@BACKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@BACKING.name"/></s:if>
					<s:elseif test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@BACKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@BACKED.name"/></s:elseif>
					<s:elseif test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@CANCELED.code"><s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@CANCELED.name"/></s:elseif>
				</td>
				<td>
					<s:if test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@BACKING.code">
					<a target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/back!inputInit.action?backId=${id }"><span>退货</span></a>	
					</s:if>
					<s:elseif test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@BACKED.code">
					<a target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/back!inputInit.action?backId=${id }"><span>查看</span></a>
					</s:elseif>
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