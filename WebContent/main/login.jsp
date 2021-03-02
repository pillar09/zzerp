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
<body style="background:#183a52;">

<div class="login">
	<c:if test="${not empty error}">
		<div class="errorblock">
			Your login attempt was not successful, try again.<br /> Caused :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	<div class="login-content">
        <form id="form1" name="form1" method="post" action="<c:url value='../j_spring_security_check' />">
        <fieldset>   
            <legend>登陆系统</legend> 
            <div>
                <label for="j_username">帐号:</label> 
                <input type="text" name='j_username' class="inp2" /> 
            </div>
            <div>
                <label for="j_password">密码:</label> 
                <input type="password"  name='j_password' class="inp2" /> 
            </div>
            <div class="login-btn">
                <input class="btn2" type="submit" value=" "/> 
            </div>
        </fieldset> 
</form>
    </div>
    
</div>

</body>
</html>
