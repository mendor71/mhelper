//package ru.pack.csps.repository;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import ru.pack.csps.entity.InvestRequests;
//
//import java.util.List;
//
///**
// * Created by Mendor on 05.04.2018.
// */
//public interface InvestRequestsRepository extends CrudRepository<InvestRequests, Long> {
//    List<InvestRequests> findInvestRequestsByIrInitUserIdUserId(Long userId);
//    List<InvestRequests> findInvestRequestsByIrInitUserIdUserIdAndIrStateIdStateId(Long userId, Integer stateId);
//
//    @Query(value = "select " +
//            "* " +
//            "from invest_requests ir " +
//            "where " +
//            "ir.ir_loan_term between ?1 - ?2 and ?1 + ?2 " +
//            "and ir.ir_invest_amount >= ?3 " +
//            //"and ir.ir_refund_amount >= :refundAmount " +
//            "and ?4 between ir.ir_age_limit_min and ir.ir_age_limit_max " +
//            "and ?5 >= ir.ir_min_user_rate " +
//            //"and ir.ir_region_criteria = :userRegionCriteria " +
//            "and not exists (select * from invest_loan_requests where ilr_inv_req_id = ir.ir_id ) limit 5", nativeQuery = true)
//    List<InvestRequests> findInvestRequestsForUser_H(Integer loanTerm, Integer appTermRange, Double loanAmount, Integer userAge, Double userRate);
//
//    @Query(value = "select " +
//            "* " +
//            "from invest_requests ir " +
//            "where " +
//            "ir.ir_loan_term between ?1 - ?2 and ?1 + ?2 " +
//            "and ir.ir_invest_amount < ?3 " +
//            //"and ir.ir_refund_amount <= :refundAmount " +
//            "and ?4 between ir.ir_age_limit_min and ir.ir_age_limit_max " +
//            "and ?5 >= ir.ir_min_user_rate " +
//            //"and ir.ir_region_criteria = :userRegionCriteria " +
//            "and not exists (select * from invest_loan_requests where ilr_inv_req_id = ir.ir_id ) limit 5", nativeQuery = true)
//    List<InvestRequests> findInvestRequestsForUser_L(Integer loanTerm, Integer appTermRange, Double loanAmount, Integer userAge, Double userRate);
//
//}
