<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>
<style type="text/css">
#backConfirm${rand} label,#backConfirm${rand} input[type='radio']{vertical-align: middle;}
#backConfirm${rand} input[type='radio']{margin:0;}
</style>
<div class="pageContent">
  <form method="post" action="${ctx}/stock/orderBack!confirm.action?callbackType=closeCurrent&navTabId=tab_orderBack" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);" id="form${rand}">
    <input type="hidden" name="orderBack.id" value="${backOrder.id }" />
    <input type="hidden" name="orderBack.backCode" value="${backOrder.backCode }" />
    <div class="pageFormContent" layoutH="56" id="backConfirm${rand}">
      <s:if test="backOrder.backStatus == @com.gionee.wms.common.WmsConstants$BackStatus@BACKING.code">
        <dl class="nowrap">
          <label style="width: 65px;">退回物流：</label> 
          <select name="orderBack.shippingCode" id="shippingCode${rand}">
            <optgroup label="B">
              <option value="百世汇通">百世汇通</option>
            </optgroup>
            <optgroup label="C">
              <option value="城市100">城市100</option>
              <option value="城际速递">城际速递</option>
            </optgroup>
            <optgroup label="D">
              <option value="东方快递">东方快递</option>
              <option value="D速物流">D速物流</option>
            </optgroup>
            <optgroup label="E">
              <option value="EMS">EMS</option>
            </optgroup>
            <optgroup label="G">
              <option value="国通快递">国通快递</option>
              <option value="共速达">共速达</option>
            </optgroup>
            <optgroup label="J">
              <option value="捷特快递">捷特快递</option>
              <option value="久易快递">久易快递</option>
              <option value="急顺通">急顺通</option>
            </optgroup>
            <optgroup label="K">
              <option value="快捷快递">快捷快递</option>
            </optgroup>
            <optgroup label="L">
              <option value="龙邦速递">龙邦速递</option>
              <option value="乐天速递">乐天速递</option>
            </optgroup>
            <optgroup label="N">
              <option value="能达速递">能达速递</option>
            </optgroup>
            <optgroup label="Q">
              <option value="全日通快递">全日通快递</option>
            </optgroup>
            <optgroup label="R">
              <option value="如风达快递">如风达快递</option>
            </optgroup>
            <optgroup label="S">
              <option value="申通快递">申通快递</option>
              <option value="顺丰速运">顺丰速运</option>
              <option value="速尔快递">速尔快递</option>
              <option value="EMS">EMS</option>
            </optgroup>
            <optgroup label="T">
              <option value="天天快递">天天快递</option>
              <option value="腾达速递">腾达速递</option>
            </optgroup>
            <optgroup label="Y">
              <option value="圆通速递">圆通速递</option>
              <option value="韵达快运">韵达快运</option>
              <option value="运通快递">运通快递</option>
              <option value="优速快递">优速快递</option>
              <option value="银捷速递">银捷速递</option>
              <option value="易通达">易通达</option>
            </optgroup>
            <optgroup label="Z">
              <option value="中通快递">中通快递</option>
              <option value="宅急送">宅急送</option>
              <option value="中国邮政快递">中国邮政快递</option>
              <option value="中速快件">中速快件</option>
            </optgroup>
            <option value="其他物流">其他物流</option>
          </select>
          <label style="width: 85px;margin-left: 30px;">退回物流单号：</label> <input type="text" name="orderBack.shippingNo" class="digits" size="15" value="<s:property value="backOrder.shippingNo" />" />
        </dl>
        <label><input type="radio" value="<s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@BACKED.code" />" name="orderBack.backStatus" /><s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@BACKED.name" /></label>
      </s:if>
      <s:if test="backOrder.backStatus == @com.gionee.wms.common.WmsConstants$BackStatus@BACKED.code and backOrder.backType == 'back'">
        <label><input type="radio" value="<s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@REFUND.code" />" name="orderBack.backStatus" /><s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@REFUND.name" /></label>
      </s:if>  
      <s:if test="backOrder.backStatus == @com.gionee.wms.common.WmsConstants$BackStatus@BACKED.code and backOrder.backType == 'exchange'">
        <label><input type="radio" value="<s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@RENEWED.code" />" name="orderBack.backStatus" /><s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@RENEWED.name" /></label>
      </s:if>
      <dl class="nowrap">
        <dt>已退货备注：</dt>
        <dd><textarea name="orderBack.remarkBacked" cols="90" rows="3" maxlength="100" class="textInput valid"><s:property value="backOrder.remarkBacked" /></textarea></dd>
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
	$("#backConfirm${rand} :radio:eq(0)").prop("checked", true);
	$("#shippingCode${rand}").val('<s:property value="backOrder.shippingCode" />');
})();
</script>
</div>
