<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<h2 class="contentTitle"></h2>
<form id="fm${rand}" action="${ctx}/trans/addGoods.do?callbackType=closeCurrent" method="post" class="pageForm required-validate">
    <input type="hidden" name="transferId" value="${transferId}"/>
    <input name="transferId" value="${transferId}" type="hidden"/>
    <div class="pageContent">
        <div class="pageFormContent">
            <fieldset>
                <legend>SKU信息</legend>
                <dl>
                    <dt>SKU编码：</dt>
                    <dd>
                        <input name="skuId" bringBackName="sku.id" type="hidden"/>
                        <input id="skuCode${rand}" class="required" name="skuCode" bringBackName="sku.skuCode" type="text" lookupGroup="sku" autocomplete="off"/>
                        <a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
                        <span class="info">(查找SKU)</span>
                        <input type="hidden" name="indivEnabled"/>
                    </dd>
                </dl>
                <dl>
                    <dt>SKU名称：</dt>
                    <dd>
                        <input class="readonly" name="skuName" bringBackName="sku.skuName" readonly="readonly" type="text"/>
                    </dd>
                </dl>
                <dl>
                    <dt>库存单位：</dt>
                    <dd>
                        <input class="readonly" name="measureUnit" bringBackName="sku.measureUnit" readonly="readonly" type="text"/>
                    </dd>
                </dl>
                <dl>
                    <dt>添加数量：</dt>
                    <dd>
                        <input type="text" name="quantity" maxlength="7" class="required digits" min="1"/>
                    </dd>
                </dl>
                <dl>
                    <dt>单价：</dt>
                    <dd>
                        <input type="text" name="unitPrice" maxlength="8" class="required number" min="0"/>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="button" onclick="$('#fm${rand}').submit()">保存</button>
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
<script type="text/javascript">
    (function () {
        setTimeout(function () {
            $("#skuCode${rand}").focus();
        }, 400);
        $("#skuCode${rand}").keydown(function (event) {
            if (event.keyCode == 13) {
                var $t = $(this), v = $t.val($.trim($t.val())).val();
                $.post("${ctx}/stock/purchaseRecv!getSkuInfo.action", {"skuCode": v}, function (data) {
                    if (data != null) {
                        data.skuId = data.id;
                        data.measureUnit = data.wares.measureUnit;
                        data.indivEnabled = data.wares.indivEnabled;
                        $("#fm${rand} fieldset :input[name]").each(function () {
                            if (data[this.name] != null) {
                                $(this).val(data[this.name]);
                            }
                        });
                        $("#fm${rand} fieldset :input:visible:not([readonly]):eq(1)").focus();
                    } else {
                        $("#fm${rand} fieldset :input[name]").each(function () {
                            $(this).val("");
                        });
                    }
                })
            }
        }); // End Event

        $("#fm${rand}").submit(function () {
            console.log("a");
            return validateCallback(this, function (json) {
                DWZ.ajaxDone(json);
                if (json.statusCode == DWZ.statusCode.ok) {
                    $.pdialog.closeCurrent();
                    $.pdialog.reload('${ctx}/trans/toAddAndEdit.do?transferId=${transferId}', {dialogId: 'dlg_transfer_addAndEdit'});
                }
            });
        });
    })();
</script>



