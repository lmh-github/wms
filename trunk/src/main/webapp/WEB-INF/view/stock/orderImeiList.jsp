<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<form action="${ctx}/stock/salesOrder!configAutoPush.action?callbackType=closeCurrent&navTabId=tab_salesOrder" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
  <input type="hidden" name="id" value="${id}" />
  <input type="hidden" name="config.key" value="${config.key}" />
  <div class="pageContent">
    <div class="pageFormContent" layoutH="50">
      <table class="list" width="100%">
        <thead>
          <tr align="center">
            <th>序号</th>
            <th>串号</th>
          </tr>
        </thead>
        <tbody>
        <s:if test="#imeis == null || #imeis.size == 0">
          <tr>
            <td colspan="2">无数据</td>
          </tr>
        </s:if>
        <s:else>
          <s:iterator value="#imeis" status="s">
            <tr align="center">
              <td><s:property value="#s.index + 1"/></td>
              <td><s:property value="imei"/></td>
            </tr>
          </s:iterator>
        </s:else>
        </tbody>
      </table>
    </div>
  </div>

  <div class="formBar">
    <ul>
      <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
    </ul>
  </div>

</form>