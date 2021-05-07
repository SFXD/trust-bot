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
import java.util.Objects;
import java.util.Set;
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

        Set<Long> notOkIds = this.instanceFinder.findByIdIn(instancesById.keySet())
            .filter(old -> {
                Instance current = instancesById.get(old.getId());

                return old.getStatus().equals(Instance.STATUS_OK)
                    && !current.getStatus().equals(Instance.STATUS_OK);
            })
            .map(Instance::getId)
            .collect(Collectors.toSet());

        Map<Subscriber, List<InstanceSubscriber>> subscriptionsBySubscriber = this.instanceSubcriberFinder
            .findByInstanceIdIn(notOkIds)
            .collect(Collectors.groupingBy(InstanceSubscriber::getSubscriber));

        for (Entry<Subscriber, List<InstanceSubscriber>> entry : subscriptionsBySubscriber.entrySet()) {
            var message = new StringBuilder();
            for (InstanceSubscriber is : entry.getValue()) {
                Instance instance = is.getInstance();
                message.append(String.format(
                    "%s: %s%n",
                    instance.getKey(),
                    instancesById.get(instance.getId()).getStatus()
                ));
            }

            this.messageService.sendMessage(entry.getKey(), message.toString());
        }

        return super.update(entities);
    }

    /** {@inheritDoc} */
    @Override
    public Instance update(Instance entity) throws DmlException {
        return this.update(List.of(entity)).get(0);
    }
}
