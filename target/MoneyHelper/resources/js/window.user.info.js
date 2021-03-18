webix.ui({
    view: "window"
    , move: false
    , id: "user_info_window"
    , height: 675
    , width: 800
    , position: "center"
    , head: {
        view: "toolbar", cols: [
            {view: "label", label: "Информация о пользователе"}
            , {
                view: "button", value: "Закрыть", width: 100, click: function () {
                    $$("user_info_window").hide();
                    $$("index_page").enable();
                }
            }
        ]
    }, body: {
        view: "form", elements: [
            {
                view: "text",
                readonly: true,
                id: "info_userId",
                name: "info_userId",
                label: "ID пользователя",
                labelWidth: 195,
                hidden: true,
            }
            , {
                view: "text",
                readonly: true,
                id: "info_lastName",
                name: "info_lastName",
                label: "Фамилия",
                labelWidth: 195
            }
            , {
                view: "text",
                readonly: true,
                id: "info_firstName",
                name: "info_firstName",
                label: "Имя",
                labelWidth: 195
            }
            , {
                view: "text",
                readonly: true,
                id: "info_middleName",
                name: "info_middleName",
                label: "Отчество",
                labelWidth: 195
            }
            , {
                view: "text",
                readonly: true,
                id: "info_mail",
                name: "info_mail",
                label: "Электронная почта",
                labelWidth: 195
            }
            , {
                view: "text",
                readonly: true,
                id: "info_phone",
                name: "info_phone",
                label: "Телефон",
                labelWidth: 195
            }
            , {
                view: "text",
                readonly: true,
                id: "info_address",
                name: "info_address",
                label: "Домашний адрес",
                labelWidth: 195
            }
            , {
                view: "text",
                readonly: true,
                id: "info_birthDate",
                name: "info_birthDate",
                label: "Дата рождения",
                labelWidth: 195
            }
            , {
                view: "text",
                readonly: true,
                id: "info_card_state",
                name: "info_card_state",
                label: "Статус карты",
                labelWidth: 195
            }
            , {
                cols: [
                    {
                        view: "button", value: "Страница паспорта 1", click: function () {
                            downloadPassportPage($$("info_userId").getValue(), "1")
                    }
                    }, {
                        view: "button", value: "Страница паспорта 2", click: function () {
                            downloadPassportPage($$("info_userId").getValue(), "2")
                        }
                    }
                ]
            }
            , {
                cols: [
                    {
                        view: "button", type: "danger", value: "Замечание", click: function () {
                        $$("m_moderated_user_id").setValue($$("info_userId").getValue());
                        $$("index_page").disable();
                        $$("user_info_window").hide();
                        $$("m_notif_window").show();
                    }
                    }, {
                        view: "button", type: "form", value: "Утвердить", click: function () {
                            users_confirm_moderation("states/confirmModeration/" + $$("info_userId").getValue(), $$("info_userId").getValue());
                            var onModerationUsersList = users_get_users_on_moderation("users/states");
                            users_set_moderation_table_content(onModerationUsersList);
                        }
                    }
                ]
            }
        ]
    }
});