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

package com.github.sfxd.trust.listeners;

import java.util.Optional;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.model.finders.InstanceFinder;
import com.github.sfxd.trust.model.finders.InstanceSubscriberFinder;
import com.github.sfxd.trust.model.finders.SubscriberFinder;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;
import com.github.sfxd.trust.model.services.SubscriberService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class SubscribeBotCommand extends BotCommand {

    private final InstanceFinder instanceFinder;
    private final InstanceSubscriberFinder isFinder;
    private final InstanceSubscriberService isService;
    private final SubscriberFinder subscriberFinder;
    private final SubscriberService subscriberService;
    private final String key;

    SubscribeBotCommand(
        MessageReceivedEvent event,
        InstanceFinder instanceFinder,
        InstanceSubscriberFinder isFinder,
        InstanceSubscriberService isService,
        SubscriberFinder subscriberFinder,
        SubscriberService subscriberService,
        String key
    ) {
        super(event);

        this.instanceFinder = instanceFinder;
        this.isFinder = isFinder;
        this.isService = isService;
        this.subscriberFinder = subscriberFinder;
        this.subscriberService = subscriberService;
        this.key = key;
    }

    @Override
    public void run() {
        Optional<Instance> instance = this.instanceFinder.findByKey(key);

        if (instance.isEmpty()) {
            this.event.getChannel()
                .sendMessage(String.format("%s is not a valid instance key.", this.key)).queue();

            return;
        }

        String username = event.getAuthor().getId();
        Subscriber subscriber = this.subscriberFinder.findByUsername(username)
            .orElseGet(() -> new Subscriber(username));

        if (subscriber.isNew()) {
            try {
                this.subscriberService.insert(subscriber);
            } catch (DmlException ex) {
                throw new BotCommandException(ex);
            }

        }

        Optional<InstanceSubscriber> subscription = this.isFinder
            .findByInstanceIdAndSubscriberId(instance.get().getId(), subscriber.getId());

        if (subscription.isEmpty()) {
            try {
                this.isService.insert(new InstanceSubscriber(instance.get(), subscriber));
            } catch (DmlException ex) {
                throw new BotCommandException(ex);
            }
        }

        this.reactWithCheckMark();
    }

}
