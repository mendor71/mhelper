package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.UserCustomFields;

public interface UserCustomFieldsRepository extends CrudRepository<UserCustomFields, Integer> {
}
