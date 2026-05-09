// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.inject;

import com.github.sfxd.trust.discord.MessageListener;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.Properties;

@Factory
public class JDAProducer {
    @Bean(initMethod = "awaitReady", destroyMethod = "shutdown")
    public JDA produceJDA(MessageListener listener, Properties properties) throws Exception {
        var jda = JDABuilder.createDefault(properties.getProperty("discord.token"))
            .addEventListeners(listener)
            .build();
        var _ = jda.updateCommands()
            .addCommands(listener.commands());

        return jda;
    }
}
