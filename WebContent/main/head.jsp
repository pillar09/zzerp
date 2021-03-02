<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中智数码</title>
<link href="../css/basic.css" rel="stylesheet" type="text/css" />
<link href="../css/main.css" rel="stylesheet" type="text/css" />

<script language=JavaScript>
function logout(){
	if (confirm("您确定要退出管理平台吗？"))
	top.location = "<c:url value='/j_spring_security_logout' />";
	return false;
}
</script>


</head>

<body>
	<div class="admin-top">
     <img src="../image/zz/logo.png" />
   	 <div class="toolbar">
     	<a href="#" target="_self" onclick="logout();"><img src="../image/zz/out.gif" alt="安全退出" /></a>
         <a href="#" ><img src="../image/zz/msg_1.gif" alt="待办事项"/></a>
         <span><a href="../account/userCenter.go" target="main">个人中心</a></span>
         <span><a href="../account/editPassword.go" target="main">修改密码</a></span>
         <span><sec:authentication property="principal.username" />：您好,欢迎登陆使用！</span>
     </div>
    </div>
</body>

</html>

