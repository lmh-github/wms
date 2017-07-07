<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

    <form id="inputForm" enctype="multipart/form-data" action="${ctx}/activity/save.json?callbackType=closeCurrent&navTabId=tab_list.do" method="post" class="pageForm required-validate" onsubmit="return iframeCallback(this, dialogAjaxDone);">

    <input type="hidden" name="id" value="${activity.id }"/>
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
                    <dd><s:select name="platform" value="%{#attr.activity.platform}"
                                  list="@com.gionee.wms.common.WmsConstants$OrderSourceGionee@values()" listKey="name"
                                  listValue="name" headerValue="请选择" headerKey=""/></dd>
                </dl>
                <dl>
                    <dt>开始日期：</dt>
                    <dd><input type="text" name="startTime" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${activity.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"/></dd>
                </dl>
                <dl>
                    <dt>结束日期：</dt>
                    <dd><input type="text" name="endTime" class="date" size="15" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${activity.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"/></dd>
                </dl>

                <dl>
                    <dt>活动范围：</dt>
                    <dd><input type="text" name="area" value="${activity.area}"/></dd>
                </dl>
                <dl>
                    <dt>活动状态：</dt>
                    <dd>
                        <select name="status">
                            <option value="">请选择</option>
                            <option value="未开始" ${(activity.status == '未开始')?"selected='true'":""}>未开始</option>
                            <option value="活动中" ${(activity.status == '活动中')?"selected='true'":""}>活动中</option>
                            <option value="已结束" ${(activity.status == '已结束')?"selected='true'":""}>已结束</option>
                        </select>
                    </dd>
                </dl>
            </fieldset>
            <fieldset>
                <legend>礼物发放情况</legend>
                <dl>
                    <dt>礼物发放情况：</dt>
                    <dd>
                        <select name="giving">
                            <option value="">请选择</option>
                            <option value="未发放" ${(activity.giving == '未发放')?"selected='true'":""}>未发放</option>
                            <option value="已发放" ${(activity.giving == '已发放')?"selected='true'":""}>已发放</option>
                            <option value="其他" ${(activity.giving == '其他')?"selected='true'":""}>其他</option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>
                <dl>
                    <dt>活动赠品：</dt>
                    <dd><input type="text" name="gift" value="${activity.gift}"/></dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>
                <dl>
                    <dt>抽奖奖品：</dt>
                    <dd><input type="text" name="prize" value="${activity.prize}"/></dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>

                <dl>
                    <dt>备注：</dt>
                    <dd>
                        <textarea name="remarks">${activity.remarks}</textarea>
                    </dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>
                <dl>
                    <dt>发放明细：</dt>
                    <dd>
                        <input type="file" name="uploadFile"/>
                    </dd>
                </dl>
            </fieldset>
            <fieldset>
                <legend>活动细则</legend>
                <textarea class="editor" name="regulation" style="width:630px;height: 182px;" tools="Cut,Copy,FontSize,Bold">${activity.regulation}</textarea>
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

