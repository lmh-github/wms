<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form id="theForm" action="${ctx}/stock/purchaseRecv!addGoods.action?callbackType=closeCurrent" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, purRecvNewAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>SKU编码：</dt>
				<dd>
					<input id="inputOrg1" name="goods.skuId" bringBackName="sku.id" value="${goods.skuId }" type="hidden"/>
					<input id="inputOrg2" name="goods.indivEnabled" bringBackName="sku.indivEnabled" value="${goods.indivEnabled }" type="hidden"/>
					<input class="required" name="goods.skuCode" bringBackName="sku.skuCode" type="text" value="${goods.skuCode }" lookupGroup="sku" />
					<a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
					<span class="info">(查找SKU)</span>	
				</dd>
			</dl>
			<dl>
				<dt>SKU名称：</dt>
				<dd>
					<input class="readonly" name="goods.skuName" bringBackName="sku.skuName" readonly="readonly" type="text" value="${goods.skuName }"/>
				</dd>
			</dl>
			<dl>
				<dt>库存单位：</dt>
				<dd>
					<input class="readonly" name="goods.measureUnit" bringBackName="sku.measureUnit" readonly="readonly" type="text" value="${goods.measureUnit }"/>
				</dd>
			</dl>
			<dl>
				<dt>收货数量：</dt>
				<dd>
					<input type="text" name="goods.quantity" maxlength="7" class="required digits" value="${goods.quantity }" min="1"/>
				</dd>
			</dl>
			<dl>
				<dt>批次号：</dt>
				<dd>
					<input type="text" name="goods.productBatchNo" maxlength="15"  class="digits" value="${goods.productBatchNo }"/>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>商品状态：</dt>
				<dd>
					<label><input type="radio"  name="goods.waresStatus" class="required" value="1" ${goods.waresStatus==null?"checked='checked'":(goods.waresStatus=='1'?"checked='checked'":"") }/>良品</label>
					<label><input type="radio"  name="goods.waresStatus" class="required" value="0" ${goods.waresStatus==null?"":(goods.waresStatus=='0'?"checked='checked'":"") }/>次品</label>
				</dd>
			</dl>
		</fieldset>
		
		<div id="indivCodeEdit" style="display:${goods.indivEnabled==1?"block":"none"};">
			<h3 class="contentTitle">商品身份码录入</h3>
			<div class="tabs">
				<div class="tabsContent" style="height: 250px;">
					<div>
						<textarea name="indivText" value="" rows="20" cols="40">${indivText}</textarea>
					</div>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
//Ajax取SKU信息
$("input[name='goods.skuCode']").bind('change', function() {
   $.ajax({
	   url: "${ctx}/stock/purchaseRecv!getSkuInfo.action?skuCode="+$("input[name='goods.skuCode']").val(),
	   success: function(data){
		  if(data!=null){
			  $("input[name='goods.skuId']").val(data.id);
			  $("input[name='goods.skuName']").val(data.skuName);
			  $("input[name='goods.measureUnit']").val(data.wares.measureUnit);
			  if(data.wares.indivEnabled=='1'){
				  indivEnabled(true);
				  $("input[name='goods.indivEnabled']").val('1');
			  }else{
				  indivEnabled(false);
				  $("input[name='goods.indivEnabled']").val('0');
				 }
		  }else{
			  $("input[name='goods.skuId']").val("");
			  $("input[name='goods.skuName']").val("");
			  $("input[name='goods.measureUnit']").val("");
			  $("input[name='goods.indivEnabled']").val('0');
			  indivEnabled(false);
		  }
		  $("input[name='goods.quantity']").val("");
	   }
	});

});

$("input[name='goods.quantity']").bind('keypress',function() {
   if($("input[name='goods.skuName']").val()==''){
		alert("请先输入正确的SKU编码");
		return false;
	}
});

$("input[name='goods.quantity']").focus(function() {
	   if($("input[name='goods.skuName']").val()!='' && $("input[name='goods.indivEnabled']").val()=="1"){
		   indivEnabled(true);
		}else{
			indivEnabled(false);
		}
	});
//监听回车 
function doKeydown(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		$("#indivAddBtn").click();
		var currIndex = theEvent.target.name.substring(theEvent.target.name.indexOf('[')+1,theEvent.target.name.indexOf(']'));
    	var nextInputName = 'indivList['+(parseInt(currIndex)+1)+'].indivCode';
		$("input[name='"+nextInputName+"']").focus();//聚焦下个输入框
		return false;
	}
}

function indivEnabled(enabled){
	if(enabled){
		$('#indivCodeEdit').show();
		$("#indivCodeEdit :input").removeAttr('disabled');
	}else{
		$('#indivCodeEdit').hide();
		$("#indivCodeEdit :input").attr('disabled', true);
	}
}
//自定义DWZ回调函数
function purRecvNewAjaxDone(json){
	DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
    	$.pdialog.closeCurrent();
    	$.pdialog.reload('${ctx}/stock/purchaseRecv!inputManual.action?id=${id}',{dialogId:'dlg_purchaseRecvInputManual'});
    }
}
function doSubmit(){
	$('#theForm').submit();
}
//-->
</script>



