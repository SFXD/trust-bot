// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.discord;

import com.github.sfxd.trust.Message;
import com.github.sfxd.trust.Messages;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.JDA;

@Singleton
public class JdaMessages implements Messages {
    private final JDA jda;

	@Inject
    public JdaMessages(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void send(Message message) {
        this.jda.retrieveUserById(message.to().username()).queue(u -> {
            u.openPrivateChannel().queue(channel -> channel.sendMessage(message.body()).queue());
        });
    }
}
