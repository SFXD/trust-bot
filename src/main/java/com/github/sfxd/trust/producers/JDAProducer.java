package com.github.sfxd.trust.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.github.sfxd.trust.listeners.MessageListener;
import com.github.sfxd.trust.runtime.RuntimeManager.StartupHook;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

class JDAProducer {

    @Produces
    @ApplicationScoped
    JDA produceJDA(
        @ConfigProperty(name = "trust.discord.token") String token,
        MessageListener listener
    ) throws Exception {
        return new JDABuilder(token)
                .setActivity(Activity.playing("In Vip we trust."))
                .addEventListeners(listener)
                .build();
    }

    @Produces
    @ApplicationScoped
    StartupHook produceJDAstartupHook(JDA jda) {
        return () -> {
            try {
                jda.awaitReady();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
