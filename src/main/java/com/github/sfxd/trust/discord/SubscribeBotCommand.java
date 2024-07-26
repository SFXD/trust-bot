// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import java.util.Optional;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceRepository;
import com.github.sfxd.trust.core.subscription.Subscription;
import com.github.sfxd.trust.core.subscription.SubscriptionRepository;
import com.github.sfxd.trust.core.users.User;

import com.github.sfxd.trust.core.users.UserRepository;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


class SubscribeBotCommand extends BotCommand {

    private final InstanceRepository instanceRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final String key;

    SubscribeBotCommand(
        SlashCommandEvent event,
        InstanceRepository instanceRepository,
        UserRepository userRepository,
        SubscriptionRepository subscriptionRepository,
        String key
    ) {
        super(event);

        this.instanceRepository = instanceRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.key = key;
    }

    @Override
    public void run() {
        Instance instance = this.instanceRepository.findByKey(key);
        if (instance == null) {
            this.event.reply(String.format("%s is not a valid instance key.", this.key))
                .queue();

            return;
        }

        String username = this.event.getUser().getId();
        User user = this.findUser(username);

        Subscription subscription = null;
        if (!user.isNew()) {
            subscription = this.subscriptionRepository.findByInstanceAndUser(instance, user);
            if (subscription == null)
                this.subscriptionRepository.insert(new Subscription(instance, user));
        }

        this.replyWithCheckMark();
    }

    private User findUser(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null)
            user = new User(username);

        return user;
    }
}
