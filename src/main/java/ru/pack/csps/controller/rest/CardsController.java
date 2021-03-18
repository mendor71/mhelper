package ru.pack.csps.controller.rest;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pack.csps.entity.Cards;
import ru.pack.csps.exceptions.InvalidValueException;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.service.dao.CardsService;
import ru.pack.csps.util.IncludeAPI;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static ru.pack.csps.security.app.AccessResolver.isAdmin;
import static ru.pack.csps.util.JSONResponse.createOKResponse;

@RestController
@RequestMapping(value = "/cards")
public class CardsController {
    private CardsService cardsService;

    @IncludeAPI
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    private Cards findCardByUserId(@PathVariable(value = "userId") Long userId, Authentication authentication) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, PropertyFindException {
        return cardsService.findCardByUserId(userId, authentication.getPrincipal().toString(), isAdmin(authentication));
    }

    @IncludeAPI
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.POST)
    private JSONAware addCardToUser(@PathVariable(value = "userId") Long userId, @RequestBody Cards cards, Authentication authentication) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidValueException, PropertyFindException {
        cardsService.addCardToUser(userId, cards, authentication.getPrincipal().toString());
        return createOKResponse("Карта успешно привязана к пользователю");
    }

    @Autowired
    public void setCardsService(CardsService cardsService) {
        this.cardsService = cardsService;
    }
}
