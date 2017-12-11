<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="pagerForm${rand}" action="javascript:;" method="post" class="pageForm required-validate" onsubmit="return false;">
<input type="hidden" name="orderCode" value="${order.orderCode }">
<div class="pageContent">
    <div class="pageFormContent" layoutH="97">
        <fieldset>
            <legend>基本信息</legend>
            <dl>
                <dt>销售订单号：</dt>
                <dd>
                    <input type="text" maxlength="30" class="required" value="${order.orderCode }" readonly="readonly"/>
                </dd>
            </dl>
            <dl>
                <dt>退货仓库：</dt>
                <dd>
                    <input type="text" readonly="readonly" value="东莞电商仓"/>
                    <input type="hidden" value="1643" name="warehouseId"/>
                </dd>
            </dl>
            <dl class="nowrap">
                <dt>备注：</dt>
                <dd><textarea name="remark" cols="80" rows="1" id="remark" style="width: 527px;height: 35px;resize: none"></textarea></dd>
            </dl>
        </fieldset>

        <fieldset>
            <legend>商品信息</legend>
            <table class="list" width="100%">
                <thead>
                <tr>
                    <th width="22"><input type="checkbox" id="check-all${rand}" title="全选"></th>
                    <th align="center">SKU编码</th>
                    <th align="center">SKU名称</th>
                    <th align="center">单位</th>
                    <th align="center">单价</th>
                    <th align="center">数量</th>
                    <th align="center"><input type="checkbox" id="check-all-good${rand}" title="全部良品">良品数量</th>
                    <th align="center"><input type="checkbox" id="check-all-bad${rand}" title="全部次品">次品数量</th>
                </tr>
                </thead>
                <tbody id="tbody${rand}">
                <s:iterator value="orderGoods" status="status">
                    <tr class="unitBox" target="sid_user" rel="${status.index }">
                        <td><s:if test="skuCode.startsWith('1') == false"><input data-name="skuCode" value="${skuCode }" type="checkbox"></s:if></td>
                        <td align="center">${skuCode }</td>
                        <td align="center">${skuName }</td>
                        <td align="center">${measureUnit }</td>
                        <td align="center">${unitPrice }</td>
                        <td align="center">${quantity }</td>
                        <td align="center"><s:if test="skuCode.startsWith('1') == false"><input data-name="goodQuantity" disabled readonly data-qty="${quantity}" data-default-value="0" value="" type="text" style="width: 40px;" maxlength="3" /></s:if></td>
                        <td align="center"><s:if test="skuCode.startsWith('1') == false"><input data-name="badQuantity" disabled readonly data-qty="${quantity}"  data-default-value="0" value="${quantity}" type="text" style="width: 40px;" maxlength="3"/></s:if></td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
            <legend>已发出串号信息</legend>
            <table class="list" width="100%">
                <thead>
                <tr>
                    <th align="center">SKU编码</th>
                    <th align="center">SKU名称</th>
                    <th align="center">IMEI</th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="indivList" status="status">
                    <tr class="unitBox" target="sid_user" rel="${status.index }">
                        <td align="center">${skuCode}</td>
                        <td align="center">${skuName}</td>
                        <td align="center">${indivCode}</td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
            <legend>良品录入<span style="color: red;">（回车）</span>：<input type="text" class="imei-input${rand}" data-type="good" style="float:inherit;" maxlength="15" /></legend>
            <table class="list imei-table${rand}" width="100%">
                <thead>
                <tr>
                    <th align="center">良品IMEI</th>
                    <th align="center">SKU 编码</th>
                    <th align="center">SKU 名称</th>
                    <th type="del" width="30%">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
            <legend>次品录入<span style="color: red;">（回车）</span>：<input type="text" class="imei-input${rand}" data-type="bad" style="float:inherit;" maxlength="15" /></legend>
            <table class="list imei-table${rand}" width="100%" onclick="imei-table${rand}">
                <thead>
                <tr>
                    <th align="center">次品IMEI</th>
                    <th align="center">SKU 编码</th>
                    <th align="center">SKU 名称</th>
                    <th type="del" width="30%">操作</th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="indivList" status="status">
                    <tr style="border-width: 1px;border-style: solid;" data-type="bad">
                        <td>${indivCode}<input type="hidden" data-name="badImeis" value="${indivCode}" /></td>
                        <td>${skuCode}<input type="hidden" data-name="skuCode" value="${skuCode}" /></td>
                        <td>${skuName}</td>
                        <td><a href="javascript:;" class="btnDel" onclick="$(this).closest('tr').remove()">删除</a></td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </fieldset>
    </div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button id="submit-button${rand}" type="button">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
(function () {
    $("#check-all${rand}").click(function () {
        $("#tbody${rand} :checkbox").prop("checked", this.checked).trigger("myclick");
    });

    $("#check-all-good${rand},#check-all-bad${rand}").click(function () {
        var $t = $(this), checked = this.checked, td = $t.parent(), index = td.index();
        $("#tbody${rand} tr").each(function () {
            var txt = $(this).find("td").eq(index).find(":text");
            if (checked) {
                txt.val(txt.data("qty"));
            } else {
                txt.val("");
            }
        });
    });
    // 给checkbox绑定单击事件，选中，则可以填入数量
    $("#tbody${rand}").delegate(":checkbox", "click change myclick", function () {
        var $t = $(this), tr = $t.closest("tr");
        if (this.checked) {
            tr.find(":text").removeAttr("disabled").removeAttr("readonly").removeClass("readonly");
        } else {
            tr.find(":text").attr("disabled", "disabled").attr("readonly", "readonly").addClass("readonly");
            $("#check-all${rand}").prop("checked", false);
        }
    });

    // IMEI 录入框回车事件
    $(".imei-input${rand}").keyup(function (event) {
        if (event.keyCode == 13) {
            var $t = $(this), value = $.trim(this.value), table = $t.closest("fieldset").find("table");
            addTr(value, $t.data("type"), table);
            this.value = "";
        }
    });

    /**
     * 添加一行
     * @param imei
     * @param type
     * @param table
     */
    function addTr(imei, type, table) {
        if (imei.length < 15) {
            return alert("IMEI是由15位数字组成的“电子串号”！");
        }
        if ($(".imei-table${rand}").find("td:contains(" + imei + ")").length > 0) {
            return alert("IMEI【" + imei + "】已经存在！");
        }
        $.get("${ctx}/rma/lookup.do", {imei: imei}, function (data) {
            if (data == null) {
                return alert("IMEI【" + imei + "】在库中未找到！");
            }

            var template = $.trim($("#tr-template${rand}").html()), reg = /\{(\w*)\}/ig,
                obj = {imei: imei, skuCode: data.skuCode, skuName: data.skuName, type: type};
            var html = template.replace(reg, function ($0, $1) {
                return obj[$1] || "";
            });
            table.find("tbody").append(html);
        }, "JSON");
    }

    /**
     * 表单提交事件
     */
    $("#submit-button${rand}").click(function () {
        var $form = $("#pagerForm${rand}"), formData = {goodsList: [], goodImeis: [], badImeis: []};
        $.each($form.serializeArray(), function (i, o) {
            formData[o.name] = o.value;
        });
        $("#tbody${rand} tr").each(function () {
            var $tr = $(this), checkbox = $tr.find(":checkbox:checked");
            if (checkbox.length > 0) {
                var goods = {};
                $tr.find(":input").each(function () {
                    var value = $.trim(this.value), defaultValue = $(this).data("default-value");
                    goods[$(this).data("name")] = value.length == 0 ? (defaultValue == null ? "" : defaultValue) : value;
                });
                formData.goodsList.push(goods);
            }
        });
        var goodsListMap = {};
        $form.find("table.imei-table${rand} tbody tr").each(function () {
            var $tr = $(this), skuCode = $tr.find(":input[data-name='skuCode']").val(), type = $tr.data("type");
            var goods = goodsListMap[skuCode] || {skuCode: skuCode, goodQuantity: 0, badQuantity: 0};
            goods[type + "Quantity"] = goods[type + "Quantity"] + 1;
            goodsListMap[skuCode] = goods;
        });
        $.each(goodsListMap, function (k, v) {
            formData.goodsList.push(v)
        });

        $form.find(":input[data-name='goodImeis']").each(function () {
            formData.goodImeis.push(this.value);
        });
        $form.find(":input[data-name='badImeis']").each(function () {
            formData.badImeis.push(this.value);
        });

        submit(JSON.stringify(formData));

    });

    function submit(formData) {
        $.ajax({
            type: "post",
            url: "${ctx}/rma/create.do?callbackType=closeCurrent&navTabId=tab_salesOrder",
            data: formData,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            cache: false,
            success: function (data) {
                if (data.statusCode == DWZ.statusCode.ok) {
                    alertMsg.correct(data.message || "操作成功！");
                    $.pdialog.closeCurrent();
                    navTab.reload(null, {navTabId: "tab_salesOrder"});
                } else {
                    alertMsg.error(data.message);
                }
            },
            error: DWZ.ajaxError
        });
    }

})();
</script>
<script type="text/template" id="tr-template${rand}">
    <tr style="border-width: 1px;border-style: solid;" data-type="{type}">
        <td>{imei}<input type="hidden" data-name="{type}Imeis" value="{imei}" /></td>
        <td>{skuCode}<input type="hidden" data-name="skuCode" value="{skuCode}" /></td>
        <td>{skuName}</td>
        <td><a href="javascript:;" class="btnDel" onclick="$(this).closest('tr').remove()">删除</a></td>
    </tr>
</script>