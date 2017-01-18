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

p {font-size: 120%}
	</style>
</head>
<body>
<div class="main" id="page_1">
<div id="page1">

<div class="e" style="top: 75px; left: 115px">
<p>金立配送中心</p>
</div>

<div class="e" style="top: 75px; left: 275px">
<p>0769-89296666-8864</p>
</div>

<div class="e" style="top: 97px; left: 135px">
<p>金立公司</p>
</div>

<div class="e" style="width: 250px; height: 45px; top: 120px; left: 125px">
<p>广东省东莞市大岭山镇湖畔工业园</p>
</div>

<div class="e" style="letter-spacing: 12px;top: 152px; left: 330px">
<p>523810</p>
</div>

<div class="e" style="top: 190px; left: 130px">
<p>${order.consignee?if_exists}</p>
</div>

<div class="e" style="width: 250px; height: 70px; top: 238px; left: 125px">
<p>${order.fullAddress?if_exists}</p>
</div>

<div class="e" style="letter-spacing: 12px;top: 292px; left: 330px">
<p>${order.zipcode?if_exists}</p>
</div>

<div class="e" style="top: 190px; left: 290px">
<p>${order.tel?if_exists} ${order.mobile?if_exists}</p>
</div>

<div class="e" style="top: 80px; left: 615px">
<p>${order.deliveryCode?if_exists}</p>
</div>

<div class="e" style="top: 155px; left: 675px">
<p>金立配送中心</p>
</div>

<div class="e" style="top: 200px; left: 680px">
<p>${.now?string("yyyy-MM-dd")}</p>
</div>

<div class="e" style="top: 285px; left: 650px">
<p>请本人当场开箱验收</p>
</div>

<div class="e" style="width: 150px; top: 363px; left: 135px">
<p>${shippingRemark?if_exists}</p>
</div>

<div class="e" style="top: 403px; left: 380px">
<p>${order.orderAmount?if_exists}</p>
</div>

</div>
</body>
</html>