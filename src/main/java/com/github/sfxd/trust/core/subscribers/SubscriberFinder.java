// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.subscribers;

import java.util.Optional;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;

@Singleton
class SubscriberFinder extends Repository<Subscriber> {

    SubscriberFinder() {
        super(Subscriber.class);
    }

    /**
     * Finds a subscriber by their unique username.
     *
     * @param username the user you want to find
     * @return An Optional containing the subscriber or empty if hibernate throws
     *         NoResultException
     */
    public Optional<Subscriber> findByUsername(String username) {
        return this.query()
            .where()
            .eq("username", username)
            .findOneOrEmpty();
    }
}
