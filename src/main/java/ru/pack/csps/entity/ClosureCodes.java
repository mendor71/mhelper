package ru.pack.csps.entity;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;

@Entity
@Table(name = "closure_codes")
public class ClosureCodes extends ResourceSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clo_code_id")
    private Long cloCodeId;
    @Column(name = "clo_code_name")
    private String cloCodeName;
    @Column(name = "clo_code_name_locale")
    private String cloCodeNameLocale;

    public ClosureCodes() {
    }

    public ClosureCodes(Long cloCodeId, String cloCodeName, String cloCodeNameLocale) {
        this.cloCodeId = cloCodeId;
        this.cloCodeName = cloCodeName;
        this.cloCodeNameLocale = cloCodeNameLocale;
    }

    public Long getCloCodeId() {
        return cloCodeId;
    }

    public void setCloCodeId(Long cloCodeId) {
        this.cloCodeId = cloCodeId;
    }

    public String getCloCodeName() {
        return cloCodeName;
    }

    public void setCloCodeName(String cloCodeName) {
        this.cloCodeName = cloCodeName;
    }

    public String getCloCodeNameLocale() {
        return cloCodeNameLocale;
    }

    public void setCloCodeNameLocale(String cloCodeNameLocale) {
        this.cloCodeNameLocale = cloCodeNameLocale;
    }
}
