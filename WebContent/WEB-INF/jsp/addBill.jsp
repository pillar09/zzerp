<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>中智数码</title>
    <link href="../css/basic.css" rel="stylesheet" type="text/css" />
    <link href="../css/main.css" rel="stylesheet" type="text/css" />
    <link href="../css/tools.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="../js/dateinput/datetimeinput.js"></script>
	<script type="text/javascript" charset="utf-8" src="../js/jquery.tools.ext.js"></script>
	<script type="text/javascript" src="../js/tiny_mce/tiny_mce.js"></script>
	<script type="text/javascript">
		tinyMCE.init({mode : "textareas",theme : "simple", content_css : false});
	</script>	
	<script type="text/javascript">
	$(function(){
		
		//$("input[type='select']").selectinput({speed: 'fast'});
		$("button:submit").attr("disabled", true);
		function notSaved(){ 
			return '您输入的内容尚未保存!'; 
		}
		
		$("input").change(function(){
			$(window).bind('beforeunload',notSaved);
		});
		
		var currentIndex = 1;
		$("#addGoodBtn").click(function(event){
			event.preventDefault();
			var rowCount = $('#goodsTable tbody>tr').length;
			var elemClone = $('#goodsTable tbody>tr:last').clone(true);
			var $td = elemClone.children('td');
			
			$($td[0]).html(currentIndex+1);
		    //$($td[1]).children("select option:eq(0)").attr('selected', 'selected');
		    //jquery的Childen获取的是一个Jquery对象，但是它却有像数组一样的特性，可以通过下标获取，那是因为这个对象添加了字段'1','2','3'等，很让人迷惑，其实它仍是对象，千万不要以为是数组，因为pop，shift都行不通
			$($td[2]).children("select").children("option:eq(0)").attr('selected', 'selected');
			$($td[2]).children("select").attr('name','bgList['+currentIndex+'].good.id');
			$($td[3]).html("");
			$($td[4]).html("");
			$($td[5]).children("input").val(0);
			$($td[5]).children("input").attr('name','bgList['+currentIndex+'].price');
			$($td[6]).children("input").val(0);
			$($td[6]).children("input").attr('name','bgList['+currentIndex+'].quantity');
			$($td[7]).text("");
			$($td[8]).text("");
			currentIndex++;
			elemClone.insertAfter('#goodsTable tbody>tr:last');
		});

		/**
		买家 : Igor Popov  Offline Contact now! Email: ipopovi@gmail.com
		买家 ID: Olya Lazovskaya Offline 邮箱: lazovskaya_olya@mail.ru 
		买家 ID: Sergey Batalov Chat now! 邮箱: sizaus@bk.ru 
		买家 ID: Sergey Batalov Chat now! 邮箱: sizaus@bk.ru Contact now!
		Buyer ID: Singaev Maxim Offline Email: s-maxi@yandex.ru Contact now!
		*/
		$("#clipBuyerPaste").click(function(event){
			event.preventDefault();
			//var a= 'Buyer ID: Singaev Maxim Offline Email: s-maxi@yandex.ru Contact now!'.replace('Online','Offline');
			//var b= '买家 ID: Olya Lazovskaya Offline 邮箱: lazovskaya_olya@mail.ru '.replace('Online','Offline');
			//var c= '买家 : Igor Popov  Offline Contact now! Email: ipopovi@gmail.com'.replace('Online','Offline');
			
			var sArray = window.clipboardData.getData('text').replace('Chat now!','Offline').replace('Online','Offline').split("Offline");
			//var sArray =c.split("Offline");
			if(sArray.length==2){
				if(sArray[0].indexOf(':')>0){
					var buyerId = sArray[0].split(":")[1].replace(/^\s+|\s+$/g, '');
					$("input[name='buyer_wangwang']").val(buyerId);
				}
				if(sArray[1].indexOf(':')>0){
					var mailString = sArray[1].split(":")[1].replace(/^\s+|\s+$/g, '');
					if(mailString.indexOf(' ')>0){
						mailString = mailString.split(" ")[0].replace(/^\s+|\s+$/g, '');
					}
					$("input[name='mail']").val(mailString);
				}
			}
				
		});
		
		/*旧系统复制到剪贴板
Rustam Mugbilov
Ostrovskogo 14-41 
Kaliningradskaya obl., Russian Federation
236029
Kaliningrad
Kaliningradskaya obl.
Russian Federation
+7 4012 584047
		*/
		/**旧系统对应的代码
		$("#clipBoxPaste").click(function(event){
			event.preventDefault();
			var sArray = window.clipboardData.getData('text').split("\r\n");
			$("input[name='consignee']").val(sArray[0]);
			$("input[name='address']").val(sArray[1]);
			$("input[name='address2']").val(sArray[2]);
			$("input[name='zip_code']").val(sArray[3]);
			$("input[name='city']").val(sArray[4]);
			$("input[name='province']").val(sArray[5]);
			$("input[name='country']").val(sArray[6]);
			$("input[name='tel_num']").val(sArray[7]);
			$(window).bind('beforeunload',notSaved);
		});
		*/
/*
收件人 :Sergey
地址:Kosareva 41-1-57
城市:Saransk
省份:Republic of Mordovia
国家:Russian Federation
邮编:430000
手机:89026698349
电话:
传真:
 */
 /**
	$("#clipBoxPaste").click(function(event){
		event.preventDefault();
		var sArray = window.clipboardData.getData('text').split("\n");
		$("input[name='consignee']").val(trimTag(sArray[0]));
		$("input[name='address']").val(trimTag(sArray[1]));
		
		$("input[name='city']").val(trimTag(sArray[2]));
		$("input[name='province']").val(trimTag(sArray[3]));
		$("input[name='country']").val(trimTag(sArray[4]));
		$("input[name='zip_code']").val(trimTag(sArray[5]));
		
		$("input[name='phone_num']").val(trimTag(sArray[6]));
		$("input[name='tel_num']").val(trimTag(sArray[7]));
		$("input[name='fax_num']").val(trimTag(sArray[8]));
		$(window).bind('beforeunload',notSaved);
	});
	**/
	
	
/**
Contact Name: Kamenskaya Anzhela
Address: 469-167
         moskow, zelenograd, Russian Federation
Zip Code: 124498
Tel: 7-915-2025570
Mobile: 7-915-2025570
No: 1112955811
 */
		$("#clipBoxPaste").click(function(event){
			event.preventDefault();
			var sArray = window.clipboardData.getData('text').split("\n");
			$("input[name='consignee']").val(searchFor('Contact Name:',sArray));
			$("input[name='address']").val(searchFor('Address:',sArray));
			$("input[name='zip_code']").val(searchFor('Zip Code:',sArray));
			cityProvinceCountryAddress2(sArray);
			$("input[name='tel_num']").val(searchFor('Tel:',sArray));
			$("input[name='phone_num']").val(searchFor('Mobile:',sArray));
			$("input[name='num']").val(searchFor('No:',sArray));
			var $that = $("input[name='num']");
			$.ajax({
				type: 'POST',
				  url: '../bill/checkBillNum.go',
				  data: 'num='+ $that.val(),
				  dataType: 'json',
				  success: function(data){
					  if(data){
						  $("#errMsg").text("编号重复");
						  $("#errMsg").show();
						  $("button:submit").attr("disabled", true);
					  }else{
						  $("#errMsg").text("").hide();
						  $("button:submit").removeAttr("disabled");
					  }
				  }
			});
			
			$(window).bind('beforeunload',notSaved);
		});
			
		function searchFor(name,sArray){
			for(var i=0;i<sArray.length;i++){
				var text = sArray[i];
				if(text.indexOf(name)>=0){
					var ind = text.indexOf(':');
					return  text.substring(ind+1).replace(/^\s+|\s+$/g, '');
				}
			}
			return "";
		}
		
		function cityProvinceCountryAddress2(sArray){
			for(var i=0;i<sArray.length;i++){
				var text = sArray[i];
				if(text.indexOf('Address:')>=0){
					var j=i+1;
					while(sArray[j].indexOf(':')<0){
						j++;
					}
					if(j>i+2){
						$("input[name='address2']").val(sArray[j-2].replace(/^\s+|\s+$/g, ''));
					}
					var cpc=sArray[j-1].split(',');
					cpc.reverse();
					$("input[name='country']").val(cpc[0].replace(/^\s+|\s+$/g, ''));
					$("input[name='province']").val(cpc[1].replace(/^\s+|\s+$/g, ''));
					$("input[name='city']").val(cpc[2].replace(/^\s+|\s+$/g, ''));
					
				}
			}
		}
	
		function trimTag(text){
			if(text == undefined){
				return;
			}
			var ind = text.indexOf(':');
			return text.substring(ind+1);
		}
	
		
		$("#sameAsConsignee").click(function(event){
			event.preventDefault();
			$("input[name='buyer']").val($("input[name='consignee']").val());
			$("input[name='buyer_tel_num']").val($("input[name='tel_num']").val());
			$("input[name='buyer_phone_num']").val($("input[name='phone_num']").val());
		});
		
		$(".delGoodBtn").click(function(event){
			event.preventDefault();
			var rowCount = $('#goodsTable tbody>tr').length;
			if(rowCount > 2){
				//必须保留一行
				$(this).parent('td').parent('tr').remove();
				amountUp();
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
			leage = validateNoVal(leage,"num");
			leage = validateNoVal(leage,"amount");
			leage = validateNoVal(leage,"country");
			leage = validateNoVal(leage,"consignee");
			leage = validateNoVal(leage,"address");
			leage = validateNoVal(leage,"order_time");
			leage = validateNoVal(leage,"pay_time");
			//leage = validateNoVal(leage,"limit_time");
			//leage = validateNoVal(leage,"expected_arrival");
			//leage = validateNoVal(leage,"amount_declared");
			
			$("select.goods").each(function(i,selectGood){
				var hasSelect = $(this).val() != -1;
				leage = leage && hasSelect;
				setBackgound($(this).val() == -1,$(this));
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
			
			$("input.quantity").each(function(i,inputQtt){
				var noVal = !$(this).val();
				leage = leage && !noVal;
				var notNum = isNaN($(this).val());
				leage = leage && !notNum;
				setBackgound(noVal || notNum,$(this));
			});
			return leage;
		}

		$("select.categorys").change(function(){
			$(window).bind('beforeunload',notSaved);
			var $that = $(this);
			if($that.val()==-1){
				return;
			}
			$.ajax({
				  type: 'POST',
				  url: '../good/listGoods.go',
				  data: 'category_id='+ $that.val(),
				  success: function(data){
					//alert(data);
					  var goods = $that.parent('td').next().children('select');
					  goods.html("<option value='-1'>请选择</option>");
					  $.each(data, function(i,value){
						//console.log(value[3]);
					  	goods.append($("<option value='"+value[0]+"'>"+value[3]+"("+value[7]+")</option>"));
					  });
				  },
				  dataType: 'json'
				});
		});
			
		
		$("select.goods").change(function(){
			$(window).bind('beforeunload',notSaved);
			var $that = $(this);
			if($that.val()==-1){
				  var goodNum = $that.parent('td').next().html("");
				  var goodSpec = goodNum.next().html("");
				  var goodPrice = goodSpec.next();
				  goodPrice.children("input").val("");
				  var goodQut = goodPrice.next();
				  goodQut.children("input").val(0);
				  var goodTotal = goodQut.next().html("");
				  amountUp();
				return;
			}
				  
			$.ajax({
				  type: 'POST',
				  url: '../good/detailGood.go',
				  data: 'id='+ $that.val(),
				  success: function(data){
					  var goodNum = $that.parent('td').next().html(data['num']);
					  var goodSpec = goodNum.next().html(data['spec']);
					  var goodPrice = goodSpec.next();
					  goodPrice.children("input").val(data['price']);
					  var goodQut = goodPrice.next();
					  goodQut.children("input").val(1);
					  var goodReserve = goodQut.next();
					  if(data['reserve']>data['ordered']){
						  goodReserve.html(data['reserve']+"<span style='color: #00cc00;'>【"+(data['reserve']-data['ordered'])+"】</span>");
					  }else{
						  goodReserve.html(data['reserve']+"<span style='color: #dd0000;'>【"+(data['reserve']-data['ordered'])+"】</span>");
					  }
					  var goodTotal = goodReserve.next().html(data['price']);
					  amountUp();
				  },
				  dataType: 'json'
				});
			
		});
		
		function amountUp(){
			var amount = 0;
			$("#goodsTable tbody>tr").each(function(i,row){
				if(i>=1){
					var $td = $(row).children('td');
					//console && console.log($td[7].innerHTML);
					if(!isNaN($td[8].innerHTML)){
						amount += parseFloat($td[8].innerHTML);
					}
				}
			});
			if(!isNaN(amount)){				
				$("input[name='amount']").val(amount);
			}else{
				$("input[name='amount']").val("");
			}
		}
		
		function sumUp(){
			var $td = $(this).parent("td").parent("tr").children('td');
			var price = parseFloat($($td[5]).children("input").val());
			var qut = parseFloat($($td[6]).children("input").val());
			if(!isNaN(price) && !isNaN(qut )){
				$td[8].innerHTML = Math.round(price*1000*qut)/1000;
			}
			amountUp();
		}
		$("input[name='num']").focus(function() { $(this).select(); } );
		$("input[name='num']").focus();
		$("input.price").bind('keyup', sumUp);
		$("input.quantity").bind('keyup', sumUp);
		$("input[name='num']").blur(function(event){
			var $that = $(this);
				
			if($that.val() && $that.val()!="" && $that.val().length !=0){
			
				$.ajax({
					type: 'POST',
					  url: '../bill/checkBillNum.go',
					  data: 'num='+ $that.val(),
					  dataType: 'json',
					  success: function(data){
						  if(data){
							  $("#errMsg").text("编号重复");
							  $("#errMsg").show();
							  $("button:submit").attr("disabled", true);
						  }else{
							  $("#errMsg").text("").hide();
							  $("button:submit").removeAttr("disabled");
						  }
					  }
				});
			}
		});

		$("#BillForecast").click(function(event){
			$("#addBillForm").attr("action","../bill/submitBill.go");
			$(window).unbind('beforeunload', notSaved);
			if(!validate()){
				event.preventDefault();
			}
		});
		
		$("#BillDraft").click(function(event){
			$(window).unbind('beforeunload', notSaved);
		});
		
	});
	</script>
</head>

<body>
	
    <div class="main" style="">
        <h2><span>新增订单</span></h2>
    	<form action="../bill/addBill.go" id="addBillForm" method="post">
        <!-- 表单模块 -->
        <div class="formBox"> 
        	<div class="content" >
        		<table class="c4" style="float:left; " >
	        	<tr>
	        	<th colspan="3" style="text-align: left;">
	        	<h5><span>订单基本信息</span><span><i>创建(${bill.create_user.full_name })</i></span></h5>
	        	</th>
	        	<td>
	        	<span style="color:black;">状态：【新增】</span>
	        	</td>
	        	</tr>
	        	<tr>
	        	<th>销售：</th>
                    <td><select class="select" autoWidth="true" name="salesman.id" >
                      <c:forEach var="account" items="${allAccounts}" varStatus="status" >
                           <option value="${account.id}"  <c:if test="${account.username==currentAccount.username}">selected='selected'</c:if> >${account.full_name}</option>
	                  </c:forEach>
	                  </select></td>
	                  
                	<th>金额($)：</th>
                    <td><input name='amount' value='${bill.amount}' type="text" readonly="readonly"></input></td>
	        	</tr>
            	<tr>
                    <th>订单编号：</th>
                    <td><input name='num' type="text" value='${bill.num }'></input>
                    <span id="errMsg" style="display:none;" class="error"></span></td>
                     <th>店铺号：</th>
                    <td><input type="text" name='store_num' value='${bill.store_num}'></input></td>
                </tr>
                <tr>
                  <th>付款方式：</th>
                  <td><input type="text" name='pay_with' value='${bill.pay_with}' ></input></td>
                  <th>客户要求发货方式：</th>
                  <td><input type="text" name='expect_deliver_with' value='${bill.expect_deliver_with}' ></input></td>
           	    </tr>
           	   <tr>
                <th>申报内容：</th>
                <td><input type="text" name='declaration' value='${bill.declaration}' ></input></td>
                <th>申报金额($)：</th>
                <td><input type="text" name='amount_declared' value='${bill.amount_declared}' onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')"></input></td>
               </tr>
				<tr>
				  <th>订单链接：</th>
                  <td colspan="3"><input type="text" name='link' value='${bill.link}' style="width:99%" ></input></td>
				</tr>   

                <tr>
                   
   				  <th>买家在线ID：</th>
                  <td><input type="text" name='buyer_wangwang' value='${bill.buyer_wangwang}' ></input>&nbsp;<a href="#" id="clipBuyerPaste" tabindex="-1" class="paste"></a></td>
				   <th>邮箱地址：</th>
                  <td><input type="text" name='mail' value='${bill.mail}' ></input></td>
                </tr>
				<tr>
				  <th><a href="#" id="sameAsConsignee" tabindex="-1" class="btn_add">同收货人信息</a>&nbsp;&nbsp;买家姓名：</th>
                  <td><input type="text" name='buyer' value='${bill.buyer}'></input></td>
                  <th>买家电话：</th>
                  <td><input type="text" name='buyer_tel_num' value='${bill.buyer_tel_num}' ></input></td>
				</tr>
				<tr>
				  
                	<th>买家手机：</th>
                    <td><input name='buyer_phone_num' value='${bill.buyer_phone_num}' type="text" ></input></td>
				</tr>
                                   
        
                <tr>
                <th colspan="6" style="text-align: left; width: 100%;">
                  <h5><span>收货信息</span>
                  <a href="#" id="clipBoxPaste" class="paste" tabindex="-1" title="从剪贴板获取收货信息，仅支持IE">粘贴</a>  
                  </h5>
                </th>
                </tr>
                <tr>
                     <th>收货人：</th>
                     <td><input  name='consignee' value='<c:out value="${bill.consignee}" />' type="text" ></input></td>
					 <th>邮编：</th>
                     <td><input  name='zip_code' value='${bill.zip_code}' type="text"></input></td>
                 </tr>
                     
                <tr>
                <th>国家/地区：</th>
                <td><input type="text"  name='country' value='<c:out value="${bill.country}" />' ></input></td>
				<th>电话：</th>
                   <td><input  name='tel_num' type="text" value='${bill.tel_num}' ></input></td>
                 </tr>
                <tr>
                     
				<th>省/州：</th>
                    <td><input  name='province' value='<c:out value="${bill.province}" />' type="text" ></input></td>
					<th>手机：</th>
                    <td><input  name='phone_num' value='<c:out value="${bill.phone_num}" />' type="text" ></input></td>
                 </tr>
                  
                 <tr>
                  <th>城市：</th>
                     <td><input  name='city' value='${bill.city}' type="text"></input></td>
                    <th>传真：</th>
                   <td><input  name='fax_num' value='${bill.fax_num}' type="text" ></input></td>
                  </tr>
                 <tr>
                   <th rowspan="2">详细地址：</th>
                   <td colspan="3"><input  name='address' value='<c:out value="${bill.address}" />' type="text" style="width:99%"></input></td>
                  </tr>
                  <tr>
                   <td colspan="3"><input  name='address2' value='<c:out value="${bill.address2}" />' type="text" style="width:99%"></input></td>
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
                  <h5><span>时间记录</span></h5>
                </th>
                </tr>
				<tr>
                  <th>下单时间：</th>
                  <td><input type="datetime" name='order_time' value='${bill.order_time}'></input></td>
                </tr>
				<tr>
                  <th>付款时间：</th>
                  <td><input type="datetime" name='pay_time' value='${bill.pay_time}'></input></td>
                  </tr>
				<tr>
                  <th>最迟发货期限：</th>
                  <td><input type="datetime" name='limit_time' ></input></td>
               </tr>
			   <tr>
                  <th>预计到货：</th>
                  <td><input type="datetime" name='expected_arrival' ></input></td>
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
                    <th width='140px'>商品类目</th>
                    <th width='180px'>商品型号</th>
                    <th width='100px'>商品编号</th>
                    <th width='150px'>规格</th>
                    <th width='80px'>单价($)</th>
                    <th width='80px'>数量</th>
                    <th width='80px'>库存【可用】</th>
                    <th width='80px'>小计($)</th>
                  	<th width='45px'>删除</th>
                </tr>
                <tr>
                    <td>1</td>
                    <td><select class="select categorys" autoWidth="true" style="width:100%">
                      <option value="-1">请选择</option>               
                      <c:forEach var="category" items="${categorys}" varStatus="status" >
                           <option value="${category.id}">${category.title}</option>
	                  </c:forEach>
	                  </select></td>
                    <td><select name='bgList[0].good.id' class="select goods" style="width:100%" autoWidth="true" >
                    <option value="-1">请选择</option>
                    </select>
                    </td>      
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td><input type="text" style='width:40px;' name='bgList[0].price' value='0' onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" maxlength="9" class="price"/></td>
                    <td><input type="text" style='width:40px;' name='bgList[0].quantity' value='1' onkeyup="this.value=this.value.replace(/[^0-9]/g,'')" maxlength="9" class="quantity"/></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
	                <td><a href="javascript:void();" title="删除" class="delGoodBtn"><img src="../image/zz/ico_cancel.png" alt="删除"  /></a></td>
                </tr>
             	
            </table> 
            <a href="javascript:void();" class="add" id="addGoodBtn"><img src="../image/zz/ico_add.png" alt="新增"  /></a>
          </div>
          
          <!-- 表单模块 -->
           <div class="memo">
        		<table width="100%">
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
      <button type="submit" class="button-s4" id="BillDraft" disabled="disabled">存为草稿</button>
      <button type="submit" class="button-s4" id="BillForecast" disabled="disabled">提交</button>
      <button type="button" class="button-s4" onclick="location.href='../bill/manageBills.go'">取消</button>
    </div>
    </form>
    <!-- 表单按钮区 END -->
    </div>
    
</body>
</html>