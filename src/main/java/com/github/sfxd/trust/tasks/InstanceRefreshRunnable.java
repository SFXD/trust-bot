package com.github.sfxd.trust.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.services.InstanceService;
import com.github.sfxd.trust.services.AbstractEntityService.DmlException;
import com.github.sfxd.trust.services.web.SalesforceTrustApiService;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.scheduler.Scheduled;

/**
 * Task that will check the Trust api instances and update their data in the
 * database.
 */
@ApplicationScoped
public class InstanceRefreshRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceRefreshRunnable.class);

    @Inject
    @RestClient
    private SalesforceTrustApiService trustApi;
    private final InstanceService instanceService;

    public InstanceRefreshRunnable(SalesforceTrustApiService trustApi, InstanceService instanceService) {
        Objects.requireNonNull(trustApi);
        Objects.requireNonNull(instanceService);

        this.trustApi = trustApi;
        this.instanceService = instanceService;
    }

    @Inject
    InstanceRefreshRunnable(InstanceService instanceService) {
        Objects.requireNonNull(instanceService);

        this.instanceService = instanceService;
    }

    @Scheduled(every = "60s")
    @Override
    public void run() {
        Map<String, Instance> instancePreviews = this.trustApi.getInstancesStatusPreview()
            .stream()
            .collect(Collectors.toMap(Instance::getKey, Function.identity()));

        Map<String, Instance> instances = this.instanceService.findByKeyIn(instancePreviews.keySet())
            .stream()
            .collect(Collectors.toMap(Instance::getKey, Function.identity()));

        List<Instance> forUpdate = new ArrayList<>();
        List<Instance> forInsert = new ArrayList<>();
        for (Instance preview : instancePreviews.values()) {
            Instance current = instances.get(preview.getKey());
            if (current != null) {
                preview.setId(current.getId());
                forUpdate.add(preview);
            } else {
                forInsert.add(preview);
            }
        }

        try {
            this.instanceService.update(forUpdate);
            this.instanceService.insert(forInsert);
        } catch (DmlException ex) {
            LOGGER.error("Failed to update instances.", ex);
        }
    }
}
