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
  <c:if test="${result == 0 }">
  <div class="mag-t1"> 修改失败！ </div>
  </c:if>
  <c:if test="${result == 1 }">
  <div class="mag-t2"> 修改成功！ </div>
  </c:if>
  <div class="formBox">
    <h3><i></i><span>修改个人信息</span></h3>
    <form action="../account/updateUserCenter.go" method="post">
    <input type="hidden" name='id' value="${account.id}"/>
    <div class="content">
      <table class="c4">
        <tr>
          <th>账号：</th>
          <td><%= request.getUserPrincipal().getName() %></td>
        </tr>
        <tr>
          <th>工号：</th>
          <td><input type="text" name="num" class="inp" value="${account.num }" /></td>
        </tr>
        <tr>
          <th>真实姓名：</th>
          <td><input type="text" name="full_name" class="inp" value="${account.full_name }" /></td>
        </tr>
        <tr>
          <th>性别：</th>
          <td><input name="male" type="radio" value="1" <c:if test="${account.male }">checked="checked"</c:if> />男
            <input name="male" type="radio" value="0" <c:if test="${!account.male }">checked="checked"</c:if>/>女<span class="red">*</span></td>
        </tr>
        <tr>
          <th>Email：</th>
          <td><input type="text" name="mail" class="inp" value="${account.mail }" /></td>
        </tr>
        <tr>
          <th>电话：</th>
          <td><input type="text" name="phone_num" class="inp" value="${account.phone_num }" /></td>
        </tr>
      </table>
    </div>
    <div class="form-but">
      <!-- 表单按钮区 -->
      <button type="submit" class="button-s4">保存</button>
    </div>
    <!-- 表单按钮区 END -->
    </form>
  </div>
  <!-- 表单模块 END -->
</div>
</body>
</html>