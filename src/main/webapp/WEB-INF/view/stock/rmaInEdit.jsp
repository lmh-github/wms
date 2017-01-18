<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">退货入库单</h2>
<form id="pagerForm" name="rmaInForm" action="${ctx}/stock/rmaIn!update.action?callbackType=closeCurrent&navTabId=tab_stockIn" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">

<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>入库编号：</dt>
				<dd><input readonly="true" name="stockInCode" id="stockInCode" type="text" value="${stockInCode }"/></dd>
			</dl>
			<dl>
				<dt>仓库：</dt>
				<dd>
					<select class="required" name="warehouse.warehouseCode" id="warehouseCode">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.warehouseCode } ${(item.warehouseCode==warehouse.warehouseCode)?"selected='true'":""}>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<!-- <dl>
				<dt>商品编码：</dt>
				<dd>
					<input name="indivCode" type="text" value="${indivCode }" />
				</dd>
			</dl>
			<dl>
				<dt>原订单号：</dt>
				<dd>
					<input name="originalCode" type="text" value="${originalCode }"/>
					<a id="showOrderInfo" href="javascript:void(0);" onclick="openOrderInfo();"><span style="color:blue;">查看订单信息</span></a>
				</dd>
			</dl>
			 -->
			<dl>
				<dt>退货人：</dt>
				<dd>
					<input name="opposite" type="text" maxlength="10" value="${opposite }" id="opposite"/>
				</dd>
			</dl>
			<!-- 
			<dl>
				<dt>经手人：</dt>
				<dd><input name="handledBy" id="handledBy" maxlength="10" class="required" type="text" value="${handledBy }" id="handleBy"/></dd>
			</dl>
			<dl>
				<dt>经手日期：</dt>
				<dd>
					<input type="text" name="handledDate" class="required date" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${handledDate }" pattern="yyyy-MM-dd"/>" id="handledDate"/>
					<a class="inputDateButton" href="javascript:;">选择</a>
				</dd>
			</dl> -->
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="remark" cols="80" rows="1" id="remark">${remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<h3 class="contentTitle">入库商品明细</h3>
		<div class="panelBar">
			<ul class="toolBar">
				<li><a class="add" href="javascript:void(0);" onclick="openIndivInput();"><span>添加有编号商品</span></a></li>
				<li class="line">line</li>
				<li><a class="add" href="javascript:void(0);" onclick="openRmaSkuInput();"><span>添加无编号商品</span></a></li>
			</ul>
		</div>
		<div class="tabs">
			<div class="tabsContent" style="height: 300px;">
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="60">订单号</th>
								<th width="60">SKU编码</th>
								<th width="60">SKU名称</th>
								<th width="60">单位</th>
								<th width="60">数量</th>
								<th width="60">商品编号</th>
								<th width="60">商品状态</th>
								<th width="60">操作</th>
							</tr>
						</thead>
						<tbody id="rmaInDetailTbody">
							<s:iterator value="stockInDetail">
							<tr class="unitBox">
								<td><a title="查看订单出库信息" href="${ctx}/stock/delivery!listGoods2.action?orderCode=${originalCode}" target="dialog" mask="true" width="1000" height="600" rel="dlg_orderInfo" class="">${originalCode }</a></td>
								<td>${sku.skuCode }</td>
								<td>${sku.skuName }</td>
								<td>${measureUnit }</td>
								<td>${quantity }</td>
								<th><s:if test="indivEnabled==1">${remark }</s:if></th>
								<td>
									<s:if test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@NON_DEFECTIVE.code">良品</s:if>
									<s:elseif test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@DEFECTIVE.code">次品</s:elseif>
							    </td>
								<td>
									<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/rmaIn!deleteStockInItem.action?id=${id}&navTabId=tab_inputRmaIn" class="btnDel"></a>
								</td>
							</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
		<div class="divider"></div>
		<div class="tabs">
						<dl>
							<dt>制单人：</dt>
							<dt><input readonly="true" name="field5" type="text" value="${preparedBy }"/></dt>
						</dl>
						<dl>
							<dt>制单日期：</dt>
							<dt><input readonly="true" name="field5" type="text" value="<fmt:formatDate value='${now}' dateStyle="long" pattern='yyyy-MM-dd' />"/></dt>
						</dl>
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div></li>
			<li>
			<s:if test="id==null">
				<a class="button" target="ajaxTodo" href="${ctx}/stock/stockIn!cancel.action?stockInCode=${stockInCode}&callbackType=closeCurrent&navTabId=tab_stockIn"><span>取消</span></a>			
			</s:if>
			<s:else>
				<div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div>
			</s:else>
			</li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
//Ajax取订单信息
var $form=$("form[name=rmaInForm]");

$form.find("input[name='indivCode']").bind('change', function() {
   $.ajax({
	   url: "${ctx}/stock/rmaIn!getIndivInfo.action?indivCode="+$form.find("input[name='indivCode']").val(),
	   success: function(data){
		  if(data!=null){
			  if(data.stockStatus!='3'){
				 alert("此商品编码对应的商品尚未出库");
				 $form.find("input[name='indivCode']").val("");
				 $form.find("input[name='originalCode']").val("");
				return;
			  }
			  $form.find("input[name='originalCode']").val(data.orderCode);
		  }else{
			  alert("此商品编码不存在");
			  $form.find("input[name='indivCode']").val("");
			  $form.find("input[name='originalCode']").val("");
			  
		  }
	   }
	});
});
$form.find("input[name='originalCode']").bind('change', function() {
	   $.ajax({
		   url: "${ctx}/stock/rmaIn!getOrderInfo.action?orderCode="+$form.find("input[name='originalCode']").val(),
		   success: function(data){
			  if(data!=null){
				  if(data.orderStatus!="<s:property value='@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code'/>"){
					alert("此订单号对应订单尚未出库");
					$form.find("input[name='indivCode']").val("");
					$form.find("input[name='originalCode']").val("");
					return;
				  }
				  $form.find("input[name='originalCode']").val(data.orderCode);
			  }else{
				  alert("此订单号不存在");
				  $form.find("input[name='indivCode']").val("");
				  $form.find("input[name='originalCode']").val("");
				  
			  }
		   }
		});
	});
//查看订单信息
function openOrderInfo(){
	var $form=$("form[name=rmaInForm]");
	if($form.find("input[name='originalCode']").val()==""){
		alert("请先输入订单号");
		return;
	}
	$.pdialog.open('${ctx}/stock/delivery!listGoods2.action?orderCode='+$form.find("input[name='originalCode']").val(), 'dlg_orderInfo', '添加入库商品', {width:800,height:800,mask:true});
}
//添加入库SKU
function openSkuInput(){
	var $form=$("form[name=rmaInForm]");
	if($form.find("input[name='originalCode']").val()==""){
		alert("请先输入原订单号");
		return;
	}
	//$form.find("input[name='warehouse.id']").val($("#warehouseCode").val());
	//$form.find("input[name='opposite']").val($("#opposite").val());
	//$form.find("input[name='handledBy']").val($("#handledBy").val());
	//$form.find("input[name='handledDate']").val($("#handledDate").val());
	//$form.find("input[name='remark']").val($("#remark").val());
	var skuInfoUrl = '${ctx}/stock/rmaIn!inputSkuItem.action?inCode='+$form.find("input[name='stockInCode']").val();
	$.pdialog.open(skuInfoUrl, 'dlg_rmaSkuInput', '添加无编号商品', {width:800,height:800,mask:true});
}
//添加入库个体
function openIndivInput(){//alert($form.find("input[name='stockInCode']").val());
	var skuInfoUrl = '${ctx}/stock/rmaRecv!inputIndivItem.action?stockInCode='+$("#stockInCode").val();//$form.find("input[name='stockInCode']").val();
	$.pdialog.open(skuInfoUrl, 'dlg_rmaIndivInput', '添加有编号商品', {width:800,height:800,mask:true});
}
//添加入库Sku
function openRmaSkuInput(){//alert($form.find("input[name='stockInCode']").val());
	var skuInfoUrl = '${ctx}/stock/rmaRecv!inputSkuItem.action?stockInCode='+$("#stockInCode").val();//$form.find("input[name='stockInCode']").val();
	$.pdialog.open(skuInfoUrl, 'dlg_rmaSkuInput', '添加无编号商品', {width:800,height:800,mask:true});
}
function cancelStockIn(){
	$.pdialog.open('${ctx}/stock/stockIn!inputInItem.action?stockInCode='+$("#rmaInForm input[name='stockInCode']").val(), 'dlg_stockIn', '添加入库商品', {width:800,height:800,mask:true});
}
function doSubmit(){
	if($('#rmaInDetailTbody tr').length==0){
		alert("入库商品明细不能为空");
		return false;
	}
	if(confirm("保存后商品库存将会相应增加，确定要提交吗？")){
		$("form[name=rmaInForm]").submit();
	}
}
//-->
</script>