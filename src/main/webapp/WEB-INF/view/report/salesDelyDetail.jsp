<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="pagerForm" method="post" action="#rel#">
    <input type="hidden" name="page.currentPage" value="1"/>
    <input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
</form>

<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/deliveryReport!listSalesDelyDetail.action" method="post">
        <input type="hidden" name="stockCheckId" value="${stockCheckId }"/>
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>发货开始时间：</label>
                    <input type="text" name="finishedTimeBegin" class="date" size="15" dateFmt="yyyy-MM-dd" value="<fmt:formatDate value="${finishedTimeBegin }" pattern="yyyy-MM-dd"/>"/>
                </li>
                <li>
                    <label>发货截止时间：</label>
                    <input type="text" name="finishedTimeEnd" class="date" size="15" dateFmt="yyyy-MM-dd" value="<fmt:formatDate value="${finishedTimeEnd }" pattern="yyyy-MM-dd"/>"/>
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
                <a class="icon" href="${ctx}/report/deliveryReport!listSalesDelyDetail.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a>
            </li>
        </ul>
    </div>
    <table class="list" width="100%" layoutH="115">
        <thead>
        <tr align="center">
            <th>订单来源</th>
            <th>发货日期</th>
            <th>订单号</th>
            <th>付款方式</th>
            <th>交易号</th>
            <th>SKU名称</th>
            <th>数量</th>
            <th>单价</th>
            <th>发票金额</th>
            <th>收货人</th>
            <th>快递类型</th>
            <th>运单号</th>
            <th>SAP物料编码</th>
            <th>商品类型</th>
            <th>发出地</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="deliveryDetails" status="status" var="b">
            <tr target="sid_user" rel="${status.index }">
                <td>
                    <s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#b.orderSource}[0].name"/></td>
                <td><fmt:formatDate value="${b.deliveryDate}" pattern="yyyy-MM-dd"/></td>
                <td>${orderCode}</td>
                <td>${payment}</td>
                <td>${payNo}</td>
                <td>${skuName}</td>
                <td>${quantity}</td>
                <td>${price}</td>
                <td>${invoiceAmount}</td>
                <td>${consignee}</td>
                <td>${expressType}</td>
                <td>${expressNo}</td>
                <td>${materialCode}</td>
                <td>${goodsType}</td>
                <td>${departure}</td>
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