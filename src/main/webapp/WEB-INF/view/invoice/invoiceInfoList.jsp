<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set name="invoiceTypes" value='#{"E":"电子发票", "Z":"纸质发票"}'/>
<form id="pagerForm" method="post" action="#rel#">
    <input type="hidden" name="page.currentPage" value="1"/>
    <input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/invoice/list.do" method="post">
        <input type="hidden" name="stockCheckId" value="${stockCheckId }"/>
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>开票开始时间：</label>
                    <input type="text" name="kprqBegin" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${kprqBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>开票截止时间：</label>
                    <input type="text" name="kprqEnd" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${kprqEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>订单号：</label>
                    <input type="text" name="orderCode" value="${orderCode}"/>
                </li>
                <li>
                    <label>手机号：</label>
                    <input type="text" name="mobile" value="${mobile}"/>
                </li>
                <li>
                    <label>发票号码：</label>
                    <input type="text" name="fpHm" value="${fpHm}"/>
                </li>
                <li>
                    <label>发票代码：</label>
                    <input type="text" name="fpDm" value="${fpDm}"/>
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
                <a class="edit" href="${ctx}/stock/invoiceInfo!makeEInvoice.action?orderCode={sid_order}" target="ajaxTodo" title="确定要开发电子票吗？" mask="true"><span>开具电子发票</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="edit" href="${ctx}/stock/invoiceInfo!ch.action?orderCode={sid_order}" target="ajaxTodo" title="确定要冲红电子票吗？" mask="true"><span>冲红电子发票</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="edit" href="${ctx}/stock/invoiceInfo!downPDF.action?orderCode={sid_order}" target="ajaxTodo" title="确定要及时取票吗？" mask="true"><span>手工取票</span></a>
            </li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="list" width="100%" layoutH="120">
        <thead>
        <tr align="center">
            <th>订单号</th>
            <th>手机</th>
            <th>邮箱</th>
            <th>发票代码</th>
            <th>发票号码</th>
            <th>开票类型</th>
            <th>开票状态</th>
            <th>开票时间</th>
            <th>返回消息</th>
            <th>状态码</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.invoiceInfoList" status="status" var="b">
            <tr target="sid_order" rel="${orderCode }" align="center">
                <td>${orderCode }</td>
                <td>${mobile }</td>
                <td>${email }</td>
                <td>${fpDm}</td>
                <td>${fpHm}</td>
                <td><s:property value="#invoiceTypes[invoiceType]"/></td>
                <td>
                    <s:property value="@com.gionee.wms.common.WmsConstants$EInvoiceStatus@values().{?#this.toString()==#b.status}[0].text"/></td>
                <td><fmt:formatDate value="${opDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>
                    <a href="javascript:;" onclick="if(window.console){console.log($(this).next('pre').html())}alert($(this).next('pre').html());">查看</a>
                    <pre style="display: none"><s:property value="jsonData"/></pre>
                </td>
                <td>${returnCode}</td>
                <td>
                    <s:if test="previewImgUrl != null">
                        <a href="${ctx}/${previewImgUrl}" target="_blank">预览图片</a>&nbsp;|&nbsp;<a href="${ctx}/${pdfUrl}" target="_blank">发票文件</a>
                    </s:if>
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