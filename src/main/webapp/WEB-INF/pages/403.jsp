<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
  ERROR 403 ACCESS FORBIDDEN
  <p><a class="btn btn-lg btn-danger" href="<c:url value="/"/>">ВЕРНУТЬСЯ НА ГЛАВНУЮ</a></p>
  <p><a class="btn btn-lg btn-danger" href="<c:url value="/logout"/>">ВЫЙТИ</a></p>
</body>
</html>
