package com.github.sfxd.trust.tasks;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.query.QInstance;
import com.github.sfxd.trust.model.services.InstanceService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;
import com.github.sfxd.trust.services.web.SalesforceTrustApiService;

import org.junit.jupiter.api.Test;

class InstanceRefreshRunnableTests {

    @Test
    void it_should_update_the_table_with_the_new_data() throws DmlException {
        var trustApi = mock(SalesforceTrustApiService.class);
        var instanceService = mock(InstanceService.class);
        var instances = new ArrayList<Instance>();
        var instance = new Instance();
        instance.setKey("NA99");
        var previews = List.of(instance);
        var qinstance = mock(QInstance.class);

        when(instanceService.findByKeyIn(anySet())).thenReturn(qinstance);
        doReturn(instances).when(qinstance).findSteam();
        when(trustApi.getInstancesStatusPreview()).thenReturn(previews);

        Runnable r = new InstanceRefreshRunnable(trustApi, instanceService);
        r.run();

        verify(instanceService).insert(previews);
        verify(instanceService).update(new ArrayList<Instance>());
    }
}
