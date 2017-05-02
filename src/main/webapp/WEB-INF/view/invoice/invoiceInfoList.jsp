<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set name="invoiceTypes" value='#{"E":"电子发票", "Z":"纸质发票"}'/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<form id="pagerForm" method="post" action="#rel#">
    <input type="hidden" name="page.currentPage" value="1"/>
    <input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
</form>
<div class="pageHeader">
    <form id="fm${rand}" rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/invoice/list.do" method="post">
        <input type="hidden" name="stockCheckId" value="${stockCheckId }"/>
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>开票开始时间：</label>
                    <input type="text" name="opDateBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${opDateBegin }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>开票截止时间：</label>
                    <input type="text" name="opDateEnd_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${opDateEnd }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
                    <label>开票状态：</label>
                    <select name="status">
                        <option value="">全部</option>
                        <s:iterator value="%{@com.gionee.wms.common.WmsConstants$EInvoiceStatus@values()}" var="e">
                            <option value="<s:property value="#e.toString()"/>"
                                <s:property value="#attr.status == #e.toString() ? 'selected':''"/> >
                                <s:property value="#e.text"/></option>
                        </s:iterator>
                    </select>
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
                <a class="edit" href="${ctx}/invoice/makeEInvoice.do?orderCode={sid_order}" target="ajaxTodo" title="确定要开发电子票吗？" mask="true"><span>开具电子发票</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="edit" href="${ctx}/invoice/chEInvoice.do?orderCode={sid_order}" target="ajaxTodo" title="确定要冲红电子票吗？" mask="true"><span>冲红电子发票</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="edit" href="${ctx}/invoice/downPDF.do?orderCode={sid_order}" target="ajaxTodo" title="确定要及时取票吗？" mask="true"><span>手工取票</span></a>
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
            <th>流水号</th>
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
                    <s:if test="status == 'RED'">
                        <span style="background:red;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="@com.gionee.wms.common.WmsConstants$EInvoiceStatus@values().{?#this.toString()==#b.status}[0].text"/></span>
                    </s:if>
                    <s:elseif test="status == 'SUCCESS'">
                        <span style="background:green;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="@com.gionee.wms.common.WmsConstants$EInvoiceStatus@values().{?#this.toString()==#b.status}[0].text"/></span>
                    </s:elseif>
                    <s:else>
                        <s:property value="@com.gionee.wms.common.WmsConstants$EInvoiceStatus@values().{?#this.toString()==#b.status}[0].text"/>
                    </s:else>
                </td>
                <td><fmt:formatDate value="${opDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>
                    <a href="javascript:;" onclick="if(window.console){console.log($(this).next('pre').html())}alert($(this).next('pre').html());">查看</a>
                    <pre style="display: none"><s:property value="jsonData"/></pre>
                </td>
                <td>${returnCode}</td>
                <td>
                    <div style="padding-bottom:3px;">${kpLsh}</div>
                    <div style="padding-bottom: 3px;color: red;">${chLsh}</div>
                </td>
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
<script type="text/javascript">
    (function () {
        $("#export${rand}").click(function () {
            var params = $("#fm${rand}").serialize();
            $('<iframe style="display:none" src="${ctx}/invoice/export.do?' + params + '&_r=' + (new Date().getTime()) + '" onload="downLoadEvent(this)" />').appendTo("body");
        });
    })();
</script>