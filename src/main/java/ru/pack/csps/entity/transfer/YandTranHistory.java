package ru.pack.csps.entity.transfer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import java.io.Serializable;
        import java.util.Date;
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
        import javax.persistence.Table;
        import javax.persistence.Temporal;
        import javax.persistence.TemporalType;
        import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mendor71
 */
@Entity
@Table(name = "yand_tran_history")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "YandTranHistory.findAll", query = "SELECT y FROM YandTranHistory y")
        , @NamedQuery(name = "YandTranHistory.findByYthId", query = "SELECT y FROM YandTranHistory y WHERE y.ythId = :ythId")
        , @NamedQuery(name = "YandTranHistory.findByYthTranRequestDatetime", query = "SELECT y FROM YandTranHistory y WHERE y.ythTranRequestDatetime = :ythTranRequestDatetime")
        , @NamedQuery(name = "YandTranHistory.findByYthTranResponseDatetime", query = "SELECT y FROM YandTranHistory y WHERE y.ythTranResponseDatetime = :ythTranResponseDatetime")
        , @NamedQuery(name = "YandTranHistory.findByYthTranRequest", query = "SELECT y FROM YandTranHistory y WHERE y.ythTranRequest = :ythTranRequest")
        , @NamedQuery(name = "YandTranHistory.findByYthTranResponse", query = "SELECT y FROM YandTranHistory y WHERE y.ythTranResponse = :ythTranResponse")
        , @NamedQuery(name = "YandTranHistory.findByYhtState", query = "SELECT y FROM YandTranHistory y WHERE y.yhtState = :yhtState")})
public class YandTranHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "yth_id")
    private Integer ythId;
    @Column(name = "yth_tran_request_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ythTranRequestDatetime;
    @Column(name = "yth_tran_response_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ythTranResponseDatetime;
    @Column(name = "yth_tran_request")
    private String ythTranRequest;
    @Column(name = "yth_tran_response")
    private String ythTranResponse;
    @Column(name = "yht_state")
    private String yhtState;
    @JoinColumn(name = "yth_yt_id", referencedColumnName = "yt_id")
    @ManyToOne
    private YandTransactions ythYtId;

    public YandTranHistory() {
    }

    public YandTranHistory(Integer ythId) {
        this.ythId = ythId;
    }

    public Integer getYthId() {
        return ythId;
    }

    public void setYthId(Integer ythId) {
        this.ythId = ythId;
    }

    public Date getYthTranRequestDatetime() {
        return ythTranRequestDatetime;
    }

    public void setYthTranRequestDatetime(Date ythTranRequestDatetime) {
        this.ythTranRequestDatetime = ythTranRequestDatetime;
    }

    public Date getYthTranResponseDatetime() {
        return ythTranResponseDatetime;
    }

    public void setYthTranResponseDatetime(Date ythTranResponseDatetime) {
        this.ythTranResponseDatetime = ythTranResponseDatetime;
    }

    public String getYthTranRequest() {
        return ythTranRequest;
    }

    public void setYthTranRequest(String ythTranRequest) {
        this.ythTranRequest = ythTranRequest;
    }

    public String getYthTranResponse() {
        return ythTranResponse;
    }

    public void setYthTranResponse(String ythTranResponse) {
        this.ythTranResponse = ythTranResponse;
    }

    public String getYhtState() {
        return yhtState;
    }

    public void setYhtState(String yhtState) {
        this.yhtState = yhtState;
    }

    public YandTransactions getYthYtId() {
        return ythYtId;
    }

    public void setYthYtId(YandTransactions ythYtId) {
        this.ythYtId = ythYtId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ythId != null ? ythId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YandTranHistory)) {
            return false;
        }
        YandTranHistory other = (YandTranHistory) object;
        if ((this.ythId == null && other.ythId != null) || (this.ythId != null && !this.ythId.equals(other.ythId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.YandTranHistory[ ythId=" + ythId + " ]";
    }

}
