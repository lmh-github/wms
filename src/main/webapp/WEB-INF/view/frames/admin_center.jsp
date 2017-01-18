<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>金立电商</title>  
<link href="${ctx}/template/css/base.css" rel="stylesheet" type="text/css" /> 
<script language=javaScript type=text/javaScript> 
var displayBar=true;
function switchBar(o) {
     if (displayBar) {
           parent.parent.document.getElementById("leftFrame").parentNode.cols="0,*";
           displayBar=false;
           o.src="${ctx}/template/img/admin_center_open.gif"
     } else {
            parent.parent.document.getElementById("leftFrame").parentNode.cols="180,*";
           displayBar=true;
           o.src="${ctx}/template/img/admin_center_close.gif"
     }
}
</script> 
<style> 
body{height:100%; background:url(${ctx}/template/img/admin_centerbg.gif) left top repeat;}
html{height:100%}
</style> 
</head> 
 
<body> 
<table width="10" border="0" cellspacing="0" cellpadding="0" style="height:100%;"> 
  <tr> 
    <td>
       <input title="打开或关闭左侧导航"  onclick=switchBar(this);this.blur(); type="image"  src="${ctx}/template/img/admin_center_close.gif" border="0" name="">
    </td> 
  </tr> 
</table> 
</body> 
</html> 