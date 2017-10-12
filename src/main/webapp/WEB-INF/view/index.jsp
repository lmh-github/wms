<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="shortcut icon" href="${ctx}/static/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7"/>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <title>电商仓管系统</title>

    <link href="${ctx}/static/themes/default/style.css?_=01" rel="stylesheet" type="text/css" media="screen"/>
    <link href="${ctx}/static/themes/css/core.css?_=01" rel="stylesheet" type="text/css" media="screen"/>
    <link href="${ctx}/static/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
    <link href="${ctx}/static/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
    <!--[if IE]>
    <link href="themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->

    <script src="${ctx}/static/js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/jquery.cookie.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/jquery.bgiframe.js" type="text/javascript"></script>
    <script src="${ctx}/static/ckeditor-4.1.2/ckeditor.js" type="text/javascript"></script>
    <script src="${ctx}/static/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>

    <script src="${ctx}/static/js/dwz.core.js?_=01" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.util.date.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.validate.method.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.regional.zh.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.barDrag.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.drag.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.tree.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.accordion.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.ui.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.theme.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.switchEnv.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.alertMsg.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.contextmenu.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.navTab.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.tab.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.resize.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.dialog.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.dialogDrag.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.sortDrag.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.cssTable.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.stable.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.taskBar.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.ajax.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.pagination.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.database.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.datepicker.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.effects.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.panel.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.checkbox.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.history.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.combox.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.print.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/gionee.util.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/gionee.weight.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/area.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/dwz.regional.zh.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/tooltip.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/xheditor/xheditor-1.2.2.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx}/dwr/util.js"></script>
    <script type="text/javascript" src="${ctx}/dwr/engine.js"></script>
    <script type="text/javascript" src="${ctx}/dwr/interface/messageService.js"></script>

    <script type="text/javascript">
$(function () {
    DWZ.init("${ctx}/static/dwz.frag.xml", {
        loginUrl: "${ssoUrl}",	// 跳到SSO登录页面
        statusCode: {ok: 200, error: 300, timeout: 301}, //【可选】
        pageInfo: {
            pageNum: "page.currentPage",
            numPerPage: "page.pageSize",
            orderField: "orderField",
            orderDirection: "orderDirection"
        }, //【可选】
        debug: false,	// 调试模式 【true|false】
        callback: function () {
            initEnv();
        }
    });
    initAudio();
    try {
        document.all.mscomm1.PortOpen = true;
    } catch (ex) {
        alert("初始化串口失败: " + ex.message);
    }
});

function initAudio() {
    var Sys = {};
    try {
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject)
            Sys.ie = ua.match(/msie ([\d.]+)/)[1];
        else if (document.getBoxObjectFor)
            Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];
        else if (window.MessageEvent && !document.getBoxObjectFor)
            Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];
        else if (window.opera)
            Sys.opera = ua.match(/opera.([\d.]+)/)[1];
        else if (window.openDatabase)
            Sys.safari = ua.match(/version\/([\d.]+)/)[1];
    } catch (err) {
        alert("初始化音频控件失败:" + err.message);
    }
    if (Sys.chrome) {
        $("#audioArea").html('<audio controls="controls" id="audio_error"><source src="${ctx}/static/audio/audio_error.wav"></audio>');
    } else {
        $("#audioArea").html('<EMBED id="audio_error" src="${ctx}/static/audio/audio_error.wav" align="center" border="0" width="1" height="1" autostart="false" loop="false"/>');
    }
}

function soundError() {
    try {
        document.getElementById("audio_error").play();
    } catch (err) {
        // do nothing
    }
}

window['downLoadEvent'] = function (e) {
    var content = e.contentWindow.document.body.innerHTML;
    $(e).remove();
    if (/HTTP Status 404/g.test(content)) {
        return alertMsg.error("您要下载的文件找不到了！");
    }
    if (/HTTP Status 500/g.test(content)) {
        return alertMsg.error("服务器下载出现异常！");
    }
    if ($.trim(content).length > 0) {
        return alertMsg.error($.trim(content));
    }
}
</script>
</head>

<body scroll="no" >
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<!-- <a class="logo" href="http://www.gionee.com">标志</a> -->
				<ul class="nav">
					<li><a href="${ctx}/account/login!index.action" target="_blank">平台首页</a></li>
					<li><a href="${ctx}/account/login!logout.action">退出</a></li>
				</ul>
				<ul class="themeList" id="themeList" style="display: none;">
					<li theme="default"><div class="selected">蓝色</div></li>
					<li theme="green"><div>绿色</div></li>
					<li theme="purple"><div>紫色</div></li>
					<li theme="silver"><div>银色</div></li>
					<li theme="azure"><div>天蓝</div></li>
				</ul>
			</div>

			<!-- navMenu -->

		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse"><h2>主菜单</h2><div>收缩</div></div>

				<div class="accordion" fillSpace="sidebar">
					<div class="accordionContent">
						<ul class="tree treeFolder"><!--
							<li><a href="tabsPage.html" target="navTab">主框架面板</a>
								<ul>
									<li><a href="main.html" target="navTab" rel="main">我的主页</a></li>
								</ul>
							</li> -->
							${menuHtml}

<%
	/*
	root 是菜单树，但是根据显示的实际情况，只输出两级，所以配置菜单的时候需要注意。
	生成的菜单 参照以上 菜单的html结构

Menu root = (Menu) request.getAttribute("menu");
List<Menu> menuGroups = root.getChildMenus();
for (Menu menu : menuGroups) {
	out.println("<li><a>" + menu.getName()+ "</a> ");
	out.println("<ul>");
	List<Menu> childMenus = menu.getChildMenus();
	for (Menu childMenu : childMenus) {
		String actionFullName = org.apache.commons.lang3.StringUtils.substringAfterLast(childMenu.getUrl(),"/");
		String actionName = org.apache.commons.lang3.StringUtils.substringBefore(actionFullName,".action");
		if(actionName!=null){
			if(actionName.indexOf("!")>-1){
				actionName = actionName.substring(actionName.indexOf("!")+1);
			}
			actionName ="tab_"+actionName;
		}else{
			actionName="";
		}

		out.println("<li>");
		out.println("<a href=\"" + request.getContextPath()+ childMenu.getUrl()+ "\" target=\"navTab\" rel=\""+actionName+"\">" + childMenu.getName()+ "</a>");
		out.println("</li>");
	}
	out.println("</ul>");
	out.println("</li>");
}*/
%>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo" style="width: 100%;height: 100%;">
							<p><span>欢迎登录系统！</span></p>
						</div>
						<div class="pageFormContent" layoutH="80" style="margin-right:230px">
						</div>
					</div>

				</div>
			</div>
		</div>
    </div>


<div id="audioArea" style="display: none;"></div>
<object id="mscomm1" classid="clsid:648A5600-2C6E-101B-82B6-000000000014" codebase="${ctx}/static/weight/mscomm32.cab#version=1,0,0,1" style="height: 0;width: 0; position: absolute;left: -999px;top: -999px;" >
  <param name="CommPort" value="3">
  <param name="InBufferSize" value="1024">
  <param name="InputLen" value="1">
  <param name="NullDiscard" value="0">
  <param name="OutBufferSize" value="512">
  <param name="RThreshold" value="1">
  <param name="RTSEnable" value="0">
  <param name="BaudRate" value="9600">
  <param name="ParitySetting" value="0">
  <param name="DataBits" value="8">
  <param name="StopBits" value="0">
  <param name="SThreshold" value="0">
  <param name="EOFEnable" value="0">
  <param name="InputMode" value="0">
</object>
<SCRIPT   LANGUAGE="javascript"   FOR="mscomm1"   EVENT="OnComm">
   <!--
   // MSComm1控件每遇到 OnComm 事件就调用 MSComm1_OnComm()函数
           msComm_OnComm();
    //-->
</SCRIPT>
<script type="text/javascript" src="${ctx}/static/layer/layer.js"></script>
<script type="text/javascript">
    function showMessage(msg){
        //边缘弹出
        var index = layer.open({
            type: 1
            ,title:false
            ,offset: 'rb' //具体配置参考：offset参数项
            ,content: '<div style="padding: 30px 80px;font-size: 16px;">'+msg+'</div>'
            ,btnAlign: 'c' //按钮居中
            ,shade: 0 //不显示遮罩
        });
        $('#layui-layer'+index).css({'margin-top':'-40px',"margin-left":'-10px'});
    }

    $(function () {
        $.post("${ctx}/workorder/todo.do", function (data) {
            if (data.result) {
                if (data.data > 0) {
                    layer.open({
                        type: 1
                        ,title: false
                        ,offset: 'cc' //具体配置参考：offset参数项
                        ,content: '<div style="padding: 30px 80px;font-size: 16px;">您有【<span style="color: red;font-size: 18px;">' + data.data + '</span>】张工单待处理！</div>'
                        ,shade: 0 //不显示遮罩
                        ,time: 5000
                        ,anim: 0
                    });
                }
            }
        }, "JSON");
    });
</script>
    <script type="text/javascript" src="${ctx}/static/searchableSelect/jquery.searchableSelect.js"></script>
    <link href="${ctx}/static/searchableSelect/jquery.searchableSelect.css" rel="stylesheet" type="text/css" media="screen"/>
</body>
</html>
