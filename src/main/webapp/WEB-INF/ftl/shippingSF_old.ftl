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
	width: 818px;
	height: 529px;
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

p {font-size: 120%}
	</style>
</head>
<body>
<div class="main" id="page_1">
<div id="page1">

<div class="e" style="letter-spacing: 12px;top: 110px; left: 107px">
<p>7699476943</p>
</div>

<div class="e" style="top: 137px; left: 105px">
<p>金立公司</p>
</div>

<div class="e" style="top: 137px; left: 293px">
<p>金立配送中心</p>
</div>

<div class="e" style="width: 250px; height: 45px; top: 165px; left: 105px">
<p>广东省东莞市大岭山镇湖畔工业园</p>
</div>

<div class="e" style="top: 220px; left: 180px">
<p>0769-89296666-8864</p>
</div>

<div class="e" style="top: 222px; left: 315px">
<p>√</p>
</div>

<div class="e" style="width: 50px; height: 30px; top: 290px; left: 307px">
<p>${order.consignee?if_exists}</p>
</div>

<div class="e" style="width: 250px; height: 70px; top: 320px; left: 105px">
<p>${order.fullAddress?if_exists}</p>
</div>

<div class="e" style="top: 370px; left: 190px">
<p>${order.tel?if_exists} ${order.mobile?if_exists}</p>
</div>

<div class="e" style="top: 440px; left: 110px">
<p>电子产品</p>
</div>

<div class="e" style="top: 33px; left: 187px">
<p>${order.deliveryCode?if_exists}</p>
</div>

<#if order.orderSource=='2' && order.paymentType==2>
<div class="e" style="top: 157px; left: 480px">
<p>0000000001</p>
</div>
</#if>

<#if order.orderSource=='1' && order.paymentType==2>
<div class="e" style="top: 157px; left: 480px">
<p>7699476943</p>
</div>
</#if>

<#if order.paymentType==2>
<div class="e" style="top: 177px; left: 510px">
<p>${order.invoiceAmount?if_exists}</p>
</div>

<div class="e" style="top: 167px; left: 375px">
<p>√</p>
</div>

<div class="e" style="top: 482px; left: 600px">
<p>${order.paymentName?if_exists}</p>
</div>
</#if>

<div class="e" style="top: 403px; left: 460px">
<p>${order.orderAmount?if_exists}</p>
</div>

<div class="e" style="top: 109px; left: 580px">
<p>√</p>
</div>

<div class="e" style="letter-spacing: 11px;top: 143px; left: 580px">
<p>7699476943</p>
</div>

<div class="e" style="top: 310px; left: 680px">
<p>金立配送中心</p>
</div>

<div class="e" style="top: 347px; left: 595px">
<p>${.now?string("yyyy-MM-dd")}</p>
</div>

<div class="e" style="top: 410px; left: 595px">
<p>请本人当场开箱验收</p>
</div>

</div>
</div>
</body>
</html>
