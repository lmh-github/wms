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
                    <dt>预警名称：</dt>
                    <dd>${orderOutlier.name}</dd>
                </dl>
                <dl>
                    <dt>预警单价：</dt>
                    <dd>${orderOutlier.unitPrice}</dd>
                </dl>
                <dl>
                    <dt>开始日期：</dt>
                    <dd><fmt:formatDate value="${orderOutlier.beginTime }" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
                </dl>
                <dl>
                    <dt>结束日期：</dt>
                    <dd><fmt:formatDate value="${orderOutlier.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
                </dl>

                <dl>
                    <dt>订单来源：</dt>
                    <dd>
                        <c:forEach items="${fn:split(orderOutlier.orderSource,',')}" var="var" varStatus="status">
                            <c:set var="data" scope="request" value="${var}"/>
                            <s:property value="@com.gionee.wms.common.WmsConstants$OrderSource@values().{?#this.code==#request.data}[0].name"/>
                            <c:if test="${status.last == false}">
                                ,
                            </c:if>
                        </c:forEach>
                    </dd>
                </dl>
                <dl>
                    <dt>sku：</dt>
                    <dd>${orderOutlier.skuIds}</dd>
                </dl>

                <dl>
                    <dt>是否开启：</dt>
                    <dd>
                        <c:if test="${orderOutlier.logSwitch == '0'}">
                            已开启
                        </c:if>
                        <c:if test="${orderOutlier.logSwitch == '1'}">
                            已关闭
                        </c:if>
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
