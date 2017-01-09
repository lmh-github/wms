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
          <label>退货单号：</label> <input type="text" readonly="readonly" name="orderBack.backCode" value="${backOrder.backCode }" class="textInput">
        </p>
        <p>
          <label>退货类型：</label> <s:select disabled="true" name="orderBack.backType" value="backOrder.backType" list="#{'back':'退货','exchange':'换货'}" listKey="key" listValue="value"></s:select>
        </p>
        <p>
          <label>退货平台：</label> <s:select name="orderBack.backPlatform" value="backOrder.backPlatform" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="name" listValue="name" />
        </p>
        <p>
          <label>是否有检测单：</label> <s:select name="orderBack.hasTestBill" value="backOrder.hasTestBill" list="#{'1':'有', '0':'无'}" listKey="key" listValue="value"></s:select>
        </p>
        <p>
          <label>退回物流：</label>
          <select name="orderBack.shippingCode" id="shippingCode${rand }">
            <option value=""></option>
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
        </p>
        <p>
          <label>退回物流单号：</label> <input type="text" name="orderBack.shippingNo" value="${backOrder.shippingNo}" class="digits" size="15">
        </p>
        <p>
          <label>运费承担方：</label> <s:select name="orderBack.bearParty" value="backOrder.bearParty" list="#{'买家':'买家', '公司':'公司'}" listKey="key" listValue="value"></s:select>
        </p>
        <p>
          <label>退款金额：</label> <input type="text" name="orderBack.backMoney" value="${backOrder.backMoney }" class="number" size="15">
        </p>
        <p>
          <label>是否退回发票：</label> <s:select name="orderBack.invoice" value="backOrder.invoice" list="#{'1':'是', '0':'否'}" listKey="key" listValue="value"></s:select>
        </p>
        <dl class="nowrap">
          <dt>退货请求备注：</dt>
          <dd><textarea name="orderBack.remarkBacking" cols="90" rows="2" maxlength="100" class="textInput valid">${backOrder.remarkBacking }</textarea></dd>
        </dl>
        <dl class="nowrap">
          <dt>签收备注：</dt>
          <dd><textarea name="orderBack.remarkBacked" cols="90" rows="2" maxlength="100" class="textInput valid" readonly="readonly">${backOrder.remarkBacked }</textarea></dd>
        </dl>
        <dl class="nowrap">
          <dt>备注：</dt>
          <dd><textarea name="orderBack.remark" cols="90" rows="2" maxlength="100" class="textInput valid" >${backOrder.remark }</textarea></dd>
        </dl>
      </fieldset>
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>订单号：</label> <input name="orderBack.orderCode" type="text" value="${backOrder.orderCode}" class="textInput" readonly="readonly" id="orderCode${rand}" />
        </p>
        <p>
          <label>订单来源：</label> <s:select value="salesOrder.orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" disabled="true" />
        </p>
        <p>
          <label>下单人：</label> <input type="text" readonly="readonly" value="${salesOrder.orderUser}" class="textInput">
        </p>
        <p>
          <label>支付类型：</label> <s:select value="salesOrder.paymentType" list="@com.gionee.wms.common.WmsConstants$PaymentType@values()" listKey="code" listValue="name" disabled="true" />
        </p>
        <p>
          <label>支付方式：</label> <input type="text" readonly="readonly" value="${salesOrder.paymentName}" class="textInput">
        </p>
        <p>
          <label>配送方式：</label> <input type="text" readonly="readonly" value="${salesOrder.shippingName}" class="textInput">
        </p>
        <p>
          <label>配送单号：</label> <input type="text" readonly="readonly" value="${salesOrder.shippingNo}" class="textInput">
        </p>
        <p>
          <label>收货人：</label> <input type="text" readonly="readonly" value="${salesOrder.consignee}" class="textInput">
        </p>
        <p>
          <label>手机号：</label> <input type="text" readonly="readonly" value="${salesOrder.mobile}" class="textInput">
        </p>
        <p>
          <label>收货地址：</label> <input type="text" readonly="readonly" value="${salesOrder.province}" class="textInput">
        </p>
        <p>
          <label>商品总金额：</label> <input type="text" readonly="readonly" value="${salesOrder.goodsAmount}" class="textInput">
        </p>
        <p>
          <label>订单总金额：</label> <input type="text" readonly="readonly" value="${salesOrder.orderAmount}" class="textInput">
        </p>
        <p>
          <label>应付金额：</label> <input type="text" readonly="readonly" value="${salesOrder.payableAmount}" class="textInput">
        </p>
      </fieldset>
      <div class="divider"></div>
      <table class="list nowrap" width="100%" style="table-layout: fixed;">
        <thead>
          <tr>
            <th align="center">SKU编码</th>
            <th align="center">SKU名称</th>
            <th align="center">单位</th>
            <th align="center">数量</th>
            <th align="center">良品数量</th>
            <th align="center">次品数量</th>
          </tr>
        </thead>
        <tbody id="goodsTbody${rand}">
          <s:iterator value="backGoodsList" status="index">
            <tr>
              <td><input type="hidden" name="orderBack.backGoods[<s:property value="#index.index"/>].backCode" value="${backCode}" />
              <input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].skuCode" value="${skuCode }" readonly="readonly" class="textInput" /></td>
              <td><input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].skuName" value="${skuName }" readonly="readonly" class="textInput" /></td>
              <td><input type="text" value="${measureUnit }" readonly="readonly" class="textInput" /></td>
              <td><input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].quantity" value="${quantity }" class="textInput digits" readonly="readonly" /></td>
              <td><input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].nonDefectiveQuantity" value="${nonDefectiveQuantity }" class="textInput digits" /></td>
              <td><input type="text" name="orderBack.backGoods[<s:property value="#index.index"/>].defectiveQuantity" value="${defectiveQuantity }" class="textInput digits" /></td>
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
