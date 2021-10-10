package com.github.sfxd.trust.core.instances;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import com.github.sfxd.trust.core.MessageService;
import com.github.sfxd.trust.core.instanceusers.InstanceUser;
import com.github.sfxd.trust.core.instanceusers.InstanceUserService;
import com.github.sfxd.trust.core.users.User;

import org.junit.jupiter.api.Test;

class InstanceServiceTests {

    @Test
    void on_update_it_should_send_a_message_to_all_subscribers_if_the_instance_is_not_ok() throws Exception {
        var messageService = mock(MessageService.class);
        var instanceSubscriberService = mock(InstanceUserService.class);
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

        var subscriber = new User("vips#7L");
        var instanceSubscribers = List.of(new InstanceUser(oldInstance, subscriber));

        when(instanceFinder.findByIdIn(anySet())).thenReturn(Stream.of(oldInstance));
        when(instanceSubscriberService.findByInstanceIdIn(anySet()))
            .thenReturn(instanceSubscribers.stream());

        var service = new InstanceService(messageService, instanceSubscriberService, instanceFinder);
        service.update(instances);

        verify(messageService).sendMessage(eq(subscriber), anyString());
    }
}
