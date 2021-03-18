//package pu.pack.csps;
//
//import org.json.simple.JSONObject;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import ru.pack.csps.entity.Cards;
//import ru.pack.csps.exceptions.InvalidValueException;
//import ru.pack.csps.service.dao.CardsService;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
//public class CardsServiceTestWithContext {
//
//    @Autowired
//    private CardsService cardsService;
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testFindCardByUserId_UserIdIsNull() throws Exception {
//        cardsService.findCardByUserId(null, "userName", false);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testFindCardByUserId_CurrentUserNameIsNull() throws Exception {
//        cardsService.findCardByUserId(-1L, null, false);
//    }
//
//    @Test(expected = InvalidValueException.class)
//    public void testFindCardByUserId_NotExistsUser() throws Exception {
//        Cards cards = cardsService.findCardByUserId(-1L, "!!!)@!@1_mendor71", false);
//        assertNull(cards);
//    }
//
//    @Test(expected = AccessDeniedException.class)
//    public void testFindCardByUserId_NotOwnerNotAdmin() throws Exception {
//        cardsService.findCardByUserId(42L, "tester1", false);
//    }
//
//    @Test
//    public void testFindCardByUserId_NotOwnerAdmin() throws Exception {
//        Cards cards = cardsService.findCardByUserId(47L, "test01", true);
//        assertNull(cards);
//    }
//
//    @Test
//    public void testFindCardByUserId_CardOwnerNotExistsCard() throws Exception {
//        Cards cards = cardsService.findCardByUserId(47L, "test01", false);
//        assertNull(cards);
//    }
//
//    @Test
//    public void testFindCardByUserId_CardOwnerExistsCard() throws Exception {
//        Cards cards = cardsService.findCardByUserId(70L, "new_user", false);
//        assertNotNull(cards);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testAddCardToUser_UserIdIsNull() throws Exception {
//        cardsService.addCardToUser(null, new Cards(), "");
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testAddCardToUser_CardsIsNull() throws Exception {
//        cardsService.addCardToUser(-100L, null, "");
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testAddCardToUser_CurrentUserNameIsNull() throws Exception {
//        cardsService.addCardToUser(-100L, new Cards(), null);
//    }
//
//    @Test(expected = InvalidValueException.class)
//    public void testAddCardToUser_NotExistsUser() throws Exception {
//        Cards cards = cardsService.addCardToUser(-100L, new Cards(), "test");
//        assertNotNull(cards);
//    }
//
//    @Test(expected = AccessDeniedException.class)
//    public void testAddCardToUser_NotOwnerUser() throws Exception {
//        cardsService.addCardToUser(42L, new Cards(), "tester1");
//    }
//
//    @Test
//    public void testAddCardToUser_OwnerUser() throws Exception {
//        Cards cards = cardsService.addCardToUser(-1L, new Cards(), "test");
//        assertNotNull(cards);
//    }
//}
