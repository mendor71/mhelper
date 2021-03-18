package ru.pack.csps.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 * @author mendor71
 */
@Entity
@Table(name = "region")
public class Regions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "region_id")
    private Integer regionId;
    @Column(name = "region_name")
    private String regionName;
    @Column(name = "region_time_zone")
    private Integer regionTimeZone;
    @OneToMany(mappedBy = "rrRegAId")
    private List<RegionRelations> regionRelationsListA;
    @OneToMany(mappedBy = "rrRegBId")
    private List<RegionRelations> regionRelationsListB;
    @OneToMany(mappedBy = "ucfRegionId")
    private List<UserCustomFields> ucfList;

    public Regions() {
    }

    public Regions(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getRegionTimeZone() {
        return regionTimeZone;
    }

    public void setRegionTimeZone(Integer regionTimeZone) {
        this.regionTimeZone = regionTimeZone;
    }

    @XmlTransient
    public List<RegionRelations> getRegionRelationsListA() {
        return regionRelationsListA;
    }

    public void setRegionRelationsList(List<RegionRelations> regionRelationsList) {
        this.regionRelationsListA = regionRelationsList;
    }

    @XmlTransient
    public List<RegionRelations> getRegionRelationsListB() {
        return regionRelationsListB;
    }

    public void setRegionRelationsListB(List<RegionRelations> regionRelationsList1) {
        this.regionRelationsListB = regionRelationsList1;
    }

    @XmlTransient
    public List<UserCustomFields> getUsersList() {
        return ucfList;
    }

    public void setUsersList(List<UserCustomFields> usersList) {
        this.ucfList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (regionId != null ? regionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Regions)) {
            return false;
        }
        Regions other = (Regions) object;
        if ((this.regionId == null && other.regionId != null) || (this.regionId != null && !this.regionId.equals(other.regionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Region[ regionId=" + regionId + " ]";
    }

}

