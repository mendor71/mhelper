webix.ui({
    view: "window"
    , move: false
    , id: "deal_refund_window"
    , height: 480
    , width: 640
    , position: "center"
    , head: {
        view: "toolbar", cols: [
            {view: "label", label: "Возврат займа"}
            , {
                view: "button", value: "Закрыть", width: 100, click: function () {
                    deal_refund_clean_and_close();
                }
            }
        ]
    }, body: {
        view: "form", id: "deal_refund_form", elements: [
            {
                view: "text",
                labelWidth: 200,
                id: "refund_dealId",
                name: "refund_dealId",
                label: "ID Сделки",
                hidden: true
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "refund_dealGivenSum",
                name: "refund_dealGivenSum",
                label: "Сумма инвестирования",
                readonly: true
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "refund_dealRefundSum",
                name: "refund_dealRefundSum",
                label: "Сумма возврата",
                readonly: true
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "refund_dealCommission",
                name: "refund_dealCommission",
                label: "Комиссия агента",
                readonly: true
            }
            , {
                view: "text",
                labelWidth: 200,
                id: "refund_result",
                name: "refund_result",
                label: "Итого к возврату",
                readonly: true
            }
            , {
                cols: [
                    {}, {
                        view: "button", value: "Подтвердить", type: "form", click: function () {
                            deals_refund($$("refund_dealId").getValue());
                            deal_refund_clean_and_close();
                        }
                    }
                ]
            }
        ]
    }
});

function deal_refund_clean_and_close() {
    $$("refund_dealGivenSum").setValue("");
    $$("refund_dealRefundSum").setValue("");
    $$("refund_dealCommission").setValue("");
    $$("refund_result").setValue("");

    $$("deal_refund_window").hide();
    $$("index_page").enable();
}