// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord.cdi;

import com.github.sfxd.trust.discord.MessageListener;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

@Factory
public class JDAProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDAProducer.class);

    // This MUST be @Singleton scoped!!
    // Weld's proxying will cause JDA to not properly register
    // slash commands since the calls will be made on the proxy object.
    @Bean(initMethod = "awaitReady", destroyMethod = "shutdown")
    public JDA produceJDA(MessageListener listener) throws Exception {
        var jda = JDABuilder.createDefault(System.getProperty("trust.discord.token"))
            .addEventListeners(listener)
            .build();
        var _ = jda.updateCommands()
            .addCommands(listener.commands());

        return jda;
    }
}
