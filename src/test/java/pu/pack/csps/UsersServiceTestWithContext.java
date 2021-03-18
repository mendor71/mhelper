package pu.pack.csps;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.service.dao.UsersService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class UsersServiceTestWithContext {

    @Autowired
    private UsersService usersService;

    @Ignore
    @Test
    public void testGetUsersByStateId() throws Exception {
        usersService.getUsersByStateId(null, false);
    }

    @Ignore
    @Test(expected = InvalidValueException.class)
    public void test() throws Exception {
        usersService.getUserById(null, null, false);
    }
}
