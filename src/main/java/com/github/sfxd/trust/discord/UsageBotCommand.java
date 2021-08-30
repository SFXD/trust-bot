// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2021 George Doenlen

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
