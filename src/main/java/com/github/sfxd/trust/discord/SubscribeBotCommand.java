// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import java.util.Optional;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.instanceusers.InstanceUser;
import com.github.sfxd.trust.core.instanceusers.InstanceUserService;
import com.github.sfxd.trust.core.users.UserService;
import com.github.sfxd.trust.core.users.User;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


class SubscribeBotCommand extends BotCommand {

    private final InstanceService instanceService;
    private final InstanceUserService instanceUserService;
    private final UserService userService;
    private final String key;

    SubscribeBotCommand(
        SlashCommandEvent event,
        InstanceService instanceService,
        InstanceUserService isService,
        UserService subscriberService,
        String key
    ) {
        super(event);

        this.instanceService = instanceService;
        this.instanceUserService = isService;
        this.userService = subscriberService;
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
        User user = this.userService.findByUsername(username)
            .orElseGet(() -> new User(username));

        if (user.isNew()) {
            this.userService.insert(user);
        }

        Optional<InstanceUser> subscription = this.instanceUserService
            .findByInstanceIdAndUserId(instance.get().getId(), user.getId());

        if (subscription.isEmpty()) {
            this.instanceUserService.insert(new InstanceUser(instance.get(), user));
        }

        this.replyWithCheckMark();
    }
}
