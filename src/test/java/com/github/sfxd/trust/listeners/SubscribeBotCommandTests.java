package com.github.sfxd.trust.listeners;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.model.finders.InstanceFinder;
import com.github.sfxd.trust.model.finders.InstanceSubscriberFinder;
import com.github.sfxd.trust.model.finders.SubscriberFinder;
import com.github.sfxd.trust.model.query.QInstance;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;
import com.github.sfxd.trust.model.services.SubscriberService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

class SubscribeBotCommandTests {

    @Test
    void it_should_subscribe_insert_a_new_subscriber_on_subscribe_if_one_isnt_found() throws DmlException {
        var subscriberService = mock(SubscriberService.class);
        var isService = mock(InstanceSubscriberService.class);
        var instanceFinder = mock(InstanceFinder.class);
        var isFinder = mock(InstanceSubscriberFinder.class);
        var subscriberFinder = mock(SubscriberFinder.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);
        var user = mock(User.class);

        @SuppressWarnings("unchecked")
        var restAction = (RestAction<Void>) mock(RestAction.class);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn("!trust subscribe na99");
        when(message.addReaction(anyString())).thenReturn(restAction);
        when(event.getChannel()).thenReturn(channel);
        when(event.getMessageId()).thenReturn("");
        when(channel.sendMessage(anyString())).thenReturn(action);
        when(channel.addReactionById(anyString(), anyString())).thenReturn(restAction);
        when(event.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn("");

        var instance = new Instance();
        instance.setId(1L);
        when(instanceFinder.findByKey(anyString())).thenReturn(Optional.of(instance));
        when(subscriberFinder.findByUsername(anyString())).thenReturn(Optional.empty());
        when(isFinder.findByInstanceIdAndSubscriberId(anyLong(), any()))
            .thenReturn(Optional.empty());

        var command = new SubscribeBotCommand(
            event,
            instanceFinder,
            isFinder,
            isService,
            subscriberFinder,
            subscriberService,
            "na99"
        );

        command.run();

        verify(subscriberService).insert(new Subscriber(""));
    }

    @Test
    void it_should_respond_with_a_message_if_the_instance_is_not_found() {
        var subscriberService = mock(SubscriberService.class);
        var isService = mock(InstanceSubscriberService.class);
        var instanceFinder = mock(InstanceFinder.class);
        var isFinder = mock(InstanceSubscriberFinder.class);
        var subscriberFinder = mock(SubscriberFinder.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn("!trust subscribe na99");
        when(event.getChannel()).thenReturn(channel);
        when(channel.sendMessage(anyString())).thenReturn(action);
        when(instanceFinder.findByKey(anyString())).thenReturn(Optional.empty());

        var command = new SubscribeBotCommand(
            event,
            instanceFinder,
            isFinder,
            isService,
            subscriberFinder,
            subscriberService,
            "na99"
        );

        command.run();

        verify(channel).sendMessage(anyString());
        verify(action).queue();
    }

    @Test
    void it_should_create_a_new_subscription_only_if_there_isnt_one() throws Exception {
        var subscriberService = mock(SubscriberService.class);
        var isService = mock(InstanceSubscriberService.class);
        var instanceFinder = mock(InstanceFinder.class);
        var isFinder = mock(InstanceSubscriberFinder.class);
        var subscriberFinder = mock(SubscriberFinder.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);
        var user = mock(User.class);
        var qinstance =  mock(QInstance.class);
        var subscriber = new Subscriber("");
        subscriber.setId(1L);

        @SuppressWarnings("unchecked")
        var restAction = (RestAction<Void>) mock(RestAction.class);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn("!trust subscribe na99");
        when(message.addReaction(anyString())).thenReturn(restAction);
        when(event.getChannel()).thenReturn(channel);
        when(event.getMessageId()).thenReturn("");
        when(channel.sendMessage(anyString())).thenReturn(action);
        when(channel.addReactionById(anyString(), anyString())).thenReturn(restAction);
        when(event.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn("");

        var instance = new Instance();
        instance.setId(1L);
        when(instanceFinder.findByKey(anyString())).thenReturn(Optional.of(instance));
        when(qinstance.findOneOrEmpty()).thenReturn(Optional.of(instance));
        when(subscriberFinder.findByUsername(anyString())).thenReturn(Optional.of(subscriber));
        when(isFinder.findByInstanceIdAndSubscriberId(anyLong(), anyLong()))
            .thenReturn(Optional.empty());

        var command = new SubscribeBotCommand(
            event,
            instanceFinder,
            isFinder,
            isService,
            subscriberFinder,
            subscriberService,
            "na99"
        );

        command.run();
        verify(isService).insert(new InstanceSubscriber(instance, new Subscriber("")));
    }
}
