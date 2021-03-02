<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中智数码</title>
<link href="../css/basic.css" rel="stylesheet" type="text/css" />
<link href="../css/main.css" rel="stylesheet" type="text/css" />
<link href="../css/tools.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/dateinput/dateinput.js"></script>
<script type="text/javascript">
$(function(){

	$("input[type='date']").dateinput({
		  format: 'yyyy-mm-dd',
		  speed: 'fast',
		  lang:'cn'});
	
	$("#btnCleanForm").click(function(event){
		$('input[name="filter.id"]').val("");
		$('input[name="conditionMap[\'deliver_time_start\']"]').val("");
		$('input[name="conditionMap[\'deliver_time_end\']"]').val("");
	});
});
</script>
</head>
<body>
<div class="main">
  <!-- 搜索模块 -->
  <form action="" method="post">
  <div class="searchBox">
    <h3><span>入库搜索</span></h3>
    <table>
       <tr>
        <th>入库单号：</th>
        <td><input type="text" class="inp" name="filter.id" value="${pageInfo_warehousing.filter.id}" /></td>
        
        <th>入库时间：</th>
        <td>
        <input type="date" name="conditionMap['warehouse_date_start']" class="inp" value="${pageInfo_warehousing.conditionMap['warehouse_date_start']}"/>
        ~&nbsp;<input type="date" name="conditionMap['warehouse_date_end']" class="inp" value="${pageInfo_warehousing.conditionMap['warehouse_date_end']}"/>
        </td>
        <th><button type="submit" class="btn">搜 索</button></th>
        <td><button type="submit" class="btn" id="btnCleanForm">清空</button></td>
      </tr>
    </table>
  </div>
  </form>
  <!-- 搜索模块 END -->
  <!-- 列表模块 -->
  <ul class="page-size" >
	 <li>
	  <c:if test="${10 == pageInfo_warehousing.pageSize}"><b>10</b></c:if> 
	  <c:if test="${10 != pageInfo_warehousing.pageSize}">
	 	<a href="../good/warehousingRecord.go?page=1&pageSize=10">10</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${15 == pageInfo_warehousing.pageSize}"><b>15</b></c:if> 
	  <c:if test="${15 != pageInfo_warehousing.pageSize}">
	 	<a href="../good/warehousingRecord.go?page=1&pageSize=15">15</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${20 == pageInfo_warehousing.pageSize}"><b>20</b></c:if> 
	  <c:if test="${20 != pageInfo_warehousing.pageSize}">
	 	<a href="../good/warehousingRecord.go?page=1&pageSize=20">20</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${30 == pageInfo_warehousing.pageSize}"><b>30</b></c:if> 
	  <c:if test="${30 != pageInfo_warehousing.pageSize}">
	 	<a href="../good/warehousingRecord.go?page=1&pageSize=30">30</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${50 == pageInfo_warehousing.pageSize}"><b>50</b></c:if> 
	  <c:if test="${50 != pageInfo_warehousing.pageSize}">
	 	<a href="../good/warehousingRecord.go?page=1&pageSize=50">50</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${100 == pageInfo_warehousing.pageSize}"><b>100</b></c:if> 
	  <c:if test="${100 != pageInfo_warehousing.pageSize}">
	 	<a href="../good/warehousingRecord.go?page=1&pageSize=100">100</a>
 	  </c:if> 
 	 </li>

  </ul>
  <div class="listBox">
      <h3><span>入库单列表 （${pageInfo_warehousing.count}）</span></h3>
      <table >
        <tr>
          <th>序号</th>
          <th>商品类目</th>
          <th>经手人</th>
          <th>入库时间</th>
          <th>操作</th>
        </tr>

        <c:forEach var="warehousing" items="${warehousings}" varStatus="status" >
        <tr <c:if test="${(status.index)%2 != 0}">class="bg"</c:if> >
          <td>
          ${warehousing.id}
          </td>
          <td>${warehousing.title}</td>
          <td>${warehousing.warehouser.username}</td>
          <td><fmt:formatDate pattern="yyyy-MM-dd HH:ss" value="${warehousing.warehouse_date}" /></td>
          <td>
          <a href="../good/detailWarehousing.go?id=${warehousing.id}" title="查看明细" >明细</a>
          </td>
        </tr>
     	</c:forEach>
      </table>
      <div class="pages-box" >
        <div class="pages">
        <c:if test="${pageInfo_warehousing.totalGroup > 1}">
         <a href="../good/warehousingRecord.go?page=${pageInfo_warehousing.preGroup}" >&lt;&lt;</a>
        </c:if>
         <a href="../good/warehousingRecord.go?page=${pageInfo_warehousing.pre}" >&lt;</a>
        
         <c:forEach var="index" items="${pageInfo_warehousing.indices}" varStatus="status" >
         	<c:if test="${index == pageInfo_warehousing.page}"><b>${index}</b></c:if>  
         	<c:if test="${index != pageInfo_warehousing.page}"><a href="../good/warehousingRecord.go?page=${index}">${index}</a></c:if>  
         </c:forEach>
        <a href="../good/warehousingRecord.go?page=${pageInfo_warehousing.next}" >&gt;</a>
        <c:if test="${pageInfo_warehousing.totalGroup > 1}">
         <a href="../good/warehousingRecord.go?page=${pageInfo_warehousing.nextGroup}" >&gt;&gt;</a>
        </c:if>
        <input type="text" size="2" title="输入页码按回车" />
        <a href="#" style="color:#000">GO</a> 页数: [ ${pageInfo_warehousing.page} / ${pageInfo_warehousing.total} ] </div>
      </div>
    
  </div>
  <!-- 列表模块 END -->

</div>
</body>
</html>