<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.1.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/webix/codebase/webix.js" type="text/javascript"></script>
</head>
<body>

</body>
</html>
<script>

    var data = {
        userId: -1
        , userLastName: "Фамилия"
        , userFirstName: "Имя"
        , userMiddleName: "Отчество"
        , userCustomFields: {
            ucfBirthDate: "05.08.2011"
            , ucfPhone: "9990001122"
        }, userPassports: {
            upSeries: "6600"
            ,upNumber: "123444"
            ,upGivenBy: "УФМС России"
            ,upGivenDate: "01.01.1900"
            ,upLocationAddress: "РФ"
        }
    };


    webix.ui({
       rows: [
           {view: "button", value: "test", click: function () {
               webix.ajax().headers({"Content-type": "application/json"}).sync().post("documents/users/personal_data_agreement", data, {success: function (text, data, request) {
                    alert(text);
               }})
           }}
       ]
    });
</script>
