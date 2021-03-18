webix.ui({
    view: "window"
    , move: false
    , id: "m_notif_window"
    , height: 480
    , width: 640
    , position: "center"
    , head: {
        view: "toolbar", cols: [
            {view: "label", label: "Уведомление пользователю"}
            , {
                view: "button", value: "Закрыть", width: 100, click: function () {
                    $$("m_notif_text_area").setValue("");
                    $$("m_notif_window").hide();
                    $$("user_info_window").show();
                }
            }
        ]
    }, body:
        {
            view: "form", elements: [
            {view: "text", id: "m_moderated_user_id", label: "ID пользователя", labelWidth: 150, readonly: true}
            , {
                view: "textarea",
                id: "m_notif_text_area",
                height: 250,
                label: "Текст уведомления",
                labelWidth: 150
            }
            , {
                cols: [
                    {},
                    {
                        view: "button", value: "Отправить", type: "form", click: function () {
                        notifications_add_for_user($$("m_notif_text_area").getValue(), $$("m_moderated_user_id").getValue());
                        $$("m_notif_text_area").setValue("");
                        $$("m_notif_window").hide();
                        $$("user_info_window").show();
                    }
                    }
                ]
            }
        ]
    }
});