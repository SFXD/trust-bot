// SPDX-License-Identifier: GPL-3.0-or-later
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
