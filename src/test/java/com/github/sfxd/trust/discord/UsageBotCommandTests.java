package com.github.sfxd.trust.discord;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

class UsageBotCommandTests {

    @Test
    void it_should_print_usage() {
        var event = mock(SlashCommandEvent.class);
        var action = mock(ReplyAction.class);

        when(event.reply(anyString())).thenReturn(action);

        var command = new UsageBotCommand(event);
        command.run();

        verify(event).reply(UsageBotCommand.USAGE);
    }
}
