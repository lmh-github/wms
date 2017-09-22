<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<style>
    .time-horizontal {
        list-style-type: none;
        border-top: 2px solid #707070;
        max-width: 80%;
        padding: 0px;
        margin: 20px 0 0 70px;
    }

    .time-horizontal li {
        float: left;
        position: relative;
        text-align: center;
        width: 25%;
        padding-top: 20px;
    }

    .time-horizontal li b:before {
        content: '';
        position: absolute;
        top: -10px;
        left: 47%;
        width: 17px;
        height: 17px;
        border: 2px solid skyblue;
        border-radius: 10px;
        background: skyblue;
    }

    .time-horizontal li b.grey:before {
        content: '';
        position: absolute;
        top: -10px;
        left: 47%;
        width: 17px;
        height: 17px;
        border: 2px solid grey;
        border-radius: 10px;
        background: grey;
    }
    .container-left{
        border: 1px solid gray;
        position: absolute;
        top: 100px;
        width: 200px;
        height: 320px;
        left: 80px;
        word-wrap: break-word
    }
    .container-left .container-left-title{
        color: red;
        font-size: 25px;
        font-weight: bold;
        margin: 20px 0 0 9px;
    }
    .iframe-style{
        width: 422px;
    }
    .container-left ul li, .container-right ul li{
        margin: 22px 0 0 10px;
        font-size: 12px;
    }
    .container-right{
        border: 1px solid gray;
        position: absolute;
        top: 100px;
        width: 436px;
        height: 320px;
        right: 58px;
    }

    .chatTextarea{



        resize: none;
    }
</style>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <div class="container">
                <ul class="time-horizontal">
                    <li><b></b>工单创建时间<br />
                        <fmt:formatDate value="${workOrder.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </li>
                    <li>
                        <c:if test="${workOrder.acceptTime == null}">
                          <b class="grey"></b>
                        </c:if>
                        <c:if test="${workOrder.acceptTime != null}">
                          <b></b>
                        </c:if>
                        工单领取时间<br />
                        <fmt:formatDate value="${workOrder.acceptTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </li>
                    <li>
                        <c:if test="${workOrder.upTime == null}">
                            <b class="grey"></b>
                        </c:if>
                        <c:if test="${workOrder.upTime != null}">
                            <b></b>
                        </c:if>
                         工单跟进开始<br />
                        <fmt:formatDate value="${workOrder.upTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </li>
                    <li>
                        <c:if test="${workOrder.lastTime == null}">
                            <b class="grey"></b>
                        </c:if>
                        <c:if test="${workOrder.lastTime != null}">
                            <b></b>
                        </c:if>
                         工单跟进结束时间<br />
                        <fmt:formatDate value="${workOrder.lastTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </li>
                </ul>
            </div>

            <div class="container-left">
                <div class="container-left-title">订单明细</div>
                <ul>
                    <li>订单号：${workOrder.orderCode}</li>
                    <li>客户ID：${workOrder.salesOrder.orderUser}</li>
                    <li>收货人：${workOrder.salesOrder.consignee}</li>
                    <li>手机号码：${workOrder.salesOrder.mobile}</li>
                    <li>商品明细：
                        <c:forEach items="${workOrder.salesOrder.goodsList}" var="goods" varStatus="index">
                            ${goods.skuName}
                            <c:if test="${index.last == false}">
                                、
                            </c:if>
                        </c:forEach>
                    </li>
                </ul>
            </div>
            <div class="container-right">
                <ul>
                    <li style="margin-top:22px; ">单据状态：<span style="color:orange;font-size: 25px;font-weight: bold;">${workOrder.status}</span>
                    </li>
                    <li>处理服务单备注：</li>
                    <textarea id="chatTextarea" class="chatTextarea" style="border: 0 solid;margin: 10px 10px 0 10px;width: 400px;height: 150px;"></textarea>
                    <li id="remarks_work_order" style="margin-top: 175px;">工单备注：
                        <input type="hidden" name="workOrderId" value="${workOrder.id}"/>
                        <textarea name="remarks" cols="80" rows="2" class="textInput" style="margin: -20px 0 0 70px; width: 303px; height: 52px;"></textarea>
                    </li>
                </ul>
            </div>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="button"><div class="buttonContent"><button class="submit" id="work_order_save_remarks" type="button">保存备注</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><a class="add" href="${ctx}/workorder/downZip.json?id=${workOrder.id}"><span>文件下载</span></a></div></div></li>
                <li><div class="button"><div class="buttonContent"><a class="add" href="${ctx}/workorder/to.do?to=up_workorder&id=${workOrder.id}" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="550"><span>升级处理</span></a></div></div></li>
                <li><div class="button"><div class="buttonContent"><a class="add" href="${ctx}/workorder/to.do?to=finish_workorder&id=${workOrder.id}" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="550"><span>完成处理</span></a></div></div></li>
                <li><div class="button"><div class="buttonContent"><a class="add" href="${ctx}/workorder/to.do?to=cancel_workorder&id=${workOrder.id}" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="550"><span>作废处理</span></a></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
            </ul>
        </div>
    </div>


    <script>
        $(function () {

            var remarks = "${workOrder.remarks}";

            $("#work_order_save_remarks").click(function () {

                var id = $("input[name=workOrderId]").val();

                var remarks = $("textarea[name=remarks]").val();

                var prefixUrl = "${ctx}/workorder/update.do";

                $.post(prefixUrl,{id:id,remarks:remarks},function (data) {

                    var result = JSON.parse(data);

                    if(result.result){

                        $("#chatTextarea").val("");

                        initRemarks(result.data);

                    }

                })

            });


            var initRemarks = function (remarks) {

                if(remarks){

                    var remarksArray = remarks.split("^_^");

                    var textareaHtml = "";

                    for (var i = 0 ; i < remarksArray.length ; i++){

                        textareaHtml = textareaHtml + remarksArray[i] + "\r\n\r\n";

                    }

                    $("#chatTextarea").val(textareaHtml);

                }
            };

            initRemarks(remarks);

        });



    </script>