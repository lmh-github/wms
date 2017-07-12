<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

    <form id="inputForm" enctype="multipart/form-data" action="${ctx}/wares/putaway/save.json?callbackType=closeCurrent&navTabId=tab_list.do" method="post" class="pageForm required-validate" onsubmit=";notify();return iframeCallback(this, dialogAjaxDone)">

    <input type="hidden" name="id" value="${waresPutaway.id }"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="100">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>发起人：</dt>
                    <dd>${currentUser}</dd>
                </dl>
                <dl>
                    <dt>活动平台：</dt>
                    <dd><s:select name="platform" value="%{#attr.waresPutaway.platform}"
                                  list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="name"
                                  listValue="name" headerValue="请选择" headerKey=""/></dd>
                </dl>
                <dl>
                    <dt>上下架类型：</dt>
                    <dd>
                        <select id="type" name="type">
                            <option value="">请选择</option>
                            <option value="上架" ${(waresPutaway.type == '上架')?"selected='true'":""}>上架</option>
                            <option value="下架" ${(waresPutaway.type == '下架')?"selected='true'":""}>下架</option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt>型号：</dt>
                    <dd><input type="text" id="waresModel" required name="waresM" value="${waresPutaway.waresM}"/></dd>
                </dl>
                <dl>
                    <dt>版本：</dt>
                    <dd><input type="text" id="waresVersion" required name="version" value="${waresPutaway.version}"/></dd>
                </dl>
                <dl>
                    <dt>时间：</dt>
                    <dd><input type="text" name="operationTime" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${waresPutaway.operationTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"/></dd>
                </dl>

                <dl>
                    <dt>状态：</dt>
                    <dd>
                        <select name="status">
                            <option value="">请选择</option>
                            <option value="待定" ${(waresPutaway.status == '待定')?"selected='true'":""}>待定</option>
                            <option value="已上架" ${(waresPutaway.status == '已上架')?"selected='true'":""}>已上架</option>
                            <option value="已下架" ${(waresPutaway.status == '已下架')?"selected='true'":""}>已下架</option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt>备注：</dt>
                    <dd><input type="text" name="remarks" value="${waresPutaway.remarks}"/></dd>
                </dl>
            </fieldset>

        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button type="submit" id="activitySubmit">保存</button>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button class="close" type="button">关闭</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</form>
<script>
    function notify() {
        if(!$("input[name=id]").val() && $("#type").val()=='上架'){
            var model = $("#waresModel").val();
            var version = $("#waresVersion").val();
            messageService.sendMessageAuto(model+version+"上架通知");
        }
    }

</script>
