function users_get_current_role(userId) {
    var role = "";
    webix.ajax().sync().get("users/" +userId +"/deals_role", null, {success: function (text, data, XmlHttpRequest) {
        role = current_role = JSON.parse(text).roleName;
    }});
    return role;
}

function users_update_prepare_data() {
    return {
        userLastName: $$("lastName").getValue()
        , userFirstName: $$("firstName").getValue()
        , userMiddleName: $$("middleName").getValue()
        , userCustomFields: {
            ucfPhone: $$("phone").getValue()
            , ucfMail: $$("mail").getValue()
            , ucfBirthDate: $$("birthDate").getValue().substr(0,10)
            , ucfAddress: $$("address").getValue()
        }
        , userPassports: {
            upSeries: $$("passport_series").getValue()
            , upNumber: $$("passport_number").getValue()
            , upGivenBy: $$("passport_given_by").getValue()
            , upGivenDate: $$("passport_given_date").getValue().substr(0,10)
            , upLocationAddress: $$("passport_location_address").getValue()
        }
    }
}

function users_get_data(url, id) {
    var result;
    webix.ajax().sync().get(url + "/" + id, null
        , {
            success: function (text, data, XmlHttpReques) {
                result = JSON.parse(text);
            }, error: function (ext, data, XmlHttpReques) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
            }
        });
    return result;
}

function users_update_request_send(url, id, data) {
    webix.ajax().headers({"Content-type": "application/json"}).sync().put(url + "/" + id, data
        , {
            success: function (text, data, XmlHttpReques) {
                var response = JSON.parse(text);
                webix.message(response.message);
            }, error: function (ext, data, XmlHttpReques) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
            }
        })
}

function users_set_update_form(data) {
    $$("userId").setValue(data.userId);
    $$("lastName").setValue(data.userLastName);
    $$("firstName").setValue(data.userFirstName);
    $$("middleName").setValue(data.userMiddleName);
    $$("mail").setValue(data.userCustomFields.ucfMail);
    $$("phone").setValue(data.userCustomFields.ucfPhone);
    $$("address").setValue(data.userCustomFields.ucfAddress);
    $$("birthDate").setValue(data.userCustomFields.ucfBirthDate);
    $$("userPersonalDataAgreementConfirmed").setValue(data.userPersonalDataAgreementConfirmed);
    $$("confirmCheckbox").setValue(data.userPersonalDataAgreementConfirmed);
    if($$("confirmCheckbox").getValue() === 1){
        $$("confirmCheckbox").disable();
    }

}

function users_get_users_on_moderation(url) {
    var result;
    webix.ajax().sync().get(url + "/" + 3, null
        , {
            success: function (text, data, XmlHttpRequet) {
                result = JSON.parse(text);
            }, error: function (ext, data, XmlHttpRequet) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
            }
        });
    return result;
}

function users_set_moderation_table_content(data) {
    $$("m_table").clearAll();
    for (var i = 0; i < data.length; i++) {
        var dataRow = {
            m_user_id: data[i].userId
            , m_last_name: data[i].userLastName
            , m_first_name: data[i].userFirstName
            , m_middle_name: data[i].userMiddleName
            , m_birth_data: data[i].userCustomFields.ucfBirthDate
            , m_card_state: data[i].userCardId !== null ? data[i].userCardId.cardStateId.stateNameLocale : ""
        };
        $$("m_table").add(dataRow);
    }
    $$("m_table").refresh();
}

function user_set_moderation_user_info_form(data) {
    $$("info_userId").setValue(data.userId);
    $$("info_lastName").setValue(data.userLastName);
    $$("info_firstName").setValue(data.userFirstName);
    $$("info_middleName").setValue(data.userMiddleName !== null ? data.userMiddleName : "");
    $$("info_mail").setValue(data.userCustomFields.ucfMail);
    $$("info_phone").setValue(data.userCustomFields.ucfPhone);
    $$("info_address").setValue(data.userCustomFields.ucfAddress);
    $$("info_birthDate").setValue(data.userCustomFields.ucfBirthDate);
    $$("info_card_state").setValue(data.userCardId !== null ? data.userCardId.cardStateId.stateNameLocale : "");
}

function users_check_passport_uploaded(url) {
    var uploaded = false;
    webix.ajax().sync().get(url, null, {
        success: function (text, data, request) {
            uploaded = JSON.parse(text).state === 'OK';
        }, error: function (text, data, request) {
            webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
        }
    });
    return uploaded;
}

function users_confirm_moderation(url, userRowId) {
    webix.ajax().sync().post(url, null
        , {
            success: function (a,b,c) {
                webix.alert({width: 350, text: JSON.parse(a).message});
            }, error: function (a,b,c) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
            }
        });
}

function users_get_role(url){
    var role = false;
    webix.ajax().sync().post(url, null
        , {
            success: function (a,b,c) {
                role = a.roleName;
            }, error: function (a,b,c) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
            }
        });
    return role;
}