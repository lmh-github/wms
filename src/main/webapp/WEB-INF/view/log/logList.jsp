<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
	<input type="hidden" name="startDate" value="${startDate }" />
	<input type="hidden" name="endDate" value="${endDate }" />
	<input type="hidden" name="type" value="${log.type }" />
	<input type="hidden" name="opName" value="${log.opName }" />
	<input type="hidden" name="content" value="${log.content }" />
	<input type="hidden" name="opUserName" value="${log.opUserName }" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/log/log.action" method="post">
	<div class="searchBar">
		<ul class="searchContent" style="height: 50px;">
			<li>
				<label style="width:100px">开始时间：</label>
				<input type="text" name="startDate" value="${startDate}" class="date textInput"/>
				<!-- <a class="inputDateButton" href="javascript:;">选择</a> -->
			</li>
			<li>
				<label style="width:100px">结束时间：</label>
				<input type="text" name="endDate" value="${endDate}" class="date textInput"/>
				<!-- <a class="inputDateButton" href="javascript:;">选择</a> -->
			</li>

			<li>
				<label style="width:100px">日志类型：</label>
				<!--  
				<input type="text" name="log.type" value="${log.type }"/>
				-->
                <select  name="log.type">
                    <option value="0" <s:if test='log.type == 0'>selected="selected"</s:if>>所有日志</option>
                    <option value="1" <s:if test='log.type == 1'>selected="selected"</s:if>>操作日志</option>
                    <option value="2" <s:if test='log.type == 2'>selected="selected"</s:if>>业务日志</option>
				</select>
			</li>
			<li>
				<label style="width:100px">操作名：</label>
				<input type="text" name="log.opName" value="${log.opName}"/>
			</li>
			<li>
				<label style="width:100px">操作内容：</label>
				<input type="text" name="log.content" value="${log.content}"/>
			</li>
			<li>
				<label style="width:100px">操作用户：</label>
		
				<input type="text" name="log.opUserName" value="${log.opUserName}"/>
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
			<li>
			</li>
		</ul>
	</div>
	
	<table class="list" width="100%" layoutH="140">
		<thead>
			<tr>
			    <th style="width: 100px;">编号</th>
				<th style="width: 100px;">日志类型</th>
				<th style="width: 160px;">操作名</th>			
				<th>操作内容</th> 
				<th style="width: 110px;">操作IP</th>
				<th style="width: 100px;">操作用户</th>
				<th style="width: 130px;">操作时间</th>
			</tr>
		</thead>
		<tbody id="tbody${rand}">
			<c:forEach items="${logList}" var="item">
			<tr target="sid_user" rel="1">
			    <td>${item.id }</td>
				<td><c:if test='${item.type == 1}'>操作日志</c:if>
				    <c:if test='${item.type == 2}'>业务日志</c:if>
				</td>
				<td>${item.opName }</td>
				<td><a class="tooltip"  href="javascript:;">${item.shortContent}</a><textarea style="width:100%;height: 100px;padding: 0;resize:vertical; display: none;" readonly="readonly">${item.content}</textarea></td> 
				<td>${item.ip }</td>
				<td>${item.opUserName }</td>
				<td><fmt:formatDate value="${item.opTime}"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
<script type="text/javascript">
$(function() {
	$("#tbody${rand}").on("click", "a.tooltip", function() {
		$(this).next(":input").toggle();
	});
})
</script>
</div>
<div id='tooltip'></div>