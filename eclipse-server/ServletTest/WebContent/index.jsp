<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>upload</title>
</head>
<body>
<h1>upload file</h1>
<form method="post" action="ServletTest/UploadServlet" enctype="multipart/form-data">
    choose a file:
    <input type="file" name="uploadFile" />
    <br/><br/>
    <input type="submit" value="upload" />
</form>
</body>
</html>