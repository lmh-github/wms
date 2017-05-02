<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<h2 class="contentTitle"></h2>
<form id="fm${rand}" action="${ctx}/trans/addAndEdit.do?callbackType=closeCurrent&navTabId=tab_transfer" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" name="transferId" value="${transfer.transferId}"/>
    <input type="hidden" name="transType" value="0"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>发货仓：</dt>
                    <dd>
                        <select class="required" name="warehouseId" style="width: 152px;">
                            <option value="<s:property value="#request.warehouseList.{?#this.warehouseCode=='01'}[0].id" />">
                                <s:property value="#request.warehouseList.{?#this.warehouseCode=='01'}[0].warehouseName"/></option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt>收货地址：</dt>
                    <dd>
                        <s:textfield name="transferTo" value="%{#attr.transfer.transferTo}" cssClass="required" theme="simple" maxLength="40"/>
                    </dd>
                </dl>
                <dl>
                    <dt>收货人：</dt>
                    <dd>
                        <s:textfield name="consignee" value="%{#attr.transfer.consignee}" theme="simple" maxLength="20"/>
                    </dd>
                </dl>
                <dl>
                    <dt>联系方式：</dt>
                    <dd>
                        <s:textfield name="contact" value="%{#attr.transfer.contact}" theme="simple" maxLength="20"/>
                    </dd>
                </dl>
                <dl>
                    <dt>PO：</dt>
                    <dd>
                        <s:textfield name="po" value="%{#attr.transfer.po}" theme="simple" maxLength="20"/>
                    </dd>
                </dl>
                <dl>
                    <dt>物流公司：</dt>
                    <dd>
                        <s:textfield name="logisticName" value="%{#attr.transfer.logisticName}" theme="simple" maxLength="20"/>
                    </dd>
                </dl>
                <dl>
                    <dt>售达方：</dt>
                    <select class="required" name="transferSale">
                        <option value="">请选择</option>
                        <c:forEach items="${transferPartnerList}" var="item">
                            <option value=${item.id} ${(item.id eq transfer.transferSale)?"selected='true'":""}>${item.name}</option>
                        </c:forEach>
                    </select>
                </dl>
                <dl>
                    <dt>送达方：</dt>
                    <select class="required" name="transferSend">
                        <option value="">请选择</option>
                        <c:forEach items="${transferPartnerList}" var="item">
                            <option value=${item.id} ${(item.id eq transfer.transferSend)?"selected='true'":""}>${item.name}</option>
                        </c:forEach>
                    </select>
                </dl>
                <dl>
                    <dt>开票方：</dt>
                    <select class="required" name="transferInvoice">
                        <option value="">请选择</option>
                        <c:forEach items="${transferPartnerList}" var="item">
                            <option value=${item.id} ${(item.id eq transfer.transferInvoice)?"selected='true'":""}>${item.name}</option>
                        </c:forEach>
                    </select>
                </dl>
                <dl>
                    <dt>订单金额：</dt>
                    <dd>
                        <s:textfield name="orderAmount" value="%{#attr.transfer.orderAmount}" theme="simple" cssClass="textInput number valid required" maxLength="10"/>
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>备注：</dt>
                    <dd>
                        <textarea name="remark" style="width:526px;height:55px;resize:none" maxlength="200"><s:property value="#attr.transfer.remark"/></textarea>
                    </dd>
                </dl>
            </fieldset>
            <c:if test="${transfer.transferId != null}">
                <fieldset>
                    <legend>商品清单</legend>
                    <div class="panelBar">
                        <ul class="toolBar">
                            <li>
                                <a class="add" href="${ctx}/trans/toTransferGoods.do?transferId=${transfer.transferId}" target="dialog" rel="dlg_transfer_goodsnew" mask="true" width="800" height="600"><span>添加调拨商品</span></a>
                            </li>
                        </ul>
                    </div>
                    <table class="list" width="100%">
                        <thead>
                        <tr>
                            <th width="50">SKU编码</th>
                            <th width="50">SKU名称</th>
                            <th width="50">单位</th>
                            <th width="50">数量</th>
                            <th width="50">单价</th>
                            <s:if test="#attr.transfer.status == 2">
                                <th width="50">已退货</th>
                            </s:if>
                            <s:if test="#attr.transfer.status==1">
                                <th width="50">操作</th>
                            </s:if>
                        </tr>
                        </thead>
                        <tbody id="purRecvTbody">
                        <s:iterator value="#attr.goodsList">
                            <tr class="unitBox">
                                <td>${skuCode }</td>
                                <td>${skuName }</td>
                                <td>${measureUnit }</td>
                                <td>${quantity }</td>
                                <td>${unitPrice }</td>
                                <s:if test="#attr.transfer.status == 2">
                                    <td><s:if test="rmaNum > 0"><span style="color: red">${rmaNum }</span></s:if></td>
                                </s:if>
                                <s:if test="#attr.transfer.status == 1">
                                    <td>
                                        <a title="确实要删除吗？" target="ajaxTodo" callback="goodsDeleteAjaxDone${rand}" href="${ctx}/trans/delGoods.do?goodsId=${id}" class="btnDel"></a>
                                    </td>
                                </s:if>
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                </fieldset>
            </c:if>
        </div>
        <div class="formBar">
            <ul>
                <s:if test="#attr.transfer == null || #attr.transfer.status == 1">
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">保存</button>
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
<script type="text/javascript">
    window["goodsDeleteAjaxDone${rand}"] = function (json) {
        DWZ.ajaxDone(json);
        if (json.statusCode == DWZ.statusCode.ok) {
            $.pdialog.reload('${ctx}/trans/toAddAndEdit.do?transferId=${transfer.transferId}', {dialogId: 'dlg_transfer_addAndEdit'});
        }
    }
</script>



