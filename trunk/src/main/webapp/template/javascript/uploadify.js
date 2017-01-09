
String.prototype.Trim = function()    
{    
	return this.replace(/(^\s*)|(\s*$)/g, "");    
} 

$(document).ready(function () {	        
	
	/* 拼装资源分类一级分类列表信息 */
	$.post("resources/ajaxGetAllRootResourceType.action", function(data) {
		buildSelectOption('firstResTypeId', data);
	});
	
	/* 拼装资源分类二级分类列表信息 */
	$("#firstResTypeId").change(function() {
		$.post("resources/ajaxGetAllChildResourceTypeByParentId.action", {
			parentResTypeId : $("#firstResTypeId").val()
		}, function(data) {
			removeSelectOption('secondResTypeId');
			removeSelectOption('thirdResTypeId');
			setSelectOption('secondResTypeId', '二');
			setSelectOption('thirdResTypeId', '三');
			buildSelectOption('secondResTypeId', data);
		});
	});
	
	/* 拼装资源分类三级分类列表信息 */
	$("#secondResTypeId").change(function() {
		$.post("resources/ajaxGetAllChildResourceTypeByParentId.action", {
			parentResTypeId : $("#secondResTypeId").val()
		}, function(data) {
			removeSelectOption('thirdResTypeId');
			setSelectOption('thirdResTypeId', '三');
			buildSelectOption('thirdResTypeId', data);
		});
	});
  
	/* 选择上传文件 */
    $("#uploadify").uploadify({
	   'uploader'       : '../jqueryupload/uploadify.swf',
	   'script'         : '../scripts/uploadify',//servlet的路径或者.jsp 这是访问servlet 'scripts/uploadif' 
	   'method'         :'GET',  //如果要传参数，就必须改为GET
	   'cancelImg'      : '../jqueryupload/cancel.png',
	   'folder'         : 'uploads', //要上传到的服务器路径，
	   'queueID'        : 'fileQueue',
	   'auto'           : false, //选定文件后是否自动上传，默认false
	   'multi'          : true, //是否允许同时上传多文件，默认false
	   'simUploadLimit' : 1, //一次同步上传的文件数目  
	   'sizeLimit'      : 104857600, //设置单个文件大小限制，单位为byte  
	   'queueSizeLimit' : 10, //限制在一次队列中的次数（可选定几个文件）。默认值= 999，而一次可传几个文件有 simUploadLimit属性决定。
	   //'fileDesc'       : '支持格式:Image(*.jpg;*.jpeg;*.gif;*.png)', //如果配置了以下的'fileExt'属性，那么这个属性是必须的  
	   //'fileExt'        : '*.jpg;*.jpeg;*.gif;*.png',//允许的格式 
	   'buttonImg'      : '../jqueryupload/button_browse.png',//选择文件按钮背景
	   'width'          : 65,
	   'height'			: 30,
	   'scriptData'     :{'firstResTypeId':$('#firstResTypeId').val(),'secondResTypeId':$('#secondResTypeId').val(),'thirdResTypeId':$('#thirdResTypeId').val(),'operatorId':$('#operatorId').val(),'command':'uploadFile'}, // 多个参数用逗号隔开 'name':$('#name').val(),'num':$('#num').val(),'ttl':$('#ttl').val()
	   
	   onSelect : function(e, queueId, fileObj) {
			var filecount = $("#filecount").val();
			filecount++;
			if (filecount > 10) {
				alert("一次最多只可上传10个文件");
				return false;
			}
		},
	   
  	   onComplete : function(event, queueID, fileObj, response, data) {
			var value = response;
			// alert("文件:" + fileObj.name + "上传成功");
			var filecount = $("#filecount").val();
			var strs = new Array(); // 定义一数组
			strs = response.split(","); // 字符分割
			if (response.indexOf("formatNamerror") >= 0) {
				alert("文件:" + fileObj.name + "上传失败，当前上传文件格式不支持!");
			} else {
				var j = strs.length ;
				for (i = 0; i < j; i++) {
					var idvalue = strs[i].substr(strs[i].lastIndexOf("/") + 1);
					var idtemp = idvalue.substr(0, idvalue.lastIndexOf("."));
					var responArrs = strs[i].split('|');
					var remoteFileUrl = responArrs[0];
					var resPhyPath = responArrs[1];
					var uploadStatus = responArrs[2];
					var resourceId = responArrs[3];
					filecount++;
					//remoteFileUrl = remoteFileUrl.substring(0,remoteFileUrl.lastIndexOf('/')+1)+fileObj.name;
					//文件已经存在
					if (uploadStatus == 'File_Exits') {
					   $("#fileQueue").html($("#fileQueue").html() + "&nbsp;&nbsp;&nbsp;&nbsp;" 
					        + "<span id='" + idtemp + "'>" 
							+ "<table class='listtable' style='width: 100%'>"
							+ "<thead>"
							+   "<tr>"
							+     "<th width='30px'>序号</th>"
							+     "<th width='150px'>文件名</th>"
							+     "<th width='300px'>文件路径</th>"
							+     "<th width='150px'>操作</th>"
							+     "<th width='100px'>文件状态</th>"
							+   "</tr>"
							+  "</thead>"
							+  "<tbody>"
					        +   "<tr>"
							+       "<td width='30px'>" 
							+         (filecount)
							+       "</td>"
							+       "<td width='150px'>" 
							+         fileObj.name
							+       "</td>"
							+       "<td width='300px'>" 
							+         remoteFileUrl  
							+       "</td>"
							+       "<td width='150px'>"
							+        "<a href='javascript:void(0)' onclick=\"downloadResource('"+resourceId +"','"+filecount +"')\" >下载</a> &nbsp;&nbsp;"  
							+		 "<a href='javascript:void(0)' onclick=\"replaceResource('"+resPhyPath +"','"+remoteFileUrl +"','"+filecount +"')\"  id ='replaceResource"+filecount +"' >"+ "更新</a>"
							+       "</td>" 
							+       "<td width='100px'>" 
							+          "<div id= 'uploadStatusColor"+filecount +"' style='color: red; font-size: 13px; font-weight: bold;' >"+uploadStatus+"</div>"
							+       "</td>"
							+	"</tr>"
							+  "</tbody>" 
							+  "</table>"
							+"</span>");
					}
					//上传新文件成功,断点续传成功
					if (uploadStatus == 'Upload_New_File_Success' || uploadStatus == 'Upload_From_Break_Success') {
                         $("#fileQueue").html($("#fileQueue").html() + "&nbsp;&nbsp;&nbsp;&nbsp;" 
					        + "<span id='" + idtemp + "'>" 
							+ "<table class='listtable' style='width: 100%'>"
							+ "<thead>"
							+   "<tr>"
							+     "<th width='30px'>序号</th>"
							+     "<th width='150px'>文件名</th>"
							+     "<th width='300px'>文件路径</th>"
							+     "<th width='150px'>操作</th>"
							+     "<th width='100px'>文件状态</th>"
							+   "</tr>"
							+  "</thead>"
							+  "<tbody>"
					        +   "<tr>"
							+       "<td width='30px'>" 
							+         (filecount)
							+       "</td>"
							+       "<td width='150px'>" 
							+         fileObj.name
							+       "</td>"
							+       "<td width='300px'>" 
							+         remoteFileUrl  
							+       "</td>"
							+       "<td width='150px'>"
							+        "<a href='javascript:void(0)' onclick=\"downloadResource('"+resourceId +"','"+filecount +"')\" >下载</a>"  
							+       "</td>" 
							+       "<td width='100px'>" 
							+          "<div id= 'uploadStatusColor"+filecount +"' style='color: blue; font-size: 13px; font-weight: bold;' >"+uploadStatus+"</div>"
							+       "</td>"
							+	"</tr>"
							+  "</tbody>" 
							+  "</table>"
							+"</span>");
					}
					//alert($("#fileQueue").html());
					$("#filecount").val(filecount);
				}	
				$('#msg').text('资源上传完成');
				$(".uploadifyQueueItem").remove();
			}
		},
	   
	   onError : function(event, queueID, fileObj) {
			if (eval(fileObj.size > 1048576000)) {
				alert("此 [\" " + fileObj.name + " \"]的大小超过100MB,不可上传!");
				jQuery('#uploadify').uploadifyCancel(queueID);
			} else {
				alert("文件:" + fileObj.name + "上传失败");
			}
		},

		onCancel : function(event, queueID, fileObj) {
			alert("取消了" + fileObj.name);
		}
	   
	 });
	 
});

/* 文件上传 */
function uploasFile() {
	
	var firstResTypeId = $('#firstResTypeId').val();
	var secondResTypeId = $('#secondResTypeId').val();
	var thirdResTypeId = $('#thirdResTypeId').val();
	var operatorId = $('#operatorId').val();
	if(firstResTypeId=='-1'){
	  alert("请选择一级分类");
	  return false;
	}
	if(secondResTypeId=='-1'){
	  alert("请选择二级分类");
	  return false;
	}
	if(thirdResTypeId=='-1'){
	  alert("请选择三级分类");
	  return false;
	}
	var filecount = $("#fileQueue span img").length;
	var uploadfile = $(".uploadifyQueueItem").length;
	if (eval(filecount + uploadfile) == 0) {
		alert("请选择要上传的资源文件");
		return false;
	}
	if (eval(filecount + uploadfile) > 10) {
		alert("一次最多只可上传10个文件");
		$(".uploadifyQueueItem").remove();
		jQuery('#uploadify').uploadifyClearQueue();
		return false;
	} else {
		if (eval(uploadfile) > 0) {
			// 设置 scriptData 的参数
			$('#uploadify').uploadifySettings('scriptData', {
				'firstResTypeId' : firstResTypeId,
				'secondResTypeId' : secondResTypeId,
				'thirdResTypeId' : thirdResTypeId,
				'operatorId' : operatorId
			});
			// 上传
			jQuery('#uploadify').uploadifyUpload();
		}
		/*setTimeout(function() {
			location.href = "resources/resourceList.action";
		}, 5000);*/
		return true;
	}
}

// 清除select控件中指定项值
function removeSelectOption(id) {
	$("#" + id + " option[value!='']").remove();
}

// 设置select控件中指定项值为指定值
function setSelectOption(id, value) {
	$("#" + id).html("<option value='-1'>请选择" + value + "级分类</option>");
}

// 构建select控件中option值
function buildSelectOption(id, data) {
	var jsonObj = eval("(" + data + ")");
	for (var i = 0; i < jsonObj.length; i++) {
		var $option = $("<option></option>");
		$option.attr("value", jsonObj[i].resTypeId);
		$option.text(jsonObj[i].resTypeName);
		$("#" + id).append($option);
	}
	// alert($("#"+id).html());
}

//更新文件
function replaceResource(localFilePath,remoteFileUrl,filecount) {
	if (!confirm("确定要更新文件服务器上的同名文件吗？")) {
		return;
	}
	$.ajax({
		url : 'resources/ajaxReplaceResource.action',
		data : {
			localFilePath : localFilePath,
			remoteFileUrl : remoteFileUrl
		},
		type : "post",
		dataType : "json",
		error: function(data){
			alert("对不起，系统异常,请联系相关技术人员!");
		},
		success: function(data){
			if (data == 'true') {
				//$('#msg').text('更新文件成功');
				$('#replaceResource' + filecount).attr({style:"display: none;"});
				$('#uploadStatusColor' + filecount).attr({style:"color : blue ;font-size: 13px; font-weight: bold;"});
	            $('#uploadStatusColor' + filecount).text('Update_Old_File_Success');
			} else {
				alert('更新文件失败');
			}
		}
	});
}

//从FTP下载单个资源
function downloadResource(resourceId, filecount) {
	if (resourceId != '') {
		if (!confirm("确定要下载资源信息吗？")) {
			return;
		}
		$('#curResourceId').attr("value", resourceId);
		$('#curFilecount').attr("value", filecount);
		openDiv('divSafeDir');
	} else {
		alert("请选择要下载的记录");
	}
	return false;
}

//异步根据资源ID单个下载资源信息（单个下载当前资源ID对应的ftp服务器上的资源文件）
function downloadResourceAjax(resourceIDsArr,requestUrl,localReceiveDir,filecount){
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
				$('#curResourceId').attr("value", '');
				$('#uploadStatusColor' + filecount).attr({style:"color : blue ;font-size: 13px; font-weight: bold;"});
	            $('#uploadStatusColor' + filecount).text('Download_New_File_Success');
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
