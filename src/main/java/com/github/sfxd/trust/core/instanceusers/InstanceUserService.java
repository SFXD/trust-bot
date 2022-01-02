// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instanceusers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.Entity;
import com.github.sfxd.trust.core.EntityService;
import com.github.sfxd.trust.core.Repository;
import com.github.sfxd.trust.core.users.User;
import com.github.sfxd.trust.core.users.UserService;

import io.ebean.annotation.Transactional;

/**
 * Service for working with the {@link InstanceSubscriber} model
 */
@Singleton
public class InstanceUserService extends EntityService<InstanceUser> {

    private final InstanceUserRepository instanceUserRepository;
    private final UserService userService;

    @Inject
    public InstanceUserService(InstanceUserRepository repository, UserService userService) {
        this.instanceUserRepository = repository;
        this.userService = userService;
    }

    /**
     * Insert new InstanceUsers. The user objects can be new users and those will
     * be persisted as well.
     */
    @Override
    @Transactional
    public void insert(Collection<InstanceUser> instanceUsers) {
        List<User> newUsers = instanceUsers.stream()
            .map(InstanceUser::getUser)
            .filter(Entity::isNew)
            .toList();

        this.userService.insert(newUsers);
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
