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

package com.github.sfxd.trust.model.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.finders.InstanceFinder;
import com.github.sfxd.trust.model.finders.InstanceSubscriberFinder;

import io.ebean.Database;
import io.ebean.annotation.Transactional;
import net.dv8tion.jda.api.JDA;

/**
 * Service for working with the {@link Instance} model.
 */
@Singleton
@Transactional
public class InstanceService extends AbstractEntityService<Instance> {

    private final JDA jda;
    private final InstanceSubscriberFinder instanceSubcriberFinder;
    private final InstanceFinder instanceFinder;

    @Inject
    public InstanceService(
        Database db,
        JDA jda,
        InstanceSubscriberFinder instanceSubscriberFinder,
        InstanceFinder instanceFinder
    ) {
        super(db, Instance.class);

        Objects.requireNonNull(jda);
        Objects.requireNonNull(instanceSubscriberFinder);
        Objects.requireNonNull(instanceFinder);

        this.jda = jda;
        this.instanceSubcriberFinder = instanceSubscriberFinder;
        this.instanceFinder = instanceFinder;
    }

    /** {@inheritDoc} */
    @Override
    public List<Instance> update(List<Instance> entities) throws DmlException {
        Map<Long, Instance> instancesById = entities.stream()
            .collect(Collectors.toMap(Instance::getId, Function.identity()));

        Set<Long> notOkIds = this.instanceFinder.findByIdIn(instancesById.keySet())
            .findSteam()
            .filter(old -> {
                Instance current = instancesById.get(old.getId());

                return old.getStatus().equals(Instance.STATUS_OK)
                    && !current.getStatus().equals(Instance.STATUS_OK);
            })
            .map(Instance::getId)
            .collect(Collectors.toSet());

        Map<String, List<InstanceSubscriber>> subscriptionsBySubscriber = this.instanceSubcriberFinder
            .findByInstanceIdIn(notOkIds)
            .findSteam()
            .collect(Collectors.groupingBy(is -> is.getSubscriber().getUsername()));

        for (Entry<String, List<InstanceSubscriber>> entry : subscriptionsBySubscriber.entrySet()) {
            var message = new StringBuilder();
            for (InstanceSubscriber is : entry.getValue()) {
                Instance instance = is.getInstance();
                message.append(String.format(
                    "%s: %s%n",
                    instance.getKey(),
                    instancesById.get(instance.getId()).getStatus()
                ));
            }

            this.jda.retrieveUserById(entry.getKey()).queue(user -> {
                user.openPrivateChannel().queue(channel -> channel.sendMessage(message.toString()).queue());
            });
        }

        return super.update(entities);
    }

    /** {@inheritDoc} */
    @Override
    public Instance update(Instance entity) throws DmlException {
        return this.update(List.of(entity)).get(0);
    }
}
