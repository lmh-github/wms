<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="indivForm2" onsubmit="return validateCallback(this, navTabAjaxDone)" action="${ctx}/stock/salesPrepare!confirm.action?callbackType=forward&forwardUrl=${ctx}/stock/salesPrepare.action?orderId=${salesOrder.id}" method="post" class="pageForm required-validate">
<div class="pageHeader">
	<div class="pageFormContent">
		<fieldset>
			<legend>订单信息</legend>
			<dl>
				<dt>订单号：</dt>
				<dd>${salesOrder.orderCode }</dd>
			</dl>
			<dl>
				<dt>配送方式：</dt>
				<dd>${salesOrder.shippingName }</dd>
			</dl>
			<dl>
				<dt>发票抬头：</dt>
				<dd>${salesOrder.invoiceTitle }</dd>
			</dl>
			<dl>
				<dt>发票金额：</dt>
				<dd>${salesOrder.invoiceAmount }</dd>
			</dl>
			<dl>
				<dt>收货人：</dt>
				<dd>${salesOrder.consignee }</dd>
			</dl>
			<dl>
				<dt>地址：</dt>
				<dd><span style="width: 100px">${salesOrder.fullAddress }</span></dd>
			</dl>
			<dl>
				<dt>电话：</dt>
				<dd>${salesOrder.tel }</dd>
			</dl>
			<dl>
				<dt>手机：</dt>
				<dd>${salesOrder.mobile }</dd>
			</dl>
			<dl>
				<dt>最佳送货时间：</dt>
				<dd>${salesOrder.bestTime }</dd>
			</dl>
			<dl>
				<dt>客户订单附言：</dt>
				<dd>${salesOrder.postscript }</dd>
			</dl>
		</fieldset>
	</div>
	<div class="pageFormContent nowrap">
		<dl>
			<dt>发货单:</dt>
			<dd style="background-color:green; color:#ffffff;font-size:12pt;line-height: normal;">${salesOrder.deliveryCode }
			<input id="orderId" name="orderId" type="hidden" value="${salesOrder.id }"/></dd>
			
		</dl>
		<dl id="scanIndiv">
			<dt>扫描个体编码：</dt>
			<dd><input id="indivCode1" name="indivCode" type="text" class="textInput" onkeydown="doKeydown(event, this)" /><span id="scanCode" style="color: red; font-size: 12pt; float: left;"></span>
			<!--a href="javascript:;" onclick="submitIndiv()"><input type="button" value="扫描完成" /></a--></dd>
		</dl>
		<dl id="gweight"  style="display: none;">
			<dt>称重：</dt>
			<dd><input id="weight" name="weight" type="text" class="required number textInput" onkeydown="weightChange(event, this)"/></dd>
		</dl>
		<dl id="scanKuaidi" style="display: none;">
			<dt>扫描快递运单：</dt>
			<dd><input id="shippingNo" name="shippingNo" type="text" class="required textInput" size="30" onkeydown="toSubmit(event, this)"/></dd>
		</dl>
	</div>
	<div class="subBar">
		<span id="errorTips" style="color: red; font-size: 12pt; float: left;"></span>
	</div>
	<div class="formBar">
		<ul id="salesSubmit1" style="display: none;">
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">提交</button></div></div></li>
		</ul>
	</div>
</div>
<div class="pageContent" id="goodsBox" style="overflow: auto;">
</div>
</form>
<script type="text/javascript">
var $indivForm2 = $("#indivForm2", navTab.getCurrentPanel());
var indivCodes1 = new HashSet();

// 防止回车提交表单
document.getElementById("indivForm2").onkeydown = function(e){
  var e = e || event;
  var keyNum = e.keyCode || e.which || e.charCode;
  return keyNum==13 ? false : true;
};

//监听输入框回车 
function doKeydown(event, obj) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		prepareIndiv(obj.value);
	}
}

// 配货
function prepareIndiv(value) {
	scanCode(value);
	var codeInput = $("#indivCode1", navTab.getCurrentPanel());
	codeInput.attr("readonly", "readonly");
	codeInput.attr("class", "textInput readonly");
	var orderId = $('#orderId').val();
	var trObj1 = $("#prepare", navTab.getCurrentPanel()).find("tr[rel='"+value+"']");	// 查找sku元素
	if(trObj1.length>0 && trObj1.attr("indiv")==0) {
		updateTr(trObj1);
	} else {
		$.ajax({
		   url: "${ctx}/stock/salesPrepare!prepareIndiv.action",
		   data:$indivForm2.serialize(),
		   success: function(data){
			   	if(data.ok==true) {
			   		var indiv = data.result;
			   		scanCode(data.message);
			   		$("#goodsBox").loadUrl("${ctx}/stock/salesPrepare!loadGoods.action",$indivForm2.serialize(),confirmFinish);
			   	} else {
			   		errorTips(data.message);
			   		soundError();
			   		return false;
			   	}
		   },
		   error: function (XMLHttpRequest, textStatus, errorThrown) {     
				alert(errorThrown);     
			}
		});
	}
	codeInput.removeAttr("readonly");
	codeInput.attr("class", "textInput");
	codeInput.val("");//清空输入框
	codeInput.focus();//聚焦输入框
}

function batchPrepare(goodsId, quantity) {
	$.ajax({
		url:"${ctx}/stock/salesPrepare!batchPrepare.action",
		data:"goodsId="+goodsId+"&preparedNum="+quantity,
		success: function(data){
		   	if(data.ok==true) {
		   		// 更新商品个体列表
		   		$("#goodsBox").loadUrl("${ctx}/stock/salesPrepare!loadGoods.action",$indivForm2.serialize(),confirmFinish);
		   	} else {
		   		errorTips(data.message);
		   		return false;
		   	}
		},
		  error: function (XMLHttpRequest, textStatus, errorThrown) {     
			alert(errorThrown);
		}
	});
}
/*
//更新商品配货量
function updateTr(trObj) {
  	if(trObj.attr("done")== "true") {
  		errorTips("已达到sku申请数量");
  		soundError();
  		return false;
  	}
	var needNum = trObj.attr("nnum");	// 获得所需数量
  	var quantity = trObj.find("input[name=quantity]");	// 提交值
  	var fetchNum = trObj.find("td[name=fetchNum]");// 显示值
  	var nnum=parseInt(needNum);
  	var qnum=parseInt(quantity.val());
  	qnum=qnum+1;
  	quantity.val(qnum);
  	fetchNum.html(qnum);
  	if(qnum==nnum) {
  		// 达到配货数量
  		trObj.css({"color":"#ffffff","background-color":"green"});
  		trObj.attr("done", "true");
  	} else {
	  	trObj.css({"color":"#ffffff","background-color":"coral"});
  	}
  	submitIndiv();
}

// 批量配货
function prepareBatch(obj) {
	var trObj = $(obj).closest("tr");
	var needNum = trObj.attr("nnum");	// 获得所需数量
	trObj.find("td[name=fetchNum]").html(needNum);
	trObj.find("input[name=quantity]").val(needNum);
	trObj.find("td[name=operator]").html("");
	trObj.css({"color":"#ffffff","background-color":"green"});
	trObj.attr("done", "true");
	submitIndiv();
}

// 提交扫描的单品,显示扫描快递
function submitIndiv() {
	scanCode("");
	var skuCodes = $("#prepare", navTab.getCurrentPanel()).find("input[name=skuCodes]");
	if(skuCodes.length==0) {
		errorTips("没有商品");
		return false;
	}
	var trObj = $("#prepare", navTab.getCurrentPanel()).find("tr[done='false']");
	if(trObj.length==0) {
		var dlIndiv=$("#scanIndiv", navTab.getCurrentPanel());		//扫描个体
		var dlWeight=$("#gweight", navTab.getCurrentPanel());	//称重
		dlIndiv.find("dd").html("<div style=\"background-color:green; color:#ffffff;font-size:12pt;\">单品扫描完成</div>");
		dlWeight.show();
		dlWeight.find("input[name=weight]").focus();
		$("#salesSubmit1", navTab.getCurrentPanel()).show();
		return true;
	}
}
*/
function weightChange(event, obj) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		var dlKuaidi=$("#scanKuaidi", navTab.getCurrentPanel());	// 快递dlKuaidi.show();
		dlKuaidi.show();
		dlKuaidi.find("input[name=shippingNo]").focus();
		$("#salesSubmit1", navTab.getCurrentPanel()).show();
	}
}

function toSubmit(event, obj) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		//判断运单号长度
		var shippingNo = $('#shippingNo').val();
		if(shippingNo.length<10 || shippingNo.length>13){
			errorTips("请检查运单号码"+shippingNo);
			$('#shippingNo').val("");
			return false;
		}
		obj.blur(); // 移除焦点，防止多次确认
		$(obj).closest("form").submit();
	}
}

function scanCode(message) {
	$("#scanCode", navTab.getCurrentPanel()).html(message);
	errorTips("");
}

function errorTips(message) {
	$("#errorTips", navTab.getCurrentPanel()).html(message);
	if(message != "") {alert(message);}
}

// 确认是否已完成
function confirmFinish() {
	var finished = $("#finished", navTab.getCurrentPanel());
	if(finished.val() == 'true') {
		var dlIndiv=$("#scanIndiv", navTab.getCurrentPanel());		//扫描个体
		var dlWeight=$("#gweight", navTab.getCurrentPanel());	//称重
		dlIndiv.find("dd").html("<div style=\"background-color:green; color:#ffffff;font-size:12pt;\">单品扫描完成</div>");
		dlWeight.show();
		dlWeight.find("input[name=weight]").focus();
		$("#salesSubmit1", navTab.getCurrentPanel()).show();
		return true;
	}
}

$(function(){
	// 页面初始化加载表单
	$("#goodsBox").loadUrl("${ctx}/stock/salesPrepare!loadGoods.action",$indivForm2.serialize(),confirmFinish);
	setTimeout(function() {
		$("#indivCode1", navTab.getCurrentPanel()).focus();	
	},1000);
});
</script>