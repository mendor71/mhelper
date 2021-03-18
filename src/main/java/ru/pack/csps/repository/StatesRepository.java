package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.States;

public interface StatesRepository extends CrudRepository<States, Integer> {
    States findStatesByStateName(String stateName);
}
