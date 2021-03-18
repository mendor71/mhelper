package ru.pack.csps.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.Deals;

import java.util.List;

public interface DealsRepository extends CrudRepository<Deals, Long> {
    @Query(value = "select " +
            "* " +
            "from deals d " +
            "left join deals_borrowers_relations dbr " +
            "   on dbr.dbr_deal_id = d.deal_id " +
            "   and dbr.dbr_borrower_user_id = d.deal_borrower_user_id " +
            "   and dbr.dbr_state_id = ?5 " +
            "where " +
            "d.deal_term_days between ?1 - ?2 and ?1 + ?2 " +
            "and d.deal_given_sum >= ?3 " +
            "and d.deal_state_id = ?4 " +
            "and dbr.dbr_id is null " +
            "limit 5", nativeQuery = true)
    List<Deals> findDealsForUser_H(Integer dealTermDays, Integer termRangeDays, Double givenSum, Integer dealStateId, Integer dbrActiveState);
    @Query(value = "select " +
            "* " +
            "from deals d " +
            "left join deals_borrowers_relations dbr " +
            "   on dbr.dbr_deal_id = d.deal_id " +
            "   and dbr.dbr_borrower_user_id = d.deal_borrower_user_id " +
            "   and dbr.dbr_state_id = ?5 " +
            "where " +
            "d.deal_term_days between ?1 - ?2 and ?1 + ?2 " +
            "and d.deal_given_sum < ?3 " +
            "and d.deal_state_id = ?4 " +
            "and dbr.dbr_id is null " +
            "limit 5", nativeQuery = true)
    List<Deals> findDealsForUser_L(Integer dealTermDays, Integer termRangeDays, Double givenSum, Integer dealStateId, Integer dbrActiveState);

    List<Deals> findByDealInvestorUserUserId(Long userId);
    Page<Deals> findByDealInvestorUserUserId(Long userId, Pageable pageable);
    List<Deals> findByDealInvestorUserUserIdAndDealStateStateId(Long userId, Integer StateId);
    Page<Deals> findByDealInvestorUserUserIdAndDealStateStateId(Long userId, Integer StateId, Pageable pageable);

    List<Deals> findByDealBorrowerUserUserId(Long userId);
    Page<Deals> findByDealBorrowerUserUserId(Long userId, Pageable pageable);
    List<Deals> findByDealBorrowerUserUserIdAndDealStateStateId(Long userId, Integer StateId);
    Page<Deals> findByDealBorrowerUserUserIdAndDealStateStateId(Long userId, Integer StateId, Pageable pageable);

    Page<Deals> findByDealGivenSumGreaterThanEqualAndDealTermDaysBetweenAndDealStateStateId(Double givenSum, Integer minTerm, Integer maxTerm, Integer stateId, Pageable page);
    Page<Deals> findByDealGivenSumLessThanAndDealTermDaysBetweenAndDealStateStateId(Double givenSum, Integer minTerm, Integer maxTerm, Integer stateId, Pageable page);
}
