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
                <legend>基本信息</legend>
                <dl class="nowrap">
                    <dt>SKU编号：</dt>
                    <dd>111111</dd>
                </dl>
                <dl class="nowrap">
                    <dt>SKU名称：</dt>
                    <dd>测试</dd>
                </dl>
                <dl class="nowrap">
                    <dt>计量单位：</dt>
                    <dd>PC</dd>
                </dl>
                <dl class="nowrap">
                    <dt>可销售库存：</dt>
                    <dd>100</dd>
                </dl>
                <dl class="nowrap">
                    <dt>已分配库存：</dt>
                    <dd>10</dd>
                </dl>
                <dl class="nowrap">
                    <dt>剩余可分配库存：</dt>
                    <dd>90</dd>
                </dl>
            </fieldset>
            <s:set name="auditStatus" value="check.auditStatus"></s:set>
            <h3 class="contentTitle">平台库存分配列表</h3>
            <div class="panelBar">
                <ul class="toolBar">
                    <li><a class="icon" href="${ctx}/stock/stockCheck!inputUploadIndivGoodsList.action?id=${id}&type=${check.checkType}" rel="dlg_checkGoods" target="dialog" mask="true" width="800" height="600"><span>添加库存分配</span></a></li>
                    <li class="line">line</li>
                    <li><a class="icon" href="${ctx}/stock/stockCheck!inputUploadIndivGoodsList.action?id=${id}&type=${check.checkType}" rel="dlg_checkGoods" target="dialog" mask="true" width="800" height="600"><span>导出数据</span></a></li>
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
                            <tbody id="stockInDetailTbody">
                            <s:iterator value="list">
                                <tr class="unitBox">
                                    <td>${skuCode }</td>
                                    <td>${platformNo }</td>
                                    <td>${platformName }</td>
                                    <td>${totalNum}</td>
                                    <td>${createBy }</td>
                                    <td><fmt:formatDate value='${updateDate }'   pattern='yyyy-MM-dd' /></td>
                                    <td>
                                        <a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/stockCheck!deleteGoods.action?id=${id}&navTabId=tab_checkGoods" class="btnDel"></a>
                                        <a title="分配库存" target="navTab" rel="tab_checkItem" href="${ctx}/stock/stockCheck!listItem.action?id=${check.id}&skuCode=${skuCode}" class="btnView"></a>
                                    </td>
                                </tr>
                            </s:iterator>
                            <tr class="unitBox">
                                <td>11}</td>
                                <td>1</td>
                                <td>金立官网</td>
                                <td>10</td>
                                <td>何霞</td>
                                <td>2018-1-17</td>
                                <td>
                                    <a title="确认要删除吗？" target="ajaxTodo" href="${ctx}/stock/stockCheck!deleteGoods.action?id=${id}&navTabId=tab_checkGoods" class="btnDel"></a>
                                    <a title="分配库存" target="navTab" rel="tab_checkItem" href="${ctx}/stock/stockCheck!listItem.action?id=${check.id}&skuCode=${skuCode}" class="btnView"></a>
                                </td>
                            </tr>

                            </tbody>
                        </table>

                    </div>
                </div>
                <div class="tabsFooter">
                    <div class="tabsFooterContent"></div>
                </div>
            </div>
            <div class="divider"></div>
            <div class="tabs">
                <dl>
                    <dt>制单人：</dt>
                    <dt><input readonly="true" name="field5" type="text" value="测试"/></dt>
                </dl>
                <dl>
                    <dt>制单日期：</dt>
                    <dt><input readonly="true" name="field5" type="text" value="2018-1-1"/></dt>
                </dl>
            </div>
        </div>
        <%--<div class="formBar">
            <ul>
                <s:if test="receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
                    <li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="purchaseInSubmit();">确认</button></div></div></li>
                </s:if>
                <li>
                    <s:if test="id==null">
                        <a class="button" target="ajaxTodo" href="${ctx}/stock/stockIn!cancel.action?stockInCode=${stockInCode}&callbackType=closeCurrent&navTabId=tab_stockIn"><span>取消</span></a>
                    </s:if>
                    <s:else>
                        <div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div>
                    </s:else>
                </li>
            </ul>
        </div>--%>
    </div>
</form>
<script>
</script>