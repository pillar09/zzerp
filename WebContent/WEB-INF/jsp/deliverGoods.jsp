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
    <title>发货信息</title>
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
		var pkIndex = ${fn:length(bill.pkList)};
		$("#addPackBtn").click(function(event){
			event.preventDefault();//防止页面跳动
			//var rowCount = $('#goodsTable tbody>tr').length;
			var elemClone = $('#packsTable tbody>tr:last').clone(true);
			var $td = elemClone.children('td');
			
			$($td[0]).html(pkIndex+1);
			$($td[1]).children("input").val("");
			var td1 = $($td[1]).children("input");
			$(td1[0]).attr('name','pkList['+pkIndex+'].length');
			$(td1[1]).attr('name','pkList['+pkIndex+'].width');
			$(td1[2]).attr('name','pkList['+pkIndex+'].height');
			$($td[2]).children("input").val("5000");
			$($td[2]).children("input").attr('name','pkList['+pkIndex+'].volume_rate');
			$($td[3]).html("");
			$($td[4]).children("input").val("");
			$($td[4]).children("input").attr('name','pkList['+pkIndex+'].weight');
			
			//$($td[5]).children("input").val("");
			$($td[5]).children("input").attr('name','pkList['+pkIndex+'].deliver_with');
			//$($td[6]).children("input").val("");
			$($td[6]).children("input").attr('name','pkList['+pkIndex+'].logistics');
			
			$($td[7]).children("input").val("");
			$($td[7]).children("input").attr('name','pkList['+pkIndex+'].barcode');
			
			//$($td[8]).children("input").val("");
			//$($td[8]).children("input").attr('name','pkList['+pkIndex+'].waybill_num');
			
			$($td[8]).children("input").val("");
			$($td[8]).children("input").attr('name','pkList['+pkIndex+'].freight');
			pkIndex++;
			elemClone.insertAfter('#packsTable tbody>tr:last');
			elemClone.children("input.decimal").each(function(i,v){
				$(this).bind('keyup', onlyDecimal);			
			});
			elemClone.children("input.integer").each(function(i,v){
				$(this).bind('keyup', onlyInteger);			
			});
		});
		
		$(".delPackBtn").click(function(event){
			event.preventDefault();//防止链接跳动
			var rowCount = $('#packsTable tbody>tr').length;
			if(rowCount > 2){
				//必须保留一行
				$(this).parent('td').parent('tr').remove();
			}
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
			//leage = validateNoVal(leage,"waybill_num");
			//leage = validateNoVal(leage,"logistics");
			//leage = validateNoVal(leage,"freight");
			//leage = validateNoVal(leage,"deliver_with");
			//leage = validateNoVal(leage,"deliver_time");
			
			$("input.package").each(function(i,inputV){
				var noVal = !$(this).val();
				var val = $(this).val();
				leage = leage && !noVal;
				var notNum = isNaN($(this).val());
				leage = leage && !notNum;
				setBackgound(noVal || notNum,$(this));
			});
			
			return leage;
		}
		
		function onlyDecimal(e){
			if(e && e.keyCode != 9){
				this.value=this.value.replace(/[^0-9.]/g,'');
			}
		}
		
		function onlyInteger(e){
			if(e && e.keyCode != 9){
				this.value=this.value.replace(/[^0-9]/g,'');
			}
		}
		
		
		$("input.decimal").each(function(i,v){
			$(this).bind('keyup', onlyDecimal);			
		});
		$("input.integer").each(function(i,v){
			$(this).bind('keyup', onlyInteger);			
		});
		$("input.package").focus(function() { $(this).select(); } );
		$("input.package").first().focus();
		
		$("#btnDeliver").click(function(event){
			if(!validate()){
				event.preventDefault();
				//location = '#';
				return;
			}
			if(!confirm("发货信息是否已填写正确？确定已经发货？")){
				event.preventDefault();
			}
			$('form').attr('action','../bill/deliver.go');
		});
		
		$("#btnSave").click(function(event){
			$('form').attr('action','../bill/fillWaybill.go');
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
                                      
      <%--         <tr>
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
               <c:if test="${bill.submit_time!=null}">
                <tr>
                  <th>提交时间：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.submit_time}" /></td>
               </tr>
               </c:if>
               <c:if test="${bill.deliver_time!=null}">
                <tr>
                  <th>发货时间：</th>
                  <td><input type="datetime" name='deliver_time' value="${bill.deliver_time}" ></input></td>
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
                <td><input type="text" name='deliver_with' value="${bill.deliver_with}" disabled="disabled"></input></td>
            </tr>
             <tr>
                <th>快递代理：</th>
                  <td><input type="text"  name='logistics'  value="${bill.logistics}" disabled="disabled"></input></td>
		     </tr>
              <tr>
                <th>物流单号：</th>
                  <td><input type="text"  name='waybill_num' value="${bill.waybill_num}" disabled="disabled"></input></td>
              </tr>
              <tr>
                 <th>运费(￥)：</th>
                 <td><input type="text"  name='freight'  value="${bill.freight}" disabled="disabled" ></input></td>
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
          <h3><span class="fl_l">包裹</span></h3> 
            <table id="packsTable"> 
                <tr>
                    <th width='40px'>序号</th>
                    <th width='145px'>长 X 宽 X 高(cm)</th>
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
                  	<th width='45px'>删除</th>
                </tr>
                <c:forEach var="pk" items="${bill.pkList}" varStatus="pkStatus" >
                <tr>
                    <td>${(pk.key)+1}<input type="hidden" name='pkList[${pk.key}].id' value='${pk.value.id }'/></td>
                    <td>
                    <input type="text" style='width:40px;' name='pkList[${pk.key}].length' value='${pk.value.length}' maxlength="9" class="package decimal"/>
                    <input type="text" style='width:40px;' name='pkList[${pk.key}].width' value='${pk.value.width}' maxlength="9" class="package decimal"/>
                    <input type="text" style='width:40px;' name='pkList[${pk.key}].height' value='${pk.value.height}' maxlength="9" class="package decimal"/>
                    </td>
                    <td><input type="text" style='width:40px;' name='pkList[${pk.key}].volume_rate' value='${pk.value.volume_rate}' maxlength="9" class="package integer"/></td>
                    <td>${pk.value.volume_weight}</td>
                    <td><input type="text" style='width:40px;' name='pkList[${pk.key}].weight' value='${pk.value.weight}' maxlength="9" class="package decimal"/></td>
                    <td><input type="text" style='width:80px;' name='pkList[${pk.key}].deliver_with' value='${pk.value.deliver_with}' class="deliver_with"/></td>
                    <td><input type="text" style='width:80px;' name='pkList[${pk.key}].logistics' value='${pk.value.logistics}' class="logistics"/></td>
                    <td><input type="text" name='pkList[${pk.key}].barcode' value='${pk.value.barcode}' class="barcode"/></td>
                    <!-- 
                    <td><input type="text" name='pkList[${pk.key}].waybill_num' value='${pk.value.waybill_num}' class="waybill_num"/></td>
                     -->
                    <td><input type="text" style='width:40px;' name='pkList[${pk.key}].freight' value='${pk.value.freight}' maxlength="5" class="freight decimal"/></td>
	                <td><a href="javascript:void();" title="删除" class="delPackBtn"><img src="../image/zz/ico_cancel.png" alt="删除"  /></a></td>
                </tr>
             	</c:forEach>
            </table> 
            <a href="javascript:void();" class="add" id="addPackBtn"><img src="../image/zz/ico_add.png" alt="新增"  /></a>
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
      <c:if test="${bill.state ==2 }"><button type="submit" id="btnDeliver" class="button-s4" >完成发货</button></c:if>
      <button type="button" class="button-s4" onclick="location.href='../bill/manageBills.go'">取消</button>
    </div>
    </form>
    <!-- 表单按钮区 END -->
    </div>
    
</body>
</html>