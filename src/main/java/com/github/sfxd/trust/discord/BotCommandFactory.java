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

import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@ApplicationScoped
public class BotCommandFactory {
    static final String SUBSCRIBE = "subscribe";
    static final String UNSUBSCRIBE = "unsubscribe";
    static final String SOURCE = "source";
    static final String INSTANCE = "instance";

    private final InstanceSubscriberService isService;
    private final SubscriberService subscriberService;
    private final InstanceService instanceService;
    private final JDA jda;

    @Inject
    BotCommandFactory(
        InstanceSubscriberService isService,
        SubscriberService subscriberService,
        InstanceService instanceService,
        JDA jda
    ) {
        this.isService = isService;
        this.subscriberService = subscriberService;
        this.instanceService = instanceService;
        this.jda = jda;

        this.jda.upsertCommand(SUBSCRIBE, "subscribe to notifications about a sandbox.")
            .addOptions(
                new OptionData(OptionType.STRING, INSTANCE, "the sandbox instance you want to subscribe to.")
                    .setRequired(true)
            )
            .queue();

        this.jda.upsertCommand(UNSUBSCRIBE, "unsubscribe to notifications about a sandbox.")
            .addOptions(
                new OptionData(OptionType.STRING, INSTANCE, "the sandbox instance you want to subscribe to.")
                    .setRequired(true)
            )
            .queue();

        this.jda.upsertCommand(SOURCE, "get the source code.").queue();

        this.jda.upsertCommand("help", "prints usage instructions.").queue();
    }

    BotCommand newInstance(SlashCommandEvent event) {
        String command = event.getName();
        return switch (command) {
            case SUBSCRIBE, UNSUBSCRIBE -> {
                String instanceKey = event.getOption(INSTANCE).getAsString();
                if (StringUtils.isBlank(instanceKey)) {
                    yield new UsageBotCommand(event);
                }

                if (SUBSCRIBE.equals(command)) {
                    yield new SubscribeBotCommand(
                        event,
                        this.instanceService,
                        this.isService,
                        this.subscriberService,
                        instanceKey
                    );
                } else {
                    yield new UnsubscribeBotCommand(event, instanceKey, this.isService);
                }
            }
            case SOURCE -> new SourceBotCommand(event);
            default -> new UsageBotCommand(event);
        };
    }
}
