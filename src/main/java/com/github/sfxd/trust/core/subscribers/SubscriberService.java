// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.subscribers;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sfxd.trust.core.EntityService;

/**
 * Service for interacting with the {@link Subscriber} model.
 */
@Singleton
public class SubscriberService extends EntityService<Subscriber> {

    @Inject
    public SubscriberService(SubscriberFinder repository) {
        super(repository);
    }

    public Optional<Subscriber> findByUsername(String username) {
        return ((SubscriberFinder) this.repository).findByUsername(username);
    }
}
