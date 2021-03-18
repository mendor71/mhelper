package ru.pack.csps.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "public.documents_types")
public class DocumentsTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dt_id")
    private Integer dtId;
    @Column(name = "dt_name")
    private String dtName;
    @Column(name = "dt_name_locale")
    private String dtNameLocale;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "dt_html_template")
    private String dtHtmlTemplate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "dt_html_template_ref")
    private String dtHtmlTemplateRef;

    public DocumentsTypes() {
    }

    public Integer getDtId() {
        return dtId;
    }

    public void setDtId(Integer dtId) {
        this.dtId = dtId;
    }

    public String getDtName() {
        return dtName;
    }

    public void setDtName(String dtName) {
        this.dtName = dtName;
    }

    public String getDtHtmlTemplate() {
        return dtHtmlTemplate;
    }

    public void setDtHtmlTemplate(String dtHtmlTemplate) {
        this.dtHtmlTemplate = dtHtmlTemplate;
    }

    public String getDtHtmlTemplateRef() {
        return dtHtmlTemplateRef;
    }

    public void setDtHtmlTemplateRef(String dtHtmlTemplateRef) {
        this.dtHtmlTemplateRef = dtHtmlTemplateRef;
    }

    public String getDtNameLocale() {
        return dtNameLocale;
    }

    public void setDtNameLocale(String dtNameLocale) {
        this.dtNameLocale = dtNameLocale;
    }
}
