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
<link href="../css/tools.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/layer.js"></script>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script>
	$(function(){
		var div_a =$(".form-tab li");
		div_a.hover(function(){
			$(".form-tab li").removeClass("c");
			$(this).addClass("c");
			var index =  div_a.index(this); 
			$(".listBox li")
					.eq(index).show() 
					.siblings().hide();
		});

		$("a.delete").click(function(event){
			if(!confirm("确定要删除？")){
				event.preventDefault();
			}
		});
		
		$("#btnCleanForm").click(function(event){
			$('input[name="num"]').val("");
			$('input[name="model"]').val("");
			$('select[name="category.id"]').val(-1);
		});
	});
</script>
</head>
<body>
<div class="main">
  <!-- 搜索模块 -->
  <form action="../good/searchGoods.go" method="post">
  <div class="searchBox">
    <h3><span>搜索商品</span></h3>
    <table>
      <tr>
        <th>商品编号：</th>
        <td><input type="text" name="num" class="inp" value="${pageInfo_good.filter.num}"/></td>
        <th>主机型号：</th>
        <td><input type="text" name="model" class="inp" value="${pageInfo_good.filter.model}"/></td>
        <th>商品类目：</th>
        <td><select name="category.id" class="select" >
        	<option value="-1"> -- 请选择 -- </option>
             <c:forEach var="category" items="${categorys}" varStatus="status" >
               <option value="${category.id}" <c:if test="${pageInfo_good.filter.category.id == category.id}">selected='selected'</c:if> >${category.title}</option>
            </c:forEach>
      	</select></td>
        <th>&nbsp;</th>
        <td><button type="submit" class="btn">搜 索</button></td>
        <td><button type="submit" class="btn" id="btnCleanForm">清空</button></td>
      </tr>
    </table>
  </div>
  </form>
  <!-- 搜索模块 END -->
  
  <c:if test="${result == -1 }">
  <div class="mag-t1"> ${errMsg }</div>
  </c:if>
  <div class="clear"></div>
 <ul class="page-size" >
	 <li>
	  <c:if test="${10 == pageInfo_good.pageSize}"><b>10</b></c:if> 
	  <c:if test="${10 != pageInfo_good.pageSize}">
	 	<a href="../good/manageGoods.go?page=1&pageSize=10">10</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${15 == pageInfo_good.pageSize}"><b>15</b></c:if> 
	  <c:if test="${15 != pageInfo_good.pageSize}">
	 	<a href="../good/manageGoods.go?page=1&pageSize=15">15</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${20 == pageInfo_good.pageSize}"><b>20</b></c:if> 
	  <c:if test="${20 != pageInfo_good.pageSize}">
	 	<a href="../good/manageGoods.go?page=1&pageSize=20">20</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${30 == pageInfo_good.pageSize}"><b>30</b></c:if> 
	  <c:if test="${30 != pageInfo_good.pageSize}">
	 	<a href="../good/manageGoods.go?page=1&pageSize=30">30</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${50 == pageInfo_good.pageSize}"><b>50</b></c:if> 
	  <c:if test="${50 != pageInfo_good.pageSize}">
	 	<a href="../good/manageGoods.go?page=1&pageSize=50">50</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${100 == pageInfo_good.pageSize}"><b>100</b></c:if> 
	  <c:if test="${100 != pageInfo_good.pageSize}">
	 	<a href="../good/manageGoods.go?page=1&pageSize=100">100</a>
 	  </c:if> 
 	 </li>

  </ul>
  <div class="listBox">
      <h3><span>商品列表 （${pageInfo_good.count}）</span></h3>
      <table id="mytable">
        <tr>
          <th>&nbsp;</th>
          <th>商品类目</th>
          <th>商品编号</th>
          <th>主机型号</th>
          <th>规格</th>
          <th>库存【可用】</th>
          <th>待发货</th>
          <th>总销量</th>
          <th>销售价格($)</th>
          <th>操作</th>
        </tr>
        <c:forEach var="good" items="${goods}" varStatus="status" >
        <tr <c:if test="${(status.index)%2 != 0}">class="bg"</c:if> >
          <td><input type="checkbox" value='${good.id}'/></td>
          <td>${good.category.title}</td>
          <td>${good.num}</td>
          <td>${good.model}</td>
          <td>${good.spec}</td>
          <td>${good.reserve} 
          <c:if test="${good.reserve>good.ordered}">
          	<span style="color: #00cc00;">【${good.reserve-good.ordered}】</span>
          </c:if>
          <c:if test="${good.reserve<=good.ordered}">
          	<span style="color: #dd0000;">【${good.reserve-good.ordered}】</span>
          </c:if>
          </td>
          <td>${good.ordered}</td>
          <td>${good.total_sold}</td>
          <td>${good.price}</td>
          <td><a href="../good/toEditGood.go?id=${good.id}" title="编辑">编辑</a>&nbsp;|&nbsp;
          <a href="../good/viewReserveRecords.go?id=${good.id}" title="进销记录" target="_blank">进销</a>&nbsp;|&nbsp;
          <a href="../good/deleteGood.go?id=${good.id}" title="删除" class="delete">删除</a></td>
        </tr>
     	</c:forEach>
        
      </table>
      <div class="pages-box" >
        <div class="pages">
        <c:if test="${pageInfo_good.totalGroup > 1}">
         <a href="../good/manageGoods.go?page=${pageInfo_good.preGroup}" >&lt;&lt;</a>
        </c:if>
         <a href="../good/manageGoods.go?page=${pageInfo_good.pre}" >&lt;</a>
         <c:forEach var="index" items="${pageInfo_good.indices}" varStatus="status" >
         	<c:if test="${index == pageInfo_good.page}"><b>${index}</b></c:if>  
         	<c:if test="${index != pageInfo_good.page}"><a href="../good/manageGoods.go?page=${index}">${index}</a></c:if>  
         </c:forEach>
        <a href="../good/manageGoods.go?page=${pageInfo_good.next}" >&gt;</a>
        <c:if test="${pageInfo_good.totalGroup > 1}">
         <a href="../good/manageGoods.go?page=${pageInfo_good.nextGroup}" >&gt;&gt;</a>
        </c:if>
        <input type="text" size="2" title="输入页码按回车" />
        <a href="#" style="color:#000">GO</a> 页数: [ ${pageInfo_good.page} / ${pageInfo_good.total} ] </div>
      </div>
   
   
  </div>
</div>
</body>
</html>