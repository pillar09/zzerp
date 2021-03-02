<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
	<title>中智数码</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

<frameset rows="64,*"  frameborder="no" border="0" framespacing="0">

	<frame src="<c:url value="/main/head.jsp"/>" noresize="noresize" frameborder="NO" name="topFrame" scrolling="no" marginwidth="0" marginheight="0" target="main" />
	
    <frameset cols="202,*" framespacing="0px" border="0" id="frame">
		<frame src="<c:url value="/main/menu.jsp"/>" name="leftFrame"  frameborder="0" scrolling="yes" noresize target="main"/>
		<frame src="<c:url value="/main/welcome.jsp"/>" name="main" frameborder="0" scrolling="yes" target="_self" />
	</frameset>
    <!-- 
    <frame src="<c:url value="/main/foot.html"/>" noresize="noresize" frameborder="NO" name="footFrame" scrolling="no" marginwidth="0" marginheight="0" target="main" />
     -->
    
</frameset>

<noframes>
	<body>您的浏览器无法处理框架</body>
</noframes>

</html>
