<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle"></h2>
<form name="rmaIndivForm" action="${ctx}/stock/rmaRecv!addIndivItem.action?callbackType=closeCurrent&navTabId=dlg_rmaRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="indivFlow.skuId" value="${indivFlow.skuId}"/>
<input type="hidden" name="id" value="${receive.id}"/>

<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset id="skuInfoArea">
			<dl class="nowrap">
				<dt>商品编码：</dt>
				<dd><input name="indivFlow.indivCode" class="required" type="text" maxlength="10" value="${indivFlow.indivCode }"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>销售订单号：</dt>
				<dd>
					<input readonly="readonly"  name="indivFlow.originalCode" class="required" type="text" value="${indivFlow.originalCode }"/>
					<a id="showOrderInfo" href="javascript:void(0);" onclick="openOrderInfo();"><span style="color:blue;">查看订单信息</span></a>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>SKU编码：</dt>
				<dd><input readonly="readonly"  name="indivFlow.skuCode" type="text" value="${indivFlow.skuCode }"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>SKU名称：</dt>
				<dd>
					<input readonly="readonly"  class="readonly" name="indivFlow.skuName" type="text" value="${indivFlow.skuName }"/>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>库存单位：</dt>
				<dd>
					<input readonly="readonly"  class="readonly" name="indivFlow.measureUnit" type="text" value="${indivFlow.measureUnit }"/>
				</dd>
			</dl>
		</fieldset>
		<fieldset>
			<dl class="nowrap">
				<dt>商品状态：</dt>
				<dd>
					<label><input type="radio"  name="indivFlow.waresStatus" class="required" value="1" ${indivFlow.waresStatus==null?"":(indivFlow.waresStatus=='1'?"checked='checked'":"") }/>良品</label>
					<label><input type="radio"  name="indivFlow.waresStatus" class="required" value="0" ${indivFlow.waresStatus==null?"":(indivFlow.waresStatus=='0'?"checked='checked'":"") }/>次品</label>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="indivFlow.remark" cols="80" rows="1">${indivFlow.remark }</textarea></dd>
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
var $form=$("form[name=rmaIndivForm]");

$form.find("input[name='indivFlow.indivCode']").bind('change', function() {
   $.ajax({
	   url: "${ctx}/stock/rmaRecv!getIndivInfo.action?indivCode="+$form.find("input[name='indivFlow.indivCode']").val(),
	   success: function(data){
		  if(data!=null){
			  if(data.stockStatus!='4'){
				 alert("此商品编码对应的商品尚未出库");
				 $("#skuInfoArea").find("input").each(function(){
					 $(this).val("");
				 });
				return;
			  }
			  $form.find("input[name='indivFlow.skuId']").val(data.skuId);
			  $form.find("input[name='indivFlow.skuCode']").val(data.skuCode);
			  $form.find("input[name='indivFlow.skuName']").val(data.skuName);
			  $form.find("input[name='indivFlow.measureUnit']").val(data.measureUnit);
			  $form.find("input[name='indivFlow.originalCode']").val(data.orderCode);
		  }else{
			  alert("此商品编码不存在");
			 $("#skuInfoArea").find("input").each(function(){
				 $(this).val("");
			 });
			  
		  }
	   }
	});
});

//查看订单信息
function openOrderInfo(){
	var $form=$("form[name=rmaIndivForm]");
	if($form.find("input[name='indivFlow.originalCode']").val()==""){
		alert("请先输入订单号");
		return;
	}
	$.pdialog.open('${ctx}/stock/delivery!listGoods2.action?orderCode='+$form.find("input[name='indivFlow.originalCode']").val(), 'dlg_orderInfo', '订单信息', {width:800,height:800,mask:true});
}

//-->
</script>