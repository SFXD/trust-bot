package com.github.sfxd.trust.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

class ConfigProducer {

    @Produces
    @ApplicationScoped
    Config produceConfig() {
        return ConfigProvider.getConfig();
    }
}
