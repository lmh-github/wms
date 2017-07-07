<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>
<form id="pagerForm" method="post" action="#rel#">
    <input type="hidden" name="page.currentPage" value="1"/>
    <input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" id="fm${rand}" onsubmit="return navTabSearch(this);" action="${ctx}/activity/list.do" method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>活动开始时间：</label>
                    <input type="text" name="startTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${startTimeBegin }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>活动截止时间：</label>
                    <input type="text" name="endTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${endTimeBegin }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>发放情况：</label>
                    <select name="giving">
                        <option value="">请选择</option>
                        <option value="未开始" ${(giving == '未开始')?"selected='true'":""}>未开始</option>
                        <option value="已开始" ${(giving == '已开始')?"selected='true'":""}>已开始</option>
                        <option value="其他" ${(giving == '其他')?"selected='true'":""}>其他</option>
                    </select>
                </li>
                <li>
                    <label>活动平台：</label>
                    <s:select name="platform" value="%{#attr.platform}" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="name" listValue="name" headerValue="请选择" headerKey=""/>
                </li>
                <li>
                    <label>活动状态：</label>
                    <select name="status">
                        <option value="">请选择</option>
                        <option value="未开始" ${(status == '未开始')?"selected='true'":""}>未开始</option>
                        <option value="活动中" ${(status == '活动中')?"selected='true'":""}>活动中</option>
                        <option value="已结束" ${(status == '已结束')?"selected='true'":""}>已结束</option>
                    </select>
                </li>
                <li>
                    <label>高级检索：</label>
                    <input type="text" name="other" value="${other}"/>
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
                <a class="add" href="${ctx}/activity/to.do?to=add" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="690"><span>新建活动</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="add" onclick="filterActivity('活动中')"><span>活动中(${count})</span></a>
            </li>
        </ul>
    </div>
    <table class="list" style="width: 100%;" layoutH="115">
        <thead>
        <tr align="center">
            <th>发起人</th>
            <th>活动平台</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>活动范围</th>
            <th>活动状态</th>
            <th>礼品发放情况</th>
            <th>备注</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.list" var="b">
            <tr target="sid_activity" rel="${id}">
                <td align="center"><s:property value="sponsor"/></td>
                <td align="center"><s:property value="platform"/></td>
                <td align="center"><fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/></td>
                <td align="center"><s:property value="area"/></td>
                <td align="center">
                    <s:if test="status == '未开始'">
                        <span style="background:green;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="status"/></span>
                    </s:if>
                    <s:elseif test="status == '活动中'">
                        <span style="background:#6A6AFF;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="status"/></span>
                    </s:elseif>
                    <s:else>
                        <span style="background:#F79646;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="status"/></span>
                    </s:else>
                </td>
                <td align="center">
                    <s:if test="giving == '未发放'">
                        <span style="background:green;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="giving"/></span>
                    </s:if>
                    <s:elseif test="giving == '已发放'">
                        <span style="background:#6A6AFF;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="giving"/></span>
                    </s:elseif>
                    <s:else>
                        <span style="background:#F79646;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="giving"/></span>
                    </s:else>
                </td>
                <td align="center">
                    <a href="${cfx}/activity/to.do?to=show&id={sid_activity}" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="690"> 查看</a>
                    <s:if test="#currentUser == #sponsor">
                        <a href="${cfx}/activity/to.do?to=add&id={sid_activity}"  target="dialog" rel="dlg_orderInput" mask="true" width="815" height="690">|修改</a>
                    </s:if>
                </td>

            </tr>
        </s:iterator>
        </tbody>
    </table>
    <div class="panelBar">
        <div class="pages">
            <span>显示</span>
            <select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="1"
                ${page.pageSize==1?"selected='true'":""}>1
                </option>
                <option value="50"
                ${page.pageSize==50?"selected='true'":""}>50
                </option>
                <option value="100"
                ${page.pageSize==100?"selected='true'":""}>100
                </option>
                <option value="200"
                ${page.pageSize==200?"selected='true'":""}>200
                </option>
            </select>
            <span>条，共${page.totalRow}条</span>
        </div>
        <div class="pagination" targetType="navTab" totalCount="${page.totalRow }" numPerPage="${page.pageSize }" pageNumShown="10" currentPage="${page.currentPage}"></div>
    </div>
</div>

<script>

    function filterActivity(status){
        $("select[name='status']").val(status);
        $(".buttonContent button[type=submit]").submit();
    }
</script>
