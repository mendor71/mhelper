package ru.pack.csps.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.pack.csps.crypt.Encryptor;
import ru.pack.csps.util.IEncrypted;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author mendor71
 */
@Entity
@Table(name = "cards")
public class Cards implements Serializable, IEncrypted {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "card_id")
    private Long cardId;
    @Column(name = "card_expiry_month")
    private String cardExpiryMonth;
    @Column(name = "card_expiry_year")
    private String cardExpiryYear;
    @Column(name = "card_last_numbers")
    private String cardLastNumbers;
    @Column(name = "card_payment_method_id")
    private String cardPaymentMethodId;
    @Column(name = "card_synonym")
    private String cardSynonym;
    @Column(name = "card_type")
    private String cardType;

    @Column(name = "card_owner")
    private String cardOwner;

    @Column(name = "card_cvv")
    private String cardCvv;

    @Column(name = "card_full_number")
    private String cardFullNumber;

    @JoinColumn(name = "card_state_id", referencedColumnName = "state_id")
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private States cardStateId;

    public Cards() {
    }

    public Cards(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardLastNumbers() {
        return cardLastNumbers;
    }

    public void setCardLastNumbers(String cardLastNumbers) {
        this.cardLastNumbers = cardLastNumbers;
    }

    public String getCardPaymentMethodId() {
        return cardPaymentMethodId;
    }

    public void setCardPaymentMethodId(String cardPaymentMethodId) {
        this.cardPaymentMethodId = cardPaymentMethodId;
    }

    public String getCardSynonym() {
        return cardSynonym;
    }

    public void setCardSynonym(String cardSynonym) {
        this.cardSynonym = cardSynonym;
    }
    
    public String getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    public void setCardExpiryMonth(String cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
    }

    public String getCardExpiryYear() {
        return cardExpiryYear;
    }

    public void setCardExpiryYear(String cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardId != null ? cardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cards)) {
            return false;
        }
        Cards other = (Cards) object;
        if ((this.cardId == null && other.cardId != null) || (this.cardId != null && !this.cardId.equals(other.cardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Cards[ cardId=" + cardId + " ]";
    }

    @Override
    public void encrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.cardExpiryMonth = this.cardExpiryMonth != null ? encryptor.encrypt(this.cardExpiryMonth) : null;
        this.cardExpiryYear = this.cardExpiryYear != null ? encryptor.encrypt(this.cardExpiryYear) : null;
        this.cardLastNumbers = this.cardLastNumbers != null ? encryptor.encrypt(this.cardLastNumbers) : null;
        this.cardPaymentMethodId = this.cardPaymentMethodId != null ? encryptor.encrypt(this.cardPaymentMethodId) : null;
        this.cardSynonym = this.cardSynonym != null ? encryptor.encrypt(this.cardSynonym) : null;
        this.cardType = this.cardType != null ? encryptor.encrypt(this.cardType) : null;
        this.cardFullNumber = this.cardFullNumber != null ? encryptor.encrypt(this.cardFullNumber) : null;
        this.cardCvv = this.cardCvv != null ? encryptor.encrypt(this.cardCvv) : null;
        this.cardOwner = this.cardOwner != null ? encryptor.encrypt(this.cardOwner) : null;
    }

    @Override
    public void decrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.cardExpiryMonth = this.cardExpiryMonth != null ? encryptor.decrypt(this.cardExpiryMonth) : null;
        this.cardExpiryYear = this.cardExpiryYear != null ? encryptor.decrypt(this.cardExpiryYear) : null;
        this.cardLastNumbers = this.cardLastNumbers != null ? encryptor.decrypt(this.cardLastNumbers) : null;
        this.cardPaymentMethodId = this.cardPaymentMethodId != null ? encryptor.decrypt(this.cardPaymentMethodId) : null;
        this.cardSynonym = this.cardSynonym != null ? encryptor.decrypt(this.cardSynonym) : null;
        this.cardType = this.cardType != null ? encryptor.decrypt(this.cardType) : null;
        this.cardFullNumber = this.cardFullNumber != null ? encryptor.decrypt(this.cardFullNumber) : null;
        this.cardCvv = this.cardCvv != null ? encryptor.decrypt(this.cardCvv) : null;
        this.cardOwner = this.cardOwner != null ? encryptor.decrypt(this.cardOwner) : null;
    }

    public States getCardStateId() {
        return cardStateId;
    }

    public void setCardStateId(States cardStateId) {
        this.cardStateId = cardStateId;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public String getCardFullNumber() {
        return cardFullNumber;
    }

    public void setCardFullNumber(String cardFullNumber) {
        this.cardFullNumber = cardFullNumber;
    }
}

