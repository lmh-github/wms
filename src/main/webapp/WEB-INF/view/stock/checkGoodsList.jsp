<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<h2 class="contentTitle">盘点单</h2>
<form id="pagerForm" name="purchaseInForm" action="${ctx}/stock/purchaseRecv!confirmReceive.action?id=${id }&callbackType=closeCurrent&navTabId=tab_purchaseRecv" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">

<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend>基本信息</legend>
			<dl>
				<dt>盘点编号：</dt>
				<dd>${check.checkCode }</dd>
			</dl>
			<dl>
				<dt>仓库：</dt>
				<dd>
					${check.warehouseName }
				</dd>
			</dl>
			
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea cols="80" rows="1" id="remark" readonly="readonly">${check.remark }</textarea></dd>
			</dl>
		</fieldset>
		<s:set name="auditStatus" value="check.auditStatus"></s:set>
		<h3 class="contentTitle">商品清单</h3>
		<div class="panelBar">
			<ul class="toolBar">
			<!-- 当盘点单为待处理未审核或者待确认未审核时   可以重复上传 -->
				<s:if test="(check.handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@PENDING.code||check.handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@UNCONFIRMED.code)&&check.auditStatus==0">
				    <s:if test="check.checkType==2">
				      <li><a class="icon" href="${ctx}/stock/stockCheck!inputUploadIndivGoodsList.action?id=${id}&type=${check.checkType}" rel="dlg_checkGoods" target="dialog" mask="true" width="800" height="600"><span>上传个体采集数据</span></a></li>
				    </s:if>
				<s:else>
				       <li><a class="add" href="${ctx}/stock/stockCheck!inputGoods.action?id=${check.id}" rel="dlg_checkDetail" target="dialog" mask="true" width="1250" height="600"><span>添加待盘点商品</span></a></li>
				       <li class="line">line</li>
				       <li><a class="icon" href="${ctx}/stock/stockCheck!downloadGoodsList.action?id=${id }" target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出商品清单</span></a></li>
				       <li class="line">line</li>
				       <li><a class="icon" href="${ctx}/stock/stockCheck!inputUploadGoodsList.action?id=${id}" rel="dlg_checkGoods" target="dialog" mask="true" width="800" height="600"><span>上传采集数据</span></a></li>
				</s:else>
				</s:if>
				
				<s:if test="check.handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@UNCONFIRMED.code&&check.auditStatus==999">
                       <li><a class="add" href="${ctx}/stock/stockCheck!inputConfirm.action?id=${id}" rel="dlg_checkConfirm" target="dialog" mask="true" width="750" height="400"><span>确认盘点</span></a></li>
				</s:if>
			</ul>
		</div>
		
		<div class="tabs">
			<div class="tabsContent" style="height: 280px;">
				<div>
				<s:if test="checkStatusExceptionList.size()==0">
								<table class="list" width="100%">
						<thead>
							<tr align="center">
								<th colspan="3">SKU信息</th>
								<th colspan="4">良品</th>
								<th colspan="4">次品</th>
								<th colspan="1"></th>
							</tr>
							<tr>
								<th>SKU编码</th>
								<th>SKU名称</th>
								<th>计量单位</th>
								<th>可销库存</th>
								<th>账面盘良品数</th>
								<th>实盘良品数</th>
								<th>实盘良品盈亏</th>
								<th>不可销库存</th>
								<th>账面盘次品数</th>
								<th>实盘次品数</th>
								<th>实盘次品盈亏</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="stockInDetailTbody">
							<s:iterator value="goodsList">
							<tr class="unitBox">
								<td>${skuCode }</td>
								<td>${skuName }</td>
								<td>${measureUnit }</td>
								<td>${stock.salesQuantity}</td>
								<td>${bookNondefective }</td>
								<td>${firstNondefective }</td>
								<td><span ${firstNondefectivePl>0?"style='color:green;'":(firstNondefectivePl<0?"style='color:red;'":"")}>${firstNondefectivePl }</span></td>
								<td>${stock.unsalesQuantity }</td>
								<td>${bookDefective }</td>
								<td>${firstDefective }</td>
								<td><span ${firstDefectivePl>0?"style='color:green;'":(firstDefectivePl<0?"style='color:red;'":"")}>${firstDefectivePl }</span></td>
								<td>               
							        <!-- 当盘点单为待处理未审核或者待确认未审核时   可以删除 -->
				                    <s:if test="(check.handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@PENDING.code||check.handlingStatus==@com.gionee.wms.common.WmsConstants$StockCheckStatus@UNCONFIRMED.code)&&#auditStatus==0">
									  <a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/stock/stockCheck!deleteGoods.action?id=${id}&navTabId=tab_checkGoods" class="btnDel"></a>
									</s:if>
									<a title="差异明细" target="navTab" rel="tab_checkItem" href="${ctx}/stock/stockCheck!listItem.action?id=${check.id}&skuCode=${skuCode}" class="btnView"></a>
								</td>
							</tr>
							</s:iterator>
						</tbody>
					</table>
				</s:if>
				<s:else>
				<table class="list" width="100%">
						<thead>
							<tr>
								<th>个体商品IMEI编码</th>
								<th>账面个体状态</th>
								<th>实盘个体状态</th>
							</tr>
						</thead>
						<tbody id="stockInDetailTbody">
							<s:iterator value="checkStatusExceptionList">
							<tr class="unitBox">
								<td>${indivCode }</td>
								<s:if test="bookStatus==0">
								<td>次品</td>
								</s:if>
								<s:elseif test="bookStatus==1">
								<td>良品</td>
								</s:elseif>
								<s:elseif test="bookStatus==2">
								<td>已出库</td>
								</s:elseif>
								<s:elseif test="bookStatus==3">
								<td>在库</td>
								</s:elseif>
								<s:elseif test="bookStatus==4">
								<td>IMEI存在,SKU不一致</td>
								</s:elseif>
								<s:else>
								<td>其他异常状态</td>
								</s:else>
								<s:if test="checkStatus==0">
								<td>次品</td>
								</s:if>
								<s:elseif test="checkStatus==1">
								<td>良品</td>
								</s:elseif>
								<s:elseif test="checkStatus==2">
								<td>已出库</td>
								</s:elseif>
								<s:elseif test="checkStatus==3">
								<td>在库</td>
								</s:elseif>
								<s:elseif test="checkStatus==4">
								<td>IMEI存在,SKU不一致</td>
								</s:elseif>
								<s:else>
								<td>其他异常状态</td>
								</s:else>
							</tr>
							</s:iterator>
						</tbody>
					</table>
					</s:else>
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
							<dt><input readonly="true" name="field5" type="text" value="${check.preparedBy }"/></dt>
						</dl>
						<dl>
							<dt>制单日期：</dt>
							<dt><input readonly="true" name="field5" type="text" value="<fmt:formatDate value='${check.preparedTime}' dateStyle="long" pattern='yyyy-MM-dd' />"/></dt>
						</dl>
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<s:if test="receive.handlingStatus==@com.gionee.wms.common.WmsConstants$ReceiveStatus@RECEIVING.code">
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="purchaseInSubmit();">确认</button></div></div></li>
			</s:if>
			<li>
			<s:if test="id==null">
				<a class="button" target="ajaxTodo" href="${ctx}/stock/stockIn!cancel.action?stockInCode=${stockInCode}&callbackType=closeCurrent&navTabId=tab_stockIn"><span>取消</span></a>			
			</s:if>
			<s:else>
				<div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div>
			</s:else>
			</li>
		</ul>
	</div>
</div>
</form>
<script>
function purchaseInSubmit(){
	if($('#stockInDetailTbody tr').length==0){
		alert("入库商品明细不能为空");
		return false;
	}
	if(confirm("确认收货后商品库存将会相应增加，确定要执行吗？")){
		$("form[name=purchaseInForm]").submit();
	}
}
</script>