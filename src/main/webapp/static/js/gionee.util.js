HashSet = function HashSet() {
	if (!(this instanceof HashSet))
		return;

	var _data = {};
	var _length = 0;
	var _DEFAULT = new Date();

	this.contains = function(val) {
		val = val.toString();
		return (!!_data[val] && _data.hasOwnProperty(val));
	};

	this.add = function(val) {
		if (!this.contains(val.toString())) {
			_length++;
		}
		_data[val.toString()] = val;
	};

	this.remove = function(val) {
		val = val.toString();
		if (!this.contains(val)) {
			return false;
		} else {
			delete _data[val.toString()];
			_length--;
			return true;
		}
	};

	this.clear = function() {
		for ( var val in _data) {
			if (_data.hasOwnProperty(val)) {
				delete _data[val];
			}
		}
		_length = 0;
	};

	this.isEmpty = function() {
		return (_length === 0);
	};

	this.size = function() {
		return _length;
	};

	this.toArray = function() {
		_data.length = _length;
		var arr = Array.prototype.slice.call(_data);
		delete _data.length;
		return arr;
	};
}

// 消息框
function falert(str, caption) {
	this.disappear = function() {
		$("#bgdiv").remove();
		$("#msgdiv").remove();
		$("#msgtitle").remove();
		msgobj = null;
		msgdiv = null;
	};
	disappear();
	this.m_text = str;
	this.m_caption = caption;
	this.m_width = 200;
	this.m_height = 100;
	this.m_wait = 2000;
	this.m_fade = 500;
	this.m_bordercolor = "#336699";
	this.m_titlecolor = "#99ccff";
	var msgw, msgh, bordercolor;
	msgw = m_width;// 提示窗口的宽度
	msgh = m_height;// 提示窗口的高度
	titleheight = 25 // 提示窗口标题高度
	bordercolor = m_bordercolor;// 提示窗口的边框颜色
	titlecolor = m_titlecolor;// 提示窗口的标题颜色

	var swidth, sheight;
	swidth = document.body.offsetWidth;
	sheight = document.body.offsetHeight;
	if (sheight < screen.height) {
		sheight = screen.height;
	}
	// //此处可以添加一个背景，防止多次点击保存按钮
	// if(bgobj==undefined){
	// var bgobj=document.createElement("div");
	// }
	// bgobj.setAttribute('id','bgdiv');
	// bgobj.style.position="absolute";
	// bgobj.style.top="0";
	// bgobj.style.background="#777";
	// bgobj.style.filter="progid:dximagetransform.microsoft.alpha(style=3,opacity=25,finishopacity=75";
	// bgobj.style.opacity="0.6";
	// bgobj.style.left="0";
	// bgobj.style.width=swidth + "px";
	// bgobj.style.height=sheight + "px";
	// bgobj.style.zindex = "10000";
	// document.body.appendChild(bgobj);
	if (msgobj == null) {
		var msgobj = document.createElement("div");
	}
	msgobj.setAttribute("id", "msgdiv");
	msgobj.setAttribute("align", "center");
	msgobj.style.background = "white";
	msgobj.style.border = "1px solid " + bordercolor;
	msgobj.style.position = "absolute";
	msgobj.style.left = "50%";
	msgobj.style.top = "50%";
	msgobj.style.font = "12px/1.6em verdana, geneva, arial, helvetica, sans-serif";
	msgobj.style.marginLeft = "-115px";
	msgobj.style.marginTop = -115 + document.documentElement.scrollTop + "px";
	msgobj.style.width = msgw + "px";
	msgobj.style.height = msgh + "px";
	msgobj.style.textAlign = "center";
	msgobj.style.lineHeight = (msgh - titleheight) + "px";
	msgobj.style.zIndex = "10001";
	if (title == null) {
		var title = document.createElement("h4");
	}
	title.setAttribute("id", "msgtitle");
	title.setAttribute("align", "left");
	title.style.margin = "0";
	title.style.padding = "3px";
	title.style.background = bordercolor;
	title.style.filter = "progid:dximagetransform.microsoft.alpha(startx=20, starty=20, finishx=100, finishy=100,style=1,opacity=75,finishopacity=100);";
	title.style.opacity = "0.75";
	title.style.border = "1px solid " + bordercolor;
	title.style.height = "18px";
	title.style.font = "12px verdana, geneva, arial, helvetica, sans-serif";
	title.style.color = "white";
	title.style.cursor = "pointer";
	title.innerHTML = "消息提示";
	title.onclick = function() {
		disappear();
	}
	document.body.appendChild(msgobj);
	document.getElementById("msgdiv").appendChild(title);
	var txt = document.createElement("p");
	txt.style.margin = "1em 0"
	txt.setAttribute("id", "msgtxt");
	txt.innerHTML = str;
	document.getElementById("msgdiv").appendChild(txt);

	this.fadeout = function() {
		$("#bgdiv").fadeOut(2000);
		$("#msgdiv").fadeOut(2000);
		$("#msgtitle").fadeOut(2000, function() {
			disappear()
		});
	}
	setTimeout("fadeout()", 500);
}

function StringBuffer() {
	this.str = "";
	this.objArray = new Array();
	this.append = function (s) {
		if (this.length() == 0) {
			this.objArray[0] = s;
		} else {
			this.objArray[this.length() + 1] = s;
		}
	};
	this.toString = function () {
		if (this.length() == 0) {
			return "";
		} else {
			return this.objArray.join("");
		}
	};
	this.indexOf = function (s) {
		if (this.toString() != "") {
			return this.toString().indexOf(s);
		}
	};
	this.clear = function () {
		if (this.length() != 0) {
			this.objArray.length = 0;
		}
	};
	this.length = function () {
		return this.objArray.length;
	};
	this.substring = function (start, end) {
		if (start > 0 || start == 0) {
			if (end <= this.length() && (end > start)) {
				return this.toString().substring(start, end);
			} else {
				alert("JavaScriptException:IndexOutOfBounds");
				return null;
			}
		} else {
			alert("JavaScriptException:IndexOutOfBounds");
			return null;
		}
	};
	this.toArray = function () {
		var tempArray = this.objArray.join(",");
		return tempArray.split(",");
	};
	this.setcharAt = function (charIndex, str) {
		if (charIndex < 0 || charIndex > this.length()) {
			alert("JavaScriptException:IndexOutOfBounds");
			return null;
		} else {
			this.objArray[charIndex] = str;
		}
		return this.toString();
	};
	this.replace = function (oldchar, newchar) {
		var foundChar = this.toString().indexOf(oldchar);
		if (foundChar < 0) {
			alert("JavaScriptException:not found oldchar");
			return null;
		} else {
			return this.toString().replace(oldchar, newchar);
		}
	};
}

