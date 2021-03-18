function get_passport_data(userId) {
    webix.ajax().sync().get("passport/user/" + (userId !== -1 ? userId : -1), null, {
        success: function (data, test, request) {
            data = JSON.parse(data);
            $$("passport_series").setValue(data.upSeries);
            $$("passport_number").setValue(data.upNumber);
            $$("passport_given_by").setValue(data.upGivenBy);
            $$("passport_given_date").setValue(data.upGivenDate);
            $$("passport_location_address").setValue(data.upLocationAddress);
        }, error: function (data, test, requst) {
            webix.alert("Произошла ошибка! Повторите попытку позже...");
        }
    });
}

function set_passport_data(pSeries, pNumber, pGivenBy, pGivenDate, pLocationAddress) {
    var data = {
        upSeries: pSeries
        , upNumber: pNumber
        , upGivenBy: pGivenBy
        , upGivenDate: pGivenDate
        , upLocationAddress: pLocationAddress
    };

    webix.ajax().headers({"Content-type": "application/json"}).sync().post("passport/user/-1", data, {
        success: function (data, test, request) {
            data = JSON.parse(data);
            webix.alert(data.message);
        }, error: function (data, test, requst) {
            webix.alert("Произошла ошибка! Повторите попытку позже...");
        }
    });
}