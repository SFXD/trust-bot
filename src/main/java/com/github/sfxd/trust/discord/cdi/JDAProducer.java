// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

import com.github.sfxd.trust.discord.MessageListener;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.weld.environment.se.events.ContainerBeforeShutdown;
import org.jboss.weld.environment.se.events.ContainerInitialized;
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

    public void onStart(@Observes ContainerInitialized initialized, JDA jda)
        throws InterruptedException, IllegalStateException
    {
        LOGGER.info("Awaiting JDA ready");
        jda.awaitReady();
    }

    public void onStop(@Observes ContainerBeforeShutdown beforeShutdown, JDA jda) {
        LOGGER.info("Shutting down JDA");
        jda.shutdown();
    }
}
