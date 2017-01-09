<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>

<style type="text/css">#goodsTbody${rand} input[type=text]{width:90%;}</style>
<div class="pageContent">
  <form method="post" action="${ctx}/stock/transferRmo!add.action?callbackType=closeCurrent&navTabId=tab_transferRmo" class="pageForm required-validate" onsubmit="" id="form${rand}">
   <input type="hidden" name="id" value="${id}"/>
    <div class="pageFormContent" layoutH="56">
      <fieldset>
      <legend>基本信息</legend>

        <p>
          <label>退货平台：</label> <input  name="transferRemove.backPlatform"  class="required" type="text"  size="15">
        </p>
        <p>
          <label>品质：</label>
          <select name="transferRemove.quality" class="required">
                <option value="">请选择</option>
                <option value="1" ${(1==transferRemove.quality)?"selected='true'":""}>良品</option>
                <option value="0" ${(0==transferRemove.quality)?"selected='true'":""}>不良品</option>
            </select>
        </p>

        <dl class="nowrap">
          <dt>备注：</dt>
          <dd><textarea name="transferRemove.remark" cols="90" rows="2" maxlength="100" class="textInput valid"></textarea></dd>
        </dl>
      </fieldset>
      <fieldset>

      <div class="divider"></div>
      <a class="button" id="addGoods${rand}" href="javascript:;"><span>新增</span></a>
      <table class="list nowrap" width="100%" style="table-layout: fixed;">
        <thead>
          <tr>
            <th align="center">SKU编码</th>
            <th align="center">SKU名称</th>
            <th align="center">单位</th>
            <th align="center">单价</th>
            <th align="center">数量</th>
            <th align="center" style="width: 80px;" type="del"></th>
          </tr>
        </thead>
        <tbody id="goodsTbody${rand}">
          
        </tbody>
        <tfoot style="display: none;">
          <tr>
            <td>
                <input class="textInput readonly" name="editSkuCode" bringBackName="sku.skuCode" type="text" value="" lookupGroup="sku" readonly="readonly" style="width: 130px;" />
                <a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
            </td>
            <td><input type="text" /></td>
            <td><input type="text" /></td>
            <td><input type="text" /></td>
            <td><input type="text" /></td>
            <td>&nbsp;</td>
          </tr>
        </tfoot>
      </table>
    </div>
    <div class="formBar">
      <ul>
        <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
        <li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
      </ul>
    </div>
  </form>
<script type="text" id="template${rand}">
<tr class="userTr">
	<td><input type="text" name="transferRemoveDetailList[index].skuCode" value="%skuCode%" class="textInput" readonly="readonly" /></td>
	<td><input type="text" name="transferRemoveDetailList[index].skuName" value="%skuName%" class="textInput" readonly="readonly" /></td>
	<td><input type="text" value="%measureUnit%" class="textInput" readonly="readonly" /></td>
	<td><input type="text" value="%unitPrice%" class="number textInput" /></td>
	<td><input type="text" name="transferRemoveDetailList[index].quantity" value="%quantity%" class="digits textInput" /></td>
	<td><a href="javascript:;" onclick="$(this).closest('tr').remove();" class="btnDel">删除</a></td>
</tr>
</script>
<script type="text" id="newtr-template${rand}">
<tr class="unitBox">
  <td>
    <input class="textInput readonly required" name="transferRemoveDetailList[index].skuCode" bringBackName="sku.skuCode" type="text" lookupGroup="sku" readonly="readonly" style="width: 90px;" />
    <a class="btnLook" href="${ctx}/wares/sku!lookup.action" lookupGroup="sku" width="1200">查找SKU</a>
  </td>
  <td><input type="text" name="transferRemoveDetailList[index].skuName" bringBackName="sku.skuName" class="textInput" readonly="readonly" /></td>
  <td><input type="text" bringBackName="sku.measureUnit" class="textInput" readonly="readonly" /></td>
  <td><input type="text" name="transferRemoveDetailList[index].totle" value="0" class="digits textInput" /></td>
  <td><input type="text" class="textInput" name="transferRemoveDetailList[index].num" class="number textInput" value="0" /></td>
  <td><a href="javascript:;" class="btnDel" onclick="$(this).closest('tr').remove();">删除</a></td>
</tr>
</script>
<script type="text/javascript">
$(function() {
	var template = $.trim($("#template${rand}").text());
	$("#form${rand}").bind("submit", function() {
	  var flag=true;
		$("#goodsTbody${rand} tr").each(function(index) {

			$(this).find("input[name^=transferRemoveDetailList]").each(function() {


				this.name = "transferRemoveDetailList[" + index + "]." + this.name.replace(/transferRemoveDetailList.*?\./g, "");
				//if(this.name=="transferRemoveDetailList[" + index + "].totle"){ //判断单价
				//  var value_=this.value;
				//  if(isNaN(value_) || value_<=0){
				//    flag=false;
				//    alert("请输入合法的单价");
               //     return false;
				//  }
				//}
				if(this.name=="transferRemoveDetailList[" + index + "].num"){//判断数量
                  var value_=this.value;
                  if(isNaN(value_) || value_<=0){
                    flag=false;
                      alert("请输入合法的数量");
                       return false;
                  }
                }

                if(this.name=="transferRemoveDetailList[" + index + "].skuCode"){//判断是否为配件
                  var value_ = this.value.charAt(0);
                  if(value_!=2){
                        flag=false;
                      alert("SKU只允许保存配件");
                       return false;
                  }
                }
			});
		});

		if(flag==false){
		  flag=true;
		  return false;
		}
		return validateCallback(this, dialogAjaxDone);
	}); // END
	
	var newTrTemplate = $.trim($("#newtr-template${rand}").text());
	$("#addGoods${rand}").bind("click", function(event) { // 新增一行商品
		event.preventDefault(); // 阻止默认事件
		$("#goodsTbody${rand}").append(newTrTemplate).initUI();		
	}); // END
	
	$("#orderCode${rand}").keydown(function(event) {
		if (event.keyCode == 13) {
    		event.preventDefault(); // 阻止默认事件
    		this.value = $.trim(this.value);
    		if(this.value.length == 0) {
    			return;
    		}
    		
    		$.post("${ctx}/stock/orderBack!lookupSalesOrder.action", {orderCode : this.value}, function(data) {
    			if (data.message) {
    				return alertMsg.error(data.message);
    			}
    			
    			$("#form${rand} :input[data-bind]").each(function() {
    				var $input = $(this);
    				var name = $input.data("bind");
    				$input.val(data.salesOrder[name] == null ? '' : data.salesOrder[name]);
    			});
    			
    			
    			var html = "";
    			$.each(data.transferRemoveDetailList, function(i, o) {
    				html += template.replace(/%(\w+)%/g, function($0, $1, $2) {
    					return o[$1] == null ? '' : o[$1];
    				});
    			});
    			var tbody = $("#goodsTbody${rand}");
    			tbody.find("tr").filter(".userTr").remove();
    			$("#goodsTbody${rand}").prepend(html).initUI();
    		}, "json");
		}
	});
});
</script>
</div>
