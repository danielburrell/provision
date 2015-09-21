package uk.co.solong.provision.core;
import java.util.List;

public class PlantConfig {

    private String linodeKey;
    private List<LinodeConfiguration> linodeConfigs;

    public String getLinodeKey() {
        return linodeKey;
    }

    public void setLinodeKey(String linodeKey) {
        this.linodeKey = linodeKey;
    }

    public List<LinodeConfiguration> getLinodeConfigs() {
        return linodeConfigs;
    }

    public void setLinodeConfigs(List<LinodeConfiguration> linodeConfigs) {
        this.linodeConfigs = linodeConfigs;
    }
}
