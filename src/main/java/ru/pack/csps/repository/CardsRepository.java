package ru.pack.csps.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.Cards;

/**
 * Created by Mendor on 05.04.2018.
 */
public interface CardsRepository extends CrudRepository<Cards, Long> {

}
