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
<script src="../js/menuTree.js" type="text/javascript"></script>
</head>
<body style="background:#EEF2FB;">
<div class="menu-tree">
  <h3 class="type"><a href="javascript:void(0)">商品管理</a></h3>
  <div class="content">
    <ul>
      <li><a href="<c:url value="/good/publish.go" />" target="main">新增商品</a></li>
      <li><a href="<c:url value="/good/manageGoods.go"/>" target="main">商品管理</a></li>
      <li><a href="<c:url value="/page/todo.html"/>" target="main">商品类目管理</a></li>
      <li><a href="<c:url value="/page/todo.html"/>" target="main">属性管理</a></li>
      <li><a href="<c:url value="/good/warehousing.go"/>" target="main">入库</a></li>
      <li><a href="<c:url value="/good/warehousingRecord.go"/>" target="main">入库记录</a></li>
    </ul>
  </div> 
  <h3 class="type"><a href="javascript:void(0)">交易管理</a></h3>
  <div class="content">
    <ul>
      <li><a href="<c:url value="/bill/toAddBill.go"/>" target="main">新增订单</a></li>
      <li><a href="<c:url value="/bill/manageBills.go?state=0&page=1"/>" target="main">订单管理</a></li>
      <li><a href="<c:url value="/bill/manageBills.go?state=0&page=1&issue=1"/>" target="main">纠纷</a></li>
      <li><a href="<c:url value="/page/todo.html"/>" target="main">退换货管理</a></li>
      <li><a href="<c:url value="/page/todo.html"/>" target="main">物流公司管理</a></li>
      <li><a href="<c:url value="/bill/toScanPacks.go"/>" target="main">扫描发货</a></li>
      <li><a href="<c:url value="/bill/recycleBills.go"/>" target="main">已删除订单</a></li>
    </ul>
  </div> 

<h3 class="type"><a href="javascript:void(0)">报表统计</a></h3>
  <div class="content">
    <ul>
      <li><a href="../page/catSalesCountReport.html" target="main">类别销售额统计</a></li>
      <li><a href="../page/catSalesDiggReport.html" target="main">类别销售排行</a></li>
      <li><a href="../page/salesReport.html" target="main">销售排行</a></li>
      <li><a href="../page/goodsDiggReport.html" target="main">最受欢迎商品</a></li>
      <li><a href="../page/preservationGoodsList.html" target="main">商品统计</a></li>
      <li><a href="../page/preservationGoodsreport.html" target="main">周期商品统计报表</a></li>
    </ul>
  </div>
  <sec:authorize ifAllGranted="ROLE_ADMIN">
  <h3 class="type"><a href="javascript:void(0)">系统管理</a></h3>
  <div class="content">
    <ul>
      <li><a href="../page/todo.html" target="main">系统配置管理</a></li>
      <li><a href="<c:url value="/account/manageAccounts.go"/>" target="main">员工管理</a></li>
      <li><a href="../page/wlList.html" target="main">操作日志</a></li>
      <li><a href="../page/todo.html" target="main">汇率管理</a></li>
    </ul>
  </div>
  </sec:authorize>
  
  <div style="position: absolute; left:10px; bottom: 10px; font-size: 9px; color: gray;">
丰田：TYT<br/>
日产：NS<br/>
本田：HDA<br/>
大众：VW<br/>
福特：FD<br/>
马自达：MZD<br/>
斯巴鲁：SBR<br/>
通用汽车：GM<br/>
三菱：MSB<br/>
现代：HD<br/>
起亚：KA<br/>
奥迪：AD<br/>
奔驰：BS<br/>
标志：PG<br/>
欧宝：OP<br/>
雪铁龙：CTR<br/>
宝马：BMW<br/>
名爵：MG<br/>
凯迪拉克：CDLC<br/>
雷克萨斯：LS<br/>
保时捷：PSC<br/>
通用型：TY<br/>
  </div>
</div>
<script type="text/javascript">
        var contents = document.getElementsByClassName('content');
        var toggles = document.getElementsByClassName('type');
    
        var myAccordion = new fx.Accordion(
            toggles, contents, {opacity: true, duration: 300}
        );
        myAccordion.showThisHideOpen(contents[0]);
    </script>
</body>
</html>
