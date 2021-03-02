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
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
	tinyMCE.init({mode : "textareas",theme : "simple"});
</script>	
<script type="text/javascript" >
$(function(){
	//$("#scan").keyup(function(event){
		
	$("#scan").bind("propertychange keyup input paste", function(event){
		//alert("dd");
		
		$that = $(this);
		
		$.ajax({
			  type: 'POST',
			  url: '../bill/checkPack.go',
			  data: 'barcode='+ $that.val(),
			  success: function(data){
				  if(data){
					  var elemClone = $('#scantable tbody>tr:last').clone(true);
					  var $td = elemClone.children('td');
					  $($td[0]).html($that.val());
					  $($td[1]).html("<input type='hidden' name='barcode' value='"+$that.val()+"'></input>" );
					  elemClone.insertAfter('#scantable tbody>tr:last');
					$that.val("");
				  }
				 
			  },
			  dataType: 'json'
			});
		
		
	});
	
});
</script>
</head>
<body>
<div class="main">
  <c:if test="${result == 0 }">
  <div class="mag-t1"> 确认发货失败！</div>
  </c:if>
  <c:if test="${result >= 1 }">
  <div class="mag-t2"> 确认发货成功${result }个包裹！ </div>
  </c:if>
  <form action="../bill/deliverPacks.go" method="post">
  <div class="formBox">
    <h3><i></i><span>扫描确认发货</span></h3>
    <div class="content">
   		<table class="c4" id="scantable">
        	<tr>
        		<th>条码</th>
            	<td><input type="text" id="scan"  value=''></input></td>
            </tr>
            <tr>
        		<th></th>
            	<td></td>
            	<td></td>
            </tr>
         </table>
    </div>
    <div class="form-but">
      <button type="submit" class="button-s4">确认</button>
      <button type="reset" class="button-s4">清空</button>
      <button type="button" class="button-s4" onclick="location.href='../good/manageGoods.go';">返回</button>
    </div>
  </div>
  </form>
</div>
</body>
</html>