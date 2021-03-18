package ru.pack.csps.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "public.payment_logs")
public class PaymentLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pl_id")
    private Long plId;
    @Column(name = "pl_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date plDate;
    @Column(name = "pl_text")
    private String plText;
    @Column(name = "pl_tid")
    private String plTid;

    public PaymentLogs() {
    }

    public PaymentLogs(String text) {
        this.plDate = new Date();
        this.plText = text;
    }

    public PaymentLogs(String tid, String text) {
        this.plDate = new Date();
        this.plText = text;
        this.plTid = tid;
    }

    public Long getPlId() {
        return plId;
    }

    public void setPlId(Long plId) {
        this.plId = plId;
    }

    public Date getPlDate() {
        return plDate;
    }

    public void setPlDate(Date plDate) {
        this.plDate = plDate;
    }

    public String getPlText() {
        return plText;
    }

    public void setPlText(String plText) {
        this.plText = plText;
    }

    public String getPlTid() {
        return plTid;
    }

    public void setPlTid(String plTid) {
        this.plTid = plTid;
    }
}
