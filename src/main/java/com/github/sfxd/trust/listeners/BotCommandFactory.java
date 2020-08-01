package com.github.sfxd.trust.listeners;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.model.finders.InstanceFinder;
import com.github.sfxd.trust.model.finders.InstanceSubscriberFinder;
import com.github.sfxd.trust.model.finders.SubscriberFinder;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;
import com.github.sfxd.trust.model.services.SubscriberService;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@ApplicationScoped
class BotCommandFactory {
    static final String SUBSCRIBE = "subscribe";
    static final String UNSUBSCRIBE = "unsubscribe";
    static final String SOURCE = "source";

    private final InstanceSubscriberFinder isFinder;
    private final InstanceSubscriberService isService;
    private final InstanceFinder instanceFinder;
    private final SubscriberFinder subscriberFinder;
    private final SubscriberService subscriberService;

    @Inject
    BotCommandFactory(
        InstanceSubscriberFinder isFinder,
        InstanceSubscriberService isService,
        InstanceFinder instanceFinder,
        SubscriberFinder subscriberFinder,
        SubscriberService subscriberService
    ) {
        this.isFinder = isFinder;
        this.isService = isService;
        this.instanceFinder = instanceFinder;
        this.subscriberFinder = subscriberFinder;
        this.subscriberService = subscriberService;
    }

    BotCommand newInstance(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ", -1);
        if (!split[0].equalsIgnoreCase("!trust")) {
            return () -> {};
        }

        if (split.length < 2) {
            return new UsageBotCommand(event);
        }

        String command = split[1].toLowerCase();
        return switch (command) {
            case SUBSCRIBE, UNSUBSCRIBE -> {
                if (split.length < 3) {
                    yield new UsageBotCommand(event);
                }

                String key = split[2].toUpperCase();
                if (SUBSCRIBE.equals(command)) {
                    yield new SubscribeBotCommand(
                        event,
                        this.instanceFinder,
                        this.isFinder,
                        this.isService,
                        this.subscriberFinder,
                        this.subscriberService,
                        key
                    );
                } else {
                    yield new UnsubscribeBotCommand(event, key, this.isFinder, this.isService);
                }
            }
            case SOURCE -> new SourceBotCommand(event);
            default -> new UsageBotCommand(event);
        };
    }
}
