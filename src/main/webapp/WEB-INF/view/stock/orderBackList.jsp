<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<form id="pagerForm" method="post" action="#rel#">
  <input type="hidden" name="page.currentPage" value="1" /> <input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>
<div class="pageHeader">
  <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/orderBack.action" method="post">
    <div class="searchBar">
        <ul class="searchContent">
            <li>
                <label>发起开始时间：</label>
                <input type="text" name="createTimeBegin" class="date" size="19" dateFmt="yyyy-MM-dd HH:mm:ss" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${createTimeBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
            </li>
            <li>
                <label>发起结束时间：</label>
                <input type="text" name="createTimeEnd" class="date" size="19" dateFmt="yyyy-MM-dd HH:mm:ss" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${createTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
            </li>
            <li>
                <label>退货状态：</label>
                <s:select name="backStatus" list="@com.gionee.wms.common.WmsConstants$BackStatus@values()" listKey="code" listValue="name" headerValue="请选择" headerKey=""/>
            </li>
            <li>
                <label>手机号：</label>
                <input type="text" name="mobile" value="<s:property value="mobile" />"/>
            </li>
            <li>
                <label>订单号：</label>
                <input type="text" name="orderCode" value="${orderCode}"/>
            </li>
            <li>
                <label>运单号：</label>
                <input type="text" name="shippingNo" value="${shippingNo}"/>
            </li>
            <li>
                <label>收货人：</label>
                <input type="text" name="consignee" value="<s:property value="consignee" />"/>
            </li>
            <li>
                <label>退换货类型：</label>
                <s:select name="backType" list="#{'back':'退货', 'exchange':'换货', 'rejected':'拒签'}" listKey="key" listValue="value" value="backType" headerKey="" headerValue="" theme="simple"></s:select>
            </li>
            <li>
                <label>是否标记：</label>
                <s:select name="mark" list="#{'1':'是', '0':'否'}" listKey="key" listValue="value" value="mark" headerKey="" headerValue="" theme="simple"></s:select>
            </li>
        </ul>
      <div class="subBar">
        <ul>
          <li><div class="buttonActive"><div class="buttonContent"><button type="submit" id="search${rand }">检索</button></div></div></li>
            <li><div class="buttonActive"><a href="${ctx}/stock/orderBack.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></div></li>
        </ul>
      </div>
    </div>
  </form>
</div>
<div class="pageContent j-resizeGrid">
  <div class="panelBar">
    <ul class="toolBar">
      <li><a class="add" href="${ctx}/stock/orderBack!input.action" target="dialog" rel="dlg_orderBackInput" mask="true" width="900" height="730"><span>新建</span></a></li>
      <li class="line">line</li>
      <li><a class="edit" href="javascript:;" onclick="mark${rand}(1)"><span>标记</span></a></li>
      <li><a class="edit" href="javascript:;" onclick="mark${rand}(0)"><span>取消标记</span></a></li>
      <li class="line">line</li>
      <%-- <li class="line">line</li>
      <li><a class="icon" href="${ctx}/stock/orderBack!toUpload.action" target="dialog" rel="dlg_orderBackUpload" mask="true" width="600" height="250"><span>导入</span></a></li>
      <li class="line">line</li> --%>
    </ul>
  </div>
  <table class="list" width="100%" layoutH="140" style="" id="tb${rand}">
    <thead>
      <tr align="center">
        <th>订单来源</th>
        <th>订单号</th>
        <th>SKU号</th>
        <th>退换类型</th>
        <th>退换货备注</th>
        <th>退款金额</th>
        <th>发起时间</th>
        <th>快递类型</th>
        <th>运单号</th>
        <th>已退货备注</th>
        <th>退货时间</th>
        <th>退货状态</th>
        <th>操作时间</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <s:iterator value="backList" status="status" var="b">
        <tr target="sid_order" rel="${id }" align="center">
          <td>
            <s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#b.orderSource}[0].name"/>
            <input type="hidden" value="${id}" name="id" />
            <input type="hidden" value="${backCode}" name="backCode" />
            <input type="hidden" value="${mark}" name="mark" />
          </td>
          <td>
            <s:if test="mark == 1">
              <span style="background-color:#FF6600;padding:2px 0;">${orderCode}</span>
            </s:if>
            <s:else>
              ${orderCode}
            </s:else>
          </td>
          <td>
            <s:if test="backGoods != null">
              <s:iterator value="backGoods" status="i">
                <s:if test="skuCode != null">
                  <span style="color:#1A9B5A;"><s:property value="skuCode" /></span>&nbsp;<span style="color:#D12BC0;"><s:property value="skuName" /></span><br/>
                </s:if>
              </s:iterator>
            </s:if>
          </td>
          <td>
            <s:if test="backType == 'back'">退货</s:if>
            <s:elseif test="backType == 'exchange'">换货</s:elseif>
            <s:elseif test="backType == 'rejected'">拒签</s:elseif>
          </td>
          <td>${remarkBacking}</td>
          <td>${backMoney}</td>
          <td><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
          <td>${shippingCode}</td>
          <td>${shippingNo}</td>
          <td>${remarkBacked}</td>
          <td><fmt:formatDate value="${backedTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
          <td>
            <s:property value="@com.gionee.wms.common.WmsConstants$BackStatus@values().{?#this.code==#b.backStatus}[0].name"/>
          </td>
          <td><fmt:formatDate value="${handledTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
          <td>
            <a target="dialog" mask="true" width="900" height="730" rel="dlg_viewOrderBack" href="${ctx}/stock/orderBack!toView.action?backCode=${backCode}"><span>查看</span></a>
            <s:if test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@BACKING.code">
              <a target="dialog" mask="true" width="600" height="230" rel="dlg_orderBack" href="${ctx}/stock/orderBack!toConfirm.action?backCode=${backCode}"><span>确认收货</span></a>
              <a class="delete" href="${ctx}/stock/orderBack!cancel.action?backCode=${backCode}" target="ajaxTodo" title="确定要取消吗？"><span>取消</span></a>
            </s:if>
            <s:if test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@BACKED.code and backType == 'exchange'">
              <a target="dialog" mask="true" width="600" height="230" rel="dlg_orderBack" href="${ctx}/stock/orderBack!toConfirm.action?backCode=${backCode}"><span>确认换新</span></a>
            </s:if>
            <s:if test="backStatus==@com.gionee.wms.common.WmsConstants$BackStatus@BACKED.code and backType == 'back'">
              <a target="dialog" mask="true" width="600" height="230" rel="dlg_orderBack" href="${ctx}/stock/orderBack!toConfirm.action?backCode=${backCode}"><span>确认退款</span></a>
            </s:if>
            <%-- <a class="delete" href="${ctx}/stock/orderBack!delete.action?backCode=${backCode}" target="ajaxTodo" title="确定要删除吗？"><span>删除</span></a> --%>
          </td>
        </tr>
      </s:iterator>
    </tbody>
  </table>
  <div class="panelBar">
    <div class="pages">
      <span>显示</span><select class="combox" name="page.pageSize" onchange="navTabPageBreak({numPerPage:this.value})">
        <option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
        <option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
        <option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
        <option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
      </select> <span>条，共${page.totalRow}条</span>
    </div>
    <div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>
  </div>
  <script type="text/javascript">
  function mark${rand}(v){
  	var tb = $("#tb${rand}");
  	var selected = tb.find("tr.selected");
  	if(selected.length == 0) {
  		alert("请选择要标记的行！");
  		return;
  	}
  	
  	var mark = selected.find(":hidden[name='mark']").val();
  	if ((v+"") === mark) {
  		alertMsg.correct('操作成功！');
  		return;
  	}
  	
  	var id = selected.find(":hidden[name='id']").val();
  	var backCode = selected.find(":hidden[name='backCode']").val();
  	$.post('${ctx}/stock/orderBack!mark.action',{id:id, backCode:backCode, mark:v}, function(data) {
  		if(data.statusCode == "200") {
  			alertMsg.correct('操作成功！');
  			$("#search${rand}").trigger("click");
  		} else {
  			alertMsg.warn("操作出现异常！");
  		}
  	}, "json");
  	
  }
  </script>
</div>