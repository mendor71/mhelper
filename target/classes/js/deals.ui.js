dealsForm = [
    {cols: [
        {width: 25}
        , {rows: [
            {cols: [
                {view: "button", id: "deal_investor_button", value: "Хочу дать взаймы", click: function () {
                    current_role = "ROLE_INVESTOR";
                    $$("deal_create_window").show();
                    $$("index_page").disable();
                }}
                , {view: "button", id: "deal_borrower_button", value: "Хочу занять", click: function () {
                    current_role = "ROLE_BORROWER";
                    $$("deal_lookup_window").show();
                    $$("index_page").disable();
                }}
                , {},{}
            ]}
            , {height: 10}
            , {view: "label", label: "Для просмотра дополнительных функций дважды кликните по сделке"}

            , {view: "datatable", select: true, id: "dealsTable", columns: [
                {id: "dealId", header: "ID", width: 100}
                , {id: "dealGivenSum", header: "Сумма инвестирования", width: 200}
                , {id: "dealRefundSum", header: "Сумма возврата", width: 200}
                , {id: "dealCommission", header: "Комиссия агента", width: 200}
                , {id: "dealTermDays", header: "Длительность (дней)", width: 200}
                , {id: "dealState", header: "Статус", width: 250}
                , {id: "dealCloCode", header: "Код закрытия", width: 200}
            ], on: {
                onItemDblClick: function (id) {
                    var row = $$("dealsTable").getItem(id);
                    if (current_role === "ROLE_INVESTOR") {
                        $$("deal_ext_id").setValue(row.dealId);
                        $$("deal_ext_inv_sum").setValue(row.dealGivenSum);
                        $$("deal_ext_ref_sum").setValue(row.dealRefundSum);
                        $$("deal_ext_term").setValue(row.dealTermDays);

                        if (row.dealState === "На согласовании иницатора") {
                            $$("deal_ext_confirm").enable();
                            $$("deal_ext_refuse").enable();
                        } else if (row.dealState === "Зарегистрирован") {
                            $$("deal_ext_close").enable();
                        } else if (row.dealState === "Исполняется") {
                            $$("deal_ext_law_suit").enable();
                        }

                        $$("index_page").disable();
                        $$("deal_extended_options_window").show();
                    } else if (current_role === "ROLE_BORROWER") {
                        if (row.dealState === "На согласовании иницатора") {
                            webix.alert("Ожидам согласование инициатора сделки: " + row.dealId);
                            return;
                        }

                        if (row.dealState === "Исполняется") {
                            $$("refund_dealGivenSum").setValue(row.dealGivenSum);
                            $$("refund_dealRefundSum").setValue(row.dealRefundSum);
                            $$("refund_dealCommission").setValue(row.dealCommission);
                            $$("refund_dealId").setValue(row.dealId);

                            $$("refund_result").setValue(row.dealRefundSum + row.dealCommission);

                            $$("deal_refund_window").show();
                            $$("index_page").disable();
                        }

                        if (row.dealState === "Зарегистрирован") {
                            webix.confirm({
                                text: "Вы согласны на предложенные условия?</br></br>"
                                + "<table align='center'>" +
                                "<tbody>" +
                                "<tr>" +
                                "<td>Сумма займа:</td>" +
                                "<td align='right'>" + row.dealGivenSum + " руб.</td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td>Сумма возврата:</td>" +
                                "<td align='right'>" + row.dealRefundSum + " руб.</td>" +
                                "</tr>" +
                                "<tr>" +
                                "<td>Срок займа:</td>" +
                                "<td align='right'>" + row.dealTermDays + " дней</td>" +
                                "</tr>" +
                                "</tbody>" +
                                "</table></br></br>"
                                , title: "Подтвердите заключение сделки"
                                , width: 480
                                , ok: "Согласен"
                                , cancel: "Не согласен"
                                , callback: function (result) {
                                    if (result) {
                                        create_deal_agreement(row.dealId, "agent_agreement");
                                        //deals_set_borrower_user_request(row.dealId, "-1");
                                    }
                                }
                            });
                        }
                    }
                }
            }}
        ]}
        , {width: 25}
    ]}
];