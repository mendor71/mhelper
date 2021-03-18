package ru.pack.csps.service.dao;

import org.json.simple.JSONAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.States;
import ru.pack.csps.entity.Users;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.StatesRepository;
import ru.pack.csps.repository.UsersRepository;
import ru.pack.csps.service.SettingsService;

import java.io.IOException;

import static ru.pack.csps.util.JSONResponse.createERRResponse;
import static ru.pack.csps.util.JSONResponse.createNotFoundResponse;
import static ru.pack.csps.util.JSONResponse.createOKResponse;

@Service
public class StatesService {
    @Autowired private UsersRepository usersRepository;
    @Autowired private StatesRepository statesRepository;
    @Autowired private SettingsService settingsService;

    public Iterable<States> findAll() {
        return statesRepository.findAll();
    }

    public JSONAware confirmUserModeration(Long userId) throws IOException, PropertyFindException {
        Users users = usersRepository.findOne(userId);
        if (users == null) {
            return createNotFoundResponse("Пользователь на найден по ID: " + userId);
        }

        String errMessage = "";

        if (users.getUserCustomFields().getUcfPassport1() == null ) { errMessage += "Не загружена 1 страница паспорта!<br/>"; }
        if (users.getUserCustomFields().getUcfPassport2() == null) { errMessage += "Не загружена 2 страница паспорта!<br/>"; }
        /*if (users.getUserCardId() == null
                || !users.getUserCardId().getCardStateId().getStateId().equals((Integer) settingsService.getProperty("card_active_state"))) {
            errMessage += "Карта пользователя не подтверждена!<br/>";
        }*/

        if (errMessage.equals("")) {
            return setUserState(userId, (Integer) settingsService.getProperty("user_active_state"));
        }
        else { return createERRResponse(errMessage); }
    }

    public JSONAware setUserState(Long userId, Integer stateId) {
        Users users = usersRepository.findOne(userId);
        if (users == null) {
            return createNotFoundResponse("Пользователь на найден по ID: " + userId);
        }
        States states = statesRepository.findOne(stateId);
        if (states == null) {
            return createNotFoundResponse("Статус не найден по ID: " + stateId);
        }

        users.setUserStateId(states);
        usersRepository.save(users);

        return createOKResponse("Данные пользователя успешно обновлены");
    }
}
