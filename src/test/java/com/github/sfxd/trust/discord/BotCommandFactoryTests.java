package com.github.sfxd.trust.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberService;
import com.github.sfxd.trust.core.subscribers.SubscriberService;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class BotCommandFactoryTests {

    @Test
    void it_should_do_nothing_when_no_command_is_given() {
        var isService = mock(InstanceSubscriberService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceService = mock(InstanceService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn("");

        var factory = new BotCommandFactory(
            isService,
            subscriberService,
            instanceService
        );

        var command = factory.newInstance(event);
        command.run();

        verifyNoInteractions(isService, subscriberService, instanceService);
    }

    @Test
    void it_should_return_a_usage_command_when_no_command_is_given() {
        assertCommandClass("!trust", UsageBotCommand.class);
    }

    @Test
    void it_should_return_the_subscribe_command_on_subscribe() {
        assertCommandClass("!trust subscribe na99", SubscribeBotCommand.class);
    }

    @Test
    void it_should_return_the_unsubscribe_command_on_unsubscribe() {
        assertCommandClass("!trust unsubscribe na99", UnsubscribeBotCommand.class);
    }

    @Test
    void it_should_return_the_source_command_on_source() {
        assertCommandClass("!trust source", SourceBotCommand.class);
    }

    @Test
    void it_should_return_the_usage_command_on_junk_input() {
        assertCommandClass("!trust blah blah blah blah", UsageBotCommand.class);
    }

    @Test
    void it_should_return_the_usage_command_when_a_valid_command_has_no_args() {
        assertCommandClass("!trust subscribe", UsageBotCommand.class);
        assertCommandClass("!trust unsubscribe", UsageBotCommand.class);
    }

    private static void assertCommandClass(String args, Class<? extends BotCommand> clazz) {
        var isService = mock(InstanceSubscriberService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceService = mock(InstanceService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn(args);

        var factory = new BotCommandFactory(
            isService,
            subscriberService,
            instanceService
        );

        var command = factory.newInstance(event);
        assertEquals(command.getClass(), clazz);
    }
}
