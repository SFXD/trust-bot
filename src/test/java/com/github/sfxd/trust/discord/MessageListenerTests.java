package com.github.sfxd.trust.discord;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

class MessageListenerTests {

    @Test
    void it_should_run_whatever_the_factory_gives_it() {
        var factory = mock(BotCommandFactory.class);
        var command = mock(BotCommand.class);
        when(factory.of(any())).thenReturn(command);

        var listener = new MessageListener(factory);
        listener.onSlashCommand(null);

        verify(command).run();
    }

    @Test
    void it_should_print_an_error_message_when_a_command_errors() {
        var factory = mock(BotCommandFactory.class);
        var command = mock(BotCommand.class);
        var event = mock(SlashCommandEvent.class);
        var action = mock(ReplyAction.class);


        when(factory.of(any())).thenReturn(command);
        doThrow(RuntimeException.class).when(command).run();
        when(event.reply(anyString())).thenReturn(action);

        var listener = new MessageListener(factory);
        listener.onSlashCommand(event);

        verify(event).reply(MessageListener.ERROR_MSG);
    }
}
