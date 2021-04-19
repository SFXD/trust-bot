package com.github.sfxd.trust.tasks;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.finders.InstanceFinder;
import com.github.sfxd.trust.model.services.InstanceService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;
import com.github.sfxd.trust.web.SalesforceTrustApiService;

import org.junit.jupiter.api.Test;

class InstanceRefreshTaskTests {

    @Test
    void it_should_update_the_table_with_the_new_data() throws DmlException {
        var trustApi = mock(SalesforceTrustApiService.class);
        var instanceService = mock(InstanceService.class);
        var instanceFinder = mock(InstanceFinder.class);
        var instances = new ArrayList<Instance>();
        var instance = new Instance();
        instance.setKey("NA99");
        var previews = List.of(instance);

        when(instanceFinder.findByKeyIn(anySet())).thenReturn(instances.stream());
        when(trustApi.getInstancesStatusPreview()).thenReturn(CompletableFuture.completedFuture(previews));

        Runnable r = new InstanceRefreshTask(trustApi, instanceService, instanceFinder);
        r.run();

        verify(instanceService).insert(previews);
        verify(instanceService).update(new ArrayList<Instance>());
    }
}
