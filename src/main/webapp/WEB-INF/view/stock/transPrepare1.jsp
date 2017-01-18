<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="now" class="java.util.Date" />   
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="pageContent" id="divTransPrepare1">
	<div class="pageFormContent nowrap" layouth="56" style="height: 119px; overflow: auto;">
		<fieldset>
			<legend>在这里进行配货，请先扫描调货单的条形码</legend>
			<ul>
				<li>
					<label>扫描调货单批次号</label>
					<input id="transferId1" name="transferId" class="textInput" type="text" size="30" value="">
					<span id="scanTips" style="color: red;"></span>
				</li>
			</ul>
		</fieldset>
	</div>
</div>

<script type="text/javascript">
$("#transferId1", navTab.getCurrentPanel()).keydown(function(event) {
	var theEvent = event || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		var obj = this;
		scanDeliveryCode(obj.value);
	   	$(obj).val("");//清空输入框
		$(obj).focus();//聚焦本身
	}
});

function scanDeliveryCode(value) {
	scanTips(value);
	$.ajax({
	   url: "${ctx}/stock/transPrepare!readyPrepare.action",
	   data: "transferId=" + value,
	   success: function(data){
		   	if(data.ok==true) {
		   		navTab.reload("${ctx}/stock/transPrepare!list.action?transferId=" + data.result.transferId, navTab.getCurrentPanel());
		   	} else {
		   		scanTips(data.message);
		   		soundError();
		   		alert(data.message);
		   		return false;
		   	}
	   },
	   error: function (XMLHttpRequest, textStatus, errorThrown) {     
			alert(errorThrown);     
		}
	});
}

function scanTips(message) {
	$("#scanTips", navTab.getCurrentPanel()).html(message);
}

$(function(){
	setTimeout(function() {
		$("#transferId1", navTab.getCurrentPanel()).focus();	
	},1000);
});
</script>