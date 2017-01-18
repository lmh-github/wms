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
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/kuaidiReport!listKuaidi.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>快递公司：</label>
				<input type="text" name="company" value="${company}"/>
			</li>
			<li>
				<label>快递单号：</label>
				<input type="text" name="shippingNo" value="${shippingNo}"/>
			</li>
			<li>
				<label>订单号：</label>
				<input type="text" name="orderCode" value="${orderCode}"/>
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
			<li></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>快递单号</th>
				<th>快递公司</th>
				<th>订单号</th>
				<th>收件地址</th>
				<th>订阅结果</th>
				<th>轮询状态</th>
				<th>收件状态</th>
				<th>最后推送时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="shippingInfoList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td>${shippingNo }<a title="查看推送数据" href="${ctx}/report/kuaidiReport!kuaidiDetail.action?id=${id}"  target="dialog" rel="dia_kuaidiDetail" class="btnView"></td>
				<td>${company }</td>
				<td>${orderCode }</td>
				<td>${toAddr }</td>
				<td>
					<s:if test="subscribeResult==@com.gionee.wms.common.WmsConstants$KuaidiStatus@UNSUB.code">未订阅</s:if>
					<s:elseif test="subscribeResult==@com.gionee.wms.common.WmsConstants$KuaidiStatus@SUCCESS.code">成功</s:elseif>
					<s:elseif test="subscribeResult==@com.gionee.wms.common.WmsConstants$KuaidiStatus@REFUSE.code">拒绝</s:elseif>
					<s:elseif test="subscribeResult==@com.gionee.wms.common.WmsConstants$KuaidiStatus@NOTSUPPORT.code">不支持</s:elseif>
					<s:elseif test="subscribeResult==@com.gionee.wms.common.WmsConstants$KuaidiStatus@NOTAUTH">未授权</s:elseif>
					<s:elseif test="subscribeResult==@com.gionee.wms.common.WmsConstants$KuaidiStatus@ERROR.code">系统错误</s:elseif>
				</td>
				<td>${pushStatus }</td>
				<td>${isCheck }</td>
				<td><fmt:formatDate value="${lastPushTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="right"><a title="是否重新订阅?" href=" ${ctx}/report/kuaidiReport!reDescribe.action?id=${id}" target="ajaxTodo">重新订阅</a></td>
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