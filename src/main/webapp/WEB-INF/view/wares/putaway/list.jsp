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
    <form rel="pagerForm" id="fm${rand}" onsubmit="return navTabSearch(this);" action="${ctx}/wares/putaway/list.do" method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>活动平台：</label>
                    <s:select name="platform" value="%{#attr.platform}" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="name" listValue="name" headerValue="请选择" headerKey=""/>
                </li>
                <li>
                    <label>型号：</label>
                    <input type="text" name="waresM" value="${waresM}"/>
                </li>
                <li>
                    <label>活动状态：</label>
                    <select name="status">
                        <option value="">请选择</option>
                        <option value="待定" ${(status == '待定')?"selected='true'":""}>待定</option>
                        <option value="已上架" ${(status == '已上架')?"selected='true'":""}>已上架</option>
                        <option value="已下架" ${(status == '已下架')?"selected='true'":""}>已下架</option>
                    </select>
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
                <a class="add" href="${ctx}/wares/putaway/to.do?to=add" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="400"><span>新建活动</span></a>
            </li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="list" style="width: 100%;" layoutH="115">
        <thead>
        <tr align="center">
            <th>发起人</th>
            <th>平台</th>
            <th>上下架类型</th>
            <th>型号</th>
            <th>版本</th>
            <th>时间</th>
            <th>状态</th>
            <th>备注</th>
            <th>查看</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.list" var="b">
            <tr target="sid_activity" rel="${id}">
                <td align="center"><s:property value="sponsor"/></td>
                <td align="center"><s:property value="platform"/></td>
                <td align="center"><s:property value="type"/></td>
                <td align="center"><s:property value="waresM"/></td>
                <td align="center"><s:property value="version"/></td>
                <td align="center">
                    <s:if test="%{operationTime == null}">
                        待定
                    </s:if>
                    <s:if test="%{operationTime != null}">
                        <fmt:formatDate value="${operationTime}" pattern="yyyy-MM-dd'<br/>'HH:mm:ss"/>
                    </s:if>
                </td>
                <td align="center"><s:property value="status"/></td>
                <td align="center"><s:property value="remarks"/></td>
                <td align="center">
                    <a href="${cfx}/wares/putaway/to.do?to=show&id={sid_activity}" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="400"> 查看</a>
                    <s:if test="#currentUser == #sponsor">
                        <a href="${cfx}/wares/putaway/to.do?to=add&id={sid_activity}"  target="dialog" rel="dlg_orderInput" mask="true" width="815" height="400">|修改</a>
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
