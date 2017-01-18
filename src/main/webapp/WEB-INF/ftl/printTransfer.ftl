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
<script type="text/javascript">
    $(":input[name='printBtn']").bind("click",function(){
         $.printBox('orderDetail');
    });
</script>
</head>
<body>
<object id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0 ></object>
<div style="text-align: center;"><input name="printBtn" type="button" value="打印" /></div>
<div id="orderDetail" style="height:600px;overflow-y:auto;overflow-x:hidden;">
<div align="center">仓库批量调货单</div>
<div id="page1" style="width:95%;margin:auto;">
<div style="height: 0px;text-align:right;"><img src="${base}${barCodeImgPath?if_exists}" /></div>
<table align="center" cellpadding="1" style="width:100%;">
	<tbody>
		<tr height="20px">
			<td width="13%" style="text-align:left;font-size:80%;font-weight: bold;">调拨编号:</td>
			<td width="17%" style="text-align:left;font-size:80%;font-weight: normal;">${(transfer.transferId?c)!}</td>
			<td width="13%" style="text-align:left;font-size:80%;font-weight: bold;">打单日期:</td>
			<td width="17%" style="text-align:left;font-size:80%;font-weight: normal;">${(transfer.createTime?string("yyyy-MM-dd"))!}</td>
			<td></td>
		</tr>
		<tr height="20px">
			<td style="text-align:left;font-size:80%;font-weight: bold;">收货地址:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;">${(transfer.transferTo!)}</td>
			<td style="text-align:left;font-size:80%;font-weight: bold;">收 货 人:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;">${(transfer.consignee)!}</td>
			<td></td>
		</tr>
		<tr height="20px">
			<td style="text-align:left;font-size:80%;font-weight: bold;">发 货 仓:</td>
			<td style="text-align:left;font-size:80%;font-weight: normal;">${(transfer.warehouseName)!}</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</tbody>
</table>

<table border="1" align="center" style="border-collapse:collapse; border-color:#000; width:100%;">
	<tbody>
		<tr>
			<th style="text-align:center; width:15%;font-size:105%;font-weight: bold;">商品编号</td>
			<th style="text-align:center; width:40%;font-size:105%;font-weight: bold;">商品名称</td>
			<th style="text-align:center; width:15%;font-size:105%;font-weight: bold;">数量</td>
			<th style="text-align:center; width:15%;font-size:105%;font-weight: bold;">单位</td>
		</tr>
		<#list goodsList as item>
		<tr>
			<td style="text-align:center; width:15%;font-weight: normal;">${item.skuCode?if_exists}</td>
			<td style="text-align:center; width:40%;font-weight: normal;">${item.skuName?if_exists}</td>
			<td style="text-align:center; width:15%;font-weight: normal;">${item.quantity?if_exists}</td>
			<td style="text-align:center; width:15%;font-weight: normal;">${item.measureUnit?if_exists}</td>
		</tr>
		</#list>
	</tbody>
</table>
</div>
</body>
</html>
