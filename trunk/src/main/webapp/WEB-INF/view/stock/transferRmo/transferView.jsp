<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>
<style type="text/css">#goodsTbody${rand} input[type=text]{width:90%;}</style>
<div class="pageContent">
  <form method="post" action="${ctx}/stock/orderBack!update.action?callbackType=closeCurrent&navTabId=tab_orderBack" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
    <input type="hidden" name="orderBack.id" value="${orderBack.id}" />
    <div class="pageFormContent" layoutH="56">
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>退货单号：</label> <input type="text" readonly="readonly" name="transferRemoveOrder.transferCode" value="${transferRemoveOrder.transferCode }" class="textInput">
        </p>
        <p>
          <label>品质：</label> <s:select disabled="true" name="transferRemoveOrder.quality" value="transferRemoveOrder.quality" list="#{'1':'良品','0':'次品'}" listKey="key" listValue="value"></s:select>
        </p>
        <p>
          <label>退货平台：</label><input type="text" readonly="readonly"  name="transferRemoveOrder.backPlatform" value="${transferRemoveOrder.backPlatform}" class="digits" size="15">
        </p>

        <p>
          <label>状态：</label>
           <s:select disabled="true"  name="transferRemoveOrder.status" value="transferRemoveOrder.status" list="#{'1':'已创建','0':'无效','2':'已收货'}"></s:select>
        </p>
        <dl class="nowrap">
          <dt>备注：</dt>
          <dd><textarea name="transferRemoveOrder.remark" cols="90" rows="2" maxlength="100" class="textInput valid" >${transferRemoveOrder.remark }</textarea></dd>
        </dl>
      </fieldset>
      <fieldset>

      <div class="divider"></div>
      <table class="list nowrap" width="100%" style="table-layout: fixed;">
        <thead>
          <tr>
            <th align="center">SKU编码</th>
            <th align="center">SKU名称</th>
            <th align="center">单位</th>
            <th align="center">数量</th>
          </tr>
        </thead>
        <tbody id="goodsTbody${rand}">
          <s:iterator value="backGoodsList" status="index">
            <tr>
              <td><input type="hidden" name="orderBack.backGoods[<s:property value="#index.index"/>].backCode" value="${backCode}" />
              <input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].skuCode" value="${skuCode }" readonly="readonly" class="textInput" /></td>
              <td><input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].skuName" value="${skuName }" readonly="readonly" class="textInput" /></td>
              <td><input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].skuName" value="${num }"   readonly="readonly" class="textInput" /></td>
              <td><input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].totle" value="${totle}" class="textInput digits" readonly="readonly" /></td>
            </tr>
          </s:iterator>
        </tbody>
      </table>
      <h3 class="contentTitle">已发出的商品信息</h3>
      <table class="list nowrap" width="100%" style="table-layout: fixed;">
        <thead>
          <tr>
            <th align="center">SKU编码</th>
            <th align="center">SKU名称</th>
            <th align="center">IMEI</th>
          </tr>
        </thead>
        <tbody>
          <s:iterator value="itemList" status="status">
            <tr class="unitBox" target="sid_user" rel="${status.index }">
              <td align="center">${skuCode}</td>
              <td align="center">${skuName}</td>
              <td align="center">${indivCode}</td>
            </tr>
          </s:iterator>
        </tbody>
      </table>
      <h3 class="contentTitle">操作日志</h3>
      <table class="list nowrap" width="100%" style="table-layout: fixed;">
        <thead>
          <tr>
            <th align="center">操作时间</th>
            <th align="center">操作人</th>
            <th align="center">操作内容</th>
          </tr>
        </thead>
        <tbody>
          <s:iterator value="logs" status="status">
            <tr class="unitBox" target="sid_user" rel="${status.index }">
              <td align="center"><fmt:formatDate value="${opTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
              <td align="center">${opUserName}</td>
              <td align="center">${content}</td>
            </tr>
          </s:iterator>
        </tbody>
      </table>
    </div>
    <div class="formBar">
      <ul>
        <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
        <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
      </ul>
    </div>
  </form>
  <script type="text/javascript">
  $(function() {
  	$("#shippingCode${rand}").val('${backOrder.shippingCode}');
  });
  </script>
</div>
