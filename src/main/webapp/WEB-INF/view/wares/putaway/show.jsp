<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

<style>
    .pageFormContent p{
        width: 100%;
    }
</style>

    <input type="hidden" name="id" value="${activity.id }"/>
<div class="pageFormContent" layoutH="100">
    <fieldset>
        <legend>基本信息</legend>
        <dl>
            <dt>发起人：</dt>
            <dd>${currentUser}</dd>
        </dl>
        <dl>
            <dt>活动平台：</dt>
            <dd>${waresPutaway.platform}</dd>
        </dl>
        <dl>
            <dt>上下架类型：</dt>
            <dd>${waresPutaway.type}</dd>
        </dl>
        <dl>
            <dt>型号：</dt>
            <dd>${waresPutaway.waresM}</dd>
        </dl>
        <dl>
            <dt>版本：</dt>
            <dd>${waresPutaway.version}</dd>
        </dl>
        <dl>
            <dt>时间：</dt>
            <dd><fmt:formatDate value="${waresPutaway.operationTime }" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
        </dl>

        <dl>
            <dt>状态：</dt>
            <dd>${waresPutaway.status}</dd>
        </dl>
        <dl>
            <dt>备注：</dt>
            <dd>${waresPutaway.remarks}</dd>
        </dl>
    </fieldset>

</div>
