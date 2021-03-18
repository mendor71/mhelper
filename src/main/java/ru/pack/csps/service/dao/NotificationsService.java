package ru.pack.csps.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.Notifications;
import ru.pack.csps.entity.States;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.NotificationsRepository;
import ru.pack.csps.repository.StatesRepository;
import ru.pack.csps.repository.UsersRepository;
import ru.pack.csps.service.SettingsService;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

@Service
public class NotificationsService {
    @Autowired private NotificationsRepository notificationsRepository;
    @Autowired private UsersRepository usersRepository;
    @Autowired private StatesRepository statesRepository;
    @Autowired private SettingsService settingsService;

    public List<Notifications> getNotificationsByUserId(boolean read, Integer pageId, Long userId, String currentUserName) throws AccessDeniedException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        Users findUser = userId == -1 ? currentUser: usersRepository.findOne(userId);

        if (!currentUser.equals(findUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        PageRequest pageRequest = new PageRequest(pageId != null ? pageId : 0, 16, Sort.Direction.DESC, "notifId");
        Page<Notifications> notifPage;

        if (read) {
            notifPage = notificationsRepository.findNotificationsByNotifUserIdUserId(findUser.getUserId(), pageRequest);
        } else {
            notifPage = notificationsRepository.findNotificationsByNotifUserIdUserIdAndNotifStateIdStateId(findUser.getUserId(), (Integer) settingsService.getProperty("notif_not_read_state"), pageRequest);
        }

        return notifPage.getContent();
    }

    public List<Notifications> getNotificationsByUserId(Long userId, String currentUserName) throws AccessDeniedException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        Users findUser = userId == -1 ? currentUser : usersRepository.findOne(userId);

        if (!findUser.equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        return notificationsRepository.findNotificationsByNotifUserIdUserIdAndNotifStateIdStateId(userId, (Integer) settingsService.getProperty("notif_not_read_state"));
    }

    public Notifications setRead(Long notifId, String currentUserName) throws AccessDeniedException, PropertyFindException, InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);

        Notifications notifications = notificationsRepository.findOne(notifId);

        if (notifications == null) { throw new InvalidValueException("Уведомление не найдено по ID: " + notifId); }
        if (!currentUser.equals(notifications.getNotifUserId())) { throw new AccessDeniedException("Недостаточно привилегий"); }

        notifications.setNotifStateId(statesRepository.findOne((Integer) settingsService.getProperty("notif_read_state")));
        return notificationsRepository.save(notifications);
    }

    public void setRead(Long[] notifIdList, String currentUserName) throws AccessDeniedException, PropertyFindException, InvalidValueException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);

        States readState = statesRepository.findOne((Integer) settingsService.getProperty("notif_read_state"));

        for (Long notifId: notifIdList) {
            Notifications notifications = notificationsRepository.findOne(notifId);
            if (notifications == null) {
                throw new InvalidValueException("Уведомление не найдено по ID: " + notifId);
            }
            if (!currentUser.equals(notifications.getNotifUserId())) {
                throw new AccessDeniedException("Недостаточно привилегий");
            }

            notifications.setNotifStateId(readState);
            notificationsRepository.save(notifications);
        }
    }

    public Notifications addUserNotification(Long userId, Notifications notifications) throws PropertyFindException, InvalidValueException {
        Users users = usersRepository.findOne(userId);
        if (users == null) {
            throw new InvalidValueException("Пользователь на найден по ID: " + userId);
        }

        notifications.setNotifStateId(statesRepository.findOne((Integer) settingsService.getProperty("notif_not_read_state")));
        notifications.setNotifUserId(users);
        notifications.setNotifDate(new Date());

        return notificationsRepository.save(notifications);
    }

    public Notifications addUserNotification(Users users, String message) throws PropertyFindException {
        Notifications notifications = new Notifications();
        notifications.setNotifStateId(statesRepository.findOne((Integer) settingsService.getProperty("notif_not_read_state")));
        notifications.setNotifUserId(users);
        notifications.setNotifDate(new Date());
        notifications.setNotifText(message);

        return notificationsRepository.save(notifications);
    }
}
