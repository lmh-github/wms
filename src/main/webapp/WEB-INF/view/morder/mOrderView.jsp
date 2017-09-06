<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>

<style type="text/css">
    #goodsTbody${rand} input[type=text]{width:90%;}
</style>
<div class="pageContent">
  <form method="post" action="${ctx}/stock/morder!add.action?callbackType=closeCurrent&navTabId=tab_morder" class="pageForm required-validate" onsubmit="" id="form${rand}">
    <div class="pageFormContent" layoutH="56">
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>建单类型：</label><input type="text" readonly="readonly" value="${billType}" />
        </p>
        <p>
          <label>退货平台：</label> <s:select name="order.platform" list="@com.gionee.wms.common.WmsConstants$OrderSource@values()" listKey="code" listValue="name" value="platform" disabled="true" />
        </p>
        <p>
          <label>是否重开发票：</label> <s:textfield name="invoice" type="text" value='%{#{"1":"是", "0":"否"}[#invoice + ""]}' readonly="true" />
        </p>
        <div style="clear: both;"></div>
        <p>
          <label>创建人：</label> <s:textfield name="createBy" type="text" value='%{#createBy}' readonly="true" />
        </p>
        <p>
          <label>创建时间：</label> <input type="text" readonly="readonly" value="<fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss" />" />
        </p>
        <p>
          <label>修改人：</label> <s:textfield name="updateBy" type="text" value='%{#updateBy}' readonly="true" />
        </p>
        <p>
          <label>修改时间：</label> <input type="text" readonly="readonly" value="<fmt:formatDate value="${updateTime}" pattern="yyyy-MM-dd HH:mm:ss" />" />
        </p>
        <dl class="nowrap" style="width: 350px;">
          <dt>建单原因：</dt>
          <dd style="width: 200px;margin-left:34px;"><s:textarea name="remark" readonly="true" cols="90" rows="2" cssStyle="height:45px;" /></dd>
        </dl>
        <dl class="nowrap extension" style="width: 350px;position: absolute;float: left;right:271px;top:156px;">
          <dt>取消原因：</dt>
          <dd style="width: 200px;margin-left:34px;"><s:textarea name="extension" readonly="true" cols="90" rows="2" cssStyle="height:45px;" /></dd>
        </dl>
      </fieldset>
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>订单号：</label> <s:textfield name="orderCode" readonly="true" />
        </p>
        <p>
          <label>订单来源：</label> <s:textfield name="salesOrder.orderSource" value="%{@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values().{?#this.code==#salesOrder.orderSource}[0].name}" readonly="true" />
        </p>
        <p>
          <label>订单状态：</label> <s:textfield value="%{@com.gionee.wms.common.WmsConstants$OrderStatus@values().{?#this.code==#salesOrder.orderStatus}[0].name}" readonly="true" />
        </p>
        <p>
          <label>下单人：</label> <s:textfield value="%{#salesOrder.orderUser}" readonly="true" />
        </p>
        <p>
          <label>支付类型：</label> <s:textfield value="%{@com.gionee.wms.common.WmsConstants$PaymentType@values().{?#this.code==#salesOrder.paymentType}[0].name}" readonly="true" />
        </p>
        <p>
          <label>支付方式：</label> <s:textfield value="%{#salesOrder.paymentName}" readonly="true" />
        </p>
        <p>
          <label>配送方式：</label> <s:textfield value="%{#salesOrder.shippingName}" readonly="true" />
        </p>
        <p>
          <label>配送单号：</label> <s:textfield value="%{#salesOrder.shippingNo}" readonly="true" />
        </p>
        <p>
          <label>收货人：</label> <s:textfield value="%{#salesOrder.consignee}" readonly="true" />
        </p>
        <p>
          <label>手机号：</label> <s:textfield value="%{#salesOrder.mobile}" readonly="true" />
        </p>
        <p>
          <label>收货地址：</label> <s:textfield value="%{#salesOrder.province}" readonly="true" />
        </p>
        <p>
          <label>商品总金额：</label> <s:textfield value="%{#salesOrder.goodsAmount}" readonly="true" />
        </p>
        <p>
          <label>订单总金额：</label> <s:textfield value="%{#salesOrder.orderAmount}" readonly="true" />
        </p>
        <p>
          <label>应付金额：</label> <s:textfield value="%{#salesOrder.payableAmount}" readonly="true" />
        </p>
      </fieldset>
      <div class="divider"></div>
      <table class="list nowrap" width="100%" style="table-layout: fixed;">
        <thead>
          <tr>
            <th align="center">SKU编码</th>
            <th align="center">SKU名称</th>
            <th align="center">数量</th>
          </tr>
        </thead>
        <tbody id="goodsTbody${rand}">
          <s:iterator value="goods">
            <tr>
              <td><s:textfield value="%{skuCode}" readonly="true" theme="simple" /></td>
              <td><s:textfield value="%{skuName}" readonly="true" theme="simple" /></td>
              <td><s:textfield value="%{qty}" readonly="true" theme="simple" /></td>
            </tr>
          </s:iterator>
        </tbody>
      </table>
    </div>
    <div class="formBar">
      <ul>
        <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
      </ul>
    </div>
  </form>
</div>
