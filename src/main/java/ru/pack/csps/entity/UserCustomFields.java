/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Base64;
import org.springframework.hateoas.ResourceSupport;
import ru.pack.csps.crypt.Encryptor;
import ru.pack.csps.util.IEncrypted;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.*;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 * @author Mendor
 */
@Entity
@Table(name = "user_custom_fields")
public class UserCustomFields extends ResourceSupport implements Serializable, IEncrypted {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ucf_id")
    private Long ucfId;
    @Basic(optional = false)
    @Column(name = "ucf_mail")
    private String ucfMail;
    @Column(name = "ucf_phone")
    private String ucfPhone;
    @Column(name = "ucf_address")
    private String ucfAddress;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "ucf_passport_1")
    private byte[] ucfPassport1;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "ucf_passport_2")
    private byte[] ucfPassport2;
    @Column(name = "ucf_digital_key")
    private String ucfDigitalKey;
    @Column(name = "ucf_p1_file_format")
    private String ucfP1FileFormat;
    @Column(name = "ucf_p2_file_format")
    private String ucfP2FileFormat;
    @Column(name = "ucf_birth_date")
    private String ucfBirthDate;
    @Column(name = "ucf_insurance_number")
    private String ucfInsuranceNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "ucf_region_id", referencedColumnName = "region_id")
    @ManyToOne
    private Regions ucfRegionId;

    public UserCustomFields() {
        this.setUcfPhone("");
        this.setUcfMail("");
    }

    public UserCustomFields(Long ucfId) {
        this.ucfId = ucfId;
    }

    public UserCustomFields(String ucfMail, String ucfPhone) {
        this.ucfMail = ucfMail;
        this.ucfPhone = ucfPhone;
    }

    public UserCustomFields(String ucfMail, String ucfPhone, Users users, String ucfBirthDate, Regions regions) {
        this.ucfMail = ucfMail;
        this.ucfPhone = ucfPhone;
        this.ucfBirthDate = ucfBirthDate;
        this.ucfRegionId = regions;
    }

    public String getUcfMail() {
        return ucfMail;
    }

    public void setUcfMail(String ucfMail) {
        this.ucfMail = ucfMail;
    }

    public String getUcfPhone() {
        return ucfPhone;
    }

    public void setUcfPhone(String ucfPhone) {
        this.ucfPhone = ucfPhone;
    }

    public String getUcfAddress() {
        return ucfAddress;
    }

    public void setUcfAddress(String ucfAddress) {
        this.ucfAddress = ucfAddress;
    }

    public byte[] getUcfPassport1() throws IOException {
        return this.ucfPassport1;
    }

    public void setUcfPassport1(String ucfPassport1Path) throws IOException {
        FileInputStream is = null;
        try {
            File f = new File(ucfPassport1Path);
            is = new FileInputStream(f);

            byte[] bytes = new byte[(int) f.length()];
            is.read(bytes);

            this.ucfPassport1 = Base64.encodeBase64(bytes);

            is.close();
            is = null;
        } finally {
            if (is != null) {is.close();}
        }
    }

    public byte[] getUcfPassport2() {
        return ucfPassport2;
    }

    public void setUcfPassport2(String ucfPassport2Path) throws IOException {
        FileInputStream is = null;
        try {
            File f = new File(ucfPassport2Path);
            is = new FileInputStream(f);

            byte[] bytes = new byte[(int) f.length()];
            is.read(bytes);

            this.ucfPassport2 = Base64.encodeBase64(bytes);

            is.close();
            is = null;
        } finally {
            if (is != null) {is.close();}
        }
    }

    public String getUcfDigitalKey() {
        return ucfDigitalKey;
    }

    public void setUcfDigitalKey(String ucfDigitalKey) {
        this.ucfDigitalKey = ucfDigitalKey;
    }

    public Long getUcfId() {
        return ucfId;
    }

    public void setUcfId(Long ucfId) {
        this.ucfId = ucfId;
    }

    public String getUcfP2FileFormat() {
        return ucfP2FileFormat;
    }

    public void setUcfP2FileFormat(String ucfP2FileName) {
        this.ucfP2FileFormat = ucfP2FileName;
    }

    public String getUcfP1FileFormat() {
        return ucfP1FileFormat;
    }

    public void setUcfP1FileFormat(String ucfP1FileName) {
        this.ucfP1FileFormat= ucfP1FileName;
    }

    public String getUcfBirthDate() {
        return ucfBirthDate;
    }

    public void setUcfBirthDate(String ucfBirthDate) {
        this.ucfBirthDate = ucfBirthDate;
    }

    public Regions getUcfRegionId() {
        return ucfRegionId;
    }

    public void setUcfRegionId(Regions ucfRegionId) {
        this.ucfRegionId = ucfRegionId;
    }

    public String getUcfInsuranceNumber() {
        return ucfInsuranceNumber;
    }

    public void setUcfInsuranceNumber(String ucfInsuranceNumber) {
        this.ucfInsuranceNumber = ucfInsuranceNumber;
    }

    @Override
    public String toString() {
        return "UserCustomFields{" +
                "ucfP2FileFormat='" + ucfP2FileFormat + '\'' +
                ", ucfP1FileFormat='" + ucfP1FileFormat + '\'' +
                ", ucfDigitalKey=" + ucfDigitalKey +
                ", ucfPassport2=" + Arrays.toString(ucfPassport2) +
                ", ucfPassport1=" + Arrays.toString(ucfPassport1) +
                ", ucfAddress='" + ucfAddress + '\'' +
                ", ucfPhone='" + ucfPhone + '\'' +
                ", ucfMail='" + ucfMail + '\'' +
                ", ucfId=" + ucfId +
                '}';
    }

    @Override
    public void encrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.ucfPhone = this.ucfPhone != null ? encryptor.encrypt(this.ucfPhone) : null;
        this.ucfMail = this.ucfMail != null ? encryptor.encrypt(this.ucfMail) : null;
        this.ucfAddress = this.ucfAddress != null ? encryptor.encrypt(this.ucfAddress) : null;
        this.ucfDigitalKey = this.ucfDigitalKey != null ? encryptor.encrypt(this.ucfDigitalKey) : null;
        this.ucfBirthDate = this.ucfBirthDate != null ? encryptor.encrypt(this.ucfBirthDate) : null;
        this.ucfInsuranceNumber = this.ucfInsuranceNumber != null ? encryptor.encrypt(this.ucfInsuranceNumber) : null;
    }

    @Override
    public void decrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.ucfPhone = this.ucfPhone != null ? encryptor.decrypt(this.ucfPhone) : null;
        this.ucfMail = this.ucfMail != null ? encryptor.decrypt(this.ucfMail) : null;
        this.ucfAddress = this.ucfAddress != null ? encryptor.decrypt(this.ucfAddress) : null;
        this.ucfDigitalKey = this.ucfDigitalKey != null ? encryptor.decrypt(this.ucfDigitalKey) : null;
        this.ucfBirthDate = this.ucfBirthDate != null ? encryptor.decrypt(this.ucfBirthDate) : null;
        this.ucfInsuranceNumber = this.ucfInsuranceNumber != null ? encryptor.decrypt(this.ucfInsuranceNumber) : null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ucfId != null ? ucfId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserCustomFields)) {
            return false;
        }
        UserCustomFields other = (UserCustomFields) object;
        if ((this.ucfId == null && other.ucfId != null) || (this.ucfId != null && !this.ucfId.equals(other.ucfId))) {
            return false;
        }
        return true;
    }
}
