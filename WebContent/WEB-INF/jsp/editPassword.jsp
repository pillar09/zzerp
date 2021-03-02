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
			
			var valP = $("input[name='password']").val();
			var noVal = !valP;
			leage = leage && !noVal;
			if(noVal){
				$("input[name='password']").next().next().html("请输入新密码！").show();
			}else{
				$("input[name='password']").next().next().html("").hide();
			}
			
			var rePwdField = $("input[name='rePassword']");
			var valRep = rePwdField.val();
			noVal = !valRep;
			leage = leage && !noVal;
			if(noVal){
				rePwdField.next().next().html("请再一次输入密码！").show();
			}else{
				rePwdField.next().next().html("").hide();
			};
			
			noVal = (valP!=valRep);
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

	});
	</script>
</head>

<body>
	
    <div class="main">
    
        <!-- 表单模块 -->
        <c:if test="${result < 1 }">
        <div class="mag-t1">修改失败，${errMsg } </div>
        </c:if>
        <c:if test="${result == 1 }">
        <div class="mag-t2">修改成功</div>
        </c:if>
        <form action="../account/updatePassword.go">
        <div class="formBox">
        
        	<h3><i></i><span>修改密码</span></h3>
            
        	<div class="content">
        		<table class="c2">
            	<tr>
                	<th>原密码：</th>
                    <td><input type="password" name="cipher" class="inp" /><span class="red">*</span></td>
                </tr>
                <tr>
                	<th>新密码：</th>
                    <td><input type="password" name="password" class="inp" /><span class="red">*</span><span style="display:none;" class="error">未输入密码！</span></td>
                </tr>
                <tr>
                	<th>确认新密码：</th>
                    <td><input type="password" name="rePassword" class="inp" /><span class="red">*</span><span style="display:none;" class="error">2次输入的密码不一致！</span></td>
                </tr>
              </table>
       	  </div>
            <div class="form-but"><!-- 表单按钮区 -->
            	<button type="submit" class="button-s4">确认修改</button>
          	</div><!-- 表单按钮区 END -->
                    
            
        </div> <!-- 表单模块 END -->
   </form>
    </div>
    
</body>
</html>