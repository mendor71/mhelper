profileForm = [
    {
        view: "scrollview", scroll: "y", body: {
        rows: [
            {height: 25}
            , {
                scroll: "y", cols: [
                    {}
                    , {
                        rows: [
                            {view: "label", align: "center", css: "my_style", label: "Личные данные"}
                            , {
                                view: "label",
                                id: "moderationWarn",
                                align: "center",
                                label: "",
                                css: "warn_style"
                            }
                            , {
                                view: "form", id: "updateProfileForm", width: 650, elements: [
                                    {
                                        view: "text",
                                        id: "userId",
                                        name: "ID",
                                        label: "ID пользователя",
                                        labelWidth: 195,
                                        readonly: true,
                                        hidden: true
                                    }
                                    , {
                                        view: "text",
                                        required: true,
                                        id: "lastName",
                                        name: "lastName",
                                        label: "Фамилия",
                                        labelWidth: 195,
                                        invalidMessage: "Фамилия не указана!"
                                    }
                                    , {
                                        view: "text",
                                        required: true,
                                        id: "firstName",
                                        name: "firstName",
                                        label: "Имя",
                                        labelWidth: 195,
                                        invalidMessage: "Имя не указано!"
                                    }
                                    , {
                                        view: "text",
                                        id: "middleName",
                                        name: "middleName",
                                        label: "Отчество",
                                        labelWidth: 195,
                                        invalidMessage: "Отчество не указано!"
                                    }
                                    , {
                                        view: "text",
                                        required: true,
                                        id: "mail",
                                        name: "mail",
                                        label: "Электронная почта",
                                        labelWidth: 195,
                                        invalidMessage: "Email не указан!"
                                    }
                                    , {
                                        view: "text",
                                        required: true,
                                        id: "phone",
                                        name: "phone",
                                        label: "Телефон",
                                        labelWidth: 195,
                                        invalidMessage: "Формат 10 символов (без восьмерки)",

                                    }
                                    , {
                                        view: "text",
                                        required: true,
                                        id: "address",
                                        name: "address",
                                        label: "Домашний адрес",
                                        labelWidth: 195,
                                        invalidMessage: "Домашний адрес не указан!"
                                    }
                                    , {
                                        view: "datepicker",
                                        required: true,
                                        timepicker: false,
                                        id: "birthDate",
                                        name: "birthDate",
                                        labelWidth: 195,
                                        label: "Дата рождения",
                                        format: "%d.%m.%Y   ",
                                        stringResult: true,
                                        invalidMessage: "Поле должно быть заполнено!",
                                        format:webix.Date.dateToStr("%d.%m.%Y")
                                    },
                                    {height: 15}
                                    , {
                                        view: "text",
                                        id: "passport_series",
                                        name: "passport_series",
                                        required: true,
                                        label: "Серия паспорта",
                                        labelWidth: 195,
                                        invalidMessage: "4 Цифры!"
                                    }
                                    , {
                                        view: "text",
                                        id: "passport_number",
                                        name: "passport_number",
                                        required: true,
                                        label: "Номер паспорта",
                                        labelWidth: 195,
                                        invalidMessage: "6 Цифр!"
                                    }
                                    , {
                                        view: "text",
                                        id: "passport_given_by",
                                        name: "passport_given_by",
                                        required: true,
                                        label: "Выдан (кем)",
                                        labelWidth: 195,
                                        invalidMessage: "Поле должно быть заполнено!"
                                    }
                                    , {
                                        view: "datepicker",
                                        id: "passport_given_date",
                                        name: "passport_given_date",
                                        required: true,
                                        label: "Выдан (когда)",
                                        labelWidth: 195,
                                        timepicker: false,
                                        format: "%Y-%m-%d",
                                        stringResult: true,
                                        invalidMessage: "Поле должно быть заполнено!"
                                    }
                                    , {
                                        view: "text",
                                        id: "passport_location_address",
                                        name: "passport_location_address",
                                        required: true,
                                        label: "Адрес регистрации",
                                        labelWidth: 195,
                                        invalidMessage: "Поле должно быть заполнено!"
                                    }
                                    , {
                                        view: "checkbox",
                                        name: "confirmCheckbox",
                                        id: "confirmCheckbox",
                                        labelWidth: 0,
                                        labelRight: "Даю согласие на обработку персональных данных",
                                        //width: 450,
                                        invalidMessage: "Подтвердите согласие на обработку персональных данных!",
                                    }
                                    ,{
                                        view: "checkbox",
                                        name: "userPersonalDataAgreementConfirmed",
                                        id: "userPersonalDataAgreementConfirmed",
                                        hidden: true,
                                        labelWidth: 0
                                    }
                                    , {
                                        cols: [
                                            {}, {}
                                            , {
                                                view: "button",
                                                value: "Сохранить",
                                                type: "form",
                                                click: function () {
                                                    if ($$("updateProfileForm").validate()) {
                                                        if($$("userPersonalDataAgreementConfirmed").getValue() === 0){
                                                            create_personal_data_agreement();
                                                        } else {
                                                            var data = users_update_prepare_data();
                                                            users_update_request_send("users", $$("userId").getValue(), data);
                                                        }
                                                    }
                                                }
                                            }
                                        ]
                                    }
                                ], rules: {
                                    "lastName": webix.rules.isNotEmpty,
                                    "firstName": webix.rules.isNotEmpty,
                                    "mail": webix.rules.isEmail,
                                    "birthDate": webix.rules.isNotEmpty,
                                    "address": webix.rules.isNotEmpty,
                                    "phone": function (value) {
                                        return /^\+7\(\d{3}\)\d{3}-\d{2}-\d{2}$/.test(value);
                                    },
                                    "passport_given_by": webix.rules.isNotEmpty
                                    ,"passport_given_date": webix.rules.isNotEmpty
                                    ,"passport_location_address": webix.rules.isNotEmpty
                                    ,"passport_series": function (value) {
                                        return /^[\d]{4}$/.test(value);
                                    }
                                    ,"passport_number": function (value) {
                                        return /^[\d]{6}$/.test(value);
                                    }

                                }
                            }
                            , {height: 25}
                            //, {view: "button", hidden:true, value: "Подписать согласие на обработку персональных данных", click: create_personal_data_agreement}
                            //, {view: "button", value: "Подписать согласие на запрос информации в БКИ", click: function () {}}
                            //, {height: 25}
                            , {
                                cols: [
                                    {
                                        view: "button",
                                        value: "Скачать 1 страницу паспорта",
                                        click: function () {
                                            if (users_check_passport_uploaded("users/-1/passport/checkUpload/1")) {
                                                window.open("users/-1/passport/download/1");
                                            } else {
                                                webix.alert("Выбранная страница паспорта не была загружена пользователем");
                                            }
                                        }
                                    },
                                    {
                                        view: "button",
                                        value: "Скачать 2 страницу паспорта",
                                        click: function () {
                                            if (users_check_passport_uploaded("users/-1/passport/checkUpload/2")) {
                                                window.open("users/-1/passport/download/2")
                                            } else {
                                                webix.alert("Выбранная страница паспорта не была загружена пользователем");
                                            }
                                        }
                                    }
                                ]
                            }
                            , {height: 25}
                            , {
                                view: "form", id: "pFilesForm", elements: [
                                    {
                                        cols: [
                                            {
                                                view: "label",
                                                label: "Первая страница паспорта",
                                                width: 185
                                            }, {width: 15}
                                            , {view: "list", id: "p1List", type: "uploader"}, {width: 15}
                                            , {
                                                view: "uploader",
                                                id: "p1up",
                                                link: "p1List",
                                                width: 75,
                                                value: "Выбрать",
                                                autosend: false,
                                                multiplie: false,
                                                upload: "users/-1/passport/upload/1"
                                            }
                                        ]
                                    },
                                    {
                                        cols: [
                                            {
                                                view: "label",
                                                label: "Вторая страница паспорта",
                                                width: 185
                                            }, {width: 15}
                                            , {view: "list", id: "p2List", type: "uploader"}, {width: 15}
                                            , {
                                                view: "uploader",
                                                id: "p2up",
                                                link: "p2List",
                                                width: 75,
                                                value: "Выбрать",
                                                autosend: false,
                                                multiplie: false,
                                                upload: "users/-1/passport/upload/2"
                                            }
                                        ]
                                    },
                                    {
                                        cols: [
                                            {}, {}
                                            , {
                                                view: "button",
                                                value: "Загрузить",
                                                type: "form",
                                                click: function () {
                                                    if ($$("p1List").getLastId() === undefined || $$("p2List").getLastId() === undefined) {
                                                        webix.alert("Данные паспорта не загружены или загружены не полностью!");
                                                    } else {
                                                        var message = validateImageUploader("p1up");
                                                        message += validateImageUploader("p2up");
                                                        if (message !== "") {
                                                            message += "<br/>Допустимые форматы изображений:";
                                                            image_formats.forEach(function (f) {
                                                                message += " " + f
                                                            });
                                                            message += "<br/>Максимальный размер файла: 5 мб";

                                                            webix.alert({
                                                                type: "warning"
                                                                , width: 450
                                                                , text: message
                                                            });
                                                        } else {
                                                            $$("p1up").send(function (response) {
                                                                if (response.state === 'OK') {
                                                                    webix.message("Страница 1 загружена успешно!")
                                                                }
                                                            });
                                                            $$("p2up").send(function (response) {
                                                                if (response.state === 'OK') {
                                                                    webix.message("Страница 2 загружена успешно!")
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        ]
                                    }
                                ]
                            }

                            , {height: 25}
                            , {
                                view: "form", id: "card_data_form", elements: [
                                    {view: 'label', label: 'Добавить карту для расчетов',
                                    template: '<form method="POST"  class="application alba_widget_simplebutton"  accept-charset="UTF-8" action="https://partner.rficb.ru/alba/input/">'
                                        +'<input type="hidden" name="key" value="b3LCH6cm/RF7MCO5bjry6x5dx2aNqIY+Uo6SIdGrU5k=" />'
                                        +'<input type="hidden" name="cost" value="1" />'
                                        +'<input type="hidden" name="name" value="register" />'
                                        +'<input type="hidden" name="default_email" value="rfilip@mail.ru" />'
                                        +'<input type="hidden" name="order_id" value="0" />'
                                        +'<input type="submit" id="a1lite_button" value="Добавить карту" />'
                                        +'</form>'},

                                ]
                            }
                            , {
                                view: "form", id: "card_data_form", hidden: true, elements: [
                                    {
                                        view: "text",
                                        required: true,
                                        id: "card_full_number",
                                        name: "card_full_number",
                                        label: "Номер карты",
                                        labelWidth: 195,
                                        invalidMessage: "Номер карты от 16 до 20 символов!"
                                    }
                                    , {
                                        view: "text",
                                        required: true,
                                        id: "card_owner",
                                        name: "card_owner",
                                        label: "Держатель (как на карте)",
                                        labelWidth: 195,
                                        invalidMessage: "ФИО держателя не указаны!"
                                    }
                                    , {view: "label", label: "Срок действия"}
                                    , {
                                        view: "combo",
                                        required: true,
                                        id: "card_month",
                                        name: "card_month",
                                        labelWidth: 195,
                                        label: "Месяц",
                                        options: ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"]
                                    }
                                    , {
                                        view: "combo",
                                        required: true,
                                        id: "card_year",
                                        name: "card_year",
                                        labelWidth: 195,
                                        label: "Год",
                                        options: ["18", "19", "20", "21", "22", "23", "24", "25"]
                                    }
                                    , {
                                        view: "text",
                                        required: true,
                                        id: "card_cvv",
                                        type: "password",
                                        name: "card_cvv",
                                        label: "CVC2/CVV2 код",
                                        labelWidth: 195,
                                        invalidMessage: "CVC2/CVV2 код не указан!"
                                    }
                                    , {
                                        cols: [
                                            {}, {}
                                            , {
                                                view: "button",
                                                value: "Сохранить",
                                                type: "form",
                                                click: function () {
                                                    if ($$("card_data_form").validate()) {
                                                        cards_create("cards/users/-1")
                                                    }
                                                }
                                            }
                                        ]
                                    }
                                ], rules: {
                                    "card_full_number": function (value) {
                                        return /^[\d]{16,20}$/.test(value);
                                    },
                                    "card_owner": webix.rules.isNotEmpty,
                                    "card_month": webix.rules.isNotEmpty,
                                    "card_year": webix.rules.isNotEmpty,
                                    "card_cvv": webix.rules.isNotEmpty
                                }
                            }
                            , {height: 25}
                        ]
                    }
                    , {}
                ]
            }
        ]

    }
    }
];

userModeratorForm = [
    {
        rows: [
            {
                view: "datatable", id: "m_table", columns: [
                {id: "m_user_id", header: "ID Пользователя"}
                , {id: "m_last_name", header: ["Фамилия", {content: "textFilter"}], width: 200}
                , {id: "m_first_name", header: ["Имя", {content: "textFilter"}], width: 200}
                , {id: "m_middle_name", header: ["Отчество", {content: "textFilter"}], width: 200}
                , {id: "m_birth_data", header: "Дата рождения", width: 200}
                , {id: "m_card_state", header: "Статус карты", width: 200}
                , {
                    header: "",
                    width: 200,
                    template: "<input type='button' value='Подробно' class='m_user_info'>"
                }
            ], onClick: {
                m_user_info: function (e, id, trg) {
                    var data = users_get_data("users/", $$("m_table").getItem(id).m_user_id);
                    user_set_moderation_user_info_form(data);

                    $$("index_page").disable();
                    $$("user_info_window").show();
                }
            }
            }
        ]
    }
];


