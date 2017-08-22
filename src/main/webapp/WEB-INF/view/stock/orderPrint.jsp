<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<OBJECT id=m_ObjSoapKp classid="clsid:31EE4CA3-5469-4151-AF4B-01ED9ABD1E35" width=0 height=0 align="center" hspace=0 vspace=0 CODEBASE="SoapKpClient.cab#Version=1,0,0,0"> </OBJECT>
<script language="javascript" src="${ctx}/static/print/LodopFuncs.js"></script>
<script language="javascript" src="${ctx}/static/print/sfimage.js"></script>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/salesOrder!orderPrint.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<input id="shipping_id" type="hidden" name="shippingId" value="${shippingId}" />
	<input id="shipping_code" type="hidden" name="shippingCode" value="${shippingCode}" />
	<div class="searchBar">
		<ul class="searchContent" style="height: 50%;" id="orderPrint-ul">
			<li>
				<label>下单开始时间：</label>
				<input type="text" name="orderTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeBegin }" pattern="yyyy-MM-dd"/>"/>
            </li>
            <li>
                <label>下单截止时间：</label>
				<input type="text" name="orderTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${orderTimeEnd }" pattern="yyyy-MM-dd"/>"/>
			</li>
			<li>
				<label>支付开始时间：</label>
				<input type="text" name="paymentTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${paymentTimeBegin }" pattern="yyyy-MM-dd"/>"/>
            </li>
            <li>
                <label>支付截止时间：</label>
				<input type="text" name="paymentTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${paymentTimeEnd }" pattern="yyyy-MM-dd"/>"/>
			</li>
			<li>
				<label>订单号：</label>
				<input type="text" name="orderCode" value="${orderCode}"/>
			</li>
			<li>
				<label>下单人：</label>
				<input type="text" name="orderUser" value="${orderUser}"/>
			</li>
			<li>
				<label>收货人：</label>
				<input type="text" name="consignee" value="${consignee}"/>
			</li>
			<li>
				<label>拣货编号：</label>
				<input type="text" name="batchCode" value="${batchCode}"/>
			</li>
			<li>
				<label>订单状态：</label>
				<s:select name="orderStatus" list="@com.gionee.wms.common.WmsConstants$OrderStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey="999"/>
			</li>
			<li>
				<label>订单来源：</label>
				<s:if test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==1">
					<s:select name="orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
				</s:if><s:elseif test="@com.gionee.wms.common.WmsConstants@WMS_COMPANY==2">
					<s:select name="orderSource" list="@com.gionee.wms.common.WmsConstants$OrderSourceIuni@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
				</s:elseif>
			</li>
			<li>
				<label>是否含发票：</label>
				<select name="invoiceEnabled">
					<option value="">请选择</option>
					<option value="1" ${(1==invoiceEnabled)?"selected='true'":""}>含发票</option>
					<option value="0" ${(0==invoiceEnabled)?"selected='true'":""}>不含发票</option>
				</select>
			</li>
			<li>
				<label>SKU编码：</label>
				<select name="condition" style="width: 40px;">
					<option value="1" ${(1==condition)?"selected='true'":""}>含</option>
					<option value="2" ${(2==condition)?"selected='true'":""}>等于</option>
					<option value="3" ${(3==condition)?"selected='true'":""}>不含</option>
				</select>
				<input name="skuCode" type="text" value="${skuCode}" style="width: 117px;"/>
			</li>
          <li>
            <label>是否推送：</label>
              <select name="orderPushStatus">
                  <option value="" ${orderPushStatus == null ? "selected" : "" }>请选择</option>
                  <option value="1" <s:property value="orderPushStatus == 1 ? 'selected' : ''"/>>是</option>
                  <option value="0" <s:property value="orderPushStatus == 0 ? 'selected' : ''"/>>否</option>
              </select>
          </li>
		</ul>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button id="but_submit" type="submit">检索</button></div></div></li>
			</ul>
		</div>

		<div class="panelBar">
			<ul class="toolBar">
				<s:iterator value="shippingSumList" id="shippingSum">
					<a class="add" onclick="filterShipping(${shippingId},'${shippingCode}')"><span>${shippingName}(${count})</span></a>
				</s:iterator>
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code || orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.code">
				<li><a class="icon" onclick="allPrint()" title="确定要同时打印吗？"><span>同时打印快递面单、购物清单、拣货单</span></a></li>
				<li class="line">line</li>
			</s:if>
			<li><a class="icon" onclick="batchPrint('shipping')"><span>只打印快递面单</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" onclick="batchPrint('shopping')"><span>只打印购物清单</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" onclick="batchPrint('picking')"><span>只打印拣货单</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" onclick="ExecSfPrint()"><span>只打印购物清单拣货单</span></a></li>
			<li class="line">line</li>
			<li><a class="icon" onclick="batchPrint('invoice')"><span>只打发票</span></a></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="169">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="orderIds" class="checkboxCtrl"></th>
				<th>订单号</th>
				<th>支付时间</th>
				<th>收货人</th>
				<th>订单总金额</th>
				<th>支付类型</th>
				<th>订单状态</th>
				<th>物流公司</th>
				<th>配送单号</th>
				<th>发票状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="orderList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td><input name="orderIds" value="${id }" type="checkbox"></td>
				<td>${orderCode }</td>
				<td><fmt:formatDate value="${paymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${consignee }<br>${fullAddress }</td>
				<td><fmt:formatNumber value="${orderAmount}" pattern="#.00" /></td>
				<td>
					<s:if test="paymentType==@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@ONLINE.name"/></s:if>
					<s:elseif test="PaymentType==@com.gionee.wms.common.WmsConstants$PaymentType@COD.code"><s:property value="@com.gionee.wms.common.WmsConstants$PaymentType@COD.name"/></s:elseif>
				</td>
				<td>
					<s:if test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@FILTERED.name"/></s:if>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PRINTED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@PICKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@SHIPPED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@CANCELED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@RECEIVED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSEING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSED.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@REFUSED.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@BACKING.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@BACKING.name"/></s:elseif>
					<s:elseif test="orderStatus==@com.gionee.wms.common.WmsConstants$OrderStatus@UNFILTER.code"><s:property value="@com.gionee.wms.common.WmsConstants$OrderStatus@UNFILTER.name"/></s:elseif>
				</td>
				<td>
					${shippingName }
				</td>
				<td>
					${shippingNo}
				</td>
				<td>
					<s:if test="invoiceStatus==1"><span style='color:green;'>已出</span></s:if>
					<s:elseif test="invoiceStatus==0"><span style='color:red;'>未出</span></s:elseif>
				</td>
				<td>
					<a title="订单详情" target="navTab" rel="tab_showSalesOrder" href="${ctx}/stock/salesOrder!showSalesOrder.action?id=${id}" class="btnView"></a>
				</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="page.pageSize" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="40" ${page.pageSize==40?"selected='true'":""}>40</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>

		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>

<div id="audioArea" style="display: none">
</div>

<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript">
$(function initAudio() {
    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    if (window.ActiveXObject)
        Sys.ie = ua.match(/msie ([\d.]+)/)[1];
    else if (document.getBoxObjectFor)
        Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];
    else if (window.MessageEvent && !document.getBoxObjectFor)
        Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];
    else if (window.opera)
        Sys.opera = ua.match(/opera.([\d.]+)/)[1];
    else if (window.openDatabase)
        Sys.safari = ua.match(/version\/([\d.]+)/)[1];
    if (Sys.chrome) {
        $("#audioArea")
            .html(
                '<audio controls="controls" id="audio_error"><source src="${ctx}/static/audio/audio_error.wav"></audio>');
    } else {
        $("#audioArea")
            .html(
                '<EMBED id="audio_error" src="${ctx}/static/audio/audio_error.wav" align="center" border="0" width="1" height="1" autostart="false" loop="false"/>');
    }
});

//设置快递号，然后提交查询
function filterShipping(shippingId, shippingCode) {
    $("#shipping_id").val(shippingId);
    $("#shipping_code").val(shippingCode);
    $("#but_submit").submit();
}

var idstr = '';
//获取checkboc ids
function getSelectedIds() {
    //获取选择的orderIds值
    var id_array = new Array();
    $('input[name="orderIds"]:checked').each(function () {
        id_array.push($(this).val());//向数组中添加元素
    });
    idstr = id_array.join(',');//将数组元素连接起来以构建一个字符串
}

//编辑配送信息
function inputShipping() {
    //获取选择的orderIds值
    getSelectedIds();
    if (idstr == '') {
        alertMsg.error('请选择订单！');
        return;
    }
    var url = "${ctx}/stock/salesOrder!inputShippingInfo.action?ids=" + idstr;
    var options = '{target:selectedTodo,rel:orderIds,width:100px,height:100px,max:true,mask:true,mixable:true,minable:true,resizable:true,drawable:true,fresh:true,close:"function"}';
    $.pdialog.open(url, 'dlg_input_shipping', '编辑配送信息', options);
}

//打印所有
function allPrint() {
    getSelectedIds();
    if (idstr == '') {
        alertMsg.error('请选择订单！');
        return;
    }
    var shippingId = $("#shipping_id").val();
    if (shippingId == '') {
        alertMsg.error('请首先过滤快递类型');
        return;
    }

    //创建拣货批次
    var shippingId = $("#shipping_id").val();
    var shippingCode = $("#shipping_code").val();
    $.ajax({
        url: '${ctx}/stock/salesOrder!addDeliveryBatch.action?ids=' + idstr + '&shippingId=' + shippingId,
        dataType: 'json',
        type: 'POST',
        data: '',
        timeout: 30000,
        error: function () {
            alertMsg.info("请求失败或超时,请稍后再试！");
        },
        success: function (data) {
            if (shippingCode != 'zt') {
                //打运单
                doPrint(idstr, 'shipping');
                //打发票
                //doPrint(idstr, 'invoice');
            }
            //打拣货单
            doPrint(idstr, 'picking');
            //打购物清单
            doPrint(idstr, 'shopping');
            alertMsg.info('批次创建成功');
            //清除物流信息
            $("#shipping_id").val();
            $("#shipping_code").val();
            //刷新本页面
            navTab.reload('${ctx}/stock/salesOrder!orderPrint.action', null);
        }
    });
}

//批打
function batchPrint(type) {
    getSelectedIds();
    if (idstr == '') {
        document.getElementById("audio_error").play();
        alertMsg.error('请选择订单！');
        return;
    }
    var shippingId = $("#shipping_id").val();
    if (shippingId == '') {
        document.getElementById("audio_error").play();
        alertMsg.error('请首先点击过滤快递类型');
        return;
    }
    doPrint(idstr, type);
}

function doPrint(orderIdStr, type) {
    var idArray = orderIdStr.split(",");
    if (type == 'shipping') {
        for (var i = 0; i < idArray.length; i++) {
            var shippingCode = $("#shipping_code").val();
            if (shippingCode == 'sf_express' || shippingCode == 'sf') // 包含东莞顺丰直发
            {
                var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
                LODOP.PRINT_INIT("${printerPre}printSF");
                LODOP.SET_PRINTER_INDEXA('${printerPre}printSF');

                LODOP.SET_PRINT_PAGESIZE(1, "100mm", "150mm", "");
                LODOP.SET_SHOW_MODE("90%", 1);
                LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:80.5%;Height:74.7%");
                $.get("${urlPre}/stock/salesOrder!previewShipping.action?id=" + idArray[i] + "&_" + new Date().getTime(), function (html) {
                    html = $.trim(html);
                    if (/^sfCode/.test(html)) {
                        var matchs = html.match(/^sfCode(\d+)([\s\S]*)/);
                        LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%", matchs[2]);
                        LODOP.ADD_PRINT_BARCODE("18mm", "11mm", "63mm", "15mm", "128A", matchs[1]);
                        LODOP.ADD_PRINT_BARCODE("128mm", "45mm", "63mm", "15mm", "128A", matchs[1]);
                        LODOP.PRINT();
                        //LODOP.PREVIEW();

                    } else {
                        alert(html);
                        return;
                    }
                });
                //LODOP.ADD_PRINT_URL(0,0,"100%","100%","${urlPre}/stock/salesOrder!previewShipping.action?id="+idArray[i]);
                //LODOP.PREVIEW();
                //LODOP.PRINT();
            }
        }
    }
    if (type == 'shopping') {
        for (var i = 0; i < idArray.length; i++) {
            var j = i + 1;
            var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
            LODOP.PRINT_INIT("printGW");
            LODOP.SET_PRINTER_INDEXA('printGW');
            LODOP.SET_PRINT_PAGESIZE(0, 0, 0, "GW");
            LODOP.ADD_PRINT_URL(0, 0, "100%", "100%", "${urlPre}/stock/salesOrder!previewShoppingList.action?id=" + idArray[i] + "&rownum=" + j);
            //LODOP.PREVIEW();
            LODOP.PRINT();
        }
    }
    if (type == 'picking') {
        var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
        LODOP.PRINT_INIT("printJH");
        LODOP.SET_PRINTER_INDEXA('printJH');//18.8.0.250HP LaserJet 1020
        LODOP.SET_PRINT_PAGESIZE(0, 0, 0, "JH");
        LODOP.ADD_PRINT_URL(0, 0, "100%", "100%", "${urlPre}/stock/salesOrder!previewPicking.action?ids=" + idstr);
        //LODOP.PREVIEW();
        LODOP.PRINT();
    }
}
</script>