package ru.pack.csps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.pack.csps.entity.Roles;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.UsersRepository;
import ru.pack.csps.service.EncryptorService;
import ru.pack.csps.service.SettingsService;
import ru.pack.csps.service.dao.UsersService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

/**
 * Created by Gushchin-AA1 on 08.09.2017.
 */
@Controller
@RequestMapping("/")
public class MainController {
    private UsersRepository usersRepository;
    private SettingsService  settingsService;
    private EncryptorService encryptorService;
    private UsersService usersService;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(Principal principal, ModelMap modelMap) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException, InvalidValueException {
        if (principal != null) {
            Users user = usersRepository.findUsersByUserName(principal.getName());

            if ((Boolean) settingsService.getProperty("encrypt_enable")) {
                encryptorService.decrypt(user);
            }

            Roles roles = usersService.getCurrentUserDealsRole(user);

            String fullName = user.getUserFirstName() != null && user.getUserMiddleName() != null ? user.getUserFirstName() + " " + user.getUserMiddleName() : principal.getName();
            modelMap.addAttribute("currentUser", fullName);
            modelMap.addAttribute("currentRole", "Текущая роль: " + (roles.getRoleName().equals("ROLE_INVESTOR") ? "Инвестор" : roles.getRoleName().equals("ROLE_BORROWER") ? "Заемщик" : "Пользователь"));
        }
        return "index";
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @Autowired
    public void setEncryptorService(EncryptorService encryptorService) {
        this.encryptorService = encryptorService;
    }
    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
}
