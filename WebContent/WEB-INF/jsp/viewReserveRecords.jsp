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
    <h3><span>商品信息</span></h3>
    <table>
       <tr>
       <th>商品类目：</th>
        <td>${goodInfo.category.title}</td>
       <th>主机型号：</th>
        <td>${goodInfo.model}</td>
       <th>规格：</th>
        <td>${goodInfo.spec}</td>
       <th>库存【可用】：</th>
        <td>${goodInfo.reserve}
		  <c:if test="${goodInfo.reserve>goodInfo.ordered}">
		  	<span style="color: #00cc00;">【${goodInfo.reserve-goodInfo.ordered}】</span>
		  </c:if>
		  <c:if test="${goodInfo.reserve<=goodInfo.ordered}">
		  	<span style="color: #dd0000;">【${goodInfo.reserve-goodInfo.ordered}】</span>
		  </c:if>
         </td>
      </tr>
    </table>
  </div>
  </form>
  <!-- 搜索模块 END -->
  <!-- 列表模块 -->
  <ul class="page-size" >
	 <li>
	  <c:if test="${10 == pageInfo_reserveRecord.pageSize}"><b>10</b></c:if> 
	  <c:if test="${10 != pageInfo_reserveRecord.pageSize}">
	 	<a href="../good/viewReserveRecords.go?page=1&pageSize=10">10</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${15 == pageInfo_reserveRecord.pageSize}"><b>15</b></c:if> 
	  <c:if test="${15 != pageInfo_reserveRecord.pageSize}">
	 	<a href="../good/viewReserveRecords.go?page=1&pageSize=15">15</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${20 == pageInfo_reserveRecord.pageSize}"><b>20</b></c:if> 
	  <c:if test="${20 != pageInfo_reserveRecord.pageSize}">
	 	<a href="../good/viewReserveRecords.go?page=1&pageSize=20">20</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${30 == pageInfo_reserveRecord.pageSize}"><b>30</b></c:if> 
	  <c:if test="${30 != pageInfo_reserveRecord.pageSize}">
	 	<a href="../good/viewReserveRecords.go?page=1&pageSize=30">30</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${50 == pageInfo_reserveRecord.pageSize}"><b>50</b></c:if> 
	  <c:if test="${50 != pageInfo_reserveRecord.pageSize}">
	 	<a href="../good/viewReserveRecords.go?page=1&pageSize=50">50</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${100 == pageInfo_reserveRecord.pageSize}"><b>100</b></c:if> 
	  <c:if test="${100 != pageInfo_reserveRecord.pageSize}">
	 	<a href="../good/viewReserveRecords.go?page=1&pageSize=100">100</a>
 	  </c:if> 
 	 </li>

  </ul>
  <div class="listBox">
      <h3><span>进销存列表 （${pageInfo_reserveRecord.count}）</span></h3>
      <table >
        <tr>
          <th>序号</th>
          <th width="120px">操作时间</th>
          <th>经手人</th>
          <th>发货数量</th>
          <th>入库数量</th>
          <th>结存</th>
          <th>相关链接</th>
        </tr>

        <c:forEach var="reserveRecord" items="${reserveRecords}" varStatus="status" >
        <tr <c:if test="${(status.index)%2 != 0}">class="bg"</c:if> >
          <td>${status.index+1}</td>
          <td><fmt:formatDate pattern="yyyy-MM-dd HH:ss" value="${reserveRecord.reserve_time}" /></td>
          <td>&nbsp;</td>
          <c:if test="${reserveRecord.reserve_type==1}"><td>${reserveRecord.quantity}</td><td></td></c:if>
          <c:if test="${reserveRecord.reserve_type==2}"><td></td><td>${reserveRecord.quantity}</td></c:if>
          <td>${reserveRecord.reserve}
	          <c:if test="${reserveRecord.reserve!=null && reserveRecord.ordered!=null && reserveRecord.reserve>reserveRecord.ordered}">
	          	<span style="color: #00cc00;">【${reserveRecord.reserve-reserveRecord.ordered}】</span>
	          </c:if>
	          <c:if test="${reserveRecord.reserve!=null && reserveRecord.ordered!=null &&reserveRecord.reserve<=reserveRecord.ordered}">
	          	<span style="color: #dd0000;">【${reserveRecord.reserve-reserveRecord.ordered}】</span>
	          </c:if>
          </td>
          <td>
          <c:if test="${reserveRecord.reserve_type==1}"><a href="../bill/detailBill.go?id=${reserveRecord.from_id}" title="查看订单" target="_blank" >订单明细</a></c:if>
          <c:if test="${reserveRecord.reserve_type==2}"><a href="../good/detailWarehousing.go?id=${reserveRecord.from_id}" title="查看入库单" target="_blank" >入库单明细</a></c:if>
          </td>
        </tr>
     	</c:forEach>
      </table>
      <div class="pages-box" >
        <div class="pages">
        <c:if test="${pageInfo_reserveRecord.totalGroup > 1}">
         <a href="../good/viewReserveRecords.go?page=${pageInfo_reserveRecord.preGroup}" >&lt;&lt;</a>
        </c:if>
         <a href="../good/viewReserveRecords.go?page=${pageInfo_reserveRecord.pre}" >&lt;</a>
        
         <c:forEach var="index" items="${pageInfo_reserveRecord.indices}" varStatus="status" >
         	<c:if test="${index == pageInfo_reserveRecord.page}"><b>${index}</b></c:if>  
         	<c:if test="${index != pageInfo_reserveRecord.page}"><a href="../good/viewReserveRecords.go?page=${index}">${index}</a></c:if>  
         </c:forEach>
        <a href="../good/viewReserveRecords.go?page=${pageInfo_reserveRecord.next}" >&gt;</a>
        <c:if test="${pageInfo_reserveRecord.totalGroup > 1}">
         <a href="../good/viewReserveRecords.go?page=${pageInfo_reserveRecord.nextGroup}" >&gt;&gt;</a>
        </c:if>
        <input type="text" size="2" title="输入页码按回车" />
        <a href="#" style="color:#000">GO</a> 页数: [ ${pageInfo_reserveRecord.page} / ${pageInfo_reserveRecord.total} ] </div>
      </div>
    
  </div>
  <!-- 列表模块 END -->

</div>
</body>
</html>