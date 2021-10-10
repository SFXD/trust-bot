package com.github.sfxd.trust.discord;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.github.sfxd.trust.core.instanceusers.InstanceUser;
import com.github.sfxd.trust.core.instanceusers.InstanceUserService;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

class UnsubscribeBotCommandTests {

    @Test
    void it_should_delete_the_subscription_if_found() throws Exception {
        var event = mock(SlashCommandEvent.class);
        var isService = mock(InstanceUserService.class);
        var key = "NA99";
        var username = "george";
        var user = mock(User.class);
        var subscription = new InstanceUser(null, null);
        var action = mock(ReplyAction.class);

        when(event.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(username);
        when(event.reply(anyString())).thenReturn(action);
        when(isService.findByKeyAndUsername(key, username)).thenReturn(Optional.of(subscription));

        var command = new UnsubscribeBotCommand(event, key, isService);
        command.run();

        verify(isService).delete(subscription);
    }
}
