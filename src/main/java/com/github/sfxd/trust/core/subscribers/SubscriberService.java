// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.subscribers;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.EntityService;
import com.github.sfxd.trust.core.Repository;

/**
 * Service for interacting with the {@link Subscriber} model.
 */
@Singleton
public class SubscriberService extends EntityService<Subscriber> {

    private final SubscriberFinder repository;

    @Inject
    public SubscriberService(SubscriberFinder repository) {
        this.repository = repository;
    }

    public Optional<Subscriber> findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    protected Repository<Subscriber> repository() {
        return this.repository;
    }
}
