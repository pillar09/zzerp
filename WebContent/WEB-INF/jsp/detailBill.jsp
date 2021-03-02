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
   	<script type="text/javascript" src="../js/tiny_mce/tiny_mce.js"></script>
	<script type="text/javascript">
		tinyMCE.init({mode : "textareas",theme : "simple"});
	</script>	
	<script type="text/javascript">
	$(function(){
		$("#BillAccepted").click(function(event){
			if(!confirm("确定已经签收？")){
				event.preventDefault();
			}else{
				location.href='../bill/acceptBill.go?id=${bill.id}';
			}
		});
		$("#BillSubmited").click(function(event){
			if(!confirm("该订单是否已填写正确？确定提交订单？")){
				event.preventDefault();
			}else{
				location.href='../bill/submitBill.go?id=${bill.id}';
			}
		});
		$("#del_bill").click(function(event){
			if(!confirm("确定要删除？")){
				event.preventDefault();
			}
		});
		
		$("#solve_issue").click(function(event){
			if(!confirm("确定已经解决纠纷了吗？")){
				event.preventDefault();
			}
		});
	});
	</script>
</head>
<body>
	
    <div class="main">
   <div style="float:right;"><img src="<c:url value='/image/code.jpg' />" width="100px" height="100px" /></div>
        <div class="tool">
        	 <c:if test="${(fn:startsWith(bill.state,'1') || fn:startsWith(bill.state,'2'))&& (currentUsername==bill.create_user.username || currentUsername == bill.salesman.username)}">
            <span><a href="../bill/toEditBill.go?id=${bill.id}" hidefocus="true" class="bt_edit">编辑</a></span>
            </c:if>
            
            <span><a href="../bill/toDeliverGoods.go?id=${bill.id}" hidefocus="true" class="bt_all">发货信息</a></span>
            <span><a href="../bill/toDeclare.go?id=${bill.id}" hidefocus="true" class="bt_all">申报信息</a></span>
            
            <span><a href="../bill/duplicateBill.go?id=${bill.id}" hidefocus="true" class="bt_add">新增订单(补货/换货)</a></span>
            <c:if test="${fn:startsWith(bill.state,'1') || fn:startsWith(bill.state,'2') }">
            <span><a href="../bill/recycleBill.go?id=${bill.id}" hidefocus="true" class="bt_del" id="del_bill">删除</a></span>
            </c:if>
            <c:if test="${bill.issue==null || bill.issue==0}">
            <span><a href="../bill/issueBill.go?id=${bill.id}" hidefocus="true" class="bt_del">纠纷</a></span>
            </c:if>
            <c:if test="${bill.issue==1 }">
            <span><a href="../bill/solveIssue.go?id=${bill.id}" hidefocus="true" class="bt_all" id="solve_issue">解决纠纷</a></span>
            </c:if>
            <span><a href="../bill/exportToXls.go?id=${bill.id}" hidefocus="true" class="bt_all">导出</a></span>
        </div>
       
        <form action="../bill/noteBill.go" id = "BillForm" method="post">
        <input type="hidden" name='id' value="${bill.id}"/>
        <!-- 表单模块 -->
        <div class="formBox"> 
        	<div class="content" style="padding:0;">
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
                  <th>买家手机：</th>
                  <td>${bill.buyer_phone_num}</td>
				 </tr>
				<tr>
				   <th>邮箱地址：</th>
                  <td>${bill.mail}</td>
 				  <th>买家电话：</th>
                  <td>${bill.buyer_tel_num}</td>
				</tr>
<%--               <tr>
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
                <th colspan="4" style="text-align: left; width: 50%;">
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
               <c:if test="${bill.submit_time!=null}">
                <tr>
                  <th>提交时间：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.submit_time}" /></td>
               </tr>
               </c:if>
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
			<tr>
                <th colspan="2" style="text-align: left; width: 100%;">
                  <h5><span>发货信息</span>
                  <c:if test="${bill.waybill_changed }">
		        	<span class='warning'>订单已被重新修改！</span>
	        	  </c:if>
                  </h5>
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
            </table>
            <div class="clear"></div>
        	</div>
            
         </div> <!-- 表单模块 END -->    
		
		<div class="listBox" style="border-top:1px solid #fff;"> 
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
                    <c:choose>
                    <c:when test="${bill.state < 3 }">
                    <th width='65px'>库存【可用】</th>
                    </c:when>
                    <c:otherwise>
                    <th width='65px'>发货后</th>
                    </c:otherwise>
                    </c:choose>
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
                    <c:choose>
                    <c:when test="${bill.state < 3 }">
                    <td>${goodMap[fn:trim(bg.good.id)].reserve}
			          <c:if test="${goodMap[fn:trim(bg.good.id)].reserve>goodMap[fn:trim(bg.good.id)].ordered}">
			          	<span style="color: #00cc00;">【${goodMap[fn:trim(bg.good.id)].reserve-goodMap[fn:trim(bg.good.id)].ordered}】</span>
			          </c:if>
			          <c:if test="${goodMap[fn:trim(bg.good.id)].reserve<=goodMap[fn:trim(bg.good.id)].ordered}">
			          	<span style="color: #dd0000;">【${goodMap[fn:trim(bg.good.id)].reserve-goodMap[fn:trim(bg.good.id)].ordered}】</span>
			          </c:if>
                    </td>
                    </c:when>
                    <c:otherwise>
                    <td>${bg.reserve}
			          <c:if test="${bg.reserve!=null && bg.ordered!=null && bg.reserve>bg.ordered}">
			          	<span style="color: #00cc00;">【${bg.reserve-bg.ordered}】</span>
			          </c:if>
			          <c:if test="${bg.reserve!=null && bg.ordered!=null && bg.reserve<=bg.ordered}">
			          	<span style="color: #dd0000;">【${bg.reserve-bg.ordered}】</span>
			          </c:if>
                    </td>
                    </c:otherwise>
                    </c:choose>

                </tr>
             	</c:forEach>
            </table> 
          </div>
          
         <c:if test="${fn:length(bill.declareList)>0 }">
		<div class="listBox" style="border-top:1px solid #fff;"> 
          <h3><span class="fl_l">申报信息</span></h3> 
            <table id="declareTable"> 
                <tr>
                    <th width='40px'>序号</th>
                    <th width='90px'>中文品名</th>
                    <th width='90px'>英文品名</th>
                    <th width='50px'>重量(g)</th>
                    <th width='60px'>单价</th>
                    <th width='80px'>数量</th>
                    <th width='60px'>小计</th>
                    <th width='50px'>币种</th>
                    <th width='50px'>编码</th>
                </tr>
                <c:forEach var="declare" items="${bill.declareList}" varStatus="declareStatus" >
                <tr>
                    <td>${(declare.key)+1}</td>
                    <td>${declare.value.declaration}</td>
                    <td>${declare.value.declaration_en}</td>
                    <td>${declare.value.weight}</td>
                    <td>${declare.value.price}</td>
                    <td>${declare.value.quantity}</td>
                    <td>${declare.value.total}</td>
                    <td>${declare.value.currency}</td>
                    <td>${declare.value.code}</td>
                </tr>
             	</c:forEach>
             	
            <tfoot>
               <tr>
            <td></td>
                <td></td>
                <td></td>
                <td>${bill.weight_declared}</td>
                <td></td>
                <td>合计($)：</td>
                <td>${bill.amount_declared}</td>
                <td></td>
                <td></td>
             </tr>
             </tfoot>
            </table> 
          </div>
          </c:if>
          
          
       <c:if test="${fn:length(bill.pkList)>0 }">
  		<div class="listBox" style="border-top:1px solid #fff;"> 
          <h3><span class="fl_l">包裹</span></h3> 
            <table id="packsTable"> 
                <tr>
                    <th width='50px'>序号</th>
                    <th width='65px'>状态</th>
                    <th width='130px'>长 X 宽 X 高(cm)</th>
                    <th width='80px'>体积系数</th>
                    <th width='80px'>体积重量</th>
                    <th width='80px'>重量(kg)</th>
                    <th width='60px'>发货方式</th>
                    <th width='60px'>快递代理</th>
                    <th width='80px'>条码</th>
                    <!-- 
                    <th width='100px'>物流单</th>
                     -->
                    <th width='60px'>运费</th>
                </tr>
                <c:forEach var="pk" items="${bill.pkList}" varStatus="pkStatus" >
                <tr>
                    <td>${(pk.key)+1}</td>
                    <td><c:if test="${pk.value.state ==3 }"><span style="color:#00dd00">【已发】</span></c:if></td>
                    <td>${pk.value.length} X ${pk.value.width} X ${pk.value.height}</td>
                    <td>${pk.value.volume_rate}</td>
                    <td>${pk.value.volume_weight}</td>
                    <td>${pk.value.weight}</td>
                    <td>${pk.value.deliver_with}</td>
                    <td>${pk.value.logistics}</td>
                    <td>${pk.value.barcode}</td>
                    <!-- 
                    <td>${pk.value.waybill_num}</td>
                     -->
                    <td>${pk.value.freight}</td>
                </tr>
             	</c:forEach>
            </table> 
          </div>
      	</c:if>
          <!-- 表单模块 -->
           <div class="memo">
        		<table width="100%">
                    <c:if test="${bill.buyer_note != null && fn:length(bill.buyer_note) > 0}">
                    <tr>
	                    <td >客户留言：</td>
                    </tr>
                    <tr>
                        <td valign="top"><div>${bill.buyer_note}</div></td>
                    </tr>
                    </c:if>
                    <tr>
	                    <td>销售留言：</td>
                    </tr>
                    <tr>
                        <td valign="top"><div>${bill.salesman_note}</div></td>
                    </tr>
                    <tr>
	                    <td>留言：</td>
                    </tr>
                    <tr>
                        <td valign="top" align="center"><textarea name='salesman_note' cols="" rows="3" style="width: 98%"></textarea></td>
                    </tr>
                </table>
           </div>
            
          <!-- 表单模块 END --> 
       
    <div class="form-but">
      <button type="submit" class="button-s4" >留言</button>
      <!-- 表单按钮区 -->
      <c:if test="${fn:startsWith(bill.state,'3') }">
      <button type="button" class="button-s4" id="BillAccepted">确认签收</button>
      </c:if>
      <c:if test="${bill.state == 1 }">
      <button type="button" class="button-s4"  id="BillSubmited">提交</button>
      </c:if>
      <button type="button" class="button-s4" onclick="location.href='../bill/manageBills.go'">返回</button>
    </div>
    </form>
    <!-- 表单按钮区 END -->
    </div>
    
</body>
</html>