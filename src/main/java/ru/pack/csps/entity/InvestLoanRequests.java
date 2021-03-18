package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invest_loan_requests")
public class InvestLoanRequests extends ResourceSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ilr_id")
    private Long ilrId;
    @Column(name = "ilr_invest_sum")
    private Double ilrInvestSum;
    @Column(name = "ilr_refund_sum")
    private Double ilrRefundSum;
    @Column(name = "ilr_term")
    private Integer ilrTerm;
    @Column(name = "ilr_investor_confirmed")
    private Boolean ilrInvestorConfirmed;
    @Column(name = "ilr_borrower_confirmed")
    private Boolean ilrBorrowerConfirmed;
    @Column(name = "ilr_reg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ilrRegDate;
    @Column(name = "ilr_dead_line")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ilrDeadLine;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "ilr_loan_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users ilrLoanUsers;

    @JoinColumn(name = "ilr_inv_req_id", referencedColumnName = "ir_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private InvestRequests ilrInvestRequests;

    @JoinColumn(name = "ilr_state_id", referencedColumnName = "state_id")
    @Fetch(FetchMode.JOIN)
    @ManyToOne(optional = false)
    private States ilrStateId;

    @JoinColumn(name = "ilr_clo_code_id", referencedColumnName = "clo_code_id")
    @ManyToOne()
    private ClosureCodes ilrClosureCodeId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "traIlrId", fetch = FetchType.LAZY)
    private List<Transactions> transactionsList;

    public InvestLoanRequests() {
    }

    public InvestLoanRequests(Double ilrInvestSum, Double ilrRefundSum, Integer ilrTerm, Date ilrRegDate, Users ilrLoanUsers, InvestRequests ilrInvestRequests, States ilrStateId) {
        this.ilrInvestSum = ilrInvestSum;
        this.ilrRefundSum = ilrRefundSum;
        this.ilrTerm = ilrTerm;
        this.ilrRegDate = ilrRegDate;
        this.ilrLoanUsers = ilrLoanUsers;
        this.ilrInvestRequests = ilrInvestRequests;
        this.ilrStateId = ilrStateId;
    }

    public Long getIlrId() {
        return ilrId;
    }

    public void setIlrId(Long ilrId) {
        this.ilrId = ilrId;
    }

    public InvestRequests getIlrInvestRequests() {
        return ilrInvestRequests;
    }

    public void setIlrInvestRequests(InvestRequests ilrInvestRequests) {
        this.ilrInvestRequests = ilrInvestRequests;
    }

    public States getIlrStateId() {
        return ilrStateId;
    }

    public void setIlrStateId(States ilrStateId) {
        this.ilrStateId = ilrStateId;
    }

    public Double getIlrInvestSum() {
        return ilrInvestSum;
    }

    public void setIlrInvestSum(Double ilrInvestSum) {
        this.ilrInvestSum = ilrInvestSum;
    }

    public Double getIlrRefundSum() {
        return ilrRefundSum;
    }

    public void setIlrRefundSum(Double ilrRefundSum) {
        this.ilrRefundSum = ilrRefundSum;
    }

    public Integer getIlrTerm() {
        return ilrTerm;
    }

    public void setIlrTerm(Integer ilrTerm) {
        this.ilrTerm = ilrTerm;
    }

    public List<Transactions> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<Transactions> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public Boolean getIlrInvestorConfirmed() {
        return ilrInvestorConfirmed;
    }

    public void setIlrInvestorConfirmed(Boolean ilrInvestorConfirmed) {
        this.ilrInvestorConfirmed = ilrInvestorConfirmed;
    }

    public Boolean getIlrBorrowerConfirmed() {
        return ilrBorrowerConfirmed;
    }

    public void setIlrBorrowerConfirmed(Boolean ilrBorrowerConfirmed) {
        this.ilrBorrowerConfirmed = ilrBorrowerConfirmed;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getIlrRegDate() {
        return ilrRegDate;
    }

    public void setIlrRegDate(Date ilrRegDate) {
        this.ilrRegDate = ilrRegDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getIlrDeadLine() {
        return ilrDeadLine;
    }

    public void setIlrDeadLine(Date ilrDeadLine) {
        this.ilrDeadLine = ilrDeadLine;
    }


    public Users getIlrLoanUsers() {
        return ilrLoanUsers;
    }

    public void setIlrLoanUsers(Users ilrLoanUsers) {
        this.ilrLoanUsers = ilrLoanUsers;
    }

    public ClosureCodes getIlrClosureCodeId() {
        return ilrClosureCodeId;
    }

    public void setIlrClosureCodeId(ClosureCodes ilrClosureCodeId) {
        this.ilrClosureCodeId = ilrClosureCodeId;
    }
}
