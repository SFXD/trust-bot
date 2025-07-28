package com.github.sfxd.trust.integrations;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.github.sfxd.trust.core.Messages;
import com.github.sfxd.trust.core.instances.Instance;

import com.github.sfxd.trust.core.instances.InstanceRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class InstanceRefreshConsumerTests {

    @Test
    @Disabled
    void it_should_update_the_table_with_the_new_data() {
        var instanceRepo = mock(InstanceRepository.class);
        var instances = new ArrayList<Instance>();
        var previews = List.of(new InstancePreviewViewModel("NA99", "us-west", "1.02", "14", "down", "sandbox"));

        when(instanceRepo.findByKeyIn(anySet())).thenReturn(instances);

        var consumer = new InstanceRefreshConsumer(instanceRepo, mock(Messages.class));
        consumer.accept(previews);
    }
}
