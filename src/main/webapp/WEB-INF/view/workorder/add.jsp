<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

<form id="inputForm" action="${ctx}/workorder/save.do?callbackType=closeCurrent&navTabId=tab_list.do" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" name="id" value="${id }"/>
    <input type="hidden" name="orderStatus" value="${status }"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>订单号：</dt>
                    <dd><input name="orderCode" id="orderCode${rand}" maxlength="32" type="text" class="textInput required" value="${orderCode }" <c:if test="${op=='update'}">readonly="readonly"</c:if> /></dd>
                </dl>
                <dl>
                    <dt>订单来源：</dt>
                    <dd>
                        <input name="platform" placeholder="后台自动匹配" readonly="readonly" />
                    </dd>
                </dl>
                <dl>
                    <dt>处理人：</dt>
                    <dd>
                        <input id="worker${rand}" class="required" name="worker" bringBackName="uc.userName" type="text" readonly="readonly" autocomplete="off"/>
                        <a class="btnLook" href="${ctx}/uc/list.do?switch=true" lookupGroup="uc" width="660" height="550">查找</a>
                    </dd>
                </dl>
                <dl>
                    <dt>紧急程度：</dt>
                    <dd>
                        <select name="lv">
                            <option value="一般" <s:if test="paymentType=='一般'">selected</s:if>>一般</option>
                            <option value="紧急" <s:if test="paymentType=='紧急'">selected</s:if>>紧急</option>
                        </select>
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>问题明细：</dt>
                    <dd><textarea name="description" maxlength="512" class="textInput valid required" style="width: 526px;height: 70px;">${remark }</textarea></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="button"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
            </ul>
        </div>
    </div>
</form>
<script type="text/javascript">
</script>
