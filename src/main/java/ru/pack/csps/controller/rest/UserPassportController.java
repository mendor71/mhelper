package ru.pack.csps.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pack.csps.entity.UserPassports;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.service.PassportsService;
import ru.pack.csps.util.IncludeAPI;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static ru.pack.csps.security.app.AccessResolver.isAdmin;
import static ru.pack.csps.util.JSONResponse.createOKResponse;

@RestController
@RequestMapping(value = "/passport")
public class UserPassportController {
    private PassportsService passportsService;

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public UserPassports getUserPassport(@PathVariable Long userId, Authentication authentication) throws PropertyFindException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException {
        return passportsService.getUserPassport(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
    public JSONAware setUserPassport(@PathVariable Long userId, @RequestBody UserPassports userPassports, Authentication authentication) throws PropertyFindException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, JsonProcessingException {
        UserPassports up = passportsService.addUserPassport(userId, authentication.getPrincipal().toString(), userPassports);
        return createOKResponse("Данные паспорта сохранены", up);
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT)
    public JSONAware updateUserPassport(@PathVariable Long userId, @RequestBody UserPassports userPassports, Authentication authentication) throws PropertyFindException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, JsonProcessingException {
        UserPassports up = passportsService.updateUserPassport(userId, authentication.getPrincipal().toString(), userPassports);
        return createOKResponse("Данные паспорта обновлены", up);
    }

    @Autowired
    public void setPassportsService(PassportsService passportsService) {
        this.passportsService = passportsService;
    }
}
