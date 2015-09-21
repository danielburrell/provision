package uk.co.solong.provision.core;

import java.util.List;

public class Domain {
    private String domain;
    private String description;
    private String type;
    private String soaEmail;
    private Integer refreshSec;
    private Integer retrySec;
    private Integer expireSec;
    private Integer ttlSec;
    private String lpmDisplayGroup;
    private Integer status;
    private String masterIps;
    private String axfrIps;
    private List<DomainResource> domainResources;
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSoaEmail() {
        return soaEmail;
    }
    public void setSoaEmail(String soaEmail) {
        this.soaEmail = soaEmail;
    }
    public Integer getRefreshSec() {
        return refreshSec;
    }
    public void setRefreshSec(Integer refreshSec) {
        this.refreshSec = refreshSec;
    }
    public Integer getRetrySec() {
        return retrySec;
    }
    public void setRetrySec(Integer retrySec) {
        this.retrySec = retrySec;
    }

    public Integer getTtlSec() {
        return ttlSec;
    }
    public void setTtlSec(Integer ttlSec) {
        this.ttlSec = ttlSec;
    }
    public String getLpmDisplayGroup() {
        return lpmDisplayGroup;
    }
    public void setLpmDisplayGroup(String lpmDisplayGroup) {
        this.lpmDisplayGroup = lpmDisplayGroup;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getMasterIps() {
        return masterIps;
    }
    public void setMasterIps(String masterIps) {
        this.masterIps = masterIps;
    }
    public String getAxfrIps() {
        return axfrIps;
    }
    public void setAxfrIps(String axfrIps) {
        this.axfrIps = axfrIps;
    }
    public List<DomainResource> getDomainResources() {
        return domainResources;
    }
    public void setDomainResources(List<DomainResource> domainResources) {
        this.domainResources = domainResources;
    }
    public Integer getExpireSec() {
        return expireSec;
    }
    public void setExpireSec(Integer expireSec) {
        this.expireSec = expireSec;
    }
}
