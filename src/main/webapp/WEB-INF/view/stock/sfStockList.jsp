<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<form action="javascript:;" method="post" class="pageForm required-validate">
  <input type="hidden" name="id" value="${id}" />
  <input type="hidden" name="config.key" value="${config.key}" />
  <div class="pageContent">
    <div class="pageFormContent" layoutH="50">
      <s:if test="#inv_response == null">无商品库存信息</s:if>
      <s:else>
        <dl>
          <%-- <dt>库存数量：<dd><s:property value="#inv_response"/> </dd></dt> --%>
          <dt>总库存数量：<dd><s:property value="#inv_response.total_stock == null ? '0' : #inv_response.total_stock"/> </dd></dt>
          <dt>在库库存数量：<dd><s:property value="#inv_response.on_hand_stock == null ? '0' : #inv_response.on_hand_stock"/> </dd></dt>
          <dt>可用库存数量：<dd><s:property value="#inv_response.available_stock == null ? '0' : #inv_response.available_stock"/> </dd></dt>
          <dt>在途库存数量：<dd><s:property value="#inv_response.in_transit_stock == null ? '0' : #inv_response.in_transit_stock"/> </dd></dt>
        </dl>      
      </s:else>
    </div>
  </div>

  <div class="formBar">
    <ul>
      <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
    </ul>
  </div>

</form>