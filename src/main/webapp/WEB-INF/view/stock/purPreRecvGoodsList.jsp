<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="pageContent">
    <table class="list" width="100%" style="height: 100%;">
        <thead>
        <tr align="center">
            <th>采购编号</th>
            <th>过账凭证号</th>
            <th>SKU编码</th>
            <th>SKU名称</th>
            <th>数量</th>
            <th>计量单位</th>
            <th>生产批次号</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="goodsList" status="status">
            <tr target="sid_user" rel="${status.index }" align="center">
                <td>${purPreRecv.purchaseCode }</td>
                <td>${purPreRecv.postingNo }</td>
                <td>${skuCode }</td>
                <td>${skuName }</td>
                <td>${quantity }</td>
                <td>${measureUnit }</td>
                <td>${productBatchNo }</td>
                <td>
                    <s:if test="indivEnabled==1">
                        <a title="查看商品编码" target="dialog" rel="dlg_purPreRecvListIndiv" mask="true" width="800" height="600" href="${ctx}/stock/purPreRecv!listIndiv.action?id=${id}" class="btnView"></a>
                    </s:if>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</div>