<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中智数码</title>
<link href="../css/basic.css" rel="stylesheet" type="text/css" />
<link href="../css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main">
  <!-- 表单模块 -->
  <div class="formBox">
    <h3><i></i><span>修改员工信息</span></h3>
	<form action="../account/updateAccount.go" method="post">
	<input type="hidden" name='id' value="${account.id}"/>
    <div class="content">
      <table class="c4">
        <tr>
          <th>登录帐号：</th>
          <td>${account.username }</td>
        </tr>
        <tr>
          <th>员工姓名：</th>
          <td><input type="text" name='full_name' class="inp" value="${account.full_name }" /></td>
        </tr>
        <tr>
          <th>岗位名称：</th>
          <td>
          <select class="select" name="capacity.id" >
             <c:forEach var="capacity" items="${capacitys}" varStatus="status" >
               <option value="${capacity.id}" <c:if test="${account.capacity.id==capacity.id}">selected='selected'</c:if> >${capacity.post}</option>
             </c:forEach>
          </select>
          </td>
        </tr>
        <tr>
          <th>工号：</th>
          <td><input type="text" name="num" class="inp" value="${account.num }" /></td>
        </tr> <tr>
          <th>性别：</th>
          <td><input name="male" type="radio" value="1" <c:if test="${account.male }">checked="checked"</c:if> />男
            <input name="male" type="radio" value="0" <c:if test="${!account.male }">checked="checked"</c:if>/>女<span class="red">*</span></td>
        </tr><tr>
          <th>电话：</th>
          <td><input type="text" name="phone_num" class="inp" value="${account.phone_num }" />
          </td>
        </tr>
        <tr>
          <th>Email：</th>
          <td><input type="text" name="mail" class="inp" value="${account.mail }" />
            <span>普通文字提醒</span></td>
        </tr>
        <tr>
          <th>状态：</th>
          <td><input name="state" type="radio" value="0" <c:if test="${account.state == 0 }">checked="checked"</c:if> />正常
		  <input name="state" type="radio" value="-1" <c:if test="${account.state == -1 }">checked="checked"</c:if>/>冻结</td>
		</tr>
		<tr>
          <th>备注：</th>
          <td><textarea name="description" >${account.description }</textarea></td>
		</tr>
      </table>
    </div>
    <div class="form-but">
      <!-- 表单按钮区 -->
      <button type="submit" class="button-s4">修改</button>
      <button type="button" class="button-s4" onclick="javascript:history.back(-1);">取消</button>
    </div>
    <!-- 表单按钮区 END -->
    </form>
  </div>
  <!-- 表单模块 END -->
</div>
</body>
</html>