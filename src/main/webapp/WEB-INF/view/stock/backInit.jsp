<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">退货入库单</h2>
<form id="pagerForm" name="rmaRecvForm" action="${ctx}/stock/back!handledBack.action?callbackType=closeCurrent&navTabId=tab_back" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="back.backCode" value="${back.backCode}">
<input type="hidden" name="back.id" value="${back.id}">
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<dl>
				<dt>销售订单号：</dt>
				<dd>
					<input name="back.orderCode" type="text" maxlength="30" class="required" value="${back.orderCode}"/>
				</dd>
			</dl>
			<dl>
				<dt>退货仓库：</dt>
				<dd>
					<select class="required" name="warehouseId">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.id } ${(item.id eq warehouseId)?"selected='true'":""}>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="back.remarkBacked" cols="80" rows="1"></textarea></dd>
			</dl>
		</fieldset>
		
		<s:if test="editEnabled">
			<h2 class="contentTitle" align="center">退货商品</h2>
		</s:if>
		<s:else>
			<h2 class="contentTitle" align="center">退货商品</h2>
		</s:else>
		<div>
			<table class="list" width="100%">
				<thead>
					<tr>
						<s:if test="editEnabled">
							<th width="22"><input type="checkbox" group="goodIds" class="checkboxCtrl"></th>
						</s:if>
						<th align="center">SKU编码</th>
						<th align="center">SKU名称</th>
						<th align="center">单位</th>
						<th align="center">数量</th>
						<th align="center">良品数量</th>
						<th align="center">次品数量</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="goodsList" status="status">
					<tr class="unitBox" target="sid_user" rel="${status.index }">
						<s:if test="editEnabled">
							<td><input name="goodIds" value="${id}_${status.index}" type="checkbox"></td>
						</s:if>
						<td align="center">${skuCode }</td>
						<td align="center">${skuName }</td>
						<td align="center">${measureUnit }</td>
						<td align="center">${quantity }</td>
						<td align="center"><input name="nonDefective" value="${quantity}" size="1"/></td>
						<td align="center"><input name="defective" value="0" size="1"/></td>
						<input type="hidden" name="skuCode" value="${skuCode}">
						<input type="hidden" name="indivEnabled" value="${indivEnabled}">
						<input type="hidden" name="quantity" value="${quantity}">
					</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<s:if test="editEnabled">
			<h3 class="contentTitle">已发出的商品信息</h3>
			<div>
			<table class="list" width="100%">
				<thead>
					<tr>
						<th align="center">SKU编码</th>
						<th align="center">SKU名称</th>
						<th align="center">IMEI</th>
						<th align="center">数量</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="itemList" status="status">
					<tr class="unitBox" target="sid_user" rel="${status.index }">
						<td align="center">${skuCode}</td>
						<td align="center">${skuName}</td>
						<td align="center">${indivCode}</td>
						<td align="center">${num}</td>
					</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
			
			<h3 class="contentTitle">良品身份码录入</h3>
			<div class="tabs">
				<div class="tabsContent">
					<div>
						<table class="list nowrap itemDetail" addButton="添加" id="indivAddBtn1" width="100%">
							<thead>
								<tr>
									<th type="text" name="indivList1[#index#].indivCode" size="30" fieldClass="required alphanumeric" fieldAttrs="{minlength:1,maxlength:20}"  onkeydown="doKeydown1(event)">商品身份编码</th>
									<th type="del" width="70%">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${indivList1}" var="indiv" varStatus="status">
									<tr class="unitBox">
										<td>
											<input type="text" maxlength="20" minlength="1" class="required alphanumeric textInput valid" size="30" value="${indiv.indivCode }" name="indivList1[${status.index}].indivCode" onkeydown="doKeydown1(event)">
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
		
		<h3 class="contentTitle">次品身份码录入</h3>
		<div class="tabs">
			<div class="tabsContent">
				<div>
					<table class="list nowrap itemDetail" addButton="添加" id="indivAddBtn2" width="100%">
						<thead>
							<tr>
								<th type="text" name="indivList2[#index#].indivCode" size="30" fieldClass="required alphanumeric" fieldAttrs="{minlength:1,maxlength:20}"  onkeydown="doKeydown2(event)">商品身份编码</th>
								<th type="del" width="70%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${indivList2}" var="indiv" varStatus="status">
								<tr class="unitBox">
									<td>
										<input type="text" maxlength="20" minlength="1" class="required alphanumeric textInput valid" size="30" value="${indiv.indivCode }" name="indivList2[${status.index}].indivCode" onkeydown="doKeydown2(event)">
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
		</s:if>
	</div>
	<div class="formBar">
		<ul>
			<s:if test="editEnabled">
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="doSubmit()">保存</button></div></div></li>
			</s:if>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
function doSubmit(){
	$("form[name=rmaRecvForm]").submit();
}
//监听回车 
function doKeydown1(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		$("#indivAddBtn1").click();
		var currIndex = theEvent.target.name.substring(theEvent.target.name.indexOf('[')+1,theEvent.target.name.indexOf(']'));
    	var nextInputName = 'indivList1['+(parseInt(currIndex)+1)+'].indivCode';
		$("input[name='"+nextInputName+"']").focus();//聚焦下个输入框
		return false;
	}
}

//监听回车 
function doKeydown2(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		$("#indivAddBtn2").click();
		var currIndex = theEvent.target.name.substring(theEvent.target.name.indexOf('[')+1,theEvent.target.name.indexOf(']'));
    	var nextInputName = 'indivList2['+(parseInt(currIndex)+1)+'].indivCode';
		$("input[name='"+nextInputName+"']").focus();//聚焦下个输入框
		return false;
	}
}
</script>