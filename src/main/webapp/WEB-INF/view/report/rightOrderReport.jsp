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
    <form rel="pagerForm" id="fm${rand}" onsubmit="return navTabSearch(this);" action="${ctx}/report/list.do" method="post">
        <input type="hidden" name="stockCheckId" value="${stockCheckId }"/>
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>支付开始时间：</label>
                    <input type="text" name="paymentTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${paymentTimeBegin }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>支付截止时间：</label>
                    <input type="text" name="paymentTimeEnd_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${paymentTimeEnd }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>订单来源：</label>
                    <s:select name="orderSource" value="%{#request.orderSource}" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
                </li>
                <li>
                    <label>订单号：</label>
                    <input type="text" name="orderCode" value="${orderCode}"/>
                </li>
                <li>
                    <label>SKU编码：</label>
                    <input type="text" name="skuCode" value="${skuCode}"/>
                </li>
                <li>
                    <label>签收开始时间：</label>
                    <input type="text" name="finishTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${finishTimeBegin }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>签收截止时间：</label>
                    <input type="text" name="finishTimeEnd_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${finishTimeEnd }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
                    <li>
                        <div class="buttonActive" id="export${rand}">
                            <a href="javascript:;"><span>导出excel</span></a>
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
            <li></li>
        </ul>
    </div>
    <table class="list" style="width: 2500px;" layoutH="115">
        <thead>
        <tr align="center">
            <th>订单号</th>
            <th>目标发货仓</th>
            <th>实际发货仓</th>
            <th>省</th>
            <th>市</th>
            <th>区</th>
            <th>订单来源</th>
            <th>下单时间</th>
            <th>支付时间</th>
            <th>流入WMS时间</th><!-- 流入WMS时间 -->
            <th>推单时间</th><!-- 推单时间 -->
            <th>出库时间</th>
            <th>签收时间</th>
            <th>推单耗时</th>
            <th>顺丰发货操作耗时</th>
            <th>顺丰物流耗时</th>
            <th>顾客购机耗时</th>
            <th>机型</th>
            <th>收货信息</th>
            <th>订单总金额</th>
            <th>物流公司</th>
            <th>物流单号</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.list" status="status" var="b">
            <tr target="sid_user">
                <td align="center"><s:property value="orderCode"/></td>
                <td align="center"><s:property value="targetWarehouse"/></td>
                <td align="center"><s:property value="realWarehouse"/></td>
                <td align="center"><s:property value="province"/></td>
                <td align="center"><s:property value="city"/></td>
                <td align="center"><s:property value="district"/></td>
                <td align="center"><s:property value="orderSourceName"/></td>
                <td align="center"><fmt:formatDate value="${orderTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><fmt:formatDate value="${paymentTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><fmt:formatDate value="${joinTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><fmt:formatDate value="${orderPushTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><fmt:formatDate value="${orderSendTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><fmt:formatDate value="${orderFinishTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><s:property value="filterToPrintTime"/></td>
                <td align="center"><s:property value="printToSendTime"/></td>
                <td align="center"><s:property value="totalUseTime"/></td>
                <td align="center"><s:property value="orderUseTime"/></td>
                <td align="center">
                    <s:iterator value="%{orderSkuInfo.split('\\n')}" var="sku">
                        <s:property value="sku"/><br/>
                    </s:iterator>
                </td>
                <td align="center"><s:property value="address"/></td>
                <td align="center"><s:property value="orderAmount"/></td>
                <td align="center"><s:property value="express"/></td>
                <td align="center"><s:property value="expressNo"/></td>
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
<script type="text/javascript">
    (function () {
        $("#export${rand}").click(function () {
            var params = $("#fm${rand}").serialize();
            $('<iframe style="display:none" src="${ctx}/report/export.do?' + params + '&_r=' + (new Date().getTime()) + '" onload="downLoadEvent(this)" />').appendTo("body");
        });
    })();
</script>