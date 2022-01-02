// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instances;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.EntityService;
import com.github.sfxd.trust.core.MessageService;
import com.github.sfxd.trust.core.Repository;
import com.github.sfxd.trust.core.instanceusers.InstanceUser;
import com.github.sfxd.trust.core.instanceusers.InstanceUserService;
import com.github.sfxd.trust.core.users.User;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.ebean.annotation.Transactional;

/**
 * Service for working with the {@link Instance} model.
 */
@Singleton
public class InstanceService extends EntityService<Instance> {

    private final InstanceUserService instanceUserService;
    private final InstanceFinder instanceFinder;
    private final MessageService messageService;

    @Inject
    public InstanceService(
        MessageService messageService,
        InstanceUserService instanceUserService,
        InstanceFinder instanceFinder
    ) {
        this.messageService = messageService;
        this.instanceUserService = instanceUserService;
        this.instanceFinder = instanceFinder;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void update(Collection<Instance> entities) {
        Map<Long, Instance> instancesById = entities.stream()
            .collect(Collectors.toMap(Instance::getId, Function.identity()));

        Map<Long, DiffResult<Instance>> changes = this.instanceFinder.findByIdIn(instancesById.keySet())
            .map(old -> diff(old, instancesById.get(old.getId())))
            .filter(diff -> !diff.getDiffs().isEmpty())
            .collect(Collectors.toMap(diff -> diff.getLeft().getId(), Function.identity()));

        Map<User, List<InstanceUser>> subscriptions = this.instanceUserService
            .findByInstanceIdIn(changes.keySet())
            .collect(Collectors.groupingBy(InstanceUser::getUser));

        for (Entry<User, List<InstanceUser>> entry : subscriptions.entrySet()) {
            var message = new StringBuilder();
            for (InstanceUser is : entry.getValue()) {
                Instance instance = is.getInstance();
                message.append(instance.getKey() + System.lineSeparator());

                DiffResult<Instance> diff = changes.get(instance.getId());
                for (Diff<?> d : diff) {
                    message.append(String.format("  %s: %s -> %s%n", d.getFieldName(), d.getLeft(), d.getRight()));
                }
            }

            this.messageService.sendMessage(entry.getKey(), message.toString());
        }
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

    public Optional<Instance> findByKey(String key) {
        return this.instanceFinder.findByKey(key);
    }

    public Stream<Instance> findByKeyIn(Collection<String> keys) {
        return this.instanceFinder.findByKeyIn(keys);
    }

    protected Repository<Instance> repository() {
        return this.instanceFinder;
    }
}
