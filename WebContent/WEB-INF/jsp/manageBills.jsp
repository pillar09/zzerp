<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="zz" uri="http://www.zzerp.com/dev/jsp/jstl/zz" %>
<sec:authentication property="principal.username" var="currentUsername" scope="page" />
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
	$("input[name='filter.num']").focus(function() { $(this).select(); } );
	$("input[name='filter.num']").focus();
	$("input[type='date']").dateinput({
		  format: 'yyyy-mm-dd',
		  speed: 'fast',
		  lang:'cn'});
	
	$("a.accept").click(function(event){
		if(!confirm("确定已经签收？")){
			event.preventDefault();
		}
	});
	$("a.delete").click(function(event){
		if(!confirm("确定要删除？")){
			event.preventDefault();
		}
	});
	
	$('#checkAll').click(function(event){
		if(this.checked){
			$('input[name="id"]').prop('checked', true);
		}else{
			$('input[name="id"]').prop('checked', false);
		}
			
	});
	
	$("#btnCleanForm").click(function(event){
		event.preventDefault();
		$('input[name="filter.num"]').val("");
		$('input[name="filter.consignee"]').val("");
		$('input[name="filter.waybill_num"]').val("");
		$('input[name="filter.buyer"]').val("");
		$('input[name="filter.buyer_tel_num"]').val("");
		$('input[name="filter.mail"]').val("");
		$('select[name="filter.salesman.id"]').val(-1);
		$('input[name="conditionMap[\'order_time_start\']"]').val("");
		$('input[name="conditionMap[\'order_time_end\']"]').val("");
		$('input[name="conditionMap[\'submit_time_start\']"]').val("");
		$('input[name="conditionMap[\'submit_time_end\']"]').val("");
		$('input[name="conditionMap[\'pay_time_start\']"]').val("");
		$('input[name="conditionMap[\'pay_time_end\']"]').val("");
		$('input[name="conditionMap[\'deliver_time_start\']"]').val("");
		$('input[name="conditionMap[\'deliver_time_end\']"]').val("");
	});
});
function subExport(event){
	if(0==$('input[name="id"]:checked').length){
		event.preventDefault();
	}
}
</script>
</head>
<body>
<div class="main">
  <!-- 搜索模块 -->
  <form action="../bill/searchBills.go" method="post">
  <div class="searchBox">
    <h3><span>订单列表</span></h3>
    <table>
      <tr>
        <th>订单编号：</th>
        <td><input type="text" class="inp" name="filter.num" value="${pageInfo.filter.num }" /></td>
        
        <th>下单时间：</th>
        <td>
        <input type="date" name="conditionMap['order_time_start']" class="inp" value="${pageInfo.conditionMap['order_time_start']}"/>
        ~&nbsp;<input type="date" name="conditionMap['order_time_end']" class="inp" value="${pageInfo.conditionMap['order_time_end']}"/>
        </td>
        
        <th>买家ID：</th>
  		<td><input type="text" class="inp" name="filter.buyer" value="${pageInfo.filter.buyer }" /></td>
      </tr>
      <tr>
      <th> 销售：</th>
        <td><select class="select" autoWidth="true" name="filter.salesman.id" style="width:100%">
              <option value="-1" >- 请选择 -</option>
              <c:forEach var="account" items="${allAccounts}" varStatus="status" >
                   <option value="${account.id}"  <c:if test="${account.id==pageInfo.filter.salesman.id}">selected='selected'</c:if>>${account.full_name}</option>
           </c:forEach>
        </select></td>
        <th>提交时间：</th>
        <td>
        <input type="date" name="conditionMap['submit_time_start']" class="inp" value="${pageInfo.conditionMap['submit_time_start']}"/>
        ~&nbsp;<input type="date" name="conditionMap['submit_time_end']" class="inp" value="${pageInfo.conditionMap['submit_time_end']}"/>
        </td>
        <th>买家电话：</th>
  		<td><input type="text" class="inp" name="filter.buyer_tel_num" value="${pageInfo.filter.buyer_tel_num }" /></td>
        </tr>
      <tr>
          <th>收货人：</th>
        <td><input type="text" class="inp" name="filter.consignee" value="${pageInfo.filter.consignee }"/></td>
        
        <th>付款时间：</th>
        <td>
        <input type="date" name="conditionMap['pay_time_start']" class="inp"  value="${pageInfo.conditionMap['pay_time_start']}" />
     	~&nbsp;<input type="date" name="conditionMap['pay_time_end']" class="inp" value="${pageInfo.conditionMap['pay_time_end']}"/>
        </td>
        <th>邮箱地址：</th>
  		<td><input type="text" class="inp" name="filter.mail" value="${pageInfo.filter.mail }" /></td>
        
       </tr>
       <tr>
             <th>物流单号：</th>
        <td><input type="text" class="inp" name="filter.waybill_num" value="${pageInfo.filter.waybill_num }" /></td>
        
        <th>发货时间：</th>
        <td>
        <input type="date" name="conditionMap['deliver_time_start']" class="inp" value="${pageInfo.conditionMap['deliver_time_start']}"/>
        ~&nbsp;<input type="date" name="conditionMap['deliver_time_end']" class="inp" value="${pageInfo.conditionMap['deliver_time_end']}"/>
        </td>
        <th><button type="submit" class="btn">搜 索</button></th>
        <td><button type="submit" class="btn" id="btnCleanForm">清空</button></td>
        <td><button type="button" class="btn" onclick="location.href='../bill/importBills.go'">导入订单</button></td>
      </tr>
    </table>
  </div>
  </form>
  <!-- 搜索模块 END -->
  <!-- 列表模块 -->
  <ul class="form-tab">
    <li <c:if test="${state==0 && issue==null }" >class="c"</c:if>><span><a href="../bill/manageBills.go?state=0&page=1">全部订单 [${allCount}]</a></span></li>
    <li <c:if test="${state==1 }" >class="c"</c:if>><span><a href="../bill/manageBills.go?state=1&page=1">草稿 [${draftCount}]</a></span></li>
    <li <c:if test="${state==102 }" >class="c"</c:if>><span><a href="../bill/manageBills.go?state=102&page=1">备货 [${preparingCount}]</a></span></li>
    <li <c:if test="${state==2 }" >class="c"</c:if>><span><a href="../bill/manageBills.go?state=2&page=1">待发货 [${readyCount}]</a></span></li>
    <li <c:if test="${state==3 }" >class="c"</c:if>><span><a href="../bill/manageBills.go?state=3&page=1">已发货 [${sendCount}]</a></span></li>
    <li <c:if test="${state==9 }" >class="c"</c:if>><span><a href="../bill/manageBills.go?state=9&page=1">已签收 [${acceptCount}]</a></span></li>
    <li <c:if test="${state==0 && issue==1 }" >class="c"</c:if>><span><a href="../bill/manageBills.go?state=0&page=1&issue=1">纠纷 [${issueCount}]</a></span></li>
  </ul>
  <form action="../bill/exportToXls.go" onsubmit="subExport(event)">

  <ul class="page-size" >
	 <li>
	  <c:if test="${10 == pageInfo.pageSize}"><b>10</b></c:if> 
	  <c:if test="${10 != pageInfo.pageSize}">
	 	<a href="../bill/manageBills.go?page=1&pageSize=10">10</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${15 == pageInfo.pageSize}"><b>15</b></c:if> 
	  <c:if test="${15 != pageInfo.pageSize}">
	 	<a href="../bill/manageBills.go?page=1&pageSize=15">15</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${20 == pageInfo.pageSize}"><b>20</b></c:if> 
	  <c:if test="${20 != pageInfo.pageSize}">
	 	<a href="../bill/manageBills.go?page=1&pageSize=20">20</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${30 == pageInfo.pageSize}"><b>30</b></c:if> 
	  <c:if test="${30 != pageInfo.pageSize}">
	 	<a href="../bill/manageBills.go?page=1&pageSize=30">30</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${50 == pageInfo.pageSize}"><b>50</b></c:if> 
	  <c:if test="${50 != pageInfo.pageSize}">
	 	<a href="../bill/manageBills.go?page=1&pageSize=50">50</a>
 	  </c:if> 
 	 </li>
	 <li>
	  <c:if test="${100 == pageInfo.pageSize}"><b>100</b></c:if> 
	  <c:if test="${100 != pageInfo.pageSize}">
	 	<a href="../bill/manageBills.go?page=1&pageSize=100">100</a>
 	  </c:if> 
 	 </li>
  </ul>
  <ul class="page-size" >
	<li>
		<button type="submit" value='导出'>导出</button>
	 </li>
	
  </ul>
  <div class="listBox">
      <h3><span>订单列表 （${pageInfo.count}）</span></h3>
      <table >
        <tr>
        <th><input type="checkbox" id='checkAll' value='${bill.id}' class='billid'/></th>
          <th>订单号</th>
          <th>商品类目</th>
          <th>收货人</th>
          <th>销售</th>
          <th>金额($)</th>
          <c:if test="${state<=2||state==102 }" >
          <th>发货要求</th>
          </c:if>
          <c:if test="${state==3||state==9 }" >
          <th>发货方式</th>
          </c:if>
          <th>国家</th>
          <c:if test="${state==1 }" >
          <th>付款时间</th>
          </c:if>
          <c:if test="${state==2 || state==3 }" >
          <th>发货时间</th>
          <th>代理</th>
          </c:if>
          <c:if test="${state==2 || state==102 }" >
          <th>物流单</th>
          </c:if>
          <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_COST')">
          <c:if test="${state==3 || state==9 }" >
          <th>运费(￥)</th>
          </c:if>
          </sec:authorize>
          <c:if test="${state==9 }" >
          <th>签收时间</th>
          </c:if>
          <c:if test="${state==0 }" >
          <th>状态</th>
          </c:if>
          <th>操作</th>
        </tr>

        <c:forEach var="bill" items="${bills}" varStatus="status" >
        <tr <c:if test="${(status.index)%2 != 0}">class="bg"</c:if> >
          <td><input type="checkbox" name='id' value='${bill.id}' class='billid'/></td>
          <td>
          <%--
          <c:if test="${bill.link!=null && fn:length(bill.link)!=0 }" >
          <a href="${bill.link}" title="打开网店链接" target="_blank">${bill.num}</a>
          </c:if>
          --%>
          <a href="../bill/detailBill.go?id=${bill.id}&state=${bill.state}" >${bill.num}</a>
          </td>
          <td><span style="<c:if test="${bill.waybill_changed}">color: red</c:if>">${bill.title}</span>
          </td>
          <td>${bill.consignee}</td>
          <td>${bill.salesman.full_name}</td>
          <td>${bill.amount}</td>
          <c:if test="${state<=2||state==102 }" >
          <td>${bill.expect_deliver_with}</td>
          </c:if>
          
          <c:if test="${state==3||state==9 }" >
          <td>${bill.deliver_with}</td>
          </c:if>
          <td>${bill.country}</td>
          <c:if test="${state==1 }" >
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${bill.pay_time}" /></td>
          </c:if>
          <c:if test="${state==2 || state==3 }" >
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${bill.deliver_time}" /></td>
          <td>${bill.logistics}</td>
          </c:if>
          <c:if test="${state==2 || state==102 }" >
          <td>${bill.waybill_num}</td>
          </c:if>
          <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_COST')">
          <c:if test="${state==3 || state==9 }" >
          <td>${bill.freight}</td>
          </c:if>
          </sec:authorize>
          <c:if test="${state==9 }" >
          <td><fmt:formatDate pattern="yyyy-MM-dd" value="${bill.accept_time}" /></td>
          </c:if>
          <c:if test="${state==0 }" >
          <td>
   			 <c:if test="${bill.state ==1 }">草稿</c:if>
             <c:if test="${bill.state ==2 }">待发货</c:if>
             <c:if test="${bill.state ==3 }">已发货</c:if>
             <c:if test="${bill.state ==9 }">已签收</c:if>
          </td>
          </c:if>
          <td>
          <a href="../bill/detailBill.go?id=${bill.id}&state=${bill.state}" title="查看详情并进行后续操作" >详情</a>
          <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
          &nbsp;|&nbsp;
          <a href="../bill/toReviseBill.go?id=${bill.id}" title="错误修正">修改</a>
          </sec:authorize>
          </td>
        </tr>
     	</c:forEach>
      </table>
      <div class="pages-box" >
        <div class="pages">
        <c:if test="${pageInfo.totalGroup > 1}">
         <a href="../bill/manageBills.go?page=${pageInfo.preGroup}" >&lt;&lt;</a>
        </c:if>
         <a href="../bill/manageBills.go?page=${pageInfo.pre}" >&lt;</a>
        
         <c:forEach var="index" items="${pageInfo.indices}" varStatus="status" >
         	<c:if test="${index == pageInfo.page}"><b>${index}</b></c:if>  
         	<c:if test="${index != pageInfo.page}"><a href="../bill/manageBills.go?page=${index}">${index}</a></c:if>  
         </c:forEach>
        <a href="../bill/manageBills.go?page=${pageInfo.next}" >&gt;</a>
        <c:if test="${pageInfo.totalGroup > 1}">
         <a href="../bill/manageBills.go?page=${pageInfo.nextGroup}" >&gt;&gt;</a>
        </c:if>
        <input type="text" size="2" title="输入页码按回车" />
        <a href="#" style="color:#000">GO</a> 页数: [ ${pageInfo.page} / ${pageInfo.total} ] </div>
      </div>
    
  </div>
  
</form>
  <!-- 列表模块 END -->
</div>
</body>
</html>