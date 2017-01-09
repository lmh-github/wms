function checkName() {
	var realname = $("#name").val().replace(/^\s+|\s+$/g, "");
	var realname_length = realname.replace(/[^\x00-\xff]/g, 'xx').length;
	if ((realname == null) || (realname == "")) {
		$("#name_info").text(" 真实姓名不能为空");
		return false;
	} else if ((realname_length > 20) || (realname_length < 2)) {
		$("#name_info").text("  真实姓名长度为2－20个字符");
		return false;
	} else {
		$("#name_info").text("");
		return true;
	}
}

var format_email = /^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2} |net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cn|CN)$/;

function checkEmail() {
	var emailname = $("#email").val().replace(/^\s+|\s+$/g, "");
	if (emailname == '' || emailname == null) {
		$("#email_info").text("邮箱名称不能为空!");
		return false;
	} else if (emailname.match(format_email) == null) {
		$("#email_info").text("邮箱格式不正确!");
		return false;
	} else if (emailname.length > 40) {
		$("#email_info").text("邮箱名称过长!");
		return false;
	} else {
		$("#email_info").text("");
		return true;
	}
}

function checkLoginName() {
	var realname = $("#username").val().replace(/^\s+|\s+$/g, "");
	var realname_length = realname.replace(/[^\x00-\xff]/g, 'xx').length;
	if ((realname == null) || (realname == "")) {
		$("#msg").text(" 登录名不能为空");
		return false;
	} else if ((realname_length > 20) || (realname_length < 4)) {
		$("#msg").text("  登录姓名长度为4－20个字符");
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
		return false;
	} else if ((password_length < 6) || (password_length > 20)) {
		$("#msg").text("  密码长度在6-20个字符之间");
		return false;
	} else {
		$("#msg").text("");
		return true;
	}
}

function reCheckPassword() {
	var password = $("#password").val().replace(/^\s+|\s+$/g, "");
	var verify = $("#repassword").val().replace(/^\s+|\s+$/g, "");
	if (verify != password) {
		$("#repassword_info").text("  两次密码输入不一致");
		return false;
	} else {
		$("#repassword_info").text("");
		return true;
	}
}

function checkMemo(){
	var memo = $("#memo").val().replace(/^\s+|\s+$/g, "");
	if ((memo != null) && (memo != "")) {
		if(memo.replace(/[^\x00-\xff]/g,"**").length>100){
			$("#memo_info").text("备注在100个字符之内");
			return false;
		}	
	}
	return true;	
}


/**
 * 添加操作员验证
 * @returns {Boolean}
 */
function operatorAddCheck() {

	var ck1 = checkName();
	var ck2 = checkEmail();
	var ck3 = checkLoginName();
	var ck4 = checkPassword();
	var ck5 = reCheckPassword();
	var ck6 = checkMemo();

	if (ck1 && ck2 && ck3 && ck4 && ck5 && ck6) {
		return true;
	} else {
		return false;
	}
}

 function operatorUpdateCheck(){
	 
	    var ck1 = checkName();
		var ck2 = checkEmail();
		//var ck3 = checkLoginName(); 
		var ck4 = true;
		var ck5 = true;
		var ck6 = checkMemo();
		
		var password = $("#password").val().replace(/^\s+|\s+$/g, "");
		if ((password != null) && (password != "")) {
			 ck4 = checkPassword();
			 ck5 = reCheckPassword();
		}
		
		if (ck1 && ck2  && ck4 && ck5 && ck6) {
			return true;
		} else {
			return false;
		}
 }
 
 
 function userUpdateCheck(){
	 
	    var ck1 = checkName();
		var ck2 = checkEmail();

		var ck4 = true;
		var ck5 = true;
		
		var password = $("#password").val().replace(/^\s+|\s+$/g, "");
		if ((password != null) && (password != "")) {
			 ck4 = checkPassword();
			 ck5 = reCheckPassword();
		}
		
		if (ck1 && ck2  && ck4 && ck5 ) {
			return true;
		} else {
			return false;
		}
}
