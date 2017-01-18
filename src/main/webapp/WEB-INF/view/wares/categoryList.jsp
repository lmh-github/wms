<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${ctx}/wares/category!input.action" target="dialog" mask="true" width="800" height="600"><span>添加</span></a></li>
		</ul>
	</div>
	<table class="list" width="1200" id="list-table">
		<thead>
			<tr>
				<th width="100">分类名称</th>
				<th width="70">分类描述</th>
				<th width="150">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="categoryList">
			<s:if test="id>0">
				<c:set var="level" value="${fn:length(fn:split(catPath,','))}"></c:set>
				<tr align="center" class="${level}"  id="${level}_${id} }">
					<td align="left" class="first-cell" >
			            <img src="${ctx}/static/images/menu_minus.gif" id="icon_${level}_${id} }" width="9" height="9" border="0" style="margin-left:${fn:length(fn:split(catPath,','))-2}em" onclick="rowClicked(this)" />
			            <span><a title="查看分类下的商品SKU" target="navTab" rel="tab_sku" href="${ctx}/wares/sku.action?wares.category.catPath=${catPath}">${catName}</a></span>
				    </td>
				    <td width="50%">${catDesc}</td>
				    <td width="30%" align="center">
				      <shiro:hasPermission name="category:delete">
				        <a title="删除" target="ajaxTodo" href="${ctx}/wares/category!delete.action?id=${id}&navTabId=tab_category" class="btnDel"></a>
				      </shiro:hasPermission>
				      <shiro:hasPermission name="category:edit">
				        <a title="编辑" target="dialog" mask="true" width="800" height="600" href="${ctx}/wares/category!input.action?id=${id}" class="btnEdit"></a>
				      </shiro:hasPermission>
				    </td>
				</tr>
			</s:if>
			</s:iterator>
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