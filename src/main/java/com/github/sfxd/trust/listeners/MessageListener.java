// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

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

import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.model.services.InstanceService;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;
import com.github.sfxd.trust.model.services.SubscriberService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listener that watches messages that start with !trust
 */
@Singleton
public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    static final String CHECK_MARK = "✅";
    static final String X = "❌";
    static final String USAGE = "Usage: \n" +
                                "  !trust <subscribe, unsubscribe> <instance_id>\n" +
                                "  !trust source\n";
    static final String GITHUB = "https://github.com/SFXD/trust-bot";
    static final String ERROR_MSG = "Oops! An unexpected error occured.";

    private final SubscriberService subscriberService;
    private final InstanceService instanceService;
    private final InstanceSubscriberService instanceSubscriberService;

    @Inject
    public MessageListener(
        SubscriberService subscriberService,
        InstanceService instanceService,
        InstanceSubscriberService instanceSubscriberService
    ) {
        Objects.requireNonNull(subscriberService);
        Objects.requireNonNull(instanceService);
        Objects.requireNonNull(instanceSubscriberService);

        this.subscriberService = subscriberService;
        this.instanceService = instanceService;
        this.instanceSubscriberService = instanceSubscriberService;
    }

    /**
     * Handles the message received event. If the message doesn't start with !trust
     * it will do nothing, if it does if will check if the next word is one of our
     * supported commands (subscribe or unsubscribe). If the command doesn't have an
     * instance provided it will respond in the channel with how to use the command.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ", -1);
        if (!split[0].equalsIgnoreCase("!trust")) {
            return;
        }

        if (split.length < 2) {
            this.printUsage(event);
            return;
        }

        String command = split[1].toLowerCase();
        String key = null;
        switch (command) {
        case "subscribe":
            key = split[2].toUpperCase();
            try {
                this.handleSubscribe(event, key);
            } catch (DmlException ex) {
                LOGGER.error("Failed subscribe.", ex);
                this.printError(event);
            }
            return;
        case "unsubscribe":
            key = split[2].toUpperCase();
            try {
                this.handleUnsubscribe(event, key);
            } catch (DmlException ex) {
                LOGGER.error("Failed unsubscribe.", ex);
                this.printError(event);
            }
            return;
        case "source":
            this.handleSource(event);
            return;
        default:
            this.printUsage(event);
            return;
        }
    }

    /**
     * Handles the subscribe command. This will sign a user up for notifications
     * for the given key.
     * @param event The event from JDA
     * @param key the instance key from the command.
     */
    private void handleSubscribe(MessageReceivedEvent event, String key) throws DmlException {
        Optional<Instance> instance = this.instanceService.findByKey(key).findOneOrEmpty();

        if (!instance.isPresent()) {
            event.getChannel().sendMessage(String.format("%s is not a valid instance key.", key)).queue();
            return;
        }

        String username = event.getAuthor().getId();
        Subscriber subscriber = this.subscriberService.findByUsername(username)
            .findOneOrEmpty()
            .orElseGet(() -> new Subscriber(username));

        if (subscriber.isNew()) {
            this.subscriberService.insert(subscriber);
        }

        Optional<InstanceSubscriber> subscription
            = this.instanceSubscriberService.findByInstanceIdAndSubscriberId(
                instance.get().getId(),
                subscriber.getId()
            )
            .findOneOrEmpty();

        if (!subscription.isPresent()) {
            this.instanceSubscriberService.insert(new InstanceSubscriber(instance.get(), subscriber));
        }

        event.getMessage().addReaction(CHECK_MARK).queue();
    }

    /**
     * Handles the unsubscribe command. This will delete the subscription if it
     * finds it.
     *
     * @param event The event from JDA.
     * @param key   The instance key from the command.
     */
    private void handleUnsubscribe(MessageReceivedEvent event, String key) throws DmlException {
        Optional<InstanceSubscriber> subscription
            = this.instanceSubscriberService.findByKeyAndUsername(
                key,
                event.getAuthor().getName()
            )
            .findOneOrEmpty();

        if (subscription.isPresent()) {
            this.instanceSubscriberService.delete(subscription.get());
        }

        event.getMessage().addReaction(CHECK_MARK).queue();
    }

    /**
     * Handles the source command. This should respond with a link to the source
     * code of this project.
     *
     * @param event the event from jda
     */
    private void handleSource(MessageReceivedEvent event) {
        event.getChannel().sendMessage(GITHUB).queue();
    }

    private void printUsage(MessageReceivedEvent event) {
        event.getChannel().sendMessage(USAGE).queue();
    }

    private void printError(MessageReceivedEvent event) {
        event.getChannel().sendMessage(ERROR_MSG).queue();
        event.getMessage().addReaction(X).queue();
    }
}
