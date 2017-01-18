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
				<dt>商品数量：</dt>
				<dd>
					${goods.qty }
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
							
							<tbody>
								<c:forEach items="${transferList}" var="transfer" varStatus="status">
									<tr class="unitBox">
										<td>
											<input type="text" readonly="readonly" size="30" value="${transfer.po }" name="transferList[${status.index}].po">
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
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
</form>



