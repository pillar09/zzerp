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
<script type="text/javascript">
$(function(){

	function validate(){
		var leage = true;
		
		var valU = $("input[name='username']").val();
		var noVal = !valU;
		leage = leage && !noVal;

		if(noVal){
			$("input[name='username']").next().next().html("请输入正确的用户名！");
		}else{
			
			$("input[name='username']").next().next().html("").hide();
		};
		
		var valCip = $("input[name='cipher']").val();
		noVal = !valCip;
		leage = leage && !noVal;
		
		var lenEnough = valCip.length >= 5;
		leage = leage && lenEnough;
		
		if(noVal || !lenEnough){
			$("input[name='cipher']").next().next().html("密码不少于5位！").css("color","red").show();
		}else{
			
			$("input[name='cipher']").next().next().html("").hide();
		};
		
		var rePwdField = $("input[name='rePassword']");
		var valRep = rePwdField.val();
		noVal = !valRep;
		leage = leage && !noVal;
		if(noVal){
			rePwdField.next().next().html("请再一次输入密码！").show();
		}else{
			rePwdField.next().next().html("").hide();
		};
		
		noVal = (valCip!=valRep);
		leage = leage && !noVal;
		if(noVal){
			rePwdField.next().next().html("两次密码不一致！").show();
		}else{
			rePwdField.next().next().html("").hide();
		}
		
		
		return leage;
	}
	
	$("button:submit").click(function(event){
		if(!validate()){
			event.preventDefault();
		}
	});
	
	$("input[name='username']").blur(function(){
		var $that = $(this);
		$.ajax({
			type: 'POST',
			  url: '../account/checkUsername.go',
			  data: 'username='+ $that.val(),
			  dataType: 'json',
			  success: function(data){
				  if(data){
					  $("input[name='username']").next().next().html("用户已存在！").show();
					  $("button:submit").attr("disabled", true);
				  }else{
					  $("input[name='username']").next().next().html("").hide();
					  $("button:submit").removeAttr("disabled");
				  }
			  }
		});
	});
	
});
</script>
</head>
<body>
<div class="main">
  <c:if test="${result == 0 }">
  <div class="mag-t1"> 添加失败！${errMsg }</div>
  </c:if>
  <!-- 表单模块 -->
  <form action="../account/addAccount.go" method="post" autocomplete="off">
  <div class="formBox">
    <h3><i></i><span>添加新员工</span></h3>
    <div class="content">
      <table class="c4">
        <tr>
          <th>登录帐号：</th>
          <td><input type="text" name="username" class="inp" value="${account.username }"/><span class="red">*</span><span style="display:none;" class="error">请输入正确的用户名！</span></td>
        </tr>
        <tr>
          <th>登陆密码：</th>
          <td><input type="password" name="cipher" class="inp" />
            <span class="red">*</span><span>5位以上字母、数字或组合</span></td>
        </tr>
        <tr>
          <th>确认密码：</th>
          <td><input type="password" name="rePassword" id="rePassword" class="inp" />
            <span class="red">*</span><span style="display:none;" class="error">2次密码不一样</span></td>
        </tr>
        <tr>
          <th>员工姓名：</th>
          <td><input type="text" name="full_name" class="inp" value="${account.full_name }"/></td>
        </tr>
        <tr>
          <th>岗位名称：</th>
          <td><select class="select" name="capacity.id">
             <c:forEach var="capacity" items="${capacitys}" varStatus="status" >
               <option value="${capacity.id}" <c:if test="${account.capacity.id==capacity.id}">selected='selected'</c:if> >${capacity.post}</option>
             </c:forEach>
          </select></td>
        </tr>
        <tr>
          <th>工号：</th>
          <td><input type="text" name="num" class="inp" value="${account.num }"/></td>
        </tr> <tr>
          <th>性别：</th>
          <td><input name="male" type="radio" value="1" <c:if test="${account.male }">checked="checked"</c:if> />男
            <input name="male" type="radio" value="0" <c:if test="${!account.male }">checked="checked"</c:if> />女<span class="red">*</span></td>
        </tr><tr>
          <th>电话：</th>
          <td><input type="text" name="phone_num" value="${account.phone_num }" class="inp" /></td>
        </tr>
        <tr>
          <th>Email：</th>
          <td><input type="text" name="mail" class="inp" value="${account.mail }" /></td>
        </tr>
        <tr>
          <th>状态：</th>
          <td><input name="state" type="radio" value="0"  <c:if test="${account.state == 0 }">checked="checked"</c:if> />正常
  			<input name="state" type="radio" value="-1" <c:if test="${account.state == -1 }">checked="checked"</c:if>/>冻结</td>
        </tr>
  		<tr>
          <th>备注：</th>
          <td><textarea name="description" >${account.description }</textarea></td>
		</tr>
      </table>
    </div>
    <div class="form-but">
      <!-- 表单按钮区 -->
      <button type="submit" disabled="disabled" class="button-s4">添加</button>
      <button type="button" class="button-s4" onclick="location.href='../account/manageAccounts.go'">返回</button>
    </div>
    <!-- 表单按钮区 END -->
  </div>
  </form>
  <!-- 表单模块 END -->
</div>
</body>
</html>