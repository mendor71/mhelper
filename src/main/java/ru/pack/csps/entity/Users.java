/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.pack.csps.crypt.Encryptor;
import ru.pack.csps.util.IEncrypted;
import ru.pack.csps.util.SDFUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mendor
 */
@Entity
@Table(name = "users")
public class Users implements Serializable, IEncrypted {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_first_name")
    private String userFirstName;
    @Column(name = "user_last_name")
    private String userLastName;
    @Column(name = "user_middle_name")
    private String userMiddleName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Basic(optional = false)
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_reg_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userRegCreated;
    @Column(name = "user_updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userUpdatedDate;
    @Column(name = "user_rate")
    private Double userRate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_pwd_restore_uuid")
    private String userPwdRestoreUUID;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_pwd_restore_uuid_expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userPwdRestoreUUIDExpiryDate;
    @Column(name = "user_credit_history_request_agreement_confirmed")
    private Boolean userCreditHistoryRequestAgreementConfirmed;
    @Column(name = "user_personal_data_agreement_confirmed")
    private Boolean userPersonalDataAgreementConfirmed;

    @JoinColumn(name = "user_card_id", referencedColumnName = "card_id")
    @ManyToOne
    private Cards userCardId;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "user_roles", inverseJoinColumns = {@JoinColumn(name = "ur_role_id")}, joinColumns = {@JoinColumn(name = "ur_user_id")})
    private List<Roles> roleList = new ArrayList<Roles>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_devices", inverseJoinColumns = {@JoinColumn(name = "ud_dev_id")}, joinColumns = {@JoinColumn(name = "ud_user_id")})
    private List<Devices> deviceList = new ArrayList<Devices>();

    @JoinColumn(name = "user_state_id", referencedColumnName = "state_id")
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private States userStateId;

    @JoinColumn(name = "user_ucf_id", referencedColumnName = "ucf_id")
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private UserCustomFields userCustomFields;

    @JoinColumn(name = "user_passport_id", referencedColumnName = "up_id")
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private UserPassports userPassports;

    public Users() {
    }

    public Users(Long userId) {
        this.userId = userId;
    }

    public Users(String userName, String userPassword, States userStateId) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userStateId = userStateId;
        this.userRegCreated = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public States getUserStateId() {
        return userStateId;
    }

    public void setUserStateId(States userStateId) {
        this.userStateId = userStateId;
    }

    @XmlTransient
    public UserCustomFields getUserCustomFields() {
        return userCustomFields;
    }

    public void setUserCustomFields(UserCustomFields userCustomFields) {
        this.userCustomFields = userCustomFields;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    public List<Roles> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Roles> roleList) {
        this.roleList = roleList;
    }

    public List<Devices> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Devices> deviceList) {
        this.deviceList = deviceList;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUserRegCreated() {
        return userRegCreated;
    }

    public void setUserRegCreated(Date userRegCreated) {
        this.userRegCreated = userRegCreated;
    }

    public Double getUserRate() {
        return userRate;
    }

    public void setUserRate(Double userRate) {
        this.userRate = userRate;
    }

    public Date getUserUpdatedDate() {
        return userUpdatedDate;
    }

    public void setUserUpdatedDate(Date userUpdatedDate) {
        this.userUpdatedDate = userUpdatedDate;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserMiddleName() {
        return userMiddleName;
    }

    public void setUserMiddleName(String userMiddleName) {
        this.userMiddleName = userMiddleName;
    }

    public Integer getUserAge() throws ParseException {
        if (this.getUserCustomFields() != null) {
            int age = 0;
            if (this.getUserCustomFields().getUcfBirthDate() != null) {
                Calendar dob = Calendar.getInstance();
                Calendar today = Calendar.getInstance();

                dob.setTime(SDFUtil.parseDate(this.getUserCustomFields().getUcfBirthDate()));
                dob.add(Calendar.DAY_OF_MONTH, -1);

                age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
            }
            return age;
        } else {
            return 0;
        }
    }

    public Cards getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(Cards userCardId) {
        this.userCardId = userCardId;
    }

    public String getUserPwdRestoreUUID() {
        return userPwdRestoreUUID;
    }

    public void setUserPwdRestoreUUID(String userPwdRestoreUUID) {
        this.userPwdRestoreUUID = userPwdRestoreUUID;
    }

    public Date getUserPwdRestoreUUIDExpiryDate() {
        return userPwdRestoreUUIDExpiryDate;
    }

    public void setUserPwdRestoreUUIDExpiryDate(Date userPwdRestoreUUIDExpiryDate) {
        this.userPwdRestoreUUIDExpiryDate = userPwdRestoreUUIDExpiryDate;
    }

    public UserPassports getUserPassports() {
        return userPassports;
    }

    public void setUserPassports(UserPassports userPassports) {
        this.userPassports = userPassports;
    }

    public String getFullName() {
        return userLastName + " " + userFirstName + (userMiddleName != null ? " " + userMiddleName : "");
    }

    @Override
    public void encrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (this.userFirstName != null) {this.userFirstName = encryptor.encrypt(this.getUserFirstName());}
        if (this.userLastName != null) {this.userLastName = encryptor.encrypt(this.getUserLastName());}
        if (this.userMiddleName != null) {this.userMiddleName = encryptor.encrypt(this.getUserMiddleName());}
        if (this.userCustomFields != null) {this.getUserCustomFields().encrypt(encryptor);}
        if (this.userPassports != null) {this.getUserPassports().encrypt(encryptor);}
    }

    @Override
    public void decrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (this.userFirstName != null) {this.userFirstName = encryptor.decrypt(this.getUserFirstName());}
        if (this.userLastName != null) {this.userLastName = encryptor.decrypt(this.getUserLastName());}
        if (this.userMiddleName != null) {this.userMiddleName = encryptor.decrypt(this.getUserMiddleName());}
        if (this.userCustomFields != null) {this.getUserCustomFields().decrypt(encryptor);}
        if (this.userPassports != null) {this.getUserPassports().decrypt(encryptor);}
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.getUserId() == null && other.getUserId() != null) || (this.getUserId() != null && !this.getUserId().equals(other.getUserId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", userMiddleName='" + userMiddleName + '\'' +
                '}';
    }

    public Boolean getUserPersonalDataAgreementConfirmed() {
        return userPersonalDataAgreementConfirmed;
    }

    public void setUserPersonalDataAgreementConfirmed(Boolean userPersonalDataAgreementConfirmed) {
        this.userPersonalDataAgreementConfirmed = userPersonalDataAgreementConfirmed;
    }

    public Boolean getUserCreditHistoryRequestAgreementConfirmed() {
        return userCreditHistoryRequestAgreementConfirmed;
    }

    public void setUserCreditHistoryRequestAgreementConfirmed(Boolean userCreditHistoryRequestAgreementConfirmed) {
        this.userCreditHistoryRequestAgreementConfirmed = userCreditHistoryRequestAgreementConfirmed;
    }
}
