// requestTable = [
//     {
//         cols: [
//             {width: 25}
//             , {
//                 rows: [
//                     {
//                         cols: [
//                             {
//                                 view: "button", width: 200, value: "Создать новую", click: function () {
//                                 $$("index_page").disable();
//                                 $$("ir_create_window").show();
//                             }
//                             }, {}
//                         ]
//                     }
//                     , {height: 10}
//                     , {
//                         view: "datatable", id: "reqTable", columns: [
//                             {id: "irId", header: "id", width: 200}
//                             , {id: "irRegCreated", header: "Дата регистрации", width: 200}
//                             , {id: "irInvestAmount", header: "Сумма инвестирования", width: 200}
//                             , {id: "irRefundAmount", header: "Сумма возврата", width: 200}
//                             , {id: "irLoanTerm", header: "Продолжительность", width: 200}
//                             , {id: "irStateIdStatename", header: "Статус", width: 200}
//                             , {id: "irDeadLine", header: "Дата возврата", width: 200}
//                             , {id: "irIlrId", header: "ID сделки", width: 200}
//                         ]
//                     }
//                     , {}
//                 ]
//             }
//             , {width: 25}
//         ]
//     }
// ];