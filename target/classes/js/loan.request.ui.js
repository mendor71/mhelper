loanForm = [
    {
        cols: [
            {width: 25}
            , {
                rows: [
                    {
                        cols: [
                            {
                                view: "form", id: "f_loan_ir_form", elements: [
                                {
                                    view: "text",
                                    name: "f_loan_sum",
                                    id: "f_loan_sum",
                                    label: "Сумма сделки",
                                    labelWidth: 125,
                                    width: 350,
                                    invalidMessage: "В поле должно быть введено число"
                                }
                                , {
                                    view: "text",
                                    name: "f_refund_sum",
                                    id: "f_refund_sum",
                                    label: "Сумма возврата",
                                    labelWidth: 125,
                                    width: 350,
                                    invalidMessage: "В поле должно быть введено число"
                                }
                                , {
                                    view: "text",
                                    name: "f_loan_term",
                                    id: "f_loan_term",
                                    label: "Срок (дней)",
                                    labelWidth: 125,
                                    width: 350,
                                    invalidMessage: "В поле должно быть введено число"
                                }
                                , {
                                    view: "button",
                                    value: "Подобрать",
                                    align: "right",
                                    width: 350,
                                    click: function () {
                                        if ($$("f_loan_ir_form").validate()) {
                                            var data = users_get_ir_for_loan_filter("investRequests/users/-1/for");
                                            users_set_ir_for_loan_table(data);
                                        }
                                    }
                                }
                            ], rules: {
                                "f_loan_sum": webix.rules.isNumber
                                , "f_refund_sum": webix.rules.isNumber
                                , "f_loan_term": webix.rules.isNumber
                            }
                            }, {}]
                    }

                    , {height: 10}
                    , {
                        view: "datatable", id: "loanTable", columns: [
                            {id: "irId", header: "id", width: 100}
                            , {id: "irRegCreated", header: "Дата регистрации", width: 200}
                            , {id: "irInvestAmount", header: "Сумма инвестирования", width: 200}
                            , {id: "irRefundAmount", header: "Сумма возврата", width: 200}
                            , {id: "irLoanTerm", header: "Срок займа (дней)", width: 200}
                            , {id: "irStateIdStatename", header: "Статус", width: 100}
                            , {
                                header: "",
                                width: 200,
                                template: "<input type='button' value='Заключить сделку' class='loan_ir_deal'>"
                            }
                        ]
                        , onClick: {
                            loan_ir_deal: function (e, id, trg) {
                                var irRow = $$("loanTable").getItem(id);

                            }
                        }
                    }
                ]
            }
            , {width: 25}
        ]
    }
];
