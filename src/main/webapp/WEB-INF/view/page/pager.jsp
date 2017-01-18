<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>  
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>
<body>


<s:set name="totalPage"><%=request.getParameter("totalPage") %></s:set>
<s:set name="totalRecord"><%=request.getParameter("totalRecord") %></s:set>
<s:set name="currentPage"><%=request.getParameter("currentPage") %></s:set>
<s:set name="queryAction"><%=request.getParameter("queryAction") %></s:set>


<!-- 用移位操作弥补ognl加减分类型转换问题 -->
<s:set name="offsetBegin" value="(#currentPage>3)?((#currentPage<<1)-4>>1):1"></s:set>
<s:set name="offsetEnd" value="(#totalPage-2>#currentPage)?((#currentPage<<1)+4>>1):#totalPage"></s:set>

<div class="libraryPage">
	<fieldset class="page01">
		共 <b><s:property value="#totalPage"></s:property></b> 页, <b><s:property value="#totalRecord"></s:property></b> 条记录&nbsp;
		到第 <input id="inputPageId" onkeypress="keyPress()" name="" type="text" class="inp01" size="5" /> 页 
		<a href="javascript:queryByPage()" onclick="return inputPageCheck()"><img src="/skins/img/but_ok.gif"/></a>
	</fieldset>
	<div class="page02">
		<s:if test="#offsetBegin gt 1">
			<a href="javascript:queryByPage()" onclick="return setPage(this.innerHTML)">1</a>
		</s:if>
		<s:if test="#offsetBegin gt 2">
			<a href="javascript:queryByPage()" onclick="return toPreOffset()">…</a>
		</s:if>
		<s:iterator var="num" begin="#offsetBegin" end="#offsetEnd">
				<s:if test="#currentPage eq #num">
					<span class="spYes"><s:property value="#num"></s:property></span>  
				</s:if>
				<s:else>
					<a href="javascript:queryByPage()" onclick="return setPage(this.innerHTML)"><s:property value="#num"></s:property></a>
				</s:else>			
		</s:iterator>
		<s:if test="#totalPage>#offsetEnd">
			<a href="javascript:queryByPage()" onclick="return toNextOffset()">…</a>
			<a href="javascript:queryByPage()" onclick="return toLast()"><s:property value="#totalPage"></s:property></a> 
		</s:if> 
	</div>
</div>

<s:hidden id="offsetBegin" name="#offsetBegin"></s:hidden>
<s:hidden id="offsetEnd" name="#offsetEnd"></s:hidden>
<s:hidden id="totalPage" name="#totalPage"></s:hidden>
<s:hidden id="queryAction" name="#queryAction"></s:hidden>

<script>
	function inputPageCheck() {
		var pageId_Format = /[1-9][0-9]*$/;
		var inputPageId = $("#inputPageId").val().replace(/^\s+|\s+$/g, "");
		var totalPage = $("#totalPage").val();
		if (inputPageId.match(pageId_Format) == null) {
			return false;
		}else {
			setPage(inputPageId);
			return true;
		}
	}
	function toLast(){
		var totalPage = $("#totalPage").val();
		setPage(totalPage);
		return true;
	}
	function toPreOffset(){
		var offsetBegin = $("#offsetBegin").val();
		setPage(offsetBegin-1);
		return true;
	}
	function toNextOffset(){
		var offsetEnd = $("#offsetEnd").val();
		setPage(parseInt(offsetEnd)+1);
		return true;
	}
	function setPage(pageId){
		$("#pageId").attr("value",pageId);
		return true;
	}
	function queryByPage(){
		var queryAction = $("#queryAction").val();
		var form = document.forms[0];
		form.action = queryAction;
		form.submit();
	}
    function keyPress(){
        if(event.keyCode==13)     //判断回车按钮事件
        {
        	event.returnValue=false;
            event.cancel = true;
        	if(inputPageCheck() == true){
        		queryByPage();
        	}
        }
   }

</script>
</body>