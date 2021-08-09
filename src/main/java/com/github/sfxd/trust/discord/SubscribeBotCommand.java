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

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriber;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberService;
import com.github.sfxd.trust.core.subscribers.Subscriber;
import com.github.sfxd.trust.core.subscribers.SubscriberService;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class SubscribeBotCommand extends BotCommand {

    private final InstanceService instanceService;
    private final InstanceSubscriberService isService;
    private final SubscriberService subscriberService;
    private final String key;

    SubscribeBotCommand(
        MessageReceivedEvent event,
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
            this.event.getChannel()
                .sendMessage(String.format("%s is not a valid instance key.", this.key))
                .queue();

            return;
        }

        String username = event.getAuthor().getId();
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

        this.reactWithCheckMark();
    }

}
