<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/webix/codebase/webix.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/css/site.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/webix/codebase/webix.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.1.min.js" type="text/javascript"></script>
    <title>Вход</title>
</head>
<body>

<div class="login_container">
    <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="logo"/>
    <p class="logo_description">ОН-ЛАЙН КАССА ВЗАИМОВЫГОДНОГО ПАРТНЕРСТВА</p>
    <form id="login_form" action="j_spring_security_check" method="post">
        <input type="text" id="j_username" name="j_username" required>
        <div class="input_info"></div>
        <input type="password" id="j_password" name="j_password" required>
        <div class="input_info"></div>
        <div class="forget_pass"><a href="${pageContext.request.contextPath}/password_reset">Забыли пароль?</a></div>
        <button type="submit" id="login_btn" class="btn" value="Войти в систему">Войти в систему</button>
        <span class="i_am_mew">Я новый пользователь</span>
        <button type="button" id="sign_up_btn" class="btn">Зарегистрироваться</button>

        <div class="login_quote">
            <img src="${pageContext.request.contextPath}/resources/images/quote.png"/>
        </div>

        <a href="/404">Помощь</a>
        <a href="/404">Разработчик</a>
    </form>
</div>

</body>
</html>

<script>
    $(document).ready(function(){
        $('#sign_up_btn').on('click', function () {
            window.location = '${pageContext.request.contextPath}/registration'
        })
    })
</script>
