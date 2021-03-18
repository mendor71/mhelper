package ru.pack.csps.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.Cards;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.CardsRepository;
import ru.pack.csps.repository.StatesRepository;
import ru.pack.csps.repository.UsersRepository;
import ru.pack.csps.service.EncryptorService;
import ru.pack.csps.service.SettingsService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class CardsService {
    private UsersRepository usersRepository;
    private CardsRepository cardsRepository;
    private StatesRepository statesRepository;
    private SettingsService settingsService;
    private EncryptorService encryptorService;

    public Cards findCardByUserId(Long userId, String currentUserName, boolean isAdmin) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, PropertyFindException {
        if (userId == null) { throw new IllegalArgumentException("Параметр userId не может иметь значение null"); }
        if (currentUserName == null) { throw new IllegalArgumentException("Параметр currentUserName не может иметь значение null"); }

        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значение параметра currentUserName"); }

        Users findUser = userId == -1 ? currentUser : usersRepository.findOne(userId);

        if (!isAdmin && !currentUser.equals(findUser)) { throw new AccessDeniedException("Недостаточно привилегий"); }

        Cards cards = findUser.getUserCardId();

        if (cards == null) { return new Cards(); }

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.decrypt(cards);
        }

        return cards;
    }

    public Cards addCardToUser(Long userId, Cards cards, String currentUserName) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, PropertyFindException {
        if (userId == null) { throw new IllegalArgumentException("Параметр userId не может иметь значение null"); }
        if (currentUserName == null) { throw new IllegalArgumentException("Параметр currentUserName не может иметь значение null"); }
        if (cards == null) { throw new IllegalArgumentException("Параметр cards не может иметь значение null"); }

        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значение параметра currentUserName"); }

        Users findUser = userId == -1 ? currentUser : usersRepository.findOne(userId);

        if (findUser == null) { throw new InvalidValueException("User not found by ID: " + userId); }
        if (!findUser.equals(currentUser)) { throw new AccessDeniedException("Недостаточно привилегий");  }

        cards.setCardStateId(statesRepository.findOne((Integer) settingsService.getProperty("card_on_moderation_state")));

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.encrypt(cards);
        }

        cards = cardsRepository.save(cards);
        findUser.setUserCardId(cards);
        usersRepository.save(findUser);

        return cards;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @Autowired
    public void setCardsRepository(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }
    @Autowired
    public void setStatesRepository(StatesRepository statesRepository) {
        this.statesRepository = statesRepository;
    }
    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @Autowired
    public void setEncryptorService(EncryptorService encryptorService) {
        this.encryptorService = encryptorService;
    }
}
