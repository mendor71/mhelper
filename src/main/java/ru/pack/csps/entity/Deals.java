package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "public.deals")
public class Deals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deal_id")
    private Long dealId;
    @Column(name = "deal_term_days")
    private Integer dealTermDays;
    @Column(name = "deal_given_sum")
    private Double dealGivenSum;
    @Column(name = "deal_refund_sum")
    private Double dealRefundSum;
    @Column(name = "deal_commission")
    private Double dealCommission;
    @Column(name = "deal_investor_confirmed")
    private Boolean dealInvestorConfirmed;
    @Column(name = "deal_dead_line")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dealDeadLine;
    @Column(name = "deal_reg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dealRegDate;
    @Column(name = "deal_conclusion_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dealConclusionDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "deal_investor_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users dealInvestorUser;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "deal_borrower_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users dealBorrowerUser;
    @JoinColumn(name = "deal_state_id", referencedColumnName = "state_id")
    @ManyToOne
    private States dealState;
    @JoinColumn(name = "deal_clo_code_id", referencedColumnName = "clo_code_id")
    @ManyToOne
    private ClosureCodes dealClosureCode;

    public Deals() {
    }

    public Deals(Long dealId) {
        this.dealId = dealId;
    }

    public Deals(Long dealId, Integer dealTermDays, Double dealGivenSum, Double dealRefundSum) {
        this.dealId = dealId;
        this.dealTermDays = dealTermDays;
        this.dealGivenSum = dealGivenSum;
        this.dealRefundSum = dealRefundSum;
    }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

    public Integer getDealTermDays() {
        return dealTermDays;
    }

    public void setDealTermDays(Integer dealTermDays) {
        this.dealTermDays = dealTermDays;
    }

    public Double getDealGivenSum() {
        return dealGivenSum;
    }

    public void setDealGivenSum(Double dealGivenSum) {
        this.dealGivenSum = dealGivenSum;
    }

    public Double getDealRefundSum() {
        return dealRefundSum;
    }

    public void setDealRefundSum(Double dealRefundSum) {
        this.dealRefundSum = dealRefundSum;
    }

    public Double getDealCommission() {
        return dealCommission;
    }

    public void setDealCommission(Double dealCommission) {
        this.dealCommission = dealCommission;
    }

    public Boolean getDealInvestorConfirmed() {
        return dealInvestorConfirmed;
    }

    public void setDealInvestorConfirmed(Boolean dealInvestorConfirmed) {
        this.dealInvestorConfirmed = dealInvestorConfirmed;
    }

    public Date getDealDeadLine() {
        return dealDeadLine;
    }

    public void setDealDeadLine(Date dealDeadLine) {
        this.dealDeadLine = dealDeadLine;
    }

    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    public Date getDealRegDate() {
        return dealRegDate;
    }

    public void setDealRegDate(Date dealRegDate) {
        this.dealRegDate = dealRegDate;
    }
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    public Date getDealConclusionDate() {
        return dealConclusionDate;
    }

    public void setDealConclusionDate(Date dealConclusionDate) {
        this.dealConclusionDate = dealConclusionDate;
    }

    public Users getDealInvestorUser() {
        return dealInvestorUser;
    }

    public void setDealInvestorUser(Users dealInvestorUser) {
        this.dealInvestorUser = dealInvestorUser;
    }

    public Users getDealBorrowerUser() {
        return dealBorrowerUser;
    }

    public void setDealBorrowerUser(Users dealBorrowerUser) {
        this.dealBorrowerUser = dealBorrowerUser;
    }

    public States getDealState() {
        return dealState;
    }

    public void setDealState(States dealState) {
        this.dealState = dealState;
    }

    public ClosureCodes getDealClosureCode() {
        return dealClosureCode;
    }

    public void setDealClosureCode(ClosureCodes dealClosureCode) {
        this.dealClosureCode = dealClosureCode;
    }
}
