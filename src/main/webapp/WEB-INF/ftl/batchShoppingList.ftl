<html>
<head>
	<title></title>
<style media="print" type="text/css">
.Noprint {
	display: none;
}

.PageNext {
	page-break-after: always;
}
</style>
</head>
<body>
<object id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0 ></object>
<div id="orderDetail" style="height:600px;overflow-y:auto;overflow-x:hidden;" >
<div style="text-align: center;" class="Noprint">
    <input type="button" value="打印预览(Only IE)" onclick="document.all.WebBrowser.ExecWB(7,1)" />
    <input type="button" value="打印设置(Only IE)" onclick="document.all.WebBrowser.ExecWB(8,1)" />
    <!--<input type="button" value="预览刷新" onclick="document.all.WebBrowser.ExecWB(21,1)" />
    <input type="button" value="Window.print打    印" onclick="javascript:window.print()" />-->
    <input name="printBtn" type="button" value="打印" />
</div>

<#list orderVoList as order> 
<div>
<h1><img alt="" src="data:image/png;base64,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" /></h1>

<table cellpadding="1" style="width:100%">
	<tbody>
		<tr height="20px">
			<td align="right">收 货 人：</td>
			<td>${order.consignee?if_exists}</td>
			<td align="right">订购日期：</td>
			<td style="">${order.orderTime?string("yyyy-MM-dd")}</td>
			<td rowspan="3"><img src="${base}${order.barCodeImgPath?if_exists}"></img></td>
			<td rowspan="3"><h1 style="font-size: 50px;">${order.rownum?if_exists}</h1></td>
		</tr>
		<tr height="20px">
			<td align="right">付款方式：</td>
			<td><!-- <#if order.paymentType==1> -->在线支付 <!-- </#if> --> <!-- <#if order.paymentType==2> --> 货到付款 <!-- </#if> --></td>
			<td align="right">拣货编号：</td>
			<td>${order.batchCode?if_exists}</td>
		</tr>
		<tr height="20px">
			<td align="right">收货地址：</td>
			<td>${order.fullAddress?if_exists}</td>
			<td align="right">发货编号：</td>
			<td>${order.deliveryCode?if_exists}</td>
		</tr>
	</tbody>
</table>

<table border="1" style="border-collapse:collapse; border-color:#000; width:100%">
	<tbody>
		<tr>
			<td style="text-align:center">商品编号名称 <!-- 商品名称 --></td>
			<td style="text-align:center">商品名</td>
			<td style="text-align:center">单价(元) <!-- 商品单价 --></td>
			<td style="text-align:center">数量<!-- 商品数量 --></td>
			<td style="text-align:center">金额（元） <!-- 价格小计 --></td>
		</tr>
		<!-- <#list order.getGoodsList() as item> -->
		<tr>
			<td style="text-align:center">&nbsp;${item.skuCode?if_exists}</td>
			<td style="text-align:center">&nbsp;${item.skuName?if_exists}</td>
			<td style="text-align:center">${item.unitPrice?string.currency}&nbsp;</td>
			<td style="text-align:center">${item.quantity?if_exists}&nbsp;</td>
			<td style="text-align:center">${item.subtotalPrice?string.currency}&nbsp;</td>
		</tr>
		<!-- </#list> -->
		<tr><!-- 发票抬头和发票内容 -->
			<td colspan="3">&nbsp;</td>
			<!-- 商品总金额 -->
			<td colspan="2" style="text-align:right">&nbsp;</td>
		</tr>
	</tbody>
</table>

<table border="0" style="width:100%">
	<tbody>
		<tr>
			<td style="text-align:right"><!-- 配送费用 --></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
	</tbody>
</table>

<table border="0" style="width:100%">
	<tbody>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr><!-- 支付备注 -->
			<td style="text-align:right">购物明细单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />
			服务热线：300-779-6666<br />
			www.gionee.com&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
	</tbody>
</table>
</div>
<#if order_index < orderVoList.size()-1>
<br class="PageNext"/>
</#if>
</#list>

<div style="text-align: center;" class="Noprint">
    <input type="button" value="打印预览(Only IE)" onclick="document.all.WebBrowser.ExecWB(7,1)" />
    <input type="button" value="打印设置(Only IE)" onclick="document.all.WebBrowser.ExecWB(8,1)" />
    <!--<input type="button" value="预览刷新" onclick="document.all.WebBrowser.ExecWB(21,1)" />
    <input type="button" value="Window.print打    印" onclick="javascript:window.print()" />-->
    <input name="printBtn" type="button" value="打印" />
</div>

<script type="text/javascript">
    $(":input[name='printBtn']").bind("click",function(){
         $.printBox('orderDetail');
    });
</script></body>
</html>
