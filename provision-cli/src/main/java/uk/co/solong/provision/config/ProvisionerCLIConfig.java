package uk.co.solong.provision.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.provision.cli.ProvisionerCLI;
import uk.co.solong.provision.core.PlantConfig;
import uk.co.solong.provision.core.SimpleProvisioner;

@Configuration
public class ProvisionerCLIConfig {

    @Inject
    private SimpleProvisioner provisioner;
    @Inject
    private PlantConfig plantConfig;

    @Bean
    public ProvisionerCLI provisionerCLI() {
        ProvisionerCLI provisionerCLI = new ProvisionerCLI(provisioner, plantConfig);
        return provisionerCLI;
        
    }
}
