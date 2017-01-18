<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h2 class="contentTitle">编辑模板</h2>
<div>
	<form method="post" action="${ctx}/basis/printableTemplate!updateShippingTemplate.action?callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return iframeCallback(this)">
	<input type="hidden" name="templateName" value="${templateName }" />
		<div class="pageFormContent" layoutH="95">
			<div class="unit">
				<textarea name="templateContent" id="templateContent" rows="20" cols="200">${templateContent }</textarea>
			</div>
		</div>
		<div class="formBar">
			<ul>
				<shiro:hasPermission name="printableTemplate:editShippingTemplate">
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit" title="test">保存</button></div></div></li>
				</shiro:hasPermission>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
			</ul>
		</div>
	</form>
</div>
<script>
	CKEDITOR.replace("templateContent",{
		coreStyles_bold: { element: 'b' },
		coreStyles_italic: { element: 'i' },

		fontSize_style: {
			element: 'font',
			attributes: { 'size': '#(size)' }
		},

		on: {
	        instanceReady: function( ev ) {
	            // Output paragraphs as <p>Text</p>.
	            this.dataProcessor.writer.setRules( 'p', {
	                indent: false,
	                breakBeforeOpen: true,
	                breakAfterOpen: false,
	                breakBeforeClose: false,
	                breakAfterClose: true
	            });
	        }
	    }
	}); 

	/*$("#btSubmit").bind("click",function(){//$("#btSubmit")获取的是提交按钮，这里绑定提交按钮的click事件
		var editor = CKEDITOR.instances.templateContent;//获得编辑器对象
		var text = editor.getData();//获取编辑器的数据
		alert(text);
		text = CKEDITOR.tools.htmlEncode(text);//进行HTMLEncode编码
		alert(text);
		//$.post("PostUrl",{Text:text},callback);//提交数据，具体的调用方式和返回类型请查阅jQuery的帮助
		//function callback(data){}//回调函数
	});	
*/
</script>