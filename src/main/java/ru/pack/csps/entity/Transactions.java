/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pack.csps.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mendor71
 */
@Entity
@Table(name = "transfer.transactions")
public class Transactions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tra_id")
    private Integer traId;
    @JoinColumn(name = "tra_from_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users traFromUserId;
    @JoinColumn(name = "tra_to_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users traToUserId;
    @Basic(optional = false)
    @Column(name = "tra_sum")
    private double traSum;
    @Basic(optional = false)
    @Column(name = "tra_type")
    private String traType;
    @JoinColumn(name = "tra_state_id", referencedColumnName = "state_id")
    @ManyToOne(optional = false)
    private States traStateId;
    @JoinColumn(name = "tra_ilr_id", referencedColumnName = "ilr_id")
    @ManyToOne(optional = false)
    private InvestLoanRequests traIlrId;
    @Column(name = "tra_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date traCreated;
    @Column(name = "tra_handled")
    @Temporal(TemporalType.TIMESTAMP)
    private Date traHandled;

    @Column(name = "tra_handled_message")
    private String traHandleMessage;

    public String getTraHandleMessage() {
        return traHandleMessage;
    }

    public void setTraHandleMessage(String traHandledMessage) {
        this.traHandleMessage = traHandledMessage;
    }

    public enum TYPE {FROM_INVESTOR_TO_BORROWER, FROM_BORROWER_TO_INVESTOR}

    public Transactions() {
    }

    public Integer getTraId() {
        return traId;
    }

    public void setTraId(Integer traId) {
        this.traId = traId;
    }

    public Users getTraFromUserId() {
        return traFromUserId;
    }

    public void setTraFromUserId(Users traFromUserId) {
        this.traFromUserId = traFromUserId;
    }

    public Users getTraToUserId() {
        return traToUserId;
    }

    public void setTraToUserId(Users traToUserId) {
        this.traToUserId = traToUserId;
    }

    public double getTraSum() {
        return traSum;
    }

    public void setTraSum(double traSum) {
        this.traSum = traSum;
    }

    public String getTraType() {
        return traType;
    }

    public void setTraType(String traType) {
        this.traType = traType;
    }

    public States getTraStateId() {
        return traStateId;
    }

    public void setTraStateId(States traStateId) {
        this.traStateId = traStateId;
    }

    public InvestLoanRequests getTraIlrId() {
        return traIlrId;
    }

    public void setTraIlrId(InvestLoanRequests traIlrId) {
        this.traIlrId = traIlrId;
    }

    public Date getTraCreated() {
        return traCreated;
    }

    public void setTraCreated(Date traCreated) {
        this.traCreated = traCreated;
    }

    public Date getTraHandled() {
        return traHandled;
    }

    public void setTraHandled(Date traHandled) {
        this.traHandled = traHandled;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "traId=" + traId +
                ", traFromUserId=" + traFromUserId +
                ", traToUserId=" + traToUserId +
                ", traSum=" + traSum +
                ", traType='" + traType + '\'' +
                ", traStateId=" + traStateId +
                ", traIlrId=" + traIlrId +
                ", traCreated=" + traCreated +
                ", traHandled=" + traHandled +
                '}';
    }
}
