<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.1.min.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/resources/css/site.css" rel="stylesheet"/>
    <title>Сброс пароля</title>
</head>
<body>
<div class="login_container">
    <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="logo"/>
    <p class="logo_description">ОН-ЛАЙН КАССА ВЗАИМОВЫГОДНОГО ПАРТНЕРСТВА</p>
    <sec:authorize access="!isAuthenticated()">

        <div id="content">
            <p></p><label for="mail">Введите адрес электронной почты</label>
            <p></p><input type="text" id="mail">
            <p></p><input class="btn" type="button" id="reset" value="Сбросить пароль">
        </div>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
        <div id="content">
            <p></p><label for="mail">Введите новый пароль</label>
            <p></p><input type="text" id="pass1">
            <p></p><input type="text" id="pass2">
            <p></p><input class="btn" type="button" id="save" value="Сохранить">
        </div>
    </sec:authorize>
</div>


</body>
</html>

<script>
    $("#reset").click(function () {
        $.ajax({
            type: "post"
            , url: "${pageContext.request.contextPath}/users/pwd_reset"
            , async: false
            , data: {'userMail': $("#mail").val()}
            , success: function (data) {
                $("#content").html("<p>" + data.message);
            }
        });
    });

    $("#save").click(function () {
        if ($("#pass1").val() !== $("#pass2").val()) {
            alert("Пароли не совпадают!");
            return;
        }

        $.ajax({
            type: "put"
            , url: "${pageContext.request.contextPath}/users/pwd_reset"
            , async: false
            , contentType: "application/json"
            , data: encodeURIComponent($("#pass2").val())
            , success: function (data) {
                alert(data.message);
                window.location.href = "${pageContext.request.contextPath}";
            }
        });
    });
</script>
