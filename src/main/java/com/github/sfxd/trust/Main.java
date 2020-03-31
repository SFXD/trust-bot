package com.github.sfxd.trust;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import com.github.sfxd.trust.runtime.RuntimeManager;

import org.jboss.resteasy.microprofile.client.RestClientExtension;

import io.smallrye.config.inject.ConfigExtension;

public class Main {
    public static void main(String[] args) throws Exception {
        var initializer = SeContainerInitializer.newInstance()
            .disableDiscovery()
            .addExtensions(new ConfigExtension(), new RestClientExtension())
            .addPackages(true, Main.class);

        try (SeContainer container = initializer.initialize()) {
            container.select(RuntimeManager.class).get().onStart();
        }
    }
}
