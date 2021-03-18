package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.Deals;
import ru.pack.csps.entity.DealsBorrowersRelations;
import ru.pack.csps.entity.States;
import ru.pack.csps.entity.Users;

public interface DealsBorrowerRelationsRepository extends CrudRepository<DealsBorrowersRelations, Long> {
    DealsBorrowersRelations findByDbrBorrowerUserAndDbrDealAndDbrState(Users borrowerUser, Deals deal, States states);
}
