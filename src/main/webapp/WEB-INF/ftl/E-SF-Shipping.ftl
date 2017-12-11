<#if errorMsg?? && errorMsg?length gt 0 >
${errorMsg}
</#if>
<#if errorMsg?? && errorMsg?length == 0 >
sfCode${bspSourceMap.sfCode!}<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<!--100*150,90-->
<html>
<head>
    <title>顺丰运单模板</title>
    <meta charset="utf-8"/>
    <style type="text/css">
        *{margin:0;padding:0}.panel{border:1px solid #000;width:455px;padding:0 5px 0 5px}.fy{font-family:"Microsoft YaHei"}.fh{font-family:"黑体"}.f12{font-size:12px}.bb{border-bottom:1px solid #000}.bl{border-left:1px solid #000}.br{border-right:1px solid #000}.bl{border-left:1px solid #000}.fw{font-weight:bold}.tl{text-align:left}table{border-collapse:collapse;table-layout:fixed}.f_d1{border-bottom:1px solid #000;height:55px;text-align:center;font-size:36pt}
    </style>
</head>
<body>
<div class="panel">
    <div class="f_d1 fh"><span>E</span></div>
    <table class="bb">
        <tr>
            <td style="text-align: center; width: 64%;">&nbsp;</td>
            <td class="bl" style="height: 100%;" valign="top">
                <table style="width: 100%;">
                    <tr><td class="fh fw" align="center" style="font-size: 16px; letter-spacing: 6px;border-bottom: 1px solid #999;height: 22px;">电商特惠</td></tr>
                    <tr><td class="fh fw" align="center" style="font-size: 20px;letter-spacing: 5px;height: 24px;"><#if order.paymentType==2>代收货款</#if></td></tr>
                    <tr><td class="fy tl" style="font-size: 13px;height: 19px;"><#if order.paymentType==2>卡号：7693255199</#if></td></tr>
                    <tr><td class="fy fw tl" style="font-size: 15px;height: 22px;"><#if order.paymentType==2>￥${(order.payableAmount?if_exists)?string("#.##")}元</#if></td></tr>
                </table>
            </td>
        </tr>
    </table>
    <table class="bb" style="width: 100%;height: 54px;">
        <tr>
            <td class="fy br f12" style="padding: 1px 4px;width: 20px;" align="center">目的地</td>
            <td class="fy fw" style="font-size: 38px;">${bspSourceMap.destcode!}</td>
        </tr>
    </table>
    <table class="bb" style="width: 100%;height: 64px;">
        <tr>
            <td class="fy br f12" style="padding: 6px 4px;width: 20px;" align="center">收件人</td>
            <td class="fy fw" style="font-size: 14px;">
                <div>${order.consignee?if_exists} ${order.tel?if_exists} ${order.mobile?if_exists}</div>
                <div>${order.fullAddress?if_exists}</div>
            </td>
        </tr>
    </table>
    <table class="bb" style="width: 100%;height: 52px;">
        <tr>
            <td class="fy br f12" style="padding: 1px 4px;width: 20px;" align="center">寄件人</td>
            <td class="fy fw" style="font-size: 14px;">
                <div>电商配送中心 400-779-6666</div>
                <div>广东省东莞市大岭山镇湖畔工业区金立工业园</div>
            </td>
        </tr>
    </table>
    <table style="width: 100%;">
        <tr>
            <td class="fy br bb f12" style="height: 81px;">
                <table style="width: 100%;" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="width: 42%">付款方式：寄付月结</td>
                        <td style="width: 33%">计费重量：</td>
                        <td style="width: 25%"><span style="visibility: hidden">包装费用：</span></td>
                    </tr>
                    <tr>
                        <td>月结账号：7693255199</td>
                        <td>声明价值：${(order.invoiceAmount?if_exists)?string("#.##")}<br>保价费用：</td>
                        <td><span style="visibility: hidden">运费：元</span></td>
                    </tr>
                    <tr style="display: none;">
                        <td><span style="visibility: hidden">第三方地区：</span></td>
                        <td><span style="visibility: hidden">保价费用：元</span></td>
                        <td><span style="visibility: hidden">费用合计：元</span></td>
                    </tr>
                    <tr style="display: none;">
                        <td><span style="visibility: hidden">实际重量：KG</span></td>
                        <td colspan="2"><span style="visibility: hidden">定时派送：</span></td>
                    </tr>
                    <tr>
                        <td colspan="3" align="right">转寄协议客户</td>
                    </tr>
                </table>
            </td>
            <td class="fh f12" valign="top" align="left" style="padding: 4px 0 0 3px;width: 20%;">签名：</td>
        </tr>
        <tr>
            <td class="br">
                <table style="width: 100%;height: 52px;">
                    <tr>
                        <td class="fy br f12" style="padding: 0px 4px;width: 20px;" align="center">托寄物</td>
                        <td class="fy f12"></td>
                    </tr>
                </table>
            </td>
            <td class="f12" style="width: 20%;" valign="bottom" align="right">月&nbsp;&nbsp;&nbsp;日&nbsp;</td>
        </tr>
    </table>
</div>
<div class="panel" style="margin-top: 20px;">
    <table class="bb" style="height: 80px;width: 100%;">
        <tr>
            <td class="br" style="width: 110px;">
                <img class="logo" height="35" src="data:image/png;base64,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" alt="">
                <img height="35" class="phone" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAKAAAAA8CAMAAADWtUEnAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAABhQTFRFIiIi1NTUjo6OsbGxYWFh7u7uAAAA////lVpTKwAABPNJREFUeNrsWYvO8yYM9Y30/d94NgZig5t+0/5NlfZFqpKcgDm+gA2F15df8Evwl+D3EUQi0YuIv5MgClx+CX8fQRaBye+6AITaVxFEufaLzj7UW0n8kvstoUyuLSUpNTrlAuFbggwHvwtksyHdn/CZIAZxsMKlRtPgcchIEAt+1jzxS1TwkSCXrqjRoHdWHXYViA6aUaGNCf6c4NTmRygUBN1+tOmyxSEvz1Cybu+7zyfWeGq3ceQJhfky3EgnwT624GGO5GS57UbRAF3qPpk4exUeUL7HaZBMuB6aLOJtd/KK5Ra1g8A9u+VcK65KgYBGbaUmSGHs04QpUvC1C2ofCEpJUHaCr+P5Jjis5uvy2yj0Dy2y7S/4E4LwgAZZPshhQQzObPJuqSkI4h2/H1wsD2jb/XdMEg5M+HommPzNeZHY8kD0JT+htGYf1csMhXUJi3wSOVEcgI6YOFKPFOv9gY5XX4WLhZqi+NPF0IInoIWgOAnOxhmnKllGVK7HVEfRm2fOm2PKitSZOguCt1loOb4V2Tyjd+SnMu8kaE4+VsKpEz5WOzjTJJ6FRWnBgFK1rBWTZARAf4UHm6zPOfQRUnMqQ7NCh/lGDIYgLAh2f6oHhaRIdnynl1zQFFkt23UrPW+0pczOeUCoSi1TC9XMy2VxCrYOarmK7/PXtiY3qGrfiMZelDwDR4gmC4+pANXuhD+lh2Cu6xnlzCnVSXU1vQKmmwvKnYmUOwIoaX9AUwJ4Q3CvOTlYForsMDyEfyOr/UOC2+IHbO1bN6y8LQCOL1TutFrp4htNvdpVEzxKBCBmeRuBKZRp09IriK3OHZO0QjFmA0k6QrVjv/fFB79RDGyZVOXxitgJ84auwqdA7ww1dr4rXcLzrviwX510y7zIZUVUo0dypXJffNrw8G9dFJRbPS4TdI3uW3J6c7LQ9q07yDZN6/1yWSlgqSe+0T66Lxpln1kcjVgczfCMPtoOxGYuTWXLjMl0UFajK/lBhovTLeUAeolUxfF3HGAi64Xt9fpWgr9HwL8Efwn+CYKWndo3E9RNi3y1BQH+axezpVHREg8vzXc43ynAsxIExLwZBXtBKx9IK1BNQ55Q5RIrSMXTv6VbWbdZ+1ltYf37aYCNDCyhiI/1oMkClUSXJlzqQ+lYEXZ+ICBN7E+UVbxqYtRB7QeqitEVq/SkJ22FxWFFcd6cn8kyla09W6ljD1eXdrpYzWYtzFzUbxdjH3LBLpR6CR1cDKavvjcznbg6vV1/6xqqoGZ3buM2FDPPdaVeNiS7vSVIh7TL0KIEYAylN3unCA8P9xrMXntxx20UP617vw3pOHXieUAGftQB68RDxpGz71a0F7tm/IYgmytoBp0+QPdFhOduZOiI9q8jNvOwSDdWbxQIoou1Pyd78KmtcYXY8HRzyZ8JvqzIUrn+X4R6VuMDMrwotuzi5YPuLZtiLzc+jsm3qlK5bzOqb4U+ERSbbHC5VLU7uUFu2DyvltItcSJo4zWxYAefWTaloU+SHgzaHYGbzlN1w7qBRac+y+ivrT4RJI9Rj4irn75QgtFZXymM55aHOydwZbyNE5zHuxBvONefzn60/0SwSV+tfFdoM4Fkg+0PSpb1eNtQpGmT3o5sVnsb9iMUFF+RxHOP33r/+YW8vZ1YaScM0v+VdPAnk8z/kyDRbz34PddfAgwAfP7m2QIPxogAAAAASUVORK5CYII=" alt="">
            </td>
            <td align="center" style="text-align: center">&nbsp;</td>
        </tr>
    </table>
    <table class="bb" style=" width: 100%;height: 52px;">
        <tr>
            <td class="fy br f12" style="padding: 0px 4px;width: 20px;" align="center">寄件人</td>
            <td class="fy fw" style="font-size: 14px;">
                <div>电商配送中心 400-779-6666</div>
                <div>广东省东莞市大岭山镇湖畔工业区金立工业园</div>
            </td>
        </tr>
    </table>
    <table class="bb" style=" width: 100%;height: 64px;">
        <tr>
            <td class="fy br f12" style="padding: 6px 4px;width: 20px;" align="center">收件人</td>
            <td class="fy fw" style="font-size: 14px;">
                <div>${order.consignee?if_exists} ${order.tel?if_exists} ${order.mobile?if_exists}</div>
                <div>${order.fullAddress?if_exists}</div>
            </td>
        </tr>
    </table>
    <div style="height: 55px;width: 100%;">

    </div>
</div>
</body>
</html>
</#if>