<%--
  Created by IntelliJ IDEA.
  User: mendor71
  Date: 27.10.17
  Time: 20:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="/SpringSec/uploadPassport" enctype="multipart/form-data">
    <input type="file" name="uploadedFile" id="fileToUpload" required="" >
    <input type="text" name="pageId" required="" >
    <input type="submit" name="import_file" value="Import File" id="" />
</form>
</body>
</html>
