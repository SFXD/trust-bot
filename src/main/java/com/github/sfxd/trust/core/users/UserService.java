// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.users;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.EntityService;
import com.github.sfxd.trust.core.Repository;

/**
 * Service for interacting with the {@link User} model.
 */
@Singleton
public class UserService extends EntityService<User> {

    private final UserFinder repository;

    @Inject
    public UserService(UserFinder repository) {
        this.repository = repository;
    }

    public Optional<User> findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    protected Repository<User> repository() {
        return this.repository;
    }
}
