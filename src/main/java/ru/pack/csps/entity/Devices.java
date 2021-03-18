package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devices")
public class Devices extends ResourceSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dev_id")
    private Integer devId;
    @Column(name = "dev_uid")
    private String devUid;
    @Column(name = "dev_pin")
    private String devPin;

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer devId) {
        this.devId = devId;
    }

    public String getDevUid() {
        return devUid;
    }

    public void setDevUid(String devUid) {
        this.devUid = devUid;
    }

    public String getDevPin() {
        return devPin;
    }

    public void setDevPin(String devPin) {
        this.devPin = devPin;
    }

    public Devices() {
    }

    public Devices(String devUid, String devPin) {
        this.devUid = devUid;
        this.devPin = devPin;
    }
}
