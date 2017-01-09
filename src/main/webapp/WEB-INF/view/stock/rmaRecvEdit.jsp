<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2 class="contentTitle">退货入库单</h2>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>退货编号：</dt>
				<dd>${receive.receiveCode }</dd>
			</dl>
			<dl>
				<dt>退货仓库：</dt>
				<dd>
					${receive.warehouseName }
				</dd>
			</dl>
			<dl>
				<dt>销售订单号：</dt>
				<dd>
					<a title="查看订单发货信息" href="${ctx}/stock/delivery!listGoods2.action?orderId=${receive.originalId}" target="dialog" mask="true" width="1000" height="600" rel="dlg_orderInfo" class="">${receive.originalCode }</a>
				</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="receive.remark" cols="80" rows="1" id="remark">${receive.remark }</textarea></dd>
			</dl>
		</fieldset>
		
		<h3 class="contentTitle">商品清单</h3>
		<!-- <div class="panelBar">
			<ul class="toolBar">
				<li><a class="add" href="javascript:void(0);" onclick="openIndivInput();"><span>添加有编号商品</span></a></li>
				<li class="line">line</li>
				<li><a class="add" href="javascript:void(0);" onclick="openRmaSkuInput();"><span>添加无编号商品</span></a></li>
				<li class="line">line</li>
				<li><a class="add" href="${ctx}/stock/delivery!listGoods3.action?receiveId=${receive.id }&orderCode=${receive.originalCode}" rel="dlg_rmaRecvGoodsEdit" target="dialog" mask="true" width="1250" height="600"><span>添加退货商品</span></a></li>
			</ul>
		</div> -->
		<div class="tabs">
			<div class="tabsContent" style="height: 200px;">
				<div>
					<table class="list" width="100%">
						<thead>
							<tr>
								<th width="60">SKU编码</th>
								<th width="60">SKU名称</th>
								<th width="60">单位</th>
								<th width="60">数量</th>
								<th width="60">商品状态</th>
								<th width="60">操作</th>
							</tr>
						</thead>
						<tbody id="rmaInDetailTbody">
							<s:iterator value="goodsList">
							<tr class="unitBox">
								<td>${skuCode }</td>
								<td>${skuName }</td>
								<td>${measureUnit }</td>
								<td>${quantity }</td>
								<td>
									<s:if test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@NON_DEFECTIVE.code">良品</s:if>
									<s:elseif test="waresStatus==@com.gionee.wms.common.WmsConstants$IndivWaresStatus@DEFECTIVE.code">次品</s:elseif>
							    </td>
								<td>
									<s:if test="indivEnabled==1">
									<a title="查看商品编码" target="dialog" mask="true" width="800" height="600" href="${ctx}/stock/rmaRecv!inputGoods.action?id=${id}" class="btnView"></a>
									</s:if>
								</td>
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
		<div class="divider"></div>
		<div class="tabs">
						<dl>
							<dt>制单人：</dt>
							<dt><input readonly="true" type="text" value="${receive.preparedBy }"/></dt>
						</dl>
						<dl>
							<dt>制单日期：</dt>
							<dt><input readonly="true" type="text" value="<fmt:formatDate value='${receive.preparedTime}' dateStyle="long" pattern='yyyy-MM-dd' />"/></dt>
						</dl>
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div>
		</ul>
	</div>
</div>
