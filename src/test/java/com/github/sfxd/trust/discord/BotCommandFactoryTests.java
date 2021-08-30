package com.github.sfxd.trust.discord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberService;
import com.github.sfxd.trust.core.subscribers.SubscriberService;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.JDA;

class BotCommandFactoryTests {
    @Test
    void it_should_return_a_usage_command_when_no_command_is_given() {
        assertCommandClass("help", UsageBotCommand.class);
    }

    @Test
    void it_should_return_the_subscribe_command_on_subscribe() {
        assertCommandClass(
            BotCommandFactory.SUBSCRIBE,
            Map.of(BotCommandFactory.INSTANCE, "na99"),
            SubscribeBotCommand.class
        );
    }

    @Test
    void it_should_return_the_unsubscribe_command_on_unsubscribe() {
        assertCommandClass(
            BotCommandFactory.UNSUBSCRIBE,
            Map.of(BotCommandFactory.INSTANCE, "na99"),
            UnsubscribeBotCommand.class
        );
    }

    @Test
    void it_should_return_the_source_command_on_source() {
        assertCommandClass(BotCommandFactory.SOURCE, SourceBotCommand.class);
    }

    private static void assertCommandClass(String name, Class<? extends BotCommand> clazz) {
        assertCommandClass(name, null, clazz);
    }

    private static void assertCommandClass(String name, Map<String, String> options, Class<? extends BotCommand> clazz) {
        var isService = mock(InstanceSubscriberService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceService = mock(InstanceService.class);
        var event = mock(SlashCommandEvent.class);
        var message = mock(Message.class);
        var jda = mock(JDA.class);
        var createAction = mock(CommandCreateAction.class);

        when(createAction.addOptions(any(OptionData.class))).thenReturn(createAction);
        when(jda.upsertCommand(anyString(), anyString())).thenReturn(createAction);
        when(event.getName()).thenReturn(name);
        when(message.getContentRaw()).thenReturn(name);
        if (options != null) {
            for (Map.Entry<String, String> entry : options.entrySet()) {
                var mapping = mock(OptionMapping.class);
                when(event.getOption(entry.getKey())).thenReturn(mapping);
                when(mapping.getAsString()).thenReturn(entry.getValue());
            }
        }

        var factory = new BotCommandFactory(
            isService,
            subscriberService,
            instanceService,
            jda
        );

        var command = factory.newInstance(event);
        assertEquals(command.getClass(), clazz);
    }
}
