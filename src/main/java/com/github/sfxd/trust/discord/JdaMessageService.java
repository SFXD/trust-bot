// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.MessageService;
import com.github.sfxd.trust.core.users.User;

import net.dv8tion.jda.api.JDA;

@ApplicationScoped
class JdaMessageService implements MessageService {

    private final JDA jda;

    @Inject
    JdaMessageService(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void sendMessage(User user, String message) {
        this.jda.retrieveUserById(user.getUsername()).queue(u -> {
            u.openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
        });
    }
}
