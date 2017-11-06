<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<form id="pagerForm" method="post" action="#rel#">
    <input type="hidden" name="page.currentPage" value="1"/>
    <input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" id="fm${rand}" onsubmit="return navTabSearch(this);" action="${ctx}/outlierlog/list.do" method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>订单开始时间：</label>
                    <input type="text" name="beginHandledTime_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${beginHandledTime }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>订单结束时间：</label>
                    <input type="text" name="endHandledTime_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${endHandledTime }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>订单来源：</label>
                    <s:select name="orderSource" value="%{#attr.orderSource}" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
                </li>
                <li>
                    <label>sku：</label>
                    <input type="text" name="skuCode" value="${skuCode}"/>
                </li>
                <li>
                    <label>订单号：</label>
                    <input type="text" name="orderCode" value="${orderCode}"/>
                </li>
            </ul>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">检索</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
        </ul>
    </div>
    <table class="list" style="width: 100%;white-space: nowrap;" layoutH="115">
        <thead>
        <tr align="center">
            <th>订单号</th>
            <th>sku编码</th>
            <th>订单来源</th>
            <th>规则名称</th>
            <th>处理时间</th>
            <th>预警单价</th>
            <th>订单单价</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.list" var="b">
            <tr target="sid_activity" rel="${id}">
                <td align="center" style="white-space: nowrap;"><s:property value="orderCode"/></td>
                <td align="center" style="white-space: nowrap;"><s:property value="skuCode"/></td>
                <td align="center" style="white-space: nowrap;">
                    <s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#b.orderSource}[0].name"/>
                </td>
                <td align="center" style="white-space: nowrap;"><s:property value="orderOutlier.name"/></td>
                <td align="center"><fmt:formatDate value="${handledTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td align="center" style="white-space: nowrap;"><s:property value="minUnitPrice"/></td>
                <td align="center" style="white-space: nowrap;"><s:property value="unitPrice"/></td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
    <div class="panelBar">
        <div class="pages">
            <span>显示</span>
            <select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="1"
                ${page.pageSize==1?"selected='true'":""}>1
                </option>
                <option value="50"
                ${page.pageSize==50?"selected='true'":""}>50
                </option>
                <option value="100"
                ${page.pageSize==100?"selected='true'":""}>100
                </option>
                <option value="200"
                ${page.pageSize==200?"selected='true'":""}>200
                </option>
            </select>
            <span>条，共${page.totalRow}条</span>
        </div>
        <div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>
    </div>
</div>
