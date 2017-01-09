<html>
<head>
	<title></title>
</head>
<body>
<div id="orderDetail">
<div style="height: 130px; margin-left:500px;"><img src="${base}${order.barCodeImgPath?if_exists}" /></div>

<div id="page1" style="width:90%;margin:auto;">
<table cellpadding="1" style="width:100%;margin-left:37px;">
	<tbody>
		<tr height="20px">
			<td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">收 货 人:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;">${order.consignee?if_exists}</td>
			<td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">订购日期:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;">${order.orderTime?string("yyyy-MM-dd")}</td>
		</tr>
		<tr height="20px">
			<td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">付款方式:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;"><!-- <#if order.paymentType==1> -->在线支付 <!-- </#if> --> <!-- <#if order.paymentType==2> --> 货到付款 <!-- </#if> --></td>
			<td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">拣货编号:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;">${order.batchCode?if_exists}</td>
		</tr>
		<tr height="20px">
			<td style="text-align:left;width:80px;font-size:80%;font-weight: bold;">收货地址:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;">${order.fullAddress?if_exists}</td>
		</tr>
	</tbody>
</table>

<table border="1" style="border-collapse:collapse; border-color:#000; width:100%;margin-left:37px;">
	<tbody>
		<tr>
			<td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">商品编号</td>
			<td style="text-align:center; width:40%;font-size:105%;font-weight: bold;">商品名称</td>
			<td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">单价（元）</td>
			<td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">数量</td>
			<td style="text-align:center; width:15%;font-size:105%;font-weight: bold;">金额（元）</td>
		</tr>
		<!-- <#list goodsList as item> -->
		<tr>
			<td style="text-align:center; width:15%;font-weight: normal;">${item.skuCode?if_exists}</td>
			<td style="text-align:center; width:40%;font-weight: normal;">${item.skuName?if_exists}</td>
			<td style="text-align:center; width:15%;font-weight: normal;">${item.unitPrice?string.currency}</td>
			<td style="text-align:center; width:15%;font-weight: normal;">${item.quantity?if_exists}</td>
			<td style="text-align:center; width:15%;font-weight: normal;">${item.subtotalPrice?string.currency}</td>
		</tr>
		<!-- </#list> -->
	</tbody>
</table>

<table border="0" style="width:100%;margin-left:37px;">
	<tbody>
		<tr>
			<td style="text-align:left;font-size:80%;font-weight: bold;">序号:${rownum?if_exists}</td>
			<td style="text-align:right;font-size:80%;font-weight: normal;">合计:${order.orderAmount?if_exists}元</td>
		</tr>
	</tbody>
</table>

</div>
</div>

</body>
</html>
