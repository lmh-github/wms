<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<s:set value="@java.lang.Math@random().toString().substring(2, 10)" name="rand"/>

<style>
    .pageFormContent p{
        width: 100%;
    }
</style>

    <input type="hidden" name="id" value="${activity.id }"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="100">
            <fieldset>
                <legend>基本信息</legend>
                <dl>
                    <dt>发起人：</dt>
                    <dd>${activity.sponsor}</dd>
                </dl>
                <dl>
                    <dt>活动平台：</dt>
                    <dd>${activity.platform}</dd>
                </dl>
                <dl>
                    <dt>开始日期：</dt>
                    <dd><fmt:formatDate value="${activity.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
                </dl>
                <dl>
                    <dt>结束日期：</dt>
                    <dd><fmt:formatDate value="${activity.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
                </dl>

                <dl>
                    <dt>活动范围：</dt>
                    <dd>${activity.area}</dd>
                </dl>
                <dl>
                    <dt>活动状态：</dt>
                    <dd>${activity.status}</dd>
                </dl>
            </fieldset>
            <fieldset>
                <legend>活动细则</legend>
                ${activity.regulation}
            </fieldset>
            <fieldset>
                <legend>礼物发放情况</legend>
                <dl>
                    <dt>礼物发放情况：</dt>
                    <dd>${activity.giving}</dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>
                <dl>
                    <dt>活动赠品：</dt>
                    <dd>${activity.gift}</dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>
                <dl>
                    <dt>抽奖奖品：</dt>
                    <dd>${activity.prize}</dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>

                <dl>
                    <dt>备注：</dt>
                    <dd>${activity.remarks}</dd>
                </dl>
                <dl>
                    <dt></dt>
                    <dd></dd>
                </dl>
                <dl>
                    <dt>发放明细：</dt>
                    <dd>
                        <a href="${cfx}/activity/down.do?id=${activity.id}" ><span style="">${activity.fileName}</span></a>
                    </dd>
                </dl>
            </fieldset>

        </div>
        <div class="formBar">
            <ul>
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
