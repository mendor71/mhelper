package ru.pack.csps.entity.transfer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import javax.persistence.Basic;
        import javax.persistence.Column;
        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;
        import javax.persistence.JoinColumn;
        import javax.persistence.ManyToOne;
        import javax.persistence.NamedQueries;
        import javax.persistence.NamedQuery;
        import javax.persistence.OneToMany;
        import javax.persistence.Table;
        import javax.persistence.Temporal;
        import javax.persistence.TemporalType;
        import javax.xml.bind.annotation.XmlRootElement;
        import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mendor71
 */
@Entity
@Table(name = "yand_transactions")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "YandTransactions.findAll", query = "SELECT y FROM YandTransactions y")
        , @NamedQuery(name = "YandTransactions.findByYtId", query = "SELECT y FROM YandTransactions y WHERE y.ytId = :ytId")
        , @NamedQuery(name = "YandTransactions.findByYtRequest", query = "SELECT y FROM YandTransactions y WHERE y.ytRequest = :ytRequest")
        , @NamedQuery(name = "YandTransactions.findByYtResponse", query = "SELECT y FROM YandTransactions y WHERE y.ytResponse = :ytResponse")
        , @NamedQuery(name = "YandTransactions.findByYtState", query = "SELECT y FROM YandTransactions y WHERE y.ytState = :ytState")
        , @NamedQuery(name = "YandTransactions.findByYtIdemKey", query = "SELECT y FROM YandTransactions y WHERE y.ytIdemKey = :ytIdemKey")
        , @NamedQuery(name = "YandTransactions.findByYtNextTryDatetime", query = "SELECT y FROM YandTransactions y WHERE y.ytNextTryDatetime = :ytNextTryDatetime")
        , @NamedQuery(name = "YandTransactions.findByYtReRun", query = "SELECT y FROM YandTransactions y WHERE y.ytReRun = :ytReRun")})
public class YandTransactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "yt_id")
    private Integer ytId;
    @Column(name = "yt_request")
    private String ytRequest;
    @Column(name = "yt_response")
    private String ytResponse;
    @Column(name = "yt_state")
    private String ytState;
    @Column(name = "yt_idem_key")
    private String ytIdemKey;
    @Column(name = "yt_yandex_payment_id")
    private String ytYandexPaymentId;
    @Column(name = "yt_next_try_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ytNextTryDatetime;
    @Column(name = "yt_re_run")
    private Integer ytReRun;
    @JoinColumn(name = "yt_paym_id", referencedColumnName = "p_id")
    @ManyToOne
    private Payments ytPaymId;
    @JoinColumn(name = "yt_refund_id", referencedColumnName = "r_id")
    @ManyToOne
    private Refunds ytRefundId;
    @OneToMany(mappedBy = "ythYtId")
    private List<YandTranHistory> yandTranHistoryList = new ArrayList<YandTranHistory>();

    public YandTransactions() {
    }

    public YandTransactions(Payments payments) {
        this.setYtPaymId(payments);
        this.setYtIdemKey(payments.getPIdemKey());
    }

    public Integer getYtId() {
        return ytId;
    }

    public void setYtId(Integer ytId) {
        this.ytId = ytId;
    }

    public String getYtRequest() {
        return ytRequest;
    }

    public void setYtRequest(String ytRequest) {
        this.ytRequest = ytRequest;
    }

    public String getYtResponse() {
        return ytResponse;
    }

    public void setYtResponse(String ytResponse) {
        this.ytResponse = ytResponse;
    }

    public String getYtState() {
        return ytState;
    }

    public void setYtState(String ytState) {
        this.ytState = ytState;
    }

    public String getYtIdemKey() {
        return ytIdemKey;
    }

    public void setYtIdemKey(String ytIdemKey) {
        this.ytIdemKey = ytIdemKey;
    }

    public Date getYtNextTryDatetime() {
        return ytNextTryDatetime;
    }

    public void setYtNextTryDatetime(Date ytNextTryDatetime) {
        this.ytNextTryDatetime = ytNextTryDatetime;
    }

    public Integer getYtReRun() {
        return ytReRun;
    }

    public void setYtReRun(Integer ytReRun) {
        this.ytReRun = ytReRun;
    }

    public Payments getYtPaymId() {
        return ytPaymId;
    }

    public void setYtPaymId(Payments ytPaymId) {
        this.ytPaymId = ytPaymId;
    }

    public Refunds getYtRefundId() {
        return ytRefundId;
    }

    public void setYtRefundId(Refunds ytRefundId) {
        this.ytRefundId = ytRefundId;
    }

    @XmlTransient
    public List<YandTranHistory> getYandTranHistoryList() {
        return yandTranHistoryList;
    }

    public void setYandTranHistoryList(List<YandTranHistory> yandTranHistoryList) {
        this.yandTranHistoryList = yandTranHistoryList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ytId != null ? ytId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YandTransactions)) {
            return false;
        }
        YandTransactions other = (YandTransactions) object;
        if ((this.ytId == null && other.ytId != null) || (this.ytId != null && !this.ytId.equals(other.ytId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.YandTransactions[ ytId=" + ytId + " ]";
    }

    public String getYtYandexPaymentId() {
        return ytYandexPaymentId;
    }

    public void setYtYandexPaymentId(String ytYandexPaymentId) {
        this.ytYandexPaymentId = ytYandexPaymentId;
    }
}

