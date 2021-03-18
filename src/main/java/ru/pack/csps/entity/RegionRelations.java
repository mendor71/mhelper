package ru.pack.csps.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author mendor71
 */
@Entity
@Table(name = "region_relations")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "RegionRelations.findAll", query = "SELECT r FROM RegionRelations r")
        , @NamedQuery(name = "RegionRelations.findByRrId", query = "SELECT r FROM RegionRelations r WHERE r.rrId = :rrId")})
public class RegionRelations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rr_id")
    private Integer rrId;
    @JoinColumn(name = "rr_reg_a_id", referencedColumnName = "region_id")
    @ManyToOne
    private Regions rrRegAId;
    @JoinColumn(name = "rr_reg_b_id", referencedColumnName = "region_id")
    @ManyToOne
    private Regions rrRegBId;

    public RegionRelations() {
    }

    public RegionRelations(Integer rrId) {
        this.rrId = rrId;
    }

    public Integer getRrId() {
        return rrId;
    }

    public void setRrId(Integer rrId) {
        this.rrId = rrId;
    }

    public Regions getRrRegAId() {
        return rrRegAId;
    }

    public void setRrRegAId(Regions rrRegAId) {
        this.rrRegAId = rrRegAId;
    }

    public Regions getRrRegBId() {
        return rrRegBId;
    }

    public void setRrRegBId(Regions rrRegBId) {
        this.rrRegBId = rrRegBId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rrId != null ? rrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegionRelations)) {
            return false;
        }
        RegionRelations other = (RegionRelations) object;
        if ((this.rrId == null && other.rrId != null) || (this.rrId != null && !this.rrId.equals(other.rrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.RegionRelations[ rrId=" + rrId + " ]";
    }

}

