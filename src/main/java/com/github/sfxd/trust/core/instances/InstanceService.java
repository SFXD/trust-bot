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

package com.github.sfxd.trust.core.instances;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.AbstractEntityService;
import com.github.sfxd.trust.core.MessageService;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriber;
import com.github.sfxd.trust.core.instancesubscribers.InstanceSubscriberFinder;
import com.github.sfxd.trust.core.subscribers.Subscriber;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.ebean.Database;
import io.ebean.annotation.Transactional;

/**
 * Service for working with the {@link Instance} model.
 */
@Singleton
@Transactional
public class InstanceService extends AbstractEntityService<Instance> {

    private final InstanceSubscriberFinder instanceSubcriberFinder;
    private final InstanceFinder instanceFinder;
    private final MessageService messageService;

    @Inject
    public InstanceService(
        Database db,
        MessageService messageService,
        InstanceSubscriberFinder instanceSubscriberFinder,
        InstanceFinder instanceFinder
    ) {
        super(db, Instance.class);

        this.messageService = messageService;
        this.instanceSubcriberFinder = instanceSubscriberFinder;
        this.instanceFinder = instanceFinder;
    }

    /** {@inheritDoc} */
    @Override
    public List<Instance> update(List<Instance> entities) throws DmlException {
        Map<Long, Instance> instancesById = entities.stream()
            .collect(Collectors.toMap(Instance::getId, Function.identity()));

        Map<Long, DiffResult<Instance>> changes = this.instanceFinder.findByIdIn(instancesById.keySet())
            .map(old -> diff(old, instancesById.get(old.getId())))
            .filter(diff -> !diff.getDiffs().isEmpty())
            .collect(Collectors.toMap(diff -> diff.getLeft().getId(), Function.identity()));

        Map<Subscriber, List<InstanceSubscriber>> subscriptionsBySubscriber = this.instanceSubcriberFinder
            .findByInstanceIdIn(changes.keySet())
            .collect(Collectors.groupingBy(InstanceSubscriber::getSubscriber));

        for (Entry<Subscriber, List<InstanceSubscriber>> entry : subscriptionsBySubscriber.entrySet()) {
            var message = new StringBuilder();
            for (InstanceSubscriber is : entry.getValue()) {
                Instance instance = is.getInstance();
                message.append(instance.getKey() + System.lineSeparator());

                DiffResult<Instance> diff = changes.get(instance.getId());
                for (Diff<?> d : diff) {
                    message.append(String.format("  %s: %s -> %s%n", d.getFieldName(), d.getLeft(), d.getRight()));
                }
            }

            this.messageService.sendMessage(entry.getKey(), message.toString());
        }

        return super.update(entities);
    }

    private static DiffResult<Instance> diff(Instance old, Instance current) {
        return new DiffBuilder<>(old, current, ToStringStyle.DEFAULT_STYLE, false)
            .append("status", old.getStatus(), current.getStatus())
            .append("environment", old.getEnvironment(), current.getEnvironment())
            .append("location", old.getLocation(), current.getLocation())
            .append("releaseNumber", old.getReleaseNumber(), current.getReleaseNumber())
            .append("releaseVersion", old.getReleaseVersion(), current.getReleaseVersion())
            .build();
    }

    /** {@inheritDoc} */
    @Override
    public Instance update(Instance entity) throws DmlException {
        return this.update(List.of(entity)).get(0);
    }
}
