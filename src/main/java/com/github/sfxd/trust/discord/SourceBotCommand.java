// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

class SourceBotCommand extends BotCommand {
    static final String GITHUB = "https://github.com/SFXD/trust-bot";

    SourceBotCommand(SlashCommandEvent event) {
        super(event);
    }

    @Override
    public void run() {
        this.event.reply(GITHUB).queue();
    }
}
