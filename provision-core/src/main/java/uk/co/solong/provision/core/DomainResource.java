package uk.co.solong.provision.core;

public class DomainResource {
    private Integer domainId;
    private String type;
    private String name;
    private String target;
    private Integer priority;
    private Integer weight;
    private Integer port;
    private String protocol;
    private Integer ttlSec;
    public Integer getDomainId() {
        return domainId;
    }
    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public Integer getTtlSec() {
        return ttlSec;
    }
    public void setTtlSec(Integer ttlSec) {
        this.ttlSec = ttlSec;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
