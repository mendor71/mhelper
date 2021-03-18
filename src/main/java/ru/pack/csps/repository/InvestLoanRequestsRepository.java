//package ru.pack.csps.repository;
//
//import org.springframework.data.repository.CrudRepository;
//import ru.pack.csps.entity.InvestLoanRequests;
//
//import java.util.List;
//
///**
// * Created by Mendor on 05.04.2018.
// */
//public interface InvestLoanRequestsRepository extends CrudRepository<InvestLoanRequests,Long> {
//    List<InvestLoanRequests> findInvestLoanRequestsByIlrLoanUsersUserId(Long userId);
//    List<InvestLoanRequests> findInvestLoanRequestsByIlrLoanUsersUserIdAndIlrStateIdStateId(Long userId, Integer stateId);
//
//    List<InvestLoanRequests> findInvestLoanRequestsByIlrInvestRequestsIrInitUserIdUserId(Long userId);
//    List<InvestLoanRequests> findInvestLoanRequestsByIlrInvestRequestsIrInitUserIdUserIdAndIlrStateIdStateId(Long userId, Integer stateId);
//}
