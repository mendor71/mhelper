package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.DocumentsTypes;
import ru.pack.csps.entity.States;
import ru.pack.csps.entity.UserDocuments;
import ru.pack.csps.entity.Users;

/**
 * Created by Mendor on 01.07.2018.
 */
public interface UserDocumentsRepository extends CrudRepository<UserDocuments, Long> {
    UserDocuments findFirstByUserDocUserAndUserDocTypeAndUserDocState(Users user, DocumentsTypes dt, States states);
}
