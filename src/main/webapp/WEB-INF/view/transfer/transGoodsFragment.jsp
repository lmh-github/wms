<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:set var="IndivStockStatus" value="@com.gionee.wms.common.WmsConstants$IndivStockStatus@values()"/>
<fieldset>
    <legend>配货信息</legend>
    <table class="list" style="width: 100%" layoutH="170">
        <thead>
        <tr>
            <th>商品名称</th>
            <th>sku编码</th>
            <th>申请数量</th>
            <th>已配货总数</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.goodsList" status="i">
            <tr class="<s:property value="#i.odd?'':'trbg'" />" finish="<s:property value="quantity==preparedNum?'true':'false'" />" style="<s:property value="quantity==preparedNum?'color:#ffffff;background-color:green':''" />">
                <td><s:property value="skuName"/></td>
                <td><s:property value="skuCode"/></td>
                <td><s:property value="quantity"/></td>
                <td><s:property value="preparedNum"/></td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</fieldset>
<fieldset>
    <legend>IMEI信息</legend>
    <table class="list" style="width: 100%;">
        <thead>
        <tr>
            <th>个体编码</th>
            <th>sku编号</th>
            <th>库存状态</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.indivList" var="g" status="i">
            <tr class="<s:property value="#i.odd?'':'trbg'" />">
                <td><s:property value="#g.indivCode"/></td>
                <td><s:property value="#g.skuCode"/></td>
                <td><s:property value="#IndivStockStatus.{?#this.code==#g.stockStatus}[0].name"/></td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
</fieldset>