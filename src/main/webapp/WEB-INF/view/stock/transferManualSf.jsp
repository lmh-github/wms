<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>

<input type="hidden" name="transfer.transferId" value="${transfer.transferId}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl class="nowrap">
				<dt>发货仓：</dt>
				<dd>
					<select class="required" name="transfer.warehouseId">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.id } ${(item.id eq transfer.warehouseId)?"selected='true'":""}>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>收货仓：</dt>
				<dd>
				<select class="required" name="transfer.transferTo">
						<option value="">请选择</option>
						<c:forEach items="${warehouseList}" var="item">
							<option value=${item.id } ${(item.id eq transfer.transferTo)?"selected='true'":""}>${item.warehouseName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>收货人：</dt>
				<dd><input name="transfer.consignee" type="text" maxlength="20" size="20" value="${transfer.consignee }" /></dd>
			</dl>
			<dl>
				<dt>联系方式：</dt>
				<dd><input name="transfer.contact" type="text" maxlength="20" size="20" value="${transfer.contact }" /></dd>
			</dl>
			<dl>
				<dt>调货类型：</dt>
				<dd>
					<select name="transfer.transType">
						<option value="">请选择</option>
						<option value="0" ${(0==transfer.transType)?"selected='true'":""}>良品调拨</option>
						<option value="1" ${(1==transfer.transType)?"selected='true'":""}>次品调拨</option>
					</select>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="transfer.remark" cols="80" rows="2">${transfer.remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<h3 class="contentTitle">商品清单</h3>
		<s:if test="transfer.status==1 || (transfer.status==3 && goodsList.size()==0)">
			<div class="panelBar">
				<ul class="toolBar">
					<li><a class="add" href="${ctx}/stock/transferSf!inputGoodsNew.action?transferId=${transfer.transferId}" target="dialog" rel="dlg_transfer_goodsnew" mask="true" width="800" height="600"><span>添加调拨商品</span></a></li>
				</ul>
			</div>
		</s:if>
		<div class="tabs">
			<div class="tabsHeader">
	            <div class="tabsHeaderContent">
	                  <ul>
	                        <li class="selected"><a href="#"><span>商品清单</span></a></li>
	                  </ul>
	            </div>
	      </div>
			<div class="tabsContent" style="height: 300px;">
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="50">SKU编码</th>
								<th width="50">SKU名称</th>
								<th width="50">单位</th>
								<th width="50">数量</th>
								<th width="50">单价</th>
								<th width="50">已退货</th>
								<s:if test="transfer.status==1">
								<th width="50">操作</th>
								</s:if>
							</tr>
						</thead>
						<tbody id="purRecvTbody">
							<s:iterator value="goodsList">
							<tr class="unitBox">
								<td>${skuCode }</td>
								<td>${skuName }</td>
								<td>${measureUnit }</td>
								<td>${quantity }</td>
								<td>${unitPrice }</td>
								<td><s:if test="rmaNum > 0"><span style="color: red">${rmaNum }</span></s:if></td>
								<s:if test="transfer.status==1">
								<td>
								<a title="确实要删除吗？" target="ajaxTodo" callback="goodsDeleteAjaxDone" href="${ctx}/stock/transferSf!deleteGoods.action?goodsId=${id}&warehouseId=${transfer.warehouseId}" class="btnDel"></a>
								</td>
								</s:if>
							</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
		
		<div class="tabs">
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
<script>
//自定义DWZ回调函数
function goodsDeleteAjaxDone(json){
	DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
    	//$.pdialog.closeCurrent();
    	$.pdialog.reload('${ctx}/stock/transferSf!inputManual.action?transfer.transferId=${transfer.transferId}',{dialogId:'dlg_transfer_manual'});
    }
}
</script>