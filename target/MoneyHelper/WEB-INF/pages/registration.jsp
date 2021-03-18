<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/webix/codebase/webix.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/css/site.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/webix/codebase/webix.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/webix/codebase/i18n/ru.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.1.min.js" type="text/javascript"></script>
    <script src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.min.js"></script>
    <script src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/additional-methods.min.js"></script>
    <title>Регистрация шаг 1</title>
</head>
<body>
<div class="logo">
    <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="logo"/>
    <p class="logo_description">ОН-ЛАЙН КАССА ВЗАИМОВЫГОДНОГО ПАРТНЕРСТВА</p>
</div>
<div id="reg_1" class="reg_container">
    <h3>РЕГИСТРАЦИЯ</h3>
    <form id="reg_form" action="">
        <div class="labels">
            <label for="username">Придумайте логин</label>
            <label for="mail">E-mail</label>
            <label for="password">Придумайте пароль</label>
            <label for="password2">Подтвердите пароль</label>
        </div>
        <div class="inputs">
            <input type="text" id="username" name="username" required/>
            <input type="email" id="mail" name="mail" required/>
            <input type="password" id="password" name="password" required/>
            <input type="password" id="password2" name="password2" required/>
        </div>
        <div class="errors">
            <div class="input-err"></div>
            <div class="input-err"></div>
            <div class="input-err"></div>
            <div class="input-err"></div>
        </div>

        <button type="submit" id="login_btn" class="btn" value="Войти в систему">Продолжить регистрацию</button>

    </form>
</div>
</body>
</html>

<script>
    jQuery.validator.setDefaults({
        debug: true,
        success: "valid"
    });
    $( "form" ).validate({
        rules: {
            password: "required",
            password2: {
                equalTo: "#password"
            }
        },
        submitHandler: function(form) {
            var data = buildUserData();
            webix.ajax().headers({"Content-type":"application/json"}).sync().post("${pageContext.request.contextPath}/users", data
                , {success:function (text, data, XmlHttpReques) {
                        var response = JSON.parse(text);
                        if (response.state === 'OK') {
                            webix.send("j_spring_security_check", {"j_username": $("#username").val(), "j_password": $("#password").val()});
                        } else {
                            webix.alert(response.message);
                        }
                    }, error:function (ext, data, XmlHttpReques) {
                        webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
                    }})
            //$(form).submit();
        },
        invalidHandler: function(event, validator) {
            // 'this' refers to the form
            var errors = validator.numberOfInvalids();
            if (errors) {

                $("div.error").show();
            } else {
                $("div.error").hide();
            }
        }
    });

  webix.ui({
      container: "appContainer"
      , rows: [
          {height: 50}
          , {cols: [
              {}
              , {rows: [
                  {view: "label", label: "Данные для регистрации", align: "center", css: "my_style"}
                  , {view: "form", id: "registrationForm", width: 550, elements: [
                      {view: "text", required: true, id: "username", name: "username", label: "Придумайте логин", labelWidth: "200", invalidMessage: "Логин занят или не заполнен!"}
                      ,{view: "text", required: true, id: "password", name: "password", type: "password", label: "Придумайте пароль", labelWidth: "200", invalidMessage: "Пароли не совпадают!"}
                      ,{view: "text", required: true, id: "pass_confirm", name: "pass_confirm", type: "password", label: "Подтвердите пароль", labelWidth: "200", invalidMessage: "Пароли не совпадают!"}
                      ,{view: "text", required: true, id: "lastName", name: "lastName", label: "Фамилия", labelWidth: "200", invalidMessage: "Фамилия не указана!"}
                      ,{view: "text", required: true, id: "firstName", name: "firstName", label: "Имя", labelWidth: "200", invalidMessage: "Имя не указано!"}
                      ,{view: "text", id: "middleName", name: "middleName", label: "Отчество", labelWidth: "200", invalidMessage: "Отчество не указано!"}
                      ,{view: "text", required: true, id: "mail", name: "mail", label: "Электронная почта", labelWidth: "200", invalidMessage: "Email указан некорректно!"}
                      ,{view: "text", required: true, id: "phone", name: "phone", label: "Номер телефона", labelWidth: "200", invalidMessage: "Формат 10 символов (без восьмерки)"}
                      ,{view: "datepicker", required: true, timepicker: false, id: "birthDate", name: "birthDate", labelWidth: "200", label: "Дата рождения", format: "%Y-%m-%d", stringResult: true, invalidMessage: "Поле должно быть заполнено!"}
                      ,{view: "checkbox", name: "confirmCheckbox", id: "confirmCheckbox", labelWidth: 0, labelRight: "Даю согласие на обработку персональных данных", width: 450, invalidMessage: "Подтвердите согласие на обработку персональных данных!"}
                      ,{cols: [
                          {view: "button", value: "Отмена", click: function () {
                              webix.send("${pageContext.request.contextPath}", null, "GET");
                          }}
                          ,{view: "button", value: "Зарегистрироваться", type: "form", click: function () {
                              if ($$("registrationForm").validate()) {
                                var data = buildUserData();
                                webix.ajax().headers({"Content-type":"application/json"}).sync().post("${pageContext.request.contextPath}/users", data
                                    , {success:function (text, data, XmlHttpReques) {
                                        var response = JSON.parse(text);
                                        if (response.state === 'OK') {
                                            webix.send("j_spring_security_check", {"j_username": $$("username").getValue(), "j_password": $$("password").getValue()});
                                        } else {
                                            webix.alert(response.message);
                                        }
                                    }, error:function (ext, data, XmlHttpReques) {
                                        webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
                                    }})
                              }
                          }}
                        ]}
                      ], rules: {
                          "lastName":webix.rules.isNotEmpty,
                          "firstName":webix.rules.isNotEmpty,
                          "mail":webix.rules.isEmail,
                          "birthDate":webix.rules.isNotEmpty,
                          "confirmCheckbox":webix.rules.isChecked,
                          "phone": function (value) {
                              return /^[\d]{10}$/.test(value);
                          },
                          "password":function(value){return value !== '' && value === $$("pass_confirm").getValue();},
                          "pass_confirm":function(value){return value !== '' && value === $$("password").getValue();},
                          "username":function(value){
                              var valid = false;
                              webix.ajax().sync().get("${pageContext.request.contextPath}/users/checkExists", {userName: value}
                              , {success:function(text, data, XmlHttpReques) {
                                  valid = JSON.parse(text).state === 'NOT_FOUND' && value !== '';
                              }, error:function (ext, data, XmlHttpReques) {
                                  webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
                              }});
                              return valid;
                          }
                    }
                  }
                  , {}
              ]}
              , {}
          ]}
          ,{}
      ]
  });

  webix.i18n.setLocale("ru-RU");
  webix.Date.startOnMonday=true;

  function buildUserData() {
      return {
          userName: $("#username").val()
          , userPassword: $("#password").val()
          , userCustomFields: {
              ucfMail: $("#mail").val()
          }
      }
  }
</script>