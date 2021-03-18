//package ru.pack.csps.repository;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import ru.pack.csps.entity.Transactions;
//
//import java.util.List;
//
//public interface TransactionsRepository extends CrudRepository<Transactions, Integer> {
//    @Query("select t from Transactions t join t.traStateId s where s.stateId = :stateId")
//    List<Transactions> findByTraStateId(Integer stateId);
//}
