function cards_create(url) {
    var card_data = {
        cardExpiryMonth: $$("card_month").getValue()
        , cardExpiryYear: $$("card_year").getValue()
        , cardCvv: $$("card_cvv").getValue()
        , cardOwner: $$("card_owner").getValue()
        , cardFullNumber: $$("card_full_number").getValue()
    };

    webix.ajax().headers({"Content-type": "application/json"}).sync().post(url, card_data
        , {
            success: function (text, data, XmlHttpReques) {
                var response = JSON.parse(text);
                webix.alert(response.message);
            }, error: function (ext, data, XmlHttpReques) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
            }
        })
}

function cards_set_update_user_form_part() {
    var result;
    webix.ajax().sync().get("cards/users/-1", null
        , {
            success: function (text, data, XmlHttpReques) {
                result = JSON.parse(text);
            }
            , error: function (ext, data, XmlHttpReques) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
            }
        });

    if (result.cardOwner !== undefined) {
        $$("card_owner").setValue(result.cardOwner);
        $$("card_full_number").setValue(result.cardFullNumber);
        $$("card_month").setValue(result.cardExpiryMonth);
        $$("card_year").setValue(result.cardExpiryYear);
        $$("card_cvv").setValue(result.cardCvv);
    }
}