<html>
<head>
	<title></title>
	<script src="${base}/static/js/jquery-1.8.3.min.js" type="text/javascript"></script>
	<style type="text/css"><!--
* {
	-moz-box-sizing: border-box;
}

.hide_for_jatools_print {}

.main {
	overflow: hidden;
	position: relative;
	width: 873px;
	height: 450px;
	background-color: white;
	border-left: 0px solid black;
	border-top: 0px solid black;
	border-right: 0px solid black;
	border-bottom: 0px solid black;
}
.e {
	position: absolute;
	overflow: hidden;
	font-family: Arial, Vernada, Tahoma, Helvetica, sans-serif;
	font-size: 12px;
	color: #444444;
	text-decoration: none;
	line-height: 24px;
}
-->

p {font-size: 120%;}	
	</style>
</head>
<body>
<div class="main" id="page_1">
<div id="page1">

<div class="e" style="top: 43px; left: 150px">
<p>${order.deliveryCode?if_exists}</p>
</div>

<div class="e" style="top: 105px; left: 115px">
<p>金立公司</p>
</div>

<div class="e" style="top: 105px; left: 327px">
<p>金立配送中心</p>
</div>

<div class="e" style="top: 131px; left: 125px">
<p>0769-89296666-8864</p>
</div>

<div class="e" style="top: 131px; left: 306px">
<p>√</p>
</div>

<div class="e" style="width: 250px; height: 45px; top: 156px; left: 115px">
<p>广东省东莞市大岭山镇湖畔工业园</p>
</div>

<div class="e" style="top: 242px; left: 330px">
<p>${order.consignee?if_exists}</p>
</div>

<div class="e" style="top: 268px; left: 285px">
<p>${order.tel?if_exists} ${order.mobile?if_exists}</p>
</div>

<div class="e" style="width: 270px; height: 70px; top: 290px; left: 115px">
<p>${order.fullAddress?if_exists}</p>
</div>

<div class="e" style="width: 150px; top: 370px; left: 150px">
<p>${shippingRemark?if_exists}</p>
</div>

<div class="e" style="top: 373px; left: 330px">
<p>${order.orderAmount?if_exists}</p>
</div>

<div class="e" style="top: 142px; left: 443px">
<p>√</p>
</div>

<div class="e" style="top: 142px; left: 525px">
<p>${order.orderAmount?if_exists}</p>
</div>

<#if order.orderSource=='2' && order.paymentType==2>
<div class="e" style="top: 170px; left: 510px">
<p>0000000001</p>
</div>
</#if>

<#if order.orderSource=='1' && order.paymentType==2>
<div class="e" style="top: 170px; left: 510px">
<p>7699476943</p>
</div>
</#if>

<#if order.paymentType==2>
<div class="e" style="top: 190px; left: 515px">
<p>${order.invoiceAmount?if_exists}</p>
</div>

<div class="e" style="top: 177px; left: 443px">
<p>√</p>
</div>

<div class="e" style="top: 403px; left: 530px">
<p>${order.paymentName?if_exists}</p>
</div>
</#if>

<div class="e" style="top: 295px; left: 443px">
<p>√</p>
</div>

<div class="e" style="letter-spacing: 11px;top: 317px; left: 560px">
<p>7699476943</p>
</div>

<div class="e" style="top: 355px; left: 480px">
<p>金立配送中心</p>
</div>

<div class="e" style="top: 378px; left: 515px">
<p>${.now?string("yyyy-MM-dd")}</p>
</div>

<div class="e" style="top: 347px; left: 610px">
<p>请本人当场开箱验收</p>
</div>

</div>
</div>
</body>
</html>
