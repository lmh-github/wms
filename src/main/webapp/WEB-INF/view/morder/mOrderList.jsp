<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<form id="pagerForm" method="post" action="#rel#">
  <input type="hidden" name="page.currentPage" value="1" />
  <!--  -->
  <input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
  <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/stock/morder.action" method="post">
    <div class="searchBar">
      <ul class="searchContent">
        <li style="width: 330px;"><label>创建日期从：</label> <input type="text" name="createTimeBegin" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${createTimeBegin}" pattern="yyyy-MM-dd"/>" /> 到： <input type="text" name="createTimeEnd" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${createTimeEnd}" pattern="yyyy-MM-dd"/>" /></li>
        <li style="width: 330px;"><label>状态：</label> <s:select name="order.status" list="#{'1':'已处理', '0':'未处理'}" listKey="key" listValue="value" value="order.status" headerKey="" headerValue="" theme="simple"></s:select></li>
        <li style="width: 330px;"><label>建单类型：</label> <s:select name="order.billType" list="#{'赠品漏发':'赠品漏发', '三包期限内质量问题补发':'三包期限内质量问题补发', '系统BUG':'系统BUG'}" listKey="key" listValue="value" value="order.billType" headerKey="" headerValue="" theme="simple"></s:select></li>
      </ul>
      <ul class="searchContent">
        <li style="width: 330px;"><label>订单号：</label> <input type="text" name="order.orderCode" value="${order.orderCode}" style="width: 190px" /></li>
        <li style="width: 330px;"><label>电话：</label> <input type="text" name="mobile" value="${mobile}" /></li>
        <li style="width: 330px;"><label>是否重开发票：</label> <s:select name="order.invoice" list="#{'1':'是', '0':'否'}" listKey="key" listValue="value" value="order.invoice" headerKey="" headerValue="" theme="simple"></s:select></li>
      </ul>
      <div class="subBar">
        <ul>
          <li><div class="buttonActive"><div class="buttonContent"><button type="submit" id="search${rand}">检索</button></div></div></li>
          <li><div class="buttonActive"><a href="${ctx}/stock/morder.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></div></li>
        </ul>
      </div>
    </div>
  </form>
</div>
<div class="pageContent j-resizeGrid">
  <div class="panelBar">
    <ul class="toolBar">
      <li><a class="add" href="${ctx}/stock/morder!prepareInput.action" target="dialog" rel="dlg_morderInput" mask="true" width="900" height="730">
          <span>新建</span>
        </a></li>
      <li class="line">line</li>
      <li><a class="edit" href="javascript:;" onclick="finish${rand}()"><span>完成此单</span></a></li>
      <li class="line">line</li>
      <li><a class="edit" href="${ctx}/stock/morder!toView.action?order.id={sid_order}" target="dialog" rel="dlg_orderView" mask="true" width="1024" height="768" warn="请选择单据"><span>查看</span></a></li>
      <li style="display: none;"><a id="finish_a${rand}" class="edit" href="${ctx}/stock/morder!toFinish.action?id={sid_order}" target="dialog" mask="true" width="400" height="180" rel="dlg_morderFinish"><span>完成此单</span></a></li>
    </ul>
  </div>
  <table class="list" width="100%" layoutH="140" id="tb${rand}">
    <thead>
      <tr align="center">
        <th>日期</th>
        <th>平台</th>
        <th>订单号</th>
        <th>客户信息</th>
        <th>需建单物品</th>
        <th>补发单号</th>
        <th>是否要重开发票</th>
        <th>建单类型</th>
        <th>建单原因</th>
        <th>扩展备注</th>
        <th>处理状态</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <s:set name="statusMap" value='#{"0":"未处理", "1":"已处理"}' />
      <s:iterator value="dataList" status="status" var="b">
        <tr target="sid_order" rel="${id}" align="center">
          <td><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss" /><input type="hidden" name="id" value="${id}" /></td>
          <td><s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#b.platform}[0].name"/></td>
          <td>${orderCode}</td>
          <td>${consignee}<br>${mobile}</td>
          <td>
            <s:if test="goods != null">
              <s:iterator value="goods" status="i">
                <s:if test="skuCode != null">
                  <span style="color:#1A9B5A;"><s:property value="skuCode" /></span>&nbsp;<span style="color:#D12BC0;"><s:property value="skuName" /></span>&nbsp;<span style="color:#000;"><s:property value="qty" /></span><br/>
                </s:if>
              </s:iterator>
            </s:if>
          </td>
          <td>${newOrderCode}</td>
          <td>${invoice == 0 ? "否" : "是"}</td>
          <td>${billType}</td>
          <td>${remark}</td>
          <td>${extension}</td>
          <td><s:property value="#statusMap[(status + '')]" /><input type="hidden" name="status" value="${status}" /></td>
          <td><s:if test="status != 1"><a href="javascript:;" onclick="delFun${rand}(${id})">删除</a></s:if></td>
        </tr>
      </s:iterator>
    </tbody>
  </table>
  <div class="panelBar">
    <div class="pages">
      <span>显示</span> <select class="combox" name="page.pageSize" onchange="navTabPageBreak({numPerPage:this.value})">
        <option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
        <option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
        <option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
        <option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
      </select> <span>条，共${page.totalRow}条</span>
    </div>
    <div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>
  </div>
<script type="text/javascript">
function finish${rand}() {
	var tb = $("#tb${rand}");
	var selected = tb.find("tr.selected");
	
	var status = selected.find(":hidden[name='status']").val();
	if (status == "1") {
		//alertMsg.correct('操作成功！');
		alert("请选择未处理的行！");
		return;
	}
	$("#finish_a${rand}").trigger("click");
}

function delFun${rand}(id) {
	if(confirm("您确定要删除此单吗？")){
		$.post('${ctx}/stock/morder!delete.action',{id:id}, function(data) {
			if(data.statusCode == "200") {
				alertMsg.correct('操作成功！');
				$("#search${rand}").trigger("click");
			} else {
				alertMsg.warn("操作出现异常！");
			}
		}, "json");
	}
}
</script>
</div>