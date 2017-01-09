<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>

<style type="text/css">#goodsTbody${rand} input[type=text]{width:90%;}</style>
<div class="pageContent">
  <form method="post" action="${ctx}/stock/morder!add.action?callbackType=closeCurrent&navTabId=tab_morder" class="pageForm required-validate" onsubmit="" id="form${rand}">
    <div class="pageFormContent" layoutH="56">
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>建单类型：</label> <select name="order.billType"><option value="赠品漏发">赠品漏发</option><option value="三包期限内质量问题补发">三包期限内质量问题补发</option><option value="系统BUG">系统BUG</option></select>
        </p>
        <p>
          <label>退货平台：</label> <s:select name="order.platform" list="@com.gionee.wms.common.WmsConstants$OrderSource@values()" listKey="code" listValue="name" />
        </p>
        <p>
          <label>是否重开发票：</label> <select name="order.invoice"><option value="0">否</option><option value="1">是</option></select>
        </p>
        <dl class="nowrap">
          <dt>建单原因：</dt>
          <dd><textarea name="order.remark" cols="90" rows="2" maxlength="100" class="textInput valid" style="height: 45px;"></textarea></dd>
        </dl>
        <dl class="nowrap">
          <dt>扩展备注：</dt>
          <dd><textarea name="order.extension" cols="90" rows="2" maxlength="100" class="textInput valid" style="height: 45px;"></textarea></dd>
        </dl>
      </fieldset>
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>输入订单号：</label> <input type="text" size="30" value="" alt="输入订单号按回车带出其他信息" class="required" id="orderCode${rand}" />
        </p>
        <div class="divider"></div>
        <p>
          <label>订单号：</label> <input type="text" name="order.orderCode" readonly="readonly" data-bind="orderCode" class="textInput required">
        </p>
        <p>
          <label>订单来源：</label> <s:select data-bind="orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerKey="" headerValue="" disabled="true" />
        </p>
        <p>
          <label>订单状态：</label> <s:select data-bind="orderStatus" list="@com.gionee.wms.common.WmsConstants$OrderStatus@values()" listKey="code" listValue="name" headerValue="" headerKey="" disabled="true" />
        </p>
        <p>
          <label>下单人：</label> <input type="text" readonly="readonly" data-bind="orderUser" class="textInput">
        </p>
        <p>
          <label>支付类型：</label> <s:select data-bind="paymentType" list="@com.gionee.wms.common.WmsConstants$PaymentType@values()" listKey="code" listValue="name" headerKey="" headerValue="" disabled="true" />
        </p>
        <p>
          <label>支付方式：</label> <input type="text" readonly="readonly" data-bind="paymentName" class="textInput">
        </p>
        <p>
          <label>配送方式：</label> <input type="text" readonly="readonly" data-bind="shippingName" class="textInput">
        </p>
        <p>
          <label>配送单号：</label> <input type="text" readonly="readonly" data-bind="shippingNo" class="textInput">
        </p>
        <p>
          <label>收货人：</label> <input type="text" readonly="readonly" data-bind="consignee" class="textInput">
        </p>
        <p>
          <label>手机号：</label> <input type="text" readonly="readonly" data-bind="mobile" class="textInput">
        </p>
        <p>
          <label>收货地址：</label> <input type="text" readonly="readonly" data-bind="province" class="textInput">
        </p>
        <p>
          <label>商品总金额：</label> <input type="text" readonly="readonly" data-bind="goodsAmount" class="textInput">
        </p>
        <p>
          <label>订单总金额：</label> <input type="text" readonly="readonly" data-bind="orderAmount" class="textInput">
        </p>
        <p>
          <label>应付金额：</label> <input type="text" readonly="readonly" data-bind="payableAmount" class="textInput">
        </p>
      </fieldset>
      <div class="divider"></div>
      <a class="button" id="addGoods${rand}" href="javascript:;"><span>新增</span></a>
      <table class="list nowrap" width="100%" style="table-layout: fixed;">
        <thead>
          <tr>
            <th align="center">SKU编码</th>
            <th align="center">SKU名称</th>
            <th align="center">单位</th>
            <th align="center">数量</th>
            <th align="center" style="width: 80px;" type="del"></th>
          </tr>
        </thead>
        <tbody id="goodsTbody${rand}">
          
        </tbody>
        <tfoot style="display: none;">
          <tr>
            <td>
                <input class="textInput readonly" name="editSkuCode" bringBackName="sku.skuCode" type="text" value="" lookupGroup="sku" readonly="readonly" style="width: 130px;" />
                <a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
            </td>
            <td><input type="text" /></td>
            <td><input type="text" /></td>
            <td><input type="text" /></td>
            <td>&nbsp;</td>
          </tr>
        </tfoot>
      </table>
    </div>
    <div class="formBar">
      <ul>
        <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
        <li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
      </ul>
    </div>
  </form>
<script type="text" id="template${rand}">
<tr class="userTr">
	<td><input type="text" name="goodsList[index].skuCode" value="%skuCode%" class="textInput" readonly="readonly" /></td>
	<td><input type="text" name="goodsList[index].skuName" value="%skuName%" class="textInput" readonly="readonly" /></td>
	<td><input type="text" value="%measureUnit%" class="textInput" readonly="readonly" /></td>
	<td><input type="text" name="goodsList[index].qty" value="%quantity%" class="digits textInput" /></td>
	<td><a href="javascript:;" onclick="$(this).closest('tr').remove();" class="btnDel">删除</a></td>
</tr>
</script>
<script type="text" id="newtr-template${rand}">
<tr class="unitBox">
  <td>
    <input class="textInput readonly required" name="goodsList[index].skuCode" bringBackName="sku.skuCode" type="text" lookupGroup="sku" readonly="readonly" style="width: 90px;" />
    <a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
  </td>
  <td><input type="text" name="goodsList[index].skuName" bringBackName="sku.skuName" class="textInput" readonly="readonly" /></td>
  <td><input type="text" bringBackName="sku.measureUnit" class="textInput" readonly="readonly" /></td>
  <td><input type="text" name="goodsList[index].qty" value="0" class="digits textInput" /></td>
  <td><a href="javascript:;" class="btnDel" onclick="$(this).closest('tr').remove();">删除</a></td>
</tr>
</script>
<script type="text/javascript">
$(function() {
	var template = $.trim($("#template${rand}").text());
	$("#form${rand}").bind("submit", function() {
		$("#goodsTbody${rand} tr").each(function(index) {
			$(this).find("input[name^=goodsList]").each(function() {
				this.name = "order.goods[" + index + "]." + this.name.replace(/goodsList.*?\./g, "");
			});
		});
		return validateCallback(this, dialogAjaxDone);
	}); // END
	
	var newTrTemplate = $.trim($("#newtr-template${rand}").text());
	$("#addGoods${rand}").bind("click", function(event) { // 新增一行商品
		event.preventDefault(); // 阻止默认事件
		$("#goodsTbody${rand}").append(newTrTemplate).initUI();		
	}); // END
	
	$("#orderCode${rand}").keydown(function(event) {
		if (event.keyCode == 13) {
    		event.preventDefault(); // 阻止默认事件
    		this.value = $.trim(this.value);
    		if(this.value.length == 0) {
    			return;
    		}
    		
    		$.post("${ctx}/stock/morder!lookupSalesOrder.action", {orderCode : this.value}, function(data) {
    			if (data.message) {
    				return alertMsg.error(data.message);
    			}
    			
    			$("#form${rand} :input[data-bind]").each(function() {
    				var $input = $(this);
    				var name = $input.data("bind");
    				$input.val(data.salesOrder[name] == null ? '' : data.salesOrder[name]);
    			});
    			
    			
    			var html = "";
    			$.each(data.goodsList, function(i, o) {
    				html += template.replace(/%(\w+)%/g, function($0, $1, $2) {
    					return o[$1] == null ? '' : o[$1];
    				});
    			});
    			var tbody = $("#goodsTbody${rand}");
    			tbody.find("tr").filter(".userTr").remove();
    			$("#goodsTbody${rand}").prepend(html).initUI();
    		}, "json");
		}
	});
});
</script>
</div>
