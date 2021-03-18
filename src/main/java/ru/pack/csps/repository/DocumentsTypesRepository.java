package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.DocumentsTypes;

public interface DocumentsTypesRepository extends CrudRepository<DocumentsTypes, Integer> {
    DocumentsTypes findByDtName(String dtName);
}
