// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.Message;
import com.github.sfxd.trust.core.Messages;

import net.dv8tion.jda.api.JDA;

@ApplicationScoped
class JdaMessages implements Messages {

    private final JDA jda;

    @Inject
    JdaMessages(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void send(Message message) {
        this.jda.retrieveUserById(message.to().getUsername()).queue(u -> {
            u.openPrivateChannel().queue(channel -> channel.sendMessage(message.body()).queue());
        });
    }
}
