// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Collection;

@Singleton
public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    static final String ERROR_MSG = "Oops! An unexpected error occurred. ❌";

    private final BotCommandFactory botCommandFactory;

    @Inject
    public MessageListener(BotCommandFactory botCommandFactory) {
        this.botCommandFactory = botCommandFactory;
    }

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

    public Collection<CommandData> commands() {
        return this.botCommandFactory.commands();
    }

    private void printError(SlashCommandEvent event) {
        event.reply(ERROR_MSG).queue();
    }
}
