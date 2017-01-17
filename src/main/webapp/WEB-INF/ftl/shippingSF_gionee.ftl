<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<style type="text/css">
* {
  -moz-box-sizing: border-box;
}

.hide_for_jatools_print {
  
}

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
  font-family: Arial, Vernada, Tahoma, Helvetica, sans-serif;
  font-size: 12px;
  color: #444444;
  text-decoration: none;
  line-height: 20px;
}

p {
  font-size: 120%
}
</style>
<style type="text/css"></style></head>
<body style="margin: 0;padding: 0;">
  <div class="main" id="page_1">
    <div id="page1">

      <div class="e" style="letter-spacing: 0px; top: 57px; left: 150px; font-size: 9px;">
        <p>${order.orderCode?if_exists}</p>
      </div>

      <div class="e" style="top: 90px; left: 120px;">
        <p>金立集团</p>
      </div>

      <div class="e" style="top: 90px; left: 310px;">
        <p>电商配送中心</p>
      </div>

      <div class="e" style="top: 147px; left: 106px;">
        <p>400-779-6666</p>
      </div>
      <div class="e" style="width: 310px; height: 25px; top: 117px; left: 100px;">
        <p>广东省东莞市大岭山镇湖畔工业区金立工业园</p>
      </div>
      <div class="e" style="top: 250px; left: 107px">
        <p></p>
      </div>
      <!-- 收件人名称 -->
      <div class="e" style="top: 172px; left: 310px;">
        <p>${order.consignee?if_exists}</p>
      </div>
      <!-- 收件人固定电话 -->
      <div class="e" style="top: 276px; left: 107px">
        <p></p>
      </div>
      <!-- 收件人手机 -->
      <div class="e" style="top: 258px; left: 106px;">
        <p>${order.mobile?if_exists}&nbsp;&nbsp;${order.tel?if_exists}</p>
      </div>

      <!-- 收件人地址 -->
      <div class="e" style="width: 310px; height: 25px; top: 203px; left: 100px;">
        <p>${order.fullAddress?if_exists}</p>
      </div>

      <div class="e" style="top: 310px; left: 75px;">
        <p>电子产品</p>
      </div>
      <!-- 声明价值 -->
      <div class="e" style="top: 315px; left: 305px;">
        <p><span style="font-size: 130%; letter-spacing: 5px;">${(order.payableAmount?if_exists)?string("#.##")}</span></p>
      </div>
      <!-- 原寄地 -->
      <div class="e" style="top: 60px; left: 590px;">
        <p>0769T</p>
      </div>

      <div class="e" style="top: 148px; left: 523px">
        <p></p>
      </div>

      <!-- 代收货款 淘宝来源 0000000001 官网 7699476943 -->      
      <div class="e" style="letter-spacing: 8px;top: 164px;left: 496px;">
        <#if order.orderSource=='2' && order.paymentType==2>
        <p style="margin-left: 24px;letter-spacing: 5px;">0000000001</p>
        </#if>
        <#if order.orderSource=='1' && order.paymentType==2>
        <p style="margin-left: 24px;letter-spacing: 5px;">7699476943</p>
        </#if>
        <#if order.paymentType==2><b style="font-size: 20px;font-family: Vernada, Tahoma, Helvetica, sans-serif;position: absolute;left: -72px;top: 7px;">√</b></#if>
        <!--金额-->
        <#if order.paymentType==2>&nbsp;&nbsp;<span style="font-size: 130%; letter-spacing: 5px;position: absolute;right: -120px;top: 15px;">${(order.payableAmount?if_exists)?string("#.##")}</span></#if>
      </div>

      <!-- 寄件方 -->
      <div class="e" style="top: 382px;left: 425px;">
        <p><b style="font-size: 20px;font-family: Vernada, Tahoma, Helvetica, sans-serif;">√</b></p>
      </div>
      
      <div class="e" style="letter-spacing: 11px;top: 405px;left: 474px;">
	  	<p>7699476943</p>
	  </div>

      <!-- 收件人签收提示 -->
      <div class="e" style="top: 265px; left: 500px;display:none;">
        <p>请本人当场开箱验收</p>
      </div>

      <div class="e" style="top: 399px; left: 80px; font-size: 8px">
        <p>电商配送中心</p>
      </div>

      <!-- 日期 -->
      <div class="e" style="top: 415px; left: 95px; font-size: 8px">
        <p>${.now?string("MM")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${.now?string("dd")}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${.now?string("HH")}</p>
      </div>

      <!-- 备注 -->
      <div class="e" style="top: 295px;left: 489px;">
      	<#if order.paymentType==2>
        	<p>代收货款，金额：${(order.payableAmount?if_exists)?string("#.##")}</p>
        </#if>
        <#if order.paymentType==1>
        	<p>转寄协议客户</p>
        </#if>
      </div>
    </div>
  </div>


</body></html>