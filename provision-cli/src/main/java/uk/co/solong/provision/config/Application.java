package uk.co.solong.provision.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.solong.application.annotations.RootConfiguration;
import uk.co.solong.application.config.PropertyPlaceholderConfig;

@Configuration
@Import({ProvisionerCLIConfig.class, SimpleProvisionerConfig.class, LinodeConfig.class, PropertyPlaceholderConfig.class, PlantLoader.class})
@RootConfiguration
public class Application {

}
