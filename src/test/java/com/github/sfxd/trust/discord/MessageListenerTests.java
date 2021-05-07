package com.github.sfxd.trust.discord;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

class MessageListenerTests {

    @Test
    void it_should_run_whatever_the_factory_gives_it() {
        var factory = mock(BotCommandFactory.class);
        var command = mock(BotCommand.class);
        when(factory.newInstance(any())).thenReturn(command);

        var listener = new MessageListener(factory);
        listener.onMessageReceived(null);

        verify(command).run();
    }

    @Test
    void it_should_print_an_error_message_when_a_command_errors() {
        var factory = mock(BotCommandFactory.class);
        var command = mock(BotCommand.class);
        var event = mock(MessageReceivedEvent.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);
        var message = mock(Message.class);

        @SuppressWarnings("unchecked")
        var restAction = (RestAction<Void>) mock(RestAction.class);

        when(factory.newInstance(any())).thenReturn(command);
        doThrow(BotCommandException.class).when(command).run();
        when(event.getChannel()).thenReturn(channel);
        when(channel.sendMessage(anyString())).thenReturn(action);
        when(event.getMessage()).thenReturn(message);
        when(message.addReaction(anyString())).thenReturn(restAction);

        var listener = new MessageListener(factory);
        listener.onMessageReceived(event);

        verify(channel).sendMessage(MessageListener.ERROR_MSG);
    }
}
