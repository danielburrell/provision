package uk.co.solong.provision.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.provision.core.PlantConfig;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class PlantLoader {

    @Value("${defaultConfig}")
    private String defaultConfig;
    @Bean
    public PlantConfig plantConfig() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(defaultConfig), PlantConfig.class);
    }
}
