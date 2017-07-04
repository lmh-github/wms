<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

<form id="inputForm" action="${ctx}/workorder/finish.do?callbackType=closeCurrent&navTabId=tab_list.do" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" name="id" value="${workOrder.id }"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>订单号：</dt>
                    <dd><input name="orderCode" readonly="readonly" type="text" class="textInput" value="${workOrder.orderCode}"/></dd>
                </dl>
                <dl>
                    <dt>订单来源：</dt>
                    <dd>
                        <input name="platform" placeholder="${workOrder.platform}" readonly="readonly" />
                    </dd>
                </dl>
                <dl>
                    <dt>处理人：</dt>
                    <dd>
                        <input class="required" name="worker" type="text" readonly="readonly" value="${workOrder.worker}" />
                    </dd>
                </dl>
                <dl>
                    <dt>升级处理：</dt>
                    <dd>
                        <input class="textInput" name="uper" type="text" readonly="readonly" value="${workOrder.uper}" />
                    </dd>
                </dl>
                <dl>
                    <dt>紧急程度：</dt>
                    <dd>
                        <input class="required" name="lv" type="text" readonly="readonly" value="${workOrder.lv}" />
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>问题明细：</dt>
                    <dd><textarea name="description" readonly="readonly" maxlength="512" class="textInput valid" style="width: 526px;height: 70px;"><s:property value="#request.workOrder.description" escapeHtml="true"/></textarea></dd>
                </dl>
                <dl class="nowrap">
                    <dt>升级建议：</dt>
                    <dd><textarea name="suggest" maxlength="512" readonly="readonly" class="textInput valid" style="width: 526px;height: 70px;"><s:property value="#request.workOrder.suggest" escapeHtml="true"/></textarea></dd>
                </dl>
                <dl class="nowrap">
                    <dt>处理结果：</dt>
                    <dd><textarea name="resultMsg" maxlength="512" class="textInput valid required" style="width: 526px;height: 70px;"></textarea></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="button"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
            </ul>
        </div>
    </div>
</form>
<script type="text/javascript">
</script>
