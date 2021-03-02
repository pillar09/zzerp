<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
	<script type="text/javascript">
	$(function(){
		$("a.bt_ren").click(function(event){
			if(!confirm("确定要还原？")){
				event.preventDefault();
			}
		});
		$("a.bt_del").click(function(event){
			if(!confirm("确定要彻底删除？删除后将不可恢复！")){
				event.preventDefault();
			}
		});
	});
	</script>
</head>
<body>
	
    <div class="main" style="width: 1024px">

        <div class="tool">
            <span><a href="../bill/recycleBill.go?id=${bill.id}" hidefocus="true" class="bt_ren">还原</a></span>
            <span><a href="../bill/deleteBill.go?id=${bill.id}" hidefocus="true" class="bt_del">彻底删除</a></span>
        </div>

        
        <!-- 表单模块 -->
        <div class="formBox"> 
        	<div class="content" style="padding:0; width:1020px">
        		<table class="c4" style="float:left; width:630px; margin-right: 10px; ">
	        	<tr>
	        	<th colspan="4" style="text-align: left; width: 100%;">
	        	<h5><span>订单基本信息</span><span><i>创建(${bill.create_user.full_name })</i></span>
	        	<span><i> 编辑(${bill.last_edit_user.full_name })</i></span>
	        	<c:if test="${bill.consignor.full_name!=null }">
	        	<span><i> 发货(${bill.consignor.full_name })</i></span>
	        	</c:if>
	        	</h5>
	        	</th>
	        	</tr>
            	<tr>
                    <th>订单编号：</th>
                    <td>${bill.num }</td>
                    <th>订单状态：</th>
                    <td>
                    <c:if test="${bill.state ==1 }"><span style="color: blue;">【草稿】</span></c:if>
       			 	<c:if test="${bill.state ==101 }"><span style="color: blue;">【预报】</span></c:if>
   			 		<c:if test="${bill.state ==102 }"><span style="color: blue;">【备货】</span></c:if>
                    <c:if test="${bill.state ==2 }"><span style="color: red;">【待发货】</span></c:if>
                    <c:if test="${bill.state ==3 }"><span style="color:#00dd00">【已发货】</span></c:if>
                    <c:if test="${bill.state ==9 }"><span style="color: black;">【已签收】</span></c:if>
                    </td>
                </tr>
                <tr>
                	<th>店铺号：</th>
                    <td>${bill.store_num}</td>
                	<th>金额($)：</th>
                    <td>${bill.amount }</td>
                </tr>

                <tr>
                   <th>销售：</th>
                    <td>${bill.salesman.full_name}</td>
   				  <th>买家在线ID：</th>
                  <td>${bill.buyer_wangwang}</td>
                </tr>
                <tr>
                <th>付款方式：</th>
                  <td>${bill.pay_with}</td>
                
                  <th>客户要求发货方式：</th>
                  <td>${bill.expect_deliver_with}</td>
                  
           	    </tr>
				<tr>
				  <th>买家姓名：</th>
                  <td>${bill.buyer}</td>
                  <th>买家电话：</th>
                  <td>${bill.buyer_phone_num}</td>
				 </tr>
				<tr>
				   <th>邮箱地址：</th>
                  <td>${bill.mail}</td>
 				  <th>买家电话：</th>
                  <td>${bill.buyer_tel_num}</td>
				</tr>
             <%--  <tr>
                <th>申报内容：</th>
                <td>${bill.declaration}</td>
                <th>申报金额($)：</th>
                <td>${bill.amount_declared}</td>
             </tr> --%>
				<tr>
				  <th>订单链接：</th>
                  <td colspan="3"><a href="${bill.link}" title="打开网店链接">${bill.link}</a></td>
				</tr>

                <tr>
                <th colspan="4" style="text-align: left; width: 100%;">
                  <h5><span>收货信息</span></h5>
                </th>
                </tr>
                <tr>
                     <th>收货人：</th>
                     <td>${bill.consignee}</td>
                      <th>邮编：</th>
                     <td>${bill.zip_code}</td>

                 </tr>
                 
                <tr>
                    <th>国家/地区：</th>
                    <td>${bill.country}</td>
                    <th>电话：</th>
                   <td>${bill.tel_num}</td>
                </tr>
                <tr>
                     <th>省/州：</th>
                     <td>${bill.province}</td>
					 <th>手机：</th>
                     <td>${bill.phone_num}</td>
                 </tr>
                 
                 <tr>
                    <th>城市：</th>
                    <td>${bill.city}</td>
                    <th>传真：</th>
                    <td>${bill.fax_num}</td>
                  </tr>
                  <tr>
                  <th rowspan="2">详细地址：</th>
                   <td colspan="3">${bill.address}</td>
                   </tr>
                  <tr>
                   <td colspan="3">${bill.address2}</td>
                  </tr>
                  <tr>
                   <td colspan="3"></td>
                   <th></th>
                   <td></td>
                  </tr>
            </table>
            <table class="c4" style="float:left; ">
               <tr>
                <th colspan="2" style="text-align: left; width: 100%;">
                  <h5><span>时间记录</span>
                  </h5>
                </th>
                </tr>
                <tr>
                  <th>下单时间：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.order_time}" /></td>
                </tr>
                <tr>
                  <th>付款时间：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.pay_time}" /></td>
               </tr>
               <tr>
                  <th>最迟发货期限：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.limit_time}" /></td>
               </tr>
				<tr>
                  <th>预计到货：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.expected_arrival}" /></td>
               </tr>
               <c:if test="${bill.deliver_time!=null}">
                <tr>
                  <th>发货时间：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.deliver_time}" /></td>
               </tr>
               </c:if>
               <c:if test="${bill.accept_time!=null}">
               <tr>
                  <th>签收时间：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.accept_time}" /></td>
           	    </tr>
           	    </c:if>
           	 <c:if test="${bill.state>=2}">
			<tr>
                <th colspan="2" style="text-align: left; width: 100%;">
                  <h5><span>发货信息</span></h5>
                </th>
            </tr>
                <tr>
                <th>发货方式：</th>
                <td>${bill.deliver_with}</td>
            </tr>
             <tr>
                <th>快递代理：</th>
                  <td>${bill.logistics}</td>
		     </tr>
              <tr>
                <th>物流单号：</th>
                  <td>${bill.waybill_num}</td>
              </tr>
              <tr>
                 <th>运费(￥)：</th>
                 <td>${bill.freight}</td>
			  </tr>
             </c:if>
            </table>
            <div class="clear"></div>
        	</div>
            
         </div> <!-- 表单模块 END -->    
		
		<div class="listBox" style="margin-top:10px;"> 
          <h3><span class="fl_l">购买商品</span></h3> 
            <table id="goodsTable"> 
                <tr>
                    <th width='50px'>序号</th>
                    <th width='150px'>商品类目</th>
                    <th width='150px'>商品型号</th>
                    <th width='150px'>商品编号</th>
                    <th width='150px'>规格</th>
                    <th width='80px'>单价($)</th>
                    <th width='50px'>数量</th>
                    <th width='40px'>库存</th>
                </tr>
                <c:forEach var="bg" items="${bill.bgList}" varStatus="bgStatus" >
                <tr>
                    <td>${(bgStatus.index)+1}</td>
                    <td>${goodMap[fn:trim(bg.good.id)].category.title}</td>
                    <td>${goodMap[fn:trim(bg.good.id)].model}</td>      
                    <td>${goodMap[fn:trim(bg.good.id)].num}</td>
                    <td>${goodMap[fn:trim(bg.good.id)].spec}</td>
                    <td>$${bg.price}</td>
                    <td>${bg.quantity}</td>
                    <td>${goodMap[fn:trim(bg.good.id)].reserve}</td>
                </tr>
             	</c:forEach>
            </table> 
          </div>
          
          <!-- 表单模块 -->
        <div class="formBox"> 
            
            <h4><span>其他信息:</span></h4>
        	<div class="content">
        		<table class="c6">
                    <tr>
                        <th>销售留言：</th>
                        <td><p>${bill.salesman_note}</p></td>
                    </tr>
                </table>
        	</div>
            
            
            
         </div> <!-- 表单模块 END --> 
       
    <div class="form-but">
      <!-- 表单按钮区 -->

      <button type="button" class="button-s4" onclick="location.href='../bill/recycleBills.go'">返回</button>
    </div>
    <!-- 表单按钮区 END -->
    </div>
    
</body>
</html>