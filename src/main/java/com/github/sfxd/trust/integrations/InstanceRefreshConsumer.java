// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceService;

@ApplicationScoped
class InstanceRefreshConsumer implements Consumer<Collection<Instance>> {

    private final InstanceService instanceService;

    @Inject
    InstanceRefreshConsumer(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @Override
    public void accept(Collection<Instance> incomingInstances) {
        Map<String, Instance> instancePreviews = incomingInstances.stream()
            .collect(Collectors.toMap(Instance::getKey, Function.identity()));

        Map<String, Instance> instances = this.instanceService.findByKeyIn(instancePreviews.keySet())
            .collect(Collectors.toMap(Instance::getKey, Function.identity()));

        var forUpdate = new ArrayList<Instance>();
        var forInsert = new ArrayList<Instance>();
        for (Instance preview : instancePreviews.values()) {
            Instance current = instances.get(preview.getKey());
            if (current != null) {
                current
                    .setLocation(preview.getLocation())
                    .setReleaseVersion(preview.getReleaseVersion())
                    .setReleaseNumber(preview.getReleaseNumber())
                    .setStatus(preview.getStatus())
                    .setEnvironment(preview.getEnvironment());

                forUpdate.add(current);
            } else {
                forInsert.add(preview);
            }
        }

        this.instanceService.update(forUpdate);
        this.instanceService.insert(forInsert);
    }
}
