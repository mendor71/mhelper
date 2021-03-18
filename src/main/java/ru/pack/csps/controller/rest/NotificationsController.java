package ru.pack.csps.controller.rest;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pack.csps.entity.Notifications;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.service.dao.NotificationsService;
import ru.pack.csps.util.IncludeAPI;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static ru.pack.csps.util.JSONResponse.createERRResponse;
import static ru.pack.csps.util.JSONResponse.createOKResponse;

@RestController
@RequestMapping(value = "/notifications")
public class NotificationsController {
    private NotificationsService notificationsService;

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/paged/users/{userId}", method = RequestMethod.GET)
    public List<Notifications> getNotificationsByUserId(@RequestParam(value = "read") boolean read, @RequestParam(value = "pageId", required = false) Integer pageId, @PathVariable(value = "userId") Long userId, Authentication authentication) throws AccessDeniedException, PropertyFindException {
        return notificationsService.getNotificationsByUserId(read, pageId, userId, authentication.getPrincipal().toString());
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{notifId}/read", method = RequestMethod.POST)
    public JSONAware setRead(@PathVariable(value = "notifId") Long notifId, Authentication authentication) throws AccessDeniedException, PropertyFindException {
        try {
            notificationsService.setRead(notifId, authentication.getPrincipal().toString());
            return createOKResponse("Уведомление " + notifId + " помечено как прочитанное");
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public JSONAware setRead(@RequestParam(value = "notifIdList") Long[] notifIdList, Authentication authentication) throws AccessDeniedException, PropertyFindException {
        try {
            notificationsService.setRead(notifIdList, authentication.getPrincipal().toString());
            return createOKResponse("Уведомления помечены как прочитанные");
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @IncludeAPI
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/users/{userId}")
    public JSONAware addUserNotification(@PathVariable(value = "userId") Long userId, @RequestBody Notifications notifications) throws PropertyFindException {
        try {
            notificationsService.addUserNotification(userId, notifications);
            return createOKResponse("Уведомление успешно отправлено");
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return createERRResponse(e.getMessage());
        }
    }

    @Autowired
    public void setNotificationsService(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }
}
