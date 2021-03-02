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
          <!-- <th>状态</th> -->
          <th>运单号</th>
          <th>实际运费（RMB）</th>
          <th>发货时间</th>
          <th>长X宽X高（CM)</th>
          <th>重量（Kg）</th>
          <th>导入成功</th>
          <th>操作</th>
        </tr>

        <c:forEach var="pack" items="${packs}" varStatus="s" >
        <tr <c:if test="${(s.index)%2 != 0}">class="bg"</c:if> >
          <td><input type="checkbox" value='${pack.id}'/></td>
          
          <td>${pack.bill_num}</td>
         <%--  <td><c:if test="${pack.state==0 }"><span style="color: green;">【新增】</span></c:if><c:if test="${pack.state==1 }"><span style="color: blue;">【更新】</span></c:if></td> --%>
          <td>${pack.barcode}</td>
          <td>${pack.freight}</td>
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${pack.deliver_time}"/></td>
          <td>${pack.length}X${pack.width }X${pack.height}</td>
          <td>${pack.weight}</td>
          <td>${pack.status}</td>
          <td>
          <c:if test="${pack.bill_id!=null }">
          <a href="../bill/detailBill.go?id=${pack.bill_id}" title="详情" target="_blank" >详情</a>
          </c:if>
          </td>
        </tr>
     	</c:forEach>
      </table>
    
  </div>
  <!-- 列表模块 END -->

</div>
</body>
</html>