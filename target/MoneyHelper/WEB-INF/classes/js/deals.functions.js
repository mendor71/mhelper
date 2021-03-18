function deals_create_new(dealGivenSum, dealRefundSum, dealTermDays) {
    var deal = {dealGivenSum: dealGivenSum, dealRefundSum: dealRefundSum, dealTermDays: dealTermDays};

    webix.ajax().headers({"Content-type": "application/json"}).sync().post("deals", deal, {success: function (text, data, XmlHttpReques) {
        var response = JSON.parse(text);
        webix.alert(response.message);
        var deal = response.object;
        if (response.state === 'OK') {
            $$("dealsTable").add({dealId: deal.dealId
                , dealGivenSum: deal.dealGivenSum
                , dealRefundSum: deal.dealRefundSum
                , dealTermDays: deal.dealTermDays
                , dealState: deal.dealState.stateNameLocale
                , dealCloCode: deal.dealCloCode !== "" ? deal.dealClosureCode.cloCodeNameLocale : ""});
            $$("dealsTable").refresh();
        }
    }, error: function () {
        webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
    }})
}

function deals_get_deals_by_current_user() {
    var result;
    webix.ajax().sync().get("deals/users/-1", null
        , {
            success: function (text, data, XmlHttpRequet) {
                result = JSON.parse(text);
                deals_set_deals_table(result);
            }, error: function (ext, data, XmlHttpRequet) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
            }
        });
    return result;
}

function deals_set_deals_table(data, auto) {
    $$("dealsTable").clearAll();

    if (auto !== undefined && auto === true) {
        if (data.length === 0) {
            webix.alert("По заданным критериям не найдено заявок на финансирование!");
            return;
        }
    }

    for (var i = 0; i < data.length; i++) {
        var dataRow = {
            dealId: data[i].dealId
            , dealGivenSum: data[i].dealGivenSum
            , dealRefundSum: data[i].dealRefundSum
            , dealTermDays: data[i].dealTermDays
            , dealCommission: data[i].dealCommission
            , dealState: data[i].dealState.stateNameLocale
            , dealCloCode: data[i].dealCloCode !== undefined ? data[i].dealCloCode.cloCodeNameLocale : ""
        };
        $$("dealsTable").add(dataRow);
    }

    if (current_role === 'ROLE_INVESTOR')
        $$("dealsTable").hideColumn("dealCommission");
    else
        $$("dealsTable").showColumn("dealCommission");

    $$("dealsTable").refresh();
}

function deals_lookup_for_borrower(borrowerUserId, dealGivenSum, dealRefundSum, dealTermDays) {
    var result;
    webix.ajax().sync().get("deals/for_user/" + borrowerUserId, {dealGivenSum: dealGivenSum, dealRefundSum: dealRefundSum, dealTermDays: dealTermDays}
        , {
            success: function (text, data, XmlHttpRequet) {
                result = JSON.parse(text);
                deals_set_deals_table(result);
            }, error: function (ext, data, XmlHttpRequet) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
            }
        });
}

function deals_set_allowed_options(currentUserRole) {
    if (currentUserRole === "ROLE_INVESTOR") {
        $$("deal_borrower_button").hide();
    } else if (currentUserRole === "ROLE_BORROWER") {
        $$("deal_investor_button").hide();
    } else {
        $$("deal_borrower_button").show();
        $$("deal_investor_button").show();
    }
}

function deals_set_borrower_user_request(dealId, borrowerId) {
    webix.ajax().sync().post("deals/" + dealId + "/users/" + borrowerId + "/borrower", null
        , {
            success: function (text, data, XmlHttpReques)
            {
                var response = JSON.parse(text);
                if (response.state === "OK") {
                    webix.message(response.message);
                    $$("dealsTable").getSelectedItem().dealState = "На согласовании иницатора";
                    $$("dealsTable").refresh();
                } else {
                    webix.alert(response.message);
                }

            }, error: function () {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
        }}
    );
}

function deals_confirm(dealId) {
    deals_do_post_update("deals/" + dealId + "/confirm");
}

function deals_reject(dealId) {
    deals_do_post_update("deals/" + dealId + "/reject");
}

function deals_close(dealId) {
    deals_do_post_update("deals/" + dealId + "/cancel");
}

function deals_law_suit(dealId) {
    deals_do_post_update("deals/" + dealId + "/lawsuit");
}

function deals_refund(dealId) {
    deals_do_post_update("deals/" + dealId + "/refund");
}

function deals_do_post_update(url) {
    webix.ajax().sync().post(url, null
        , {
            success: function (text, data, XmlHttpReques)
            {
                var response = JSON.parse(text);
                var deal = response.object;

                $$("dealsTable").getSelectedItem().dealState = deal.dealState.stateNameLocale;
                $$("dealsTable").refresh();

                webix.message(response.message);
            }, error: function () {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
            }}
    );
    $$("deal_extended_options_window").hide();
    $$("index_page").enable();
}