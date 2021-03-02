<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改日志</title>
<link href="../css/basic.css" rel="stylesheet" type="text/css" />
<link href="../css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script>
</script>
</head>
<body>
<div class="main">
 <h2><span>修改日志及待开发功能</span></h2>
 <c:forEach items="${logs }" var="log">
<p>${log.date } ： ${log.version } </p>
 <div class="formBox"> 
 <div class="content" >
 <c:forEach items="${log.items }" varStatus="status" var="item">
<p>${status.index +1 } 、  ${item } ；</p>
 </c:forEach>
</div></div>
<br></br>
</c:forEach>
</div>
</body>
</html>