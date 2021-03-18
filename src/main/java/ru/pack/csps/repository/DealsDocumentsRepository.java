package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.DealsDocuments;
import ru.pack.csps.entity.DocumentsTypes;
import ru.pack.csps.entity.States;
import ru.pack.csps.entity.Users;

public interface DealsDocumentsRepository extends CrudRepository<DealsDocuments, Long> {
    DealsDocuments findFirstByDealDocUserAndDealDocTypeAndDealDocState(Users users, DocumentsTypes dt, States states);
}
