/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pack.csps.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Mendor
 */
@Entity
@Table(name = "states")
public class States implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "state_id")
    private Integer stateId;
    @Basic(optional = false)
    @Column(name = "state_name")
    private String stateName;
    @Column(name = "state_name_locale")
    private String stateNameLocale;

    public States() {
    }

    public States(Integer stateId) {
        this.stateId = stateId;
    }

    public States(Integer stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateNameLocale() {
        return stateNameLocale;
    }

    public void setStateNameLocale(String stateNameLocale) {
        this.stateNameLocale = stateNameLocale;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stateId != null ? stateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof States)) {
            return false;
        }
        States other = (States) object;
        if ((this.stateId == null && other.stateId != null) || (this.stateId != null && !this.stateId.equals(other.stateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.db.States[ stateId=" + stateId + " ]";
    }
}
