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
		$("a.bt_del").click(function(event){
			if(!confirm("确定要删除？")){
				event.preventDefault();
			}
		});
	});
	</script>
</head>
<body>
	
    <div class="main">

        <!-- 表单模块 -->
        <div class="formBox"> 
        	<div class="content" style="padding:0;">
            <table class="c4" style="float:left; ">
                <tr>
                  <th>入库时间：</th>
                  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${w.warehouse_date}" /></td>
                  <th>经手人：</th>
                  <td>${w.warehouser.username}</td>
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
                    <th width='50px'>数量</th>
                    <th width='60px'>入库后</th>
                    <th width='40px'>当前</th>
                </tr>
                <c:forEach var="wg" items="${w.wgList}" varStatus="wgStatus" >
                <tr>
                    <td>${(wgStatus.index)+1}</td>
                    <td>${goodMap[fn:trim(wg.good.id)].category.title}</td>
                    <td>${goodMap[fn:trim(wg.good.id)].model}</td>      
                    <td>${goodMap[fn:trim(wg.good.id)].num}</td>
                    <td>${goodMap[fn:trim(wg.good.id)].spec}</td>
                    <td>${wg.quantity}</td>
			         <td>${wg.reserve} 
			          <c:if test="${wg.reserve!=null && wg.reserve>wg.ordered}">
			          	<span style="color: #00cc00;">【${wg.reserve-wg.ordered}】</span>
			          </c:if>
			          <c:if test="${wg.reserve!=null && wg.reserve<=wg.ordered}">
			          	<span style="color: #dd0000;">【${wg.reserve-wg.ordered}】</span>
			          </c:if>
		            </td>
                    <td>${goodMap[fn:trim(wg.good.id)].reserve}</td>
                </tr>
             	</c:forEach>
            </table> 
          </div>
          <!-- 表单模块 -->
           <div class="memo">
        		<table width="100%">
                    <tr>
	                    <td>留言：</td>
                    </tr>
                   	<tr>
                        <td valign="top"><div>${w.memo}</div></td>
                    </tr>
                </table>
           </div>
            
          <!-- 表单模块 END --> 
       
    <div class="form-but">
      <button type="button" class="button-s4" onclick="location.href='../good/warehousingRecord.go'">返回</button>
    </div>
    <!-- 表单按钮区 END -->
    </div>
    
</body>
</html>