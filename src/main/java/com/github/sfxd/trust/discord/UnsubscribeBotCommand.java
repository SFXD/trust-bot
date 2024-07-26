// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import com.github.sfxd.trust.core.subscription.Subscription;

import com.github.sfxd.trust.core.subscription.SubscriptionRepository;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

/** Command for !trust unsubscribe <serverkey> */
class UnsubscribeBotCommand extends BotCommand {
    private final String key;
    private final SubscriptionRepository repository;

    UnsubscribeBotCommand(SlashCommandEvent event, String key, SubscriptionRepository repository) {
        super(event);

        this.key = key;
        this.repository = repository;
    }

    @Override
    public void run() {
        Subscription subscription = this.repository.findByKeyAndUsername(this.key, this.event.getUser().getId());
        if (subscription != null)
            this.repository.delete(subscription);

        this.replyWithCheckMark();
    }
}
