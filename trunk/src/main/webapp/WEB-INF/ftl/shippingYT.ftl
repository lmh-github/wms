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

<div class="e" style="top: 40px; left: 250px">
<p>${order.deliveryCode?if_exists}</p>
</div>

<div class="e" style="top: 75px; left: 120px">
<p>金立配送中心</p>
</div>

<div class="e" style="top: 75px; left: 280px">
<p>东莞</p>
</div>

<div class="e" style="top: 95px; left: 130px">
<p>金立公司</p>
</div>

<div class="e" style="width: 250px; height: 45px; top: 140px; left: 110px">
<p>广东省东莞市大岭山镇湖畔工业园</p>
</div>

<div class="e" style="top: 187px; left: 155px">
<p>0769-89296666-8864</p>
</div>

<div class="e" style="width: 150px; top: 245px; left: 120px">
<p>${shippingRemark?if_exists}</p>
</div>

<div class="e" style="top: 355px; left: 230px">
<p>金立配送中心</p>
</div>

<div class="e" style="top: 355px; left: 320px">
<p>${.now?string("yyyy-MM-dd")}</p>
</div>

<div class="e" style="top: 75px; left: 500px">
<p>${order.consignee?if_exists}</p>
</div>

<div class="e" style="width: 230px; height: 50px; top: 140px; left: 450px">
<p>${order.fullAddress?if_exists}</p>
</div>

<div class="e" style="top: 187px; left: 520px">
<p>${order.mobile?if_exists}</p>
</div>

<div class="e" style="top: 187px; left: 680px">
<p>${order.tel?if_exists}</p>
</div>

<div class="e" style="top: 360px; left: 430px">
<p>请本人当场开箱验收</p>
</div>

</div>
</body>
</html>
