package com.github.sfxd.trust.inject;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Factory
public class PropertiesProducer {
    @Bean
    public Properties produceProperties() throws IOException {
        var properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(is);
        }

        return properties;
    }
}
