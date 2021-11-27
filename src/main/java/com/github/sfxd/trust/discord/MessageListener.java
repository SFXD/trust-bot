// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listener that watches messages that start with !trust
 */
@Singleton
public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    static final String ERROR_MSG = "Oops! An unexpected error occurred. ❌";

    private final BotCommandFactory botCommandFactory;

    @Inject
    public MessageListener(BotCommandFactory botCommandFactory) {
        this.botCommandFactory = botCommandFactory;
    }

    /**
     * Handles the message received event. If the message doesn't start with !trust
     * it will do nothing, if it does if will check if the next word is one of our
     * supported commands (subscribe or unsubscribe). If the command doesn't have an
     * instance provided it will respond in the channel with how to use the command.
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        BotCommand command = this.botCommandFactory.of(event);
        try {
            command.run();
        } catch (RuntimeException ex) {
            LOGGER.error("Command failed: ", ex);
            this.printError(event);
        }
    }

    private void printError(SlashCommandEvent event) {
        event.reply(ERROR_MSG).queue();
    }
}
