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
	height: 491px;
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
	</style>
</head>
<body><object classid="CLSID:B43D3361-D975-4BE2-87FE-057188254255" codebase="${base}/print/jatoolsPrinter.cab#version=2,1,0,5" height="0" id="jatoolsPrinter" width="0"></object>
<div style="position: relative; background-color: #808080; width: 898px; height: 516px; overflow: scroll; top: 20px; left: 100px">
<div class="main" id="page_1">
<div id="page1">
<div style="position: absolute;clip: rect(0px,873px,491px,0px); top: 0px; left: 0px"><img class="hide_for_jatools_print" src="${base}/static/print/shentong.jpg" style="height:491px; width:873px" /></div>

<div class="e" style="width: 220px; height: 45px; top: 136px; left: 130px">
<p>广东深圳福田车公庙东海国际中心B座16楼</p>
</div>

<div class="e" style="width: 153px; height: 20px; top: 195px; left: 130px">
<p>深圳金立通信设备有限公司</p>
</div>

<div class="e" style="width: 50px; height: 20px; top: 230px; left: 150px">
<p>霍平</p>
</div>

<div class="e" style="width: 100px; height: 20px; top: 260px; left: 140px">
<p>13888888888</p>
</div>

<div class="e" style="letter-spacing: 8px;top: 260px; left: 270px">
<p>518000</p>
</div>

<div class="e" style="width: 230px; height: 50px; top: 137px; left: 430px">
<p>${order.fullAddress?if_exists}</p>
</div>

<div class="e" style="top: 230px; left: 450px">
<p>${order.consignee?if_exists}</p>
</div>

<div class="e" style="line-height: 15px; width: 90px; height: 40px; top: 255px; left: 445px">
<p>${order.tel?if_exists} ${order.mobile?if_exists}</p>
</div>

<div class="e" style="letter-spacing: 8px;top: 260px; left: 572px">
<p>${order.zipcode?if_exists}</p>
</div>
</div>
</div>
</div>

<div class="e" style="top:550px;left:300px;"><input name="previewBtn" type="button" value="打印预览..." /> <input name="printBtn1" type="button" value="打印..." /> <input name="printBtn2" type="button" value="打印" /></div>
<script type="text/javascript">
<!--
$(":input[name='previewBtn']").bind("click",function(){
    doPrint("1");
});
$(":input[name='printBtn1']").bind("click",function(){
    doPrint("2");
});
$(":input[name='printBtn2']").bind("click",function(){
    doPrint("3");
});

function doPrint(how){
    if(typeof(jatoolsPrinter.page_div_prefix)=='undefined'){
        alert("请按页顶提示下载ActiveX控件.如果没有提示请按以下步骤设置ie.\n 工具-> internet 选项->安全->自定义级别,设置 ‘下载未签名的 ActiveX ’为'启用'状态")
        return ;
    }
    var myreport ={documents: document,copyrights:'杰创软件拥有版权 www.jatools.com'};   			  
    // 调用打印方法
    if(how == '1'){
       jatoolsPrinter.printPreview(myreport ); // 打印预览
    }else if(how == '2'){
       jatoolsPrinter.print(myreport ,true);   // 打印前弹出打印设置对话框
    }else{
       jatoolsPrinter.print(myreport ,false);  // 直接打印
    }
}
//-->
</script></body>
</html>
