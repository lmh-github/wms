<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="/skins/css/base.css" rel="stylesheet" type="text/css" />
<link href="/skins/css/common.css" rel="stylesheet" type="text/css" />
<link href="/skins/css/page_admin_main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/skins/js/jquery-1.3.1.js"></script>
<script type="text/javascript" src="/skins/js/resource.js"></script>
<script type="text/javascript" src="/skins/js/validate.js"></script>
<script type="text/javascript" src="/skins/js/selectcheckbox.js"></script>
<script type="text/javascript" src="/skins/js/My97DatePicker/WdatePicker.js"></script>
<title>查询<s:text name="cardTypeDesc" />批次列表</title>
</head>

<script type="text/javascript">

//卡类型
var cardType = '<s:property value="cardType"/>';

//查询批次
function doQuery(){
	if(validateQuery()){
		var form = document.forms[0];
		form.action = "query"+cardType+"BatchList.action";
		form.submit();
	}
}

//新增批次
function doGoCreate(){
	openwindow("goCreate"+cardType+"Batch.action","createWindow",600,650);
}


//修改批次
function doGoModify(batchId){
	if(batchId == ""){
		alert("批次号不能为空");
		return;
	}
	openwindow("goModify"+cardType+"Batch.action?batchId="+batchId,"modifyWindow",600,650);
}

//查看批次下的资源
function doQueryResourceByBatchCode(batchCode){
	if(batchCode == ""){
		alert("批次号不能为空");
		return;
	}
	var form = document.forms[0];
	form.action = "query"+cardType+"List.action?batchCode="+batchCode;
	var target = form.target;
	form.target="_blank";
	form.submit();
	form.target=target;
}


//查看批次下已使用的资源
function doQueryResourceUseBatchId(batchId){
	if(batchId == ""){
		alert("批次号不能为空");
		return;
	}
	var form = document.forms[0];
	form.action = "query"+cardType+"List.action?batchId="+batchId+"&resStatus=2";
	var target = form.target;
	form.target="_blank";
	form.submit();
	form.target=target;
}

//查看批次详细信息
function doQueryBatchDetail(batchId){
	if(batchId == ""){
		alert("批次号不能为空");
		return;
	}
	openwindow("detail"+cardType+"Batch.action?batchId="+batchId,"detailWindow",600,650);
}

//分配资源
function doGoDistrib(batchCode){
	if(batchCode == ""){
		alert("批次号不能为空");
		return;
	}
	var form = document.forms[0];
	form.action = "queryDistrib"+cardType+"List.action?batchCode="+batchCode;
	var target = form.target;
	form.target="_self";
	form.submit();
	form.target=target;
}

function validateQuery(){
	var selectOne = $("#selectOne").val();
	var selectOneValue = $("#selectOneValue").val();
	if(selectOne == "batchId" && selectOneValue != "" && !isInteger(selectOneValue)){
		$("#infoMessage").text("批次号必须为数字");
		$("#selectOneValue").focus();
		return false;
	}
	return true;
}


//刷新
function doRefresh(){
	doQuery();
}

</script>

<body>
<table class="adminMain_top">
	<tbody>
		<tr>
			<td class="td01"></td>
			<td class="td02"><h3 class="topTitle fb f14">查询<s:text name="cardTypeDesc" />批次列表</h3></td>
			<td class="td03"></td>
		</tr>
	</tbody>
</table>

<s:form id="queryForm" theme="simple" method="post">
<div class="adminMain_wrap">
<s:hidden id="pageId" name="currentPage"></s:hidden>
	
	<div class="adminUp_wrap">
		<dl class="adminPath clearfix">
			<dt>您现在的位置：</dt>
			<dd><a href="/admin_main.jsp" target="mainFrame">资源管理系统</a></dd>
			<dd class="last">
			<h3>查询<s:text name="cardTypeDesc" />批次列表</h3>
			</dd>
		</dl>
		
		<fieldset class="clearfix adminSearch">
			<s:select name="selectOne" id="selectOne" list="#{'batchCode':'批次号', 'batchDesc':'批次名称'}" theme="simple" listKey="key" listValue="value"></s:select>
			<s:textfield name="selectOneValue" id="selectOneValue" theme="simple" cssClass="w100 ml10"></s:textfield>
			
			<s:select name="selectTwo" list="#{'createTime':'创建时间', 'begTime':'生效开始时间', 'endTime':'生效结束时间'}" theme="simple" listKey="key" listValue="value"></s:select>
			<s:textfield name="selectTwoValueMin" theme="simple" cssClass="inpData" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>-<s:textfield name="selectTwoValueMax"  theme="simple" cssClass="inpData" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>

			面额:<s:textfield name="amountMin"  theme="simple" cssClass="w100 ml10"/>-<s:textfield name="amountMax"  theme="simple" cssClass="w100 ml10"/>
			<s:select name="batchStatus" theme="simple" list="resourcePropertyUtil.BATCH_STATUS" listKey="key" listValue="value"  headerKey="" headerValue="-批次状态-" />
					
			<input type="image" src="/skins/img/button_query24.gif" class="ml10" onclick="javascript:doQuery();return false"/>
			<s:iterator var="e" value="modubles">
				<s:if test="#e=='goCreate'+cardType+'Batch'">
					<input type="image" src="/skins/img/button_add24.gif" class="ml10" onclick="javascript:doGoCreate();return false" />
				</s:if>
			</s:iterator>

				<br/>
			排序方式：
			<s:select name="sort" theme="simple" list="resourcePropertyUtil.SORT_FIELD" listKey="key" listValue="value" headerKey="" headerValue="-请选择-" />
			<s:select name="order" theme="simple" list="resourcePropertyUtil.SORT_ORDER" listKey="key" listValue="value"/>
			显示：
			<s:select name="pageSize" theme="simple" list="resourcePropertyUtil.PAGE_SIZE" listKey="key" listValue="value"/>
		</fieldset>
		
		<div id="infoMessage" style="color:red">
			<s:iterator value="infoMessages">
				<s:property value="value"/><br />
			</s:iterator>
		</div>
	</div>
		
	<div class="adminContent clearfix">
		<table width="100%" class="table_bg01 table_hg01">
			<thead>
				<tr>
					<th>序号</th>
					<th>批次号</th>
					<th>批次名称</th>
					<th>面额(元)</th>
					<th>发行数量</th>
					<th>使用数量</th>
					<th>可分配数量</th>
					<th>生效开始时间</th>
					<th>生效结束时间</th>
					<th>创建时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="batchlist" status="status">
					<tr>			
						<td><s:property value="#status.index+1"/></td>
						<td>
							<a href="javascript:doQueryResourceByBatchCode(<s:property value="batchCode"/>)">
								<s:property value="batchCode"/>
							</a>
						</td>
						<td>
							<a href="javascript:doQueryBatchDetail(<s:property value="batchId"/>)">
								<s:property value="batchDesc"/>
							</a>
						</td>
						<td><s:property value="amount/100"/></td>
						<td><s:property value="resAmount"/></td>
						<td>
							<s:if test="resAmountUse==0"><s:property value="resAmountUse"/></s:if>
							<s:if test="resAmountUse>0">
								<a href="javascript:doQueryResourceUseBatchId(<s:property value="batchId"/>)">
									<s:property value="resAmountUse"/>
								</a>
							</s:if>
						</td>
						<td><s:property value="canDistribCount"/></td>	
						<td><s:date name="begTime" format="yyyy-MM-dd HH:mm:ss"/></td>
						<td><s:date name="endTime" format="yyyy-MM-dd HH:mm:ss"/></td>
						<td><s:date name="createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
						<td><s:property value="batchStatusDesc"/></td>
						<td>
							<s:if test="batchStatus!=9">
								<s:iterator var="e" value="modubles">
									<s:if test="#e=='goModify'+cardType+'Batch'">
										<a href="javascript:doGoModify(<s:property value="batchId"/>)">编辑</a>
									</s:if>
								</s:iterator>
								<a href="javascript:doGoDistrib(<s:property value="batchCode"/>)">查看分配</a>
							</s:if>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
				
		<div class="w mt20 clearfix">				
			<s:if test="page.totalRecord!=null">
				<s:include value="/layout/pager.jsp">
					<s:param name="totalRecord" value="page.totalRecord"></s:param>
					<s:param name="totalPage" value="page.totalPage"></s:param>
					<s:param name="currentPage" value="page.currentPage"></s:param>
					<s:param name="queryAction" >query<s:property value="cardType"/>BatchList.action</s:param>
				</s:include>
			</s:if>
		</div>
	</div>
	</div>
</s:form>
	
</body>
</html>