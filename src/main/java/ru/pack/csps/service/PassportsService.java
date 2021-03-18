package ru.pack.csps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.UserPassports;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.UsersPassportsRepository;
import ru.pack.csps.repository.UsersRepository;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class PassportsService {
    private SettingsService settingsService;
    private EncryptorService encryptorService;
    private UsersPassportsRepository usersPassportsRepository;
    private UsersRepository usersRepository;

    private UserPassports createPassport(UserPassports userPassports) throws PropertyFindException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.encrypt(userPassports);
        }

        return usersPassportsRepository.save(userPassports);
    }

    private UserPassports updatePassport(UserPassports dbPassport, UserPassports newPassport) throws InvalidValueException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, PropertyFindException {
        dbPassport.setUpSeries(newPassport.getUpSeries());
        dbPassport.setUpNumber(newPassport.getUpNumber());
        dbPassport.setUpGivenBy(newPassport.getUpGivenBy());
        dbPassport.setUpGivenDate(newPassport.getUpGivenDate());
        dbPassport.setUpLocationAddress(newPassport.getUpLocationAddress());

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.encrypt(dbPassport);
        }

        return usersPassportsRepository.save(dbPassport);
    }

    public UserPassports getUserPassport(Long userId, String currentUserName, boolean isAdmin) throws InvalidValueException, PropertyFindException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значение параметра currentUserName"); }

        Users users = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (!currentUser.equals(users) && !isAdmin) { throw new AccessDeniedException("недостаточно привилегий"); }

        UserPassports passports = users.getUserPassports();

        if (passports == null) { return new UserPassports(); }

        if ((Boolean) settingsService.getProperty("encrypt_enable")) {
            encryptorService.decrypt(passports);
        }
        return passports;
    }

    public UserPassports addUserPassport(Long userId, String currentUserName, UserPassports userPassports) throws InvalidValueException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, PropertyFindException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значение параметра currentUserName"); }

        Users users = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (!currentUser.equals(users)) { throw new AccessDeniedException("недостаточно привилегий"); }

        userPassports = createPassport(userPassports);
        users.setUserPassports(userPassports);

        usersRepository.save(users);
        return userPassports;
    }

    public UserPassports updateUserPassport(Long userId, String currentUserName, UserPassports userPassports) throws PropertyFindException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException {
        Users currentUser = usersRepository.findUsersByUserName(currentUserName);
        if (currentUser == null) { throw new InvalidValueException("Передано некорректное значение параметра currentUserName"); }

        Users users = userId == -1 ? currentUser : usersRepository.findOne(userId);
        if (!currentUser.equals(users)) { throw new AccessDeniedException("недостаточно привилегий"); }

        if (users.getUserPassports() == null) { throw new InvalidValueException("К пользователю " + currentUserName + " не привязан паспорт"); }

        return updatePassport(users.getUserPassports(), userPassports);
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @Autowired
    public void setEncryptorService(EncryptorService encryptorService) {
        this.encryptorService = encryptorService;
    }
    @Autowired
    public void setUsersPassportsRepository(UsersPassportsRepository usersPassportsRepository) {
        this.usersPassportsRepository = usersPassportsRepository;
    }
    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
