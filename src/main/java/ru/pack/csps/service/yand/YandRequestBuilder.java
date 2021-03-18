package ru.pack.csps.service.yand;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class YandRequestBuilder {

    public JSONObject buildPaymentRequest(String token, Double amount) {
        JSONObject pObj = new JSONObject();
        pObj.put("capture", "true");
        pObj.put("payment_token", token);
        pObj.put("save_payment_method", "true");

        JSONObject amountObj = new JSONObject();
        amountObj.put("value", String.format( "%.2f", amount ).replace(",","."));
        amountObj.put("currency", "RUB");

        pObj.put("amount", amountObj);

        System.out.printf(pObj.toJSONString());
        return pObj;
    }
}
