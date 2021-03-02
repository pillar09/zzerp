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
    <title>申报信息</title>
    <link href="../css/basic.css" rel="stylesheet" type="text/css" />
    <link href="../css/main.css" rel="stylesheet" type="text/css" />
    <link href="../css/tools.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="../js/dateinput/datetimeinput.js"></script>
	<script type="text/javascript" charset="utf-8" src="../js/jquery.tools.ext.js"></script>
   	<script type="text/javascript" src="../js/tiny_mce/tiny_mce.js"></script>
	<script type="text/javascript">
		tinyMCE.init({mode : "textareas",theme : "simple"});
	</script>	
	<script type="text/javascript" >
	$(function(){
		var declareIndex = ${fn:length(bill.declareList)};
		$("#addDeclareBtn").click(function(event){
			event.preventDefault();//防止页面跳动
			//var rowCount = $('#goodsTable tbody>tr').length;
			var elemClone = $('#declareTable tbody>tr:first').clone(true);
			var $td = elemClone.children('td');
			
			$($td[0]).html(declareIndex+1);
			$($td[1]).children("input").val("");
			$($td[1]).children("input").attr('name','declareList['+declareIndex+'].declaration');
			$($td[2]).children("input").val("");
			$($td[2]).children("input").attr('name','declareList['+declareIndex+'].declaration_en');
			$($td[3]).children("input").val("0");
			$($td[3]).children("input").attr('name','declareList['+declareIndex+'].weight');
			$($td[3]).children("input").bind('keyup blur', weightUp);
			$($td[4]).children("input").val("0.0");
			$($td[4]).children("input").attr('name','declareList['+declareIndex+'].price');
			$($td[4]).children("input").bind('keyup blur', sumUp);
			$($td[5]).children("input").val("0");
			$($td[5]).children("input").attr('name','declareList['+declareIndex+'].quantity');
			$($td[5]).children("input").bind('keyup blur', sumUp);
			$($td[6]).html("0");
			//$($td[7]).children("input").val("");
			$($td[7]).children("input").attr('name','declareList['+declareIndex+'].currency');
			$($td[8]).children("input").val("0");
			$($td[8]).children("input").attr('name','declareList['+declareIndex+'].code');
			elemClone.insertAfter('#declareTable tbody>tr:last');
			declareIndex++;
		});
		
		$(".delDeclareBtn").click(function(event){
			event.preventDefault();//防止链接跳动
			var rowCount = $('#declareTable tbody>tr').length;
			if(rowCount > 1){
				//必须保留一行
				$(this).parent('td').parent('tr').remove();
			}
			amountUp();
			weightUp();
		});
		function setBackgound(illeage,$obj){
			if(illeage){
				$obj.css("background","#fee");
			}else{
				$obj.css("background","");
			}
		}
		
		function validateNoVal(leage,name){
			var $obj = $("input[name='"+name+"']");
			var noVal = !$obj.val();
			setBackgound(noVal,$obj);
			return leage && !noVal;
		}

		function validate(){
			var leage = true;
			$("input.declaration").each(function(i,inputPrice){
				var noVal = !$(this).val();
				leage = leage && !noVal;
				setBackgound(noVal,$(this));
			});
			$("input.price").each(function(i,inputPrice){
				var noVal = !$(this).val();
				var val = $(this).val();
				//console && console.log($(this).val());
				leage = leage && !noVal;
				var notNum = isNaN($(this).val());
				leage = leage && !notNum;
				setBackgound(noVal || notNum,$(this));
			});
			
			$("input.weight").each(function(i,inputQtt){
				var noVal = !$(this).val();
				leage = leage && !noVal;
				var notNum = isNaN($(this).val());
				leage = leage && !notNum;
				setBackgound(noVal || notNum,$(this));
			});
			
			$("input.quantity").each(function(i,inputQtt){
				var noVal = !$(this).val();
				leage = leage && !noVal;
				var notNum = isNaN($(this).val());
				leage = leage && !notNum;
				setBackgound(noVal || notNum,$(this));
			});
			return leage;
		}
		
		function weightUp(e){
			if(e && e.type == 'keyup' && e.keyCode != 9){
				this.value=this.value.replace(/[^0-9]/g,'');
			}
			var weight = 0;
			$("#declareTable tbody>tr").each(function(i,row){
				
					var $td = $(row).children('td');
					weight += parseInt($($td[3]).children("input").val());
			});
			if(!isNaN(weight)){				
				$("input[name='weight_declared']").val(weight);
			}else{
				$("input[name='weight_declared']").val("");
			}
		}

		function amountUp(){
			var amount = 0;
			$("#declareTable tbody>tr").each(function(i,row){
				
					var $td = $(row).children('td');
					if(!isNaN($td[6].innerHTML)){
						amount += parseFloat($td[6].innerHTML);
					}
				
			});
			amount = Math.round(amount*1000)/1000;
			if(!isNaN(amount)){				
				$("input[name='amount_declared']").val(amount);
			}else{
				$("input[name='amount_declared']").val("");
			}
		}
		
		function sumUp(e){
			if(e && e.type == 'keyup' && e.keyCode != 9){
				if($(this).hasClass('price')){
					this.value=this.value.replace(/[^0-9.]/g,'');
				}
				if($(this).hasClass('quantity')){
					this.value=this.value.replace(/[^0-9]/g,'');
				}
			}
			var $td = $(this).parent("td").parent("tr").children('td');
			var price = parseFloat($($td[4]).children("input").val());
			var qut = parseFloat($($td[5]).children("input").val());
			if(!isNaN(price) && !isNaN(qut )){
				$td[6].innerHTML = Math.round(price*1000*qut)/1000;
			}
			amountUp();
		}
		$("input.declaration").focus(function() { $(this).select(); } );
		$("input.declaration").first().focus();
		$("input.weight").bind('keyup blur', weightUp);
		$("input.price").bind('keyup blur', sumUp);
		$("input.quantity").bind('keyup blur', sumUp);

		$("#btnSave").click(function(event){
			if(!validate()){
				event.preventDefault();
			}
			$('form').attr('action','../bill/declare.go');
		});
	});
	</script>
</head>

<body>
	
    <div class="main" >
    	<div class="tool">
       	<span><a href="../bill/detailBill.go?id=${bill.id}" hidefocus="true" class="bt_par">返回查看</a></span>
       	</div>
    	<form action="" method="post">
    	<input type="hidden" name='id' value="${bill.id}"/>
        <!-- 表单模块 -->
        <div class="formBox"> 
        	<div class="content">
        		<table class="c4" style="float:left; width:630px; margin-right: 10px; ">
	        	<tr>
	        	<th colspan="4" style="text-align: left; width: 100%;">
	        	<h5><span>订单基本信息</span><span><i>创建(${bill.create_user.full_name })</i></span><span><i> 编辑(${bill.last_edit_user.full_name })</i></span></h5>
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
                    <th width='80px'>数量</th>
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
                </tr>
             	</c:forEach>
            </table> 
          </div>
          
		<div class="listBox" style="border-top:1px solid #fff;"> 
          <h3><span class="fl_l">申报信息</span></h3> 
            <table id="declareTable"> 
            <thead>
                <tr>
                    <th width='40px'>序号</th>
                    <th width='100px'>中文品名</th>
                    <th width='100px'>英文品名</th>
                    <th width='50px'>重量(g)</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th width='50px'>小计</th>
                    <th>币种</th>
                    <th>编码</th>
                    <th>删除</th>
                </tr>
                </thead>
                <c:forEach var="declare" items="${bill.declareList}" varStatus="declareStatus" >
                <tr>
                    <td>${(declare.key)+1}<input type="hidden" name='declareList[${declare.key}].declaration_id' value='${declare.value.declaration_id }'/></td>
                    <td><input type="text" name='declareList[${declare.key}].declaration' value='${declare.value.declaration}' class="declaration"/></td>
                    <td><input type="text" name='declareList[${declare.key}].declaration_en' value='${declare.value.declaration_en}' class="declaration"/></td>
                    <td><input type="text" style='width:40px;' name='declareList[${declare.key}].weight' value='${declare.value.weight}' maxlength="5" class="weight"/></td>
                    <td><input type="text" style='width:60px;' name='declareList[${declare.key}].price' value='${declare.value.price}'  maxlength="9" class="price"/></td>
                    <td><input type="text" style='width:40px;' name='declareList[${declare.key}].quantity' value='${declare.value.quantity}' maxlength="9" class="quantity"/></td>
                    <td width='50px'>${declare.value.total}</td>
                    <td><input type="text" style='width:50px;' name='declareList[${declare.key}].currency' value='${declare.value.currency}' maxlength="5"/></td>
                    <td><input type="text" name='declareList[${declare.key}].code' value='${declare.value.code}' class="code"/></td>
                    <td><a href="javascript:void();" title="删除" class="delDeclareBtn"><img src="../image/zz/ico_cancel.png" alt="删除"  /></a></td>
                </tr>
             	</c:forEach>
             	                                      

            <tfoot>
               <tr>
            <td><a href="javascript:void();" class="add" id="addDeclareBtn"><img src="../image/zz/ico_add.png" alt="新增"  /></a></td>
                <td colspan="1"></td>
                <td></td>
                <td><input name='weight_declared' type="text" style='width:40px;' readonly="readonly" value='${bill.weight_declared}'></input></td>
                <td></td>
                <td>合计($)：</td>
                <td width='50px'><input name='amount_declared' style='width:40px;' type="text" readonly="readonly" value='${bill.amount_declared}'></input></td>
                <td></td>
                <td></td>
                <td></td>
             </tr>
             </tfoot>
            </table>
          </div>
          
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
      <!-- 表单按钮区 -->
      <button type="submit" id="btnSave" class="button-s4" >保存</button>
      <button type="button" class="button-s4" onclick="location.href='../bill/manageBills.go'">取消</button>
    </div>
    </form>
    <!-- 表单按钮区 END -->
    </div>
</body>
</html>