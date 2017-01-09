<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form id="theForm" action="<s:if test='id == null'>${ctx}/stock/rmaIn!addInItem.action?callbackType=closeCurrent&navTabId=tab_inputPurchaseIn</s:if><s:else>${ctx}/stock/rmaIn!updateInItem.action?callbackType=closeCurrent&navTabId=tab_inputPurchaseIn</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="stockInItem.stockIn.stockInCode" value="${stockInItem.stockIn.stockInCode}"/>
<input type="hidden" name="stockInItem.originalCode" value="${stockInItem.originalCode}"/>
<div class="pageContent">
	<div class="pageFormContent">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>SKU编码：</dt>
				<dd>
					<input id="inputOrg1" name="stockInItem.sku.id" bringBackName="sku.id" value="${stockInItem.sku.id }" type="hidden"/>
					<input id="inputOrg2" name="stockInItem.indivEnabled" bringBackName="sku.indivEnabled" value="${stockInItem.indivEnabled }" type="hidden"/>
					<input class="required" name="sku.skuCode" bringBackName="sku.skuCode" type="text" value="${stockInItem.sku.skuCode }" lookupGroup="sku" ${id==null?'':'readonly="readonly"'}/>
					<c:if test="${id==null}">
						<a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
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
					<input type="text" name="stockInItem.quantity"  class="required digits" value="${stockInItem.quantity }" min="1"/>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>商品状态：</dt>
				<dd>
					<label><input type="radio"  name="stockInItem.waresStatus" class="required" value="1" ${stockInItem.waresStatus==null?"checked='checked'":(stockInItem.waresStatus=='1'?"checked='checked'":"") }/>良品</label>
					<label><input type="radio"  name="stockInItem.waresStatus" class="required" value="0" ${stockInItem.waresStatus==null?"":(stockInItem.waresStatus=='0'?"checked='checked'":"") }/>次品</label>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="stockInItem.remark" cols="80" rows="1">${stockInItem.remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<div id="indivCodeEdit" style="display:${stockInItem.indivEnabled==1?"block":"none"};">
			<h3 class="contentTitle">商品身份码录入</h3>
			<div class="tabs">
				<div class="tabsContent" style="height: 300px;">
					<div>
						<table class="list nowrap itemDetail" addButton="添加" id="indivAddBtn" width="100%">
							<thead>
								<tr>
									<th type="text" name="indivList[#index#].indivCode" size="30" fieldClass="required alphanumeric" fieldAttrs="{minlength:10,maxlength:10}"  onkeydown="doKeydown(event)">商品身份编码</th>
									<th type="del" width="60">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${indivList}" var="indiv" varStatus="status">
									<tr class="unitBox">
										<td>
											<input type="text" maxlength="10" minlength="10" class="required alphanumeric textInput valid" size="30" value="${indiv.indivCode }" name="indivList[${status.index}].indivCode" onkeydown="doKeydown(event)">
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
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
//Ajax取SKU信息
$("input[name='sku.skuCode']").bind('change', function() {
   $.ajax({
	   url: "${ctx}/stock/stockIn!getSkuInfo.action?skuCode="+$("input[name='sku.skuCode']").val(),
	   success: function(data){
		  if(data!=null){
			  $("input[name='stockInItem.sku.id']").val(data.id);
			  $("input[name='sku.skuName']").val(data.skuName);
			  $("input[name='stockInItem.measureUnit']").val(data.wares.measureUnit);
			  if(data.wares.indivEnabled=='1'){
				  indivEnabled(true);
				  //$('#indivCodeEdit').show();
				  //$("#indivCodeEdit :input").removeAttr('disabled');
				  $("input[name='stockInItem.indivEnabled']").val('1');
			  }else{
				  indivEnabled(false);
				  //$('#indivCodeEdit').hide();
				  //$("#indivCodeEdit :input").attr('disabled', true);
				  $("input[name='stockInItem.indivEnabled']").val('0');
				 }
		  }else{
			  $("input[name='stockInItem.sku.id']").val("");
			  $("input[name='sku.skuName']").val("");
			  $("input[name='stockInItem.measureUnit']").val("");
			  $("input[name='stockInItem.indivEnabled']").val('0');
			  indivEnabled(false);
			  //$('#indivCodeEdit').hide();
			  //$("#indivCodeEdit :input").attr('disabled', true);
			  
		  }
		  
	   }
	});

});

$("input[name='stockInItem.quantity']").bind('keypress',function() {
   if($("input[name='sku.skuName']").val()==''){
		alert("请先输入正确的SKU编码");
		return false;
	}
});

$("input[name='stockInItem.quantity']").focus(function() {
	   if($("input[name='sku.skuName']").val()!='' && $("input[name='stockInItem.indivEnabled']").val()=="1"){
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
	$("input[name='stockInItem.sku.id']").val($("input[name='stockInItem.sku.id']").val());
	$('#theForm').submit();
}
//-->
</script>



