
function checkLoginName() {
    var realname = $.trim($("#username").val().replace(/^\s+|\s+$/g, ""));
    var realname_length = realname.replace(/[^\x00-\xff]/g, 'xx').length;
    if ((realname == null) || (realname == "")) {
        $("#msg").text(" 登录名不能为空");
        $("#username").select();
        $("#username").focus();
        return false;
    } else if ((realname_length > 20) || (realname_length < 4)) {
        $("#msg").text("  登录姓名长度为4－20个字符");
        $("#username").select();
        $("#username").focus();
        return false;
    } else {
        $("#msg").text("");
        return true;
    }
}

function checkPassword() {
	var password = $("#password").val().replace(/^\s+|\s+$/g, "");
	var password_length = password.replace(/[^\x00-\xff]/g, 'xx').length;
	if ((password == null) || (password == "")) {
		$("#msg").text("  密码不能为空");
		  $("#password").select();
	        $("#password").focus();
		return false;
	} else if ((password_length < 6) || (password_length > 20)) {
		$("#msg").text("  密码长度在6-20个字符之间");
		 $("#password").select();
	        $("#password").focus();
		return false;
	} else {
		$("#msg").text("");
		return true;
	}
}

function checkCode(){
	var code = $("#code").val().replace(/^\s+|\s+$/g, "");
	var code_length = code.replace(/[^\x00-\xff]/g, 'xx').length;
	if ((code == null) || (code == "")) {
		$("#msg").text("  验证码不能为空！");
		 $("#code").select();
	        $("#code").focus();
		return false;
	} else if (code_length !=4) {
		$("#msg").text("  验证码应该有4个字符！");
		 $("#code").select();
	        $("#code").focus();
		return false;
	} else {
		$("#msg").text("");
		return true;
	}
}

/**
 * 登录验证
 * @returns {Boolean}
 */
function validate_login() {
    var realname = $.trim($("#username").val().replace(/^\s+|\s+$/g, ""));
    var realname_length = realname.replace(/[^\x00-\xff]/g, 'xx').length;
    if ((realname == null) || (realname == "")) {
        $("#msg").text(" 登录名不能为空");
        $("#username").select();
         $("#username").focus();
         $("#username").css({"border": "1px red solid"}); 
        return false;
    } else if ((realname_length > 50) || (realname_length < 5)) {
        $("#msg").text("  登录姓名长度为5－50个字符");
        $("#username").select();
        $("#username").focus();
        $("#username").css({"border": "1px red solid"}); 
        return false;
    } else {
    	$("#username").css({"border": "1px #ccc solid"}); 
    	var password = $("#password").val().replace(/^\s+|\s+$/g, "");
    	var password_length = password.replace(/[^\x00-\xff]/g, 'xx').length;
    	if ((password == null) || (password == "")) {
    		$("#msg").text("  密码不能为空");
    		$("#password").select();
	          $("#password").focus();
	          $("#password").css({"border": "1px red solid"}); 
    		return false;
    	} else if ((password_length <5) || (password_length > 20)) {
    		$("#msg").text("  密码长度在5-20个字符之间");
    		 $("#password").css({"border": "1px red solid"}); 
    		 $("#password").select();
 	          $("#password").focus();
    		return false;
    	} else {
    		 $("#password").css({"border": "1px #ccc solid"}); 
	
			var code = $("#code").val().replace(/^\s+|\s+$/g, "");
			var code_length = code.replace(/[^\x00-\xff]/g, 'xx').length;
			if ((code == null) || (code == "")) {
				$("#msg").text("  验证码不能为空！");
				 $("#code").css({"border": "1px red solid"}); 
				 $("#code").select();
			        $("#code").focus();
				return false;
			} else if (code_length !=4) {
				$("#msg").text("  验证码应该有4个字符！");
				 $("#code").css({"border": "1px red solid"}); 
				 $("#code").select();
			        $("#code").focus();
			        	return false;
			} else {
				 $("#code").css({"border": "1px #ccc solid"}); 
				$("#msg").text("");
				document.forms[0].submit();
				return true;
			} 
    	}
    }
}


