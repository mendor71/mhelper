package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.ClosureCodes;

public interface ClosureCodesRepository extends CrudRepository<ClosureCodes, Long> {
    ClosureCodes findClosureCodesByCloCodeName(String cloCodeName);
}
