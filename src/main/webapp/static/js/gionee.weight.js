var txtRecive=new StringBuffer();
var isDisplay = false;

function msComm_OnComm() {
	if(mscomm1.CommEvent==2) {
		// 端口接收事件
		txtRecive.append(document.all.mscomm1.input);
		if(isDisplay==false) {
			isDisplay=true;
			setTimeout("dispWeight()",500);
		}
	}
}

function dispWeight() {
	isDisplay=false;
	var dispTxt = txtRecive.toString();
	var index = dispTxt.lastIndexOf("GS");
	if(index!=-1) {
		// 截取重量数
		dispTxt=dispTxt.substring(index+3, dispTxt.lastIndexOf(","));
		// 去除空格
		dispTxt=dispTxt.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	} else {
		dispTxt = "0";
	}
	var weiInput = $("#weight", navTab.getCurrentPanel());
	// 确定记重输入框存在
	if(weiInput.length>0) {
		weiInput.val(dispTxt);
		var dlKuaidi=$("#scanKuaidi", navTab.getCurrentPanel());	// 快递dlKuaidi.show();
		dlKuaidi.show();
		dlKuaidi.find("input[name=shippingNo]").focus();
		//$("#salesSubmit1", navTab.getCurrentPanel()).show();
	}
	setTimeout(function() {
		txtRecive.clear();
	}, 1000);
}