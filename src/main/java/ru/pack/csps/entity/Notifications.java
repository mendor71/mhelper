/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mendor71
 */
@Entity
@Table(name = "notifications")
public class Notifications extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "notif_id")
    private Long notifId;
    @Basic(optional = false)
    @Column(name = "notif_type")
    private int notifType;
    @Column(name = "notif_text")
    private String notifText;
    @Basic(optional = false)
    @Column(name = "notif_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "notif_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users notifUserId;
    @JoinColumn(name = "notif_state_id", referencedColumnName = "state_id")
    @ManyToOne(optional = false)
    private States notifStateId;

    public Notifications() {
    }

    public Notifications(String text) {
        this.notifText = text;
    }

    public Notifications(int notifType, String notifText, Date notifDate, Users notifUserId, States notifStateId) {
        this.notifType = notifType;
        this.notifText = notifText;
        this.notifDate = notifDate;
        this.notifUserId = notifUserId;
        this.notifStateId = notifStateId;
    }

    public Notifications(Long notifId) {
        this.notifId = notifId;
    }

    public Long getNotifId() {
        return notifId;
    }

    public void setNotifId(Long notifId) {
        this.notifId = notifId;
    }

    public int getNotifType() {
        return notifType;
    }

    public void setNotifType(int notifType) {
        this.notifType = notifType;
    }

    public String getNotifText() {
        return notifText;
    }

    public void setNotifText(String notifText) {
        this.notifText = notifText;
    }

    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    public Date getNotifDate() {
        return notifDate;
    }

    public void setNotifDate(Date notifDate) {
        this.notifDate = notifDate;
    }

    public Users getNotifUserId() {
        return notifUserId;
    }

    public void setNotifUserId(Users notifUserId) {
        this.notifUserId = notifUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notifId != null ? notifId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.notifId == null && other.notifId != null) || (this.notifId != null && !this.notifId.equals(other.notifId))) {
            return false;
        }
        return true;
    }

    public States getNotifStateId() {
        return notifStateId;
    }

    public void setNotifStateId(States notifStateId) {
        this.notifStateId = notifStateId;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "notifId=" + notifId +
                ", notifType=" + notifType +
                ", notifText='" + notifText + '\'' +
                //", notifDate=" + notifDate +
                //", notifUserId=" + notifUserId +
                //", notifStateId=" + notifStateId +
                '}';
    }
}
