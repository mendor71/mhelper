package ru.pack.csps.util;

public class CompanyProperties {
    private String companyName;
    private String companyOrgNum;
    private String companyINN;
    private String companyPhysicalAddress;
    private String platformName;

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyOrgNum(String companyOrgNum) {
        this.companyOrgNum = companyOrgNum;
    }

    public String getCompanyOrgNum() {
        return companyOrgNum;
    }

    public void setCompanyINN(String companyINN) {
        this.companyINN = companyINN;
    }

    public String getCompanyINN() {
        return companyINN;
    }

    public void setCompanyPhysicalAddress(String companyPhysicalAddress) {
        this.companyPhysicalAddress = companyPhysicalAddress;
    }

    public String getCompanyPhysicalAddress() {
        return companyPhysicalAddress;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformName() {
        return platformName;
    }
}
