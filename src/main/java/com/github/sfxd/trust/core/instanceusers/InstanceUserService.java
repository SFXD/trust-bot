// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instanceusers;

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
public class InstanceUserService extends EntityService<InstanceUser> {

    private final InstanceUserRepository instanceUserRepository;

    @Inject
    public InstanceUserService(InstanceUserRepository repository) {
        this.instanceUserRepository = repository;
    }

    public Optional<InstanceUser> findByInstanceIdAndUserId(Long instanceId, Long userId) {
        return this.instanceUserRepository.findByInstanceIdAndUserId(instanceId, userId);
    }

    public Stream<InstanceUser> findByInstanceIdIn(Collection<Long> instanceIds) {
        return this.instanceUserRepository.findByInstanceIdIn(instanceIds);
    }

    public Optional<InstanceUser> findByKeyAndUsername(String key, String username) {
        return this.instanceUserRepository.findByKeyAndUsername(key, username);
    }

    protected Repository<InstanceUser> repository() {
        return this.instanceUserRepository;
    }
}
