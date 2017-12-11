<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<form action="${ctx}/stock/salesOrder!configAutoPush.action?callbackType=closeCurrent&navTabId=tab_salesOrder" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
  <input type="hidden" name="id" value="${id}" />
  <input type="hidden" name="config.key" value="${config.key}" />
  <div class="pageContent">
    <div class="pageFormContent" layoutH="50">
      <s:if test="config.key == @com.gionee.wms.common.WmsConstants@ORDER_AUTO_PUSH_SF">
      <dl>
        <dt>自动推送：</dt>
        <dd>
            <shiro:hasPermission name="order:order_push">
          <label for="auto_push_sf_radio_0"><input id="auto_push_sf_radio_0" type="radio" name="config.value" value="true" ${config.value == 'true' ? 'checked' : '' } />开</label>
            </shiro:hasPermission>
          <label for="auto_push_sf_radio_1"><input id="auto_push_sf_radio_1" type="radio" name="config.value" value="false" ${config.value == 'false' ? 'checked' : '' } />关</label>
        </dd>
      </dl>
      </s:if>
      <s:if test="config.key == @com.gionee.wms.common.WmsConstants@ORDER_PUSH_CHECK_STOCK">
      <dl>
        <dt>推送库存校验：</dt>
        <dd>
          <label for="push_check_sf_radio_0"><input id="push_check_sf_radio_0" type="radio" name="config.value" value="true" ${config.value == 'true' ? 'checked' : '' } />开</label>
          <label for="push_check_sf_radio_1"><input id="push_check_sf_radio_1" type="radio" name="config.value" value="false" ${config.value == 'false' ? 'checked' : '' } />关</label>
        </dd>
      </dl>
      </s:if>
    </div>
  </div>

  <div class="formBar">
    <ul>
      <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
      <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
    </ul>
  </div>

</form>