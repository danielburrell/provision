package uk.co.solong.provision.cli;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.solong.application.annotations.CommandLine;
import uk.co.solong.provision.core.LinodeConfig;
import uk.co.solong.provision.core.LinodeExists;
import uk.co.solong.provision.core.PlantConfig;
import uk.co.solong.provision.core.Provisioner;
import asg.cliche.Command;
import asg.cliche.Param;

@CommandLine(prompt = ">")
public class ProvisionerCLI {

    private static final Logger logger = LoggerFactory.getLogger(ProvisionerCLI.class);
    private final Provisioner provisioner;
    private final PlantConfig plantConfig;
    
    @Command(description = "Lists all machines in this configuration")
    public void machines() {
        for (LinodeConfig config : plantConfig.getLinodeConfigs()){
            logger.info(config.getName());
        }
    }
    
    @Command(description = "Upgrades the existing server without destroying data.")
    public void upgrade(@Param(name = "Machine") String machine) {
        for (LinodeConfig config : plantConfig.getLinodeConfigs()){
            if (config.getName().equals(machine)) {
                provisioner.rebuildKeepData(config);
            }
        }
    }

    @Command(description = "Destroys and rebuilds the server. All data will be lost")
    public void destroyRebuild(@Param(name = "Machine") String machine) {
        for (LinodeConfig config : plantConfig.getLinodeConfigs()){
            if (config.getName().equals(machine)) {
                try {
                    provisioner.rebuildLoseData(config);
                } catch (IOException | LinodeExists | InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Command(description = "Destroys the server. All data will be lost")
    public void nuke(@Param(name = "Machine") String machine) {
        for (LinodeConfig config : plantConfig.getLinodeConfigs()){
            if (config.getName().equals(machine)) {
                provisioner.destroy(config);
            }
        }
    }

    @Command(description = "Builds the server for the first time.")
    public void build(@Param(name = "Machine") String machine) throws IOException, LinodeExists, InterruptedException {
        for (LinodeConfig config : plantConfig.getLinodeConfigs()){
            if (config.getName().equals(machine)) {
                provisioner.buildFirstTime(config);
            }
        }
    }
    
    public ProvisionerCLI(Provisioner provisioner, PlantConfig plantConfig) {
        this.provisioner = provisioner;
        this.plantConfig = plantConfig;
    }
}
