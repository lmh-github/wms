<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form id="theForm" action="${ctx}/stock/stock!add.action?callbackType=closeCurrent&navTabId=tab_stock" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>仓库：</dt>
				<dd>
					<select class="required combox" name="warehouse.warehouseCode">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.warehouseCode }>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>SKU编码：</dt>
				<dd>
					<input id="inputOrg1" name="sku.id" value="" type="hidden"/>
					<input id="inputOrg2" name="sku.indivEnabled" value="" type="hidden"/>
					<input class="required" name="sku.skuCode" type="text" postField="keyword" lookupGroup="sku" />
					<a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>	
					<span class="info">(查找SKU)</span>
				</dd>
			</dl>
			<dl>
				<dt>SKU名称：</dt>
				<dd>
					<input class="readonly" name="sku.skuName" readonly="readonly" type="text"/>
				</dd>
			</dl>
			<dl>
				<dt>库存单位：</dt>
				<dd>
					<input class="readonly" name="sku.measureUnit" readonly="readonly" type="text"/>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>库存类型：</dt>
				<dd>
					<label><input type="radio"  name="stockType" class="required" value="1"/>良品库存</label>
					<label><input type="radio"  name="stockType" class="required" value="3">次品库存</label>
				</dd>
			</dl>
			<dl>
				<dt>库存数量：</dt>
				<dd>
					<input type="text" name="salesAccount"  class="required digits"/>
				</dd>
			</dl>
		</fieldset>
		
		<div id="indivCodeEdit" style="display:none;">
			<h3 class="contentTitle">商品身份码录入</h3>
			<div class="tabs">
				<div class="tabsContent" style="height: 450px;">
					<div>
						<table class="list nowrap itemDetail" addButton="添加" width="100%">
							<thead>
								<tr>
									<th type="text" name="indivCodeList[#index#].indivCode" size="30" fieldClass="required alphanumeric" fieldAttrs="{minlength:10,maxlength:10}"  onkeydown="doKeydown(event)">商品身份编码</th>
									<th type="del" width="60">操作</th>
								</tr>
							</thead>
							<tbody>
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
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="$('#theForm').submit();">保存</button></div></div></li>
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
	   url: "${ctx}/stock/stock!getSkuInfo.action?skuCode="+$("input[name='sku.skuCode']").val(),
	   success: function(data){
		  if(data!=null){
			  $("input[name='sku.skuName']").val(data.skuName);
			  $("input[name='sku.measureUnit']").val(data.wares.measureUnit);
			  if(data.wares.indivEnabled=='1'){
				  $('#indivCodeEdit').show();
				  $("input[name='sku.indivEnabled']").val('1');
			  }else{
				  $('#indivCodeEdit').hide();
				  $("input[name='sku.indivEnabled']").val('0');
				 }
		  }else{
			  $("input[name='sku.skuName']").val("");
			  $("input[name='sku.measureUnit']").val("");
			  $('#indivCodeEdit').hide();
			  $("input[name='sku.indivEnabled']").val('0');
		  }
		  
	   }
	});

});

$("input[name='salesAccount']").bind('keypress',function() {
   if($("input[name='sku.skuName']").val()==''){
		alert("请先输入正确的SKU编码");
		return false;
	}else{

	}
});

$("input[name='salesAccount']").focus(function() {
	   if($("input[name='sku.skuName']").val()!='' && $("input[name='sku.indivEnabled']").val()=="1"){
		   $('#indivCodeEdit').show();
		}else{
			$('#indivCodeEdit').hide();
		}
	});
//监听回车 
function doKeydown(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		$(".button").find("button")[0].click();
		var currIndex = theEvent.target.name.substring(theEvent.target.name.indexOf('[')+1,theEvent.target.name.indexOf(']'));
    	var nextInputName = 'indivCodeList['+(parseInt(currIndex)+1)+'].indivCode';
		$("input[name='"+nextInputName+"']").focus();//聚焦下个输入框
		return false;
	}
}
//-->
</script>



