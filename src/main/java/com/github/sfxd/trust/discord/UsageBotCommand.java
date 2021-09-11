// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

class UsageBotCommand extends BotCommand {

    static final String USAGE = """
                                Usage:
                                    /<subscribe, unsubscribe> <instance_id>
                                    /source
                                    /help
                                """;

    UsageBotCommand(SlashCommandEvent event) {
        super(event);
    }

    @Override
    public void run() {
        this.event.reply(USAGE).queue();
    }
}
