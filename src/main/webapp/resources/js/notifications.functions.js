function notifications_set_selected_read(currentPageId) {
    var readNotif = [];
    var readRowsId = [];

    var activeRangeStart = currentPageId * 15 ;
    var activeRangeEnd = activeRangeStart + 14;

    var i = 0;
    $$("userNotificationsTable").data.each(function (row) {
        if (i >= activeRangeStart && i <= activeRangeEnd) {
            if (row.n_readed === 1) {
                readNotif.push(row.notifId);
                readRowsId.push(row.id);
            }
        } else {
            row.n_readed = 0;
        }
        i++;
    });

    webix.ajax().sync().post("notifications/read", {notifIdList: readNotif.toString()}, {success: function (a,b,c) {
        webix.message(JSON.parse(a).message);
    }, error: function (a,b,c) {
        webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
    }});

    var delRead = $$("n_mode_switch").getValue() === 1;
    var notifTable = $$("userNotificationsTable");

    readRowsId.forEach(function (item_id) {
        if (delRead) {
            notifTable.remove(item_id)
        } else {
            notifTable.getItem(item_id).n_readed = 0;
            notifTable.getItem(item_id).$css = "n_notif_read";
        }
        notifTable.refresh();
    })
}

function notifications_add_for_user(notifText, notificationUserId) {
    var notification = {
        notifText: notifText
        , notifType: 0
        , notifUserId: {
            userId: notificationUserId
        }
    };

    webix.ajax().headers({"Content-type": "application/json"}).sync().post("notifications/users/" + notificationUserId, notification
        , {
            success: function (text, data, XmlHttpReques) {
                var response = JSON.parse(text);
                if (response.state === 'OK') {
                    webix.alert(response.message);
                }
            }, error: function (ext, data, XmlHttpReques) {
                webix.alert("Сервер вернул ошибку! Повторите попытку позже...")
            }
        })
}

function notifications_get_by_user_id(userId, pageId, read) {
    var nData;
    webix.ajax().sync().get("notifications/paged/users/" + userId + "/?pageId=" + pageId + "&read=" + read, null, {success: function (text, data, request) {
        nData = JSON.parse(text);
    }, error: function (text, data, request) {
        webix.alert("Сервер вернул ошибку! Повторите попытку позже...");
    }});

    for (var i = 0; i < nData.length; i++) {
        var row = nData[i];
        if (row.notifStateId.stateId === 18) {
            row.$css = "n_notif_read";
        }
        $$("userNotificationsTable").add(row);
    }
    $$("userNotificationsTable").refresh();
}