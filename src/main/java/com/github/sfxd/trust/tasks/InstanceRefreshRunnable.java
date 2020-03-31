// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package com.github.sfxd.trust.tasks;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.services.InstanceService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;
import com.github.sfxd.trust.services.web.SalesforceTrustApiService;
import com.github.sfxd.trust.tasks.TaskManager.Task;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task that will check the Trust api instances and update their data in the
 * database.
 */
@ApplicationScoped
public class InstanceRefreshRunnable implements Task {
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

    @Override
    public void run() {
        LOGGER.info("Starting {}", InstanceRefreshRunnable.class.getName());

        Map<String, Instance> instancePreviews = this.trustApi.getInstancesStatusPreview()
            .stream()
            .collect(Collectors.toMap(Instance::getKey, Function.identity()));

        Map<String, Instance> instances = this.instanceService.findByKeyIn(instancePreviews.keySet())
            .findSteam()
            .collect(Collectors.toMap(Instance::getKey, Function.identity()));

        var forUpdate = new ArrayList<Instance>();
        var forInsert = new ArrayList<Instance>();
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

    @Override
    public long interval() {
        return 60L;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
