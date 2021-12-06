package com.github.sfxd.trust.discord;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.instanceusers.InstanceUser;
import com.github.sfxd.trust.core.instanceusers.InstanceUserService;
import com.github.sfxd.trust.core.users.UserService;
import com.github.sfxd.trust.core.users.User;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

class SubscribeBotCommandTests {

    @Test
    void it_should_respond_with_a_message_if_the_instance_is_not_found() {
        var subscriberService = mock(UserService.class);
        var isService = mock(InstanceUserService.class);
        var instanceService = mock(InstanceService.class);
        var event = mock(SlashCommandEvent.class);
        var replyAction = mock(ReplyAction.class);

        when(event.reply(anyString())).thenReturn(replyAction);
        when(instanceService.findByKey(anyString())).thenReturn(Optional.empty());

        var command = new SubscribeBotCommand(
            event,
            instanceService,
            isService,
            subscriberService,
            "na99"
        );

        command.run();

        verify(event).reply(anyString());
        verify(replyAction).queue();
    }

    @Test
    void it_should_create_a_new_subscription_only_if_there_isnt_one() throws Exception {
        var subscriberService = mock(UserService.class);
        var isService = mock(InstanceUserService.class);
        var instanceService = mock(InstanceService.class);
        var event = mock(SlashCommandEvent.class);
        var user = mock(net.dv8tion.jda.api.entities.User.class);
        var subscriber = new User("");
        subscriber.setId(1L);

        when(event.getUser()).thenReturn(user);
        when(user.getId()).thenReturn("");
        when(event.reply(anyString())).thenReturn(mock(ReplyAction.class));

        var instance = new Instance();
        instance.setId(1L);
        when(instanceService.findByKey(anyString())).thenReturn(Optional.of(instance));
        when(subscriberService.findByUsername(anyString())).thenReturn(Optional.of(subscriber));
        when(isService.findByInstanceIdAndUserId(anyLong(), anyLong()))
            .thenReturn(Optional.empty());

        var command = new SubscribeBotCommand(
            event,
            instanceService,
            isService,
            subscriberService,
            "na99"
        );

        command.run();
        verify(isService).insert(new InstanceUser(instance, new User("")));
    }
}
