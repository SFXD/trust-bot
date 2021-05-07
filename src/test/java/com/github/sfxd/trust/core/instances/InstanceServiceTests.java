package com.github.sfxd.trust.core.instances;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Consumer;

import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriber;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberFinder;
import com.github.sfxd.trust.core.subscribers.Subscriber;

import org.junit.jupiter.api.Test;

import io.ebean.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

class InstanceServiceTests {

    @Test
    @SuppressWarnings("unchecked")
    void on_update_it_should_send_a_message_to_all_subscribers_if_the_instance_is_not_ok() throws Exception {
        var db = mock(Database.class);
        var jda = mock(JDA.class);
        var instanceSubscriberFinder = mock(InstanceSubscriberFinder.class);
        var instanceFinder = mock(InstanceFinder.class);
        var action = mock(RestAction.class);
        var channelAction = mock(RestAction.class);
        var user = mock(User.class);
        var channel = mock(PrivateChannel.class);
        var messageAction = mock(MessageAction.class);

        var instance = new Instance();
        instance.setKey("NA99");
        instance.setId(1L);
        instance.setStatus("NOT_OK");
        List<Instance> instances = List.of(instance);

        var oldInstance = new Instance();
        oldInstance.setKey("NA99");
        oldInstance.setId(1L);
        oldInstance.setStatus(Instance.STATUS_OK);

        var subscriber = new Subscriber("vips#7L");
        var instanceSubscribers = List.of(new InstanceSubscriber(oldInstance, subscriber));

        when(instanceFinder.findByIdIn(anySet())).thenReturn(instances.stream());
        when(instanceSubscriberFinder.findByInstanceIdIn(anySet()))
            .thenReturn(instanceSubscribers.stream());
        when(jda.retrieveUserById(anyString())).thenReturn(action);
        doReturn(channelAction).when(user).openPrivateChannel();
        doAnswer(invocation -> {
            var cb = (Consumer<PrivateChannel>) invocation.getArgument(0);
            cb.accept(channel);
            return null;
        })
        .when(channelAction).queue(any(Consumer.class));

        doAnswer(invocation -> {
            var cb = (Consumer<User>) invocation.getArgument(0);
            cb.accept(user);
            return null;
        })
        .when(action).queue(any(Consumer.class));

        when(channel.sendMessage(anyString())).thenReturn(messageAction);

        var service = new InstanceService(db, jda, instanceSubscriberFinder, instanceFinder);
        service.update(instances);

        verify(channel).sendMessage(anyString());
    }
}
