<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

<form id="inputForm" action="${ctx}/uc/save.do?callbackType=closeCurrent&navTabId=tab_list.do" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, function(json) {DWZ.ajaxDone(json);if (json.statusCode == DWZ.statusCode.ok){$.pdialog.closeCurrent();$.pdialog._current=$('#fm${r}').closest('.dialog'); dialogSearch($('#fm${r}')[0]);}})">
    <input type="hidden" name="id" value="${id }"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>登录账号：</dt>
                    <dd><input name="ucAccount" maxlength="32" type="text" class="textInput required" value="${ucAccount }"/></dd>
                </dl>
                <dl>
                    <dt>用户名：</dt>
                    <dd>
                        <input name="userName" maxlength="32" class="textInput required" value="${userName}" />
                    </dd>
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
