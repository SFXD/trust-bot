package com.github.sfxd.trust;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.jboss.resteasy.microprofile.client.RestClientExtension;

import io.smallrye.config.inject.ConfigExtension;
import net.dv8tion.jda.api.JDA;

public class Main {
    public static void main(String[] args) throws Exception {
        var initializer = SeContainerInitializer.newInstance()
            .disableDiscovery()
            .addExtensions(new ConfigExtension(), new RestClientExtension())
            .addPackages(true, Main.class);

        try (SeContainer container = initializer.initialize()) {
            container.select(JDA.class).get().awaitReady();
        }
    }
}
