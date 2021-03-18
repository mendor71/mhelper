package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "deals_documents")
public class DealsDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deal_doc_id")
    private Long dealDocId;

    @JoinColumn(name = "deal_doc_type_id", referencedColumnName = "dt_id")
    @ManyToOne
    private DocumentsTypes dealDocType;

    @Column(name = "deal_doc_text")
    private String dealDocText;

    @Column(name = "deal_doc_confirmed")
    private Boolean dealDocConfirmed;

    @Column(name = "deal_doc_confirm_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dealDocConfirmDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "deal_doc_confirm_code")
    private String dealDocConfirmCode;

    @JoinColumn(name = "deal_doc_deal_id", referencedColumnName = "deal_id")
    @ManyToOne
    private Deals dealDocDeal;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "deal_doc_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users dealDocUser;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "deal_doc_state_id", referencedColumnName = "state_id")
    @ManyToOne
    private States dealDocState;

    public DealsDocuments() {
    }

    public Long getDealDocId() {
        return dealDocId;
    }

    public void setDealDocId(Long dealDocId) {
        this.dealDocId = dealDocId;
    }

    public DocumentsTypes getDealDocType() {
        return dealDocType;
    }

    public void setDealDocType(DocumentsTypes dealDocType) {
        this.dealDocType = dealDocType;
    }

    public String getDealDocText() {
        return dealDocText;
    }

    public void setDealDocText(String dealDocText) {
        this.dealDocText = dealDocText;
    }

    public Deals getDealDocDeal() {
        return dealDocDeal;
    }

    public void setDealDocDeal(Deals dealDocDeal) {
        this.dealDocDeal = dealDocDeal;
    }

    public States getDealDocState() {
        return dealDocState;
    }

    public void setDealDocState(States dealDocState) {
        this.dealDocState = dealDocState;
    }

    public String getDealDocConfirmCode() {
        return dealDocConfirmCode;
    }

    public void setDealDocConfirmCode(String dealDocConfirmCode) {
        this.dealDocConfirmCode = dealDocConfirmCode;
    }

    public Users getDealDocUser() {
        return dealDocUser;
    }

    public void setDealDocUserId(Users dealDocUser) {
        this.dealDocUser = dealDocUser;
    }

    public Boolean getDealDocConfirmed() {
        return dealDocConfirmed;
    }

    public void setDealDocConfirmed(Boolean dealDocConfirmed) {
        this.dealDocConfirmed = dealDocConfirmed;
    }

    public Date getDealDocConfirmDate() {
        return dealDocConfirmDate;
    }

    public void setDealDocConfirmDate(Date dealDocConfirmDate) {
        this.dealDocConfirmDate = dealDocConfirmDate;
    }
}
