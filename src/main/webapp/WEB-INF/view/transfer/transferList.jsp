<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<s:set var="TransferStatus" value="@com.gionee.wms.common.WmsConstants$TransferStatus@values()"/>
<form id="pagerForm" method="post" action="#rel#">
    <input type="hidden" name="page.currentPage" value="1"/>
    <input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
</form>
<div class="pageHeader">
    <form id="fm${rand}" rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/trans/list.do" method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>收货地址：</label>
                    <input type="text" name="transferTo" value="${transferTo}"/>
                </li>
                <li>
                    <label>调货批次号：</label>
                    <input type="text" name="transferId" value="${transferId}"/>
                </li>
                <li>
                    <label>调货状态：</label>
                    <select name="status">
                        <option value="">请选择</option>
                        <option value="1" ${(1==status)?"selected='true'":""}>未发货</option>
                        <option value="2" ${(2==status)?"selected='true'":""}>已发货</option>
                    </select>
                </li>
                <li>
                    <label>物流单号：</label>
                    <input type="text" name="logisticNo" value="${logisticNo}"/>
                </li>
                <li>
                    <label>变动开始时间：</label>
                    <input type="text" name="createTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>变动截止时间：</label>
                    <input type="text" name="createTimeEnd_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
            <li>
                <a class="add" href="${ctx}/trans/toAddAndEdit.do" target="dialog" mask="true" width="800" height="600"><span>添加调拨单</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="edit" href="${ctx}/trans/toAddAndEdit.do?transferId={sid_transfer}" target="dialog" rel="dlg_transfer_addAndEdit" mask="true" width="800" height="600"><span>查看编辑调拨单</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="icon" href="${ctx}/stock/transfer!printTransfer.action?transferId={sid_transfer}" target="navTab" targetType="navTab" warn="请选择调拨单"><span>打印调拨单</span></a>
            </li>
        </ul>
    </div>
    <table class="list" width="100%" layoutH="140">
        <thead>
        <tr>
            <th>调拨批次号</th>
            <th>发货仓</th>
            <th>收货人</th>
            <th>收货地址</th>
            <th>物流单号</th>
            <th>调货状态</th>
            <th>审核状态</th>
            <th>备注</th>
            <th>创建时间</th>
            <th>操作人</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${transferList}" var="item">
            <tr target="sid_transfer" rel="${item.transferId }">
                <td>${item.transferId }</td>
                <td>${item.warehouseName }</td>
                <td>${item.consignee }</td>
                <td>${item.transferTo }</td>
                <td>${item.logisticNo }</td>
                <td><s:property value="#TransferStatus.{?#this.code==#attr.item.status}[0].name"/></td>
                <td>${item.flowType }</td>
                <td>${item.remark }</td>
                <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>${item.handledBy }</td>
                <td>
                    <c:if test="${!(item.status=='2' || item.status=='5') && item.flowType !='待审核'}">
                        <a title="确实要取消吗？" target="ajaxTodo" href="${ctx}/trans/cancel.do?transferId=${item.transferId}&navTabId=tab_transfer" class="btnDel"></a>
                    </c:if>
                    <c:if test="${item.flowType =='待审核'}">
                        <a title="审核通过?" target="ajaxTodo" href="${ctx}/trans/complete.do?transferId=${item.transferId}&flowType=0&navTabId=tab_transfer" class="btnSelect"></a>
                        <a title="审核不通过?" target="ajaxTodo" href="${ctx}/trans/complete.do?transferId=${item.transferId}&flowType=1&navTabId=tab_transfer" class="btnDel"></a>
                    </c:if>
                </td>
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
</div>
<script type="text/javascript">
    (function () {
        $("#export${rand}").click(function () {
            var params = $("#fm${rand}").serialize();
            $('<iframe style="display:none" src="${ctx}/trans/export.do?' + params + '&_r=' + (new Date().getTime()) + '" onload="downLoadEvent(this)" />').appendTo("body");
        });
    })();
</script>