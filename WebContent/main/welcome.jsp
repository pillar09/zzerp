<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>中智数码</title>
    <link href="../css/basic.css" rel="stylesheet" type="text/css" />
    <link href="../css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
	
    <div class="welcome">
    	<ul>
        	<li>你好，<strong><sec:authentication property="principal.username" />
        	</strong>！ 欢迎你使用SINOSMART ERP (<a href="../changelog/list.go" title="修改日志"> 1.0.4 </a>)</li>
            <li>软件更新：<span>2017年12月29日</span></li>
            <li>常用操作：</li>
        </ul>
        <p>
        	<a href="../changelog/list.go" title="修改日志"><img src="../image/icon_55_12.gif" /></a>
            <a href="<c:url value="/bill/manageBills.go?state=0&page=1"/>" title="订单管理"><img src="../image/icon_55_2.gif" /></a>
        	<a href="#" title="订单管理"><img src="../image/icon_55_5.gif" /></a>
        	<a href="#" title="修改系统"><img src="../image/icon_55_6.gif" /></a>
            <a href="#" title="退出系统"><img src="../image/icon_55_4.gif" /></a>
        </p>
    </div>
</body>
</html>