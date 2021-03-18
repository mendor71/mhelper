package ru.pack.csps.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pack.csps.entity.Settings;
import ru.pack.csps.exceptions.PropertyFindException;
import ru.pack.csps.repository.SettingsRepository;

/**
 * Created by Mendor on 20.02.2018.
 */
@Service
public class SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;

    public Object getProperty(String key) throws PropertyFindException {
        if (key == null) { throw new IllegalArgumentException("key must be not null"); }

        Settings settings = settingsRepository.findBySKey(key);
        if (settings == null) { throw new PropertyFindException("Параметр не найден по ключу: " + key); }

        if (settings.getsDataType().equals("Integer")) {
            return Integer.parseInt(settings.getsValue());
        } else if (settings.getsDataType().equals("Long")) {
            return Long.parseLong(settings.getsValue());
        } else if (settings.getsDataType().equals("Boolean")) {
            return Boolean.parseBoolean(settings.getsValue());
        } else if (settings.getsDataType().equals("Double")) {
            return Double.parseDouble(settings.getsValue());
        } else {
            return settings.getsValue();
        }
    }
}
