package com.github.sfxd.trust.discord;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceRepository;
import com.github.sfxd.trust.core.users.User;

import com.github.sfxd.trust.core.users.UserRepository;
import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

class SubscribeBotCommandTests {

    @Test
    void it_should_respond_with_a_message_if_the_instance_is_not_found() {
        var subscriberService = mock(UserRepository.class);
        var instanceService = mock(InstanceRepository.class);
        var event = mock(SlashCommandEvent.class);
        var replyAction = mock(ReplyAction.class);

        when(event.reply(anyString())).thenReturn(replyAction);
        when(instanceService.findByKey(anyString())).thenReturn(null);

        var command = new SubscribeBotCommand(
            event,
            instanceService,
            subscriberService,
            "na99"
        );

        command.run();

        verify(event).reply(anyString());
        verify(replyAction).queue();
    }

    @Test
    void it_should_create_a_new_subscription_only_if_there_isnt_one() throws Exception {
        var subscriberService = mock(UserRepository.class);
        var instanceService = mock(InstanceRepository.class);
        var event = mock(SlashCommandEvent.class);
        var user = mock(net.dv8tion.jda.api.entities.User.class);
        var subscriber = new User("");
        subscriber.setId(1L);

        when(event.getUser()).thenReturn(user);
        when(user.getId()).thenReturn("");
        when(event.reply(anyString())).thenReturn(mock(ReplyAction.class));

        var instance = new Instance();
        instance.setId(1L);
        when(instanceService.findByKey(anyString())).thenReturn(instance);
        when(subscriberService.findByUsername(anyString())).thenReturn(subscriber);

        var command = new SubscribeBotCommand(
            event,
            instanceService,
            subscriberService,
            "na99"
        );

        command.run();
    }
}
