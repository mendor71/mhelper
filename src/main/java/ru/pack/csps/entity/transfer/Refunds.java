package ru.pack.csps.entity.transfer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import ru.pack.csps.entity.Cards;

        import java.io.Serializable;
        import java.math.BigInteger;
        import java.util.ArrayList;
        import java.util.List;
        import javax.persistence.*;
        import javax.xml.bind.annotation.XmlRootElement;
        import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mendor71
 */
@Entity
@Table(name = "refunds")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Refunds.findAll", query = "SELECT r FROM Refunds r")
        , @NamedQuery(name = "Refunds.findByRId", query = "SELECT r FROM Refunds r WHERE r.rId = :rId")
        , @NamedQuery(name = "Refunds.findByRAmount", query = "SELECT r FROM Refunds r WHERE r.rAmount = :rAmount")
        , @NamedQuery(name = "Refunds.findByRPaymentId", query = "SELECT r FROM Refunds r WHERE r.rPaymentId = :rPaymentId")
        , @NamedQuery(name = "Refunds.findByRState", query = "SELECT r FROM Refunds r WHERE r.rState = :rState")})
public class Refunds implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "r_id")
    private Integer rId;
    @Column(name = "r_amount")
    private BigInteger rAmount;
    @Column(name = "r_payment_id")
    private Integer rPaymentId;
    @Column(name = "r_state")
    private String rState;
    @JoinColumn(name = "r_card_id", referencedColumnName = "card_id")
    @ManyToOne
    private Cards refundCardId;
    @OneToMany(mappedBy = "ytRefundId")
    private List<YandTransactions> yandTransactionsList = new ArrayList<YandTransactions>();

    public Refunds() {
    }

    public Refunds(Integer rId) {
        this.rId = rId;
    }

    public Integer getRId() {
        return rId;
    }

    public void setRId(Integer rId) {
        this.rId = rId;
    }

    public BigInteger getRAmount() {
        return rAmount;
    }

    public void setRAmount(BigInteger rAmount) {
        this.rAmount = rAmount;
    }

    public Integer getRPaymentId() {
        return rPaymentId;
    }

    public void setRPaymentId(Integer rPaymentId) {
        this.rPaymentId = rPaymentId;
    }

    public String getRState() {
        return rState;
    }

    public void setRState(String rState) {
        this.rState = rState;
    }

    @XmlTransient
    public List<YandTransactions> getYandTransactionsList() {
        return yandTransactionsList;
    }

    public void setYandTransactionsList(List<YandTransactions> yandTransactionsList) {
        this.yandTransactionsList = yandTransactionsList;
    }

    public Cards getRefundCardId() {
        return refundCardId;
    }

    public void setRefundCardId(Cards refundCardId) {
        this.refundCardId = refundCardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rId != null ? rId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Refunds)) {
            return false;
        }
        Refunds other = (Refunds) object;
        if ((this.rId == null && other.rId != null) || (this.rId != null && !this.rId.equals(other.rId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Refunds[ rId=" + rId + " ]";
    }
}

