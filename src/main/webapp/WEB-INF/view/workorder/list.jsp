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
    <form rel="pagerForm" id="fm${rand}" onsubmit="return navTabSearch(this);" action="${ctx}/workorder/list.do" method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <li>
                    <label>创建开始时间：</label>
                    <input type="text" name="createTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeBegin }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>创建截止时间：</label>
                    <input type="text" name="createTimeEnd_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${createTimeEnd }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>订单号：</label>
                    <input type="text" name="orderCode" value="${orderCode}"/>
                </li>
                <li>
                    <label>平台：</label>
                    <s:select name="platform" value="%{#attr.platform}" list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="name" listValue="name" headerValue="请选择" headerKey=""/>
                </li>
                <li>
                    <label>处理人：</label>
                    <input type="text" name="worker" value="${worker}"/>
                </li>
                <li>
                    <label>创建人：</label>
                    <input type="text" name="sponsor" value="${sponsor}"/>
                </li>
                <li>
                    <label>升级处理人：</label>
                    <input type="text" name="uper" value="${uper}"/>
                </li>
                <li>
                    <label>处理状态：</label>
                    <select name="status">
                        <option></option>
                        <option <s:if test="#attr.status == '待处理'">selected</s:if> value="待处理">待处理</option>
                        <option <s:if test="#attr.status == '跟进中'">selected</s:if> value="跟进中">跟进中</option>
                        <option <s:if test="#attr.status == '已完成'">selected</s:if> value="已完成">已完成</option>
                        <option <s:if test="#attr.status == '已作废'">selected</s:if> value="已作废">已作废</option>
                    </select>
                </li>
                <li>
                    <label>级别：</label>
                    <select name="lv">
                        <option></option>
                        <option <s:if test="#attr.lv == '一般'">selected</s:if> value="一般">一般</option>
                        <option <s:if test="#attr.lv == '紧急'">selected</s:if> value="紧急">紧急</option>
                    </select>
                </li>
                <li>
                    <label>接收开始时间：</label>
                    <input type="text" name="acceptTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${acceptTimeBegin }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>接收截止时间：</label>
                    <input type="text" name="acceptTimeEnd_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${acceptTimeEnd }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>完成开始时间：</label>
                    <input type="text" name="lastTimeBegin_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${lastTimeBegin }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
                <li>
                    <label>完成截止时间：</label>
                    <input type="text" name="lastTimeEnd_t" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${lastTimeEnd }"
                pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </li>
            </ul>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button id="work_order_submit" type="submit">检索</button>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="buttonActive" id="export${rand}">
                            <a href="javascript:;"><span>导出excel</span></a>
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
                <a class="add" href="${ctx}/workorder/to_add.do" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="550"><span>新建工单</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="add" onclick="filterWorkOrder('待处理')"><span>待领取(${countMap.get("receive")})</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="add" onclick="filterWorkOrder('跟进中')"><span>跟进中(${countMap.get("flowUp")})</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="add" onclick="filterWorkOrder('已完成')"><span>已完成(${countMap.get("finish")})</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="add" onclick="filterWorkOrder('已作废')"><span>已作废(${countMap.get("cancel")})</span></a>
            </li>
            <li class="line">line</li>
            <li>
                <a class="add" onclick="filterWorkOrder('全部工单')"><span>全部工单</span></a>
            </li>
        </ul>
    </div>
    <table class="list" style="width: 100%;white-space: nowrap;" layoutH="115">
        <thead>
        <tr align="center">
            <th>创建时间</th>
            <th>发起人</th>
            <th>紧急程度</th>
            <th>所属平台</th>
            <th>订单号</th>
            <th>问题明细</th>
            <th>处理状态</th>
            <th>处理结果</th>
            <th>耗时</th>
            <th>处理人</th>
            <th>升级处理人</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator value="#request.list" var="b">
            <tr target="sid_workorder" rel="${id}">
                <td align="center" style="white-space: nowrap;"><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm" /> </td>
                <td align="center" style="white-space: nowrap;"><s:property value="sponsor"/></td>
                <td align="center" style="white-space: nowrap;">
                    <s:if test="lv == '紧急'">
                        <span style="background:red;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="lv"/></span>
                    </s:if>
                    <s:else>
                        <span style="background:#92CDDC;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="lv"/></span>
                    </s:else>
                </td>
                <td align="center" style="white-space: nowrap;"><s:property value="platform"/></td>
                <td align="center" style="white-space: nowrap;"><s:property value="orderCode"/></td>
                <td align="center" style="white-space: normal"><s:property value="description"/></td>
                <td align="center" style="white-space: nowrap;">
                    <s:if test="status == '已完成'">
                        <span style="background:green;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="status"/></span>
                    </s:if>
                    <s:elseif test="status == '跟进中'">
                        <span style="background:#6A6AFF;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="status"/></span>
                    </s:elseif>
                    <s:elseif test="status == '已作废'">
                        <span style="background:grey;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="status"/></span>
                    </s:elseif>
                    <s:else>
                        <span style="background:#F79646;color:white;padding:3px;font-family:'宋体';border-radius:2px;"><s:property value="status"/></span>
                    </s:else>
                </td>
                <td align="center" style="white-space: normal"><s:property value="resultMsg"/></td>

                <td align="center">
                    <c:if test="${lastTime != null}">
                        <c:set var="interval" value="${lastTime.time - createTime.time}"/>
                        <fmt:formatNumber value="${interval/1000/60/60}" pattern="#0.00h"/>
                    </c:if>
                </td>
                <td align="center" style="white-space: nowrap;"><s:property value="worker"/></td>
                <td align="center" style="white-space: nowrap;"><s:property value="uper"/></td>

                <td align="center" style="white-space: nowrap;">
                    <s:if test="#attr.status != '待处理'">
                    <a class="add" href="${ctx}/workorder/to.do?to=look&id={sid_workorder}" target="dialog" rel="dlg_orderInput" mask="true" width="815" height="550"><span>查看</span></a>
                    </s:if>
                    <s:if test="#attr.status == '待处理'">
                         <a class="add" target="ajaxTodo" title="确定接收此工单吗？" href="${ctx}/workorder/accept.do?id={sid_workorder}" mask="true"><span>处理</span></a>
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
<script type="text/javascript">
    (function () {
        if (window["downWorkorder"] == null ){
            window.downWorkorder = function(id) {
                $('<iframe style="display:none" src="${ctx}/atta/down/' + id +'/.do" onload="downLoadEvent(this)" />').appendTo("body");
            }
        }
    })();
</script>
<script type="text/javascript">
    (function () {



        $("#export${rand}").click(function () {
            var params = $("#fm${rand}").serialize();
            $('<iframe style="display:none" src="${ctx}/workorder/export.do?' + params + '&_r=' + (new Date().getTime()) + '" onload="downLoadEvent(this)" />').appendTo("body");
        });
    })();

    var currentUser = "${currentUser}";


    function filterWorkOrder(status){

        $("select[name='status']").val("");

        $("input[name='worker']").val("");

        $("select[name='status']").val(status);

        if(status !== '已作废' && status !== '全部工单'){

             $("input[name='worker']").val(currentUser);

        }

        $("#work_order_submit").submit();



    }
</script>