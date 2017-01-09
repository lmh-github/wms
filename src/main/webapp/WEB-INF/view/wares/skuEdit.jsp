<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form action="<s:if test='id == null'>${ctx}/wares/sku!add.action?callbackType=closeCurrent&navTabId=tab_sku</s:if><s:else>${ctx}/wares/sku!update.action?callbackType=closeCurrent&navTabId=tab_sku</s:else>" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
<input type="hidden" name="id" value="${id}"/>
<div class="pageContent">
	<div class="pageFormContent" layoutH="97">
		<fieldset>
			<legend></legend>
			<dl class="nowrap">
				<dt>商品型号：</dt>
				<dd>
					<select class="required" name="wares.id" ${id==null?"":"disabled='disabled'" } onchange="$.pdialog.reload('${ctx}/wares/sku!input.action?selectEnabled=1&wares.id='+this.value+'&skuCode='+$('#skuCode').val()+'&skuName='+$('#skuName').val())">
						<option value="">请选择</option>
						<c:forEach items="${waresList}" var="item">
							<option value=${item.id } ${(item.id eq wares.id)?"selected='true'":""}>${item.waresName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>SKU名称：</dt>
				<dd><input name="skuName" id="skuName" class="required" type="text" maxlength="20" size="20" value="${skuName }" ${editEnabled?"":"readonly='readonly'"} /></dd>
			</dl>
			<dl>
				<dt>SKU编码：</dt>
				<dd><input name="skuCode" id="skuCode" class="required" type="text" maxlength="10" size="20" value="${skuCode }" ${editEnabled?"":"readonly='readonly'"}/></dd>
			</dl>
			<dl>
				<dt>条形码：</dt>
				<dd><input name="skuBarcode" type="text" maxlength="13" size="20" value="${skuBarcode }" ${editEnabled?"":"readonly='readonly'"}/></dd>
			</dl>
			<dl>
				<dt>ERP物料编码：</dt>
				<dd><input name="materialCode" class="required" type="text" maxlength="13" size="20" value="${materialCode }" ${editEnabled?"":"readonly='readonly'"}/></dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd><textarea name="remark" cols="80" rows="2">${remark }</textarea></dd>
			</dl>
		</fieldset>
		<c:if test="${id==null}"></c:if>
		<fieldset>
			<legend>SKU属性值设定</legend>
			<c:set var="idsAsStr" value=",${itemIds},"/>
			<c:forEach items="${waresAttrInfo.attrSet.attrList}" var="attr" varStatus="status">
			
			<dl class="nowrap">
				<dt>${attr.attrName }：</dt>
				<dd>
					<select name="itemIdList[${status.index}]" ${id==null?"":"disabled='disabled'"}>
						<option value="">请选择</option>
						<c:forEach items="${attr.itemList}" var="item">
							<c:set var="idAsStr" value=",${item.id},"/>
							<option value=${item.id } ${fn:contains(idsAsStr,idAsStr)?"selected='true'":"" }>${item.itemName }</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			
			</c:forEach>
		</fieldset>
        <fieldset>
          <legend>组合SKU</legend>
          <table class="list nowrap" width="100%">
            <colgroup>
              <col style="width: 25%;">
              <col style="width: 24%;">
            </colgroup>
            <thead>
              <tr>
                <th type="text" name="itemList[#index#].itemName" size="12" fieldClass="required">SKU编码</th>
                <th type="text">SKU名称</th>
                <th type="text">单位</th>
                <th type="text" style="width: 250px;">数量<span style="color:red;">&nbsp;(默认为1个)</span></th>
                <th type="text">是否赠品</th>
                <th type="del" width="60">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr class="unitBox" style="background-color: transparent;">
                <td><input name="editSkuid" bringBackName="sku.id" value="" type="hidden" /> <input class="textInput readonly" name="editSkuCode" bringBackName="sku.skuCode" type="text" value="" lookupGroup="sku" readonly="readonly" /> <a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a></td>
                <td><input name="editSkuName" bringBackName="sku.skuName" value="" readonly="readonly" class="readonly" style="border: none;background-color: transparent;" /></td>
                <td><input name="editMeasureUnit" bringBackName="sku.measureUnit" value="" readonly="readonly" class="readonly" style="width: 90px;border: none;background-color: transparent;" /></td>
                <td><input name="editQuantity" maxlength="5" class="textInput" style="width: 60px" value="1" /></td>
                <td><select name="isBonus" style="width:70px;"><option value="0">主商品</option><option value="1">赠品</option></select></td>
                <td><a class="btnSelect" rel="select-sku" id="dig-add-sku" href="javascript:void(0)">添加</a></td>
              </tr>
            </tbody>
            <tbody id="bom-sku">
              <c:forEach items="${skuBomList }" var="item" varStatus="i">
                <tr>
                  <td>${item.skuCode}<input type="hidden" name="skuBomList[${i.index}].cSkuCode" value="${item.skuCode}" /></td>
                  <td>${item.skuName}<input type="hidden" name="skuBomList[${i.index}].skuName" value="${item.skuName}" /></td>
                  <td>${item.measureUnit}<input type="hidden" name="skuBomList[${i.index}].measureUnit" value="${item.measureUnit}" /></td>
                  <td><input type="text" name="skuBomList[${i.index}].quantity" value="${item.quantity}" style="width: 60px" /></td>
                  <td><select style="width:70px;" name="skuBomList[${i.index}].isBonus"><option value="0" ${item.isBonus == 0 ? "selected" : ""}>主商品</option><option value="1" ${item.isBonus == 1 ? "selected" : ""}>赠品</option></select></td>
                  <td><a href="javascript:;" class="btnDel">删除</a></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </fieldset>
		
		
		<div class="tabs">
		</div>
		
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
			<li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
		</ul>
	</div>
<script type="text/template" id="tr-template">
<tr>
  <td>{skuCode}<input type="hidden" name="skuBomList[index].cSkuCode" value="{skuCode}" /></td>
  <td>{skuName}<input type="hidden" name="skuBomList[index].skuName" value="{skuName}" /></td>
  <td>{measureUnit}<input type="hidden" name="skuBomList[index].measureUnit" value="{measureUnit}" /></td>
  <td><input type="text" class="textInput" name="skuBomList[index].quantity" value="{quantity}" style="width: 60px" /></td>
  <td><select style="width:70px;" name="skuBomList[index].isBonus"><option value="0">主商品</option><option value="1">赠品</option></select></td>
  <td><a href="javascript:;" class="btnDel">删除</a></td>
</tr>
</script>
<script type="text/javascript">
(function() {
	var template = $.trim($("#tr-template").html());
	var reg = /\{(\w*)\}/ig;
	var bomSkuBodty = $("#bom-sku");
	$("#dig-add-sku").click(function() {
		var tr = $(this).closest("tr");
		var item = {
			skuCode : tr.find(":input[name=editSkuCode]").val(),
			skuName : tr.find(":input[name=editSkuName]").val(),
			measureUnit : tr.find(":input[name=editMeasureUnit]").val(),
			quantity : $.trim(tr.find(":input[name=editQuantity]").val()) || 1,
			isBonus : tr.find(":input[name$=isBonus]").val()
		};
		if (item.skuCode.length == 0) {
			alert("请选择SKU！");
			return;
		}
		
		if (item.quantity.length > 0 && (!/^\d+$/.test(item.quantity) || +item.quantity == 0)) {
			alert("数量必须为数字，且大于0！");
			return;
		}
		
		if ($("#bom-sku tr>td:first-child:contains('" + item.skuCode + "')").length > 0) {
			alert("SKU：" + item.skuCode + " 已存在！");
			return;
		}
		
		var html = template.replace(reg, function($0, $1){
			return item[$1] || "";
		});
		$(html).appendTo(bomSkuBodty).find(":input[name$=isBonus]").val(item['isBonus']);
		//bomSkuBodty.append(html);
		bomSkuBodty.trigger("refresh");
		tr.find(":input").val("");
	}); // End click
	
	bomSkuBodty.delegate(".btnDel", "click", function() {
		$(this).closest("tr").remove();
		bomSkuBodty.trigger("refresh");
	}); // End delegate
	
	bomSkuBodty.bind("refresh", function() {
		$("tr", this).each(function(i) {
		  $(this).find(":input[name*='[']").each(function() {
			  this.name = this.name.replace(/\[\w+\]/, "[" + i + "]");
		  });
		});
	}); // END bind
})();
</script>
</div>
</form>




