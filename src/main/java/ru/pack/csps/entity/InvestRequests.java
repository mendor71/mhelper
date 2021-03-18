package ru.pack.csps.entity;

/**
 * Created by Mendor on 24.10.2017.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mendor
 */
@Entity
@Table(name = "invest_requests")
/*@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "InvestRequests.findAll", query = "SELECT i FROM InvestRequests i")
        , @NamedQuery(name = "InvestRequests.findByIrId", query = "SELECT i FROM InvestRequests i WHERE i.irId = :irId")
        , @NamedQuery(name = "InvestRequests.findByIrInvestAmount", query = "SELECT i FROM InvestRequests i WHERE i.irInvestAmount = :irInvestAmount")
        , @NamedQuery(name = "InvestRequests.findByIrRefundAmount", query = "SELECT i FROM InvestRequests i WHERE i.irRefundAmount = :irRefundAmount")
        , @NamedQuery(name = "InvestRequests.findByIrLoanTerm", query = "SELECT i FROM InvestRequests i WHERE i.irLoanTerm = :irLoanTerm")
        , @NamedQuery(name = "InvestRequests.findByIrLoanDeadline", query = "SELECT i FROM InvestRequests i WHERE i.irLoanDeadline = :irLoanDeadline")
        , @NamedQuery(name = "InvestRequests.findByIrCurrentInvestAmount", query = "SELECT i FROM InvestRequests i WHERE i.irCurrentInvestAmount = :irCurrentInvestAmount")
        , @NamedQuery(name = "InvestRequests.findByIrDivided", query = "SELECT i FROM InvestRequests i WHERE i.irDivided = :irDivided")
        , @NamedQuery(name = "InvestRequests.findActiveByUserIdAndStateId", query = "SELECT i FROM InvestRequests i " +
        "JOIN i.irInitUserId u " +
        "JOIN i.irStatId st " +
        "WHERE u.userId = :userId AND st.stateId = :stateId")
        , @NamedQuery(name = "InvestRequests.findActiveByUserIdAndNotEqualToStateId", query = "SELECT i FROM InvestRequests i " +
        "JOIN i.irInitUserId u " +
        "JOIN i.irStatId st " +
        "WHERE u.userId = :userId AND st.stateId != :stateId")
})
@NamedNativeQueries({
        @NamedNativeQuery(resultClass = InvestRequests.class, name = "Native.InvestRequests.findForMe_H", query = "select " +
                "* " +
                "from invest_requests ir " +
                "where " +
                "ir.ir_loan_term between :loanTerm - :appTermRange and :loanTerm + :appTermRange " +
                "and ir.ir_invest_amount >= :loanAmount " +
                //"and ir.ir_refund_amount >= :refundAmount " +
                "and :userAge between ir.ir_age_limit_min and ir.ir_age_limit_max " +
                "and :userRate >= ir.ir_min_user_rate " +
                //"and ir.ir_region_criteria = :userRegionCriteria " +
                "and not exists (select * from invest_loan_requests where ilr_inv_req_id = ir.ir_id ) limit 5")
        , @NamedNativeQuery(resultClass = InvestRequests.class, name = "Native.InvestRequests.findForMe_L", query = "select " +
                "* " +
                "from invest_requests ir " +
                "where " +
                "ir.ir_loan_term between :loanTerm - :appTermRange and :loanTerm + :appTermRange " +
                "and ir.ir_invest_amount < :loanAmount " +
                //"and ir.ir_refund_amount <= :refundAmount " +
                "and :userAge between ir.ir_age_limit_min and ir.ir_age_limit_max " +
                "and :userRate >= ir.ir_min_user_rate " +
                //"and ir.ir_region_criteria = :userRegionCriteria " +
                "and not exists (select * from invest_loan_requests where ilr_inv_req_id = ir.ir_id ) limit 5")
})*/
public class InvestRequests extends ResourceSupport implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ir_id")
    private Long irId;
    @Column(name = "ir_invest_amount")
    private Double irInvestAmount;
    @Column(name = "ir_refund_amount")
    private Double irRefundAmount;
    @Column(name = "ir_loan_term")
    private Integer irLoanTerm;
    @Column(name = "ir_loan_deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date irLoanDeadline;
    @Column(name = "ir_reg_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date irRegCreated;
    @Column(name = "ir_current_invest_amount")
    private Double irCurrentInvestAmount;
    @Column(name = "ir_divided")
    private Boolean irDivided;
    @Column(name = "ir_age_limit_min")
    private Integer irAgeLimitMin;
    @Column(name = "ir_age_limit_max")
    private Integer irAgeLimitMax;
    @Column(name = "ir_min_user_rate")
    private Double irMinUserRate;
    @Column(name = "ir_region_criteria")
    private Integer irRegionCriteria;
    @Column(name = "ir_max_parts")
    private Integer irMaxParts;

    @OneToMany(mappedBy = "irParentId", fetch = FetchType.LAZY)
    private List<InvestRequests> investRequestsList;

    @JoinColumn(name = "ir_parent_id", referencedColumnName = "ir_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private InvestRequests irParentId;

    @JoinColumn(name = "ir_stat_id", referencedColumnName = "state_id")
    @Fetch(FetchMode.JOIN)
    @ManyToOne
    private States irStateId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "ir_init_user_id", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users irInitUserId;

    public InvestRequests() {
    }

    public InvestRequests(Long irId) {
        this.irId = irId;
    }

    public Long getIrId() {
        return irId;
    }

    public void setIrId(Long irId) {
        this.irId = irId;
    }

    public Double getIrInvestAmount() {
        return irInvestAmount;
    }

    public void setIrInvestAmount(Double irInvestAmount) {
        this.irInvestAmount = irInvestAmount;
    }

    public Double getIrRefundAmount() {
        return irRefundAmount;
    }

    public void setIrRefundAmount(Double irRefundAmount) {
        this.irRefundAmount = irRefundAmount;
    }

    public Integer getIrLoanTerm() {
        return irLoanTerm;
    }

    public void setIrLoanTerm(Integer irLoanTerm) {
        this.irLoanTerm = irLoanTerm;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getIrLoanDeadline() {
        return irLoanDeadline;
    }

    public void setIrLoanDeadline(Date irLoanDeadline) {
        this.irLoanDeadline = irLoanDeadline;
    }

    public Double getIrCurrentInvestAmount() {
        return irCurrentInvestAmount;
    }

    public void setIrCurrentInvestAmount(Double irCurrentInvestAmount) {
        this.irCurrentInvestAmount = irCurrentInvestAmount;
    }

    public Boolean getIrDivided() {
        return irDivided;
    }

    public void setIrDivided(Boolean irDivided) {
        this.irDivided = irDivided;
    }

    @XmlTransient
    public List<InvestRequests> getInvestRequestsList() {
        return investRequestsList;
    }

    public void setInvestRequestsList(List<InvestRequests> investRequestsList) {
        this.investRequestsList = investRequestsList;
    }

    public InvestRequests getIrParentId() {
        return irParentId;
    }

    public void setIrParentId(InvestRequests irParentId) {
        this.irParentId = irParentId;
    }

    public States getIrStateId() {
        return irStateId;
    }

    public void setIrStateId(States irStatId) {
        this.irStateId = irStatId;
    }

    public Users getIrInitUserId() {
        return irInitUserId;
    }

    public void setIrInitUserId(Users irInitUserId) {
        this.irInitUserId = irInitUserId;
    }

    public Integer getIrMaxParts() {
        return irMaxParts;
    }

    public void setIrMaxParts(Integer irMaxParts) {
        this.irMaxParts = irMaxParts;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getIrRegCreated() {
        return irRegCreated;
    }

    public void setIrRegCreated(Date irRegCreated) {
        this.irRegCreated = irRegCreated;
    }

    public Integer getIrAgeLimitMin() {
        return irAgeLimitMin;
    }

    public void setIrAgeLimitMin(Integer irAgeLimitMin) {
        this.irAgeLimitMin = irAgeLimitMin;
    }

    public Integer getIrAgeLimitMax() {
        return irAgeLimitMax;
    }

    public void setIrAgeLimitMax(Integer irAgeLimitMax) {
        this.irAgeLimitMax = irAgeLimitMax;
    }

    public Double getIrMinUserRate() {
        return irMinUserRate;
    }

    public void setIrMinUserRate(Double minUserRate) {
        this.irMinUserRate = minUserRate;
    }

    public Integer getIrRegionCriteria() {
        return irRegionCriteria;
    }

    public void setIrRegionCriteria(Integer irRegionCriteria) {
        this.irRegionCriteria = irRegionCriteria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (irId != null ? irId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvestRequests)) {
            return false;
        }
        InvestRequests other = (InvestRequests) object;
        if ((this.irId == null && other.irId != null) || (this.irId != null && !this.irId.equals(other.irId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InvestRequests{" +
                "irId=" + irId +
                ", irInvestAmount=" + irInvestAmount +
                ", irRefundAmount=" + irRefundAmount +
                ", irLoanTerm=" + irLoanTerm +
                ", irLoanDeadline=" + irLoanDeadline +
                ", irCurrentInvestAmount=" + irCurrentInvestAmount +
                ", irDivided=" + irDivided +
                ", investRequestsList=" + investRequestsList +
                ", irParentId=" + irParentId +
                ", irStateId=" + irStateId +
                ", irInitUserId=" + irInitUserId +
                '}';
    }

}