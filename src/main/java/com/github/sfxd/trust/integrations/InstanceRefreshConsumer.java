// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2021 George Doenlen

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

package com.github.sfxd.trust.integrations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.AbstractEntityService.DmlException;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceFinder;
import com.github.sfxd.trust.core.instances.InstanceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
class InstanceRefreshConsumer implements Consumer<Collection<Instance>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceRefreshConsumer.class);

    private final InstanceService instanceService;
    private final InstanceFinder instanceFinder;

    @Inject
    InstanceRefreshConsumer(InstanceService instanceService, InstanceFinder instanceFinder) {
        this.instanceService = instanceService;
        this.instanceFinder = instanceFinder;
    }

    @Override
    public void accept(Collection<Instance> incomingInstances) {
        Map<String, Instance> instancePreviews = incomingInstances.stream()
            .collect(Collectors.toMap(Instance::getKey, Function.identity()));

        Map<String, Instance> instances = this.instanceFinder.findByKeyIn(instancePreviews.keySet())
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

        try {
            this.instanceService.update(forUpdate);
            this.instanceService.insert(forInsert);
        } catch (DmlException ex) {
            LOGGER.error("Failed to update instances.", ex);
        }
    }
}
