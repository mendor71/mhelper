package ru.pack.csps.entity.transfer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import ru.pack.csps.entity.Cards;

        import java.io.DataOutput;
        import java.io.Serializable;
        import java.math.BigInteger;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.UUID;
        import javax.persistence.*;
        import javax.sql.rowset.serial.SerialStruct;
        import javax.xml.bind.annotation.XmlRootElement;
        import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mendor71
 */
@Entity
@Table(name = "payments")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Payments.findAll", query = "SELECT p FROM Payments p")
        , @NamedQuery(name = "Payments.findByPId", query = "SELECT p FROM Payments p WHERE p.pId = :pId")
        , @NamedQuery(name = "Payments.findByPAmount", query = "SELECT p FROM Payments p WHERE p.pAmount = :pAmount")
        , @NamedQuery(name = "Payments.findByPToken", query = "SELECT p FROM Payments p WHERE p.pToken = :pToken")
        , @NamedQuery(name = "Payments.findByPPaymentMethodId", query = "SELECT p FROM Payments p WHERE p.pPaymentMethodId = :pPaymentMethodId")
        , @NamedQuery(name = "Payments.findByPState", query = "SELECT p FROM Payments p WHERE p.pState = :pState")
        , @NamedQuery(name = "Payments.findByPIdemKey", query = "SELECT p FROM Payments p WHERE p.pIdemKey = :pIdemKey")})
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "p_id")
    private Integer pId;
    @Column(name = "p_amount")
    private Double pAmount;
    @Column(name = "p_token")
    private String pToken;
    @Column(name = "p_payment_method_id")
    private String pPaymentMethodId;
    @Column(name = "p_state")
    private String pState;
    @JoinColumn(name = "p_card_id", referencedColumnName = "card_id")
    @ManyToOne
    private Cards paymentCardId;
    @Column(name = "p_idem_key")
    private String pIdemKey;
    @OneToMany(mappedBy = "ytPaymId")
    private List<YandTransactions> yandTransactionsList = new ArrayList<YandTransactions>();

    public Payments() {
    }

    public Payments(Double pAmount, String pToken, String idemKey) {
        this.setPAmount(pAmount);
        this.setPToken(pToken);
        this.setPIdemKey(idemKey);
    }

    public Payments(Integer pId) {
        this.pId = pId;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public Double getPAmount() {
        return pAmount;
    }

    public void setPAmount(Double pAmount) {
        this.pAmount = pAmount;
    }

    public String getPToken() {
        return pToken;
    }

    public void setPToken(String pToken) {
        this.pToken = pToken;
    }

    public String getPPaymentMethodId() {
        return pPaymentMethodId;
    }

    public void setPPaymentMethodId(String pPaymentMethodId) {
        this.pPaymentMethodId = pPaymentMethodId;
    }

    public String getPState() {
        return pState;
    }

    public void setPState(String pState) {
        this.pState = pState;
    }

    public String getPIdemKey() {
        return pIdemKey;
    }

    public void setPIdemKey(String pIdemKey) {
        this.pIdemKey = pIdemKey;
    }

    @XmlTransient
    public List<YandTransactions> getYandTransactionsList() {
        return yandTransactionsList;
    }

    public void setYandTransactionsList(List<YandTransactions> yandTransactionsList) {
        this.yandTransactionsList = yandTransactionsList;
    }

    public Cards getPaymentCardId() {
        return paymentCardId;
    }

    public void setPaymentCardId(Cards paymentCardId) {
        this.paymentCardId = paymentCardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pId != null ? pId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payments)) {
            return false;
        }
        Payments other = (Payments) object;
        if ((this.pId == null && other.pId != null) || (this.pId != null && !this.pId.equals(other.pId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Payments[ pId=" + pId + " ]";
    }
}

