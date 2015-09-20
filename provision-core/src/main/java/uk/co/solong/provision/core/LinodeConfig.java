package uk.co.solong.provision.core;
import java.util.List;
import java.util.Map;

/**
 * Created by terabyte on 23/12/2014.
 */
public class LinodeConfig {
    private String description;
    private String services;
    private String region;
    private String name;
    private String billing;
    private String os;
    private String stackScriptLabel;
    private String stackScriptDescription;
    private boolean stackScriptPublic;
    private String stackScriptRevisionNote;
    private String stackScript;
    private List<Disk> disks;
    private String rootPassword; //set this if you don't want to generateUnknownPassword
    private String rootSshKey;
    private boolean generateUnknownPassword;
    private boolean bit64Kernel; //set to true if you want to use 64 bit kernel
    private boolean latestKernel; //set to true if you want to use the latest kernel
    private String kernel; //optional provide the kernel manually if you don't want to use the latest.
    private Map<String,String> stackScriptResponses;
    
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

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
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

    public boolean isGenerateUnknownPassword() {
        return generateUnknownPassword;
    }

    public void setGenerateUnknownPassword(boolean generateUnknownPassword) {
        this.generateUnknownPassword = generateUnknownPassword;
    }

    public boolean isBit64Kernel() {
        return bit64Kernel;
    }

    public void setBit64Kernel(boolean bit64Kernel) {
        this.bit64Kernel = bit64Kernel;
    }

    public boolean isLatestKernel() {
        return latestKernel;
    }

    public void setLatestKernel(boolean latestKernel) {
        this.latestKernel = latestKernel;
    }

    public String getKernel() {
        return kernel;
    }

    public void setKernel(String kernel) {
        this.kernel = kernel;
    }

    public Map<String, String> getStackScriptResponses() {
        return stackScriptResponses;
    }

    public void setStackScriptResponses(Map<String, String> stackScriptResponses) {
        this.stackScriptResponses = stackScriptResponses;
    }
}
