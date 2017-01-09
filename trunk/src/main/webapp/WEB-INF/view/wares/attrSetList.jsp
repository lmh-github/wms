<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/wares/attrSet!input.action" target="dialog" mask="true" width="800" height="600"><span>添加商品类型</span></a></li>
		</ul>
	</div>
	<table class="list" width="1200" id="list-table">
		<thead>
			<tr>
				<th width="100">商品类型名</th>
				<th width="70">类型描述</th>
				<th width="150">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${attrSetList}" var="item">
			<tr target="sid_user" rel="1">
				<td>${item.attrSetName }</td>
				<td>${item.remark }</td>
				<td>
					<a title="查看属性列表" href="${ctx}/wares/skuAttr.action?attrSet.id=${item.id}"  target="navTab" rel="tab_skuAttr" class="btnView"></a>
					<a title="确实要删除吗？" target="ajaxTodo" href="${ctx}/wares/attrSet!delete.action?id=${item.id}&navTabId=tab_attrSet" class="btnDel"></a>
			        <a title="编辑" target="dialog" mask="true" width="800" height="600" href="${ctx}/wares/attrSet!input.action?id=${item.id}" class="btnEdit"></a>
				</td>

			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script language="JavaScript">
<!--

var imgPlus = new Image();
imgPlus.src = "${ctx}/static/images/menu_plus.gif";

/**
 * 折叠分类列表
 */
function rowClicked(obj)
{
  // 当前图像
  img = obj;
  // 取得上二级tr>td>img对象
  obj = obj.parentNode.parentNode;
  // 整个分类列表表格
  var tbl = document.getElementById("list-table");
  // 当前分类级别
  var lvl = parseInt(obj.className);
  // 是否找到元素
  var fnd = false;
  var sub_display = img.src.indexOf('menu_minus.gif') > 0 ? 'none' : ($.browser.msie) ? 'block' : 'table-row' ;
  // 遍历所有的分类
  for (i = 0; i < tbl.rows.length; i++)
  {
      var row = tbl.rows[i];
      if (row == obj)
      {
          // 找到当前行
          fnd = true;
          //document.getElementById('result').innerHTML += 'Find row at ' + i +"<br/>";
      }
      else
      {
          if (fnd == true)
          {
              var cur = parseInt(row.className);
              var icon = 'icon_' + row.id;
              if (cur > lvl)
              {
                  row.style.display = sub_display;
                  if (sub_display != 'none')
                  {
                      var iconimg = document.getElementById(icon);
                      iconimg.src = iconimg.src.replace('plus.gif', 'minus.gif');
                  }
              }
              else
              {
                  fnd = false;
                  break;
              }
          }
      }
  }

  for (i = 0; i < obj.cells[0].childNodes.length; i++)
  {
      var imgObj = obj.cells[0].childNodes[i];
      if (imgObj.tagName == "IMG" && imgObj.src != '${ctx}/static/images/menu_arrow.gif')
      {
          imgObj.src = (imgObj.src == imgPlus.src) ? '${ctx}/static/images/menu_minus.gif' : imgPlus.src;
      }
  }
}

//-->
</script>	