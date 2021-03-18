package pu.pack.csps;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.pack.csps.entity.Notifications;
import ru.pack.csps.repository.NotificationsRepository;

import java.util.Iterator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class NotificationsRepositoryTest {
    @Autowired
    private NotificationsRepository notificationsRepository;

    @Ignore
    @Test
    public void testFindAllPageRequest() {
        PageRequest pageRequest = new PageRequest(0, 5);
        Iterable<Notifications> all = notificationsRepository.findAll(pageRequest);
        Iterator<Notifications> iter = all.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    @Ignore
    @Test
    public void testFindByUserIdPagable() {
        PageRequest pageRequest = new PageRequest(1, 1, Sort.Direction.DESC, "notifId");
        Iterable<Notifications> all = notificationsRepository.findNotificationsByNotifUserIdUserId(57L, pageRequest);
        Iterator<Notifications> iter = all.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
