notificationsTable = [
    {
        cols: [{width: 25}
            , {
                rows: [
                    {
                        cols: [
                            {
                                view: "button", value: "Пометить прочитанными", width: 200, click: function () {
                                    notifications_set_selected_read(notifications_current_page_id);
                            }
                            }
                            , {width: 25}
                            , {
                                view: "switch", id: "n_mode_switch", onLabel: "Новые", offLabel: "Все", value: 1
                                , on: {
                                    onChange: function (newv, oldv) {
                                        notifications_current_page_id = 0;
                                        notifications_max_selected_page_id = 0;
                                        $$("userNotificationsTable").clearAll();

                                        notifications_get_by_user_id("-1", notifications_current_page_id, newv !== 1);
                                    }
                                }
                            }, {}
                        ]
                    }
                    , {height: 10}
                    , {
                        cols: [
                            {
                                view: "label",
                                label: "Для просмотра полного текста уведомления дважды кликинте по нему"
                            }, {}
                        ]
                    }
                    , {
                        view: "datatable", height: 556, id: "userNotificationsTable"
                        , columns: [
                            {
                                id: "n_readed",
                                width: 40,
                                align: "center",
                                css: "center",
                                header: {content: "masterCheckbox", css: "center"},
                                template: "{common.checkbox()}"
                            }
                            , {id: "notifId", header: "ID"}
                            , {id: "notifDate", header: "Дата", width: 150}
                            , {id: "notifText", header: "Текст", width: 650}]
                        , pager: "userNotifTablePager"
                        , select: true
                        , on: {
                            onItemDblClick: function (id) {
                                webix.alert($$("userNotificationsTable").getItem(id).notifText);
                            }
                        }
                    }
                    , {height: 10}
                    , {
                        view: "pager"
                        , id: "userNotifTablePager"
                        , size: 15
                        , group: 10
                        , on: {
                            onItemClick: function (id) {
                                notifications_current_page_id = id;
                                if (id > notifications_max_selected_page_id) {
                                    notifications_max_selected_page_id = id;
                                    notifications_get_by_user_id("-1", id, $$("n_mode_switch").getValue() !== 1);
                                }
                            }
                        }
                    }, {}
                ]
            }
            , {width: 25}]
    }
];