<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<h2 class="contentTitle"></h2>
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
					${goods.quantity }
				</dd>
			</dl>
		</fieldset>
	</div>
	<div class="formBar">
		<ul>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
</div>
