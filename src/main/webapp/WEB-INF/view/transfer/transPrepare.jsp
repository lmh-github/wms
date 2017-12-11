<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<jsp:useBean id="now" class="java.util.Date"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<div class="pageContent" id="divTransPrepare1" style="padding: 5px 5px 0 5px;height: 100%;">
    <fieldset id="transFieldset${rand}">
        <legend>在这里进行配货，请先扫描调货单的条形码</legend>
        <ul>
            <li>
                <label>扫描调货单批次号</label>
                <input id="transferId${rand}" name="transferId" class="textInput" type="text" maxlength="30">
                <span id="scanTips${rand}" style="color: red;"></span>
            </li>
        </ul>
    </fieldset>
    <div id="step2${rand}" style="display: none;"></div>
    <script type="text/javascript">
        (function () {
            $("#transferId${rand}").keydown(function (event) {
                if (event.keyCode == 13) {
                    var $t = $(this), sv = $t.val($.trim($t.val())).val();
                    if (!/\d+/.test(sv)) {
                        soundError();
                        $t.val("");
                        return showMsg("请输入有效的信息！");
                    }
                    $.post("${ctx}/trans/trans${type}.do", {"transferId": $t.val()}, function (data) {
                        try {
                            var jsonData = $.parseJSON(data);
                            soundError();
                            $t.val("");
                            showMsg(jsonData.message);
                        } catch (e) {
                            showMsg("");
                            step2(data);
                        }
                    });
                }
            });

            setTimeout(function () {
                $("#transferId${rand}").focus();
            }, 500);

            // 显示提示消息
            function showMsg(msg) {
                $("#scanTips${rand}").html(msg);
            }

            // 第二步配货
            function step2(html) {
                $("#transFieldset${rand}").hide();
                $("#step2${rand}").html(html).show().find(":text:visible:not([readonly])").focus();
            }
        })();
    </script>
</div>