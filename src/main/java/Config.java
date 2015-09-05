import java.util.List;

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
    private String os;
    private String stackScriptLabel;
    private String stackScriptDescription;
    private boolean stackScriptPublic;
    private String stackScriptRevisionNote;
    private String stackScript;
    private List<Disk> disks;
    private String rootPassword;
    private String rootSshKey;
    
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

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getStackScriptLabel() {
        return stackScriptLabel;
    }

    public void setStackScriptLabel(String stackScriptLabel) {
        this.stackScriptLabel = stackScriptLabel;
    }

    public String getStackScriptDescription() {
        return stackScriptDescription;
    }

    public void setStackScriptDescription(String stackScriptDescription) {
        this.stackScriptDescription = stackScriptDescription;
    }

    public boolean isStackScriptPublic() {
        return stackScriptPublic;
    }

    public void setStackScriptPublic(boolean stackScriptPublic) {
        this.stackScriptPublic = stackScriptPublic;
    }

    public String getStackScriptRevisionNote() {
        return stackScriptRevisionNote;
    }

    public void setStackScriptRevisionNote(String stackScriptRevisionNote) {
        this.stackScriptRevisionNote = stackScriptRevisionNote;
    }

    public String getStackScript() {
        return stackScript;
    }

    public void setStackScript(String stackScript) {
        this.stackScript = stackScript;
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    public String getRootPassword() {
        return rootPassword;
    }

    public void setRootPassword(String rootPassword) {
        this.rootPassword = rootPassword;
    }

    public String getRootSshKey() {
        return rootSshKey;
    }

    public void setRootSshKey(String rootSshKey) {
        this.rootSshKey = rootSshKey;
    }
}
