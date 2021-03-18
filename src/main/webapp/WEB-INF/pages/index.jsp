<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/webix/codebase/webix.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/css/site.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/webix/codebase/webix.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/users.functions.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/cards.functions.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/deals.functions.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/notifications.functions.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/user.passport.functions.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/documents.functions.js" type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/resources/js/deals.ui.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/users.ui.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/loan.request.ui.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/notifications.ui.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/invest.request.ui.js" type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/resources/webix/codebase/i18n/ru.js"
            type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.1.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/resources/Inputmask/dist/jquery.inputmask.bundle.js"></script>
    <script src="${pageContext.request.contextPath}/resources/Inputmask/dist/inputmask/phone-codes/phone.js"></script>
    <script src="${pageContext.request.contextPath}/resources/Inputmask/dist/inputmask/phone-codes/phone-be.js"></script>
    <script src="${pageContext.request.contextPath}/resources/Inputmask/dist/inputmask/phone-codes/phone-ru.js"></script>
    <title>Главная</title>
</head>
<body>
<div class="container" id="page_content" style="width: 100%; height: 100%;">
    <sec:authorize access="!isAuthenticated()">
        <c:redirect url="/login"/>">
    </sec:authorize>
    <div id="profile_container"></div>
</div>
</body>
</html>
<style>
    .my_style {
        font-size: 25px;
    }

    .warn_style {
        font-size: 18px;
        margin-bottom: 5px;
        color: #b70f20;
    }

    .m_user_info {
        font-size: 14px;
        border-radius: 7px;
        width: 180px;
        margin-top: 5px;
        background: #4437ff;
        color: white;
        text-align: center;
    }

    .loan_ir_deal {
        font-size: 14px;
        border-radius: 7px;
        width: 180px;
        margin-top: 5px;
        background: #4e98d6;
        color: white;
        text-align: center;
    }

    .m_confirm {
        font-size: 14px;
        border-radius: 7px;
        width: 180px;
        margin-top: 5px;
        background: #19a455;
        color: white;
        text-align: center;
    }

    .m_notif {
        font-size: 14px;
        border-radius: 7px;
        width: 180px;
        margin-top: 5px;
        background: #8b0f23;
        color: #ffffff;
        text-align: center;
    }

    .center {
        text-align: center;
    }

    .webix_hcell.center input[type=checkbox] {
        width: 22px;
        height: 22px;
        margin-top: 12px;
    }

    .webix_table_checkbox {
        width: 22px;
        height: 22px;
        margin-top: 5px;
    }

    .n_notif_read {
        color: #aeaeae;
    }

    .webix_my_confirm_style {
        text-align: left;
        color: #07003a;
    }
</style>

<script src="${pageContext.request.contextPath}/resources/js/window.notification.create.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/window.user.info.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/window.deal.investor.options.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/window.deal.investor.create.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/window.deal.borrower.lookup.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/window.deal.borrower.refund.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/js/window.documents.js" type="text/javascript"></script>

<script>
    webix.i18n.setLocale("ru-RU");

    var notifications_max_selected_page_id = 0;
    var notifications_current_page_id = 0;
    var image_formats = ["png", "jpg", "jpeg", "gif", "tiff"];
    var current_role = "free_man";

    adminForm = [
        {
            cols: [
                {width: 250}
                , {
                    rows: [
                        {height: 250}
                        , {height: 250}
                    ]
                }
                , {width: 250}
            ]
        }
    ];

    webix.ui({
        id: "index_page"
        , container: "page_content"
        , rows: [
            {
                cols: [
                    {   css: 'left_container',
                        rows: [
                            {
                                css: 'left_logo',
                                data: {
                                    title: "Logo",
                                    src: "${pageContext.request.contextPath}/resources/images/logo.png"
                                },
                                height: 150,
                                width: 200,
                                type: "clean",
                                template: function (obj) {
                                    return '<img src="' + obj.src + '" />' +
                                        '<p>ОН-ЛАЙН КАССА</br>ВЗАИМОВЫГОДНОГО ПАРТНЕРСТВА</p>'
                                }
                            },
                            {
                                view: "menu", id: "mainMenu", width: 300, layout: "y", select: true,
                                css: "left_menu",
                                    type: "clean",
                                    borderless:true,
                                    data: [
                                        {id: "profile", value: "профиль"}
                                        , {id: "notifications", value: "уведомления"}
                                        , {id: "deals", value: "сделки"}
                                        , {id: "admin", value: "Администрирование"}
                                        , {id: "moderation", value: "Модерация пользователей"}
                                        , {$template: "Spacer"}
                                        , {id: "logout", value: "Выйти"}
                                ], on: {
                                    onMenuItemClick: function (id) {
                                        if (id === "logout") {
                                            webix.send("${pageContext.request.contextPath}/logout", "", "GET");
                                        } else if (id === "profile") {
                                            showProfile();
                                        } else if (id === "deals") {
                                            showDeals();
                                            //webix.ui(dealsForm, $$("defaultForm"));
                                            //deals_get_deals_by_current_user();
                                            //current_role = 'ROLE_BORROWER'
                                             //current_role = users_get_current_role("-1");
                                            //  deals_set_allowed_options(current_role);
                                        } else if (id === "moderation") {
                                            webix.ui(userModeratorForm, $$("defaultForm"));
                                            var onModerationUsersList = users_get_users_on_moderation("users/states");
                                            users_set_moderation_table_content(onModerationUsersList);
                                        } else if (id === "notifications") {
                                            webix.ui(notificationsTable, $$("defaultForm"));
                                            notifications_max_selected_page_id = 0;
                                            notifications_current_page_id = 0;
                                            notifications_get_by_user_id("-1", 0, false);
                                        } else if (id === "admin") {
                                            webix.ui(adminForm, $$("defaultForm"));
                                        }
                                    }, onAfterLoad: function () {
                                        $$("mainMenu").hideItem("moderation");
                                        $$("mainMenu").hideItem("userAdmin");
                                        $$("mainMenu").hideItem("admin");
                                    }
                                }
                            }
                        ]
                    }
                    , {
                        rows: [
                            {
                                rows: [
                                    {
                                        type: "clean", cols: [
                                            {view: "label",
                                                css:'head_fullname',
                                                label: "${currentUser}",
                                                align: "left",
                                                height: 70,
                                                width: 200
                                            },
                                            {
                                                view: "label",
                                                css:'head_alert',
                                                id: 'head_alert',
                                                label: "! Ваши действия на портале ограничены до прохождения модерации",
                                                align: "center",
                                                height: 70,
                                                width: 500,
                                                hidden: true,
                                            }
                                            , {view: "label", label: "8-800-XXX-XX-XX", align: "right",height: 70, css:'head_contact'}
                                        ]
                                    }
                                    , {height: 70}
                                ]
                            },
                            {id: "defaultForm", cols: [{}]}
                        ]
                    }
                ]
            }
        ]
    });

    webix.i18n.setLocale("ru-RU");
    webix.Date.startOnMonday = true;

    checkUserRoles();
    checkUserState();

    //setDefaultContent();

    function checkUserRoles() {
        webix.ajax().sync().get("${pageContext.request.contextPath}/users/-1/roles"
            , {
                success: function (text, data, XmlHttpReques) {
                    var roleList = JSON.parse(text);
                    for (var i = 0; i < roleList.length; i++) {
                        if (roleList[i].roleName === 'ROLE_ADMIN') {
                            $$("mainMenu").showItem("moderation");
                            $$("mainMenu").showItem("userAdmin");
                            $$("mainMenu").hideItem("deals");
                        }
                    }
                }, error: function (ext, data, XmlHttpReques) {
                    webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
                }
            });
    }

    function checkUserState() {
        webix.ajax().sync().get("${pageContext.request.contextPath}/users/-1/states"
            , {
                success: function (text, data, XmlHttpReques) {
                    var state = JSON.parse(text);
                    if (state.stateId !== 1) {
                        $$("mainMenu").hideItem("deals");
                        $$("mainMenu").hideItem("loan");
                        $$("mainMenu").hideItem("requests");
                        $$('head_alert').show();

                        showProfile();
                    }else{
                        showDeals();
                    }
                }, error: function (ext, data, XmlHttpReques) {
                    webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
                }
            });
    }

    function setDefaultContent() {
        $$("mainMenu").select("deals", true);

        webix.ui(dealsForm, $$("defaultForm"));

        deals_get_deals_by_current_user();

        //current_role = 'ROLE_BORROWER'
        current_role = users_get_current_role("-1");
        deals_set_allowed_options(current_role);
    }

    function validateImageUploader(id) {
        var message = "";
        $$(id).files.data.each(function (obj) {
            if (!validateImageFormat(obj.type)) {
                message += "Формат файла " + obj.name + " некорректен!<br/>";
            }
            if (parseInt(obj.size) > 5000000) {
                message += "Файл " + obj.name + " превышает допустимый размер!<br/>"
            }
        });
        return message
    }

    function validateImageFormat(format) {
        var ok = false;
        image_formats.forEach(function (f) {
            if (f === format) {
                ok = true;
            }
        });
        return ok;
    }

    function downloadPassportPage(userId, pageId) {
        var dl_userId = $$("info_userId").getValue();
        if (users_check_passport_uploaded("users/" + userId + "/passport/checkUpload/" + pageId)) {
            window.open("users/" + dl_userId + "/passport/download/" + pageId);
        } else {
            webix.alert("Выбранная страница паспорта не была загружена пользователем");
        }
    }

    function showProfile(){
        webix.ui(profileForm, $$("defaultForm"));
        var currentUserData = users_get_data("${pageContext.request.contextPath}/users", "-1");
        users_set_update_form(currentUserData);
        cards_set_update_user_form_part();
        get_passport_data(-1);
        $(".webix_el_text[view_id='phone'] input").inputmask({"mask": "+7(999)999-99-99"});
    }

    function showDeals(){
        webix.ui(dealsForm, $$("defaultForm"));
        deals_get_deals_by_current_user();
        current_role = users_get_current_role("-1");
        deals_set_allowed_options(current_role);
    }
</script>
