<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<div class="pageContent">
	<div class="pageFormContent">
		<fieldset>
			<legend>基本信息</legend>
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
					${goods.quantity }
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
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
