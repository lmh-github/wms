<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<form id="pagerForm" method="post" action="#rel#">
    <input type="hidden" name="page.currentPage" value="1"/>
    <input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
</form>
<div class="pageHeader" style="margin-bottom: 1px;">
    <form id="fm${rand}" rel="pagerForm" onsubmit="return dialogSearch(this);" action="${ctx}/uc/list.do" method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>登录账号：</label>
                    <input id="account${rand}" type="text" name="account" value="${account}" maxlength="20"/>
                </li>
                <li>
                    <label>用户名称：</label>
                    <input type="text" name="userName" value="${userName}" maxlength="20"/>
                </li>
            </ul>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">检索</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li>
                <a class="add" href="${ctx}/uc/to.do?to=add_user&r=${rand}" target="dialog" rel="add_user" mask="true" width="500" height="400"><span>添加用户</span></a>
            </li>
        </ul>
    </div>
    <table class="table" style="width: 97%" layoutH="138">
        <thead>
        <tr>
            <th align="center"><b>登录账号</b></th>
            <th class="center"><b>用户名称</b></th>
            <th width="80" align="center"><b>选择带回</b></th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.list" var="b">
            <tr target="sid_user">
                <td align="center"><s:property value="ucAccount"/></td>
                <td align="center"><s:property value="userName"/></td>
                <td align="center">
                    <a class="btnSelect" style="float: inherit;" href="javascript:$.bringBack({account:'<s:property value="ucAccount"/>', userName:'<s:property value="userName"/>'})" title="选择">选择</a>
                </td>
            </tr>
        </s:iterator>
        </tbody>
    </table>
    <div class="panelBar">
        <div class="pages">
            <span>显示</span>
            <select class="combox" name="page.pageSize" onchange="dialogPageBreak({numPerPage:this.value})">
                <option value="20" ${page.pageSize==20?"selected='true'":""}>20</option>
                <option value="50" ${page.pageSize==50?"selected='true'":""}>50</option>
                <option value="100" ${page.pageSize==100?"selected='true'":""}>100</option>
                <option value="200" ${page.pageSize==200?"selected='true'":""}>200</option>
            </select>
            <span>条，共${page.totalRow}条</span>
        </div>
        <div class="pagination" targetType="dialog" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        setTimeout(function () {
            $("#account${rand}").focus();
        }, 100);
    });
</script>