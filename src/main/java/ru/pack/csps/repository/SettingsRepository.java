package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.Settings;

public interface SettingsRepository extends CrudRepository<Settings, Integer> {
    Settings findBySKey(String skey);
}
