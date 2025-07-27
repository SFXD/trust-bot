// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import com.github.sfxd.trust.core.instances.InstanceRepository;

import com.github.sfxd.trust.core.users.UserRepository;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

/// Command for /unsubscribe <server-key>
class UnsubscribeBotCommand extends BotCommand {
    private final String key;
    private final UserRepository repository;
    private final InstanceRepository instanceRepository;

    UnsubscribeBotCommand(
        SlashCommandEvent event,
        String key,
        UserRepository repository,
        InstanceRepository instanceRepository
    ) {
        super(event);

        this.key = key;
        this.repository = repository;
        this.instanceRepository = instanceRepository;
    }

    @Override
    public void run() {
        var user = this.repository.findByUsername(this.event.getUser().getId());
        if (user != null) {
            var instance = this.instanceRepository.findByKey(this.key);
            if (instance != null) {
                user.unsubscribe(instance);
                this.repository.save(user);
            }
        }

        this.replyWithCheckMark();
    }
}
