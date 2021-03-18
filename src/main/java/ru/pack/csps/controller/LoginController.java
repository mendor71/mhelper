package ru.pack.csps.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by Gushchin-AA1 on 08.09.2017.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String loginPage(ModelMap modelMap) {
        return "login";
    }

    @RequestMapping(value = "/login_error", method = RequestMethod.GET)
    public final String displayLoginform(Model model, @RequestParam(value = "type") String type
            , @RequestParam(value = "text", required = false) String text
            , @RequestParam(value = "link", required = false) String link) {

        if (type.equals("badCredentials")) {
            model.addAttribute("error", "Логин или пароль введены неверно! <a href='" + link + "'>Сбросить пароль</a><br/>");
        } else if (type.equals("accountDisabled")) {
            model.addAttribute("error", "Ваша учетная запись отключена! Перейдите по ссылке для восстановления доступа: <a href='" + link + "'>Продлить доступ</a><br/>" );
        } else {
            model.addAttribute("error", "Авторизация не удалась, повторите попытку позже...<br/>");
        }

        return "login";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/admin")
    public String admin(ModelMap modelMap) {
        return "admin";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user")
    public String user(ModelMap modelMap, HttpSession session) {
        return "user";
    }

    @RequestMapping(value = "/403")
    public String err403(ModelMap modelMap) {
        return "403";
    }
}
