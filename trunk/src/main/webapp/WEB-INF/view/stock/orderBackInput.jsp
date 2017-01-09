<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>

<style type="text/css">#goodsTbody${rand} input[type=text]{width:90%;}</style>
<div class="pageContent">
  <form method="post" action="${ctx}/stock/orderBack!add.action?callbackType=closeCurrent&navTabId=tab_orderBack" class="pageForm required-validate" onsubmit="" id="form${rand}">
    <div class="pageFormContent" layoutH="56">
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>退货类型：</label> <select name="orderBack.backType"><option value="back">退货</option><option value="exchange">换货</option><option value="rejected">拒签</option></select>
        </p>
        <p>
          <label>退货平台：</label> <s:select name="orderBack.backPlatform" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="name" listValue="name" />
        </p>
        <p>
          <label>是否有检测单：</label> <select name="orderBack.hasTestBill"><option value="0">无</option><option value="1">有</option></select>
        </p>
        <p>
          <label>退回物流：</label> 
          <select name="orderBack.shippingCode">
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
          <label>退回物流单号：</label> <input type="text" name="orderBack.shippingNo" class="digits" size="15">
        </p>
        <p>
          <label>运费承担方：</label> <select name="orderBack.bearParty"><option value="买家">买家</option><option value="公司">公司</option></select>
        </p>
        <p>
          <label>退款金额：</label> <input type="text" name="orderBack.backMoney" class="number" size="15">
        </p>
        <p>
          <label>是否退回发票：</label> <select name="orderBack.invoice"><option value="1">是</option><option value="0">否</option></select>
        </p>
        <dl class="nowrap">
          <dt>退货请求备注：</dt>
          <dd><textarea name="orderBack.remarkBacking" cols="90" rows="2" maxlength="100" class="textInput valid"></textarea></dd>
        </dl>
        <dl class="nowrap">
          <dt>备注：</dt>
          <dd><textarea name="orderBack.remark" cols="90" rows="2" maxlength="100" class="textInput valid"></textarea></dd>
        </dl>
      </fieldset>
      <fieldset>
      <legend>基本信息</legend>
        <p>
          <label>输入订单号：</label> <input type="text" size="30" value="" alt="输入订单号按回车带出其他信息" class="required" id="orderCode${rand}" />
        </p>
        <div class="divider"></div>
        <p>
          <label>订单号：</label> <input type="text" name="orderBack.orderCode" readonly="readonly" data-bind="orderCode" class="textInput required">
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
            <th align="center">单价</th>
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
	<td><input type="text" value="%unitPrice%" class="number textInput" /></td>
	<td><input type="text" name="goodsList[index].quantity" value="%quantity%" class="digits textInput" /></td>
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
  <td><input type="text" class="textInput" class="number textInput" value="0" /></td>
  <td><input type="text" name="goodsList[index].quantity" value="0" class="digits textInput" /></td>
  <td><a href="javascript:;" class="btnDel" onclick="$(this).closest('tr').remove();">删除</a></td>
</tr>
</script>
<script type="text/javascript">
$(function() {
	var template = $.trim($("#template${rand}").text());
	$("#form${rand}").bind("submit", function() {
		$("#goodsTbody${rand} tr").each(function(index) {
			$(this).find("input[name^=goodsList]").each(function() {
				this.name = "goodsList[" + index + "]." + this.name.replace(/goodsList.*?\./g, "");
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
    		
    		$.post("${ctx}/stock/orderBack!lookupSalesOrder.action", {orderCode : this.value}, function(data) {
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
