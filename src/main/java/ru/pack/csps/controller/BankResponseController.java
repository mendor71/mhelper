package ru.pack.csps.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pack.csps.entity.PaymentLogs;
import ru.pack.csps.repository.PaymentLogsRepository;
import ru.pack.csps.util.BankProperties;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/payment")
public class BankResponseController {
    private BankProperties bankProperties;
    private PaymentLogsRepository paymentLogsRepository;

    @RequestMapping(value = "/callback")
    public String paymentCallback(@RequestParam(value = "tid", required = false) String tid
            , @RequestParam(value = "name", required = false) String name
            , @RequestParam(value = "card_binding_id", required = false) String card_binding_id
            , @RequestParam(value = "comment", required = false) String comment
            , @RequestParam(value = "partner_id", required = false) String partner_id
            , @RequestParam(value = "service_id", required = false) String service_id
            , @RequestParam(value = "order_id", required = false) String order_id
            , @RequestParam(value = "type", required = false) String type
            , @RequestParam(value = "currency", required = false) String currency
            , @RequestParam(value = "partner_income", required = false) String partner_income
            , @RequestParam(value = "system_income", required = false) String system_income
            , @RequestParam(value = "test", required = false) String test
            , @RequestParam(value = "check", required = false) String check
            , @RequestParam(value = "phone_number", required = false) String phone_number
            , @RequestParam(value = "email", required = false) String email, HttpServletResponse response) throws IOException {

        name = name != null ? name : "";
        comment = comment != null ? comment : "";

        byte[] nameBytes = name.getBytes("ISO-8859-1");
        name = new String(nameBytes, "UTF-8");

        byte[] commentBytes = comment.getBytes("ISO-8859-1");
        comment = new String(commentBytes, "UTF-8");

        JSONObject o = new JSONObject();
        o.put("tid", tid);
        o.put("card_binding_id", card_binding_id);
        o.put("name", name);
        o.put("comment", comment);
        o.put("partner_id", partner_id);
        o.put("service_id", service_id);
        o.put("order_id", order_id);
        o.put("type", type);
        o.put("currency", currency);
        o.put("partner_income", partner_income);
        o.put("system_income", system_income);
        o.put("test", test);
        o.put("check", check);
        o.put("phone_number", phone_number);
        o.put("email", email);

        PaymentLogs plTra = new PaymentLogs(tid, o.toJSONString());
        PaymentLogs plExternal = new PaymentLogs(tid,"request md5: " + check);
        PaymentLogs plStringToHash = new PaymentLogs(tid, "to hash:" + ((tid != null ? tid : "")
                + name
                + comment
                + (partner_id != null ? partner_id : "")
                + (service_id != null ? service_id : "")
                + (order_id != null ? order_id : "")
                + (type != null ? type : "")
                + (partner_income != null ? partner_income : "")
                + (system_income != null ? system_income : "")
                + (test != null ? test : "")
                + bankProperties.getSecretApiKey()));


        String md5hash = DigestUtils.md5DigestAsHex(((tid != null ? tid : "")
                + name
                + comment
                + (partner_id != null ? partner_id : "")
                + (service_id != null ? service_id : "")
                + (order_id != null ? order_id : "")
                + (type != null ? type : "")
                + (partner_income != null ? partner_income : "")
                + (system_income != null ? system_income : "")
                + (test != null ? test : "")
                + bankProperties.getSecretApiKey()).getBytes("UTF-8"));
        PaymentLogs plInternal = new PaymentLogs(tid, "generated md5: " + md5hash);

        paymentLogsRepository.save(plTra);
        paymentLogsRepository.save(plStringToHash);
        paymentLogsRepository.save(plExternal);
        paymentLogsRepository.save(plInternal);

        if (md5hash.equals(check)) {
            PaymentLogs ok = new PaymentLogs(tid,"return 200");
            paymentLogsRepository.save(ok);
            return "response";
        } else {
            PaymentLogs err = new PaymentLogs( tid, "return 400");
            paymentLogsRepository.save(err);
            response.setStatus(400);
            return "response";
        }
    }

    @RequestMapping(value = "/success")
    public String paymentSuccess(ModelMap modelMap) {
        modelMap.addAttribute("response", "payment success!");
        return "response";
    }

    @RequestMapping(value = "/error")
    public String paymentError(ModelMap modelMap) {
        modelMap.addAttribute("response", "payment error!");
        return "response";
    }

    @Autowired
    public void setBankProperties(BankProperties bankProperties) {
        this.bankProperties = bankProperties;
    }
    @Autowired
    public void setPaymentLogsRepository(PaymentLogsRepository paymentLogsRepository) {
        this.paymentLogsRepository = paymentLogsRepository;
    }
}
