<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>
<div class="pageContent">
  <form method="post" action="${ctx}/stock/morder!extension.action?callbackType=closeCurrent&navTabId=tab_morder" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);" id="form${rand}">
    <input type="hidden" name="order.id" value="${id }" />
    <div class="pageFormContent" layoutH="56">
      <dl>
        <dt>取消原因：</dt>
        <dd><input type="text" name="order.extension" class="textInput valid required" /></dd>
      </dl>
    </div>
    <div class="formBar">
      <ul>
        <li><div class="buttonActive"><div class="buttonContent"><button type="submit">确定</button></div></div></li>
        <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
      </ul>
    </div>
  </form>
<script type="text/javascript">
(function() {
	
})();
</script>
</div>
