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
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
$(function(){
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
});
</script>
</head>
<body>
<div class="main">
  <!-- 搜索模块 -->
  <div class="searchBox">
    <h3><span>订单导入</span></h3>
    <table>

  <form action="../bill/importFromXlsNew.go" method="post"  enctype="multipart/form-data">
  	  <tr>
        <th>速卖通新格式xls：</th>
        <td><input type="file" name='file' class="inp inp-w300" /></td>
        <td><button type="submit" class="btn">导 入</button></td>
      </tr>
  </form>
   <form action="../bill/importPackFromXls.go" method="post" enctype="multipart/form-data">
      <tr>
        <th>物流信息xls：</th>
        <td><input type="file" name='file' class="inp inp-w300" /></td>
        <td><button type="submit" class="btn">导 入</button></td>
        </tr>
  </form>
    </table>
  </div>
  <!-- 搜索模块 END -->
  <div class="clear"></div>
  <!-- 列表模块 -->
  <div class="listBox">
      <h3><span>已导入订单列表</span></h3>
      <table >
        <tr>
        <th></th>
          <th>订单号</th>
          <th>收货人</th>
          <th>销售</th>
          <th>金额($)</th>
          <th>下单时间</th>
          <th>付款时间</th>
          <th>要求发货方式</th>
          <th>导入成功</th>
          <th>操作</th>
        </tr>

        <c:forEach var="bill" items="${bills}" varStatus="status" >
        <tr <c:if test="${(status.index)%2 != 0}">class="bg"</c:if> >
          <td><input type="checkbox" value='${bill.id}'/></td>
          <td>${bill.num}</td>
          <td>${bill.consignee}</td>
          <td>${bill.salesman.full_name}</td>
          <td>${bill.amount}</td>
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${bill.order_time}"/></td>
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${bill.pay_time}"/></td>
          <td>${bill.expect_deliver_with}</td>
          <td><c:if test="${bill.state==0 }"><span style="color: red;">【已存在】</span></c:if><c:if test="${bill.state==1 }"><span style="color: blue;">【成功】</span></c:if></td>
          <td>
          <a href="../bill/detailBill.go?id=${bill.id}" title="详情" target="_blank" >详情</a>
          </td>
        </tr>
     	</c:forEach>
      </table>
    
  </div>
  <!-- 列表模块 END -->

</div>
</body>
</html>