package com.github.sfxd.trust.listeners;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

class SourceBotCommandTests {

    @Test
    void it_should_print_a_link_to_the_source_code() {
        var event = mock(MessageReceivedEvent.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);

        when(event.getChannel()).thenReturn(channel);
        when(channel.sendMessage(anyString())).thenReturn(action);

        var command = new SourceBotCommand(event);
        command.run();

        verify(channel).sendMessage(SourceBotCommand.GITHUB);
    }
}
