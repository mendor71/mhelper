package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Mendor on 01.07.2018.
 */
@Entity
@Table(name = "users_documents")
public class UserDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_doc_id")
    private Long userDocId;

    @JoinColumn(name = "user_doc_type_id", referencedColumnName = "dt_id")
    @ManyToOne
    private DocumentsTypes userDocType;

    @Column(name = "user_doc_text")
    private String userDocText;

    @Column(name = "user_doc_confirmed")
    private Boolean userDocConfirmed;

    @Column(name = "user_doc_confirm_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userDocConfirmDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_doc_confirm_code")
    private String userDocConfirmCode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_doc_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users userDocUser;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_doc_state_id", referencedColumnName = "state_id")
    @ManyToOne
    private States userDocState;

    public Long getUserDocId() {
        return userDocId;
    }

    public void setUserDocId(Long userDocId) {
        this.userDocId = userDocId;
    }

    public DocumentsTypes getUserDocType() {
        return userDocType;
    }

    public void setUserDocType(DocumentsTypes userDocType) {
        this.userDocType = userDocType;
    }

    public String getUserDocText() {
        return userDocText;
    }

    public void setUserDocText(String userDocText) {
        this.userDocText = userDocText;
    }

    public Users getUserDocUser() {
        return userDocUser;
    }

    public void setUserDocUser(Users userDocUser) {
        this.userDocUser = userDocUser;
    }

    public States getUserDocState() {
        return userDocState;
    }

    public void setUserDocState(States userDocState) {
        this.userDocState = userDocState;
    }

    public String getUserDocConfirmCode() {
        return userDocConfirmCode;
    }

    public void setUserDocConfirmCode(String userDocConfirmCode) {
        this.userDocConfirmCode = userDocConfirmCode;
    }

    public Date getUserDocConfirmDate() {
        return userDocConfirmDate;
    }

    public void setUserDocConfirmDate(Date userDocConfirmDate) {
        this.userDocConfirmDate = userDocConfirmDate;
    }

    public Boolean getUserDocConfirmed() {
        return userDocConfirmed;
    }

    public void setUserDocConfirmed(Boolean userDocConfirmed) {
        this.userDocConfirmed = userDocConfirmed;
    }
}
