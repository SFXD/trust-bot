// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceRepository;
import com.github.sfxd.trust.core.users.User;

import com.github.sfxd.trust.core.users.UserRepository;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


class SubscribeBotCommand extends BotCommand {

    private final InstanceRepository instanceRepository;
    private final UserRepository userRepository;
    private final String key;

    SubscribeBotCommand(
        SlashCommandEvent event,
        InstanceRepository instanceRepository,
        UserRepository userRepository,
        String key
    ) {
        super(event);

        this.instanceRepository = instanceRepository;
        this.userRepository = userRepository;
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
        user.subscribe(instance);
        this.userRepository.save(user);

        this.replyWithCheckMark();
    }

    private User findUser(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            user = new User(username);
        }

        return user;
    }
}
