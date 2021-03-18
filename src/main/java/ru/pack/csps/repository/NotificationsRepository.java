package ru.pack.csps.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.pack.csps.entity.Notifications;

import java.util.List;

/**
 * Created by Mendor on 05.04.2018.
 */
public interface NotificationsRepository extends PagingAndSortingRepository<Notifications, Long> {
    List<Notifications> findNotificationsByNotifUserIdUserId(Long userId);
    List<Notifications> findNotificationsByNotifUserIdUserIdAndNotifStateIdStateId(Long userId, Integer stateId);

    Page<Notifications> findNotificationsByNotifUserIdUserId(Long userId, Pageable pageable);
    Page<Notifications> findNotificationsByNotifUserIdUserIdAndNotifStateIdStateId(Long userId, Integer stateId, Pageable pageable);
}
