package uk.co.solong.provision.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.linode4j.Linode;
import uk.co.solong.provision.core.PlantConfig;


@Configuration
public class LinodeConfig {

    @Inject
    private PlantConfig plantConfig;
    
    @Bean
    public Linode linode() {
        Linode linode = new Linode(plantConfig.getLinodeKey());
        return linode;  
    }
}
