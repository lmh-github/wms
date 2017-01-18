<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form id="theForm" action="<s:if test='id == null'>${ctx}/stock/stockOut!addStockOutItem.action?callbackType=closeCurrent&navTabId=tab_salesOrderEdit</s:if><s:else>${ctx}/stock/delivery!updateDeliveryIndivs.action?callbackType=closeCurrent&navTabId=tab_salesOrderEdit</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>SKU编码：</dt>
				<dd>
					<input id="inputOrg1" value="${goods.skuId }" type="hidden"/>
					<input id="inputOrg2" value="${goods.indivEnabled }" type="hidden"/>
					<input class="required"  type="text" value="${goods.skuCode }" lookupGroup="sku" ${id==null?'':'readonly="readonly"'}/>
					<c:if test="${id==null}">
						<a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
						<span class="info">(查找SKU)</span>
					</c:if>	
				</dd>
			</dl>
			<dl>
				<dt>SKU名称：</dt>
				<dd>
					<input class="readonly"  readonly="readonly" type="text" value="${goods.skuName }"/>
				</dd>
			</dl>
			<dl>
				<dt>库存单位：</dt>
				<dd>
					<input class="readonly"  readonly="readonly" type="text" value="${goods.measureUnit }"/>
				</dd>
			</dl>
			<dl>
				<dt>商品数量：</dt>
				<dd>
					<input class="readonly"  readonly="readonly"  value="${goods.quantity }" min="1"/>
				</dd>
			</dl>
		</fieldset>
		
		<div id="indivCodeEdit" style="display:${goods.indivEnabled==1?"block":"none"};">
			<h3 class="contentTitle">商品编码录入</h3>
			<div class="tabs">
				<div class="tabsContent" style="height: 300px;">
					<div>
						<table class="list nowrap itemDetail" addButton="添加" id="indivAddBtn" width="100%">
							<thead>
								<tr>
									<th type="text" name="indivList[#index#].indivCode" size="30" fieldClass="required alphanumeric" fieldAttrs="{minlength:1,maxlength:20}"  onkeydown="doKeydown(event)">商品编码</th>
									<th type="del" width="70%"></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${indivList}" var="indiv" varStatus="status">
									<tr class="unitBox">
										<td>
											<input type="text" maxlength="20" minlength="1" class="required alphanumeric textInput valid" size="30" value="${indiv.indivCode }" name="indivList[${status.index}].indivCode" onkeydown="doKeydown(event)">
										</td>
										<td>
											<a class="btnDel " href="javascript:void(0)">删除</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
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
			<s:if test="goods.enabled==0">
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div></li>
			</s:if>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
//Ajax取SKU信息
$("input[name='stockOutItem.sku.skuCode']").bind('change', function() {
   $.ajax({
	   url: "${ctx}/stock/stockOut!getSkuInfo.action?skuCode="+$("input[name='stockOutItem.sku.skuCode']").val(),
	   success: function(data){
		  if(data!=null){
			  $("input[name='stockOutItem.sku.id']").val(data.id);
			  $("input[name='stockOutItem.sku.skuName']").val(data.skuName);
			  $("input[name='stockOutItem.measureUnit']").val(data.wares.measureUnit);
			  if(data.wares.indivEnabled=='1'){
				  indivEnabled(true);
				  //$('#indivCodeEdit').show();
				  //$("#indivCodeEdit :input").removeAttr('disabled');
				  $("input[name='stockOutItem.indivEnabled']").val('1');
			  }else{
				  indivEnabled(false);
				  //$('#indivCodeEdit').hide();
				  //$("#indivCodeEdit :input").attr('disabled', true);
				  $("input[name='stockOutItem.indivEnabled']").val('0');
				 }
		  }else{
			  $("input[name='stockOutItem.sku.id']").val("");
			  $("input[name='stockOutItem.sku.skuName']").val("");
			  $("input[name='stockOutItem.measureUnit']").val("");
			  $("input[name='stockOutItem.indivEnabled']").val('0');
			  indivEnabled(false);
			  //$('#indivCodeEdit').hide();
			  //$("#indivCodeEdit :input").attr('disabled', true);
			  
		  }
		  
	   }
	});

});

$("input[name='stockOutItem.quantity']").bind('keypress',function() {
   if($("input[name='stockOutItem.sku.skuName']").val()==''){
		alert("请先输入正确的SKU编码");
		return false;
	}
});

$("input[name='stockOutItem.quantity']").focus(function() {
	   if($("input[name='stockOutItem.sku.skuName']").val()!='' && $("input[name='stockOutItem.indivEnabled']").val()=="1"){
		   indivEnabled(true);
		   //$('#indivCodeEdit').show();
		   //$("#indivCodeEdit :input").removeAttr('disabled');
		}else{
			indivEnabled(false);
			//$('#indivCodeEdit').hide();
			//$("#indivCodeEdit :input").attr('disabled', true);
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

function doSubmit(){
	$("input[name='stockOutItem.sku.id']").val($("input[name='stockOutItem.sku.id']").val());
	$('#theForm').submit();
}
//-->
</script>



