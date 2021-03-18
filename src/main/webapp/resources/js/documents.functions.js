function create_deal_document(doc_type_name, deal_id) {
    var result = "";
    webix.ajax().sync().get("documents/deals/" + deal_id + "?dtName=" + doc_type_name, null,
        {
            success: function (data, text, request) {
                result = JSON.parse(data);
            }, error: function (d,t,r) {
            webix.alert("Произошла ошибка, повторите попытку позже");
        }
        });
    return result;
}

function create_user_document(doc_type_name, user_id) {
    var result = "";
    webix.ajax().sync().get("documents/users/" + user_id + "?dtName=" + doc_type_name, null,
        {
            success: function (data, text, request) {
                result = JSON.parse(data);
            }, error: function (d,t,r) {
            webix.alert("Произошла ошибка, повторите попытку позже");
        }
        });
    return result;
}

function confirm_deal_document(doc_id, confirm_code) {
    var result = "";
    webix.ajax().sync().post("documents/deal_documents/" + doc_id, {code: confirm_code},
        {
            success: function (data, text, request) {
                result = JSON.parse(data);
            }, error: function (d,t,r) {
            webix.alert("Произошла ошибка, повторите попытку позже");
        }
        });
    return result;
}

function confirm_user_document(doc_id, confirm_code) {
    var result = "";
    webix.ajax().sync().post("documents/user_documents/" + doc_id, {code: confirm_code},
        {
            success: function (data, text, request) {
                result = JSON.parse(data);
            }, error: function (d,t,r) {
            webix.alert("Произошла ошибка, повторите попытку позже");
        }
        });
    return result;
}

function create_personal_data_agreement() {
    var userData = users_update_prepare_data();
    var docData = "";

    webix.ajax().headers({"Content-type": "application/json"}).sync().post("documents/users/personal_data_agreement", userData,
        {
            success: function (data, text, request) {
                docData = JSON.parse(data);
            }, error: function (d,t,r) {
                webix.alert("Произошла ошибка, повторите попытку позже");
            }
        });

    if (docData.state !== "OK") {
        webix.alert(docData.message);
        return;
    }

    $$("doc_obj_type").setValue("user_document");
    $$("doc_type_name").setValue("personal_data_agreement");
    $$("doc_obj_id").setValue(docData.object.userDocId);
    $$("doc_text_place").setHTML(docData.object.userDocText);
    $$("doc_text_view").resize();

    $$("docs_window").isPersonalAgreement = true;

    $$("docs_window").show();
    $$("index_page").disable();
}

function create_deal_agreement(deal_id, doc_type) {
    var result = "";
    webix.ajax().sync().get("documents/deals/" + deal_id + "?dtName=" + doc_type, null,
        {
            success: function (data, text, request) {
                result = JSON.parse(data);
            }, error: function (d,t,r) {
            webix.alert("Произошла ошибка, повторите попытку позже");
        }
        });

    if (result.state !== "OK") {
        webix.alert(result.message);
        return;
    }

    if(current_role == 'ROLE_INVESTOR' && doc_type == 'borrower_agreement'){
        showPayingWarning();
    }

    $$("doc_obj_type").setValue("deal_document");
    $$("doc_type_name").setValue(doc_type);
    $$("parent_obj_id").setValue(deal_id);
    $$("doc_obj_id").setValue(result.object.dealDocId);
    $$("doc_text_place").setHTML(result.object.dealDocText);
    $$("doc_text_view").resize();

    $$("docs_window").show();
    $$("index_page").disable();
}

function start_user_personal_agreement_confirmation() {
    var result = confirm_user_document($$("doc_obj_id").getValue(), $$("confirm_code").getValue());

    if (result.state !== "OK") {
        webix.alert(result.message);
        return;
    }

    $$("confirmCheckbox").setValue(1);
    $$("confirmCheckbox").disable();
    $$("userPersonalDataAgreementConfirmed").setValue(1);

    if($$("docs_window").isPersonalAgreement){
        var data = users_update_prepare_data();
        users_update_request_send("users", $$("userId").getValue(), data);
    }

    webix.confirm({
        ok: "Скачать"
        , cancel: "Закрыть"
        , text: "Документ успешно подтвержден, для скачивания версии в формате PDF нажмите \"Скачать\". "+
        "Вы также можете скачать его позже в разделе \"Документы\""
        , callback: function (result) {
            if (result) {
                window.open("documents/user_documents/" + $$("doc_obj_id").getValue());
            }
            close_docs_window();
        }
    });
}

function start_deal_document_confirmation(hasNext) {
    var result = confirm_deal_document($$("doc_obj_id").getValue(), $$("confirm_code").getValue());

    if (result.state !== "OK") {
        webix.alert(result.message);
        return;
    }

    webix.confirm({
        ok: "Скачать"
        , cancel: "Закрыть"
        , text: "Документ успешно подтвержден, для скачивания версии в формате PDF нажмите \"Скачать\". "+
        "Вы также можете скачать его позже в разделе \"Документы\""
        , callback: function (result) {
            if (result)
                window.open("documents/deal_documents/" + $$("doc_obj_id").getValue());

            close_docs_window();
            if (hasNext)
                nextAction($$("parent_obj_id").getValue());
            else
                lastAction($$("parent_obj_id").getValue())
        }
    });
}

function nextAction(deal_id) {
        create_deal_agreement(deal_id, "borrower_agreement");
}

function lastAction(deal_id) {
    if (current_role === 'ROLE_BORROWER') {
        deals_set_borrower_user_request(deal_id, "-1");
    } else if (current_role === 'ROLE_INVESTOR') {
        deals_confirm(deal_id);
    }
}


