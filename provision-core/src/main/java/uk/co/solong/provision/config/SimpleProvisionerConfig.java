package uk.co.solong.provision.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.linode4j.Linode;
import uk.co.solong.provision.core.SimpleProvisioner;

@Configuration
public class SimpleProvisionerConfig {

    @Inject
    private Linode linode;

    @Bean
    public SimpleProvisioner simpleProvisioner() {
        SimpleProvisioner simpleProvisioner = new SimpleProvisioner(linode);
        return simpleProvisioner;
        
    }
}
