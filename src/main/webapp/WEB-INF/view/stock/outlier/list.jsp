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
    <form rel="pagerForm" id="fm${rand}" onsubmit="return navTabSearch(this);" action="${ctx}/stock/outlier/list.do" method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>生效时间：</label>
                    <input type="text" name="beginTime_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${beginTime }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>失效时间：</label>
                    <input type="text" name="endTime_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${endTime }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>规则名称：</label>
                    <input type="text" name="name" value="${name}"/>
                </li>
                <li>
                    <label>订单来源：</label>
                    <s:select name="source" value="%{#attr.source}" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
                </li>
                <li>
                    <label>是否开启：</label>
                    <select name="logSwitch">
                        <option value="">请选择</option>
                        <option value="0" ${(logSwitch == '0')?"selected='true'":""}>已开启</option>
                        <option value="1" ${(logSwitch == '1')?"selected='true'":""}>未开启</option>
                    </select>
                </li>
                <li>
                    <label>sku：</label>
                    <input type="text" name="sku" value="${sku}"/>
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
            <li>
                <a class="add" href="${ctx}/stock/outlier/to.do?to=add" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="650"><span>新建预警规则</span></a>
            </li>

        </ul>
    </div>
    <table class="list" style="width: 100%;white-space: nowrap;" layoutH="115">
        <thead>
        <tr align="center">
            <th>预警名称</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>订单来源</th>
            <th>影响sku</th>
            <th>预警价格</th>
            <th>是否开启</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.list" var="b">
            <tr target="sid_outlier" rel="${id}">
                <td align="center" style="white-space: nowrap;"><s:property value="name"/></td>
                <td align="center"><fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center" style="white-space: normal">
                    <c:forEach items="${fn:split(orderSource,',')}" var="var" varStatus="status">
                        <c:set var="data" scope="request" value="${var}"/>
                        <s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#request.data}[0].name"/>
                        <c:if test="${status.last == false}">
                            ,
                        </c:if>
                    </c:forEach>
                </td>
                <td align="center" style="white-space: normal"><s:property value="skuIds"/></td>
                <td align="center" style="white-space: normal"><s:property value="unitPrice"/></td>
                <td align="center" style="white-space: normal">
                    <s:if test="logSwitch == 0">
                        已开启
                    </s:if>
                    <s:if test="logSwitch == 1">
                        未开启
                    </s:if>
                </td>
                <td align="center" style="white-space: nowrap;">
                    <a href="${ctx}/stock/outlier/to.do?to=show&id={sid_outlier}" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="350"> 查看</a>
                    <a href="${ctx}/stock/outlier/to.do?to=add&id={sid_outlier}"  target="dialog" rel="dlg_orderInput" mask="true" width="815" height="650">|修改</a>
                </td>
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
