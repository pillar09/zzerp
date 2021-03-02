<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>中智数码</title>
    <link href="../css/basic.css" rel="stylesheet" type="text/css" />
    <link href="../css/main.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="../js/layer.js"></script>
    <script type="text/javascript">
$(function(){
	$("a.froze").click(function(event){
		if(!confirm("确定要冻结该账户？")){
			event.preventDefault();
		}
	});
	$("a.resetPassword").click(function(event){
		if(!confirm("确定要重置密码？")){
			event.preventDefault();
		}
	});
});
</script>
</head>

<body>
	
<div class="main">
    	
  <div class="searchBox">
    <h3><span>员工搜索</span></h3>
    <table>
      <tr>
        <th style="width:90px;">员工姓名：</th>
        <td><input type="text" class="inp inp-w90"/></td>
        <th>登录帐号：</th>
        <td><input type="text" class="inp inp-w90"/></td>
        <th>状态：</th>
        <td><select name="select3" class="select" style="width:60px;">
          <option value="0">解冻</option>
          <option value="-1">冻结</option>
        </select></td>
        <th>&nbsp;</th>
        
      </tr>
      <tr>
        <th style="width:90px;">岗位：</th>
        <td><select name="select2" class="select" style="width:96px;">
          <option>请选择</option>
        </select></td>
        <th>直接上级：</th>
        <td><select name="select4" class="select" style="width:96px;">
          <option>请选择</option>
        </select></td>
        <th>是否在职：          
          </th>
        <td><select name="select" class="select" style="width:60px;">
          <option>在职</option>
          <option>离职</option>
        </select></td>
        <th><button type="submit" class="btn">搜 索</button></th>
        
      </tr>
    </table>
  </div> 
          
  <!-- 表单模块 END -->            
 <div class="tool"> <span><a href="../account/toAddAccount.go" hidefocus="true" class="bt_add">新增员工</a></span> 
 </div>
  <div class="clear"></div>
		
        
        <!-- 列表模块 -->
        <div class="listBox"> 
          <h3><span>员工列表</span></h3> 
            <table > 
                <tr>
                  <th><input name="" type="checkbox" value="" /></th>
                  <th>员工姓名</th>					
                  <th>岗位名称</th>
                  <th>性别</th>
                  <th>电话</th>
                  <th>Email</th>
                  <th>登录帐号</th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
                <c:forEach var="account" items="${accounts}" varStatus="status" >
             
                <tr <c:if test="${(status.index)%2 != 0}">class="bg"</c:if> >
                  <td><input name="input" type="checkbox" value="" /></td>
					<td>${account.full_name }</td>
                	<td>${account.capacity.post } </td>
                    <td><c:if test="${account.male }">男</c:if><c:if test="${!account.male }">女</c:if></td>
                    <td>${account.phone_num }</td>
                    <td>${account.mail }</td>
                    <td>${account.username }</td>
                    <td><c:if test="${account.state==0 }">正常</c:if><c:if test="${account.state ==-1 }">冻结</c:if></td>
                    <td><a href="../account/toEditAccount.go?id=${account.id  }">编辑</a> |
                     <a href="../account/frozeAccount.go?id=${account.id  }" class="froze">冻结</a> | 
                     <a href="../account/unfrozeAccount.go?id=${account.id  }">解冻</a> | 
                     <a href="../account/resetPassword.go?id=${account.id  }" class="resetPassword">重置密码</a></td>
                </tr>
                </c:forEach>

            </table> 
			
	      <div class="pages-box" >
	        <div class="pages">
	     <c:if test="${pageInfo.totalGroup > 1}">
	      <a href="../account/manageAccounts.go?page=${pageInfo.preGroup}" >&lt;&lt;</a>
	     </c:if>
	         <a href="../account/manageAccounts.go?page=${pageInfo.pre}" >&lt;</a>
	        
	         <c:forEach var="index" items="${pageInfo.indices}" varStatus="status" >
	         	<c:if test="${index == pageInfo.page}"><b>${index}</b></c:if>  
	         	<c:if test="${index != pageInfo.page}"><a href="../account/manageAccounts.go?page=${index}">${index}</a></c:if>  
	         </c:forEach>
	        <a href="../account/manageAccounts.go?page=${pageInfo.next}" >&gt;</a>
	        
	     <c:if test="${pageInfo.totalGroup > 1}">
	      <a href="../account/manageAccounts.go?page=${pageInfo.nextGroup}" >&gt;&gt;</a>
	     </c:if>
	        <input type="text" size="2" title="输入页码按回车" />
	        <a href="#" style="color:#000">GO</a> 页数: [ ${pageInfo.page} / ${pageInfo.total} ] </div>
	      </div>
             
        </div>

    </div>
    
</body>
</html>