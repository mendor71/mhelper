package pu.pack.csps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;
import ru.pack.csps.entity.Cards;
import ru.pack.csps.entity.States;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.repository.CardsRepository;
import ru.pack.csps.repository.StatesRepository;
import ru.pack.csps.repository.UsersRepository;
import ru.pack.csps.service.EncryptorService;
import ru.pack.csps.util.IEncrypted;
import ru.pack.csps.service.SettingsService;
import ru.pack.csps.service.dao.CardsService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CardsServiceTestMockito {
    @Mock private UsersRepository usersRepository;
    @Mock private CardsRepository cardsRepository;
    @Mock private StatesRepository statesRepository;
    @Mock private SettingsService settingsService;
    @Mock private EncryptorService encryptorService;

    private CardsService cardsService;
    private Users testUserWithoutCard = new Users(42L);
    private Users testUserWithCard = new Users(43L);
    private Cards testCard = new Cards(1L);

    @Before
    public void testInit() throws Exception {
        testUserWithCard.setUserCardId(testCard);

        when(usersRepository.findUsersByUserName("user_without_card")).thenReturn(testUserWithoutCard);
        when(usersRepository.findUsersByUserName("user_with_card")).thenReturn(testUserWithCard);

        when(usersRepository.findOne(-1L)).thenReturn(testUserWithoutCard);
        when(usersRepository.findOne(42L)).thenReturn(testUserWithoutCard);

        when(usersRepository.findOne(43L)).thenReturn(testUserWithCard);

        when(settingsService.getProperty("encrypt_enable")).thenReturn(new Boolean(true));
        when(settingsService.getProperty("card_on_moderation_state")).thenReturn(1);

        when(statesRepository.findOne(1)).thenReturn(new States(1));
        when(cardsRepository.save(testCard)).thenReturn(testCard);

        doThrow(new NullPointerException()).when(encryptorService).decrypt((IEncrypted) null);

        cardsService = new CardsService();
        cardsService.setCardsRepository(cardsRepository);
        cardsService.setEncryptorService(encryptorService);
        cardsService.setStatesRepository(statesRepository);
        cardsService.setUsersRepository(usersRepository);
        cardsService.setSettingsService(settingsService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCardByUserId_userIdIsNull() throws Exception {
        cardsService.findCardByUserId(null, "user_with_card", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCardByUserId_currentUserNameIsNull() throws Exception {
        cardsService.findCardByUserId(-1L, null, false);
    }

    @Test(expected = InvalidValueException.class)
    public void testFindCardByUserId_notExistsUser() throws Exception {
        cardsService.findCardByUserId(-1L, "not_exists_user", false);
    }

    @Test
    public void testFindCardByUserId_cardNotExists() throws Exception {
        Cards cards = cardsService.findCardByUserId(-1L, "user_without_card", true);
        assertNull(cards.getCardId());
    }

    @Test
    public void testFindCardByUserId_cardExists_cardOwner() throws Exception {
        Cards cards = cardsService.findCardByUserId(43L, "user_with_card", false);
        assertNotNull(cards);
    }

    @Test
    public void testFindCardByUserId_cardExists_notCardOwner_admin() throws Exception {
        Cards cards = cardsService.findCardByUserId(43L, "user_without_card", true);
        assertNotNull(cards);
    }

    @Test(expected = AccessDeniedException.class)
    public void testFindCardByUserId_cardExists_notCardOwner_notAdmin() throws Exception {
        cardsService.findCardByUserId(43L, "user_without_card", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCardToUser_userIdIsNull() throws Exception {
        cardsService.addCardToUser(null, testCard, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCardToUser_cardIsNull() throws Exception {
        cardsService.addCardToUser(-1L, null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCardToUser_currentUserNameIsNull() throws Exception {
        cardsService.addCardToUser(-1L, testCard, null);
    }

    @Test(expected = InvalidValueException.class)
    public void testAddCardToUser_notExistsUser() throws Exception {
        cardsService.addCardToUser(-100L, testCard, "not_exists_user");
    }

    @Test(expected = AccessDeniedException.class)
    public void testAddCardToUser_notCardOwner() throws Exception {
        cardsService.addCardToUser(42L, testCard, "user_with_card");
    }

    @Test
    public void testAddCardToUser_cardOwner() throws Exception {
        Cards cards = cardsService.addCardToUser(42L, testCard, "user_without_card");
        assertNotNull(cards);
    }
}
