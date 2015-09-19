package uk.co.solong.provision.core;
import java.util.List;

public class PlantConfig {

    private String linodeKey;
    private List<LinodeConfig> linodeConfigs;

    public String getLinodeKey() {
        return linodeKey;
    }

    public void setLinodeKey(String linodeKey) {
        this.linodeKey = linodeKey;
    }

    public List<LinodeConfig> getLinodeConfigs() {
        return linodeConfigs;
    }

    public void setLinodeConfigs(List<LinodeConfig> linodeConfigs) {
        this.linodeConfigs = linodeConfigs;
    }
}
