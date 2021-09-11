// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instancesubscribers;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.EntityService;
import com.github.sfxd.trust.core.Repository;

/**
 * Service for working with the {@link InstanceSubcriber} model
 */
@Singleton
public class InstanceSubscriberService extends EntityService<InstanceSubscriber> {

    private final InstanceSubscriberRepository instanceSubscriberRepository;

    @Inject
    public InstanceSubscriberService(InstanceSubscriberRepository repository) {
        this.instanceSubscriberRepository = repository;
    }

    public Optional<InstanceSubscriber> findByInstanceIdAndSubscriberId(Long instanceId, Long subscriberId) {
        return this.instanceSubscriberRepository.findByInstanceIdAndSubscriberId(instanceId, subscriberId);
    }

    public Stream<InstanceSubscriber> findByInstanceIdIn(Collection<Long> instanceIds) {
        return this.instanceSubscriberRepository.findByInstanceIdIn(instanceIds);
    }

    public Optional<InstanceSubscriber> findByKeyAndUsername(String key, String username) {
        return this.instanceSubscriberRepository.findByKeyAndUsername(key, username);
    }

    protected Repository<InstanceSubscriber> repository() {
        return this.instanceSubscriberRepository;
    }
}
