// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import java.util.Optional;

import com.github.sfxd.trust.core.instanceusers.InstanceUser;
import com.github.sfxd.trust.core.instanceusers.InstanceUserService;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

/** Command for !trust unsubscribe <serverkey> */
class UnsubscribeBotCommand extends BotCommand {
    private final String key;
    private final InstanceUserService instanceUserService;

    UnsubscribeBotCommand(
        SlashCommandEvent event,
        String key,
        InstanceUserService isService
    ) {
        super(event);

        this.key = key;
        this.instanceUserService = isService;
    }

    @Override
    public void run() {
        Optional<InstanceUser> subscription = this.instanceUserService
            .findByKeyAndUsername(this.key, this.event.getUser().getId());

        if (subscription.isPresent()) {
            this.instanceUserService.delete(subscription.get());
        }

        this.replyWithCheckMark();
    }
}
