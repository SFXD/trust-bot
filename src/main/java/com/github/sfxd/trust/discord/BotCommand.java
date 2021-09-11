// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

abstract class BotCommand implements Runnable {
    static final String CHECK_MARK = "✅";

    protected final SlashCommandEvent event;

    protected BotCommand(SlashCommandEvent event) {
        this.event = event;
    }

    protected void replyWithCheckMark() {
        this.event.reply(CHECK_MARK).queue();
    }
}
