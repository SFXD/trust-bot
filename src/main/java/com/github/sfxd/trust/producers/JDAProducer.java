package com.github.sfxd.trust.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.github.sfxd.trust.listeners.MessageListener;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

class JDAProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDAProducer.class);

    @Produces
    @ApplicationScoped
    JDA produceJDA(
        @ConfigProperty(name = "trust.discord.token") String token,
        MessageListener listener
    ) throws Exception {
        return JDABuilder.createDefault(token)
            .setActivity(Activity.playing("In Vip we trust."))
            .addEventListeners(listener)
            .build();
    }
}
