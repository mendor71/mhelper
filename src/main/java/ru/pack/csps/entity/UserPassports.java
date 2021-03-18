package ru.pack.csps.entity;

import ru.pack.csps.crypt.Encryptor;
import ru.pack.csps.util.IEncrypted;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "public.users_passports")
public class UserPassports implements IEncrypted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "up_id")
    private Long upId;
    @Column(name = "up_series")
    private String upSeries;

    @Column(name = "up_number")
    private String upNumber;

    @Column(name = "up_given_by")
    private String upGivenBy;

    @Column(name = "up_given_date")
    private String upGivenDate;

    @Column(name = "up_location_address")
    private String upLocationAddress;

    public UserPassports() {
    }

    public Long getUpId() {
        return upId;
    }

    public void setUpId(Long upId) {
        this.upId = upId;
    }

    public String getUpSeries() {
        return upSeries;
    }

    public void setUpSeries(String upSeries) {
        this.upSeries = upSeries;
    }

    public String getUpNumber() {
        return upNumber;
    }

    public void setUpNumber(String upNumber) {
        this.upNumber = upNumber;
    }

    public String getUpGivenBy() {
        return upGivenBy;
    }

    public void setUpGivenBy(String upGivenBy) {
        this.upGivenBy = upGivenBy;
    }

    public String getUpGivenDate() {
        return upGivenDate;
    }

    public void setUpGivenDate(String upGivenDate) {
        this.upGivenDate = upGivenDate;
    }

    public String getUpLocationAddress() {
        return upLocationAddress;
    }

    public void setUpLocationAddress(String upLocationAddress) {
        this.upLocationAddress = upLocationAddress;
    }

    @Override
    public void encrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (upSeries != null) { upSeries = encryptor.encrypt(upSeries); }
        if (upNumber != null) { upNumber = encryptor.encrypt(upNumber); }
        if (upGivenBy != null) { upGivenBy = encryptor.encrypt(upGivenBy); }
        if (upGivenDate != null) { upGivenDate = encryptor.encrypt(upGivenDate); }
        if (upLocationAddress != null) { upLocationAddress = encryptor.encrypt(upLocationAddress); }
    }

    @Override
    public void decrypt(Encryptor encryptor) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (upSeries != null) { upSeries = encryptor.decrypt(upSeries); }
        if (upNumber != null) { upNumber = encryptor.decrypt(upNumber); }
        if (upGivenBy != null) { upGivenBy = encryptor.decrypt(upGivenBy); }
        if (upGivenDate != null) { upGivenDate = encryptor.decrypt(upGivenDate); }
        if (upLocationAddress != null) { upLocationAddress = encryptor.decrypt(upLocationAddress); }
    }
}
