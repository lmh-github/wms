<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form id="theForm" action="${ctx}/stock/purchaseRecv!updateGoods.action?callbackType=closeCurrent&navTabId=dlg_purchaseRecvInput" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, purRecvGoodsEditAjaxDone)">
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
function purRecvGoodsEditAjaxDone(json){
	DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
    	$.pdialog.closeCurrent();
    	$.pdialog.reload('${ctx}/stock/purchaseRecv!input.action?id=${goods.receive.id}',{dialogId:'dlg_purchaseRecvInput'});
    }
}
//-->
</script>