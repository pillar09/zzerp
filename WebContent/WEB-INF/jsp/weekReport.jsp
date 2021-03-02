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
	
	function setBackgound(illeage,$obj){
		if(illeage){
			$obj.css("background-color","#fee");
		}else{
			$obj.css("background-color","#fff");
		}
	}
	
	function validate(){
		var leage = true;
		
		var noSelect = $("select[name='category.id']").val() == -1;
		leage = leage && !noSelect;
		setBackgound(noSelect,$("select[name='category.id']"));
		
		var valPrice = $("input[name='price']").val();
		var noValPrice = !valPrice;
		leage = leage && !noValPrice;
		var notNum = isNaN(valPrice);
		leage = leage && !notNum;
		if(noValPrice || notNum){
			$("#spInputNum").show();
		}else{
			$("#spInputNum").hide();
		}
		
		return leage;
	}
	
	
	$("button:submit").click(function(event){
		if(!validate()){
			event.preventDefault();
		}
	});
});
</script>
</head>
<body>
<div class="main">
  <c:if test="${result == 0 }">
  <div class="mag-t1"> 添加失败！${errMsg }</div>
  </c:if>
  <c:if test="${result == 1 }">
  <div class="mag-t2"> 添加成功！ </div>
  </c:if>
  <form action="../good/addGood.go" method="post">
  <div class="formBox">
    <h3><i></i><span>填写商品信息</span></h3>
    <div class="content">
      <div class="form">
        <ol>
            <li>
            <label>商品类目：</label>
            <div class="con">
            <ul>
              <li>
                <select name="category.id" class="select" >
		           <option value=-1>请选择</option>
                   <c:forEach var="category" items="${categorys}" varStatus="status" >
                           <option value="${category.id}">${category.title}</option>
                  </c:forEach>
		        </select>
              </li>
              <li></li>
            </ul>
            </div></li>
            <li>
            <label>商品编码：</label>
            <div class="con">
            <ul>
              <li>
                <input type="text" name="num" class="inp" />
              </li>
              <li></li>
            </ul>
            </div></li>
            <li>
            <label>主机型号：</label>
            <div class="con">
            <ul>
              <li>
                <input type="text" name="model" class="inp" />
              </li>
              <li></li>
            </ul>
            </div></li>
            <li>
            <label>规格：</label>
            <div class="con">
            <ul>
              <li>
                <input type="text" name="spec" class="inp" />
              </li>
              <li></li>
            </ul>
            </div></li>
          <li>
            <label>销售价格($)：</label>
            <div class="con">
            <ul>
              <li>
                <input type="text" name="price" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" class="inp" maxlength="9"/>
                </li>
              <li><em>*</em></li>
              <li><span id="spInputNum" style="display:none;" class="error">请输入数字</span></li>
            </ul>
            </div></li>
          
          <li>
            <label>工厂型号：</label>
            <div class="con">
            <ul>
              <li>
                <input type="text" name="factory_model" class="inp" />
              </li>
            </ul>
            </div></li>
          
          <li>
            <label>描述：</label>
                <textarea name="description" cols="" rows="" class="textarea"></textarea>
          </li>
        </ol>
      </div>
    </div>
    <div class="form-but">
      <button type="submit" class="button-s4">添加</button>
      <button type="reset" class="button-s4">清空</button>
      <button type="button" class="button-s4" onclick="location.href='../good/manageGoods.go';">返回</button>
    </div>
  </div>
  </form>
</div>
</body>
</html>