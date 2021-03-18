webix.ui({
    view: "window"
    , move: false
    , id: "deal_extended_options_window"
    , height: 480
    , width: 600
    , position: "center"
    , head: {
        view: "toolbar", cols: [
            {view: "label", label: "Выберите действие"}
            , {
                view: "button", value: "Закрыть", width: 100, click: function () {
                    $$("deal_extended_options_window").hide();
                    $$("index_page").enable();
                }
            }
        ]
    }, body: {
        view: "form", elements: [
            {view: "text", id: "deal_ext_id", label: "ID Сделки", labelWidth: 200, readonly: true}
            , {view: "text", id: "deal_ext_inv_sum", label: "Сумма финансирования", labelWidth: 200, readonly: true}
            , {view: "text", id: "deal_ext_ref_sum", label: "Сумма возврата", labelWidth: 200, readonly: true}
            , {view: "text", id: "deal_ext_term", label: "Длительность (дней)", labelWidth: 200, readonly: true}
            , {height: 5}
            , {
                cols: [
                    {view: "button", disabled: true, id: "deal_ext_law_suit", type: "danger", value: "Судиться", click: function () {
                            deals_law_suit($$("deal_ext_id").getValue());
                        }
                    }, {view: "button", disabled: true, id: "deal_ext_refuse", value: "Отказать", click: function () {
                            deals_reject($$("deal_ext_id").getValue());
                        }
                    }, {view: "button", disabled: true, id: "deal_ext_close", value: "Отозвать", click: function () {
                            deals_close($$("deal_ext_id").getValue());
                        }
                    }, {view: "button", disabled: true, id: "deal_ext_confirm", type: "form", value: "Согласовать", click: function () {
                            create_deal_agreement($$("deal_ext_id").getValue(), "agent_agreement");
                        }
                    }
                ]
            }
        ]
    }
});