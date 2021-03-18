package ru.pack.csps.entity;

import javax.persistence.*;
import javax.persistence.spi.LoadState;

/**
 * Created by Mendor on 20.02.2018.
 */
@Entity
@Table(name = "public.settings")
public class Settings {
    @Id
    @Column(name = "s_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sId;
    @Column(name = "s_key")
    private String sKey;
    @Column(name = "s_value")
    private String sValue;
    @Column(name = "s_data_type")
    private String sDataType;

    public Settings() {
    }

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public String getsKey() {
        return sKey;
    }

    public void setsKey(String sKey) {
        this.sKey = sKey;
    }

    public String getsValue() {
        return sValue;
    }

    public void setsValue(String sValue) {
        this.sValue = sValue;
    }

    public String getsDataType() {
        return sDataType;
    }

    public void setsDataType(String sDataType) {
        this.sDataType = sDataType;
    }
}
