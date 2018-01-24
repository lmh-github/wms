<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle">平台库存分配</h2>
<form id="pagerForm" name="purchaseInForm" action="${ctx}/stock/purchaseRecv!confirmReceive.action?id=${id }&callbackType=closeCurrent&navTabId=tab_purchaseRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">

    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>库存信息</legend>
                <dl>
                    <dt>SKU：</dt>
                    <dd>${sku.skuCode}</dd>
                </dl>
                <dl >
                    <dt>SKU名称：</dt>
                    <dd>${sku.skuName}</dd>
                </dl>
                <dl>
                    <dt>计量单位：</dt>
                    <dd>${wares.measureUnit}</dd>
                </dl>
                <dl class="">
                    <dt>可销售库存：</dt>
                    <dd>${stock.salesQuantity}</dd>
                </dl>
                <dl class="">
                    <dt>已分配库存：</dt>
                    <dd>${totalUseStore}</dd>
                </dl>
                <dl class="">
                    <dt>可分配库存：</dt>
                    <dd>${unuseStore}</dd>
                </dl>
            </fieldset>
            <s:set name="auditStatus" value="check.auditStatus"></s:set>
            <h3 class="contentTitle">平台库存分配列表</h3>
            <div class="panelBar">
                <ul class="toolBar">
                    <li><a class="add" href="${ctx}/store/platform/toAdd.do?skuNo=${sku.skuCode}" rel="dlg_addPlatformStore" target="dialog" mask="true" width="800" height="400"><span>添加库存分配</span></a></li>
                    <li class="line">line</li>
                    <li><a class="icon" href="${ctx}/store/platform/export.do?skuNo=${sku.skuCode}&navTabId=tab_store_platform" target="dwzExport" targettype="navTab"><span>导出数据到excel</span></a></li>
                </ul>
            </div>

            <div class="tabs">
                <div class="tabsContent" style="height: 280px;">
                    <div>
                        <table class="list" width="100%">
                            <thead>
                            </tr>
                                <th>SKU编码</th>
                                <th>平台编号</th>
                                <th>平台名称</th>
                                <th>库存</th>
                                <th>设置人</th>
                                <th>最新设置时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="">
                           <%-- <s:iterator value="list">
                                <tr class="unitBox">
                                    <td>${skuCode }</td>
                                    <td>${platformNo }</td>
                                    <td>${platformName }</td>
                                    <td>${totalNum}</td>
                                    <td>${createBy }</td>
                                    <td><fmt:formatDate value='${updateDate }'   pattern='yyyy-MM-dd' /></td>
                                    <td>
                                        <a title="删除库存分配" target="ajaxTodo" href="${ctx}/store/platform/delete.json?id=${id}&navTabId=tab_store_platform" class="btnDel"></a>
                                        <a title="修改库存分配" target="dialog" rel="dlg_editPlatformStore" maxable="false" resizable="false" mask="true" width="600" height="500" warn="修改库存分配" href="${ctx}/store/platform/toUpdate.do?id=${id}" class="btnEdit"></a>
                                    </td>
                                </tr>
                            </s:iterator>--%>
                           <c:forEach items="${list}" var="item">
                               <tr class="unitBox">
                                   <td>${item.skuNo }</td>
                                   <td>${item.platformNo }</td>
                                   <td>${item.platformName }</td>
                                   <td>${item.totalNum}</td>
                                   <td>${item.createBy }</td>
                                   <td><fmt:formatDate value='${item.updateDate }'   pattern='yyyy-MM-dd' /></td>
                                   <td>
                                       <a title="确认删除库存分配？" target="ajaxTodo" href="${ctx}/store/platform/delete.json?id=${item.id}&navTabId=tab_store_platform" class="btnDel"></a>
                                       <a title="修改库存分配" target="dialog" rel="dlg_editPlatformStore" maxable="false" resizable="false" mask="true" width="800" height="400" warn="修改库存分配" href="${ctx}/store/platform/toUpdate.do?id=${item.id}" class="btnEdit"></a>
                                   </td>
                               </tr>
                           </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tabsFooter">
                    <div class="tabsFooterContent"></div>
                </div>
            </div>
        </div>
    </div>
</form>
<script>
</script>