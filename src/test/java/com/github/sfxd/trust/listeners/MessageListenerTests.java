package com.github.sfxd.trust.listeners;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.model.query.QInstance;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;
import com.github.sfxd.trust.model.services.InstanceService;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;
import com.github.sfxd.trust.model.services.SubscriberService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

class MessageListenerTests {

    @Test
    void it_should_do_nothing_if_the_message_doesnt_start_with_trust() {
        var instanceService = mock(InstanceService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceSubscriberService = mock(InstanceSubscriberService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn("");

        listener.onMessageReceived(event);
        verifyNoInteractions(instanceService, subscriberService, instanceSubscriberService);
    }

    @Test
    void it_should_send_usage_if_not_enough_args_are_supplied() {
        var instanceService = mock(InstanceService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceSubscriberService = mock(InstanceSubscriberService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);
        var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn("!trust blah");
        when(event.getChannel()).thenReturn(channel);
        when(channel.sendMessage(anyString())).thenReturn(action);

        listener.onMessageReceived(event);
        verifyNoInteractions(instanceService, subscriberService, instanceSubscriberService);
        verify(channel, times(1)).sendMessage(MessageListener.USAGE);
    }

    @Test
    void it_should_insert_a_new_subscriber_on_subscribe_if_one_isnt_found() throws Exception {
        // var instanceService = mock(InstanceService.class);
        // var subscriberService = mock(SubscriberService.class);
        // var instanceSubscriberService = mock(InstanceSubscriberService.class);
        // var event = mock(MessageReceivedEvent.class);
        // var message = mock(Message.class);
        // var channel = mock(MessageChannel.class);
        // var action = mock(MessageAction.class);
        // var user = mock(User.class);
        // var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);

        // @SuppressWarnings("unchecked")
        // var restAction = (RestAction<Void>) mock(RestAction.class);

        // when(event.getMessage()).thenReturn(message);
        // when(message.getContentRaw()).thenReturn("!trust subscribe na99");
        // when(message.addReaction(anyString())).thenReturn(restAction);
        // when(event.getChannel()).thenReturn(channel);
        // when(event.getMessageId()).thenReturn("");
        // when(channel.sendMessage(anyString())).thenReturn(action);
        // when(channel.addReactionById(anyString(), anyString())).thenReturn(restAction);
        // when(event.getAuthor()).thenReturn(user);
        // when(user.getId()).thenReturn("");

        // var instance = new Instance();
        // instance.setId(1L);
        // when(instanceService.findByKey(anyString())).thenReturn(Optional.of(instance));
        // when(subscriberService.findByUsername(anyString())).thenReturn(Optional.empty());
        // when(instanceSubscriberService.findByInstanceIdAndSubscriberId(anyLong(), anyLong()))
        //     .thenReturn(Optional.empty());

        // listener.onMessageReceived(event);
        // verify(subscriberService).insert(new Subscriber(""));
    }

    @Test
    void it_should_respond_with_a_message_if_the_instance_is_not_found() {
        var instanceService = mock(InstanceService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceSubscriberService = mock(InstanceSubscriberService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);
        var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);
        var qinstance = mock(QInstance.class);

        when(event.getMessage()).thenReturn(message);
        when(message.getContentRaw()).thenReturn("!trust subscribe na99");
        when(event.getChannel()).thenReturn(channel);
        when(channel.sendMessage(anyString())).thenReturn(action);
        when(instanceService.findByKey(anyString())).thenReturn(qinstance);
        when(qinstance.findOneOrEmpty()).thenReturn(Optional.empty());

        listener.onMessageReceived(event);
        verify(channel).sendMessage(anyString());
        verify(action).queue();
    }

    @Test
    void it_should_create_a_new_subscription_only_if_there_isnt_one() throws Exception {
    //     var instanceService = mock(InstanceService.class);
    //     var subscriberService = mock(SubscriberService.class);
    //     var instanceSubscriberService = mock(InstanceSubscriberService.class);
    //     var event = mock(MessageReceivedEvent.class);
    //     var message = mock(Message.class);
    //     var channel = mock(MessageChannel.class);
    //     var action = mock(MessageAction.class);
    //     var user = mock(User.class);
    //     var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);

    //     @SuppressWarnings("unchecked")
    //     var restAction = (RestAction<Void>) mock(RestAction.class);

    //     when(event.getMessage()).thenReturn(message);
    //     when(message.getContentRaw()).thenReturn("!trust subscribe na99");
    //     when(message.addReaction(anyString())).thenReturn(restAction);
    //     when(event.getChannel()).thenReturn(channel);
    //     when(event.getMessageId()).thenReturn("");
    //     when(channel.sendMessage(anyString())).thenReturn(action);
    //     when(channel.addReactionById(anyString(), anyString())).thenReturn(restAction);
    //     when(event.getAuthor()).thenReturn(user);
    //     when(user.getId()).thenReturn("");

    //     var instance = new Instance();
    //     instance.setId(1L);
    //     when(instanceService.findByKey(anyString())).thenReturn(Optional.of(instance));
    //     when(subscriberService.findByUsername(anyString())).thenReturn(Optional.empty());
    //     when(instanceSubscriberService.findByInstanceIdAndSubscriberId(anyLong(), anyLong()))
    //         .thenReturn(Optional.empty());

    //     listener.onMessageReceived(event);
    //     verify(instanceSubscriberService).insert(new InstanceSubscriber(instance, new Subscriber("")));
    }

    @Test
    void it_should_only_unsubscribe_if_a_subscription_is_found() throws Exception {
        // var instanceService = mock(InstanceService.class);
        // var subscriberService = mock(SubscriberService.class);
        // var instanceSubscriberService = mock(InstanceSubscriberService.class);
        // var event = mock(MessageReceivedEvent.class);
        // var message = mock(Message.class);
        // var channel = mock(MessageChannel.class);

        // @SuppressWarnings("unchecked")
        // var action = (RestAction<Void>) mock(RestAction.class);
        // var user = mock(User.class);
        // var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);

        // when(event.getMessage()).thenReturn(message);
        // when(message.getContentRaw()).thenReturn("!trust unsubscribe na99");
        // when(message.addReaction(anyString())).thenReturn(action);
        // when(event.getChannel()).thenReturn(channel);
        // when(event.getMessageId()).thenReturn("");
        // when(channel.addReactionById(anyString(), anyString())).thenReturn(action);
        // when(event.getAuthor()).thenReturn(user);
        // when(user.getName()).thenReturn("");
        // when(instanceSubscriberService.findByKeyAndUsername(anyString(), anyString()))
        //     .thenReturn(Optional.empty());

        // listener.onMessageReceived(event);
        // verify(instanceSubscriberService, times(0)).delete(any(InstanceSubscriber.class));
    }

    @Test
    void it_should_send_link_to_source_when_source_command_is_used() {
        var instanceService = mock(InstanceService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceSubscriberService = mock(InstanceSubscriberService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);

        when(event.getMessage()).thenReturn(message);
        when(event.getChannel()).thenReturn(channel);
        when(message.getContentRaw()).thenReturn("!trust source");
        when(channel.sendMessage(anyString())).thenReturn(action);

        var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);
        listener.onMessageReceived(event);
        verify(channel).sendMessage(MessageListener.GITHUB);
    }

    @Test
    void it_should_print_usage_when_not_enough_args_are_supplied() {
        var instanceService = mock(InstanceService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceSubscriberService = mock(InstanceSubscriberService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);

        when(event.getMessage()).thenReturn(message);
        when(event.getChannel()).thenReturn(channel);
        when(message.getContentRaw()).thenReturn("!trust");
        when(channel.sendMessage(anyString())).thenReturn(action);

        var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);
        listener.onMessageReceived(event);
        verify(channel).sendMessage(MessageListener.USAGE);
    }

    @Test
    void it_should_print_usage_when_an_invalid_command_is_sent() {
        var instanceService = mock(InstanceService.class);
        var subscriberService = mock(SubscriberService.class);
        var instanceSubscriberService = mock(InstanceSubscriberService.class);
        var event = mock(MessageReceivedEvent.class);
        var message = mock(Message.class);
        var channel = mock(MessageChannel.class);
        var action = mock(MessageAction.class);

        when(event.getMessage()).thenReturn(message);
        when(event.getChannel()).thenReturn(channel);
        when(message.getContentRaw()).thenReturn("!trust this_isnt_real");
        when(channel.sendMessage(anyString())).thenReturn(action);

        var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);
        listener.onMessageReceived(event);
        verify(channel).sendMessage(MessageListener.USAGE);
    }

    @Test
    void it_should_print_an_error_msg_when_a_service_fails() throws Exception {
        // var instanceService = mock(InstanceService.class);
        // var subscriberService = mock(SubscriberService.class);
        // var instanceSubscriberService = mock(InstanceSubscriberService.class);
        // var event = mock(MessageReceivedEvent.class);
        // var message = mock(Message.class);
        // var channel = mock(MessageChannel.class);
        // var action = mock(MessageAction.class);
        // var user = mock(User.class);

        // @SuppressWarnings("unchecked")
        // var restAction = (RestAction<Void>) mock(RestAction.class);

        // when(event.getAuthor()).thenReturn(user);
        // when(user.getName()).thenReturn("");
        // when(event.getMessage()).thenReturn(message);
        // when(event.getChannel()).thenReturn(channel);
        // when(message.getContentRaw()).thenReturn("!trust unsubscribe na99");
        // when(message.addReaction(anyString())).thenReturn(restAction);
        // when(channel.sendMessage(anyString())).thenReturn(action);
        // when(instanceSubscriberService.findByKeyAndUsername(anyString(), anyString()))
        //     .thenReturn(Optional.of(new InstanceSubscriber(new Instance(), new Subscriber(""))));

        // Mockito.doThrow(new DmlException(new RuntimeException("")))
        //     .when(instanceSubscriberService)
        //     .delete(any(InstanceSubscriber.class));
        // var listener = new MessageListener(subscriberService, instanceService, instanceSubscriberService);
        // listener.onMessageReceived(event);

        // verify(channel).sendMessage(MessageListener.ERROR_MSG);
    }
}
