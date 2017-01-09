/*
 * 公共JS方法
 */

/*
 * 批量输入以换行组装成字符串
 */
function batchInputGenerator(inputId, hiddenId, limit, msg) {
	limit = limit == '' ? 100 : limit;
	msg = msg == '' ? '商品SN' : msg;
	var inputValue = $.trim($("#" + inputId).val());
	if ($.trim(inputValue) != '') {
		var reg = new RegExp("[\s　]*[\r\n]+[\s　]*", "g");
		inputValue = $.trim(inputValue.replace(reg, ";"));
 		inputValue = inputValue.replace(/[ ]/g,"");
   		$("#" + hiddenId).attr("value", inputValue);
	}
	var inputLength = inputValue.split(";");
	if (inputLength.length > limit) {
		alert("你输入的" + msg + "个数不能超过" + limit + "个!");
		return false;
	}
	return true;
}

/**
 * 验证是否为空值
 * @param id
 * @param msg
 * @returns {Boolean}
 */
function v_isBlank(id, msg){
	var _id = "#"+id;
	var _obj = $(_id);
	var _objInfo = $(_id+"_info");
	if(_obj.length>0 && _obj.val()==""){
		if(undefined != msg && "" != msg){
			if(_objInfo.length>0){
				_objInfo.text(msg+"不能为空!");
			}else{
				alert(msg+"不能为空!");
			}		
		}
		return true;
	}else{
		_objInfo.text("");
		return false;
	}
}


/**
 * 是否整数(包括正负0)
 * @param str
 * @returns {Boolean}
 */
function isAllInteger(str){ 
	var result=str.match(/^-?\d+$/);
	if(result==null) return false; 
	return true; 
} 

/**
 * 是否正整数(包括0)
 * @param str
 * @returns {Boolean}
 */
function isObvInteger(str){ 
	var result=str.match(/^\d+$/);
	if(result==null) return false; 
	return true; 
}


/**
 * 是否正整数(不包括0)
 * @param str
 * @returns {Boolean}
 */
function isInteger(str){ 
	var result=str.match(/^\+?[1-9][0-9]*$/);
	if(result==null) return false; 
	return true; 
}