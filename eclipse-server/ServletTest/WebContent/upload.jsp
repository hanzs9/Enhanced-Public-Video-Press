<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>file uploaded</title>
</head>
<body>
<h1>file uploaded</h1>
<form method="post" action="ServletTest/UploadServlet" enctype="multipart/form-data">
    Choose a file
    <input type="file" name="uploadFile" />
    <br/><br/>
    <input type="submit" value="Submit" />
</form>
</body>
</html>