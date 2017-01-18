<html>
<head>
	<title></title>
	<style type="text/css">#printBox {display:none}
	</style>
	<link href="${request.contextPath}/static/themes/css/print.css" media="print" rel="stylesheet" type="text/css" />
    <script src="${request.contextPath}/static/js/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${request.contextPath}/static/js/dwz.print.js" type="text/javascript"></script>
</head>
<body>
<div id="orderDetail">
<h1 style="text-align:center">购物清单</h1>

<table cellpadding="1" style="width:100%">
	<tbody>
		<tr>
			<td style="width:8%">购货人：</td>
			<td>${orderUser?if_exists}</td>
			<td style="text-align:right">下单时间：</td>
			<td>${orderTime?string("yyyy-MM-dd")}</td>
			<td style="text-align:right">支付方式：</td>
			<td>test</td>
			<td style="text-align:right">订单编号：</td>
			<td>${orderCode?if_exists}</td>
		</tr>
		<tr>
			<td>付款时间：</td>
			<td>&nbsp;</td>
			<td style="text-align:right">发货时间：</td>
			<td>test</td>
			<td style="text-align:right">配送方式：</td>
			<td>test</td>
			<td style="text-align:right">发货单号：</td>
			<td>test</td>
		</tr>
		<tr>
			<td>收货地址：</td>
			<td colspan="7">${consigneeAddress?if_exists}</td>
		</tr>
	</tbody>
</table>

<table border="1" style="border-collapse:collapse; border-color:#000; width:100%">
	<tbody>
		<tr>
			<td style="text-align:center">商品名称 <!-- 商品名称 --></td>
			<td style="text-align:center">货号 <!-- 商品货号 --></td>
			<td style="text-align:center">属性 <!-- 商品属性 --></td>
			<td style="text-align:center">价格 <!-- 商品单价 --></td>
			<td style="text-align:center">数量<!-- 商品数量 --></td>
			<td style="text-align:center">小计 <!-- 价格小计 --></td>
		</tr>
		<!-- <#list orderDetail as item> -->
		<tr>
			<td>&nbsp;${item.sku.skuName?if_exists}</td>
			<td>&nbsp;${item.sku.skuCode?if_exists}</td>
			<td>&nbsp;</td>
			<td style="text-align:right">${item.unitPrice?if_exists}&nbsp;</td>
			<td style="text-align:right">${item.quantity?if_exists}&nbsp;</td>
			<td style="text-align:right">${item.subtotalPrice?if_exists}&nbsp;</td>
		</tr>
		<!-- </#list> -->
		<tr><!-- 发票抬头和发票内容 -->
			<td colspan="4">发票抬头：&nbsp;&nbsp;&nbsp; 发票内容：</td>
			<!-- 商品总金额 -->
			<td colspan="2" style="text-align:right">商品总金额：</td>
		</tr>
	</tbody>
</table>

<table border="0" style="width:100%">
	<tbody>
		<tr>
			<td style="text-align:right"><!-- 配送费用 -->+ 配送费用： <!-- 订单总金额 --> = 订单总金额：</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
	</tbody>
</table>

<table border="0" style="width:100%">
	<tbody>
		<tr><!-- 给购货人看的备注信息 -->
			<td>&nbsp;</td>
		</tr>
		<tr><!-- 发货备注 -->
			<td>&nbsp;</td>
		</tr>
		<tr><!-- 支付备注 -->
			<td>&nbsp;</td>
		</tr>
		<tr><!-- 网店名称, 网店地址, 网店URL以及联系电话 -->
			<td>&nbsp;</td>
		</tr>
	</tbody>
</table>
</div>

<div style="text-align: center;"><input name="printBtn" type="button" value="打印" /></div>
<script type="text/javascript">
    $(":input[name='printBtn']").bind("click",function(){
         $.printBox('orderDetail');
    });
</script></body>
</html>
