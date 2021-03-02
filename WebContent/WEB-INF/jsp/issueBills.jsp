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
	$("a.issue").click(function(event){
		if(!confirm("确定纠纷已经解决了吗？")){
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
    <h3><span>订单列表</span></h3>
    <table>
      <tr>
        <th>订单编号：</th>
        <td><input type="text" class="inp" /></td>
        <th> 销售：</th>
        <td><input type="text" class="inp" /></td>
        <th>订单状态：</th>
        <td><select class="select">
            <option>请选择</option>
          </select></td>
      </tr>
      <tr>
        <th>收货人：</th>
        <td><input type="text" class="inp" /></td>
        <th>物流单号：</th>
        <td><input type="text" class="inp" /></td>
        <th>支付方式：</th>
        <td><select class="select">
          <option>请选择</option>
        </select></td>
      </tr>
      <tr>
        <th>下单时间：</th>
        <td colspan="2"><input type="date" class="inp inp-w90" />
          -
          <input type="date" class="inp inp-w90" /></td>
        <td>&nbsp;</td>
        <th></th>
        <td></td>

        <th><button type="submit" class="btn">搜 索</button></th>
        <td><button type="submit" class="btn">导 出</button></td>
      </tr>
    </table>
  </div>
  <!-- 搜索模块 END -->
  <div class="clear"></div>
  <!-- 列表模块 -->
  <div class="listBox">
      <h3><span>有纠纷的订单列表</span></h3>
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
          <a href="../bill/detailBill.go?id=${bill.id}" title="详情" >详情</a>
          </td>
        </tr>
     	</c:forEach>
      </table>
      <div class="pages-box" >
        <div class="pages">
         <a href="#" style="font-weight:bold">&laquo;</a>
        
         <c:forEach var="index" items="${pageInfo_issueBills.indices}" varStatus="status" >
         	<c:if test="${index == pageInfo_issueBills.page}"><b>${index}</b></c:if>  
         	<c:if test="${index != pageInfo_issueBills.page}"><a href="../bill/issueBills.go?page=${index}">${index}</a></c:if>  
         </c:forEach>
        <a href="#" style="font-weight:bold">&raquo;</a>
        <input type="text" size="2" title="输入页码按回车" />
        <a href="#" style="color:#000">GO</a> 页数: [ ${pageInfo_issueBills.page} / ${pageInfo_issueBills.total} ] </div>
      </div>
    
  </div>
  <!-- 列表模块 END -->

</div>
</body>
</html>