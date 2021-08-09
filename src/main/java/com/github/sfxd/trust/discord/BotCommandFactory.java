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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberService;
import com.github.sfxd.trust.core.subscribers.SubscriberService;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@ApplicationScoped
public class BotCommandFactory {
    private static final BotCommand NULL_COMMAND = new NullBotCommand();

    static final String SUBSCRIBE = "subscribe";
    static final String UNSUBSCRIBE = "unsubscribe";
    static final String SOURCE = "source";

    private final InstanceSubscriberService isService;
    private final SubscriberService subscriberService;
    private final InstanceService instanceService;

    @Inject
    BotCommandFactory(
        InstanceSubscriberService isService,
        SubscriberService subscriberService,
        InstanceService instanceService
    ) {
        this.isService = isService;
        this.subscriberService = subscriberService;
        this.instanceService = instanceService;
    }

    BotCommand newInstance(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ", -1);
        if (!split[0].equalsIgnoreCase("!trust")) {
            return NULL_COMMAND;
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
                        this.instanceService,
                        this.isService,
                        this.subscriberService,
                        key
                    );
                } else {
                    yield new UnsubscribeBotCommand(event, key, this.isService);
                }
            }
            case SOURCE -> new SourceBotCommand(event);
            default -> new UsageBotCommand(event);
        };
    }

    private static class NullBotCommand extends BotCommand {
        private NullBotCommand() {
            super(null);
        }

        public void run() {
            // do nothing
        }
    }
}
