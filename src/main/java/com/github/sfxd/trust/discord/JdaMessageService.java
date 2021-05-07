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

import com.github.sfxd.trust.core.MessageService;
import com.github.sfxd.trust.core.subscribers.Subscriber;

import net.dv8tion.jda.api.JDA;

@ApplicationScoped
class JdaMessageService implements MessageService {

    private final JDA jda;

    @Inject
    JdaMessageService(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void sendMessage(Subscriber user, String message) {
        this.jda.retrieveUserById(user.getUsername()).queue(u -> {
            u.openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
        });
    }
}
