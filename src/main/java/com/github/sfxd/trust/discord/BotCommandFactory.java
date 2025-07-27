// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.instances.InstanceRepository;
import com.github.sfxd.trust.core.users.UserRepository;
import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@ApplicationScoped
class BotCommandFactory {
    static final String SUBSCRIBE = "subscribe";
    static final String UNSUBSCRIBE = "unsubscribe";
    static final String SOURCE = "source";
    static final String INSTANCE = "instance";

    private final InstanceRepository instanceRepository;
    private final UserRepository userRepository;
    private final JDA jda;

    @Inject
    BotCommandFactory(
        InstanceRepository instanceRepository,
        UserRepository userRepository,
        JDA jda
    ) {
        this.instanceRepository = instanceRepository;
        this.userRepository = userRepository;
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

    BotCommand of(SlashCommandEvent event) {
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
                        this.instanceRepository,
                        this.userRepository,
                        instanceKey
                    );
                } else {
                    yield new UnsubscribeBotCommand(event, instanceKey, this.userRepository, this.instanceRepository);
                }
            }
            case SOURCE -> new SourceBotCommand(event);
            default -> new UsageBotCommand(event);
        };
    }
}
