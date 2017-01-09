<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form id="theForm" action="${ctx}/stock/purchaseRecv!updateGoods.action?callbackType=closeCurrent&navTabId=tab_inputPurRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, purRecvIndivEditAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent">
		<fieldset>
			<legend></legend>
			<dl>
				<dt>SKU编码：</dt>
				<dd>
					${goods.skuCode }	
				</dd>
			</dl>
			<dl>
				<dt>SKU名称：</dt>
				<dd>
					${goods.skuName }
				</dd>
			</dl>
			<dl>
				<dt>库存单位：</dt>
				<dd>
					${goods.measureUnit }
				</dd>
			</dl>
			<dl>
				<dt>批次号：</dt>
				<dd>
					${goods.productBatchNo }
				</dd>
			</dl>
			<dl>
				<dt>商品数量：</dt>
				<dd>
					<input type="text" name="goods.quantity" maxlength="7" class="required digits" value="${goods.quantity }" min="1"/>
				</dd>
			</dl>
		</fieldset>
		
		<div id="indivCodeEdit">
			<h3 class="contentTitle">商品编码</h3>
			<div class="tabs">
				<div class="tabsContent" style="height: 300px;">
					<div>
						<!-- <table class="list nowrap itemDetail" addButton="添加" id="indivAddBtn" width="100%"> -->
						<table class="list nowrap" width="100%">
							<thead><!-- 
								<tr>
									<th type="text" name="indivList[#index#].indivCode" size="30" fieldClass="required alphanumeric" fieldAttrs="{minlength:10,maxlength:10}"  onkeydown="doKeydown(event)">商品编码</th>
									<th type="del" width="60">操作</th>
								</tr> -->
							</thead>
							<tbody>
								<c:forEach items="${indivList}" var="indiv" varStatus="status">
									<!-- 
									<tr class="unitBox">
										<td>
											<input type="text" maxlength="10" minlength="10" class="required alphanumeric textInput valid" size="30" value="${indiv.indivCode }" name="indivList[${status.index}].indivCode" onkeydown="doKeydown(event)">
										</td>
										<td>
											<a class="btnDel " href="javascript:void(0)">删除</a>
										</td>
									</tr> -->
									<tr class="unitBox">
										<td>
											<input type="text" readonly="readonly" size="30" value="${indiv.indivCode }" name="indivList[${status.index}].indivCode">
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
			<s:if test="goods.receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
			</s:if>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>
<script type="text/javascript">
<!--
//自定义DWZ回调函数
function purRecvIndivEditAjaxDone(json){
	DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
    	$.pdialog.closeCurrent();
    	$.pdialog.reload('${ctx}/stock/purchaseRecv!input.action?id=${goods.receive.id}',{dialogId:'dlg_purchaseRecvInput'});
    }
}
//-->
</script>



