<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function(){
		_init_area();

		var province=$("#province").val();
        var city=$("#city").val();

        if(province!=''){
        	$("#s_city").val(city);

        	$("#s_province").val(province);
        }
        if(city!=''){
			$("#s_city").val(city);
        }

        $.fn.extend({
        	dwzExport: function(){
        		function _doExport($this) {
        			var $p = $this.attr("targetType") == "dialog" ? $.pdialog.getCurrent() : navTab.getCurrentPanel();
        			var $form = $("#pagerForm", $p);
        			var url = $this.attr("href");
        			//window.location = url+(url.indexOf('?') == -1 ? "?" : "&")+$form.serialize();
        			$form.wrapInner("<form action='" + url + "' method='post'></form>");
        			$form.find(">form").submit();
        			$form.find(">form >").unwrap();
        		}

        		return this.each(function(){
        			var $this = $(this);
        			$this.click(function(event){
        				var title = $this.attr("title");
        				if (title) {
        					alertMsg.confirm(title, {
        						okCall: function(){_doExport($this);}
        					});
        				} else {_doExport($this);}

        				event.preventDefault();
        			});
        		});
        	}
        });
	});
</script>
<form id="pagerForm" method="post" action="#rel#">
	<input type="hidden" name="page.currentPage" value="1" />
	<input type="hidden" name="page.pageSize" value="${page.pageSize}" />
</form>

<div class="pageHeader">
	<input id="province" type="hidden" name="province" value="${s_province}">
	<input id="city" name="city" type="hidden" value="${s_city}">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="${ctx}/report/userAreaReport!listPurRecvSummary.action" method="post">
	<input type="hidden" name="stockCheckId" value="${stockCheckId }" />
	<div class="searchBar">
		<ul class="searchContent">
        			<li style="width: 430px;"><label>创建时间从：</label> <input type="text" name="createTimeBegin" class="date" size="19" dateFmt="yyyy-MM-dd HH:mm:ss" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${createTimeBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>" /> 到： <input type="text" name="createTimeEnd" class="date" size="19" dateFmt="yyyy-MM-dd HH:mm:ss" maxDate="{%y}-%M-%d" value="<fmt:formatDate value="${createTimeEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>" /></li>
		<li style="width: 140px;"><label style="width: 20px;">省：</label>
				<select id="s_province" name="s_province"></select>  
		 </li>
     	<li style="width: 140px;"><label style="width: 20px;">市：</label>
             <select id="s_city" name="s_city" ></select>  
          </li>
          	<!--<li style="width: 230px;"><label style="width: 70px;">退货状态：</label>
                  <select id="s_county" name="s_county"></select>
          		</li>-->


		</ul>

		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li>
			    <li><div class="buttonActive"><a href="${ctx}/report/userAreaReport!listPurRecvSummary.action?exports=1" target="dwzExport" targettype="navTab"><span>导出excel</span></a></div></li>
			</ul>
		</div>

	</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li></li>
		</ul>
	</div>
	<table class="list" width="100%" layoutH="115">
		<thead>
			<tr align="center">
				<th>序号</th>
				<th>省份</th>
				<th>市/县</th>
				<th>区/镇</th>
				<th>机型</th>
				<th>数量</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="userAreaList" status="status">
			<tr target="sid_user" rel="${status.index }">
				<td>${status.index +1  }</td>
				<td>${province }</td>
				<td>${city }</td>
				<td>${district }</td>
				<td>${sku_name }</td>
				<td align="right">${num }</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="1" ${page.pageSize==1?"selected='true'":""}>1</option>
				<option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
				<option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
				<option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>

	</div>
</div>