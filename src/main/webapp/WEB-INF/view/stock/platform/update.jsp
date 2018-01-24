<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle"></h2>
<form action="${ctx}/store/platform/update.json?callbackType=closeCurrent&navTabId=tab_store_platform" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone)">
    <input type="hidden" name="id" value="${storePlatform.id}"/>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend></legend>
                <dl >
                    <dt>SKU：</dt>
                    <dd>
                        <input name="skuNo" readonly type="text" value="${storePlatform.skuNo}"/>
                    </dd>
                </dl>
                <dl>
                    <dt>可分配库存：</dt>
                    <dd>
                        <input readonly type="text" value="${unuseStore}" />
                    </dd>
                </dl>
                <dl>
                    <dt>平台：</dt>
                    <dd>
                        <select class="required" name="platformNo"  disabled>
                            <option value="">请选择</option>
                            <c:forEach items="${platforms}" var="platform">
                                <option value="${platform.code}"  ${platform.code==storePlatform.platformNo?"selected":""}>${platform.name}</option>
                            </c:forEach>
                        </select>

                    </dd>
                </dl>
                <dl>
                    <dt>库存：</dt>
                    <dd>
                        <input class="required" name="totalNum" type="number" maxlength="10" max="${unuseStore}" min="0"  value="" />
                    </dd>
                </dl>
                <dl class="nowrap">
                    <dt>备注：</dt>
                    <dd><textarea name="remark" cols="80" rows="2" style="width: 520px;height: 150px;" placeholder="请说明分配原因"></textarea></dd>
                </dl>
            </fieldset>

            <div class="tabs">
            </div>

        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
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
