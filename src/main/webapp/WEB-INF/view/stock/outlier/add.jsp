<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

    <form id="inputForm" action="${ctx}/stock/outlier/save.do?callbackType=closeCurrent&navTabId=tab_stock_outlier_list" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

    <input type="hidden" name="id" value="${orderOutlier.id }"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="100">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>预警名称：</dt>
                    <dd><input type="text" class="textInput required" name="name" value="${orderOutlier.name}"></dd>
                </dl>
                <dl></dl>
                <dl>
                    <dt>预警单价：</dt>
                    <dd><input type="text" class="textInput required" name="unitPrice" value="${orderOutlier.unitPrice}"></dd>
                </dl>
                <dl></dl>

                <dl>
                    <dt>开始日期：</dt>
                    <dd><input type="text" name="beginTime" class="date required" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${orderOutlier.beginTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"/></dd>
                </dl>
                <dl></dl>

                <dl>
                    <dt>结束日期：</dt>
                    <dd><input type="text" name="endTime" class="date required" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${orderOutlier.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"/></dd>
                </dl>
                <dl></dl>

                <dl>
                    <dt>是否开启：</dt>
                    <dd>
                        <select name="logSwitch">
                            <option value="0" ${(orderOutlier.logSwitch == '0')?"selected='true'":""}>开启</option>
                            <option value="1" ${(orderOutlier.logSwitch == '1')?"selected='true'":""}>关闭</option>
                        </select>
                    </dd>
                </dl>
                <dl></dl>

                <dl style="margin-bottom: 100px;">
                    <dt>订单来源：</dt>
                    <dd>
                        <s:select id="source" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey="" onchange="changeSource(this)"/>

                        <input type="hidden" id="orderSourceHidden" name="orderSource" value="${orderOutlier.orderSource}" class="textInput"/>

                        <textarea class="textInput valid required" id="displaySource" readonly style="margin-top:10px;height: 82px;"><c:forEach items="${fn:split(orderOutlier.orderSource,',')}" var="var" varStatus="status"><c:set var="data" scope="request" value="${var}"/><s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#request.data}[0].name"/><c:if test="${status.last == false}">,</c:if></c:forEach></textarea>
                    </dd>
                </dl>

            </fieldset>

            <fieldset>
                <table class="list nowrap" width="100%">
                    <colgroup>
                        <col style="width: 25%;">
                        <col style="width: 24%;">
                    </colgroup>
                    <thead>
                    <tr>
                        <th type="text" name="itemList[#index#].itemName" size="12" fieldClass="required">SKU编码</th>
                        <th type="text">SKU名称</th>
                        <th type="del" width="60">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="unitBox" style="background-color: transparent;">
                        <td><input name="editSkuid" bringBackName="sku.id" value="" type="hidden" /> <input class="textInput readonly" name="editSkuCode" bringBackName="sku.skuCode" type="text" value="" lookupGroup="sku" readonly="readonly" /> <a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a></td>
                        <td><input name="editSkuName" bringBackName="sku.skuName" value="" readonly="readonly" class="readonly" style="border: none;background-color: transparent;" /></td>
                        <td><a class="btnSelect" rel="select-sku" id="dig-add-sku" href="javascript:void(0)">添加</a></td>
                    </tr>
                    </tbody>
                    <tbody id="bom-sku">
                    <c:if test="${orderOutlier.skuIds != null}">
                        <c:forEach items="${fn:split(orderOutlier.skuIds,',')}" var="item" varStatus="i">
                                <tr>
                                    <td>${item}<input type="hidden" name="skuList[${i.index}].skuCode" value="${item}" /></td>
                                    <td>${(fn:split(orderOutlier.skuNames,','))[i.index]}<input type="hidden" name="skuList[${i.index}].skuName" value="${(fn:split(orderOutlier.skuNames,','))[i.index]}" /></td>
                                    <td><a href="javascript:;" class="btnDel">删除</a></td>
                                </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </fieldset>


        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button type="submit" id="activitySubmit">保存</button>
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

<script type="text/template" id="tr-template">
    <tr>
        <td>{skuCode}<input type="hidden" name="skuList[index].skuCode" value="{skuCode}" /></td>
        <td>{skuName}<input type="hidden" name="skuList[index].skuName" value="{skuName}" /></td>
        <td><a href="javascript:;" class="btnDel">删除</a></td>
    </tr>
</script>

<script>
    function changeSource(source) {

        var orderSourceSelector = $("#orderSourceHidden");
        var displaySourceSelector = $("#displaySource");

        var value = $(source).val();
        var displayValue = source.selectedOptions[0].label;

        var orderSource =orderSourceSelector.val() || "";
        var displaySource = displaySourceSelector.val() || "";

        if(orderSource && displaySource){

            if("请选择" === displayValue || displaySource.indexOf(displayValue) >= 0){
                return;
            }

            value = "," + value;
            displayValue = "," + displayValue;

        }

        value = orderSource + value;
        displayValue = displaySource + displayValue;

        orderSourceSelector.val(value);
        displaySourceSelector.val(displayValue);
    }

    (function() {
        var template = $.trim($("#tr-template").html());
        var reg = /\{(\w*)\}/ig;
        var bomSkuBodty = $("#bom-sku");
        $("#dig-add-sku").click(function() {
            var tr = $(this).closest("tr");
            var item = {
                skuCode : tr.find(":input[name=editSkuCode]").val(),
                skuName : tr.find(":input[name=editSkuName]").val(),
            };
            if (item.skuCode.length == 0) {
                alert("请选择SKU！");
                return;
            }

            if ($("#bom-sku tr>td:first-child:contains('" + item.skuCode + "')").length > 0) {
                alert("SKU：" + item.skuCode + " 已存在！");
                return;
            }

            var html = template.replace(reg, function($0, $1){
                return item[$1] || "";
            });
            $(html).appendTo(bomSkuBodty);
            bomSkuBodty.trigger("refresh");
            tr.find(":input").val("");
        }); // End click

        bomSkuBodty.delegate(".btnDel", "click", function() {
            $(this).closest("tr").remove();
            bomSkuBodty.trigger("refresh");
        }); // End delegate

        bomSkuBodty.bind("refresh", function() {
            $("tr", this).each(function(i) {
                $(this).find(":input[name*='[']").each(function() {
                    this.name = this.name.replace(/\[\w+\]/, "[" + i + "]");
                });
            });
        }); // END bind
    })();


</script>