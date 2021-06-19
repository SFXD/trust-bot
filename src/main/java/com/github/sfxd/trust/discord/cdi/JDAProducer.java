// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
