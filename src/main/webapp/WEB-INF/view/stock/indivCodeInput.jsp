<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle"><s:if test='id==null'>创建</s:if><s:else>编辑</s:else>盘点单</h2>
<form id="theForm" action="<s:if test='id==null'>${ctx}/stock/stockCheck!add.action?callbackType=closeCurrent&navTabId=tab_stockCheck</s:if><s:else>${ctx}/stock/stockCheck!update.action?callbackType=closeCurrent&navTabId=tab_stockCheck</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>盘点编号：</dt>
				<dd><input readonly="true" name="checkCode" type="text" value="${checkCode }"/></dd>
			</dl>
			<dl>
				<dt>盘点仓库：</dt>
				<dd>
					<select class="required combox" name="warehouse.warehouseCode">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.warehouseCode } ${(item.warehouseCode==warehouse.warehouseCode)?"selected='true'":""}>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>预盘时间：</dt>
				<dd>
					<input type="text" name="firstTime" class="date" dateFmt="yyyy-MM-dd" minDate="{%y}-%M-%d" value="<fmt:formatDate value="${firstTime }" pattern="yyyy-MM-dd"/>"/>
					<a class="inputDateButton" href="javascript:;">选择</a>
				</dd>
			</dl>
			<dl>
				<dt>复盘时间：</dt>
				<dd>
					<input type="text" name="secondTime" class="date" dateFmt="yyyy-MM-dd" minDate="{%y}-%M-%d" value="<fmt:formatDate value="${secondTime }" pattern="yyyy-MM-dd"/>"/>
					<a class="inputDateButton" href="javascript:;">选择</a>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="remark" cols="80" rows="2" onkeypress="doKeydown(event)">${remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<h3 class="contentTitle">盘点单SKU列表</h3>
		<div class="tabs">
			<div class="tabsContent" style="height: 200px;">
				<div>
					<table class="list nowrap itemDetail" addButton="添加" width="100%">
						<thead>
							<tr>
								<th type="lookup" name="checkDetail[#index#].sku.skuCode" lookupGroup="checkDetail[#index#].sku" lookupUrl="${ctx}/wares/sku!lookup.action" rel="dlg_skuLookup" postField="keywords" size="12" fieldClass="required" readonly="true">SKU编码</th>
								<th type="text" name="checkDetail[#index#].sku.skuName" size="30" fieldClass="required" onkeydown="doKeydown(event)">SKU名称</th>
								<th type="text" name="checkDetail[#index#].sku.measureUnit" size="12" fieldClass="required" readonly="true">单位</th>
								<th type="del" width="60">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${checkDetail}" var="item" varStatus="status">
								<tr class="unitBox">
									<td>
										<input type="hidden" name="checkDetail[${status.index}].sku.id" value="${item.sku.id }">
										<input type="text" class="required textInput readonly valid" size="12" lookuppk="id" readonly="readonly" name="checkDetail[${status.index}].sku.skuCode" value="${item.sku.skuCode }">
										<a title="查找带回" lookuppk="id" readonly="readonly" lookupgroup="checkDetail[1].sku" href="/wms/wares/sku!lookup.action" class="btnLook">查找带回</a>
									</td>
									<td>
										<input type="text" class="required textInput readonly" size="30" name="checkDetail[${status.index}].sku.skuName" readonly="readonly" value="${item.sku.skuName }">
									</td>
									<td>
										<input type="text" class="required textInput readonly" size="12" name="checkDetail[${status.index}].sku.wares.measureUnit" readonly="readonly" value="${item.sku.wares.measureUnit }">
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
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="$('#theForm').submit();">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">

//监听回车 
function doKeydown(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		$(".button").find("button")[0].click();
		var currIndex = theEvent.target.name.substring(theEvent.target.name.indexOf('[')+1,theEvent.target.name.indexOf(']'));
    	var nextInputName = 'checkDetail['+(parseInt(currIndex)+1)+'].sku.skuName';
		$("input[name='"+nextInputName+"']").focus();//聚焦下个输入框
		return false;
	}
//var currKey=0,e=e||event;
//currKey=e.keyCode||e.which||e.charCode;
//var keyName = String.fromCharCode(currKey);
//alert("按键码: " + currKey + " 字符: " + keyName);
	// 兼容FF和IE和Opera
	//var event = arguments.callee.caller.arguments[0] || window.event;alert(e);
}
</script>