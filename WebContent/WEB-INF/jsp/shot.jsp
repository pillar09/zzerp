<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=320, user-scalable=no, maximum-scale=1.8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>中智数码</title>
    <link href="../css/basic.css" rel="stylesheet" type="text/css" />
    <link href="../css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="main">
<form class="rz-form" method="post" enctype="multipart/form-data" action="uploadImage.go">
        	<div class="content" >
<input name="billNo" type="hidden" value="${billNo}"/>
<table class="c5" >
<tr><td height="50"><input name='file' type="file" accept="image/*" />打开摄像机</td></tr>
<tr><td height="50"><input name='file' type="file" accept="image/*" />打开摄像机</td></tr>
<tr><td height="50"><input name='file' type="file" accept="image/*" />打开摄像机</td></tr>
<tr><td height="50"><input name='file' type="file" accept="image/*" />打开摄像机</td></tr>
<tr><td height="50"><input name='file' type="file" accept="image/*" />打开摄像机</td></tr>
<tr><td height="50"><input name='file' type="file" accept="image/*" />打开摄像机</td></tr>
</table>
</div>
<div class="form-but">
      <button type="submit" class="button-s4" >上传</button>
</div>
</form>
</div>
</body>