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
	$("a.recycle").click(function(event){
		if(!confirm("确定要还原吗？")){
			event.preventDefault();
		}
	});
	$("a.delete").click(function(event){
		if(!confirm("删除后将不可恢复，确定要删除？")){
			event.preventDefault();
		}
	});
	
	$("#btnCleanForm").click(function(event){
		$('input[name="filter.num"]').val("");
		$('select[name="filter.salesman.id"]').val(-1);
		$('input[name="conditionMap[\'order_time_start\']"]').val("");
		$('input[name="conditionMap[\'order_time_end\']"]').val("");
	});
});
</script>
</head>
<body>
<div class="main">
  <!-- 搜索模块 -->
  <form action="../bill/recycleBills.go" method="post">
  <div class="searchBox">
    <h3><span>订单列表</span></h3>
    <table>
      <tr>
        <th>订单编号：</th>
        <td><input type="text" name="filter.num" value='${pageInfo_recycleBills.filter.num }' class="inp" /></td>
        <th> 销售：</th>
        <td><select class="select" autoWidth="true" name="filter.salesman.id" style="width:100%">
              <option value="-1" >- 请选择 -</option>
              <c:forEach var="account" items="${allAccounts}" varStatus="status" >
                   <option value="${account.id}"  <c:if test="${account.id==pageInfo_recycleBills.filter.salesman.id}">selected='selected'</c:if>>${account.full_name}</option>
           </c:forEach>
        </select></td>
        <th>下单时间：</th>
        <td>
        <input type="date" name="conditionMap['order_time_start']" class="inp" value="${pageInfo_recycleBills.conditionMap['order_time_start']}"/>
        ~&nbsp;<input type="date" name="conditionMap['order_time_end']" class="inp" value="${pageInfo_recycleBills.conditionMap['order_time_end']}"/>
        </td>

        <th><button type="submit" class="btn">搜 索</button></th>
        <td><button type="submit" class="btn" id="btnCleanForm">清空</button></td>
      </tr>
    </table>
  </div>
   </form>
  <!-- 搜索模块 END -->
  <div class="clear"></div>
  <!-- 列表模块 -->
  <div class="listBox">
      <h3><span>已删除订单列表</span></h3>
      <table >
        <tr>
        <th></th>
          <th>订单号</th>
          <th>收货人</th>
          <th>销售</th>
          <th>金额($)</th>
          <th>付款方式</th>
          <th>发货方式</th>
          <th>下单时间</th>
          <th>操作</th>
        </tr>

        <c:forEach var="bill" items="${bills}" varStatus="status" >
        <tr <c:if test="${(status.index)%2 != 0}">class="bg"</c:if> >
          <td><input type="checkbox" value='${bill.id}'/></td>
          <td>${bill.num}</td>
          <td>${bill.consignee}</td>
          <td>${bill.create_user.full_name}</td>
          <td>${bill.amount}</td>
          <td>${bill.pay_with}</td>
          <td>${bill.deliver_with}</td>
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${bill.order_time}"/></td>
          <td>
          <a href="../bill/detailDeletedBill.go?id=${bill.id}" title="详情" >详情</a> | 
          <a class='delete' href="../bill/deleteBill.go?id=${bill.id}" title="删除后不可恢复，包括相关包裹信息" >彻底删除</a>
          </td>
        </tr>
     	</c:forEach>
      </table>
      <div class="pages-box" >
        <div class="pages">
         <a href="#" style="font-weight:bold">&laquo;</a>
        
         <c:forEach var="index" items="${pageInfo_recycleBills.indices}" varStatus="status" >
         	<c:if test="${index == pageInfo_recycleBills.page}"><b>${index}</b></c:if>  
         	<c:if test="${index != pageInfo_recycleBills.page}"><a href="../bill/recycleBills.go?page=${index}">${index}</a></c:if>  
         </c:forEach>
        <a href="#" style="font-weight:bold">&raquo;</a>
        <input type="text" size="2" title="输入页码按回车" />
        <a href="#" style="color:#000">GO</a> 页数: [ ${pageInfo_recycleBills.page} / ${pageInfo_recycleBills.total} ] </div>
      </div>
    
  </div>
  <!-- 列表模块 END -->

</div>
</body>
</html>