webix.ui({
    view: "window"
    , move: false
    , id: "deal_lookup_window"
    , height: 480
    , width: 640
    , position: "center"
    , head: {
        view: "toolbar", cols: [
            {view: "label", label: "Подбор предложений"}
            , {
                view: "button", value: "Закрыть", width: 100, click: function () {
                    $$("deal_lookup_window").hide();
                    $$("index_page").enable();
                }
            }
        ]
    }, body: {
        view: "form", id: "deal_lookup_form", elements: [
            {
                view: "text",
                labelWidth: 200,
                id: "lookup_dealGivenSum",
                name: "lookup_dealGivenSum",
                label: "Сумма инвестирования",
                required: true,
                invalidMessage: "В поле должно быть введено число"
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "lookup_dealRefundSum",
                name: "lookup_dealRefundSum",
                label: "Сумма возврата",
                required: true,
                invalidMessage: "В поле должно быть введено число"
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "lookup_dealTermDays",
                name: "lookup_dealTermDays",
                label: "Продолжительность",
                required: true,
                invalidMessage: "В поле должно быть введено число"
            }
            , {
                cols: [
                    {}, {
                        view: "button", value: "Подобрать", type: "form", click: function () {
                            if ($$("deal_lookup_form").validate()) {
                                deals_lookup_for_borrower("-1", $$("lookup_dealGivenSum").getValue(), $$("lookup_dealRefundSum").getValue(), $$("lookup_dealTermDays").getValue());

                                $$("lookup_dealGivenSum").setValue("");
                                $$("lookup_dealRefundSum").setValue("");
                                $$("lookup_dealTermDays").setValue("");
                                $$("deal_lookup_window").hide();
                                $$("index_page").enable();
                            }
                        }
                    }
                ]
            }
        ], rules: {
            "lookup_dealGivenSum": webix.rules.isNumber
            , "lookup_dealRefundSum": webix.rules.isNumber
            , "lookup_dealTermDays": webix.rules.isNumber
        }
    }
});