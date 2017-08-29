<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

<form id="inputForm" enctype="multipart/form-data" action="${ctx}/atta/up.json?callbackType=closeCurrent&navTabId=tab_workorder_list" method="post" class="pageForm required-validate" onsubmit="return iframeCallback(this, dialogAjaxDone);">
    <input type="hidden" name="id" value="${workOrder.id }"/>
    <input type="hidden" name="billCode" value="${workOrder.workCode}"/>
    <input type="hidden" name="billType" value="工单"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>文件信息</legend>
                <dl>
                    <dt>文件信息：</dt>
                    <dd><input name="uploadFile" type="file" /></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
            </ul>
        </div>
    </div>
</form>
<script type="text/javascript">
</script>
