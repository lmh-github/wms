
$(document).ready(function () {	        
	
	/* 拼装所有资源扩展名列表信息 */
	$.post("resources/ajaxGetAllExtName.action", function(data) {
		buildSelectOption('extNameSelect', data);
	});
	
});

// 构建select控件中option值
function buildSelectOption(id, data) {
	var jsonObj = eval("(" + data + ")");
	for (var i = 0; i < jsonObj.length; i++) {
		var $option = $("<option></option>");
		$option.attr("value", jsonObj[i]);
		$option.text(jsonObj[i]);
		$("#" + id).append($option);
	}
	// alert($("#"+id).html());
}

//创建并初始化数组
function creatInitArray(obj){
    var arr = new Array(obj.length);
	obj.each(function(i,e){
		arr[i] = $(e).val();
	});
	return arr;
}

//全选
function selAll(name) {
	$('input[name='+name+']').each(function(){
		$this = $(this);
		if (! $this.attr("disabled")) {
			$this.attr("checked", "checked");
		}
	});
}

//反选
function switchAll(name) {
	obj = eval(name);
	if (typeof (obj.length) == "undefined") {
		obj.checked = !obj.checked;
	} else {
		for ( var i = 0; i < obj.length; i++){
			obj[i].checked = !obj[i].checked;
		}
	}
}

//根据资源路径批量下载资源信息（下载当前资源路径对应的ftp服务器上的资源文件）
function batchDownload(resourceId){
	if (resourceId == '') {//批量下载
		var selObj = $("[name=resourceIDs]:checked");
		if (typeof (selObj.length) != "undefined" && selObj.length > 0) {
			if (!confirm("确定要批量下载资源信息吗？")) {
				return;
			}
			openDiv('divSafeDir');
		} else {
			alert("请选择要下载的记录");
		}
	} else {// 单个下载
		if (resourceId != '') {
			if (!confirm("确定要下载资源信息吗？")) {
				return;
			}
			openDiv('divSafeDir');
		} else {
			alert("请选择要下载的记录");
		}
	}
	return false;
}

//根据资源ID批量删除资源信息（同时也删除当前资源ID对应的ftp服务器上的资源文件）
function batchDelete(resourceId){
	if (resourceId == '') {//批量删除
		var selObj = $("[name=resourceIDs]:checked");
		if (typeof (selObj.length) != "undefined" && selObj.length > 0) {
			if (!confirm("确定要批量删除资源信息吗？")) {
				return;
			}
			var resourceIDsArr = creatInitArray(selObj);
			var requestUrl = '/resources/batchDeleteResource.action';
			batchOperateResourceAjax(resourceIDsArr, requestUrl,'batchDelete','');
		} else {
			alert("请选择要删除的记录");
		}
	}else{//单个删除
	   if (resourceId!='') {
			if (!confirm("确定要删除资源信息吗？")) {
				return;
			}
			var resourceIDsArr = resourceId;
			var requestUrl = '/resources/batchDeleteResource.action';
			batchOperateResourceAjax(resourceIDsArr, requestUrl,'batchDelete','');
		} else {
			alert("请选择要删除的记录");
		}
	}
	return false;
}

//异步根据资源ID批量删除资源信息（同时也删除当前资源ID对应的ftp服务器上的资源文件）
//异步根据资源ID批量下载资源信息（批量下载当前资源ID对应的ftp服务器上的资源文件）
function batchOperateResourceAjax(resourceIDsArr,requestUrl,operateFlag,localReceiveDir){
  $.ajax({
		url : requestUrl,
		data : {
			resourceIDs : resourceIDsArr,
			localReceiveDir : localReceiveDir
		},
		type : "post",
		dataType : "json",
		error: function(data){
			alert("对不起，系统异常,请联系相关技术人员!");
		},
		success: function(data){
			if (data.errorMsg == "") {
				if (operateFlag == 'batchDelete') {
					doRefresh();
				} else if (operateFlag == 'batchDownload') {
					$('#msg').text('资源下载成功');
				}
			} else {
				alert(data.errorMsg);
			}
		}
	});
}

function openDiv(divId) { 
	$("#"+divId).OpenDiv(); 
}

function closeDiv(divId) { 
	$("#"+divId).CloseDiv(); 
}
