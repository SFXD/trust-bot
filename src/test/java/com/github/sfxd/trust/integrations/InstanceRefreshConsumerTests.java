package com.github.sfxd.trust.integrations;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.github.sfxd.trust.core.AbstractEntityService.DmlException;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceFinder;
import com.github.sfxd.trust.core.instances.InstanceService;

import org.junit.jupiter.api.Test;

class InstanceRefreshConsumerTests {

    @Test
    void it_should_update_the_table_with_the_new_data() throws DmlException {
        var instanceService = mock(InstanceService.class);
        var instanceFinder = mock(InstanceFinder.class);
        var instances = new ArrayList<Instance>();
        var instance = new Instance();
        instance.setKey("NA99");
        var previews = List.of(instance);

        when(instanceFinder.findByKeyIn(anySet())).thenReturn(instances.stream());

        var consumer = new InstanceRefreshConsumer(instanceService, instanceFinder);
        consumer.accept(previews);

        verify(instanceService).insert(previews);
        verify(instanceService).update(new ArrayList<Instance>());
    }
}
