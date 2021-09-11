// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import java.util.Optional;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriber;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberService;
import com.github.sfxd.trust.core.subscribers.Subscriber;
import com.github.sfxd.trust.core.subscribers.SubscriberService;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


class SubscribeBotCommand extends BotCommand {

    private final InstanceService instanceService;
    private final InstanceSubscriberService isService;
    private final SubscriberService subscriberService;
    private final String key;

    SubscribeBotCommand(
        SlashCommandEvent event,
        InstanceService instanceService,
        InstanceSubscriberService isService,
        SubscriberService subscriberService,
        String key
    ) {
        super(event);

        this.instanceService = instanceService;
        this.isService = isService;
        this.subscriberService = subscriberService;
        this.key = key;
    }

    @Override
    public void run() {
        Optional<Instance> instance = this.instanceService.findByKey(key);

        if (instance.isEmpty()) {
            this.event.reply(String.format("%s is not a valid instance key.", this.key))
                .queue();

            return;
        }

        String username = this.event.getUser().getId();
        Subscriber subscriber = this.subscriberService.findByUsername(username)
            .orElseGet(() -> new Subscriber(username));

        if (subscriber.isNew()) {
            this.subscriberService.insert(subscriber);
        }

        Optional<InstanceSubscriber> subscription = this.isService
            .findByInstanceIdAndSubscriberId(instance.get().getId(), subscriber.getId());

        if (subscription.isEmpty()) {
            this.isService.insert(new InstanceSubscriber(instance.get(), subscriber));
        }

        this.replyWithCheckMark();
    }
}
