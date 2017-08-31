<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<jsp:useBean id="now" class="java.util.Date"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<fieldset>
    <legend>调拨单信息</legend>
    <form action="javascript:;" method="post" id="fm${rand}">
        <table>
            <tr>
                <td height="30">调拨仓：</td>
                <td>${transfer.transferTo }</td>
            </tr>
            <tr>
                <td height="30">调货单：</td>
                <td>
                    <input name="transferId" type="text" class="textInput digits readonly" value="${transfer.transferId }" readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td height="30">扫描个体编码：</td>
                <td id="scanTd${rand}">
                    <input id="scanInput${rand}" name="sv" type="text" class="textInput" maxlength="21" autocomplete="off"/><span id="scanTips${rand}" style="color: red;"></span>
                </td>
            </tr>
            <tr style="display: none;" id="wlTr${rand}">
                <td height="30">扫描物流运单：</td>
                <td>
                    <input id="wlInput${rand}" name="logisticNo" type="text" class="textInput" maxlength="21" autocomplete="off"/><span id="wlTips${rand}" style="color: red;"></span>
                </td>
            </tr>
        </table>
    </form>
</fieldset>
<div id="goodsPanel${rand}">
    <%@include file="transGoodsFragment.jsp" %>
</div>
<script type="text/javascript">
    (function () {
        checkFilsh(); // 一进来就检查配货完成情况
        $("#scanInput${rand}").keydown(function (event) { // 个体编码输入框事件
            if (event.keyCode == 13) {
                var $t = $(this), sv = $t.val($.trim($t.val())).val(), num;
                if (!/\d{4,}/.test(sv)) {
                    soundError();
                    $t.val("");
                    return showMsg("请输入有效的信息！");
                }
                if (sv.length <= 5) {
                    num = window.prompt("请输入配货数量：", 1) || "";
                    if (!/\d+/.test(num) || +num == 0) {
                        soundError();
                        return showMsg("请输入正确的数量");
                    }
                }
                var formData = {}, fields = $("#fm${rand}").serializeArray();
                $.each(fields, function () {
                    formData[this.name] = this.value;
                });
                formData["num"] = num;
                $.post("${ctx}/trans/scan.do", formData, function (data) {
                    $t.val("");
                    try {
                        var jsonData = $.parseJSON(data);
                        soundError();
                        showMsg(jsonData.message);
                    } catch (e) {
                        showMsg("");
                        $("#goodsPanel${rand}").html(data);
                        checkFilsh();
                    }
                });
            }
        }); // End Event

        // 指定显示焦点
        setTimeout(function () {
            $("#fm${rand}").find(":input:visible:not([readonly])").eq(0).focus();
        }, 500);

        // 显示提示消息
        function showMsg(msg) {
            $("#scanTips${rand}").html(msg);
        }

        // 检查配货是否完成
        function checkFilsh() {
            var finish = $("#goodsPanel${rand}").find("tr[finish='false']").length == 0;
            if (finish) {
                $("#scanTd${rand}").html("<span style='color:green;font-weight:bold;'>扫描已完成</span>");
                $("#wlTr${rand}").show();
            }
        } // End checkFilsh

        $("#wlInput${rand}").keydown(function (event) { // 物流号输入框事件
            if (event.keyCode == 13) {
                var $t = $(this), sv = $t.val($.trim($t.val())).val();
                if (sv.length < 4) {
                    return;
                }
                var formData = {}, fields = $("#fm${rand}").serializeArray();
                $.each(fields, function () {
                    formData[this.name] = this.value;
                });
                $.post("${ctx}/trans/dispatch.do", formData, function (data) {
                    if (data.result) {
                        alertMsg.correct("操作成功！")
                        navTab.reload();
                    } else {
                        $("#wlTips${rand}").html(data.message);
                    }
                }, "JSON");
            }
        }); // End Event
    })();
</script>
