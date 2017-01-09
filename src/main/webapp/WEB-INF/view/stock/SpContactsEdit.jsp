<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>示例画面</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script language="JavaScript" type="text/javascript">
    // 电话号码行号
    var phoneListRowId = -1;
   
    function createTr() {
        var tr = $('<tr></tr>');
        return tr;
    }
   
    function createTd() {
        var td = $('<td></td>');
        return td;
    }
   
    function createSpan() {
        var span = $('<span></span>');
        return span;
    }
   
    function createInput() {
        var input = $('<input></input>');
        return input;
    }
   
    function createMinusButton() {
        var minusButton = $("<input type='button' value='del'></input>");
        return minusButton;
    }
   
    function getPhoneTypeSelect() {
        var phoneTypeSelect = $("<select></select>");
        // 添加option
        phoneTypeSelect.append("<option value='0'>自定义</option>");
        phoneTypeSelect.append("<option value='1'>家庭</option>");
        phoneTypeSelect.append("<option value='2'>手机</option>");
        phoneTypeSelect.append("<option value='3'>公司</option>");
        phoneTypeSelect.append("<option value='4'>公司传真</option>");
        phoneTypeSelect.append("<option value='5'>家庭传真</option>");
        phoneTypeSelect.append("<option value='7'>其他</option>");
        // 默认选择手机[2]
        phoneTypeSelect.val('2');
        return phoneTypeSelect;
    }
   
    function delPhoneListInfo(pDelBtn) {
        // 取得当前tr
        var vTargetTr = pDelBtn.parent().parent();alert(vTargetTr.html());
        // 删除
        vTargetTr.remove();//vTargetTr.find("input").remove();alert(vTargetTr.html());
    }
   
    function phoneTypeChanged(phoneTypeSelect) {
        // 电话分类
        var phoneType = phoneTypeSelect.val();
        var vTargetTr = phoneTypeSelect.parent().parent();
        // 自定义span
        var customSpan = vTargetTr.find("span[id='customSpan']");
        // 自定义input
        var phoneLabel = vTargetTr.find("input[name$='phoneLabel']");
        if (phoneType == '0') {
            // 电话分类是「自定义」时
            customSpan.attr('style', 'display: inline;');
            phoneLabel.attr('style', 'display: inline;');
        } else {
            // 电话分类不是「自定义」时
            customSpan.attr('style', 'display: none;');
            phoneLabel.attr('style', 'display: none;');
            phoneLabel.val('');
        }
    }
   
    function addPhoneListInfo(phoneInfo) {
        // 最大行号判断
        if ($("#phoneListTbl").find("tr").length > 6) {
           alert("无法追加了");
           return false;
        }
        // 行号+1
        phoneListRowId++;
        // 做成tr
        var newTr = createTr();
        // 做成td
        var newTd = createTd();
        // 做成电话分类SELECT，然后添加到td中
        var phoneTypeSelect = getPhoneTypeSelect();
        // 命名例子：phoneList[1].phoneType
        phoneTypeSelect.attr("name", "phoneList[" + phoneListRowId + "].phoneType");
        phoneTypeSelect.change(function() {
            phoneTypeChanged($(this));
        });
        if (phoneInfo != null) {
            phoneTypeSelect.val(phoneInfo.phoneType);
        }
        phoneTypeSelect.appendTo(newTd);
        // 做成定义span项目，然后添加到td中
        var customSpan = createSpan();
        customSpan.text("自定义名");
        customSpan.attr('id', 'customSpan');
        customSpan.attr('style', 'display: none;');
        customSpan.appendTo(newTd);
        // 做成定义input项目，然后添加到td中
        var phoneLabelInput = createInput();
        // 命名例子：phoneList[1].phoneLabel
        phoneLabelInput.attr("name", "phoneList[" + phoneListRowId + "].phoneLabel");
        phoneLabelInput.attr("size","11");
        phoneLabelInput.attr('style', 'display: none;');
        if (phoneInfo != null) {
            phoneLabelInput.val(phoneInfo.phoneLabel);
        }
        phoneLabelInput.appendTo(newTd);
        // 做成电话号码Span，然后添加到td中
        var phoneNumberSpan = createSpan();
        phoneNumberSpan.text("电话号码");
        phoneNumberSpan.appendTo(newTd);
        // 做成电话号码input，然后添加到td中
        var phoneNumberInput = createInput();
        // 命名例子：phoneList[1].phoneNumber
        phoneNumberInput.attr("name", "phoneList[" + phoneListRowId + "].phoneNumber");
        phoneNumberInput.attr("size","11");
        if (phoneInfo != null) {
            phoneNumberInput.val(phoneInfo.phoneNumber);
        }
        phoneNumberInput.appendTo(newTd);
        // 做成删除按钮，然后添加到td中
        var minusButton = createMinusButton();
        minusButton.attr("id","delPhoneIcon");
        minusButton.click(function() {
            delPhoneListInfo($(this));
        });
        var minusButtonTd = createTd();
        minusButton.appendTo(minusButtonTd);
        // 向[table]phoneListTb中添加
        newTd.appendTo(newTr);
        minusButtonTd.appendTo(newTr);
        newTr.appendTo("#phoneListTbl");
        // 执行电话分类的change事件
        phoneTypeSelect.trigger("change");
    }
   
    function initPhoneListInfo() {
        // 从model的phoneList属性中取得数据，放到json中
        var phoneInfoList = [];
        <s:iterator value="phoneList" id="element" status="stt">
        phoneInfoList.push(
                        {
                            phoneType : '<s:property value="#element.phoneType"/>'
                            ,phoneLabel : '<s:property value="#element.phoneLabel"/>'
                            ,phoneNumber : '<s:property value="#element.phoneNumber"/>'
                        }
                    );
        </s:iterator>
        // 把json数据添加到画面table中
        for (var i = 0; i < phoneInfoList.length; i++) {
            // 电话信息json数据
            var phoneInfo = phoneInfoList[i];
            // 添加电话信息
            addPhoneListInfo(phoneInfo);
        }
    }
   
    $(document).ready(function() {
        // [add]按钮的事件
        $("#addPhoneIcon").click(function() {
            addPhoneListInfo(null);
        });
        // [更新]按钮的事件
        $("input[name='updBtn']").click(function() {
            var url = "test!update.action";
            $("#contactsEditFrom").attr("action", url);alert($("#contactsEditFrom").html());
            $("#contactsEditFrom").submit();
        });
        // 电话信息的初始化
        initPhoneListInfo();
    });
</script>
</head>
<body>
<s:fielderror />
<s:actionerror />
<s:actionmessage/>

<s:form id="contactsEditFrom" method="POST" theme="simple">
    <table>
        <tr>
            <td>
                <input type="button" name="updBtn" value="更新" />
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <th style="width:130px;">测试页面ID：</th>
            <td>　</td>
        </tr>
        <tr>
            <th>电话号码：</th>
            <td>
                <input type="button" id="addPhoneIcon" value="add">
                <table id="phoneListTbl"></table>
            </td>
        </tr>
    </table>
</s:form>
</body>
</html>