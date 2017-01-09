<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle"></h2>
<form name="rmaSkuForm" action="${ctx}/stock/rmaIn!addInItem.action?callbackType=closeCurrent&navTabId=tab_inputRmaIn" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>


<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>退货入库编号：</dt>
				<dd><input readonly="readonly" name="stockInItem.stockIn.stockInCode" type="text" value="${stockInCode }"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>销售订单号：</dt>
				<dd>
					<input name="stockInItem.originalCode" class="required" type="text" maxlength="20" value="${indivFlow.originalCode }"/>
					<a id="showOrderInfoLink" style="display:none;" href="javascript:void(0);" onclick="openOrderInfo();"><span style="color:blue;">查看订单信息</span></a>
				</dd>
			</dl>
		</fieldset>
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>SKU编码：</dt>
				<dd>
					<input id="inputOrg1" name="stockInItem.skuId" bringBackName="sku.id" value="${stockInItem.sku.id }" type="hidden"/>
					<input id="inputOrg2" name="stockInItem.indivEnabled" bringBackName="sku.indivEnabled" value="${stockInItem.indivEnabled }" type="hidden"/>
					<input class="required" name="sku.skuCode" bringBackName="sku.skuCode" type="text" maxlength="10" value="${stockInItem.sku.skuCode }" lookupGroup="sku" ${id==null?'':'readonly="readonly"'}/>
					<c:if test="${id==null}">
						<a class="btnLook" href="${ctx}/wares/sku!lookup.action?wares.indivEnabled=0" lookupGroup="sku" width="1200">查找SKU</a>
						<span class="info">(查找SKU)</span>
					</c:if>	
				</dd>
			</dl>
			<dl>
				<dt>SKU名称：</dt>
				<dd>
					<input class="readonly" name="sku.skuName" bringBackName="sku.skuName" readonly="readonly" type="text" value="${stockInItem.sku.skuName }"/>
				</dd>
			</dl>
			<dl>
				<dt>库存单位：</dt>
				<dd>
					<input class="readonly" name="stockInItem.measureUnit" bringBackName="sku.measureUnit" readonly="readonly" type="text" value="${stockInItem.measureUnit }"/>
				</dd>
			</dl>
			<dl>
				<dt>数量：</dt>
				<dd>
					<input type="text" name="stockInItem.quantity"  class="required digits" maxlength="7" value="${stockInItem.quantity }" min="1"/>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>商品状态：</dt>
				<dd>
					<label><input type="radio"  name="stockInItem.waresStatus" class="required" value="1" ${stockInItem.waresStatus==null?"":(stockInItem.waresStatus=='1'?"checked='checked'":"") }/>良品</label>
					<label><input type="radio"  name="stockInItem.waresStatus" class="required" value="0" ${stockInItem.waresStatus==null?"":(stockInItem.waresStatus=='0'?"checked='checked'":"") }/>次品</label>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="stockInItem.remark" cols="80" rows="1">${stockInItem.remark }</textarea></dd>
			</dl>
		</fieldset>
	</div>
	
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
//Ajax取订单信息
var $form=$("form[name=rmaSkuForm]");

$form.find("input[name='stockInItem.originalCode']").bind('change', function() {
	   $.ajax({
		   url: "${ctx}/stock/rmaIn!getOrderInfo.action?orderCode="+$form.find("input[name='stockInItem.originalCode']").val(),
		   success: function(data){
			  if(data!=null){
				  if(data.shippingStatus!="<s:property value='@com.gionee.wms.common.WmsConstants$ShippingStatus@SHIPPED.code'/>"){
					alert("此订单号对应订单尚未出库");
					$form.find("input[name='stockInItem.originalCode']").val("");
					$("#showOrderInfoLink").hide();
					return;
				  }
				  $("#showOrderInfoLink").show();
			  }else{
				  alert("此订单号不存在");
				  $form.find("input[name='stockInItem.originalCode']").val("");
				  $("#showOrderInfoLink").hide();
			  }
		   }
		});
	});

//查看订单信息
function openOrderInfo(){
	var $form=$("form[name=rmaSkuForm]");
	if($form.find("input[name='indivFlow.originalCode']").val()==""){
		alert("请先输入订单号");
		return;
	}
	$.pdialog.open('${ctx}/stock/delivery!listGoods2.action?orderCode='+$form.find("input[name='stockInItem.originalCode']").val(), 'dlg_orderInfo', '订单信息', {width:1024,height:768,mask:true});
}

//-->
</script>