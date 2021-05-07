package com.github.sfxd.trust.core.instances;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.github.sfxd.trust.core.MessageService;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriber;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberFinder;
import com.github.sfxd.trust.core.subscribers.Subscriber;

import org.junit.jupiter.api.Test;

import io.ebean.Database;

class InstanceServiceTests {

    @Test
    void on_update_it_should_send_a_message_to_all_subscribers_if_the_instance_is_not_ok() throws Exception {
        var db = mock(Database.class);
        var messageService = mock(MessageService.class);
        var instanceSubscriberFinder = mock(InstanceSubscriberFinder.class);
        var instanceFinder = mock(InstanceFinder.class);

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

        var service = new InstanceService(db, messageService, instanceSubscriberFinder, instanceFinder);
        service.update(instances);

        verify(messageService).sendMessage(eq(subscriber), anyString());
    }
}
