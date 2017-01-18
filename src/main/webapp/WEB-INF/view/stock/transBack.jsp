<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="pagerForm" action="${ctx}/stock/transfer!confirmBack.action?callbackType=forward&forwardUrl=${ctx}/stock/transfer!transBackInput.action&navTabId=tab_transBack" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
<div class="pageHeader">
	<div class="pageFormContent nowrap">
		<dl>
			<dt>退货仓库：</dt>
			<dd>
                <input type="text" readonly="readonly" value="东莞电商仓" />
                <input type="hidden" value="1643" name="warehouseId" />
			</dd>
		</dl>
		<dl id="scanIndiv">
			<dt>扫描个体编码：</dt>
			<dd><input id="indivCode2" name="indivCode" type="text" class="textInput" onKeydown="doKeydown(event, this)"/><span id="scanCode" style="color: red; font-size: 12pt; float: left;"></span></dd>
		</dl>
		<dl>
			<dd><input type="radio" name="waresStatus" value="1" checked="checked">良品</input><input type="radio" name="waresStatus" value="2">次品</input></dd>
		</dl>
		</ul>
		<div class="subBar">
			<span id="errorTips" style="color: red; font-size: 12pt; float: left;"></span>
		</div>
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="$(this).closest('form').submit();">核对数据，确认退回</button></div></div></li>
		</ul>
	</div>
</div>
<div class="pageContent" id="goodsBox">
<h2 class="contentTitle" align="center">已扫描列表</h2>
<table id="scan_indivs" class="list" width="100%" height="100%">
	<thead>
		<tr>
			<th>个体编码</th>
			<th>sku编号</th>
			<th>sku名称</th>
			<th>良次品状态</th>
			<th>个体或配件</th>
			<th>发货仓</th>
			<th>收货地址</th>
			<th>调拨批次号</th>
			<th>操作</th>
		</tr>
	</thead>
</table>
</div>
</form>
<script type="text/javascript">
var $pagerForm = $("#pagerForm", navTab.getCurrentPanel());
//防止回车提交表单
$pagerForm.keydown(function(e){
	var e = e || event;
	  var keyNum = e.keyCode || e.which || e.charCode;
	  return keyNum==13 ? false : true;
});

//监听输入框回车 
function doKeydown(event, obj) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		prepareIndiv(obj.value);
	}
}

//扫描内容提示
function scanCode(message) {
	$("#scanCode", navTab.getCurrentPanel()).html(message);
	$("#errorTips", navTab.getCurrentPanel()).html("");
}

//错误提示
function errorTips(message) {
	$("#errorTips", navTab.getCurrentPanel()).html(message);
	soundError();
	alert(message);
}

//个体扫描
function prepareIndiv(value) {
	if($.trim(value) == ''){
		errorTips('请扫描商品');
		$("#indivCode2", navTab.getCurrentPanel()).val("");//清空输入框
		$("#indivCode2", navTab.getCurrentPanel()).focus();//聚焦下个输入框
		return;
	}
	if($.trim(value).length != 15){
		errorTips('请输入15位串号');
		$("#indivCode2", navTab.getCurrentPanel()).val("");//清空输入框
		$("#indivCode2", navTab.getCurrentPanel()).focus();//聚焦下个输入框
		return;
	}	
	scanCode(value);
	var scanedFlag = false;
	$("#scan_indivs tr td:nth-child(1)").each(function () {
		var scanedCode = $(this).find('input').val();
		if(value == scanedCode){
			scanedFlag = true;
			return;
		}
	});
	if(scanedFlag){
		errorTips('该商品个体已经扫描');
		$("#indivCode2", navTab.getCurrentPanel()).val("");//清空输入框
		$("#indivCode2", navTab.getCurrentPanel()).focus();//聚焦下个输入框
		return;
	}
	$.ajax({
	   url: "${ctx}/stock/transfer!scanIndiv.action",
	   data: $pagerForm.serialize(),
	   success: function(data){
		   	if(data.ok==true) {
		   		//加入table
		   		var waresStatusStr = "良品";
		   		if(data.result.waresStatus == 0){
		   			waresStatusStr = "次品";
		   		}
		   		var indivEnabledStr = "个体";
		   		if(data.result.indivEnabled == 0){
		   			indivEnabledStr = "配件";
		   		}
		   		var tr = "<tr><td><input readonly=\"readonly\" name=\"indivCodes\" style=\"border:0px;background-color:transparent;\" value="+data.result.indivCode+"></input>"+
		   		"</td><td><input readonly=\"readonly\" name=\"skuCodes\" style=\"border:0px;background-color:transparent;\" value="+data.result.skuCode+"></input>"+
		   		"</td><td><input readonly=\"readonly\" name=\"skuNames\" style=\"border:0px;background-color:transparent;\" value="+data.result.skuName+"></input>"+
		   		"</td><td><input readonly=\"readonly\" style=\"border:0px;background-color:transparent;\" value="+waresStatusStr+"></input>"+
		   		"</td><td><input readonly=\"readonly\" style=\"border:0px;background-color:transparent;\" value="+indivEnabledStr+"></input>"+
		   		"</td><td><input readonly=\"readonly\" style=\"border:0px;background-color:transparent;\" value="+data.result.warehouseName+"></input>"+
		   		"</td><td><input readonly=\"readonly\" style=\"border:0px;background-color:transparent;\" value="+data.result.transferTo+"></input>"+
		   		"</td><td><input readonly=\"readonly\" style=\"border:0px;background-color:transparent;\" value="+data.result.transferId+"></input>"+
		   		"</td><td><div><a class=\"btnDel\" href=\"javascript:void(0);\" onclick=\"delItem(this)\">删除</a></div></td>"+
		   		"<input type=\"hidden\" name=\"waresStatuss\" value="+data.result.waresStatus+">"+
		   		"<input type=\"hidden\" name=\"indivEnableds\" value="+data.result.indivEnabled+">"+
		   		"</tr>";
		   		$("#scan_indivs tr:last", navTab.getCurrentPanel()).after(tr);
		   	} else {
		   		errorTips(data.message);
		   		return false;
		   	}
	   },
	   error: function (XMLHttpRequest, textStatus, errorThrown) {     
			alert(errorThrown);
		}
	});
   	$("#indivCode2", navTab.getCurrentPanel()).val("");//清空输入框
	$("#indivCode2", navTab.getCurrentPanel()).focus();//聚焦下个输入框
}

function delItem(obj) {
	$(obj).closest("tr").remove();
}

$(function(){
	// 页面初始化加载表单
	$("#indivCode2", navTab.getCurrentPanel()).focus();//聚焦下个输入框
});
</script>
