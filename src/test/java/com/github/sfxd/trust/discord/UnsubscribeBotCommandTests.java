package com.github.sfxd.trust.discord;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.sfxd.trust.core.instances.InstanceRepository;
import com.github.sfxd.trust.core.users.UserRepository;
import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

class UnsubscribeBotCommandItTests {

    @Test
    void it_should_delete_the_subscription_if_found() throws Exception {
        var event = mock(SlashCommandEvent.class);
        var key = "NA99";
        var username = "george";
        var user = mock(User.class);
        var action = mock(ReplyAction.class);

        when(event.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(username);
        when(event.reply(anyString())).thenReturn(action);

        var command = new UnsubscribeBotCommand(event, key, new UserRepository(), new InstanceRepository());
        command.run();
    }
}
