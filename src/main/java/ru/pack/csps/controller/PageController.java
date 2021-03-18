package ru.pack.csps.controller;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pack.csps.entity.Users;

import java.util.List;

/**
 * Created by Gushchin-AA1 on 08.09.2017.
 */
@Controller
public class PageController {

    @RequestMapping(value = "testFileUpload")
    public String getFileUploadPage() {
        return "fileUploadPage";
    }

    @RequestMapping(value = "test")
    public String getTestPage() {
        return "test";
    }

    @RequestMapping(value = "test/payment")
    public String getTestPaymentPage(){return "paymentTest";}

}
