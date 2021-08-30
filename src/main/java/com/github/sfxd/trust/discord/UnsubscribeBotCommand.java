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

import java.util.Optional;

import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriber;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberService;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

/** Command for !trust unsubscribe <serverkey> */
class UnsubscribeBotCommand extends BotCommand {
    private final String key;
    private final InstanceSubscriberService isService;

    UnsubscribeBotCommand(
        SlashCommandEvent event,
        String key,
        InstanceSubscriberService isService
    ) {
        super(event);

        this.key = key;
        this.isService = isService;
    }

    @Override
    public void run() {
        Optional<InstanceSubscriber> subscription = this.isService
            .findByKeyAndUsername(this.key, this.event.getUser().getId());

        if (subscription.isPresent()) {
            this.isService.delete(subscription.get());
        }

        this.replyWithCheckMark();
    }
}
