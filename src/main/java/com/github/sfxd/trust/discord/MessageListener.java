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
        BotCommand command = this.botCommandFactory.newInstance(event);
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
