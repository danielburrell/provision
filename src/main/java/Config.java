/**
 * Created by terabyte on 23/12/2014.
 */
public class Config {
    private String description;
    private String services;
    private String region;
    private String name;
    private String type;
    private String billing;
    private String dataSize;
    private String mountArea;
    private String mountName;

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    private int ram;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public String getDataSize() {
        return dataSize;
    }

    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
    }

    public String getMountArea() {
        return mountArea;
    }

    public void setMountArea(String mountArea) {
        this.mountArea = mountArea;
    }

    public String getMountName() {
        return mountName;
    }

    public void setMountName(String mountName) {
        this.mountName = mountName;
    }

    public String getPuppetScriptPath() {
        return puppetScriptPath;
    }

    public void setPuppetScriptPath(String puppetScriptPath) {
        this.puppetScriptPath = puppetScriptPath;
    }

    private String puppetScriptPath = name+"/"+name+".pp";
}
