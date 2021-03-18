webix.ui({
    view: "window"
    , move: false
    , id: "docs_window"
    , height: 700
    , width: 800
    , position: "center"
    , head: {
        view: "toolbar", cols: [
            {view: "label", label: "Просмотр документов"}
            , {
                view: "button", value: "Закрыть", width: 100, click: function () {
                    close_docs_window();
                }
            }
        ]
    }, body:
        {
            view: "form", elements: [
                {view: "text", hidden: true, id: "doc_obj_type"}
                ,{view: "text", hidden: true, id: "doc_obj_id"}
                ,{view: "text", hidden: true, id: "doc_type_name"}
                ,{view: "text", hidden: true, id: "parent_obj_id"}
                ,{
                    view: "scrollview"
                    , id: "doc_text_view"
                    , height: 450
                    , scroll: "y"
                    , body: {
                        rows:[
                            {template: "document text here", id: "doc_text_place", autoheight:true}
                        ]
                    }
                }
                , {view: "label", id: "confirm_warning", hidden: true, height: 25}
                , {
                    cols: [
                        {view: "text", label: "Код подтверждения", id: "confirm_code", type: "password", labelWidth: 175}
                        ,{width: 25}
                        , {
                            view: "button"
                            , value: "Подписать"
                            , type: "form"
                            , click: function () {
                                if ($$("confirm_code").getValue() === "") {
                                    webix.alert("Введите код подтверждения!");
                                    return;
                                }

                                if ($$("doc_obj_type").getValue() === "user_document" && $$("doc_type_name").getValue() === "personal_data_agreement") {
                                    start_user_personal_agreement_confirmation();
                                } else if ($$("doc_obj_type").getValue() === "deal_document" && $$("doc_type_name").getValue() === "agent_agreement") {
                                    start_deal_document_confirmation(true);
                                } else if ($$("doc_obj_type").getValue() === "deal_document" && $$("doc_type_name").getValue() === "borrower_agreement") {
                                    start_deal_document_confirmation(false);
                                } else if ($$("doc_obj_type").getValue() === "deal_document" && $$("doc_type_name").getValue() === "investor_agreement") {
                                    start_deal_document_confirmation(false);
                                }
                            }
                        }
                    ]
                }
            ]
        }
});

function close_docs_window() {
    $$("docs_window").hide();
    $$("index_page").enable();
    $$("doc_obj_type").setValue("");
    $$("doc_obj_id").setValue("");
    $$("confirm_code").setValue("");
    $$("doc_text_place").setHTML("<HTML></HTML>");
}

function showPayingWarning(){

    $$("confirm_warning").show();
    $$("confirm_warning").setHTML("<span style='color:red;'>Внимание!!! подписание договора приведет к переводу суммы заемщику</span>");
}