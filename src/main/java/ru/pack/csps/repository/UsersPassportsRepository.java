package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.UserPassports;

public interface UsersPassportsRepository extends CrudRepository<UserPassports, Long> {
}
