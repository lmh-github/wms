<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

<form id="inputForm" action="${ctx}/workorder/up.do?callbackType=closeCurrent&navTabId=tab_workorder_list" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" name="id" value="${workOrder.id }"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>订单号：</dt>
                    <dd><input name="orderCode" readonly="readonly" maxlength="32" type="text" class="textInput required" value="${workOrder.orderCode}"/></dd>
                </dl>
                <dl>
                    <dt>订单来源：</dt>
                    <dd>
                        <input name="platform" placeholder="${workOrder.platform}" readonly="readonly" />
                    </dd>
                </dl>
                <dl>
                    <dt>升级处理：</dt>
                    <dd>
                        <input id="uper${rand}" placeholder="请选择" class="required" name="uper" bringBackName="uc.userName" type="text" readonly="readonly" autocomplete="off"/>
                        <a class="btnLook" href="${ctx}/uc/list.do" lookupGroup="uc" width="660" height="550">查找</a>
                    </dd>
                </dl>
                <dl>
                    <dt>紧急程度：</dt>
                    <dd>
                        <select name="lv">
                            <option value="一般" <s:if test="#attr.workOrder.lv=='一般'">selected</s:if>>一般</option>
                            <option value="紧急" <s:if test="#attr.workOrder.lv=='紧急'">selected</s:if>>紧急</option>
                        </select>
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>问题明细：</dt>
                    <dd><textarea name="description" readonly="readonly" maxlength="512" class="textInput valid required" style="width: 526px;height: 70px;"><s:property value="#request.workOrder.description" escapeHtml="true"/></textarea></dd>
                </dl>
                <dl class="nowrap">
                    <dt>升级建议：</dt>
                    <dd><textarea name="suggest" maxlength="512" class="textInput valid required" style="width: 526px;height: 70px;"></textarea></dd>
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
