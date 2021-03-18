package ru.pack.csps.controller.rest;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.pack.csps.entity.Notifications;
import ru.pack.csps.entity.Roles;
import ru.pack.csps.entity.States;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.FileUploadException;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.NotUniqueUserNameException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.service.SettingsService;
import ru.pack.csps.service.dao.UsersService;
import ru.pack.csps.util.IncludeAPI;
import ru.pack.csps.util.JSONResponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.net.URLDecoder.decode;
import static ru.pack.csps.security.app.AccessResolver.isAdmin;
import static ru.pack.csps.util.JSONResponse.*;

/**
 * Created by Mendor on 25.10.2017.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private UsersService usersService;
    private SettingsService settingsService;

    @IncludeAPI
    @RequestMapping("/checkExists")
    public JSONAware checkUserExists(@RequestParam(required = true, value = "userName") String userName, HttpServletRequest request) {
        return usersService.userExists(userName) ? JSONResponse.createOKResponse("User found") : JSONResponse.createNotFoundResponse("User not found");
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public Users getUserById(@PathVariable(value = "userId") Long userId, Authentication authentication) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalAccessException, PropertyFindException, InvalidValueException {
        return usersService.getUserById(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/roles")
    public List getUserRolesByUserId(@PathVariable(value = "userId") Long userId, Authentication authentication) throws InvalidValueException {
        return usersService.getUserRolesByUserId(userId, authentication.getPrincipal().toString());
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/deals_role")
    public Roles getCurrentUserDealsRole(@PathVariable Long userId, Authentication authentication) {
        try {
            return usersService.getCurrentUserDealsRole(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return null;
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/states/{stateId}")
    public List getUsersByStateId(@PathVariable(value = "stateId") Integer stateId, Authentication authentication) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalAccessException, PropertyFindException {
        return usersService.getUsersByStateId(stateId, isAdmin(authentication));
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/states")
    public States getUserStateByUserId(@PathVariable(value = "userId") Long userId, Authentication authentication) throws InvalidValueException {
        return usersService.getUserState(userId, authentication.getPrincipal().toString());
    }

    @IncludeAPI
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_USER_ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/passport/checkUpload/{pageId}")
    public JSONAware checkPassportUploadedByUserIdAndPageId(@PathVariable(value = "userId") Long userId, @PathVariable(value = "pageId") Integer pageId, Authentication authentication) throws IOException {
        try {
            boolean uploaded = usersService.checkPassportUploaded(userId, pageId, authentication.getPrincipal().toString(), isAdmin(authentication));
            return uploaded ? createOKResponse("found") : createNotFoundResponse("not_found");
        } catch (InvalidValueException | IllegalArgumentException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/passport/download/{pageId}")
    public void getPassportPageByUserIdAndPageId(@PathVariable(value = "userId") Long userId, @PathVariable(value = "pageId") Integer pageId, HttpServletResponse response, Authentication authentication) throws IOException, PropertyFindException, InvalidValueException {
        usersService.getPassportPage(userId, pageId, response, authentication.getPrincipal().toString(), isAdmin(authentication));
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/notifications/all")
    public List<Notifications> getAllUserNotificationsByUserId(@PathVariable(value = "userId") Long userId, Authentication authentication) throws InvalidValueException {
        return usersService.getAllUserNotifications(userId, authentication.getPrincipal().toString());
    }

    @IncludeAPI
    @RequestMapping(method = RequestMethod.POST)
    public JSONAware createUser(@RequestBody Users user) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException {
        try {
            usersService.create(user);
            return createOKResponse("Регистрация пользователя успешно завершена!");
        } catch (NotUniqueUserNameException | InvalidValueException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST, value = "/{userId}/passport/upload/{pageId}")
    public JSONAware uploadPassport(@PathVariable(value = "userId") Long userId, @PathVariable(value = "pageId") Integer pageId
            , MultipartHttpServletRequest fileRequest, Authentication authentication) throws IOException, PropertyFindException {
        try {
            usersService.uploadPassport(userId, pageId, fileRequest, authentication.getPrincipal().toString());
            return createOKResponse("Страница паспорта " + pageId + " успешно загружена");
        } catch (FileUploadException |InvalidValueException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_USER_ADMIN','ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
    public JSONAware updateUser(@PathVariable(value = "userId") Long userId, @RequestBody Users users, Authentication authentication) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, PropertyFindException {
        return usersService.change(userId, users, authentication.getPrincipal().toString(), isAdmin(authentication));
    }

    @IncludeAPI
    @RequestMapping(value = "/pwd_reset", method = RequestMethod.POST)
    public JSONAware createRestorePasswordUrl(@RequestParam(value = "userMail") String userMail, HttpServletRequest request) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, MessagingException, PropertyFindException {
        try {
            String path = "http://localhost:8080/MoneyHelper/password_reset/";
            String resPath = request.getServerName().equals("localhost") ? path : (String) settingsService.getProperty("pwd_reset_url");

            usersService.createRestorePasswordUrl(userMail, resPath);
            return createOKResponse("Ссылка для восстановления пароля направлена на указанный адрес");
        } catch (InvalidValueException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @RequestMapping(value = "/pwd_reset", method = RequestMethod.PUT)
    public JSONAware setNewUserPassword(@RequestBody String newPassword, Authentication authentication) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, MessagingException {
        try {
            newPassword = decode(newPassword, "UTF-8");
            usersService.changeUserPassword(newPassword, authentication.getPrincipal().toString());
            return createOKResponse("Пароль пользователя успешно изменен");
        } catch (InvalidValueException e) {
            return createERRResponse(e.getMessage());
        }
    }

    @Autowired
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
}
