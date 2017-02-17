<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form action="<s:if test='transfer == null'>${ctx}/stock/transferSf!add.action?callbackType=closeCurrent&navTabId=tab_transferSf</s:if><s:else>${ctx}/stock/transferSf!update.action?callbackType=closeCurrent&navTabId=tab_transferSf</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" name="transfer.transferId" value="${transfer.transferId}"/>
    <input type="hidden" id="editGoods" name="editGoods" value="false"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend></legend>
                <dl class="nowrap">
                    <dt>发货仓：</dt>
                    <dd>
                        <select class="required" name="transfer.warehouseId" style="width: 152px;">
                            <option value="<s:property value="warehouseList.{?#this.warehouseCode=='01'}[0].id" />">
                                <s:property value="warehouseList.{?#this.warehouseCode=='01'}[0].warehouseName"/></option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt>收货仓：</dt>
                    <dd>
                        <select class="required" name="transfer.transferTo" style="width: 152px;">
                            <s:iterator value="warehouseList.{?#this.warehouseCode!='01'}" var="item">
                                <option value="${item.id}">${item.warehouseName }</option>
                            </s:iterator>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt>收货人：</dt>
                    <dd>
                        <input name="transfer.consignee" type="text" maxlength="20" size="20" value="${transfer.consignee }" ${editEnabled?"":"readonly='readonly'"}/>
                    </dd>
                </dl>
                <dl>
                    <dt>联系方式：</dt>
                    <dd>
                        <input name="transfer.contact" type="text" maxlength="20" size="20" value="${transfer.contact }" ${editEnabled?"":"readonly='readonly'"}/>
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>订单金额：</dt>
                    <dd>
                        <input name="transfer.orderAmount" type="text" class="textInput number valid required" value="${transfer.orderAmount eq null ? '0' : transfer.orderAmount}"/>
                    </dd>
                </dl>
                <dl>
                    <dt>调货类型：</dt>
                    <dd>
                        <select class="required" name="transfer.transType" style="width: 152px;">
                            <option value="0" ${(0==transfer.transType)?"selected='true'":""}>良品调拨</option>
                        </select>
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>备注：</dt>
                    <dd><textarea name="transfer.remark" cols="80" rows="2" style="width: 527px;height: 60px;">${transfer.remark }</textarea></dd>
                </dl>
            </fieldset>

            <div class="tabs">
            </div>

        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
                        </div>
                    </div>
                </li>
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



