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
			$($td[2]).children("select").children("option:eq(0)").attr('selected', 'selected');
			$($td[2]).children("select").attr('name','wgList['+currentIndex+'].good.id');
			$($td[3]).html("");
			$($td[4]).html("");
			$($td[5]).html("");
			$($td[6]).html("");
			$($td[7]).children("input").val(1);
			$($td[7]).children("input").attr('name','wgList['+currentIndex+'].quantity');
			currentIndex++;
			elemClone.insertAfter('#goodsTable tbody>tr:last');
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
			
			var goodIdList = [];
			$("select.goods").each(function(i,selectGood){
				var hasSelect = $(this).val() != -1;
				leage = leage && hasSelect;
				setBackgound($(this).val() == -1,$(this));
				for(var j=0;j<goodIdList.length;j++){
					if(goodIdList[j]==$(this).val()){
						leage = false;
						alert("包含两个相同的商品，请检查是否填错了！");
						return;
					}
				}
				goodIdList[goodIdList.length]=$(this).val();
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
		
		$("button:submit").click(function(event){
			$(window).unbind('beforeunload', notSaved);
			if(!validate()){
				event.preventDefault();
			}
		});
		
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
				  var goodQut = goodSpec.next();
				  goodQut.children("input").val(0);
				return;
			}
				  
			$.ajax({
				  type: 'POST',
				  url: '../good/detailGood.go',
				  data: 'id='+ $that.val(),
				  success: function(data){
					  var goodNum = $that.parent('td').next().html(data['num']);
					  var goodSpec = goodNum.next().html(data['spec']);
					  var goodReserve = goodSpec.next().html(data['reserve']);
					  if(data['reserve']>data['ordered']){
						  goodReserve.html(data['reserve']+"<span style='color: #00cc00;'>【"+(data['reserve']-data['ordered'])+"】</span>");
					  }else{
						  goodReserve.html(data['reserve']+"<span style='color: #dd0000;'>【"+(data['reserve']-data['ordered'])+"】</span>");
					  }
					  var goodOrdered = goodReserve.next().html(data['ordered']);
					  var goodQut = goodOrdered.next();
					  goodQut.children("input").val(1);

				  },
				  dataType: 'json'
				});
			
		});
		
			
	});
	</script>
</head>

<body>
	
    <div class="main" >
        <h2><span>入库单</span></h2>
        <c:if test="${result == -2 }">
        	<div class="mag-t1">错误：${errMsg } </div>
        </c:if>
    	<form action="../good/warehouse.go" id="warehouse" method="post">
        <!-- 表单模块 -->
        <div class="formBox"> 
        <div class="content" >
        		<table class="c4">
	        	<tr>
        		<th>入库时间</th>
            <td><input type="datetime" name='warehouse_date' value='<fmt:formatDate pattern="yyyy-MM-dd HH:ss" value="${w.warehouse_date}" />'></input></td>
            </tr>
            </table>
            </div>
         </div> <!-- 表单模块 END -->    
		
		<div class="listBox" style="border-top:1px solid #fff;"> 
          <h3><span class="fl_l">购买商品</span></h3> 
            <table id="goodsTable"> 
                <tr>
                    <th width='40px'>序号</th>
                    <th width='140px'>商品类目</th>
                    <th width='180px'>商品型号</th>
                    <th width='100px'>商品编号</th>
                    <th width='150px'>规格</th>
                    <th width='80px'>库存【可用】</th>
                    <th width='50px'>待发货</th>
                    <th width='50px'>数量</th>
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
                    <td><select name='wgList[0].good.id' class="select goods" style="width:100%" autoWidth="true" >
                    <option value="-1">请选择</option>
                    </select>
                    </td>      
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td><input type="text" style='width:40px;' name='wgList[0].quantity' value='1' onkeyup="this.value=this.value.replace(/[^-0-9]/g,'')" maxlength="9" class="quantity"/></td>
	                <td><a href="javascript:void();" title="删除" class="delGoodBtn"><img src="../image/zz/ico_cancel.png" alt="删除"  /></a></td>
                </tr>
             	
            </table> 
            <a href="javascript:void();" class="add" id="addGoodBtn"><img src="../image/zz/ico_add.png" alt="新增"  /></a>
          </div>
          
          <!-- 表单模块 -->
           <div class="memo">
        		<table width="100%">
                    <tr>
	                    <td>备注：</td>
                    </tr>
                    <tr>
                        <td valign="top" align="center"><textarea name='memo' cols="" rows="3" style="width: 98%"></textarea></td>
                    </tr>
                </table>
           </div>
         <!-- 表单模块 END --> 
       
    <div class="form-but">
      <!-- 表单按钮区 -->
      <button type="submit" class="button-s4" >提交</button>
      <button type="button" class="button-s4" onclick="location.href='../good/warehousingRecord.go'">取消</button>
    </div>
    </form>
    <!-- 表单按钮区 END -->
    </div>
    
</body>
</html>