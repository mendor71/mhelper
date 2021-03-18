webix.ui({
    view: "window"
    , move: false
    , id: "deal_create_window"
    , height: 480
    , width: 640
    , position: "center"
    , head: {
        view: "toolbar", cols: [
            {view: "label", label: "Заявка на инвестирование"}
            , {
                view: "button", value: "Закрыть", width: 100, click: function () {
                    $$("deal_create_window").hide();
                    $$("index_page").enable();
                }
            }
        ]
    }, body: {
        view: "form", id: "deal_create_form", elements: [
            {
                view: "text",
                labelWidth: 200,
                id: "new_dealGivenSum",
                name: "new_dealGivenSum",
                label: "Сумма инвестирования",
                required: true,
                invalidMessage: "В поле должно быть введено число"
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "new_dealRefundSum",
                name: "new_dealRefundSum",
                label: "Сумма возврата",
                required: true,
                invalidMessage: "В поле должно быть введено число"
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "new_dealTermDays",
                name: "new_dealTermDays",
                label: "Продолжительность",
                required: true,
                invalidMessage: "В поле должно быть введено число"
            }
            , {
                cols: [
                    {}, {
                        view: "button", value: "Создать", type: "form", click: function () {
                            if ($$("deal_create_form").validate()) {
                                deals_create_new($$("new_dealGivenSum").getValue(), $$("new_dealRefundSum").getValue(), $$("new_dealTermDays").getValue());

                                $$("new_dealGivenSum").setValue("");
                                $$("new_dealRefundSum").setValue("");
                                $$("new_dealTermDays").setValue("");
                                $$("deal_create_window").hide();
                                $$("index_page").enable();
                            }
                        }
                    }
                ]
            }
        ], rules: {
            "new_dealGivenSum": webix.rules.isNumber
            , "new_dealRefundSum": webix.rules.isNumber
            , "new_dealTermDays": webix.rules.isNumber
        }
    }
});