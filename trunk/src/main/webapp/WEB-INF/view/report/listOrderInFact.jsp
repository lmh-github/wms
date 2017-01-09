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

	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/listOrderInFact!listPurRecvSummary.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
    		<ul class="searchContent" style="height: 30px;">
    			<li style="width:380px;">
    				<label>支付时间从：</label>
    				<input type="text" name="createTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
    				到：
    				<input type="text" name="createTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
    			</li>
    			<!--<li>
    				<label>订单状态：</label>
    				<s:select name="orderStatus" list="@com.gionee.wms.common.WmsConstants$OrderStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
    			</li>-->
    			<li>
				<label>订单来源：</label>
					<s:if test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==1">
						<s:select name="orderStatus" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
					</s:if><s:elseif test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==2">
						<s:select name="orderStatus" list="@com.gionee.wms.common.WmsConstants$OrderSourceIuni@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
					</s:elseif>
				</li>
    			<li>
    				<label>订单号：</label>
    				<input type="text" name="orderCode" value="${orderCode}"/>
    			</li>
    			<li>
    				<label>SKU名称：</label>
    				<input type="text" name="skuCode" value="${skuCode}"/>
    			</li>
    			<li>
    				<label>SKU编码：</label>
    				<input type="text" name="skuName" value="${skuName}"/>
    			</li>
    		</ul>
    		<div class="subBar">
    			<ul>
    				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
    			    <li><div class="buttonActive"><a href="${ctx}/report/listOrderInFact!listPurRecvSummary.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></div></li>

    			</ul>
    		</div>
    	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
        			<tr align="center">
        				<th>序号</th>
        				<th>订单号</th>
        				<th>订单来源(平台)</th>
        				<th>订单时间</th>
        				<th>支付时间</th>
        				<th>筛单时间</th>
        				<th>已打单时间</th>
        				<th>已出库时间</th>
        				<th>已签收时间</th>

                        <th>已筛单-已打单时长</th>
                        <th>已打单-已出库</th>
                        <th>合计物流时长</th>
                        <th>订单时长</th>

        			</tr>
        		</thead>
		<tbody>
        			<s:iterator value="orderInFactList" status="status"  var="b">
        			<tr target="sid_user" rel="${status.index }" >
        				<td>${status.index + 1 }</td>
                        <td>${ORDER_CODE }</td>

                        <!--<td>${ORDERSTATUS }</td>-->
						<td>
                   			<s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#b.ORDERSTATUS}[0].name"/>
						</td>

        				<td><fmt:formatDate value="${ORDER_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        				<td><fmt:formatDate value="${PAYFOR_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        				<td ><fmt:formatDate value="${SHAIDAN_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        				<td ><fmt:formatDate value="${DADAN_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        				<td ><fmt:formatDate value="${CHUKU_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        				<td ><fmt:formatDate value="${QIANSHOU_TIME}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        				<td >${SHAIDAN_DADAN }</td>
        				<td >${DADAN_CHUKU }</td>
        				<td >${HEJI_TIME }</td>
        				<td >${DINGDAN_TIME }</td>
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