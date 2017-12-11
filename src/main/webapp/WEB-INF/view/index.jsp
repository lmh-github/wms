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
    <script src="${ctx}/static/js/json2.js" type="text/javascript"></script>

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

/**
 * 初始化音频信息
 */
function initAudio() {
    if (window.ActiveXObject) {
        $("#audioArea").html('<EMBED id="audio_error" src="${ctx}/static/audio/audio_error.wav" align="center" border="0" width="1" height="1" autostart="false" loop="false"/>');
    } else {
        $("#audioArea").html('<audio controls="controls" id="audio_error"><source src="${ctx}/static/audio/audio_error.ogg"></audio>');
    }
}

/**
 * 播放音频
 */
function soundError() {
    try {
        document.getElementById("audio_error").play();
    } catch (err) {
        // do nothing
    }
}

/**
 * 下载事件
 * @param e iframe
 * @returns {*|void}
 */
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
            <ul class="nav">
                <li><a href="${ctx}/account/login!index.action" target="_blank">平台首页</a></li>
                <li><a href="${ctx}/account/login!logout.action">退出</a></li>
            </ul>
        </div>
    </div>

    <div id="leftside">
        <div id="sidebar_s">
            <div class="collapse">
                <div class="toggleCollapse">
                    <div></div>
                </div>
            </div>
        </div>
        <div id="sidebar">
            <div class="toggleCollapse"><h2>主菜单</h2>
                <div>收缩</div>
            </div>

            <div class="accordion" fillSpace="sidebar">
                <div class="accordionContent">
                    <ul class="tree treeFolder" id="sidebar-menu" style="display:none;">
                        ${menuHtml}
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div id="container" style="right: 5px;">
        <div id="navTab" class="tabsPage">
            <div class="tabsPageHeader">
                <div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
                    <ul class="navTab-tab">
                        <li tabid="main" class="main"><a href="javascript:;"><span><span
                            class="home_icon">我的主页</span></span></a></li>
                    </ul>
                </div>
                <div class="tabsLeft" style="display: none;">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
                <div class="tabsRight" style="display: none;">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
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
<object id="mscomm1" classid="clsid:648A5600-2C6E-101B-82B6-000000000014" codebase="${ctx}/static/weight/mscomm32.cab#version=1,0,0,1" style="height: 0;width: 0; position: absolute;left: -999px;top: -999px;">
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
<script language="javascript" for="mscomm1" event="OnComm">
    <!--
    // MSComm1控件每遇到 OnComm 事件就调用 MSComm1_OnComm()函数
    msComm_OnComm();
    //-->
</script>
<script type="text/javascript" src="${ctx}/static/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/static/searchableSelect/jquery.searchableSelect.js"></script>
<link href="${ctx}/static/searchableSelect/jquery.searchableSelect.css" rel="stylesheet" type="text/css" media="screen"/>
<script type="text/javascript">
    $(function() {
        setTimeout(function () {
            $("#sidebar-menu").fadeIn();
        }, 150)
    });
</script>
</body>
</html>
