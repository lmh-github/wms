<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<jsp:useBean id="now" class="java.util.Date"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">快捷收货单</h2>
<form id="pagerForm" name="purchaseRecvForm" action="${ctx}/stock/purchaseRecv!confirmReceive.action?id=${id }&callbackType=closeCurrent&navTabId=tab_purchaseRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">

    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>收货编号：</dt>
                    <dd>${receive.receiveCode }</dd>
                </dl>
                <dl>
                    <dt>仓库：</dt>
                    <dd>
                        ${receive.warehouseName }
                    </dd>
                </dl>
                <dl>
                    <dt>供应商：</dt>
                    <dd>
                        ${receive.supplierName }
                    </dd>
                </dl>
                <dl>
                    <dt>采购单号：</dt>
                    <dd>
                        ${receive.originalCode }
                    </dd>
                </dl>
                <dl>
                    <dt>制单人：</dt>
                    <dt>${receive.preparedBy }</dt>
                </dl>
                <dl>
                    <dt>制单日期：</dt>
                    <dt>
                        <fmt:formatDate value='${receive.preparedTime}' dateStyle="long" pattern='yyyy-MM-dd'/>
                    </dt>
                </dl>
                <dl class="nowrap">
                    <dt>备注：</dt>
                    <dd>
                        <textarea name="receive.remark" id="remark" style="width: 526px;height: 70px;resize: none;border: none;">${receive.remark }</textarea>
                    </dd>
                </dl>
            </fieldset>

            <h3 class="contentTitle">商品清单</h3>
            <div class="panelBar">
                <ul class="toolBar">
                    <li>
                        <a class="add" href="${ctx}/stock/purchaseRecv!inputGoodsNew.action?id=${receive.id}" target="dialog" rel="dlg_purchaseRecvInputGoodsNew" mask="true" width="800" height="600"><span>添加收货商品</span></a>
                    </li>
                </ul>
            </div>
            <div class="tabs">
                <div class="tabsContent" style="height: 230px;">
                    <div>
                        <table class="list" width="100%">
                            <thead>
                            <tr>
                                <th width="60">SKU编码</th>
                                <th width="60">SKU名称</th>
                                <th width="60">单位</th>
                                <th width="60">数量</th>
                                <th width="60">操作</th>
                            </tr>
                            </thead>
                            <tbody id="purRecvTbody">
                            <s:iterator value="goodsList">
                                <tr class="unitBox">
                                    <td>${skuCode }</td>
                                    <td>${skuName }</td>
                                    <td>${measureUnit }</td>
                                    <td>${quantity }</td>
                                    <td>
                                        <!-- <a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/stockIn!deleteStockInItem.action?id=${item.id}&navTabId=tab_inputPurchaseIn" class="btnDel"></a> -->
                                        <s:if test="receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code && receive.receiveType==@com.gionee.wms.common.WmsConstants$ReceiveType@PURCHASE.code">
                                            <a title="确实要删除吗？" target="ajaxTodo" callback="purRecvDeleteAjaxDone" href="${ctx}/stock/purchaseRecv!deleteGoods.action?id=${id}" class="btnDel"></a>
                                            <a title="编辑商品编码" target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/purchaseRecv!inputGoodsManual.action?id=${id}" class="btnEdit"></a>
                                        </s:if>
                                        <s:else>
                                            <a title="查看商品" target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/purchaseRecv!inputGoods.action?id=${id}" class="btnView"></a>
                                        </s:else>
                                    </td>
                                </tr>
                            </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tabsFooter">
                    <div class="tabsFooterContent"></div>
                </div>
            </div>

        </div>
        <div class="formBar">
            <ul>
                <s:if test="receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="button" onclick="purchaseInSubmit();">确认</button>
                            </div>
                        </div>
                    </li>
                </s:if>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button class="close" type="button">关闭</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</form>
<script>
    function purchaseInSubmit() {
        if ($('#purRecvTbody tr').length == 0) {
            alert("商品不能为空");
            return false;
        }
        if (confirm("确认收货后商品库存将会相应增加，确定要执行吗？")) {
            $("form[name=purchaseRecvForm]").submit();
        }
    }

    //自定义DWZ回调函数
    function purRecvDeleteAjaxDone(json) {
        DWZ.ajaxDone(json);
        if (json.statusCode == DWZ.statusCode.ok) {
            //$.pdialog.closeCurrent();
            $.pdialog.reload('${ctx}/stock/purchaseRecv!inputManual.action?id=${id}', {dialogId: 'dlg_purchaseRecvInputManual'});
        }
    }
</script>